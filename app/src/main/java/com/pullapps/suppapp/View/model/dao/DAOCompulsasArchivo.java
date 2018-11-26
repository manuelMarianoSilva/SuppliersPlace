package com.pullapps.suppapp.View.model.dao;

import com.pullapps.suppapp.View.utils.ResultListener;
import com.pullapps.suppapp.View.model.pojo.Compulsa;

import java.util.ArrayList;
import java.util.List;

public class DAOCompulsasArchivo {

    public void obtenerCompulsasArchivo(ResultListener<List<Compulsa>> controladorListener){
        List<Compulsa> listaCompulsas = new ArrayList<>();

        controladorListener.finish(listaCompulsas);

    }
}
