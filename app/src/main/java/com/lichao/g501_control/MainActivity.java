package com.lichao.g501_control;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.pos.api.util.PosUtil401;
import com.telpo.tps550.api.util.ShellUtils;

public class MainActivity extends AppCompatActivity {
    SeekBar ledSeekBar;
    static TextView sensorStatus = null;
    TextView cardNum,status;
    Intent sensorIntent;
    long readCardNum;
    boolean is950readingNFC;
    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 2:

                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sensorIntent = new Intent(this, ProximityService.class);
        if("TPS980Q".equals(ShellUtils.execCommand("getprop ro.internal.model", false).successMsg)){
            NfcManager mNfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
            mNfcAdapter = mNfcManager.getDefaultAdapter();
            if (mNfcAdapter == null) {
                cardNum.setText("not support nfc");
            } else if ((mNfcAdapter != null) && (!mNfcAdapter.isEnabled())) {
                cardNum.setText("nfc not work");
            }
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        }
    }

    private void initView(){
        ledSeekBar = findViewById(R.id.ledSeekBar);
        sensorStatus = findViewById(R.id.sensorStatus);
        cardNum = findViewById(R.id.cardNum);
        status = findViewById(R.id.status);
        ledSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                PosUtil401.setLedBright(arg1);
                status.setText("bright level:"+arg1);
            }
        });
    }

    public void cameraPowerOn(View view){
        status.setText("cameraPowerOn:"+PosUtil401.setCameraPower(1));
    }

    public void cameraPowerOff(View view){
        status.setText("cameraPowerOff:"+PosUtil401.setCameraPower(0));
    }

    public void OTGCTRLPowerOn(View view){
        status.setText("OTGCTRLPowerOn:"+PosUtil401.setOTGPower(1));
    }

    public void OTGCTRLPowerOff(View view){
        status.setText("OTGCTRLPowerOff:"+PosUtil401.setOTGPower(0));
    }

    public void LANPowerOn(View view){
        status.setText("LANPowerOn:"+PosUtil401.setLanPower(1));
    }

    public void LANPowerOff(View view){
        status.setText("LANPowerOff:"+PosUtil401.setLanPower(0));
    }

    public void openSensor(View view){
        if(sensorIntent!=null)
            startService(sensorIntent);
    }

    public static void setSensorText(String content){
        if(sensorStatus!=null)
            sensorStatus.setText(content);
    }

    public void closeSensor(View view){
        if(sensorIntent!=null)
            stopService(sensorIntent);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        sensorStatus.setText("");
                    }
                });
            }
        }).start();
    }

    public void sendRS485(View view){
        status.setText("RS485 send mode:"+PosUtil401.setRs485Status(1));
    }

    public void receiveRS485(View view){
        status.setText("RS485 receive mode:"+PosUtil401.setRs485Status(0));
    }

    public void sendWG26(View view){
        if(readCardNum>0)
            if(Long.toBinaryString(readCardNum).length()>24){
                Toast.makeText(MainActivity.this, "cardNum lengh > 24", Toast.LENGTH_SHORT).show();
            }else{
                PosUtil401.getWg26Status(readCardNum);
            }
    }

    public void sendWG34(View view){
        if(readCardNum>0)
            if(Long.toBinaryString(readCardNum).length()>32){
                Toast.makeText(MainActivity.this, "cardNum lengh > 32", Toast.LENGTH_SHORT).show();
            }else{
                PosUtil401.getWg34Status(readCardNum);
            }
    }

    public void openRelay(View view){
        status.setText("open Relay:"+PosUtil401.setRelayPower(1));
    }

    public void closeRelay(View view){
        status.setText("close Relay:"+PosUtil401.setRelayPower(0));
    }
}
