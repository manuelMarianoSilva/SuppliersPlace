package com.pullapps.suppapp.View.Login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pullapps.suppapp.View.Main.MainActivity;
import com.pullapps.suppapp.R;

public class LoginActivity extends AppCompatActivity implements PasswordRecoveryFragment.RecoveryFragmentManipulator, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener fireBaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ejecutarLoginDeGoogle();
        ejecutarLoginDeFacebook();
        crearBotonSignup();
        crearBotonIngresoConEmail();
        crearBotonOlvidoDePassword();


        firebaseAuth = FirebaseAuth.getInstance();
        fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(LoginActivity.this, "Loggeado en Firebase papa!!!", Toast.LENGTH_SHORT).show();
                    goToMainScreen();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fireBaseAuthStateListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(fireBaseAuthStateListener);
    }

    /**************************Metodos del Login de Facebook*********************************/

    private void ejecutarLoginDeFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebookSigninButton);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                goToMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error en el login de FB", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(), "Error en el login de facebook a firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**************************Metodos del Login de Google*********************************/

    private void ejecutarLoginDeGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        SignInButton signInButton = findViewById(R.id.googleSignInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Login de Google falló.", Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
            goToMainScreen();
        } else {
            Toast.makeText(this, "Login failed!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Falló la autenticación con google a firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*******************************Métodos para el ingreso con email******************************/


    private void crearBotonSignup() {
        Button signUpButton = findViewById(R.id.botonCreaUnaCuenta);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RegisterActivity.class));
            }
        });
    }

    private void crearBotonIngresoConEmail() {
        Button botonIngresarConEmail = findViewById(R.id.botonIngresarConEmail);

        botonIngresarConEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                ingresarConEmail();
            }
        });
    }

    private void ingresarConEmail() {
        email = findViewById(R.id.campoEmailLogin);
        password = findViewById(R.id.campoPasswordEmailLogin);
        final TextInputLayout til = findViewById(R.id.textInputLayoutEmail);
        final TextInputLayout tilPass = findViewById(R.id.tilPass);
        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();

        if (strEmail.isEmpty() || !isEmailValid(strEmail)){
            til.setError("Debe ser una dirección válida de Email");
            til.setErrorEnabled(true);
            return;
        } else {
            til.setErrorEnabled(false);
        }

        if (strPassword.isEmpty()){
            Toast.makeText(this, "Fracaso Loggeando", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Éxito", Toast.LENGTH_SHORT).show();
                            tilPass.setErrorEnabled(false);
                            goToMainScreen();
                        } else {
                            tilPass.setError("      Fallo en el login");
                            email.setText("");
                            password.setText("");
                        }
                    }
                });
    }

    private void crearBotonOlvidoDePassword() {
        TextView olvidoDePassword = findViewById(R.id.olvideMiPassword);

        olvidoDePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMailRecuperoDePassword();
            }
        });
    }

    private void enviarMailRecuperoDePassword() {
        fragmentManager = getSupportFragmentManager();
        PasswordRecoveryFragment passwordRecoveryFragment = new PasswordRecoveryFragment();
        passwordRecoveryFragment.setRecoveryFragmentManipulator(this);
        fragmentManager.beginTransaction().replace(R.id.contenedorDeFragmentOlvidoDePassword, passwordRecoveryFragment).addToBackStack("tag").commitAllowingStateLoss();
    }

    private boolean isEmailValid (CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /*******************************Metodos comunes a todos los login******************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void interfaceHideKeyboard() {
        hideKeyboard();
    }

    @Override
    public void interfacePopBackStack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void displayEmailSentNotification() {
        RecoveryEmailSuccessNotification notification = new RecoveryEmailSuccessNotification();
        notification.setRecoveryFragmentManipulator(this);
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.contenedorDeFragmentOlvidoDePassword, notification).addToBackStack("tag").commitAllowingStateLoss();
    }
}
