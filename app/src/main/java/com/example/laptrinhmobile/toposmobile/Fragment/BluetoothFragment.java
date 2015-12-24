package com.example.laptrinhmobile.toposmobile.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.AsyncTask.SyncHinhThucThanhToan_AsyncTask;
import com.example.laptrinhmobile.toposmobile.R;
import com.example.laptrinhmobile.toposmobile.database.SQLController;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluetoothFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BluetoothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOCATION = "BluetoothFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<BluetoothDevice> arr_device ;

    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetoothFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothFragment newInstance(String param1, String param2) {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Whenever a remote Bluetooth device is found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
//                adapter.add(bluetoothDevice);
                arr_device.add(bluetoothDevice);
                adapter.add(bluetoothDevice.getName() + "\n"  + bluetoothDevice.getAddress());
            }
        }
    };
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int DISCOVERABLE_DURATION = 300;

    SQLController sqlController;
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

    public BluetoothFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sqlController = new SQLController(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        switch_turn_bluetooth = (Switch) view.findViewById(R.id.switch_turn_bluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        switch_turn_bluetooth.setChecked(false);
        switch_turn_bluetooth.setTextOff("Tắt");
        switch_turn_bluetooth.setTextOn("Mở");
        value = "";
        listBluetooth = (ListView) view.findViewById(R.id.listBluetooth);
        txtViewScanDevice = (TextView) view.findViewById(R.id.txtViewScanDevice);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(bluetoothAdapter.isEnabled()) switch_turn_bluetooth.setEnabled(true);
        else switch_turn_bluetooth.setEnabled(false);
        adapter = new ArrayAdapter
                (getActivity(),android.R.layout.simple_list_item_1);
        arr_device = new ArrayList<>();
        listBluetooth.setAdapter(adapter);
        listBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice temp = arr_device.get(position);
//                Log.d(LOCATION,temp.getAddress());
//                Log.d(LOCATION,temp.getName());
                Dialog show = showDialogSaveBluetooth(temp);
                show.show();
            }
        });
        switch_turn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_turn_bluetooth.isChecked()) { // to turn on bluetooth
                    bluetoothAdapter.enable();
                    Toast.makeText(getActivity(), getString(R.string.bluetooth_notice_turn_on),
                            Toast.LENGTH_SHORT).show();
                } else { // Turn off bluetooth
                    bluetoothAdapter.disable();
                    adapter.clear();
                    Toast.makeText(getActivity(), getString(R.string.bluetooth_notice_turn_off),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtViewScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    // A dialog will appear requesting user permission to enable Bluetooth
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.bluetooth_status_is_enable) +
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
        getActivity().registerReceiver(broadcastReceiver, filter);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    protected void discoverDevices(){
        // To scan for remote Bluetooth devices
        if (bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getActivity(),getString(R.string.bluetooth_scan_other_device),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(),getString(R.string.bluetooth_err_scan_other_device),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
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
                Toast.makeText(getActivity(), getString(R.string.bluetooth_notice_turn_on),
                        Toast.LENGTH_SHORT).show();
            } else { // RESULT_CANCELED as user refuse or failed
                Toast.makeText(getActivity(), getString(R.string.bluetooth_notice_turn_off),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Dialog showDialogSaveBluetooth(final BluetoothDevice bluetoothDevice) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Kết nối bluetooth");
        dialog.setContentView(R.layout.connect_bluetooth_device);
        dialog.setCancelable(false);
        TextView txtNameDevice = (TextView) dialog.findViewById(R.id.txtViewNameDevice);
        TextView txtViewAddressDevice = (TextView) dialog.findViewById(R.id.txtViewAddressDevice);
        txtNameDevice.setText(bluetoothDevice.getName());
        txtViewAddressDevice.setText(bluetoothDevice.getAddress());

        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        Button btnTestPrint = (Button) dialog.findViewById(R.id.btnTestPrint);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnTestPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"Test Printer",Toast.LENGTH_SHORT).show();
                Thread connectThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            UUID uuid = bluetoothDevice.getUuids()[0]
                                    .getUuid();
//                            UUID uuid = UUID.fromString(bluetoothDevice.getAddress());
                            btsocket = bluetoothDevice
                                    .createRfcommSocketToServiceRecord(uuid);
                            btsocket.connect();
                            print_bt();
                        } catch (IOException ex) {
//                            getActivity().runOnUiThread(socketErrorRunnable);
                            try {
                                btsocket.close();
                            } catch (IOException e) {
                                Log.d(LOCATION,"IOException: " + e.getMessage());
                                e.printStackTrace();
                            }
                            btsocket = null;
                            return;
                        } finally {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
//                                    finish();
                                }
                            });
                        }
                    }
                });
                connectThread.start();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlController.open();
                try {
                    if (sqlController.checkBluetooth(bluetoothDevice.getName())) {
                        sqlController.updateAllType();
                        sqlController.updateDevice(bluetoothDevice.getName());
                    } else sqlController.insertBluetooth(bluetoothDevice);
                } catch (SQLException sql) {
                    Log.d(LOCATION, sql.getMessage());
                }
                sqlController.close();
                Toast.makeText(getActivity().getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
//        dialog.show();
        return dialog;
    }
    private void print_bt() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            btoutputstream = btsocket.getOutputStream();

//            String sendData = "Test TP POS \r \n" ;
            String BILL = generalStrBill();
//            char[] str = BILL.toCharArray();
//            String temp ="";
//            for(int i=0;i<str.length;i++) {
//                temp += unicodeEscaped(str[i]);
////                Log.d(LOCATION,temp);
//            }
//            Log.d(LOCATION,"Last: " +temp);
//            btoutputstream.write(temp.getBytes("UTF-8"));
            settingForPrint(btoutputstream);
//            btoutputstream.write(sendData.getBytes());
            btoutputstream.write(BILL.getBytes());
            btoutputstream.flush();
        } catch (IOException e) {
            Log.d(LOCATION,"IOException " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void settingForPrint(OutputStream os) {
        try {
            int gs = 29;
            os.write(intToByteArray(gs));
            int h = 104;
            os.write(intToByteArray(h));
            int n = 162;
            os.write(intToByteArray(n));

            // Setting Width
            int gs_width = 29;
            os.write(intToByteArray(gs_width));
            int w = 119;
            os.write(intToByteArray(w));
            int n_width = 2;
            os.write(intToByteArray(n_width));

            // Print BarCode
            int gs1 = 29;
            os.write(intToByteArray(gs1));
            int k = 107;
            os.write(intToByteArray(k));
            int m = 73;
            os.write(intToByteArray(m));
//            String barCodeVal = "ASDFC028060000005";// "HELLO12345678912345012";
//            Log.d(LOCATION,"Barcode Length : " + barCodeVal.length());
//            int n1 = barCodeVal.length();
//            os.write(intToByteArray(n1));
        } catch (IOException IOEx) {
            Log.d(LOCATION, "IOException - settingForPrint: " + IOEx.getMessage());
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
//        for (int k = 0; k < b.length; k++) {
//            Log.d(LOCATION,"Values - " + value +" Selva  [" + k + "] = " + "0x"
//                    + UnicodeFormatter.byteToHex(b[k]));
//        }
        return b[3];
    }

    private String generalStrBill() {
        String result;

        result = "Simply Mart \n" +
                " 273 Pham Van Chieu \n" +
                " HOA DON BAN LE \n" +
                " ---o0o--- \n" +
                " Liên: 1 \n";
        result += "\nInvoice No: ABCDEF28060000005" + "    "
                + "04-08-2011\n";
        result = result
                + "-----------------------------------------";
        result = result + "\n\n";
        result = result + "Total Qty:" + "      " + "2.0\n";
        result = result + "Total Value:" + "     " + "17625.0\n";
        result = result
                + "-----------------------------------------\n";
        return result;
    }
}
