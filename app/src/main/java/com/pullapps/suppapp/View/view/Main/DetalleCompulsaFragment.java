package com.pullapps.suppapp.View.view.Main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.utils.CambiadorDeFragment;
import com.pullapps.suppapp.View.utils.SeleccionadorDeArchivo;
import com.pullapps.suppapp.View.utils.SeleccionadorDeFecha;
import com.pullapps.suppapp.View.utils.SubidorDeArchivo;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleCompulsaFragment extends Fragment {
    private TextView tvTituloCompulsa, tvDescripcionCompulsa;
    private Button btnVolverCompulsa, btnSeleccionarArchivo, btnSubirArchivo, btnSeleccionarFecha;
    private CambiadorDeFragment cambiadorDeFragment;
    private SeleccionadorDeArchivo seleccionadorDeArchivo;
    private SubidorDeArchivo subidorDeArchivo;
    private SeleccionadorDeFecha seleccionadorDeFecha;
    static int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiadorDeFragment = (CambiadorDeFragment) context;
        seleccionadorDeArchivo = (SeleccionadorDeArchivo) context;
        subidorDeArchivo = (SubidorDeArchivo) context;
        seleccionadorDeFecha = (SeleccionadorDeFecha) context;

    }


    public DetalleCompulsaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_compulsa, container, false);

        tvTituloCompulsa = view.findViewById(R.id.tvTituloCompulsa);
        tvDescripcionCompulsa = view.findViewById(R.id.tvDescripcionCompulsa);

        btnVolverCompulsa = view.findViewById(R.id.btnVolverCompulsa);
        btnSeleccionarArchivo = view.findViewById(R.id.btnSeleccionarArchivo);
        btnSubirArchivo = view.findViewById(R.id.btnSubirArchivo);
        btnSeleccionarFecha = view.findViewById(R.id.btnSeleccionarFecha);

        final Bundle bundle = getArguments();

        tvTituloCompulsa.setText(bundle.getString("title"));
        tvDescripcionCompulsa.setText(bundle.getString("description"));

        btnVolverCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiadorDeFragment.cambiarFragment(0);
            }
        });

        btnSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                year_x = calendar.get(Calendar.YEAR);
                month_x = calendar.get(Calendar.MONTH);
                day_x = calendar.get(Calendar.DAY_OF_MONTH);
                seleccionadorDeFecha.seleccionarFecha(DIALOG_ID);
            }
        });

        btnSeleccionarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionadorDeArchivo.seleccionarArchivo();

            }
        });

        btnSubirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subidorDeArchivo.subirArchivo(bundle.getString("id"));
            }
        });

        return view;
    }





}