# Firebase Setup Guide

This guide starts from zero and ends with the app connected to Firebase.

## 1. Create a Firebase project

1. Open [Firebase Console](https://console.firebase.google.com/).
2. Click `Create a project`.
3. Name it something like `Chama Manager 104`.
4. Continue through the wizard.
5. You may disable Google Analytics for the first test if you want a simpler setup.

## 2. Register the Android app

1. Inside the Firebase project, click `Add app`.
2. Choose the Android icon.
3. Enter this package name exactly:
   - `com.chamamanager104`
4. App nickname:
   - `Chama Manager 104`
5. Click `Register app`.

## 3. Download `google-services.json`

1. Download `google-services.json`.
2. Place it in:
   [app](/C:/Users/USER/Documents/Codex/2026-04-27/you-are-a-senior-software-engineer/chama-manager104/app)

The final path should be:

`C:\Users\USER\Documents\Codex\2026-04-27\you-are-a-senior-software-engineer\chama-manager104\app\google-services.json`

## 4. Enable Authentication

1. In the Firebase Console left sidebar, open `Build`.
2. Click `Authentication`.
3. Click `Get started`.
4. Open the `Sign-in method` tab.
5. Enable `Email/Password`.
6. Save.

## 5. Create Firestore Database

1. In the Firebase Console left sidebar, open `Build`.
2. Click `Firestore Database`.
3. Click `Create database`.
4. Choose `Start in production mode`.
5. Pick a region close to your users.
6. Create the database.

## 6. Apply Firestore security rules

Use the rules from:
[firestore.rules](/C:/Users/USER/Documents/Codex/2026-04-27/you-are-a-senior-software-engineer/chama-manager104/firebase/firestore.rules)

In Firebase Console:

1. Open `Firestore Database`.
2. Open the `Rules` tab.
3. Replace the existing rules with the contents of `firestore.rules`.
4. Publish.

## 7. Optional but recommended: Cloud Functions

The sample functions live in:
[firebase/cloud-functions](/C:/Users/USER/Documents/Codex/2026-04-27/you-are-a-senior-software-engineer/chama-manager104/firebase/cloud-functions)

To deploy:

1. Install Firebase CLI:
   - `npm install -g firebase-tools`
2. Log in:
   - `firebase login`
3. From the `firebase/cloud-functions` folder, install dependencies:
   - `npm install`
4. From the Firebase project root or configured folder:
   - `firebase deploy --only functions`

## 8. Confirm the app is linked

After adding `google-services.json`, opening the project in Android Studio, and syncing Gradle:

- Firebase Auth calls should work
- Firestore reads and writes should resolve
- Password reset emails should send

## 9. Test account creation

Use the app to create an account with:

- Full name
- Phone number
- Email
- New chama name
- Password

Leave invite code empty for the first chairperson account.

That should create:

- a new Firebase Auth user
- a `chamas` document
- a `users` document
- a `members` document for the signed-up user
