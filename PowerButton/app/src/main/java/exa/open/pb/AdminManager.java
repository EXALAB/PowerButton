package exa.open.pb;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class AdminManager extends DeviceAdminReceiver{

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "When disabled, you will no longer able to lock screen with this app.";
    }
}
