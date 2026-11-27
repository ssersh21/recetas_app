package com.sergio.proyectov1.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Llamadas sencillas a recetas_api.
 * Todas las respuestas llegan en el hilo principal.
 */
public final class ApiHelper {

    private ApiHelper() {}

    public interface Callback {
        /**
         * @param ok      true si el servidor respondió 2xx (y success != false)
         * @param message mensaje para el usuario ("message" o "mensaje" del JSON)
         * @param body    cuerpo de la respuesta (JSON) o null si no hubo conexión
         */
        void onResult(boolean ok, @NonNull String message, @Nullable String body);
    }

    private static final String NO_CONNECTION = "No hay conexión con el servidor";
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final ExecutorService IO = Executors.newSingleThreadExecutor();
    private static OkHttpClient okHttp;
    private static ImageLoader imageLoader;

    public static int userId(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("user_id", 0);
    }

    // ------------------------------------------------------------------ GET / POST

    public static void get(Context context, String pathAndQuery, Callback callback) {
        StringRequest req = new StringRequest(Request.Method.GET, VolleySingleton.BASE_URL + pathAndQuery,
                body -> deliver(body, true, callback),
                error -> deliverError(error, callback));
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }

    public static void post(Context context, String path, Map<String, String> params, Callback callback) {
        StringRequest req = new StringRequest(Request.Method.POST, VolleySingleton.BASE_URL + path,
                body -> deliver(body, true, callback),
                error -> deliverError(error, callback)) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }

    public static void postJson(Context context, String path, JSONObject json, Callback callback) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, VolleySingleton.BASE_URL + path, json,
                response -> deliver(response.toString(), true, callback),
                error -> deliverError(error, callback));
        // Crear/editar una receta puede tardar un poco más
        req.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(20000, 0, 1f));
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }

    private static void deliverError(VolleyError error, Callback callback) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            deliver(new String(error.networkResponse.data, StandardCharsets.UTF_8), false, callback);
        } else {
            callback.onResult(false, NO_CONNECTION, null);
        }
    }

    private static void deliver(String body, boolean httpOk, Callback callback) {
        String message = "";
        boolean ok = httpOk;
        String trimmed = body == null ? "" : body.trim();
        if (trimmed.startsWith("{")) {
            try {
                JSONObject json = new JSONObject(trimmed);
                message = json.optString("message", json.optString("mensaje", ""));
                if (json.has("success")) ok = ok && json.optBoolean("success", false);
            } catch (Exception ignored) {
                ok = false;
                message = "Respuesta no válida del servidor";
            }
        }
        if (!ok && message.isEmpty()) message = "Ocurrió un error en el servidor";
        callback.onResult(ok, message, trimmed);
    }

    // ------------------------------------------------------------------ Imágenes

    /**
     * Sube una imagen elegida con el Photo Picker a subir_imagen.php.
     * En el callback, body es la ruta devuelta (ej. "uploads/receta_xxx.jpg");
     * si falla, body es "rechazada" cuando SafeSearch no la aceptó.
     */
    public static void uploadImage(Context context, Uri uri, Callback callback) {
        Context app = context.getApplicationContext();
        int userId = userId(app);
        IO.execute(() -> {
            try {
                String mime = app.getContentResolver().getType(uri);
                if (mime == null) mime = "image/jpeg";
                byte[] bytes;
                try (InputStream in = app.getContentResolver().openInputStream(uri)) {
                    if (in == null) throw new java.io.IOException("No se pudo leer la imagen");
                    bytes = readAll(in);
                }
                if (bytes.length > 5 * 1024 * 1024) {
                    post(callback, false, "La imagen pesa demasiado (máximo 5 MB)", null);
                    return;
                }
                RequestBody file = RequestBody.create(bytes, MediaType.parse(mime));
                MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id_usuario", String.valueOf(userId))
                        .addFormDataPart("imagen", "receta", file)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(VolleySingleton.BASE_URL + "subir_imagen.php")
                        .post(body)
                        .build();
                try (Response response = client().newCall(request).execute()) {
                    String text = response.body() != null ? response.body().string() : "";
                    JSONObject json = new JSONObject(text);
                    boolean ok = response.isSuccessful() && json.optBoolean("success", false);
                    post(callback, ok, json.optString("message", ok ? "" : "No se pudo subir la imagen"),
                            // Si falla, body indica si la moderación la rechazó ("rechazada")
                            ok ? json.optString("imagen", "") : json.optString("moderacion", ""));
                }
            } catch (Exception e) {
                post(callback, false, "No se pudo subir la imagen: revisa tu conexión", null);
            }
        });
    }

    /** URL completa de una imagen guardada en el servidor ("uploads/..."). */
    @Nullable
    public static String imageUrl(@Nullable String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) return null;
        if (relativePath.startsWith("http")) return relativePath;
        return VolleySingleton.BASE_URL + relativePath;
    }

    /** Carga la imagen de una receta en el ImageView (con caché). Si no hay imagen deja el ícono. */
    public static void loadImage(ImageView view, @Nullable String relativePath, int placeholderRes) {
        String url = imageUrl(relativePath);
        view.setTag(url);
        view.setImageResource(placeholderRes);
        if (url == null) return;
        imageLoader(view.getContext()).get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer container, boolean isImmediate) {
                // La vista pudo reciclarse para otra receta mientras llegaba la imagen
                if (container.getBitmap() != null && url.equals(view.getTag())) {
                    view.setImageBitmap(container.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                // Se queda el ícono por defecto
            }
        }, 800, 800);
    }

    // ------------------------------------------------------------------ Internos

    private static synchronized ImageLoader imageLoader(Context context) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(VolleySingleton.getInstance(context).getRequestQueue(),
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                            @Override
                            protected int sizeOf(String key, Bitmap value) {
                                return value.getByteCount();
                            }
                        };

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }
        return imageLoader;
    }

    private static synchronized OkHttpClient client() {
        if (okHttp == null) {
            okHttp = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return okHttp;
    }

    private static byte[] readAll(InputStream in) throws java.io.IOException {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[16 * 1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
            if (out.size() > 6 * 1024 * 1024) break; // más de 5 MB: el servidor la rechazaría
        }
        return out.toByteArray();
    }

    private static void post(Callback callback, boolean ok, String message, @Nullable String body) {
        MAIN.post(() -> callback.onResult(ok, message, body));
    }
}
