package at.nohum.lightpulsedisabler;

import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "LightPulseService";

    /**
     * Hidden constant, copied from {@link android.provider.Settings.Secure}
     */
    private static final String NOTIFICATION_LIGHT_PULSE = "notification_light_pulse";

    /**
     * Hidden constant, copied from {@link android.provider.Settings.Secure}
     */
    private static final String DOZE_ENABLED = "doze_enabled";

    private int previousDozeValue;

    public NotificationService() {
    }

    @Override
    public void onListenerConnected() {
        Log.d(TAG, "onListenerConnected");
        handleFilterChange(getCurrentInterruptionFilter());
    }

    @Override
    public void onInterruptionFilterChanged(int interruptionFilter) {
        handleFilterChange(interruptionFilter);
    }

    private void handleFilterChange(int filter) {
        boolean alarmOnly = filter == NotificationListenerService.INTERRUPTION_FILTER_ALARMS;
        boolean setDoze = true;

        Log.i(TAG, alarmOnly ? "alarm only filter is set" : "alarm only filter is NOT set");

        if (alarmOnly) {
            try {
                previousDozeValue = Settings.System.getInt(getContentResolver(), DOZE_ENABLED);
            } catch (Exception e) {
                setDoze = false;
                // pass
            }
        }

        Settings.System.putInt(getContentResolver(), NOTIFICATION_LIGHT_PULSE, alarmOnly ? 0 : 1);

        if (setDoze) {
            Settings.System.putInt(getContentResolver(), DOZE_ENABLED, alarmOnly ? 0 : previousDozeValue);
        }
    }
}
