package com.sergio.proyectov1.core;

import android.app.Activity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sergio.proyectov1.api.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Muestra los avisos sin leer (tabla notificacion) al abrir la app:
 * receta aprobada / rechazada para el autor, imágenes por revisar para el administrador.
 * Al cerrarlos se marcan como leídos.
 */
public final class NotificationsChecker {

    private NotificationsChecker() {}

    private static boolean showing = false;

    public static void check(Activity activity) {
        int userId = ApiHelper.userId(activity);
        if (userId <= 0 || showing) return;
        ApiHelper.get(activity, "notificaciones.php?id_usuario=" + userId, (ok, message, body) -> {
            if (!ok || body == null || activity.isFinishing() || activity.isDestroyed()) return;
            try {
                JSONArray array = new JSONArray(body);
                if (array.length() == 0) return;

                StringBuilder text = new StringBuilder();
                StringBuilder ids = new StringBuilder();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject n = array.getJSONObject(i);
                    if (text.length() > 0) text.append("\n\n");
                    text.append("• ").append(n.optString("titulo")).append("\n").append(n.optString("mensaje"));
                    if (ids.length() > 0) ids.append(",");
                    ids.append(n.optInt("id_notificacion"));
                }

                showing = true;
                new MaterialAlertDialogBuilder(activity)
                        .setTitle(array.length() == 1 ? "Tienes un aviso" : "Tienes " + array.length() + " avisos")
                        .setMessage(text)
                        .setPositiveButton("Entendido", null)
                        .setOnDismissListener(d -> {
                            showing = false;
                            markRead(activity, userId, ids.toString());
                        })
                        .show();
            } catch (Exception ignored) {
            }
        });
    }

    private static void markRead(Activity activity, int userId, String ids) {
        Map<String, String> p = new HashMap<>();
        p.put("id_usuario", String.valueOf(userId));
        p.put("accion", "leidas");
        p.put("ids", ids);
        ApiHelper.post(activity, "notificaciones.php", p, (ok, message, body) -> { });
    }
}
