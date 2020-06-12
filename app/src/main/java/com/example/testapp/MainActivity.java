package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import  com.example.testapp.GattClient;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG="MainActivity";
    StitchAppClient stitchAppClient;
    private BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
      boolean res= bluetoothAdapter.setName("Abhinav");
      debug("result is"+res);
//        Stitch.initializeAppClient("bluetooth-duacw");
//        stitchAppClient = Stitch.getAppClient("bluetooth-duacw");
//        stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential());
////        final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
////
////        String macAddress = Settings.Secure.getString(getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        String bluetoothMacAddress = "";
//        try {
//            Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
//            mServiceField.setAccessible(true);
//
//            Object btManagerService = mServiceField.get(bluetoothAdapter);
//
//            if (btManagerService != null) {
//                bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
//            }
//        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
//
//        }


//

       //  debug(add);
        //    exist(bluetoothMacAddress);
        //   startActivity(new Intent(this,Discover.class));
        //go to next activity


    }
//    private final ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            BluetoothDevice device = result.getDevice();
//
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            // Ignore for now
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            // Ignore for now
//        }
//    };
    public void exist(String address){

        RemoteMongoClient mongoclient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        RemoteMongoCollection<Document> myCollection = mongoclient.getDatabase("Test").getCollection("new_coll");
   //    debug(address);
       Document filter=new Document().append("BT_address",address);
   RemoteFindIterable result=  myCollection.find(filter);
        Task<List<Document>> items=result.into(new ArrayList<Document>());
        items.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {

                   List<Document> itemList=task.getResult();
                  if(itemList.size()>0){
                    startActivity(new Intent(getApplicationContext(),Discovery.class));
                  }
                  else{
                      startActivity(new Intent(getApplicationContext(),Signup.class));
                  }
            }
        });

  //     debug("result"+flag[0]);
    }
    public void debug(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        Log.d(TAG,"checking"+msg);
    }
}
