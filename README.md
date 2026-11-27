# Recetas App

Aplicación Android de recetas (Java) con un API en PHP + MySQL (XAMPP).

```
ProyectoV2/    App Android (abrir esta carpeta en Android Studio)
recetas_api/   API PHP que usa la app (va dentro de htdocs de XAMPP)
  sql/         Scripts de la base de datos
```

## 1. Base de datos

En phpMyAdmin (o con la consola de MySQL), ejecutar en este orden:

1. `recetas_api/sql/00_esquema.sql`: crea `db_recetas` con todas las tablas, la función `unidad_permitida` y los triggers.
2. `recetas_api/sql/00_catalogos.sql`: ingredientes, especias, utensilios y medidas por alimento.

Los scripts `01_` a `07_` son las migraciones que se aplicaron a una BD ya existente; no hace falta ejecutarlos si se usan los `00_`. Si ya tienes la BD de antes, ejecuta solo los que te falten, en orden.

Las imágenes de las recetas se guardan en `recetas_api/uploads/` (no se suben a Git; esa carpeta no ejecuta código).

Para sacar un respaldo completo usa `--routines`; si no, la función de unidades no se copia y los triggers fallan:

```
C:\xampp\mysql\bin\mysqldump.exe -uroot --routines --triggers db_recetas > respaldo.sql
```

## 2. API (XAMPP)

`recetas_api` tiene que quedar dentro de `C:\xampp\htdocs`. Para trabajar sobre la carpeta del repo sin copiarla, crea un enlace (PowerShell):

```
New-Item -ItemType Junction -Path C:\xampp\htdocs\recetas_api -Target <ruta-del-repo>\recetas_api
```

La conexión está en `recetas_api/conexion.php` (por defecto `root` sin contraseña).

## 3. App Android

Agrega estas líneas a `ProyectoV2/local.properties` (ese archivo no se sube a Git):

```
API_BASE_URL=http://TU_IP_LOCAL/recetas_api/
USDA_API_KEY=tu_clave_de_api.data.gov
```

- Desde el emulador usa `http://10.0.2.2/recetas_api/` (es el valor por defecto).
- Desde un celular físico usa la IP de tu computadora en la red (`ipconfig`).
- La clave de USDA se obtiene gratis en https://api.data.gov/signup/. Si no la pones, se usa `DEMO_KEY`, que tiene un límite de consultas muy bajo.

## Moderación de imágenes (Google Cloud Vision SafeSearch)

Cada imagen que se sube pasa por SafeSearch **antes** de guardarse:

| Resultado | Regla | Qué pasa |
|---|---|---|
| Aprobada | todas las categorías en `VERY_UNLIKELY` o `UNLIKELY` | se guarda y la receta se publica |
| Revisión | cualquier otro caso (p. ej. alguna categoría en `POSSIBLE`, `racy` en `LIKELY`) | se guarda; la receta queda "En revisión" y un administrador decide en la pestaña **Imágenes** |
| Rechazada | `adult` o `violence` en `LIKELY` o `VERY_LIKELY` | no se guarda; se le pide al usuario otra imagen |

Para activarla:

1. En https://console.cloud.google.com crea un proyecto y habilita **Cloud Vision API**. Google pide una cuenta de facturación, aunque el uso bajo de SafeSearch entra en su capa gratuita mensual.
2. En *APIs y servicios → Credenciales* crea una **clave de API** y restríngela a *Cloud Vision API*.
3. Copia `recetas_api/config.example.php` como `recetas_api/config.local.php` y pega la clave en `VISION_API_KEY`. Ese archivo no se sube a Git.

Sin clave, todas las imágenes van a revisión del administrador; nunca se aprueban solas.

## Medidas por alimento

Las unidades que acepta cada alimento están en la BD (`unidad_medida`, `ingrediente_unidad`, `especia_unidad`), no en el código. Para permitir una unidad nueva basta con insertar una fila, por ejemplo:

```sql
INSERT INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
SELECT i.id_ingrediente, u.id_unidad, 'receta', 4
FROM ingrediente i JOIN unidad_medida u ON u.nombre = 'Tazas'
WHERE i.nombre = 'Jitomate';
```
