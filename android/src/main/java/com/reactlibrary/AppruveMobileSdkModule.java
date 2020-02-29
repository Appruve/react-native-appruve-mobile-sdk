package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import org.jetbrains.annotations.NotNull;

import co.appruve.identitysdk.AppruveActivity;

import static co.appruve.identitysdk.AppruveActivityKt.RESULT_SUCCESS_CODE;
import static co.appruve.identitysdk.Constants.APPRUVE_API_TOKEN;
import static co.appruve.identitysdk.Constants.APPRUVE_EXTRA_IS_VERIFIED;
import static co.appruve.identitysdk.Constants.HEADER_COLOR;
import static co.appruve.identitysdk.Constants.ICON_COLOR;
import static co.appruve.identitysdk.Constants.ICON_TEXT_COLOR;
import static co.appruve.identitysdk.Constants.NAVIGATION_BUTTON_BACKGROUND_COLOR;
import static co.appruve.identitysdk.Constants.NORMAL_TEXT_COLOR;
import static co.appruve.identitysdk.Constants.STATUS_BAR_COLOR;
import static co.appruve.identitysdk.Constants.SUB_HEADER_COLOR;
import static co.appruve.identitysdk.Constants.TEXT_LABEL_COLOR;
import static co.appruve.identitysdk.Constants.TOOLBAR_BACKGROUND_COLOR;

public class AppruveMobileSdkModule extends ReactContextBaseJavaModule {

    private static final int START_VERIFICATION_ACTIVITY_REQUEST = 101;
    private static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
    private static final String VERIFICATION_CANCELLED = "VERIFICATION_CANCELLED";
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";

    private Promise mVerificationPromise;
    private final ReactApplicationContext reactContext;

    public AppruveMobileSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        // Add the listener for `onActivityResult`
        ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                if (requestCode == START_VERIFICATION_ACTIVITY_REQUEST) {
                    if (mVerificationPromise != null) {
                        if (resultCode == Activity.RESULT_CANCELED) {
                            mVerificationPromise.reject(VERIFICATION_CANCELLED, "Verification was cancelled");
                        } else if (resultCode == RESULT_SUCCESS_CODE) {
                            if (data != null && data.getExtras() != null) {
                                boolean verified = data.getExtras().getBoolean(APPRUVE_EXTRA_IS_VERIFIED);
                                mVerificationPromise.resolve(verified);
                            } else {
                                mVerificationPromise.reject(UNEXPECTED_ERROR, "An unexpected error occurred");
                            }
                        }
                        mVerificationPromise = null;
                    }
                }
            }
        };
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @NotNull
    @Override
    public String getName() {
        return "AppruveMobileSdk";
    }

    @ReactMethod
    public void startVerification(String apiToken,
                                  String statusBarColor,
                                  String toolbarBackgroundColor,
                                  String headerTextColor,
                                  String subHeaderTextColor,
                                  String textLabelColor,
                                  String normalTextColor,
                                  String iconColor,
                                  String iconTextColor,
                                  String navigationButtonBackgroundColor,
                                  boolean isGhanaEnabled,
                                  boolean isNigeriaEnabled,
                                  boolean isKenyaEnabled, final Promise promise) {


        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        mVerificationPromise = promise;

        final Intent verificationIntent = new Intent(reactContext, AppruveActivity.class);
        final Bundle extras = new Bundle();

        extras.putString(APPRUVE_API_TOKEN, apiToken);
        extras.putString(STATUS_BAR_COLOR, statusBarColor);
        extras.putString(TOOLBAR_BACKGROUND_COLOR, toolbarBackgroundColor);
        extras.putString(HEADER_COLOR, headerTextColor);
        extras.putString(SUB_HEADER_COLOR, subHeaderTextColor);
        extras.putString(TEXT_LABEL_COLOR, textLabelColor);
        extras.putString(NORMAL_TEXT_COLOR, normalTextColor);
        extras.putString(ICON_COLOR, iconColor);
        extras.putString(ICON_TEXT_COLOR, iconTextColor);
        extras.putString(NAVIGATION_BUTTON_BACKGROUND_COLOR, navigationButtonBackgroundColor);
        verificationIntent.putExtras(extras);

        reactContext.startActivityForResult(verificationIntent, START_VERIFICATION_ACTIVITY_REQUEST, extras);
    }
}
