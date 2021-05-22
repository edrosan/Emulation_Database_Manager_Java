package com.mycompany.b;

import java.io.File;

class MostrarArchivos {
    
    private String listaArchivos = "";

    public MostrarArchivos() {
        File rutaBD = new File("C:\\Users\\Eduardo\\Documents\\NetBeansProjects\\B\\BD");
        String[] nombreArchivos = rutaBD.list();

        for (int i =0; i< nombreArchivos.length; i++) {
                listaArchivos += nombreArchivos[i] + '\n';	
        }
    }
    public String getListaArchivos() {
        return listaArchivos;
    }
    
}
