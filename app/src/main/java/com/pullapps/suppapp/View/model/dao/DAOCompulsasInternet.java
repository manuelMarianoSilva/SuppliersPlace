package com.pullapps.suppapp.View.model.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pullapps.suppapp.View.utils.ResultListener;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import java.util.ArrayList;
import java.util.List;

public class DAOCompulsasInternet {
    private static final String COMPULSA_NODO = "Compulsas";
    private static final String TAG = "DAOCompulsasInternet";
    private DatabaseReference databaseReference;

    public void obtenerCompulsasInternet(final ResultListener<List<Compulsa>> controladorListener){
        final List<Compulsa> listaCompulsas = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(COMPULSA_NODO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Compulsa compulsa = snapshot.getValue(Compulsa.class);
                        Log.w(TAG, "Titulo Compulsa: " + compulsa.getTitle());
                        listaCompulsas.add(compulsa);
                    }
                    controladorListener.finish(listaCompulsas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Cancelado");
            }
        });

    }
}
