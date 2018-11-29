package com.pullapps.suppapp.View.view.Main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.AgregadorDeCompulsa;
import com.pullapps.suppapp.View.view.Login.LoginActivity;
import com.pullapps.suppapp.View.controller.CompulsaControlador;
import com.pullapps.suppapp.View.utils.ResultListener;
import com.pullapps.suppapp.View.utils.VisualizadorDeDetalle;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaCompulsasFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private static final String TAG = "ListaCompulsasActivity";



    private Button btnCrearCompulsa, btnSalir;
    private ListView lstCompulsas;
    private List<String> titulosCompulsas;
    private List<Compulsa> listaCompulsas;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference databaseReference;
    private String claveCompulsa;
    private AgregadorDeCompulsa agregadorDeCompulsa;
    private VisualizadorDeDetalle visualizadorDeDetalle;
    private Bundle bundle;


    public ListaCompulsasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        agregadorDeCompulsa = (AgregadorDeCompulsa) context;
        visualizadorDeDetalle = (VisualizadorDeDetalle) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_compulsas, container, false);

        CompulsaControlador compulsaControlador = new CompulsaControlador();

        btnCrearCompulsa = (Button) view.findViewById(R.id.btnCrearCompulsa);
        btnSalir = (Button) view.findViewById(R.id.btnSalir);
        lstCompulsas = (ListView) view.findViewById(R.id.lstCompulsas);
        titulosCompulsas = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, titulosCompulsas);
        lstCompulsas.setAdapter(arrayAdapter);

        listaCompulsas = new ArrayList<>();

        compulsaControlador.obtenerCompulsas(new ResultListener<List<Compulsa>>() {
            @Override
            public void finish(List<Compulsa> resultado) {
                listaCompulsas = resultado;
                titulosCompulsas.clear();
                for (Compulsa compulsa: listaCompulsas) {
                    titulosCompulsas.add(compulsa.getTitle());
                    arrayAdapter.notifyDataSetChanged();
                }

            }
        }, this.getContext());


        btnCrearCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregadorDeCompulsa.agregarCompulsa();
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
                Compulsa compulsaSeleccionada = listaCompulsas.get(i);
                visualizadorDeDetalle.verDetalle(compulsaSeleccionada);

            }
        });
        return view;
    }

}
