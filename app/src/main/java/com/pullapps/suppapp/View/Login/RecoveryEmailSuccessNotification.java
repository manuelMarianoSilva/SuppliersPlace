package com.pullapps.suppapp.View.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pullapps.suppapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecoveryEmailSuccessNotification extends Fragment {


    private PasswordRecoveryFragment.RecoveryFragmentManipulator recoveryFragmentManipulator;

    public RecoveryEmailSuccessNotification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_email_success_notification, container, false);

        Button gotItButton = view.findViewById(R.id.gotItButton);

        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryFragmentManipulator.interfacePopBackStack();
            }
        });

        return view;
    }

    public void setRecoveryFragmentManipulator(PasswordRecoveryFragment.RecoveryFragmentManipulator recoveryFragmentManipulator) {
        this.recoveryFragmentManipulator = recoveryFragmentManipulator;
    }
}
