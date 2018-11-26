package com.pullapps.suppapp.View.view.Login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.view.Main.ListaCompulsasActivity;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userName, password1, password2;
    private TextInputLayout tilUserName, tilPassword1, tilpassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.mailUsuario);
        password1 = findViewById(R.id.contraseñaUsuario);
        password2 = findViewById(R.id.reingresarContraseñaUsuario);

        tilUserName = findViewById(R.id.textInputLayoutMail);
        tilPassword1 = findViewById(R.id.textInputLayoutContraseñaUsuario);
        tilpassword2 = findViewById(R.id.textInputLayoutReingresarContraseñaUsuario);

        Button botonRegistrar = findViewById(R.id.buttonRegistrar);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarDatosParaCrearNuevaCuenta();

            }
        });



//        password2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
//                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
//                        verificarDatosParaCrearNuevaCuenta();
//                        return true;
//                    }
//                }
//
//                return false;
//            }
//        });
    }

    private void verificarDatosParaCrearNuevaCuenta() {
        String nombreDeUsuario = userName.getText().toString();
        String strPassword = password1.getText().toString();
        String passwordVerification = password2.getText().toString();

        hideKeyboard();

        tilUserName.setErrorEnabled(false);
        tilPassword1.setErrorEnabled(false);
        tilpassword2.setErrorEnabled(false);

        if (nombreDeUsuario.isEmpty() || !isEmailValid(nombreDeUsuario)){
            notificarRegistroInvalido();
            return;
        }

        if (strPassword.isEmpty() || passwordVerification.isEmpty() || !strPassword.equals(passwordVerification)){
            notificarPasswordInvalido();
            return;
        }

        if (strPassword.length() < 6){
            notificarPasswordCorto();
            return;
        }

        crearNuevaCuenta(nombreDeUsuario, strPassword);
    }

    private void notificarPasswordCorto() {
        tilPassword1.setError("La contraseña debe tener por lo menos seis caracteres");
        tilPassword1.setErrorEnabled(true);
    }

    private void notificarPasswordInvalido() {
        tilpassword2.setError("Error de ingreso de contraseña, deben coincidir");
        tilpassword2.setErrorEnabled(true);
    }

    private void crearNuevaCuenta(String nombreDeUsuario, String pass1) {

        mAuth.createUserWithEmailAndPassword(nombreDeUsuario, pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //FirebaseUser user = mAuth.getCurrentUser();
                            goToMain();
                        } else {
                            //Toast.makeText(RegisterActivity.this, "El registro falló.\nPor favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                            tilpassword2.setError("El registro falló.\nPor favor inténtelo nuevamente");
                        }
                    }
                });
    }

    private void goToMain() {
        Intent intent = new Intent(this, ListaCompulsasActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void notificarRegistroInvalido() {
        //Toast.makeText(this, "Registro inválido.\nPor favor inténtelo nuevamente", Toast.LENGTH_SHORT).show();
        tilUserName.setError("Registro inválido.\nPor favor inténtelo nuevamente");
        tilUserName.setErrorEnabled(true);
    }

    private boolean isEmailValid (CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        //updateUI(user);
    }

}
