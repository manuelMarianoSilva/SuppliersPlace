package com.pullapps.suppapp.View.Main;

import android.Manifest;
import android.app.Fragment;
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
import com.pullapps.suppapp.View.Utils.CambiadorDeFragment;
import com.pullapps.suppapp.View.Utils.SeleccionadorDeArchivo;
import com.pullapps.suppapp.View.Utils.SubidorDeArchivo;
import com.pullapps.suppapp.View.model.Compulsa;

public class DetalleCompulsaActivity extends AppCompatActivity implements CambiadorDeFragment, SeleccionadorDeArchivo, SubidorDeArchivo {

    private Uri archivoUri;
    private TextView tvRutaArchivo;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compulsa);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        DetalleCompulsaFragment detalleCompulsaFragment = new DetalleCompulsaFragment();
        detalleCompulsaFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, detalleCompulsaFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void seleccionarArchivo() {
        if (ContextCompat.checkSelfPermission(DetalleCompulsaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(intent, 86);

        } else {
            ActivityCompat.requestPermissions(DetalleCompulsaActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
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
            storageReference.child("Uploads").child(fileName).putFile(archivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            DatabaseReference reference = database.getReference();
                            reference.child("Compulsas").child(id).child("pliego").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(DetalleCompulsaActivity.this, "El archivo exitosamente cargado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetalleCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetalleCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentProgress);
                }
            });

            //subirArchivo(archivoUri, id);
        } else {
            Toast.makeText(this, "Por favor, seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(intent, 86);
        } else {
            Toast.makeText(this, "Por favor, conceda los permisos", Toast.LENGTH_SHORT).show();
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


    private void subirArchivo(Uri archivoUri, final String id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Cargando archivo...");
        progressDialog.setProgress(0);
        progressDialog.show();

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        final String fileName = System.currentTimeMillis() + "";

        StorageReference storageReference = storage.getReference();
        storageReference.child("Uploads").child(fileName).putFile(archivoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        DatabaseReference reference =database.getReference();
                        reference.child("Compulsas").child(id).child("pliego").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(DetalleCompulsaActivity.this, "El archivo exitosamente cargado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetalleCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetalleCompulsaActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }


    @Override
    public void cambiarFragment(Integer idClick) {

        Intent intent = new Intent(DetalleCompulsaActivity.this, ListaCompulsasActivity.class);
        startActivity(intent);
    }


}
