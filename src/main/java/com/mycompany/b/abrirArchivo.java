package com.mycompany.b;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class abrirArchivo {
    private String informacion = "";
    
    public void leer(String namefile) {
        File file = new File(namefile);
        try {
            BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\Eduardo\\Documents\\NetBeansProjects\\B\\BD\\"+namefile+".txt"));
            String buffer="";

            while (( buffer = bf.readLine() )!= null) 
                informacion = informacion  + buffer + '\n';
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getInformacion() {
        return informacion;
    }

}
