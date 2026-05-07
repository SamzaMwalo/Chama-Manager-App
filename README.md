# Chama Manager 104

Chama Manager 104 is an Android application for managing chama operations for savings and investment groups. It supports member administration, contributions, loans, meetings, notifications, offline-first local storage, and Firebase-backed cloud sync.

## Core features

- Multi-chama onboarding with create-or-join flow
- Role-based access for chairperson, treasurer, and member
- Login, account creation, and password reset
- Onboarding experience after splash screen
- Member management with add, edit, view, and delete actions
- Contribution recording and member contribution tracking
- Loan application and repayment tracking foundation
- Meeting scheduling, attendance capture, and minutes storage
- Dashboard summaries for savings, loans, meetings, and active members
- Firebase Authentication and Cloud Firestore integration
- Room local persistence for offline-first behavior
- M-Pesa STK Push integration scaffolding

## Technology stack

- Kotlin
- Jetpack Compose
- MVVM architecture
- Hilt dependency injection
- Room
- Firebase Authentication
- Cloud Firestore
- Firebase Cloud Functions scaffolding
- Retrofit and OkHttp

## Project structure

```text
chama-manager104/
  app/
  docs/
  firebase/
  build.gradle.kts
  gradle.properties
  settings.gradle.kts
```

## Android configuration

- Application ID: `com.chamamanager104`
- Minimum SDK: `24`
- Target SDK: `34`
- Compile SDK: `34`
- Version: `1.0.0`

## Main modules

- `Authentication`: login, signup, forgot password
- `Dashboard`: group summary and operational overview
- `Members`: member records and chairperson actions
- `Contributions`: contribution capture and listing
- `Loans`: loan submission and loan-book overview
- `Meetings`: scheduling, attendance, and minutes
- `Notifications`: notification feed
- `Reports`: reporting surface and export foundation

## Import into Android Studio

1. Open Android Studio.
2. Select `Open`.
3. Choose the `chama-manager104` folder.
4. Allow Gradle sync to complete.

## Firebase setup

1. Create a Firebase project.
2. Register an Android app with package name `com.chamamanager104`.
3. Download `google-services.json`.
4. Place `google-services.json` inside:

```text
app/google-services.json
```

5. In Firebase Console, enable:
   - Authentication
   - Email/Password sign-in
   - Firestore Database
6. Publish the Firestore rules from:

```text
firebase/firestore.rules
```

Detailed Firebase steps are available in [docs/firebase-setup.md](docs/firebase-setup.md).

## Run on a phone

1. Enable Developer Options on the Android phone.
2. Turn on USB debugging.
3. Connect the phone to the computer using a USB cable.
4. Approve the USB debugging prompt on the phone.
5. In Android Studio, select the connected device.
6. Click `Run`.

Detailed device steps are available in [docs/run-on-phone.md](docs/run-on-phone.md).

## Important project notes

- Member access is role-aware.
- Chairperson workflows control member administration.
- Contribution records are tied to member IDs within the current chama.
- Meetings and members are stored locally and synchronized with Firestore-backed data flows.
- Firebase Cloud Functions and M-Pesa flows are scaffolded for secure backend extension.

## Supporting documentation

- [docs/firebase-setup.md](docs/firebase-setup.md)
- [docs/run-on-phone.md](docs/run-on-phone.md)
- [docs/database-schema.md](docs/database-schema.md)
- [docs/mpesa-sandbox.md](docs/mpesa-sandbox.md)

## Build notes

- Open the project in Android Studio and allow dependency resolution to finish before the first run.
- If Firebase configuration is changed, re-sync the project and rebuild.
- If Room schema-related local cache issues appear during development, uninstall the debug app and run again.
