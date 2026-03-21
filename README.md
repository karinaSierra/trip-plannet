# TripPlanner

Aplicación Android nativa para **planificar viajes**: registro e inicio de sesión local, gestión de viajes (CRUD) y detalle con acciones contextuales. Desarrollada en **Java** con arquitectura **MVVM** y persistencia **Room**.

---

## Requisitos

- **Android Studio** (recomendado: última versión estable)
- **JDK 17** (según `app/build.gradle`)
- **minSdk 23** · **targetSdk 35** · **compileSdk 35**

---

## Cómo abrir y compilar

1. Abre la carpeta del proyecto en Android Studio (`File → Open`).
2. Espera a que termine **Sync Project with Gradle Files**.
3. Ejecuta la app: **Run → Run 'app'** (o el botón verde) en un emulador o dispositivo físico.

Si hay errores de compilación:

- **Build → Clean Project** y luego **Build → Rebuild Project**.
- Comprueba que `JAVA_HOME` apunte a la raíz del JDK (no a la carpeta `bin`).

---

## Arquitectura

| Capa | Descripción |
|------|-------------|
| **UI** | Activities (`ui/…`), layouts XML, `RecyclerView` + adaptadores |
| **ViewModel** | Lógica de presentación y estado (`viewmodel/…`) |
| **Data** | Room: entidades, DAOs, `AppDatabase`; repositorios que delegan en DAOs |
| **Sesión** | `SessionManager` (SharedPreferences): usuario logueado |

No hay backend remoto: todo es **local** en el dispositivo.

---

## Tecnologías principales

- **Java**
- **AndroidX** (AppCompat, Material, RecyclerView, CardView, CoordinatorLayout)
- **Room** (base de datos SQLite)
- **Lifecycle** (ViewModel, LiveData donde aplica en auth)
- **Material Components** (botones redondeados, FAB, temas)

---

## Flujo de pantallas

1. **Splash** → logo y redirección a login o lista según sesión.
2. **Login / Registro** → validación mínima y datos en Room.
3. **Lista de viajes** → RecyclerView, FAB para nuevo viaje, cerrar sesión con confirmación.
4. **Detalle de viaje** → datos del viaje; editar, eliminar (con diálogo) o volver.
5. **Formulario de viaje** → crear o editar viaje; guardar o volver.

La sesión se guarda con `SessionManager` tras un login correcto. Al cerrar sesión se limpia la sesión y se navega al login sin poder volver atrás a la lista (flags de intent).

---

## Estructura de paquetes (resumen)

```
com.example.tripplanner
├── TripPlannerApp.java
├── data
│   ├── local
│   │   ├── dao/          # UserDao, TripDao, ItemDao
│   │   ├── db/           # AppDatabase
│   │   └── entity/       # UserEntity, TripEntity, ItemEntity
│   ├── repository/       # UserRepository, TripRepository, ItemRepository
│   └── session/          # SessionManager
├── ui
│   ├── base/             # BaseActivity
│   ├── splash/
│   ├── login/
│   ├── register/
│   ├── triplist/         # TripListActivity, TripAdapter
│   ├── tripdetail/
│   ├── tripform/
│   └── itemform/
└── viewmodel/            # ViewModels y factories
```

---

## Base de datos (Room)

- **Nombre:** `tripplanner_db`
- **Singleton:** `AppDatabase.getInstance(Context)`
- **Migraciones:** `fallbackToDestructiveMigration()` (desarrollo; en producción conviene migraciones reales)
- **Consultas en hilo principal:** `allowMainThreadQueries()` (simplifica el código; en producción suele usarse Executor/coroutines)

Las fechas de viaje se guardan como **`long`** (milisegundos), patrón habitual en Room/SQLite.

---

## Paleta de colores

Definida en `res/values/colors.xml`:

- **Primario (marca):** teal `#0F766E` y variantes
- **Secundario (acento):** naranja `#F97316` y variantes
- **Neutros:** fondo claro, texto principal/secundario, bordes

Tema: `Theme.TripPlanner` (`themes.xml`).

---

## Repositorio remoto (opcional)

Si usas Git, el remoto puede ser algo como:

```bash
git remote add origin https://github.com/<usuario>/<repo>.git
git branch -M main
git push -u origin main
```

---

## Licencia

Proyecto educativo / personal. Ajusta la licencia según tu caso.

---

## Autor

TripPlanner — planificación de viajes en Android.
