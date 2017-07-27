package com.digitaldesigner.adish.ledbluetooth;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.io.IOException;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {
    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    private ProgressDialog progress;// used to show the progress of a task
    BluetoothAdapter mybluetooth = null;// represents the local device's bluetooth adapter
    BluetoothSocket btSocket = null;//socket is the endpoint of communication between two machines; we are creating a bluetooth socket to communicate with the device
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //calling the widgets
        setContentView(R.layout.activity_main2);
        btnOn = (Button) findViewById(R.id.led_on);
        btnOff = (Button) findViewById(R.id.led_off);
        btnDis = (Button) findViewById(R.id.disconnect);
        brightness = (SeekBar) findViewById(R.id.seekBar7);
        Intent newIntent = getIntent();// used to receive data from another activity; here we are receiving the MAC address of the connected device
        address = newIntent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        new connectBluetooth().execute();//calling function to connect
        //On click the buttons call the respective methods
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnLED();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffLED();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true)//fromUser --> callback that notifies client when the progress level has changed
                {
                    try {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());//send value of change to the bluetooth module through socket
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();//disconnects bluetooth
                finish();
            } catch (IOException e) {
                msg("Error");// displays "Error" message
            }
        }
    }// function to disconnect bluetooth and close the connection

    private void turnOffLED() {
        if (btSocket != null){
            try {
                btSocket.getOutputStream().write("off".toString().getBytes());//sends off to the bluetooth module
                msg("Turned Off the LED!");
            } catch (IOException e) {
                msg("Error");//calls msg fuction to display "Error"
            }
        }
    }//function to turn off the LED

    private void turnOnLED() {
         if (btSocket != null){
             try {
                 btSocket.getOutputStream().write("on".toString().getBytes());//sends "on" to the bluetooth module
                 msg("Turned On the LED!");
             } catch (IOException e) {
                 msg("Error");//display msg "Error"
             }
         }
    }//function to turn the LED on

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();//displays a message on screen
    }

    private class connectBluetooth extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Main2Activity.this,"Connecting", "Please Wait");// show a progress of connection
        }
        @Override
        protected Void doInBackground(Void... devices) //while the progressdialog is shown, connection is done in background
        {
            try {
                if (btSocket == null || isBtConnected)//when bluetooth device not connected
                {
                    mybluetooth = BluetoothAdapter.getDefaultAdapter();//getthe local device's bluetooth adapter
                    BluetoothDevice device = mybluetooth.getRemoteDevice(address);// connects to the device's address
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);// creates a connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)// after executing doInBackground, it checks if everything went fine]
        {
            super.onPostExecute(result);
            if (!ConnectSuccess)
            {
                msg("Connection Failed! Try Again!");
                finish();
            }
            else
            {
                msg("Connected");
                isBtConnected = true;
            }
            progress.dismiss();//closes progress bar

        }
    }

}



