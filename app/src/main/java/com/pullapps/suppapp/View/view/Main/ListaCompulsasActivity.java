package com.pullapps.suppapp.View.view.Main;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.CambiadorDeFragment;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;
import com.pullapps.suppapp.View.utils.VisualizadorDeDetalle;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.DIALOG_ID;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.day_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.month_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.year_x;


public class ListaCompulsasActivity extends AppCompatActivity implements CambiadorDeFragment, VisualizadorDeDetalle, SeleccionadorDeFecha {

    private Uri archivoUri;
    private EditText edtFechaCierre;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


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
        //Fragment fragment = new DetalleCompulsaFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", compulsaSeleccionada.getId());
        bundle.putString("title", compulsaSeleccionada.getTitle());
        bundle.putString("description", compulsaSeleccionada.getDescription());
        bundle.putString("fechaCierre", compulsaSeleccionada.getFechaCierre());



        Intent intent = new Intent(ListaCompulsasActivity.this, DetalleCompulsaActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    @Override
    public void seleccionarFecha(int dialogId) {
        showDialog(dialogId);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;

            edtFechaCierre = findViewById(R.id.edtFechaCierre);
            edtFechaCierre.setText(day_x + "/" + month_x + "/" + year_x);

        }
    };
}
