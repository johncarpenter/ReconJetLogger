package com.twolinessoftware.reconfirebaselogger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

public class MainActivity extends Activity implements ServiceConnection
{
    private Intent mService;
    private TextView topText;
    private TextView botText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topText = (TextView) findViewById(R.id.top_text);
        botText = (TextView) findViewById(R.id.bottom_text);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Bind to ActivityService
        mService = new Intent(this, ActivityService.class);
        bindService(mService, this, 0);

        // If the service was running before we launched this application
        // then we stop the service from running in the background.
        if(ActivityService.isRunning())
        {
            stopService(mService);
            topText.setText("Service: STOPPED");
            topText.setTextColor(0xFFFF5555);
            botText.setText("(Run the app again to START the service)");
        }
        else // If no activity status service is running, we start one.
        {
            startService(mService);
            topText.setText("Service: STARTED");
            topText.setTextColor(0xFF55FF55);
            botText.setText("(Run the app again to STOP the service)");
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service){ }

    @Override
    public void onServiceDisconnected(ComponentName name){ }
}
