package com.teamxod.unilink;


import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;

public class LoadMapIntentService extends IntentService {

    private final int SUCCESS_MSG = 1;

    private final int FAILURE_MSG = 0;

    private final String INPUT_MSG = "input";

    private Handler handler;

    private String location;

    public LoadMapIntentService() {
        super("SimpleIntentService");
    }

    public LoadMapIntentService(String location, Handler handler){
        super("SimpleIntentService");
        this.location = location;
        this.handler = handler;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String msg = intent.getStringExtra(INPUT_MSG);
        SystemClock.sleep(30000); // 30 seconds
        String resultTxt = msg + " "
                + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis());
    }
}
