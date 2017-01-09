package me.edeoma.kursovik;

import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity implements  View.OnTouchListener {

    Button forwardbut, backwardbut, leftbut, rightbut;
    TextView textfield;
    int distance;
    BluetoothSocket clientSocket;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forwardbut = (Button) findViewById(R.id.buttonForward);
        backwardbut = (Button) findViewById(R.id.buttonBack);
        leftbut = (Button) findViewById(R.id.buttonLeft);
        rightbut = (Button) findViewById(R.id.buttonRight);

        textfield = (TextView) findViewById(R.id.textView);

        forwardbut.setOnTouchListener(this);
        backwardbut.setOnTouchListener(this);
        leftbut.setOnTouchListener(this);
        rightbut.setOnTouchListener(this);

        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        try {
            BluetoothDevice device = bluetooth.getRemoteDevice("98:D3:31:20:44:7B");
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    char value = 0;
    private Handler mHandler;

    public boolean onTouch(View v, MotionEvent event) {
        if (v == forwardbut)
            value = 'W';
        if (v == backwardbut)
            value = 'S';
        if (v == leftbut)
            value = 'A';
        if (v == rightbut)
            value = 'D';
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mHandler != null) return true;
                mHandler = new Handler();
                mHandler.postDelayed(mAction, 100);
                break;
            case MotionEvent.ACTION_UP:
                if (mHandler == null) return true;
                mHandler.removeCallbacks(mAction);
                mHandler = null;
                break;
        }
//        try {
//            OutputStream outStream = clientSocket.getOutputStream();
//
//
//            SystemClock.sleep(100);
//            outStream.write(value);
//
//        } catch (IOException e) {
//            //Если есть ошибки, выводим их в лог
//            Log.d("BLUETOOTH", e.getMessage());
//        }
        return false;
    }

    Runnable mAction = new Runnable() {
        @Override public void run() {
            byte[] buffer =new byte[256];
            int bytes;
            OutputStream outStream = null;
            InputStream inputStream = null;
            try {
                inputStream = clientSocket.getInputStream();
                outStream = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bytes=inputStream.read(buffer);
                String message=new String(buffer,0,bytes);
                String[] msg=message.split("\n");
                int len;
                if(msg.length>1) len=msg.length-2;
                else len=0;
                try {
                    distance=Integer.parseInt(msg[len].replaceAll("[\\D]", ""));
                }
                catch (NumberFormatException nfe){
                    textfield.setText("Can't convert");
                }
                if (distance<10 && value=='W')
                    textfield.setText("Can't move further");
                else {
                    outStream.write(value);
                    textfield.setText("");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler.postDelayed(this, 100);
        }
    };
}