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
import com.pullapps.suppapp.View.model.pojo.Compulsa;
import com.pullapps.suppapp.View.utils.SeleccionadorDeArchivo;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;
import com.pullapps.suppapp.View.utils.SubidorDeArchivo;
import com.pullapps.suppapp.View.utils.VisualizadorDeListado;

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
    private Button btnSeleccionarArchivo;
    private DatabaseReference databaseReference;
    private String claveCompulsa;
    private String tituloCompulsa;
    private String descripcionCompulsa;
    private String fechaCierre;
    private String pliego;
    private VisualizadorDeListado visualizadorDeListado;
    private SeleccionadorDeFecha seleccionadorDeFecha;
    private SeleccionadorDeArchivo seleccionadorDeArchivo;
    private SubidorDeArchivo subidorDeArchivo;
    static int year_x, month_x, day_x;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        visualizadorDeListado = (VisualizadorDeListado) context;
        seleccionadorDeFecha = (SeleccionadorDeFecha) context;
        seleccionadorDeArchivo = (SeleccionadorDeArchivo) context;
        subidorDeArchivo = (SubidorDeArchivo) context;

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
        btnSeleccionarArchivo = (Button) view.findViewById(R.id.btnSeleccionarArchivo);
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

                subidorDeArchivo.subirArchivo(claveCompulsa);

                AgregarCompulsaActivity agregarCompulsaActivity = (AgregarCompulsaActivity) getContext();
                pliego = agregarCompulsaActivity.getUrlPliego();

                if (tituloCompulsa.isEmpty() || descripcionCompulsa.isEmpty()){
                    Toast.makeText(getContext(), "Debe ingresar valores en los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Compulsa compulsa = new Compulsa(claveCompulsa, tituloCompulsa, descripcionCompulsa, fechaCierre, pliego);
                    databaseReference.child(COMPULSA_NODO).child(claveCompulsa).setValue(compulsa);
                    visualizadorDeListado.visualizarListado();
                }
            }
        });

        btnCancelarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizadorDeListado.visualizarListado();
            }
        });

        btnSeleccionarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionadorDeArchivo.seleccionarArchivo();
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
