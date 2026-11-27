# ProyectoV1 - Organización

## Paquetes Java

- `auth/` — Login, registro y recuperación.
- `home/` — Inicio (con buscador y filtros) y plan.
- `recipes/` — Crear receta y detalle de receta.
- `pantry/` — Alacena y lista de compras.
- `plan/` — Almacenamiento del plan alimenticio.
- `personalization/` — Perfil y personalización del usuario.
- `adapters/` — Adaptadores de listas.
- `api/nutrition/` — Cliente, servicio y repositorio de la API nutricional.
- `models/` — Modelos de datos.
- `viewmodels/` — ViewModels.
- `core/` — Componentes centrales, actualmente `MainActivity`.

Los recursos Android permanecen en `res/layout`, `res/menu`, etc. para respetar la estructura de recursos de Android.

La reorganización está pensada para mejorar la localización de archivos sin cambiar la lógica funcional existente.
