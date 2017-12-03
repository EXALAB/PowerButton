package exa.open.pb;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

//Extends AppCompatActivity and implements View.OnClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Int value for requesting administrator permission

    static final int RESULT_ENABLE = 1;

    //Administrator permission is needed to turn off screen

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    //The UI content that interact with code

    ViewFlipper viewFlipper;
    Button button;
    Button button2;
    Button button3;
    Button button4;

    //value to determine View Flipper state
    protected static boolean isViewFlipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        //Initialize the Administrator component

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminManager.class);

        //Initialize the UI so we can manipulate it

        viewFlipper = findViewById(R.id.viewFlipper);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        //Set the onClickListener for each button
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        //Always false at the start of activity
        isViewFlipped = false;
    }
    @Override
    public void onResume(){
        super.onResume();

        //We can call this in onCreate(), since user can always pause the activity, disable the administrator permission in the settings and resume this activity

        if(devicePolicyManager.isAdminActive(componentName)){
            //looks like administrator permission is granted, so call viewFlipper.showNext()

            //But, if user resume the activity multiple times, and the permssion is always granted, should we flip the activity multiple times?
            //Certainly not, so we must always record the state of viewFlipper

            //View is not flipped, so we flip it
            if(!isViewFlipped){
                //View is now flipped
                isViewFlipped = true;
                viewFlipper.showNext();
            }
        }else{
            //Administrator permission is not granted, so we check if flipped
            if(isViewFlipped){
                //Looks like view is flipped, so we revert it to original state
                isViewFlipped = false;
                viewFlipper.showPrevious();
            }
        }
    }
    @Override
    public void onClick(View v) {

        //Use switch for different button
        switch(v.getId()){

            case R.id.button:
                //button1 clicked, so we request administrator permission
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.please_grant);
                startActivityForResult(intent, RESULT_ENABLE);

                //always break after executing code
                break;
            case R.id.button2:
                //button2 clicked, so we remove administrator permission
                devicePolicyManager.removeActiveAdmin(componentName);
                //And then prompt user to uninstall
                Uri uri = Uri.parse("package:" + getPackageName());
                Intent intent2 = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, uri);
                startActivity(intent2);

                //always break after executing code
                break;

            case R.id.button3:
                //button3 clicked, sometimes we need to promote our apps too, if you are looking at these please consider take a look of our apps at play store
                Uri uri2 = Uri.parse("market://dev?id=8450947575366721624");
                Intent intent3 = new Intent(Intent.ACTION_VIEW, uri2);
                if(Build.VERSION.SDK_INT >= 21){
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }else{
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                }
                try{
                    startActivity(intent3);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=8450947575366721624")));
                }

                //always break after executing code
                break;

            case R.id.button4:
                //button4 clicked, sometimes we need to promote our apps too, if you are looking at these please consider take a look of our apps at play store
                Uri uri3 = Uri.parse("market://dev?id=8450947575366721624");
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri3);
                if(Build.VERSION.SDK_INT >= 21){
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }else{
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                }
                try{
                    startActivity(intent4);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=8450947575366721624")));
                }

                //always break after executing code
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //Make sure its the request we sent
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    //The permission has granted, so we show another layout that user can disable the permission
                    isViewFlipped = true;
                    viewFlipper.showNext();
                }else{
                    //Permission denied
                    Log.i("info", "Request Denied");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
