package com.example.arduinomobileapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HalamanUtama extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseFirestore firestore;
    private DatabaseReference refAmonia, refHumidity, refTemperature;
    private Double amonia, humidity, temperature;
    private static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random RANDOM = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        TextView suhuInputText  = (TextView) findViewById(R.id.suhuInputText);
        TextView kelembabanInputText = (TextView)findViewById(R.id.kelembabanInputText);
        TextView kadarAmoniakInputText = (TextView)findViewById(R.id.kadarAmoniakInputText);
        NumberFormat nm = NumberFormat.getNumberInstance();
        Button dataButton = (Button) findViewById(R.id.buttonData1MingguTerakhir);

        //Get Data from Realtime Database

        database = FirebaseDatabase.getInstance();
        refAmonia = database.getReference("Amonia");
        refAmonia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amonia = dataSnapshot.getValue(Double.class);
                Log.d("TAG", "Amonia: " + amonia);
                kadarAmoniakInputText.setText(nm.format(amonia));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refHumidity = database.getReference("Humidity");
        refHumidity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                humidity = dataSnapshot.getValue(Double.class);
                Log.d("TAG", "Humidity: " + humidity);
                kelembabanInputText.setText(nm.format(humidity));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refTemperature = database.getReference("Temperatur");
        refTemperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temperature = dataSnapshot.getValue(Double.class);
                Log.d("TAG", "Temperature: " + temperature);
                suhuInputText.setText(nm.format(temperature));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HalamanUtama.this, ListData.class));
            }
        });

        //Send Data to Firestore

        firestore = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("Amonia", amonia);
        data.put("Humidity", humidity);
        data.put("Temperatur", temperature);
        data.put("ID", randomString(15));

        firestore.collection("History").
                add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }
}