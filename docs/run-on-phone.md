# Run on Your Phone Over USB

## 1. Install Android Studio

Download and install Android Studio from:
[Android Studio](https://developer.android.com/studio)

## 2. Open the project

Open:
[chama-manager104](/C:/Users/USER/Documents/Codex/2026-04-27/you-are-a-senior-software-engineer/chama-manager104)

Then wait for Gradle sync to complete.

## 3. Connect Firebase first

Finish the steps in:
[firebase-setup.md](/C:/Users/USER/Documents/Codex/2026-04-27/you-are-a-senior-software-engineer/chama-manager104/docs/firebase-setup.md)

Especially:

- add `google-services.json`
- enable Email/Password Authentication
- create Firestore

## 4. Prepare your phone

On the phone:

1. Open `Settings`.
2. Open `About phone`.
3. Tap `Build number` 7 times.
4. Go back to `Developer options`.
5. Turn on `USB debugging`.

## 5. Connect with USB cable

1. Plug the phone into the computer.
2. If the phone asks to allow USB debugging, tap `Allow`.

## 6. Run the app

In Android Studio:

1. Choose your phone in the device selector.
2. Click the green `Run` button.

The app should build, install, and launch on the phone.

## 7. If the app does not run

Common causes:

- `google-services.json` missing
- Authentication not enabled in Firebase
- Firestore not created
- wrong package name in Firebase
- Android SDK components not installed in Android Studio

The package name used by this app is:

`com.chamamanager104`
