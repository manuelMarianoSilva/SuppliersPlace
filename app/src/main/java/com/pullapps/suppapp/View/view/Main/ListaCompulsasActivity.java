package com.pullapps.suppapp.View.view.Main;



import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.AgregadorDeCompulsa;
import com.pullapps.suppapp.View.utils.SeleccionadorDeCompulsa;
import com.pullapps.suppapp.View.model.pojo.Compulsa;


public class ListaCompulsasActivity extends AppCompatActivity implements AgregadorDeCompulsa, SeleccionadorDeCompulsa {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compulsas);



        ListaCompulsasFragment listaCompulsasFragment = new ListaCompulsasFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, listaCompulsasFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void agregarCompulsa() {
        Intent intent = new Intent(ListaCompulsasActivity.this, AgregarCompulsaActivity.class);
        startActivity(intent);

    }

    @Override
    public void seleccionarCompulsa(Compulsa compulsaSeleccionada) {
        Bundle bundle = new Bundle();
        bundle.putString("id", compulsaSeleccionada.getId());
        bundle.putString("title", compulsaSeleccionada.getTitle());
        bundle.putString("description", compulsaSeleccionada.getDescription());
        bundle.putString("fechaCierre", compulsaSeleccionada.getFechaCierre());

        Intent intent = new Intent(ListaCompulsasActivity.this, DetalleCompulsaActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
