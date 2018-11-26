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
import com.pullapps.suppapp.View.utils.SubidorDeArchivo;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleCompulsaFragment extends Fragment {
    private TextView tvTituloCompulsa, tvDescripcionCompulsa, tvRutaArchivo;
    private Button btnVolverCompulsa, btnSeleccionarArchivo, btnSubirArchivo;
    private CambiadorDeFragment cambiadorDeFragment;
    private SeleccionadorDeArchivo seleccionadorDeArchivo;
    private SubidorDeArchivo subidorDeArchivo;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiadorDeFragment = (CambiadorDeFragment) context;
        seleccionadorDeArchivo = (SeleccionadorDeArchivo) context;
        subidorDeArchivo = (SubidorDeArchivo) context;

    }


    public DetalleCompulsaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_compulsa, container, false);

        tvTituloCompulsa = (TextView) view.findViewById(R.id.tvTituloCompulsa);
        tvDescripcionCompulsa = (TextView) view.findViewById(R.id.tvDescripcionCompulsa);
        tvRutaArchivo = (TextView) view.findViewById(R.id.tvRutaArchivo);
        btnVolverCompulsa = (Button) view.findViewById(R.id.btnVolverCompulsa);
        btnSeleccionarArchivo = (Button) view.findViewById(R.id.btnSeleccionarArchivo);
        btnSubirArchivo = (Button) view.findViewById(R.id.btnSubirArchivo);

        final Bundle bundle = getArguments();

        tvTituloCompulsa.setText(bundle.getString("title"));
        tvDescripcionCompulsa.setText(bundle.getString("description"));

        btnVolverCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiadorDeFragment.cambiarFragment(0);
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
