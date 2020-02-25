package exa.open.pb;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget extends AppWidgetProvider{

    //Intent string, dont forget to register it in manifest
    public static final String ACTION_WIDGET_CLICKED = "ActionWidgetClicked";

    //Administrator component
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {

            //The int value
            int appWidgetId = appWidgetIds[i];

            //Setup the removeView
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            //Setup intent
            Intent mIntent = new Intent(context, Widget.class);

            //Set the action
            mIntent.setAction(ACTION_WIDGET_CLICKED);

            //Setup PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);

            //Set the on click listener to the button
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            //Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        //Check if the intent we received is the widget clicked
        if(intent.getAction().equals(ACTION_WIDGET_CLICKED)){
            //Initialize the administrator component here because we need to use it here only
            devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = new ComponentName(context, AdminManager.class);

            //Check if administrator permission granted first before executing, because we don't know if user would disable it in the settings
            if(devicePolicyManager.isAdminActive(componentName)){
                //Looks like user has granted administrator permission, we just need to lock the screen
                devicePolicyManager.lockNow();
            }else{
                //Looks like administrator permission is not granted, so show the user current situation
                Toast.makeText(context, "Device administrator permission is not granted, the application could not perform requested action", Toast.LENGTH_LONG).show();
            }
        }

        //It is necessary to call super here
        super.onReceive(context, intent);
    }
}
