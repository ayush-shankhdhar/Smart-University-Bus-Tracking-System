# CampusRide – Smart University Bus Tracking System 🚌🎓

**CampusRide** is a modern, production-grade Android application designed for university campus transportation. Built using **Kotlin**, **MVVM Architecture**, **Material Design 3**, **Room Database**, **Kotlin Coroutines & Flow**, **Fused Location Services**, and **Google Maps API**, CampusRide provides a real-time transit tracking experience for Students, Drivers, and Administrators.

Developed as an academic RPL / CSE226 qualifying project demonstration.

---

## 🌟 Key Features

### 👨‍🎓 Student Module
- **Live Bus Tracking**: Interactive Google Maps view displaying student location, live bus coordinates, stop markers, and polyline campus routes.
- **Distance & ETA Calculation**: Real-time calculation of distance (in km/meters) and estimated time of arrival (ETA in minutes) from student to bus.
- **Active Bus List**: RecyclerView list with search, filtering, and status badges.
- **Favourite Routes**: Save favourite buses and routes locally using Room Database.
- **Bus Schedules**: View official university bus timetables and departure frequencies.
- **Feedback & Complaints**: Submit transit complaints and view status of past submissions.
- **Offline Mode**: Automatic fallbacks to Room Database cache when internet connectivity is lost.

### 🚍 Driver Module
- **Driver Dashboard**: View assigned bus route and trip guidelines.
- **Foreground Location Service**: `LocationForegroundService` runs continuous background GPS location tracking only during active trips.
- **Persistent Notification**: Displays an ongoing notification with quick "Stop Trip" action.
- **Real-time Backend Sync**: Streams coordinates to Firebase Realtime Database and local Room Database.

### 🛡️ Admin Module
- **Fleet Overview**: Real-time monitor of active drivers, buses, and coordinates.
- **Schedule Management (CRUD)**: Create and update bus route schedules broadcast to all students.
- **Complaints Review**: Review and manage submitted student complaints.
- **Material Confirmation Dialogs**: `MaterialAlertDialogBuilder` for inspecting bus details.

---

## 🛠️ Technology Stack & Architecture

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: Material Design 3 (Cards, TextInputLayout, MaterialButtons, Chips, Surface Colors)
- **Asynchronous & Reactive**: Kotlin Coroutines (`viewModelScope`, `lifecycleScope`, `Dispatchers.IO`) & `StateFlow` / `Flow`
- **Local Database**: Room Database (SQLite ORM) for offline caching
- **Remote Services**: Firebase Authentication & Firebase Realtime Database
- **Location & Maps**: FusedLocationProviderClient & Google Maps SDK (SupportMapFragment, Polylines, Custom Markers)
- **Background Tasking**: Foreground Service with Notification Channels

```
com.example.bustracking/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt (Room ORM)
│   │   ├── dao/ (BusDao, RouteDao, ScheduleDao, FavouriteDao, ComplaintDao)
│   │   └── entity/ (BusEntity, RouteEntity, StopEntity, ScheduleEntity, FavouriteEntity, ComplaintEntity)
│   └── repository/ (BusRepository, ScheduleRepository, ComplaintRepository, FavouriteRepository)
├── service/
│   └── LocationForegroundService.kt (Foreground location tracking service)
├── ui/
│   ├── auth/ (SignIn, SignUp)
│   ├── student/ (StudentHome, RouteSchedule, ComplaintBox, WriteComplain)
│   ├── driver/ (Home)
│   ├── admin/ (Admin, UpdateSchedule, AdminComplaintBox)
│   └── maps/ (MapsActivity)
└── utils/
    ├── NetworkUtils.kt (ConnectivityObserver via Flow)
    ├── LocationUtils.kt (ETA & Distance matrix calculations)
    └── NotificationUtils.kt (Notification channels)
```

---

## 📚 CSE226 Android Concepts Demonstrated

| Concept | Implementation Details |
| :--- | :--- |
| **RecyclerView & ListAdapter** | Used in `StudentHome`, `RouteSchedule`, `Admin`, `ComplaintBox`, and `AdminComplaintBox` with `DiffUtil.ItemCallback` for optimal performance without redundant UI redraws. |
| **Kotlin Coroutines & Flow** | Asynchronous database and network operations using `lifecycleScope`, `Dispatchers.IO`, and reactive `Flow` stream updates. |
| **Services (Foreground Service)** | `LocationForegroundService` handles continuous GPS streaming when the driver starts a trip, bound with a persistent notification. |
| **SQLite & Room Database** | Local persistent storage via Room ORM (`AppDatabase`, `BusEntity`, `FavouriteEntity`, `ScheduleEntity`, `ComplaintEntity`) for offline access. |
| **Location APIs** | Integrated `FusedLocationProviderClient` with `LocationRequest` for accurate GPS tracking, distance calculation (`Location.distanceBetween`), and ETA calculation. |
| **Google Maps SDK** | `SupportMapFragment`, custom marker hue colors (Azure for student, Red for bus, Orange for stops), polyline route drawing, and camera animation. |
| **CRUD Operations** | Create/Update/Delete functionalities for Bus Schedules, Student Complaints, and Saved Favourites. |
| **Authentication & Role System** | Role-based routing after sign-in based on domain (`@bus.com` -> Driver, `@admin.com` -> Admin, default -> Student). |
| **Offline-First Storage** | Real-time `NetworkUtils` observer displays an Offline Banner and seamlessly displays cached Room data when internet is lost. |
| **Runtime Permissions** | Runtime permission requesting for `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `FOREGROUND_SERVICE`, and `POST_NOTIFICATIONS`. |

---

## 🎨 Visual Design System

- **Primary**: Deep Blue / Indigo (`#1E3A8A`)
- **Secondary**: Teal / Cyan (`#0D9488`)
- **Background**: Slate Neutral (`#F8FAFC`)
- **Cards & Elevation**: 16dp / 20dp corner radius with stroke borders

---

## 🚀 Setup & Build Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ayush-shankhdhar/Smart-University-Bus-Tracking-System.git
   cd Smart-University-Bus-Tracking-System
   ```

2. **Open in Android Studio**:
   Open the project directory in Android Studio (Hedgehog / Iguana or later with JDK 17 support).

3. **Google Maps API Key Setup**:
   Add your Google Maps API key in `local.properties`:
   ```properties
   MAPS_API_KEY=AIzaSy...
   ```

4. **Build and Run**:
   - Select device or emulator.
   - Run `./gradlew assembleDebug` or click **Run 'app'** in Android Studio.

---

## 🔐 Credentials & Role Testing Guide

- **Student**: Any standard email (e.g. `student@campus.edu`)
- **Driver**: Any email ending with `@bus.com` (e.g. `driver101@bus.com`)
- **Admin**: Any email ending with `@admin.com` (e.g. `admin@admin.com`)

---

## 👨‍💻 Project Developer & Maintainer

- **Developer**: Ayush Shankhdhar
- **Project**: CampusRide – Smart University Bus Tracking System
- **Repository**: [https://github.com/ayush-shankhdhar/Smart-University-Bus-Tracking-System](https://github.com/ayush-shankhdhar/Smart-University-Bus-Tracking-System)

