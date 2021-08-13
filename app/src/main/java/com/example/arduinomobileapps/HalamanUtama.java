package com.example.arduinomobileapps;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.NumberFormat;

public class HalamanUtama extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference refAmonia, refHumidity, refTemperature;
    private long amonia;
    private Double humidity, temperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);
        TextView suhuInputText  = (TextView) findViewById(R.id.suhuInputText);
        TextView kelembabanInputText = (TextView)findViewById(R.id.kelembabanInputText);
        TextView kadarAmoniakInputText = (TextView)findViewById(R.id.kadarAmoniakInputText);
        NumberFormat nm = NumberFormat.getNumberInstance();

        database = FirebaseDatabase.getInstance();
        refAmonia = database.getReference("amonia");
        refAmonia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amonia = dataSnapshot.getValue(Long.class);
                Log.d("TAG", "Amonia: " + amonia);
                kadarAmoniakInputText.setText(nm.format(amonia));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refHumidity = database.getReference("humidity");
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

        refTemperature = database.getReference("temperature");
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

    }
}