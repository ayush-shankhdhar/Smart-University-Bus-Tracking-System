
# 🚌 CampusRide – Smart University Bus Tracking System

CampusRide is a modern Android application designed to make university transportation smarter, more accessible, and easier to manage. It provides students with bus tracking and route information while helping drivers and administrators manage campus transportation efficiently.

The application combines real-time location services, route management, local data storage, and a role-based user experience to create a convenient campus transit solution.

---

## 📱 Overview

CampusRide is built for three primary users:

- 👨‍🎓 **Students** – Track buses, view routes and schedules, save favourite routes, and submit complaints.
- 🚍 **Drivers** – Manage assigned trips and share live bus location during active journeys.
- 🛡️ **Administrators** – Monitor the fleet, manage schedules, and review student complaints.

The goal is to improve campus mobility through reliable transportation information and a simple, user-friendly Android experience.

---

## ✨ Key Features

### 👨‍🎓 Student Module

- **Live Bus Tracking:** View bus locations and route information on an interactive map.
- **Distance & ETA:** Calculate approximate distance and estimated arrival time between the student and a bus.
- **Active Bus List:** Browse available buses with search, filtering, and status information.
- **Favourite Routes:** Save frequently used buses and routes for quick access.
- **Bus Schedules:** View university bus timetables and route schedules.
- **Feedback & Complaints:** Submit transportation-related complaints and view their status.
- **Offline Support:** Access locally cached information when network connectivity is unavailable.

### 🚍 Driver Module

- **Driver Dashboard:** View assigned bus information and trip details.
- **Trip Management:** Start and stop active transportation trips.
- **Foreground Location Service:** Share the driver's bus location during an active trip.
- **Persistent Notification:** Display an ongoing notification while location tracking is active.
- **Location Synchronization:** Send location updates to the remote backend and maintain local data where supported.

### 🛡️ Admin Module

- **Fleet Overview:** Monitor buses, drivers, and available transportation information.
- **Schedule Management:** Create, update, and manage bus schedules.
- **Complaint Management:** Review and manage student complaints.
- **Bus Information:** Inspect and manage relevant bus and route details.
- **Role-Based Access:** Provide different features according to the user's role.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary programming language |
| **Android SDK** | Android application development |
| **MVVM Architecture** | Separation of UI, business logic, and data |
| **Material Design 3** | Modern and consistent user interface |
| **Room Database** | Local SQLite-based data persistence |
| **Kotlin Coroutines** | Asynchronous programming |
| **Kotlin Flow / StateFlow** | Reactive data observation |
| **Firebase Authentication** | User authentication |
| **Firebase Realtime Database** | Remote data and location synchronization |
| **Google Maps SDK** | Interactive maps and route visualization |
| **Fused Location Provider** | Device location services |
| **Foreground Services** | Location tracking during active trips |
| **RecyclerView & ListAdapter** | Efficient list rendering |

---

## 🏗️ Architecture

CampusRide follows the **Model–View–ViewModel (MVVM)** architecture to keep the application organized, maintainable, and easier to extend.

```text
┌─────────────────────────────────────┐
│             UI Layer                │
│  Activities • Adapters • Dialogs    │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│           ViewModel Layer           │
│   UI State • Business Logic         │
│   Coroutines • StateFlow            │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│          Repository Layer           │
│       Data Access Abstraction       │
└───────────────┬───────────┬─────────┘
                │           │
                ▼           ▼
┌────────────────────┐ ┌────────────────────┐
│   Local Data       │ │   Remote Data      │
│   Room Database    │ │   Firebase         │
└────────────────────┘ └────────────────────┘
```

### Architecture Benefits

- Clear separation of responsibilities.
- Easier maintenance and testing.
- Better handling of asynchronous operations.
- Reactive UI updates using Kotlin Flow.
- Flexible integration of local and remote data sources.

---

## 📂 Project Structure

```text
Smart-University-Bus-Tracking-System/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/bustracking/
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── AppDatabase.kt
│           │   │   │   ├── dao/
│           │   │   │   └── entity/
│           │   │   └── repository/
│           │   │
│           │   ├── service/
│           │   │   └── LocationForegroundService.kt
│           │   │
│           │   ├── ui/
│           │   │   ├── auth/
│           │   │   ├── student/
│           │   │   ├── driver/
│           │   │   ├── admin/
│           │   │   └── maps/
│           │   │
│           │   └── utils/
│           │       ├── NetworkUtils.kt
│           │       ├── LocationUtils.kt
│           │       └── NotificationUtils.kt
│           │
│           ├── res/
│           └── AndroidManifest.xml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

> **Note:** The structure above is an example of the intended organization. Actual package names and files may vary depending on the current implementation.

---

## 🗺️ Bus Tracking Workflow

```text
Driver starts an active trip
            │
            ▼
Location permission is verified
            │
            ▼
Foreground location service starts
            │
            ▼
Current bus coordinates are obtained
            │
            ▼
Location is synchronized with backend
            │
            ▼
Student retrieves available bus location
            │
            ▼
Map displays bus and route information
            │
            ▼
Distance and estimated arrival time
            │
            ▼
Driver ends the active trip
```

---

## 🔐 Authentication & User Roles

CampusRide supports role-based access for different users.

| User Role | Access |
|---|---|
| **Student** | Bus tracking, schedules, favourites, complaints |
| **Driver** | Assigned bus, active trips, location sharing |
| **Administrator** | Fleet information, schedules, complaints |

User authentication is handled through Firebase Authentication. Access to different modules should be controlled by the application's role-management logic.

> **Security note:** Do not commit real passwords, API keys, Firebase credentials, or private configuration files to the repository.

---

## 📍 Location & Maps

CampusRide uses Android location services and Google Maps integration to support transportation tracking.

### Supported Capabilities

- Display the user's current location.
- Display bus coordinates on a map.
- Show bus stops and route markers.
- Draw route polylines.
- Calculate distance between locations.
- Estimate approximate arrival time.
- Track driver location during an active trip.

Location tracking should operate only when the appropriate permissions have been granted and the driver has started an active trip.

---

## 💾 Local Data & Offline Support

Room Database is used for local data persistence and caching.

Potential locally stored information includes:

- Bus information.
- Route details.
- Bus schedules.
- Favourite routes.
- Student complaints.

Kotlin Coroutines and Flow help manage asynchronous database operations and observe changes in local data.

When the network is unavailable, the application can use locally cached information where supported by the implementation.

---

## 🎨 UI & Design

CampusRide follows a clean and modern Android design approach using Material Design 3.

### Design Principles

- Clear navigation between modules.
- Consistent typography and spacing.
- Material cards and buttons.
- Informative status indicators.
- User-friendly forms and dialogs.
- Responsive layouts for different screen sizes.
- Accessible and readable interface elements.

### Color Palette

| Element | Color |
|---|---|
| Primary | Deep Blue / Indigo |
| Secondary | Teal / Cyan |
| Background | Light Slate |
| Cards | White / Elevated Surface |
| Text | Dark Neutral |

---

## ⚙️ Requirements

Before setting up CampusRide, make sure you have:

- Android Studio installed.
- Android SDK configured.
- A compatible JDK version for the project's Gradle and Android plugin configuration.
- An Android emulator or physical Android device.
- A Google Maps API key, if required by the implementation.
- Firebase project configuration, if Firebase services are enabled.
- Internet access for remote services and dependency downloads.

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ayush-shankhdhar/Smart-University-Bus-Tracking-System.git
```

### 2. Open the Project

Open the cloned project directory in Android Studio.

Allow Gradle to synchronize and download the required dependencies.

### 3. Configure Firebase

If Firebase is used by the application:

1. Create or select a Firebase project.
2. Register the Android application.
3. Download the appropriate `google-services.json` file.
4. Place it in the required application module directory.
5. Enable the Firebase services required by the application.

Do not upload private or sensitive Firebase configuration files unless they are safe to share publicly.

### 4. Configure Google Maps

If Google Maps is enabled:

1. Create a Google Maps API key.
2. Enable the required Maps SDK.
3. Configure the key according to the project's Gradle and manifest setup.
4. Restrict the API key appropriately.

For example, if the project reads a Maps key from `local.properties`:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

Use the exact configuration expected by the current project.

### 5. Build the Application

From the project root, run:

```bash
./gradlew assembleDebug
```

On Windows:

```bash
gradlew.bat assembleDebug
```

### 6. Run the Application

- Open the project in Android Studio.
- Select an emulator or connected Android device.
- Sync the project.
- Click **Run** to launch the application.

---

## 🧪 Testing Checklist

Before using the application, verify the following:

- [ ] User authentication works correctly.
- [ ] Student dashboard loads successfully.
- [ ] Bus list displays available information.
- [ ] Search and filtering work as expected.
- [ ] Bus schedules are displayed correctly.
- [ ] Favourite routes can be saved and retrieved.
- [ ] Complaint submission works correctly.
- [ ] Driver dashboard displays assigned trip information.
- [ ] Location permissions are handled properly.
- [ ] Foreground location service starts and stops correctly.
- [ ] Bus coordinates are synchronized correctly.
- [ ] Google Maps loads successfully.
- [ ] Admin schedule management works correctly.
- [ ] Offline behaviour is handled appropriately.

---

## 🔒 Privacy & Permissions

CampusRide may request permissions related to:

- Internet access.
- Fine and coarse location.
- Foreground location tracking.
- Notifications, where required.

Location access should be used transparently and only for supported transportation features. Users should be informed when location tracking is active.

---

## 🔮 Future Enhancements

Potential future improvements include:

- Push notifications for bus arrival updates.
- Improved ETA prediction using historical travel data.
- Real-time traffic-aware route estimation.
- More advanced admin analytics.
- Improved accessibility support.
- Multi-campus transportation support.
- Better offline synchronization.
- Automated testing and performance monitoring.

---

## 🤝 Contributing

Contributions and suggestions are welcome.

To contribute:

1. Fork the repository.
2. Create a new feature branch.

```bash
git checkout -b feature/your-feature-name
```

3. Make your changes.
4. Commit your work.

```bash
git add .
git commit -m "Add your feature description"
```

5. Push your branch.

```bash
git push origin feature/your-feature-name
```

6. Open a pull request.

---

## 👨‍💻 Developer

**Ayush **

CampusRide – Smart University Bus Tracking System

[View Repository](https://github.com/ayush-shankhdhar/Smart-University-Bus-Tracking-System)

---

## 📄 License

This project currently does not specify a separate open-source license.

If you plan to allow others to reuse, modify, or distribute the code, consider adding an appropriate license.

