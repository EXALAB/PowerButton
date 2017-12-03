package exa.open.pb;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TurnOffScreenActivity extends AppCompatActivity{

    //When user click on the power off button we should turn off the screen, so we use this activity to archive it

    //Administrator permission is needed to turn off screen

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Initialize the Administrator component

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminManager.class);

        //Always check before execute for safety
        if(devicePolicyManager.isAdminActive(componentName)){
            //looks like administrator permission is granted, so just turn off the screen
            devicePolicyManager.lockNow();
        }else{
            //Looks like administrator permission is not granted, so show the user current situation
            Toast.makeText(this, "Device administrator permission is not granted, the application could not perform requested action", Toast.LENGTH_LONG).show();
        }
        //After executing the task we want, this activity is no longer needed, so finish it
        finish();
    }
}
