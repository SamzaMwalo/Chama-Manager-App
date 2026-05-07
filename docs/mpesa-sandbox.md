# M-Pesa Sandbox Setup

## Required credentials

Create a Daraja sandbox app and collect:

- Consumer Key
- Consumer Secret
- Passkey
- Shortcode

## STK push flow

1. Generate OAuth token from Consumer Key and Consumer Secret.
2. Build password as `Base64(Shortcode + Passkey + Timestamp)`.
3. POST to:
   - `https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest`
4. Set callback URL to the deployed Firebase Cloud Function:
   - `https://<region>-<project-id>.cloudfunctions.net/mpesaCallback`
5. Reconcile callback responses in Firestore before marking a contribution confirmed.

## Production note

Do not keep OAuth token generation or Daraja secrets in the APK. Move them to Cloud Functions or another secure backend before release.
