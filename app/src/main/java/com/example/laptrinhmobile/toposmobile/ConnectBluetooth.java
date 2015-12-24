package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ConnectBluetooth extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Whenever a remote Bluetooth device is found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(bluetoothDevice.getName() + "\n"  + bluetoothDevice.getAddress());
            }
        }
    };

    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int DISCOVERABLE_DURATION = 300;

    private BluetoothAdapter bluetoothAdapter;
    ListView listBluetooth;
    TextView txtViewScanDevice;
    String value;
    private ArrayAdapter adapter;
    private Switch switch_turn_bluetooth;

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        switch_turn_bluetooth = (Switch) findViewById(R.id.switch_turn_bluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        switch_turn_bluetooth.setChecked(false);
        switch_turn_bluetooth.setTextOff("Tắt");
        switch_turn_bluetooth.setTextOn("Mở");
        value = "";
    }
    @Override
    protected void onResume() {
        super.onResume();
        listBluetooth = (ListView) findViewById(R.id.listBluetooth);
        adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1);
        listBluetooth.setAdapter(adapter);
        switch_turn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_turn_bluetooth.isChecked()) { // to turn on bluetooth
                    bluetoothAdapter.enable();
                    Toast.makeText(getApplicationContext(), getString(R.string.bluetooth_notice_turn_on),
                            Toast.LENGTH_SHORT).show();
                } else { // Turn off bluetooth
                    bluetoothAdapter.disable();
                    adapter.clear();
                    Toast.makeText(getApplicationContext(), getString(R.string.bluetooth_notice_turn_off),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtViewScanDevice = (TextView) findViewById(R.id.txtViewScanDevice);
        txtViewScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    // A dialog will appear requesting user permission to enable Bluetooth
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.bluetooth_status_is_enable) +
                                    "\n" + getString(R.string.bluetooth_scan_other_device),
                            Toast.LENGTH_SHORT).show();
                    // To discover remote Bluetooth devices
                    discoverDevices();
                    // Make local device discoverable by other devices
                    makeDiscoverable();
                }
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);

        listBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    protected void discoverDevices(){
        // To scan for remote Bluetooth devices
        if (bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(),getString(R.string.bluetooth_scan_other_device),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),getString(R.string.bluetooth_err_scan_other_device),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }
    protected void makeDiscoverable(){
        // Make local device discoverable
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.bluetooth_notice_turn_on),
                        Toast.LENGTH_SHORT).show();
            } else { // RESULT_CANCELED as user refuse or failed
                Toast.makeText(getApplicationContext(), getString(R.string.bluetooth_notice_turn_off),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void IntentPrint(String txtvalue) {

        byte[] buffer = txtvalue.getBytes();
        byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
        PrintHeader[3]=(byte) buffer.length;
        InitPrinter();
        if(PrintHeader.length>128) {
            value+="\nValue is more than 128 size\n";
            txtViewScanDevice.setText(value);
        } else {
            try {
                for(int i=0;i<=PrintHeader.length-1;i++) {
                    mmOutputStream.write(PrintHeader[i]);
                }
                for(int i=0;i<=buffer.length-1;i++) {
                    mmOutputStream.write(buffer[i]);
                }
                mmOutputStream.close();
                mmSocket.close();
            } catch(Exception ex) {
                value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
                txtViewScanDevice.setText(value);
            }
        }
    }
    public void InitPrinter() {
        try {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0) {
                for(BluetoothDevice device : pairedDevices) {
                    if(device.getName().equals("Your Device Name")) //Note, you will need to change this to match the name of your device {
                        mmDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                //Method m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
                //mmSocket = (BluetoothSocket) m.invoke(mmDevice, uuid);
            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

                bluetoothAdapter.cancelDiscovery();
                if(mmDevice.getBondState()==2) {
                    mmSocket.connect();
                    mmOutputStream = mmSocket.getOutputStream();
                } else {
                    value+="Device not connected";
                    txtViewScanDevice.setText(value);
                }
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        } catch(Exception ex) {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            txtViewScanDevice.setText(value);
        }
    }
    public void showNotice (ArrayList<String> str) {
        String outStr = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for(int i=0;i<str.size();i++) {
            if(i == str.size()-1) {
                outStr += str.get(i) ;
            }
            else outStr += str.get(i) + "\n";
        }
        builder.setMessage(outStr);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
