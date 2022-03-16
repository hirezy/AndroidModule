package me.shouheng.commons.tools.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.shouheng.commons.R;
import me.shouheng.commons.config.BaseConstants;
import me.shouheng.commons.view.activity.BaseActivity;

/**
 * Created by wang shouheng on 2017/12/5.*/
public class PermissionUtils {

    public static <T extends BaseActivity> void checkStoragePermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, BaseConstants.REQUEST_PERMISSION_STORAGE, callback);
    }

    public static <T extends BaseActivity> void checkPhonePermission(@NonNull T activity, OnGetPermissionCallback callback){
        checkPermission(activity, Manifest.permission.READ_PHONE_STATE, BaseConstants.REQUEST_PERMISSION_PHONE_STATE, callback);
    }

    public static <T extends BaseActivity> void checkLocationPermission(@NonNull T activity, OnGetPermissionCallback callback){
        checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, BaseConstants.REQUEST_PERMISSION_LOCATION, callback);
    }

    public static <T extends BaseActivity> void checkRecordPermission(@NonNull T activity, OnGetPermissionCallback callback){
        checkPermission(activity, Manifest.permission.RECORD_AUDIO, BaseConstants.REQUEST_PERMISSION_MICROPHONE, callback);
    }

    public static <T extends BaseActivity> void checkSmsPermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.SEND_SMS, BaseConstants.REQUEST_PERMISSION_SMS, callback);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public static <T extends BaseActivity> void checkSensorsPermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.BODY_SENSORS, BaseConstants.REQUEST_PERMISSION_SENSORS, callback);
    }

    public static <T extends BaseActivity> void checkContactsPermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.READ_CONTACTS, BaseConstants.REQUEST_PERMISSION_CONTACTS, callback);
    }

    public static <T extends BaseActivity> void checkCameraPermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.CAMERA, BaseConstants.REQUEST_PERMISSION_CAMERA, callback);
    }

    public static <T extends BaseActivity> void checkCalendarPermission(@NonNull T activity, OnGetPermissionCallback callback) {
        checkPermission(activity, Manifest.permission.READ_CALENDAR, BaseConstants.REQUEST_PERMISSION_CALENDAR, callback);
    }

    private static <T extends BaseActivity> void checkPermission(@NonNull T activity, @NonNull String permission, int requestCode, OnGetPermissionCallback callback) {
        activity.setOnGetPermissionCallback(callback);
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        } else {
            if (callback != null) {
                callback.onGetPermission();
            }
        }
    }

    public static String getPermissionName(Context context, int requestCode) {
        switch (requestCode){
            case BaseConstants.REQUEST_PERMISSION_STORAGE:
                return context.getString(R.string.permission_storage_permission);
            case BaseConstants.REQUEST_PERMISSION_LOCATION:
                return context.getString(R.string.permission_location_permission);
            case BaseConstants.REQUEST_PERMISSION_MICROPHONE:
                return context.getString(R.string.permission_microphone_permission);
            case BaseConstants.REQUEST_PERMISSION_PHONE_STATE:
                return context.getString(R.string.permission_phone_permission);
            case BaseConstants.REQUEST_PERMISSION_SMS:
                return context.getString(R.string.permission_sms_permission);
            case BaseConstants.REQUEST_PERMISSION_SENSORS:
                return context.getString(R.string.permission_sensor_permission);
            case BaseConstants.REQUEST_PERMISSION_CONTACTS:
                return context.getString(R.string.permission_contacts_permission);
            case BaseConstants.REQUEST_PERMISSION_CAMERA:
                return context.getString(R.string.permission_camera_permission);
            case BaseConstants.REQUEST_PERMISSION_CALENDAR:
                return context.getString(R.string.permission_calendar_permission);
        }
        return context.getString(R.string.permission_default_permission_name);
    }
}