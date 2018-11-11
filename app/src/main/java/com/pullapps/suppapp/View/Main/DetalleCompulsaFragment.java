package com.pullapps.suppapp.View.Main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pullapps.suppapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleCompulsaFragment extends Fragment {
    private TextView tvTituloCompulsa;
    private TextView tvDescripcionCompulsa;
    private Button btnVolverCompulsa;
    private ListaCompulsasFragment.CambiarFragmentListener cambiarFragmentListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cambiarFragmentListener = (ListaCompulsasFragment.CambiarFragmentListener) context;
    }


    public DetalleCompulsaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_compulsa, container, false);

        tvTituloCompulsa = (TextView) view.findViewById(R.id.tvTituloCompulsa);
        tvDescripcionCompulsa = (TextView) view.findViewById(R.id.tvDescripcionCompulsa);
        btnVolverCompulsa = (Button) view.findViewById(R.id.btnVolverCompulsa);

        Bundle bundle = getArguments();

        tvTituloCompulsa.setText(bundle.getString("title"));
        tvDescripcionCompulsa.setText(bundle.getString("description"));

        btnVolverCompulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarFragmentListener.cambiarFragment(0);
            }
        });

        return view;
    }

}
