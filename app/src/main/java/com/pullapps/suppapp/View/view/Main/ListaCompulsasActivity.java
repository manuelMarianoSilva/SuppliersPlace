package com.pullapps.suppapp.View.view.Main;



import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.AgregadorDeCompulsa;
import com.pullapps.suppapp.View.utils.SeleccionadorDeArchivo;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;
import com.pullapps.suppapp.View.utils.SubidorDeArchivo;
import com.pullapps.suppapp.View.utils.VisualizadorDeDetalle;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.DIALOG_ID;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.day_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.month_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.year_x;


public class ListaCompulsasActivity extends AppCompatActivity implements AgregadorDeCompulsa, VisualizadorDeDetalle {

    public String urlPliego;
    private Uri archivoUri;
    private EditText edtFechaCierre;
    private TextView tvRutaArchivo;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase database;


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
    public void agregarCompulsa() {
        Intent intent = new Intent(ListaCompulsasActivity.this, AgregarCompulsaActivity.class);
        startActivity(intent);

    }
}
