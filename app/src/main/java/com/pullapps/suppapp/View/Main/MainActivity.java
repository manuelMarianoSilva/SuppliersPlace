package com.pullapps.suppapp.View.Main;



import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pullapps.suppapp.View.model.Compulsa;


public class MainActivity extends AppCompatActivity implements ListaCompulsasFragment.CambiarFragmentListener, ListaCompulsasFragment.VerDetalleCompulsaListener, ListaCompulsasFragment.SeleccionarArchivoListener, ListaCompulsasFragment.SubirArchivoListener{

    private Uri archivoUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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

    @Override
    public void seleccionDeArchivo() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            seleccionarArchivo();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
        }
    }

    @Override
    public void subidaDeArchivo() {

        if (archivoUri != null) {
            subirArchivo(archivoUri);
        } else {
            Toast.makeText(this, "Por favor, seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            seleccionarArchivo();
        } else {
            Toast.makeText(this, "Por favor, conceda los permisos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            archivoUri = data.getData();
            Toast.makeText(this, archivoUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarArchivo() {
        Intent intent = new Intent();
        intent.setType("aplication/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);

    }

    private void subirArchivo(Uri archivoUri) {

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
                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "El archivo exitosamente cargado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "EL archivo no fue exitosamente cargado", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }


}
