# 🎓 Kids Learning App

Kids Learning App is an interactive educational Android application designed to make learning fun and rewarding. The app includes secure user registration and a progress-tracking system that rewards children with points as they complete activities.

## 📱 Features

- **User Profiles:** Secure registration and login system for multiple students.
- **Gamified Learning:** Integrated scoring system that tracks student progress and achievements.
- **Persistent Progress:** All user data and high scores are saved locally, so progress is never lost.
- **Modern UI:** Uses CardView and DrawerLayout for a friendly, easy-to-navigate interface built for children.

## 📸 How It Works (The Technical Flow)

### 1. Data Management (`DatabaseHelper.java`)
The core of the app is a robust SQLite database management system.

- **User Security:** Stores usernames and passwords locally in a users table, ensuring each username is unique.
- **Score Tracking:** Includes a score column added in Version 2, allowing the app to fetch and update points dynamically.
- **Smart Upgrades:** The `onUpgrade` logic checks for the score column before adding it when updating from an older version, preventing data loss or crashes.

### 2. Reactive UI & Lifecycle
Because the project uses `androidx.lifecycle` components:

- **State Management:** Using ViewModel ensures that screen rotations do not lose current progress or score.
- **Efficient Loading:** Uses `CursorAdapter` and loader logic to access the SQLite database without slowing down the UI.

### 3. Navigation

- **DrawerLayout:** A side menu allows students to switch between subjects like Math, English, and Science, or view profile and total score.
- **Fragments:** Lessons are loaded as fragments, making transitions smooth and keeping memory usage low.

## 🛠️ Technical Stack

- **Language:** Java
- **Database:** SQLite (via `SQLiteOpenHelper`)
- **Minimum SDK:** API 21 (Android 5.0)
- **Target SDK:** API 33/34

### Key Libraries

- `AppCompat` for consistent UI across Android versions
- `CardView` for lesson selection cards
- `Lifecycle` (`ViewModel` / `LiveData`) for state handling
- `ConstraintLayout` for responsive, kid-friendly layouts

## 📂 Project Structure

```
KidsLearningApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/kidslearningapp/
│   │   │   │   ├── DatabaseHelper.java    # SQLite logic (Users & Scores)
│   │   │   │   ├── MainActivity.java      # Main navigation host
│   │   │   │   ├── LoginActivity.java     # User authentication
│   │   │   │   └── QuizFragment.java      # Educational content logic
│   │   │   └── res/
│   │   │       ├── layout/                # XML UI designs
│   │   │       └── values/                # App themes and colors
│   └── build.gradle                       # Dependencies and SDK config
└── README.md
```

## Installation

1. Open Android Studio.
2. Import the Kids Learning App project.
3. Sync Gradle to install the necessary AndroidX libraries.
4. Run the app on an emulator or a physical device (API 21+).

Developed by Charles Pura — Empowering the next generation through technology.
