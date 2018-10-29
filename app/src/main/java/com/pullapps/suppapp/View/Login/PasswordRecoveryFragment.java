package com.pullapps.suppapp.View.Login;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pullapps.suppapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRecoveryFragment extends Fragment {


    private EditText recoveryEmailAdress;
    private TextInputLayout recoveryEmailTextInputLayout;
    private Button sendEmailButton, cancelRecoveryButton;
    private FirebaseAuth firebaseAuth;
    private RecoveryFragmentManipulator recoveryFragmentManipulator;

    public PasswordRecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_recovery, container, false);

        recoveryEmailAdress = view.findViewById(R.id.recoveryEmailAdress);
        recoveryEmailTextInputLayout = view.findViewById(R.id.recoveryEmailTextInputLayout);
        sendEmailButton = view.findViewById(R.id.sendEmailButton);
        cancelRecoveryButton = view.findViewById(R.id.cancelRecoveryButton);
        firebaseAuth = FirebaseAuth.getInstance();

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryFragmentManipulator.interfaceHideKeyboard();
                sendRecoveryEmail();
            }
        });

        cancelRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryFragmentManipulator.interfacePopBackStack();
            }
        });

        return view;
    }

    private void sendRecoveryEmail() {
        String strRecoveyEmail = recoveryEmailAdress.getText().toString();

        if (strRecoveyEmail.isEmpty() || !isEmailValid(strRecoveyEmail)){
            recoveryEmailTextInputLayout.setError("Must be your registered email adress");
            return;
        } else {
            recoveryEmailTextInputLayout.setErrorEnabled(false);
        }

        firebaseAuth.sendPasswordResetEmail(strRecoveyEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    recoveryFragmentManipulator.displayEmailSentNotification();
                } else {
                    recoveryEmailTextInputLayout.setError("An error occured! \n Please try again.");
                }
            }
        });
    }

    private boolean isEmailValid (CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /***********************Interface de comunicación con la actividad de login********************/

    public void setRecoveryFragmentManipulator(RecoveryFragmentManipulator recoveryFragmentManipulator) {
        this.recoveryFragmentManipulator = recoveryFragmentManipulator;
    }

    public interface RecoveryFragmentManipulator {
        void interfaceHideKeyboard();
        void interfacePopBackStack();
        void displayEmailSentNotification();
    }

}
