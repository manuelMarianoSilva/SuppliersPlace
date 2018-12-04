package com.pullapps.suppapp.View.view.Main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.AgregadorDeCompulsa;
import com.pullapps.suppapp.View.utils.CompulsasAdapter;
import com.pullapps.suppapp.View.utils.SeleccionadorDeCompulsa;
import com.pullapps.suppapp.View.view.Login.LoginActivity;
import com.pullapps.suppapp.View.controller.CompulsaControlador;
import com.pullapps.suppapp.View.utils.ResultListener;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaCompulsasFragment extends Fragment {
    private Button btnCrearCompulsa, btnSalir;
    private RecyclerView rcvCompulsas;
    private List<Compulsa> listaCompulsas;
    private AgregadorDeCompulsa agregadorDeCompulsa;
    private CompulsaControlador compulsaControlador;



    public ListaCompulsasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        agregadorDeCompulsa = (AgregadorDeCompulsa) context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_compulsas, container, false);

        btnCrearCompulsa = view.findViewById(R.id.btnCrearCompulsa);
        btnSalir = view.findViewById(R.id.btnSalir);
        rcvCompulsas = view.findViewById(R.id.rcvCompulsas);
        compulsaControlador = new CompulsaControlador();
        listaCompulsas = new ArrayList<>();



        compulsaControlador.obtenerCompulsas(new ResultListener<List<Compulsa>>() {
            @Override
            public void finish(List<Compulsa> resultado) {
                listaCompulsas = resultado;
                final CompulsasAdapter compulsasAdapter = new CompulsasAdapter(listaCompulsas, (SeleccionadorDeCompulsa) getContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);

                rcvCompulsas.setAdapter(compulsasAdapter);
                rcvCompulsas.setLayoutManager(layoutManager);

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

        return view;
    }


}
