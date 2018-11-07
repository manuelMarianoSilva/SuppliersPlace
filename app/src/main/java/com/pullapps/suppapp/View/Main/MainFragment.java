package com.pullapps.suppapp.View.Main;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private static final String TAG = "MainActivity";
    private Button btnCrearCompulsa;
    private ListView lstCompulsas;
    private List<String> titulosCompulsas;
    private List<Compulsa> compulsas;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference databaseReference;
    private String claveCompulsa;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnCrearCompulsa = (Button) view.findViewById(R.id.btnCrearCompulsa);
        lstCompulsas = (ListView) view.findViewById(R.id.lstCompulsas);
        titulosCompulsas = new ArrayList<>();
        compulsas = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, titulosCompulsas);
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

        btnCrearCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claveCompulsa = databaseReference.push().getKey();
                Compulsa compulsa = new Compulsa(claveCompulsa, "Compulsa " + claveCompulsa.substring(15, 19));
                databaseReference.child(COMPULSA_NODO).child(compulsa.getId()).setValue(compulsa);
            }
        });
        return view;
    }

}