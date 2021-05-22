package com.mycompany.b;

import java.util.Objects;
import javax.swing.JOptionPane;

public class crearLog {
    
    public crearLog(String consulta) {
        analizarConsulta(consulta);	
    }
	

    private void analizarConsulta(String consulta) {
        MostrarArchivos mostrar = new MostrarArchivos();
        String[] comandos = consulta.split("\n");
      
        if (comandos[1].split(" ")[0]!= null && comandos.length > 1){
            if (comandos[1].split(" ")[0].equals("from")){
               String Archivos = comandos[1].substring(5);
               Archivos = Archivos.replace(" ", "");
               String[] numArchivos = Archivos.split(",");
               String [][] logProductoCar;
               String [][] logSeleccion = null;
               VentanaTabla tabla1  =  new VentanaTabla();
               for (int i = 0; i < numArchivos.length; i++){//mostrar toda la tabla en la que se hara la consulta
                    String [][] log = obtenerLog (numArchivos[i]);
                    String [][] logProyeccion = operadorProyeccion(log ,log[0]);
                    tabla1.agregarpestaña(logProyeccion, "Tabla "+numArchivos[i].toUpperCase()); 
               }
               
               if (comandos.length>2 &&comandos[2].split(" ")[0].equals("where")){
                    String comparacion = comandos[2].substring(6);
                    comparacion = comparacion.replace(" ", "");
                    String[] condiciones = comparacion.split("=");
                    if (condiciones[0].contains(".") && condiciones[1].contains(".")){
                        String [][] log = obtenerLog (numArchivos[0]);
                        String [][] log2 = obtenerLog (numArchivos[1]);
                        logProductoCar = productoCartesiano(log,log2,numArchivos[0],numArchivos[1]);
                        tabla1.agregarpestaña(logProductoCar, "Producto Cartesiano");
                        System.out.println(comparacion);
                        logSeleccion = operadorSeleccion(comparacion,logProductoCar);
                        tabla1.agregarpestaña(logSeleccion, "Seleccion");
                        if (comandos[0].split(" ")[0].equals("select")){
                            String obtenerColumnasPedidas = comandos[0].substring(7);    
                            String peticion = obtenerColumnasPedidas.replace(" ", "");    
                            if(peticion.equals("*")){
                                peticion="";
                                for (int i = 0; i < numArchivos.length; i++){
                                    String [][] log1 = obtenerLog (numArchivos[i]);
                                    for (int j = 0; j < log1[0].length; j++){
                                        peticion += log1[0][j] + ",";
                                    }      
                                }        
                            }
                            String[] columnasPedidas = peticion.split(",");
                            String[][] logProyeccion = operadorProyeccion(logSeleccion,columnasPedidas);
                            tabla1.agregarpestaña(logProyeccion, "Proyeccion");
                       }    
                    }else{
                        logSeleccion = operadorSeleccion(comparacion.toUpperCase(), obtenerLog (numArchivos[0]));
                        tabla1.agregarpestaña(logSeleccion, "Seleccion");
                        if (comandos[0].split(" ")[0].equals("select")){
                            String obtenerColumnasPedidas = comandos[0].substring(7);
                            String peticion = obtenerColumnasPedidas.replace(" ", "");
                            if(peticion.equals("*")){
                                peticion="";
                                for (int i = 0; i < numArchivos.length; i++){
                                    String [][] log1 = obtenerLog (numArchivos[i]);
                                    for (int j = 0; j < log1[0].length; j++){
                                        peticion += log1[0][j] + ",";
                                    }      
                                } 
                            }
                            String[] columnasPedidas = peticion.split(",");
                            String[][] logProyeccion = operadorProyeccion(logSeleccion,columnasPedidas);
                            tabla1.agregarpestaña(logProyeccion, "Proyeccion");
                       }
                    }    
                }else{
                   System.out.println("si entra");
                    if (comandos[0].split(" ")[0].equals("select")){
                        String [][] log1 = null;
                            String obtenerColumnasPedidas = comandos[0].substring(7);
                            String peticion = obtenerColumnasPedidas.replace(" ", "");
                            if(peticion.equals("*")){
                                peticion="";
                                for (int i = 0; i < numArchivos.length; i++){
                                    log1 = obtenerLog (numArchivos[i]);
                                    for (int j = 0; j < log1[0].length; j++){
                                        peticion += log1[0][j] + ",";
                                    }      
                                } 
                            }
                            String[] columnasPedidas = peticion.split(",");
                            String[][] logProyeccion = operadorProyeccion(log1,columnasPedidas);
                            tabla1.agregarpestaña(logProyeccion, "Proyeccion");
                       }
                   
               }
               tabla1.setVisible(true);
            }
           
       }
    }
    
    
    private int buscadorComando(String[] consulta, String comandoABuscar) {
        int  posicion = -1;
        for (int i = 0; i < consulta.length ; i++) {
            if (consulta[i].equals(comandoABuscar)) {
                posicion = i;
            }
        }
        return posicion ;
    }
	

	//operadores

    private String[][] operadorProyeccion(String[][] log, String[] colUsuarioPide) {//selec
        String[][] logx = new String[log.length][colUsuarioPide.length];
         
        for (int i = 0; i < log[0].length; i++) { //nos movemos por las columnas

            for (int j = 0; j < colUsuarioPide.length ; j++) {//columnas que el usuario pide

                if(log[0][i].equals(colUsuarioPide[j])) {//comparamos las columnas de la tabla con las que pide el usuario
                    for (int k = 0; k < log.length; k++) {//nos movemos verticalmente, columna por columna
                            logx[k][j] = log[k][i];
                    }
                }
            }
        }
        return logx;
    }
    
    private String[][] operadorSeleccion(String condiciones, String[][] log) {// where
        String[] numerodeCondiciones = condiciones.split(",");
        
        String[][] logx = new String[log.length][log[0].length];
        
        boolean tipoSeleccion; 
        
        if (numerodeCondiciones[0].contains("."))
            tipoSeleccion = false;
        else
            tipoSeleccion = true;
        
        if (tipoSeleccion){
           
            for(int i = 0; i < logx[0].length; i++) //logx[0]=fila de los titulos de las columnas donde se buscara
                logx[0][i] = log[0][i];
            
            
            for (int fila = 1; fila < log.length ; fila++) {
                
                    String[] condicion = numerodeCondiciones[0].split("=");
                    
                    for (int columan = 0; columan < log[0].length ; columan++) {//nos movemos por los nombres de las columnas
                        String[] palabra = log[fila][columan].split(" ");
                        
                        if ( condicion[0].equals(log[0][columan])   &&    condicion[1].equals(palabra[0])) {
                            for (int i= 0; i< log[0].length ;i++)
                                logx[fila][i] = log[fila][i];
                        }
                    }
                
            }
        }else{
            
            String[] condicion = numerodeCondiciones[0].split("=");
            
            for (int j= 0 ; j< log[0].length; j++){
                    logx[0][j] = log[0][j];
            }
            
            for (int filas = 1; filas < log.length; filas ++){
                for (int i = 0 ; i < log[0].length; i ++){
                    
                    if (log[0][i].equals(condicion[0])){ //si se encuentra la primera parte
                        
                        for (int j = 0 ; j < log[0].length; j ++){
                            if (log[0][j].toLowerCase().equals(condicion[1])){ //si se encuentra la segunda parte
                               if (log[filas][i].equals(log[filas][j])){
                                   for (int colu = 0; colu < log[0].length; colu++){
                                       logx[filas][colu] = log[filas][colu];
                                   }  
                                }
                            }
                        }
                    }
                }
            }
        }
            
        return limpiarNull(logx);
    }
    
    private String[][] limpiarNull(String[][] log) {
        String[][] logx;
        int contNull = 0;
        int tamañorealtabla = log.length;
        int contador = 0;
        for (int i = 1; i < log.length; i++)
            if ( Objects.equals(null,log[i][0]) )
                contNull++;
        tamañorealtabla -= contNull;
        logx = new String[tamañorealtabla][log[0].length ];
        for (int i = 0; i < log.length; i++) {
            if ( !(Objects.equals(null,log[i][0])) ) {
                for (int j = 0; j< log[0].length; j++) {
                    logx[contador][j] = log[i][j];
                }
                contador++;
            }
        }
        return logx;
    }
    
    private String[][] productoCartesiano(String[][] log1,String[][] log2, String name1, String name2){
        int numFilasTabla1 = log1.length - 1;
        int numFilasTabla2 = log2.length - 1;
        int numColumnasTabla1 = log1[0].length;
        int numColumnasTabla2 = log2[0].length;
        int columnasTablaFinal = numColumnasTabla1 + numColumnasTabla2;
        String[][] logx= null;
        
        logx = new String[(numFilasTabla1*numFilasTabla2) + 1][ columnasTablaFinal];
        
        for ( int i = 0 ; i < columnasTablaFinal ; i++  ){
            if (i < numColumnasTabla1)
                logx[0][i] = name1+"."+log1[0][i].toLowerCase() ;
            else
                logx[0][i] = name2+"."+log2[0][i - numColumnasTabla1].toLowerCase();
        }
        
        int posicionFilaLogx = 1;
        
        for ( int i = 1 ; i < numFilasTabla1 +1; i++){//filas de la primera tabla
            for (int j = 1; j < numFilasTabla2 +1 ; j++){
                 for ( int k = 0 ; k < columnasTablaFinal ; k++  ){
                    if (k < numColumnasTabla1)
                        logx[posicionFilaLogx][k] = log1[i][k];
                    else
                        logx[posicionFilaLogx][k] = log2[j][k - numColumnasTabla1];
                }
                 posicionFilaLogx++;
            }
        }
        return logx;
    }
    
    
    private String obtenerInfArchivo(String nombreArchivo){
        String infArchivo = "";
        abrirArchivo archivoConsulta = new abrirArchivo();
        archivoConsulta.leer(nombreArchivo.toUpperCase());
        infArchivo = archivoConsulta.getInformacion();
        return infArchivo;
    }
    
    
    private String[][] obtenerLog (String nomArchivo){//estructura temporal
        String[][] logx;//log (tabla)
        String [] filas;
        String  columna = "";
        String  infArchivo = "";
        String [] descriptor;
        String tamañoVariables = "";
        String nombreColumnas = "";
        int tamaño = 0;
        int numCaracteres = 0;
        int i = 1;
        
        infArchivo += obtenerInfArchivo(nomArchivo);
        
        filas = infArchivo.split("\n");
        descriptor = filas[0].split(",");
        
        while(i != descriptor.length - 1) {//primer fila, donde empiezan  los datos de la tabla
            if((descriptor[i].charAt(0) >= 48 && descriptor[i].charAt(0) <= 57) && (descriptor[i+1].charAt(0) >= 48 && descriptor[i+1].charAt(0) <= 57)) {//buscamos el tamaño de los campos de la tabla 
                tamaño = (Integer.parseInt( descriptor[i+1])+1) - Integer.parseInt( descriptor[i]);
                tamañoVariables +=  "" +tamaño + ',';  //
            }else if((descriptor[i].charAt(0) < 48 || descriptor[i].charAt(0) > 57)){
                nombreColumnas +=  descriptor[i] + ',';
            }
            i++;
        }
        logx = new String[filas.length][nombreColumnas.split(",").length];
        
        for (i = 0; i < (filas.length) ; i ++){
            for (int j = 0; j < (nombreColumnas.split(",").length) ; j ++ ){
                if ( i == 0){//se copian los titulos
                    logx[i][j] = nombreColumnas.split(",")[j];   
                }else{
                    for ( int k = numCaracteres ; k < (Integer.parseInt(tamañoVariables.split(",")[j]) +numCaracteres) ; k ++)
                    {
                        columna +=  Character.toString(filas[i].charAt(k));
                    }
                    logx[i][j] = columna;
                    columna="";
                }
                numCaracteres += Integer.parseInt(tamañoVariables.split(",")[j]);
            }
            numCaracteres = 0;
        }
        
        return logx;
    }
    
    
}
