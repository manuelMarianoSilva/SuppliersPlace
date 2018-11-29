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
import com.pullapps.suppapp.View.utils.VisualizadorDeListado;

import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.DIALOG_ID;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.day_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.month_x;
import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.year_x;

public class AgregarCompulsaActivity extends AppCompatActivity implements VisualizadorDeListado, SeleccionadorDeFecha, SeleccionadorDeArchivo, SubidorDeArchivo {

    private TextView tvRutaArchivo;
    private Uri archivoUri;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseDatabase database;
    public String urlPliego;
    private EditText edtFechaCierre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_compulsa);

        AgregarCompulsaFragment agregarCompulsaFragment = new AgregarCompulsaFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, agregarCompulsaFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void visualizarListado() {
        Intent intent = new Intent(AgregarCompulsaActivity.this, ListaCompulsasActivity.class);
        startActivity(intent);
    }

    @Override
    public void seleccionarArchivo() {
        if (ContextCompat.checkSelfPermission(AgregarCompulsaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(intent, 86);

        } else {
            ActivityCompat.requestPermissions(AgregarCompulsaActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        tvRutaArchivo = (TextView) findViewById(R.id.tvRutaArchivo);

        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            archivoUri = data.getData();
            tvRutaArchivo.setText(archivoUri.getLastPathSegment());
        } else {
            Toast.makeText(this, "Por favor, seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void subirArchivo(final String id) {
        if (archivoUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Cargando archivo...");
            progressDialog.setProgress(0);
            progressDialog.show();

            storage = FirebaseStorage.getInstance();
            database = FirebaseDatabase.getInstance();

            final String fileName = System.currentTimeMillis() + "";

            StorageReference storageReference = storage.getReference();
            storageReference.child("Compulsas").child(id).child("pliego").child(fileName).putFile(archivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlPliego = taskSnapshot.getDownloadUrl().toString();
                            DatabaseReference reference = database.getReference();
                            reference.child("Compulsas").child(id).child("pliego").setValue(urlPliego).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(AgregarCompulsaActivity.this, "El archivo exitosamente cargado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AgregarCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AgregarCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentProgress);
                }
            });
        } else {
            Toast.makeText(this, "Por favor, seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
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

    public String getUrlPliego(){
        return urlPliego;
    }

}
