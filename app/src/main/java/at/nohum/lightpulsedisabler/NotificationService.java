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
    private static final String WAKE_GESTURE_ENABLED = "wake_gesture_enabled";

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

        Log.i(TAG, alarmOnly ? "alarm only filter is set" : "alarm only filter is NOT set");
        Settings.System.putInt(getContentResolver(), NOTIFICATION_LIGHT_PULSE, alarmOnly ? 0 : 1);
        Settings.System.putInt(getContentResolver(), WAKE_GESTURE_ENABLED, alarmOnly ? 0 : 1);
    }
}
