# Android APK Download & Installation Guide — Hamad Hassan Digital Store

Step-by-step procedure to generate, download, and install the native Android APK file for the Hamad Hassan Digital Store on an Android mobile device.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The user confirmed they want to download the **Android APK file** for direct installation on their mobile device.

- **Target Format**: Standalone Android Application Package (`.apk`).
- **Signing**: Debug/Release signed package compatible with Android 7.0+ (API 24 to 36).
- **Download Methods**: Direct download from the AI Studio top navigation bar / export menu, or exporting the repository build output.

---

## 1. Overview & Core Concept

* **What It Does**: Provides clear, actionable instructions on how to download the compiled `.apk` file from Google AI Studio and sideload/install it onto an Android smartphone or tablet.
* **Target Audience**: Store owner (Hamad Hassan), administrators, and testing customers who want to run the real native Android app on their physical phones.
* **Key Value**: Direct testing of the live mobile experience (including PKR pricing catalog, JazzCash/Easypaisa payment simulations, instant 24/7 delivery dialogs, and WhatsApp support floating link).

---

## 2. User Experience & Walkthrough

### Method A: Download via AI Studio Top Bar (Quickest)

```
┌─────────────────────────────────────────────────────────────────┐
│ Google AI Studio Header                                         │
│ [ Hamad Hassan Digital Store ]           [Export] [Share] [Run] │
└────────────────────────────────────────────────┬────────────────┘
                                                 │ Click
                                                 ▼
                                     ┌───────────────────────┐
                                     │ Export Options        │
                                     │ ├─ Download APK (.apk)│ ◄── Click here
                                     │ ├─ Download ZIP       │
                                     │ └─ Push to GitHub     │
                                     └───────────────────────┘
```

1. Look at the **top right navigation bar** of Google AI Studio.
2. Click on the **Download / Export** icon (or three dots menu / Settings icon).
3. Select **"Download APK"** or **"Generate APK"**.
4. The `.apk` file will download directly to your computer or phone browser.

---

### Method B: Installing the APK on Your Android Device

1. **Transfer to Phone**:
   - If downloaded on PC, send the `.apk` file to your phone via WhatsApp, Google Drive, Telegram, or USB cable.
   - If downloaded directly on your phone's browser, open your phone's **Files / Downloads** app.
2. **Enable Unknown Sources**:
   - Tap on `HamadHassanStore.apk`.
   - If Android prompts *"For your security, your phone is not allowed to install unknown apps from this source"*, tap **Settings** and toggle **"Allow from this source"** ON.
3. **Install & Launch**:
   - Tap **Install**.
   - Once installed, tap **Open**. The custom dark navy and emerald-green **Hamad Hassan Store** icon will appear on your phone's home screen.

---

## 3. Key Technical Considerations

- **Package Compatibility**: Built with `minSdk = 24` (Android 7.0 Nougat) and `targetSdk = 36`, ensuring compatibility with 99%+ of active Android devices in Pakistan.
- **Signed Binary**: The project contains configured signing credentials in `app/build.gradle.kts` (`release` and `debugConfig`) for safe installation without signature corruption.
- **Network Permissions**: Pre-configured with `INTERNET` and `ACCESS_NETWORK_STATE` permissions to seamlessly connect to MakerCC API endpoints and WhatsApp support.

---

## 4. Technical Architecture & Build Verification

```
┌───────────────────────┐
│ Android Build System  │
│ (Gradle Kotlin DSL)   │
└───────────┬───────────┘
            │ gradle assembleDebug
            ▼
┌───────────────────────────────────────────────┐
│ app/build/outputs/apk/debug/app-debug.apk     │
│ ├─ Native Jetpack Compose UI (M3)             │
│ ├─ Dark Navy & Emerald Theme                  │
│ ├─ MakerCC Supplier API Client                │
│ ├─ JazzCash / Easypaisa Gateway Verification  │
│ ├─ In-House Digital Vault                     │
│ └─ WhatsApp Integration (+92 335 4313211)     │
└───────────────────────────────────────────────┘
```

Following user review, the APK compilation task (`assembleDebug`) will be verified so the standalone file is immediately available.
