package com.test.media_projection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
  //com.google.android.youtube
  private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(YDGooglePlayUtils.isInstalledViaGooglePlay(MainActivity.this)){

            startProjection();

        }
        else{

        }


    }
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new ActivityNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                openApp(MainActivity.this,"com.google.android.youtube");

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startService(ScreenCaptureService.getStartIntent(MainActivity.this, resultCode, data));
                        finish();
                    }
                }, 5000);

            }
        }
    }

    private void startProjection() {
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    private void stopProjection() {
        startService(ScreenCaptureService.getStopIntent(this));
    }


    private void requestPermission(ArrayList<String> list)
    {
        try {
            if(list == null) {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
                ActivityCompat.requestPermissions(MainActivity.this, info.requestedPermissions, PERMISSION_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, (String[])list.toArray(), PERMISSION_REQUEST_CODE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    try {
                        int index = 0;
                        ArrayList<String> temp = new ArrayList<>();
                        for (int result : grantResults) {
                            if (result == PackageManager.PERMISSION_DENIED) {
                                temp.add(permissions[index]);
                                //    Toast.makeText(webview_test.this, "You must Allow the Permission " + permissions[index], Toast.LENGTH_LONG).show();
                            }
                            index++;
                        }

                        if (temp.size() > 0)
                            requestPermission(temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}