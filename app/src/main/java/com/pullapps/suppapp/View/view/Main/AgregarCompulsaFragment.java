package com.pullapps.suppapp.View.view.Main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.model.pojo.Compulsa;
import com.pullapps.suppapp.View.utils.SeleccionadorDeArchivo;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;
import com.pullapps.suppapp.View.utils.SubidorDeArchivo;
import com.pullapps.suppapp.View.utils.SubidorDeImagen;
import com.pullapps.suppapp.View.utils.VisualizadorDeListado;

import java.util.Calendar;

import static com.pullapps.suppapp.View.view.Main.DetalleCompulsaFragment.DIALOG_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarCompulsaFragment extends Fragment {
    private static final String COMPULSA_NODO = "Compulsas";
    private EditText edtTituloCompulsa, edtDescripcionCompulsa;
    private TextView tvFechaCierre, tvRutaArchivo;
    private Button btnCancelarAgregarCompulsa;
    private Button btnAceptarAgregarCompulsa;
    private DatabaseReference databaseReference;
    private FirebaseAuth authReference;
    private String claveCompulsa;
    private String tituloCompulsa;
    private String descripcionCompulsa;
    private String fechaCierre;
    private String pliego;
    private String imagenUsuario;
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
        edtTituloCompulsa = view.findViewById(R.id.edtTituloCompulsa);
        edtDescripcionCompulsa = view.findViewById(R.id.edtDescripcionCompulsa);
        tvFechaCierre = view.findViewById(R.id.tvFechaCierre);
        tvRutaArchivo = view.findViewById(R.id.tvRutaArchivo);
        btnAceptarAgregarCompulsa = view.findViewById(R.id.btnAgregarCompulsa);
        btnCancelarAgregarCompulsa = view.findViewById(R.id.btnCancelarAgregarCompulsa);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        authReference = FirebaseAuth.getInstance();


        btnAceptarAgregarCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtTituloCompulsa.getText().toString().isEmpty() || edtDescripcionCompulsa.getText().toString().isEmpty() || tvFechaCierre.getText().toString().length() > 10){
                    Toast.makeText(getContext(), "Debe ingresar valores en los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    claveCompulsa = databaseReference.push().getKey();
                    subidorDeArchivo.subirArchivo(claveCompulsa);

                    tituloCompulsa = edtTituloCompulsa.getText().toString();
                    descripcionCompulsa = edtDescripcionCompulsa.getText().toString();
                    fechaCierre = tvFechaCierre.getText().toString();
                    imagenUsuario = authReference.getCurrentUser().getPhotoUrl().toString();

                    AgregarCompulsaActivity agregarCompulsaActivity = (AgregarCompulsaActivity) getContext();
                    pliego = agregarCompulsaActivity.getUrlPliego();

                    Compulsa compulsa = new Compulsa(claveCompulsa, tituloCompulsa, descripcionCompulsa, fechaCierre, pliego, imagenUsuario);
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

        tvRutaArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionadorDeArchivo.seleccionarArchivo();
            }
        });

        tvFechaCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                seleccionadorDeFecha.seleccionarFecha(DIALOG_ID);
            }
        });


        return view;
    }


}
