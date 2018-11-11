package com.pullapps.suppapp.View.Main;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.model.Compulsa;


public class MainActivity extends AppCompatActivity implements ListaCompulsasFragment.CambiarFragmentListener, ListaCompulsasFragment.VerDetalleCompulsaListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        ListaCompulsasFragment listaCompulsasFragment = new ListaCompulsasFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, listaCompulsasFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void cambiarFragment(Integer idClick) {
        Fragment fragment = null;

        switch (idClick) {
            case 0:
                fragment = new ListaCompulsasFragment();
                break;
            case 1:
                fragment = new AgregarCompulsaFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void verDetalle(Compulsa compulsaSeleccionada) {
        Fragment fragment = new DetalleCompulsaFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", compulsaSeleccionada.getId());
        bundle.putString("title", compulsaSeleccionada.getTitle());
        bundle.putString("description", compulsaSeleccionada.getDescription());

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();


    }
}
