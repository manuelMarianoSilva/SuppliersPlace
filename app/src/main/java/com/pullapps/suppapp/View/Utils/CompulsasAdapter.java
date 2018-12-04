package com.pullapps.suppapp.View.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pullapps.suppapp.R;
import com.pullapps.suppapp.View.model.pojo.Compulsa;
import com.pullapps.suppapp.View.view.Main.ListaCompulsasActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CompulsasAdapter extends RecyclerView.Adapter {
    private List<Compulsa> listaDeCompulsas;
    private SeleccionadorDeCompulsa seleccionadorDeCompulsa;

    public CompulsasAdapter(List<Compulsa> listaDeCompulsas, SeleccionadorDeCompulsa seleccionadorDeCompulsa) {
        this.listaDeCompulsas = listaDeCompulsas;
        this.seleccionadorDeCompulsa = seleccionadorDeCompulsa;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.celda_compulsa, parent, false);
        CompulsaViewHolder compulsaViewHolder = new CompulsaViewHolder(view);
        return compulsaViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Compulsa compulsa = listaDeCompulsas.get(position);
        CompulsaViewHolder compulsaViewHolder = (CompulsaViewHolder) holder;
        compulsaViewHolder.asignarCompulsa(compulsa);

    }

    @Override
    public int getItemCount() {
        return listaDeCompulsas.size();
    }

    private class CompulsaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgUsuario;
        private TextView tvTitulo;
        private TextView tvFecha;


        public CompulsaViewHolder(View itemView) {
            super(itemView);
            imgUsuario = itemView.findViewById(R.id.imgUsuario);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvFecha = itemView.findViewById(R.id.tvFecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer posicionCeldaSeleccionada = getAdapterPosition();
                    Compulsa compulsaSeleccionada = listaDeCompulsas.get(posicionCeldaSeleccionada);
                    seleccionadorDeCompulsa.seleccionarCompulsa(compulsaSeleccionada);
                }
            });
        }

        public void asignarCompulsa(Compulsa compulsa) {
            Picasso.with(itemView.getContext()).load(compulsa.getUrlImagen()).transform(new CropCircleTransformation()).into(imgUsuario);
            tvTitulo.setText(compulsa.getTitle());
            tvFecha.setText(compulsa.getFechaCierre());
        }
    }
}
