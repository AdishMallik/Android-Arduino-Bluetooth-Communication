/*
   Copyright [2018] [Adish Mallik]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package com.digitaldesigner.adish.ledbluetooth;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import java.util.Set;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_ADDRESS = "device_address";// to store the MAC address of the connected device
    private Set<BluetoothDevice> PairedDevices;
    private BluetoothAdapter myBluetooth = null; //represents the local device's bluetooth adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = (Button) findViewById(R.id.search);// call the button widget
        final   ListView Devicelist = (ListView) findViewById(R.id.list_devices);//call the listview widget


        myBluetooth = BluetoothAdapter.getDefaultAdapter();// calls the bluetooth adapter of the local device
        //checks if bluetooth adapter is present
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device not available", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (myBluetooth.isEnabled()) {
            } else {
                Intent turnBtn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//if bluetooth not enabled,asks user to turn on the bluetooth
                startActivityForResult(turnBtn, 1);
            }
        }
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             ArrayAdapter  adapter = PairedDevicesList();
                Devicelist.setAdapter(adapter);
                Devicelist.setOnItemClickListener(myListClickListener);//Method call when a device from the list is clicked
            }
        });// on clicking the button, it calls a function to show the paired devices



    }

    //function to show the paired devices in the listview
    private ArrayAdapter<String> PairedDevicesList() {
        PairedDevices = myBluetooth.getBondedDevices();// gives all the paired Bluetooth devices
        ArrayList<String> arrayList = new ArrayList<String>();// create a arraylist of type String

        if (PairedDevices.size() > 0)//if there are paired devices
        {
            for (BluetoothDevice bt : PairedDevices) {
                arrayList.add(bt.getName() + "\n" + bt.getAddress());//stores the bluetooth device's name and MAC address in a arraylist
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Devices Found", Toast.LENGTH_SHORT).show();
        }
        //declare an Array Adapter;it accepts three parameters: context(activity instance), XML item layout and the array of data
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //show vertical list of scrollable item using a listview; the data is populated using an Adapter
        return adapter;


    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            //get the remote device's MAC address; the last 17 characters
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            //starts the next activity
            Intent i = new Intent(MainActivity.this, Main2Activity.class);
            i.putExtra(EXTRA_ADDRESS , address);// this will be received by the Main2Activity class
            startActivity(i);// start the activity
        }
    };
}
