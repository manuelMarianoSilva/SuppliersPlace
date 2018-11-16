package com.pullapps.suppapp.View.Main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.model.Compulsa;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarCompulsaFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private EditText edtTituloCompulsa;
    private EditText edtDescripcionCompulsa;
    private Button btnCancelarAgregarCompulsa;
    private Button btnAceptarAgregarCompulsa;
    private DatabaseReference databaseReference;
    private String claveCompulsa;
    private String tituloCompulsa;
    private String descripcionCompulsa;
    private ListaCompulsasFragment.CambiarFragmentListener cambiarFragmentListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiarFragmentListener = (ListaCompulsasFragment.CambiarFragmentListener) context;
    }

    public AgregarCompulsaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_compulsa, container, false);
        edtTituloCompulsa = (EditText) view.findViewById(R.id.edtTituloCompulsa);
        edtDescripcionCompulsa = (EditText) view.findViewById(R.id.edtDescripcionCompulsa);
        btnAceptarAgregarCompulsa = (Button) view.findViewById(R.id.btnAgregarCompulsa);
        btnCancelarAgregarCompulsa = (Button) view.findViewById(R.id.btnCancelarAgregarCompulsa);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnAceptarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claveCompulsa = databaseReference.push().getKey();
                tituloCompulsa = edtTituloCompulsa.getText().toString();
                descripcionCompulsa = edtDescripcionCompulsa.getText().toString();

                if (tituloCompulsa.isEmpty() || descripcionCompulsa.isEmpty()){
                    Toast.makeText(getContext(), "Debe ingresar valores en los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Compulsa compulsa = new Compulsa(tituloCompulsa, descripcionCompulsa);
                    databaseReference.child(COMPULSA_NODO).child(claveCompulsa).setValue(compulsa);
                    cambiarFragmentListener.cambiarFragment(0);
                }
            }
        });

        btnCancelarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarFragmentListener.cambiarFragment(0);
            }
        });


        return view;
    }


}
