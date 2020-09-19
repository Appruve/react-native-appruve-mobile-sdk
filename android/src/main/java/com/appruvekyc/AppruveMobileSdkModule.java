package com.appruvekyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import co.appruve.identitysdk.VerificationActivity;

import static co.appruve.identitysdk.ConstantsKt.APPRUVE_API_TOKEN;
import static co.appruve.identitysdk.ConstantsKt.APPRUVE_EXTRA_DOCUMENT_TYPE;
import static co.appruve.identitysdk.ConstantsKt.APPRUVE_EXTRA_ID_PHOTO_URL;
import static co.appruve.identitysdk.ConstantsKt.APPRUVE_EXTRA_IS_VERIFIED;
import static co.appruve.identitysdk.ConstantsKt.APPRUVE_EXTRA_SELFIE_PHOTO_URL;
import static co.appruve.identitysdk.ConstantsKt.APPRUVE_EXTRA_VERIFICATION_ID;
import static co.appruve.identitysdk.ConstantsKt.CUSTOM_PARAMS;
import static co.appruve.identitysdk.ConstantsKt.IS_GHANA_ENABLED;
import static co.appruve.identitysdk.ConstantsKt.IS_KENYA_ENABLED;
import static co.appruve.identitysdk.ConstantsKt.IS_NIGERIA_ENABLED;
import static co.appruve.identitysdk.VerificationActivityKt.RESULT_SUCCESS_CODE;

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
                                String idPhotoUrl = data.getExtras().getString(APPRUVE_EXTRA_ID_PHOTO_URL);
                                String selfiePhotoUrl = data.getExtras().getString(APPRUVE_EXTRA_SELFIE_PHOTO_URL);
                                String id = data.getExtras().getString(APPRUVE_EXTRA_VERIFICATION_ID);
                                String idType = data.getExtras().getString(APPRUVE_EXTRA_DOCUMENT_TYPE);

                                HashMap<String, Object> responseData = new HashMap<>();
                                responseData.put("id", id);
                                responseData.put("isVerified", verified);
                                responseData.put("idPhotoUrl", idPhotoUrl);
                                responseData.put("selfiePhotoUrl", selfiePhotoUrl);
                                responseData.put("idType", idType);

                                mVerificationPromise.resolve(responseData);
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
                                  ReadableMap customParams,
                                  boolean isGhanaEnabled,
                                  boolean isNigeriaEnabled,
                                  boolean isKenyaEnabled, final Promise promise) {

        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        mVerificationPromise = promise;

        final Intent verificationIntent = new Intent(reactContext, VerificationActivity.class);
        final Bundle extras = new Bundle();
        final Bundle customParamsBundle = new Bundle();

        for (Map.Entry<String, Object> entry : customParams.toHashMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                customParamsBundle.putString(key, (String) value);
            } else if (value instanceof Integer) {
                customParamsBundle.putInt(key, (int) value);
            }  else if (value instanceof Boolean) {
                customParamsBundle.putBoolean(key, (boolean) value);
            }  else if (value instanceof Double) {
                customParamsBundle.putDouble(key, (double) value);
            } else if (value instanceof Float) {
                customParamsBundle.putFloat(key, (float) value);
            }
        }

        extras.putString(APPRUVE_API_TOKEN, apiToken);
        extras.putBoolean(IS_GHANA_ENABLED, isGhanaEnabled);
        extras.putBoolean(IS_NIGERIA_ENABLED, isNigeriaEnabled);
        extras.putBoolean(IS_KENYA_ENABLED, isKenyaEnabled);
        extras.putBundle(CUSTOM_PARAMS, customParamsBundle);
        
        verificationIntent.putExtras(extras);

        reactContext.startActivityForResult(verificationIntent, START_VERIFICATION_ACTIVITY_REQUEST, extras);
    }
}
