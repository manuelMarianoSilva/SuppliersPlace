package com.pullapps.suppapp.View.view.Main;


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
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.CambiadorDeFragment;
import com.pullapps.suppapp.View.model.pojo.Compulsa;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;

import java.util.Calendar;

import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.DIALOG_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarCompulsaFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private EditText edtTituloCompulsa, edtDescripcionCompulsa, edtFechaCierre;
    private Button btnCancelarAgregarCompulsa;
    private Button btnAceptarAgregarCompulsa;
    private DatabaseReference databaseReference;
    private String claveCompulsa;
    private String tituloCompulsa;
    private String descripcionCompulsa;
    private String fechaCierre;
    private CambiadorDeFragment cambiadorDeFragment;
    private SeleccionadorDeFecha seleccionadorDeFecha;
    static int year_x, month_x, day_x;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiadorDeFragment = (CambiadorDeFragment) context;
        seleccionadorDeFecha = (SeleccionadorDeFecha) context;
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
        edtFechaCierre = (EditText) view.findViewById(R.id.edtFechaCierre);
        btnAceptarAgregarCompulsa = (Button) view.findViewById(R.id.btnAgregarCompulsa);
        btnCancelarAgregarCompulsa = (Button) view.findViewById(R.id.btnCancelarAgregarCompulsa);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnAceptarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                claveCompulsa = databaseReference.push().getKey();
                tituloCompulsa = edtTituloCompulsa.getText().toString();
                descripcionCompulsa = edtDescripcionCompulsa.getText().toString();
                fechaCierre = edtFechaCierre.getText().toString();

                if (tituloCompulsa.isEmpty() || descripcionCompulsa.isEmpty()){
                    Toast.makeText(getContext(), "Debe ingresar valores en los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Compulsa compulsa = new Compulsa(claveCompulsa, tituloCompulsa, descripcionCompulsa, fechaCierre);
                    databaseReference.child(COMPULSA_NODO).child(claveCompulsa).setValue(compulsa);
                    cambiadorDeFragment.cambiarFragment(0);
                }
            }
        });

        btnCancelarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiadorDeFragment.cambiarFragment(0);
            }
        });

        edtFechaCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                seleccionadorDeFecha.seleccionarFecha(DIALOG_ID);
            }
        });


        return view;
    }


}
