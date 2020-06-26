package com.example.myapplication;
/**
 *
 * @title: the activity to create a list of bluetooth devices that are available and connect with the one that gives a gps signal
 * @author: Eva Vogelezang
 * @reference:
 * Fahad, Engr. (August 9, 2019). Monitoring over Bluetooth. https://www.electroniclinic.com/how-to-create-android-app-for-arduino-sensor-monitoring-over-bluetooth/#AndroidManifest_xml_code
 *
 *
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class ConnectBracelet extends Activity {
    //class variables
    private Button search;
    private Button connect;
    private ListView listView;
    private BluetoothAdapter mBTAdapter;
    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
    private static final int SETTINGS = 20;
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int mBufferSize = 50000; //Default
    public static final String DEVICE_EXTRA = "com.example.anysensormonitoring.SOCKET";
    public static final String DEVICE_UUID = "com.example.anysensormonitoring.uuid";
    private static final String DEVICE_LIST = "com.example.anysensormonitoring.devicelist";
    private static final String DEVICE_LIST_SELECTED = "com.example.anysensormonitoring.devicelistselected";
    public static final String BUFFER_SIZE = "com.example.anysensormonitoring.buffersize";
    private static final String TAG = "BlueTest5-MainActivity";
    private ImageButton HomeSymbol;
    private ImageButton b_infoconnect;

//make the connection with the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bracelet); // refering to the layout of this activity

        //Make the connection with the app:
        //initialize info symbol button
        b_infoconnect = (ImageButton) findViewById(R.id.b_infoconnect);
        //when the symbol is clicked it opens up the Connect info popup
        b_infoconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnectBracelet.this, ConnectInfoPopUp.class));
            }
        });

        //initialize home symbol button
        HomeSymbol = (ImageButton) findViewById(R.id.HomeSymbol);
        //when the home symbol button is clicked open the main menu tab
        HomeSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnectBracelet.this, StartMainMenu.class));
            }
        });

        //creating connection between layout id's and class variables
        search = (Button) findViewById(R.id.b_search);
        connect = (Button) findViewById(R.id.b_connect);
        listView = (ListView) findViewById(R.id.List_PairedDevices);

        if (savedInstanceState != null) {
            ArrayList<BluetoothDevice> list = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
            if (list != null) {
                initList(list);
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                int selectedIndex = savedInstanceState.getInt(DEVICE_LIST_SELECTED);
                if (selectedIndex != -1) {
                    adapter.setSelectedIndex(selectedIndex);
                    connect.setEnabled(true);
                }
            } else {
                initList(new ArrayList<BluetoothDevice>());
            }

        } else {
            initList(new ArrayList<BluetoothDevice>());
        }

        //If search has been touched
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //initialize the mBTAdapter
                mBTAdapter = BluetoothAdapter.getDefaultAdapter();

                //if mBTAdapter is zero
                if (mBTAdapter == null) {

                    //make the text bluetooth not found
                    Toast.makeText(getApplicationContext(), "Bluetooth not found", Toast.LENGTH_SHORT).show();
                }

                //else if the mBTAdapter is working
                else if (!mBTAdapter.isEnabled()) {
                    //request enabling bluetooth
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, BT_ENABLE_REQUEST);
                }

                //else bluetooth is available and search devices
                else {
                    new SearchDevices().execute();
                }
            }
        });

        //If connect has been touched
        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //the selected item is the new bluetooth device
                BluetoothDevice device = ((MyAdapter) (listView.getAdapter())).getSelectedItem();

                //go to the the main2activity class
                //Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                //intent.putExtra(DEVICE_EXTRA, device);
                //intent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                //intent.putExtra(BUFFER_SIZE, mBufferSize);
                //startActivity(intent);
                //startActivity(new Intent(ConnectBracelet.this, Main2Activity.class));
                //show message
                showToast("Your device is connected");
            }
        });



    }

    protected void onPause() {
// TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
// TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //if there is a bluetooth enable request and the result is enabling then "Bluetooth enabled successfully" will appear otherwise "Bluetooth couldn't be enabled" will appear.
            case BT_ENABLE_REQUEST:
                if (resultCode == RESULT_OK) {
                    msg("Bluetooth Enabled successfully");
                    new SearchDevices().execute();
                }
                else {
                    msg("Bluetooth couldn't be enabled");
                }

                break;

            case SETTINGS: //If the settings have been updated
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String uuid = prefs.getString("prefUuid", "Null");
                mDeviceUUID = UUID.fromString(uuid);
                Log.d(TAG, "UUID: " + uuid);
                String bufSize = prefs.getString("prefTextBuffer", "Null");
                mBufferSize = Integer.parseInt(bufSize);

                String orientation = prefs.getString("prefOrientation", "Null");
                Log.d(TAG, "Orientation: " + orientation);
                if (orientation.equals("Landscape")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                } else if (orientation.equals("Portrait")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                } else if (orientation.equals("Auto")) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Quick way to call the Toast
     * @param str
     */
    private void msg(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

   //initialize the list adapter
    private void initList(List<BluetoothDevice> objects) {
        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, R.id.lstContent, objects);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                connect.setEnabled(true);
            }
        });
    }

    //Searches for paired devices. Doesn't do a scan, only devices which are paired through Settings->Bluetooth will show up with this.
    private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

        @Override
        protected List<BluetoothDevice> doInBackground(Void... params) {
            //get the bonded devices from your phone
            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            //put them in a list
            List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
            for (BluetoothDevice device : pairedDevices) {
                listDevices.add(device);
            }
            return listDevices;
        }

        @Override
        protected void onPostExecute(List<BluetoothDevice> listDevices) {
            super.onPostExecute(listDevices);
            //if the list of devices is bigger than 0
            if (listDevices.size() > 0) {
                //get the adapter
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                adapter.replaceItems(listDevices);
            }
            else {
                msg("No paired devices found, please pair your serial BT device and try again");
            }
        }

    }


    //Custom adapter to show the current devices in the list.
    private class MyAdapter extends ArrayAdapter<BluetoothDevice> {
        private int selectedIndex;
        private Context context;
        private int selectedColor = Color.parseColor("#abcdef");
        private List<BluetoothDevice> myList;

        public MyAdapter(Context ctx, int resource, int textViewResourceId, List<BluetoothDevice> objects) {
            super(ctx, resource, textViewResourceId, objects);
            context = ctx;
            myList = objects;
            selectedIndex = -1;
        }

        public void setSelectedIndex(int position) {
            selectedIndex = position;
            notifyDataSetChanged();
        }

        public BluetoothDevice getSelectedItem() {
            return myList.get(selectedIndex);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView tv;
        }

        public void replaceItems(List<BluetoothDevice> list) {
            myList = list;
            notifyDataSetChanged();
        }

        public List<BluetoothDevice> getEntireList() {
            return myList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if (convertView == null) {
                vi = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.tv = (TextView) vi.findViewById(R.id.lstContent);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if (selectedIndex != -1 && position == selectedIndex) {
                holder.tv.setBackgroundColor(selectedColor);
            } else {
                holder.tv.setBackgroundColor(Color.WHITE);
            }
            BluetoothDevice device = myList.get(position);
            holder.tv.setText(device.getName() + "\n " + device.getAddress());

            return vi;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(ConnectBracelet.this, Preferences.class);
                startActivityForResult(intent, SETTINGS);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //show message string for short amount of time
    private void showToast(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
