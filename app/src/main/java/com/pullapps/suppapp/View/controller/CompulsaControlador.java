package com.pullapps.suppapp.View.controller;

import android.content.Context;
import android.util.Log;

import com.pullapps.suppapp.View.model.dao.DAOCompulsasInternet;
import com.pullapps.suppapp.View.model.pojo.Compulsa;
import com.pullapps.suppapp.View.utils.HTTPConnectionManager;
import com.pullapps.suppapp.View.utils.ResultListener;

import java.util.List;

public class CompulsaControlador {
    private static final String TAG = "CompulsaControlador";
    HTTPConnectionManager httpConnectionManager = new HTTPConnectionManager();

    public void obtenerCompulsas(final ResultListener<List<Compulsa>> vistaListener, Context context) {
        ResultListener<List<Compulsa>> controladorListener = new ResultListener<List<Compulsa>>() {
            @Override
            public void finish(List<Compulsa> resultado) {
                vistaListener.finish(resultado);
            }
        };

        if(httpConnectionManager.isNetworkingOnline(context)==true){
            DAOCompulsasInternet daoCompulsasInternet = new DAOCompulsasInternet();
            daoCompulsasInternet.obtenerCompulsasInternet(controladorListener);
        } else {
            Log.w(TAG, "No hay internet");
        }

    }

}
