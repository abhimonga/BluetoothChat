package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.testapp.DataModel;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.example.testapp.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;

import static android.widget.Toast.LENGTH_LONG;

public class Signup extends AppCompatActivity {
    private Realm realm;
     EditText name,age;
     RadioGroup radioSexGroup,interest;
     RadioButton radioSexButton,minterest;
     Button submit;
     StitchAppClient stitchAppClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);

       submit=findViewById(R.id.submit);


       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               radioSexGroup=findViewById(R.id.radioSex);
               radioSexButton=findViewById(radioSexGroup.getCheckedRadioButtonId());
               interest=findViewById(R.id.interest);
               minterest=findViewById(interest.getCheckedRadioButtonId());
             //  Toast.makeText(getApplicationContext(),minterest.getText(), LENGTH_LONG).show();
               sendtoDatabase();
           }
       });

    }
    public void sendtoDatabase(){
          //      Stitch.initializeAppClient("bluetooth-duacw");
        stitchAppClient = Stitch.getAppClient("bluetooth-duacw");
        stitchAppClient.getAuth().loginWithCredential(new AnonymousCredential()).addOnSuccessListener(new OnSuccessListener<StitchUser>() {
            @Override
            public void onSuccess(StitchUser stitchUser) {
                Toast.makeText(getApplicationContext(),"Success",LENGTH_LONG).show();
            }
        });
        RemoteMongoClient mongoclient = stitchAppClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        RemoteMongoCollection<Document> myCollection = mongoclient.getDatabase("Test").getCollection("new_coll");
        Document doc = new Document();
        UUID uuid=UUID.randomUUID();
        final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";

        String macAddress = Settings.Secure.getString(getContentResolver(), SECURE_SETTINGS_BLUETOOTH_ADDRESS);
        doc.put("user_id",uuid.toString() );
        doc.put("name",name.getText().toString());
        doc.put("age",age.getText().toString());
        doc.put("gender",radioSexButton.getText().toString());
        doc.put("interest",minterest.getText().toString());
        doc.put("BT_address",macAddress);
          myCollection.insertOne(doc).addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
              @Override
              public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                  if(task.isSuccessful()){
                      Toast.makeText(getApplicationContext(),"Successfully Inserted",Toast.LENGTH_LONG).show();
                      startActivity(new Intent(Signup.this,Discovery.class));
                  }
                  else{
                      Toast.makeText(getApplicationContext(),"Please check your internet connection and try again",Toast.LENGTH_LONG).show();
                  }
              }
          });


    }


}
