package com.pullapps.suppapp.View.Main;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.model.Compulsa;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String COMPULSA_NODO = "Compulsas";
    private static final String TAG = "MainActivity";
    private ListView lstCompulsas;
    private List<String> titulosCompulsas;
    private List<Compulsa> compulsas;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference databaseReference;
    private String claveCompulsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstCompulsas = (ListView) findViewById(R.id.lstCompulsas);
        titulosCompulsas = new ArrayList<>();
        compulsas = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titulosCompulsas);
        lstCompulsas.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(COMPULSA_NODO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titulosCompulsas.clear();
                compulsas.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Compulsa compulsa = snapshot.getValue(Compulsa.class);
                        Log.w(TAG, "Titulo Compulsa: " + compulsa.getTitle());
                        titulosCompulsas.add(compulsa.getTitle());
                        compulsas.add(compulsa);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void crearCompulsa(View view) {
        claveCompulsa = databaseReference.push().getKey();
        Compulsa compulsa = new Compulsa(claveCompulsa, "Compulsa " + claveCompulsa.substring(15, 19));
        databaseReference.child(COMPULSA_NODO).child(compulsa.getId()).setValue(compulsa);
    }
}
