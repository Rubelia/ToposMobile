package com.example.laptrinhmobile.toposmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laptrinhmobile.toposmobile.Fragment.BluetoothFragment;
import com.example.laptrinhmobile.toposmobile.Fragment.ConfigurationFragment;
import com.example.laptrinhmobile.toposmobile.Fragment.ServerSettingFragment;
import com.example.laptrinhmobile.toposmobile.Fragment.SettingListFragment;
import com.example.laptrinhmobile.toposmobile.Fragment.SyncSettingFragment;
import com.example.laptrinhmobile.toposmobile.Fragment.SynchronizationFragment;

public class Setting extends Activity implements SettingListFragment.ListFragmentItemClickListener,
        ServerSettingFragment.OnFragmentInteractionListener,
        ConfigurationFragment.OnFragmentInteractionListener,
        SyncSettingFragment.OnFragmentInteractionListener,
        SynchronizationFragment.OnFragmentInteractionListener,
        ConfigurationFragment.ButtonInFragmentClickListener,
        BluetoothFragment.OnFragmentInteractionListener
{

    int prePos =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    @Override
    public void onButtonFragmentClick(int idBtn) {
        int orientation = getResources().getConfiguration().orientation;
        /** Portrait Mode or Square mode */
        if(orientation == Configuration.ORIENTATION_LANDSCAPE ){
            /** Getting the fragment manager for fragment related operations */

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment prevFrag = fragmentManager.findFragmentByTag(getFragment(prePos));
            /** Remove the existing detailed fragment object if it exists */
            if(prevFrag!=null)
                fragmentTransaction.remove(prevFrag);
            /** Getting the existing detailed fragment object, if it already exists.
             *  The fragment object is retrieved by its tag name             * */
            switch (idBtn) {
                case R.id.btnPrinterSetting:
                    BluetoothFragment bluetoothFragment = new BluetoothFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, bluetoothFragment, "com.example.laptrinhmobile.toposmobile.Fragment.BluetoothFragment");
                    /** Adding this transaction to backstack */
                    prePos = 0;
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case 1:
                    ConfigurationFragment configurationFragment = new ConfigurationFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, configurationFragment, getFragment(1));
                    /** Adding this transaction to backstack */
                    prePos = 0;
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case 2:
                    SyncSettingFragment fragment = new SyncSettingFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, fragment, getFragment(2));
                    /** Adding this transaction to backstack */
                    prePos = 0;
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case 3:
                    SynchronizationFragment test = new SynchronizationFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, test, getFragment(3));
                    /** Adding this transaction to backstack */
                    prePos = 0;
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                default:
                    break;
            }
        } else {
            switch (idBtn) {
                case R.id.btnPrinterSetting:
                    Intent view_server_setting = new Intent(getActivity(0));
//                    startActivity(view_server_setting);
                case 1:
                    Intent view_configuration = new Intent(getActivity(1));
//                    startActivity(view_configuration);
                case 2:
                    Intent view_sync_setting = new Intent(getActivity(2));
//                    startActivity(view_sync_setting);
                case 3:
                    Intent view_synchronization = new Intent(getActivity(3));
//                    startActivity(view_synchronization);
                default:
                    break;
            }
        }
    }

    @Override
    public void onListFragmentItemClick(int position) {
        int orientation = getResources().getConfiguration().orientation;
        /** Portrait Mode or Square mode */
        if(orientation == Configuration.ORIENTATION_LANDSCAPE ){
            /** Getting the fragment manager for fragment related operations */

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment prevFrag = fragmentManager.findFragmentByTag(getFragment(prePos));
            /** Remove the existing detailed fragment object if it exists */
            if(prevFrag!=null)
                fragmentTransaction.remove(prevFrag);
            /** Getting the existing detailed fragment object, if it already exists.
             *  The fragment object is retrieved by its tag name             * */
            switch (position) {
                case 0:
                    ServerSettingFragment serverSettingFragment = new ServerSettingFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, serverSettingFragment,getFragment(0));
                    /** Adding this transaction to backstack */
                    fragmentTransaction.addToBackStack(null);
                    prePos = position;
                    fragmentTransaction.commit();
                    break;
                case 1:
                    ConfigurationFragment configurationFragment = new ConfigurationFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, configurationFragment,getFragment(1));
                    /** Adding this transaction to backstack */
                    fragmentTransaction.addToBackStack(null);
                    prePos = position;
                    fragmentTransaction.commit();
                    break;
                case 2:
                    SyncSettingFragment fragment = new SyncSettingFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, fragment, getFragment(2));
                    /** Adding this transaction to backstack */
                    fragmentTransaction.addToBackStack(null);
                    prePos = position;
                    fragmentTransaction.commit();
                    break;
                case 3:
                    SynchronizationFragment test = new SynchronizationFragment();
                    fragmentTransaction.add(R.id.detail_fragment_container, test,getFragment(3));
                    /** Adding this transaction to backstack */
                    fragmentTransaction.addToBackStack(null);
                    prePos = position;
                    fragmentTransaction.commit();
                    break;
                default:
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    Intent view_server_setting = new Intent(getActivity(0));
//                    startActivity(view_server_setting);
                case 1:
                    Intent view_configuration = new Intent(getActivity(1));
//                    startActivity(view_configuration);
                case 2:
                    Intent view_sync_setting = new Intent(getActivity(2));
//                    startActivity(view_sync_setting);
                case 3:
                    Intent view_synchronization = new Intent(getActivity(3));
//                    startActivity(view_synchronization);
                default:
                    break;
            }
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    public String getActivity(int position) {
        switch (position) {
            case 0:
                return "com.example.laptrinhmobile.toposmobile.Fragment.ServerSetting";
            case 1:
                return "com.example.laptrinhmobile.toposmobile.Fragment.Configuration";
            case 2:
                return "com.example.laptrinhmobile.toposmobile.Fragment.SyncSetting";
            case 3:
                return "com.example.laptrinhmobile.toposmobile.Fragment.Synchronization";
            default:
                return null;
        }
    }

    public String getFragment(int position) {
        switch (position) {
            case 0:
                return "com.example.laptrinhmobile.toposmobile.Fragment.ServerSettingFragment";
            case 1:
                return "com.example.laptrinhmobile.toposmobile.Fragment.ConfigurationFragment";
            case 2:
                return "com.example.laptrinhmobile.toposmobile.Fragment.SyncSettingFragment";
            case 3:
                return "com.example.laptrinhmobile.toposmobile.Fragment.SynchronizationFragment";
            default:
                return null;
        }
    }
}
