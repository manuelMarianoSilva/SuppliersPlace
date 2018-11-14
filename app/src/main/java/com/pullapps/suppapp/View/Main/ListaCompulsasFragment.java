package com.pullapps.suppapp.View.Main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.Login.LoginActivity;
import com.pullapps.suppapp.View.model.Compulsa;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaCompulsasFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private static final String TAG = "MainActivity";



    private Button btnCrearCompulsa, btnSalir;
    private ListView lstCompulsas;
    private List<String> titulosCompulsas;
    private List<Compulsa> compulsas;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference databaseReference;
    private String claveCompulsa;
    private CambiarFragmentListener cambiarFragmentListener;
    private VerDetalleCompulsaListener verDetalleCompulsaListener;
    private Bundle bundle;


    public ListaCompulsasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiarFragmentListener = (CambiarFragmentListener) context;
        verDetalleCompulsaListener = (VerDetalleCompulsaListener) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnCrearCompulsa = (Button) view.findViewById(R.id.btnCrearCompulsa);
        btnSalir = (Button) view.findViewById(R.id.btnSalir);
        lstCompulsas = (ListView) view.findViewById(R.id.lstCompulsas);
        titulosCompulsas = new ArrayList<>();
        compulsas = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, titulosCompulsas);
        lstCompulsas.setAdapter(arrayAdapter);

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
                        titulosCompulsas.add(compulsa.getId() + " - " + compulsa.getTitle());
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
                cambiarFragmentListener.cambiarFragment(1);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                //closing this activity
                getActivity().finish();
                //starting login activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        lstCompulsas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Compulsa compulsaSeleccionada = compulsas.get(i);
                verDetalleCompulsaListener.verDetalle(compulsaSeleccionada);

            }
        });
        return view;
    }

    public interface CambiarFragmentListener{
        void cambiarFragment(Integer idClick);
    }

    public interface VerDetalleCompulsaListener{
        void verDetalle(Compulsa compulsaSeleccionada);
    }

    public interface SeleccionarArchivoListener{
        void seleccionDeArchivo();
    }

    public interface SubirArchivoListener{
        void subidaDeArchivo(String id);
    }

}
