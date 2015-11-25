package at.nohum.lightpulsedisabler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * See https://stackoverflow.com/questions/13045283/write-secure-settings-permission-error-even-when-added-in-manifest
 * or: http://www.addictivetips.com/mobile/how-to-install-any-app-as-system-app-on-android/
 */
public class InstallActivity extends Activity {

    /**
     * Also to be found in {@link android.provider.Settings.Secure}
     */
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            showServiceEnableDialogIfNecessary();
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage("This app needs to be install as a system app. Please move it to the /system/app folder!")
                .setPositiveButton(android.R.string.ok, null).show();
    }

    private boolean isServiceEnabled() {
        final String flatList = Settings.Secure.getString(getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);

        if (TextUtils.isEmpty(flatList)) {
            return false;
        }

        String pkgName = getPackageName();
        final String[] names = flatList.split(":");

        for (String name : names) {
            final ComponentName componentName = ComponentName.unflattenFromString(name);

            if (pkgName.equals(componentName.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    private void showServiceEnableDialogIfNecessary() {
        if (isServiceEnabled()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage("You have to grant notification access to this app")
                .setTitle("Notification access")
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}
