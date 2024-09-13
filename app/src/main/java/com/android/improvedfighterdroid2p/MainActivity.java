package com.android.improvedfighterdroid2p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.Iterator;

import android.hardware.input.InputManager;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        packageManager = getPackageManager();
        showPopup();

        //sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    private void startService() {
        if (!isMyServiceRunning(Uart2PService.class)) {
            Log.i("Bremen79", "Service not running");
            Intent serviceIntent = new Intent(this, Uart2PService.class);
            Log.i("Bremen79", "Starting service");
            Toast.makeText(this, "ImprovedFighterDroid2P started.", Toast.LENGTH_SHORT).show();
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            Log.i("Bremen79", "Service already running");
            Toast.makeText(this, "ImprovedFighterDroid2P is already running.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Log.i("Bremen79", "Creating dialog");
        builder.setTitle("App Action");
        builder.setMessage("Do you want to install or uninstall the app?");

        builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.i("Bremen79", "Clicked install");
                disablePackages();
                startService();
                finish();
            }
        });

        builder.setNegativeButton("Uninstall", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.i("Bremen79", "Clicked uninstall");
                enablePackages();
                finish();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.i("Bremen79", "Clicked cancel");
                dialog.dismiss();
                finish();
            }
        });

        //AlertDialog dialog = builder.create();

        Log.i("Bremen79", "Showing dialog");
        builder.show();
    }

    private void disablePackages() {
        String[] packages = {"com.fjtech.ComAssistant", "com.bjw.ComAssistant"};
        for (String pkg : packages) {
            try {
                packageManager.setApplicationEnabledSetting(pkg, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0);
                Toast.makeText(this, "Disabled " + pkg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to disable " + pkg + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void enablePackages() {
        String[] packages = {"com.fjtech.ComAssistant", "com.bjw.ComAssistant"};
        for (String pkg : packages) {
            try {
                packageManager.setApplicationEnabledSetting(pkg, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
                Toast.makeText(this, "Enabled " + pkg, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to enable " + pkg + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android/5921190#5921190
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        Iterator<ActivityManager.RunningServiceInfo> it = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE).iterator();
        while (it.hasNext()) {
            if (serviceClass.getName().equals(it.next().service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*@Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }*/

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.d("Bremen79", "Main Activity onDestroy");
        super.onDestroy();
    }
}
