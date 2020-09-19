# react-native-appruve-mobile-sdk

## Getting started

First get your API Token at https://www.appruve.co

`$ npm install react-native-appruve-mobile-sdk --save`

## Usage
```javascript
import AppruveMobileSdk from 'react-native-appruve-mobile-sdk';

// Start the verification activity
AppruveMobileSdk.startVerification(
    'YOUR API TOKEN',
    {userId: '47a025e3-d4a4-4a8f-aa9d-d9dde90d8a15'}, // custom metadata which will be saved alongside the verification result
    true // isGhanaEnabled,
    true // isNigeriaEnabled,
    true // isKenyaEnabled,
)
    .then(response => {
        console.log(`IS_VERIFIED: ${response.isVerified}`);
        console.log(`VERIFICATION_ID ${response.id}`);
        console.log(`ID_PHOTO_URL: ${response.idPhotoUrl}`);
        console.log(`SELFIE_PHOTO_URL: ${response.selfiePhotoUrl}`);
        console.log(`ID_TYPE: ${response.idType}`);
    })
    .catch(e => {
        console.log(e);
    });
};
```

## Customizing the look for Android

### Material Design theming

Our UI relies on a Material Design theme (ie, a theme that extends from Theme.MaterialComponents). colorPrimary, colorPrimaryDark, and colorAccent.


### Use or extend an SDK theme

The SDK has an out-of-the-box UI that requires a Material Design theme (ie, a style that extends from Theme.MaterialComponents). The SDK respects light, dark, and light with dark action bar themes. The colorPrimary, colorPrimaryDark, and colorAccent attributes are used by the SDK, so it will inherit the material theme from your app. You can extend a SDK theme as follows:

```java
<style name="YourLightTheme" parent="AppruveSdkTheme.Light">
...
</style>
```

You can then set this theme in your AndroidManifest.xml as follows:
```java
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.appruve.example" >
    ...
    <application
        ...
        android:theme="@style/YourLightTheme"
        ...
    />
    ...
</manifest>
```

Alternatively, if you don't want to set the theme at the application level, then you will need to do the following:
```java
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.appruve.example" >
    ...
    <application ...>
        <activity tools:replace="android:theme" android:name="co.appruve.identitysdk.VerificationActivity"
            android:theme="@style/YourLightTheme" />

    </application>
    ...
</manifest>
```

### Use your own theme

If you don't extend a SDK theme, you must still apply a theme that extends from Theme.MaterialComponents to the SDK Activity classes.

```java
<style name="YourLightTheme" parent="Theme.MaterialComponents.Light">
    <item name="colorPrimary">@color/my_color_primary</item>
    <item name="colorPrimaryDark">@color/my_color_primary_dark</item>
    <item name="colorAccent">@color/my_color_accent</item>
</style>
```