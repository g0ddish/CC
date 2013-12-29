package com.cranium.crusher;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.cranium.crusher.*;





public class MainActivity extends Activity {
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public static String BLE_SHIELD_TX = "713d0003-503e-4c75-ba94-3148f18d941e";
	public static String BLE_SHIELD_RX = "713d0002-503e-4c75-ba94-3148f18d941e";
	public static String BLE_SHIELD_SERVICE = "713d0000-503e-4c75-ba94-3148f18d941e";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
private int prssi =0;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		 ListView lv = (ListView) findViewById(R.id.listView1);
	//	 ExpandableListView btlist = (ExpandableListView) findViewById(R.id.expandableListView1);
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(getApplicationContext(), "BT Search Starting", duration).show();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			SetStatus("No Bluetooth Device");
		
		}else{
			SetStatus("Bluetooth Devices Available");
			 lv.setAdapter(null);
	         
	          IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	          registerReceiver(myReceiver, filter); // Don't forget to unregister during onDestroy
	          mBluetoothAdapter.startDiscovery();
			}
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn = (Button) findViewById(R.id.button1);
		/* 
		Timer t = new Timer();
		//Set the schedule function and rate
		t.scheduleAtFixedRate(new TimerTask() {

		    @Override
		    public void run() {
		    	 TextView statusview = (TextView) findViewById(R.id.textView1);
		    	 statusview.setText(prssi);
		    }
		         
		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		0,
		//Set the amount of time between each execution (in milliseconds)
		1000);
		*/
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Populate();
		    }
		});

		return true;
	}
	protected void TimerMethod() {
		fucknigger.readRemoteRssi();
		
	}
	private Timer myTimer;
	private BluetoothGatt fucknigger;
	private Handler myUIHandler = new Handler()
	{
		@Override
	    public void handleMessage(Message msg)
	    {
	    	 TextView statusview = (TextView) findViewById(R.id.textView1);
	    	 statusview.setText(msg.toString());
	    	// statusview.setText(msg.arg1);
	    }
	};

	
	
	private ArrayAdapter arrayAdapter;
public void SetStatus(String status){
	 TextView statusview = (TextView) findViewById(R.id.textView1);
	 statusview.setText(status);
}
 public void Populate(){
 ListView lv = (ListView) findViewById(R.id.listView1);
	 arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arlist);
	 lv.setAdapter(arrayAdapter);
	 Log.d(TAG, arlist.toString());
	 for(String item : arlist){
		    Log.d(TAG, item);
		}

	 
 }

 
	private	ArrayList<String> arlist = new ArrayList<String>();
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	  //  Log.d(TAG, "onReceive (myReceiver)");
	   
	    String action = intent.getAction();
	    // When discovery finds a device
	    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        // Get the BluetoothDevice object from the Intent
	        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        // Add the name and address to an array adapter to show in a ListView
	     //   Log.d(TAG,device.getName() + " " + device.getAddress());
	        
	    //    btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        arlist.add(device.getName() + "\n" + device.getAddress());
	      if(device.getType() == 2){
	  		int duration = Toast.LENGTH_SHORT;
			Toast.makeText(getApplicationContext(), "Detected a Low Engery BT Device!", duration).show();
			final BluetoothGatt test = device.connectGatt(context, true, mGattCallback);
			// test.discoverServices();
			Timer l = new Timer();
			//Set the schedule function and rate
			l.scheduleAtFixedRate(new TimerTask() {

			    @Override
			    public void run() {
			    	test.readRemoteRssi();
			    }
			         
			},
			//Set how long before to start calling the TimerTask (in milliseconds)
			0,
			//Set the amount of time between each execution (in milliseconds)
			1000);

	
	// RBLService mBluetoothLeService = new RBLService();
	
		
	
	

	//byte buf[] = new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00 };
		//buf[1] = 0x01;

	
	 
	//characteristicTx.setValue(buf);
	//test.writeCharacteristic(characteristicTx);

	      }

	    }
	    if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        Log.d(TAG, "Started discovery");
	    }
	    if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        Log.d(TAG, "Finished discovery");
	    }
	}
	};

	private final static String TAG = RBLService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;

	public final static String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_RSSI = "ACTION_GATT_RSSI";
	public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "EXTRA_DATA";

	public final static UUID UUID_BLE_SHIELD_TX = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_TX);
	public final static UUID UUID_BLE_SHIELD_RX = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_RX);
	public final static UUID UUID_BLE_SHIELD_SERVICE = UUID
			.fromString(RBLGattAttributes.BLE_SHIELD_SERVICE);

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
fucknigger = gatt;
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
					);
				 UUID uid = UUID.fromString(BLE_SHIELD_RX);   
					BluetoothGattCharacteristic characteristicTx = new BluetoothGattCharacteristic(uid, 52, 16);
					
					byte[] buf = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0x00 };
						buf[1] = 0x01;
						Log.d(TAG, "We are at before characristic is set");
					characteristicTx.setValue(buf);
					Log.d(TAG, "Successfully Set");
					gatt.writeCharacteristic(characteristicTx);
					Log.d(TAG, "Successfully written! But to where?");
				gatt.readRemoteRssi();
			//	 gatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}	@Override
		public void onServicesDiscovered (BluetoothGatt gatt, int status){
			Log.d(TAG, gatt.toString() + status);
		}	@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Message fuckyou = new Message();
				fuckyou.arg1 = rssi;
				myUIHandler.sendMessage(fuckyou);
				prssi = rssi;
				broadcastUpdate(ACTION_GATT_RSSI, rssi);
			} else {
				prssi = rssi;
				Log.w(TAG, "onReadRemoteRssi received: " + status);
			}
		};

		private BluetoothGattCharacteristic characteristicTx = null;
		private RBLService mBluetoothLeService;

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				Log.w("derp", "Charistic: " + characteristic);
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
	};

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action, int rssi) {
		final Intent intent = new Intent(action);
		intent.putExtra(EXTRA_DATA, String.valueOf(rssi));
		sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		// This is special handling for the Heart Rate Measurement profile. Data
		// parsing is
		// carried out as per profile specifications:
		// http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
		if (UUID_BLE_SHIELD_RX.equals(characteristic.getUuid())) {
			final byte[] rx = characteristic.getValue();
			intent.putExtra(EXTRA_DATA, rx);
		}

		sendBroadcast(intent);
	}

	
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;

		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.readCharacteristic(characteristic);
	}

	public void readRssi() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.readRemoteRssi();
	}

	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

		if (UUID_BLE_SHIELD_RX.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID
							.fromString(RBLGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
	}

	public BluetoothGattService getSupportedGattService() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getService(UUID_BLE_SHIELD_SERVICE);
	}
}
