# TripPlanner

Aplicación Android nativa para **planificar viajes**: registro e inicio de sesión local, crear, editar y eliminar viajes, detalle con checklist de tareas (tabla `items`) y acciones Editar, Eliminar o Volver. Las contraseñas se guardan **hasheadas** (PBKDF2), no en texto plano. Desarrollada en **Java** con arquitectura **MVVM** y persistencia local con **Room**.

---

## Requisitos

- **Android Studio** (recomendado: última versión estable)
- **JDK 21** (según `app/build.gradle`)
- **minSdk 23** · **targetSdk 35** · **compileSdk 35**

---

## Versión de Java usada

El proyecto está configurado con **Java 21**:

- `sourceCompatibility JavaVersion.VERSION_21`
- `targetCompatibility JavaVersion.VERSION_21`

### Notas de compatibilidad para Java 21

- Java 21 es una configuración estable para este proyecto con AGP 8.x.
- Puedes compilar con un JDK 21 instalado en tu sistema o con JBR 21 de Android Studio.
- Si aparece un error tipo `invalid source release`, revisa que el Gradle JDK seleccionado sea 21.

### Uso recomendado en tu entorno

1. Instala **JDK 21** en tu sistema.
2. Configura `JAVA_HOME` apuntando a la raíz del JDK (no a `bin`).
3. En Android Studio, selecciona un **Gradle JDK** compatible (21 recomendado):
   - `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`.

---

## Cómo abrir y compilar

1. Abre la carpeta del proyecto en Android Studio (`File → Open`).
2. Espera a que termine **Sync Project with Gradle Files**.
3. Ejecuta la app: **Run → Run 'app'** (o el botón verde) en un emulador o dispositivo físico.

Si hay errores de compilación:

- **Build → Clean Project** y luego **Build → Rebuild Project**.
- Comprueba que `JAVA_HOME` apunte a la **raíz del JDK** (por ejemplo `...\jdk-21`, no `...\jdk-21\bin`). Si apunta a `bin`, Gradle fallará al arrancar.
- Usa un **JDK 21** coherente con `compileOptions` del módulo `app`.

### Compilar y tests desde terminal

| Objetivo | Comando (Windows) |
|----------|-------------------|
| Compilar APK debug | `gradlew.bat assembleDebug` |
| Tests unitarios (debug) | `gradlew.bat testDebugUnitTest` |
| Alias `testClasses` | `gradlew.bat :app:testClasses` (redirige a `testDebugUnitTest`) |

En proyectos Android **no** existe la tarea estándar `testClasses` del plugin `java`; este proyecto define un **alias** en `app/build.gradle` para que herramientas que la invoquen no fallen.

---

## Arquitectura

| Capa | Descripción |
|------|-------------|
| **UI** | Activities (`ui/…`), layouts XML, `RecyclerView` + adaptadores |
| **ViewModel** | Lógica de presentación y estado (`viewmodel/…`) |
| **Data** | Room: entidades, DAOs, `AppDatabase`; repositorios que delegan en DAOs |
| **Sesión** | `SessionManager` (SharedPreferences): usuario logueado |
| **Seguridad** | `PasswordHasher`: PBKDF2-HMAC-SHA256 para el campo `password_hash` |

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

1. **Splash** → logo y **siempre** redirección a **Login** (la sesión previa se limpia al abrir la app para exigir autenticación).
2. **Login / Registro** → validación mínima; usuarios en Room; contraseña almacenada como hash.
3. **Lista de viajes** → RecyclerView, FAB para nuevo viaje, cerrar sesión con confirmación.
4. **Detalle de viaje** → datos del viaje; **checklist** de tareas (`items`) con casillas; la primera vez se crean tareas por defecto si el viaje no tenía ítems; editar, eliminar (con diálogo) o volver.
5. **Formulario de viaje** → crear o editar viaje; guardar o volver.

Tras un login correcto, `SessionManager` guarda la sesión. Al cerrar sesión se limpia y se navega al login sin poder volver atrás a la lista (flags de intent).

---

## Estructura de paquetes (resumen)

```
com.example.tripplanner
├── data
│   ├── local
│   │   ├── dao/          # UserDao, TripDao, ItemDao
│   │   ├── db/           # AppDatabase
│   │   └── entity/       # UserEntity, TripEntity, ItemEntity
│   ├── repository/       # UserRepository, TripRepository, ItemRepository
│   └── session/          # SessionManager
├── security/             # PasswordHasher (PBKDF2)
├── ui
│   ├── base/             # BaseActivity
│   ├── splash/
│   ├── login/
│   ├── register/
│   ├── triplist/         # TripListActivity, TripAdapter
│   ├── tripdetail/
│   └── tripform/
└── viewmodel/            # ViewModels y factories
```

---

## Base de datos (Room)

- **Nombre:** `tripplanner_db`
- **Singleton:** `AppDatabase.getInstance(Context)`
- **Migraciones:** `fallbackToDestructiveMigration()` (desarrollo; en producción conviene migraciones reales)
- **Consultas en hilo principal:** `allowMainThreadQueries()` (simplifica el código; en producción suele usarse Executor/coroutines)

**Tablas principales:** `users` (credenciales; `password_hash` contiene un string PBKDF2, no la contraseña en claro), `trips`, **`items`** (tareas del checklist ligadas a `trip_id`: nombre, descripción, `completed`).

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
