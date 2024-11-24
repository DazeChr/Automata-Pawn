package Automata;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Compilador {
    Nodo head, puntero, puntero_Ant, puntero_While;
    int estado = 0, columna, valorMT, numLinea = 1,caracter = 1;
    String lexema = "", valorVariable, valorVariableTemp;
    boolean errorFound = false, errorSinFound = false,errorSemFound = false , endOfFile = false;
 
    int contadorParentesis = 0, contadorCorchetes = 0; //*abiertos

    String archivo = InterfazCompilador.ruta;

    //Segun yo esto es semantico
    boolean variableExiste = false;
    boolean asigVa = false;
    boolean esif = false;
    boolean esWhile = false;
    
    int line;
    String tipo;
    String varAux;
    String oprel;
    String oprelWhile;
    
    public ArrayList<String> listaDeErrores = new  ArrayList<>();
    ArrayList<String> variables = new ArrayList<>();
    ArrayList<String> datosVariables = new ArrayList<>();

    String val1,val2,operacion;
    boolean op = false;
    
    
    public class TablaSimbolo{
        int numLine;
        String lexema;
        String tipoDato;
        String valor;
        
        public TablaSimbolo(int numLine,String lexema,String tipoDato, String valor){
            this.numLine = numLine;
            this.lexema = lexema;
            this.tipoDato = tipoDato;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return "{" + "numLinea = " + numLine + ", Lexema = " + lexema + ", Tipo = " + tipoDato + ", Valor = " + valor + "}";
        }  
    }

    public ArrayList<TablaSimbolo> simbolosTab =  new ArrayList<>();

    public Map<String, Number> valoresComparados = new HashMap<>();
    
    String comparadoStr1;
    String comparadoStr2;
    int comparado1 = 0;
    int comparado2 = 0;
    boolean Condition = false;
    boolean Loop = false;
    // semantico creo que termina hasta aca 

    String exp = "";
    String infx;
    String psfx;
    String resTemp;

    int ifcount = 1;

    String rutaArchivo = "CodigoIntermedio.txt";
    
    //*Matriz de Transiciones de Estado
    int Matriz[][] = {
        //      0   1	2	3	4	5	6	7	 8	 9	 10	 11	 12	 13	 14  15	 16	 17	18 19  20   21  22  23  24  25	26
        //	    L	d	.	+	-	*	/	^	 <	 >	 =	 !	 &	 |	 ,	 :	 ;	 (	 )	{	}	 "  eb  tab nl eof oc
        /*0*/ {  1,  2,126,103,104,105,  5,107,  9,  8, 10, 11, 12, 13,117,118,119,120,121,122,123, 14,  0,  0,  0,  0,505},
        /*1*/ {  1,  1,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100},
        /*2*/ {101,  2,  3,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101,101},
        /*3*/ {500,  4,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500},
        /*4*/ {102,  4,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102,102},
        /*5*/ {106,106,106,106,106,  6,106,106,106,106,106,106,106,106,106,106,106,106,106, 106106,106,106,106,106,106,106},
        /*6*/ {  6,  6,  6,  6,  6,  7,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,501,  6},
        /*7*/ {  6,  6,  6,  6,  6,  6,  0,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6,  6},
        /*8*/ {108,108,108,108,108,108,108,108,108,108,110,108,108,108,108,108,108,108,108,108,108,108,108,108,108,108,108},
        /*9*/ {109,109,109,109,109,109,109,109,109,109,111,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109},
        /*10*/{125,125,125,125,125,125,125,125,125,125,112,125,125,125,125,125,125,125,125,125,125,125,125,125,125,125,125},
        /*11*/{116,116,116,116,116,116,116,116,116,116,113,116,116,116,116,116,116,116,116,116,116,116,116,116,116,116,116},
        /*12*/{502,502,502,502,502,502,502,502,502,502,502,502,114,502,502,502,502,502,502,502,502,502,502,502,502,502,502},
        /*13*/{503,503,503,503,503,503,503,503,503,503,503,503,503,115,503,503,503,503,503,503,503,503,503,503,503,503,503},
        /*14*/{ 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,124, 14, 14,504, 14, 14},
    };

    //*Matriz de palabras reservadas
    String[][] PalabrasReservadas = {      
        //  0        1
        {"if"     ,"200"},
        {"else"   ,"201"},
        {"main"   ,"202"},
        {"while"  ,"203"},
        {"print"  ,"204"},
        {"new"    ,"205"},
        {"float"  ,"206"},
        {"int"    ,"207"},
        {"string" ,"208"},
        {"read"   ,"209"},
    };

    //*Matris de Errores
    String[][] ErroresLexicos = {       
        //     0                                        1
        {"Se espera un digito"                      , "500"},
        {"Se espera cerrar el comentario"           , "501"},
        {"Se espera '&'"                            , "502"},
        {"Se espera '|'"                            , "503"},
        {"Se esperar cerrar la cadena"              , "504"},
        {"Simbolo Invalido"                         , "505"},
    };

    //*Lectura del archivo
    RandomAccessFile file = null;
    
    public Compilador(){
        try{
            file = new RandomAccessFile(archivo,"r");

            while(caracter != -1){
                caracter = file.read();

                if(caracter == -1){
                    columna = 25;
                    endOfFile = true;
                }else if(Character.isLetter((char)caracter)){ //Caracter
                    columna = 0;
                    
                } else if(Character.isDigit((char)caracter)){ //Digito
                    columna = 1;
                }else{
                    switch((char) caracter){
                        case '.': columna = 2;
                        break;

                        case '+': columna = 3;
                        break;

                        case '-': columna = 4;
                        break;

                        case '*': columna = 5;
                        break;

                        case '/': columna = 6;
                        break;

                        case '^': columna = 7;
                        break;

                        case '>': columna = 8;
                        break;

                        case '<': columna = 9;
                        break;

                        case '=': columna = 10;
                        break;

                        case '!': columna = 11;
                        break;

                        case '&': columna = 12;
                        break;

                        case '|': columna = 13;
                        break;

                        case ',': columna = 14;
                        break;

                        case ':': columna = 15;
                        break; 

                        case ';': columna = 16;
                        break;

                        case '(': columna = 17;
                        break;

                        case ')': columna = 18;
                        break;

                        case '{': columna = 19;
                        break;

                        case '}': columna = 20;
                        break;

                        case '"': columna = 21;
                        break;

                        case ' ': columna = 22;
                        break;

                        //tab
                        case 9: columna = 23; 
                        break;

                        //nl
                        case 10: 
                        columna = 24; 
                        numLinea++;
                        break;

                        //nl
                        case 13: columna = 24; 
                        break;

                        default: 
                        columna = 26;
                        System.out.println((char) caracter);
                        break;
                    }                              
                }
                
                valorMT = Matriz[estado][columna];
                
                if (valorMT<100) {
                    estado=valorMT;
                    
                    if (estado == 0) {
                        lexema = "";
                    }else{
                        lexema=lexema+(char)caracter;
                    }
                }else if (valorMT>=100 && valorMT<500) {
                    if (valorMT == 100) {
                        PalabraReservada();                   
                    }
                    
                    if (valorMT == 100 ||valorMT == 101 || valorMT == 102 || valorMT == 106 ||valorMT == 108 ||
                        valorMT == 109 || valorMT == 116 || valorMT == 125 || valorMT>=200) {
                        file.seek(file.getFilePointer()-1);
                    
                    } else{
                        lexema = lexema + (char) caracter; 
                    }
                    
                    InsertarNodo();
                    estado = 0;
                    lexema = "";
                    
                }else{
                    InsertarListaDeError();
                    estado=0; 
                    lexema = ""; 
                }
            }
            ImprimirNodo();

            if (!errorFound) {
               Sintaxis();
               ImprimirListaVariables();
            }
            
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    //*Metodos del Analizador Lexico================================================================================================================
    private void PalabraReservada(){
        for (String[] PalRes : PalabrasReservadas) {
            if (lexema.equals(PalRes[0])) {
                valorMT = Integer.valueOf(PalRes[1]);  
            }
        }
    }
    
    private void InsertarListaDeError(){
        if((caracter != -1 && valorMT >= 500) || (caracter == -1 && valorMT >= 500)){
            for (String[]  Errores : ErroresLexicos) {
                if (valorMT == Integer.valueOf(Errores[1])) {
                    errorFound= true;
                    listaDeErrores.add("Error " + valorMT + ", " + Errores[0]  + " en la linea: " + (numLinea));
                }
            }
        }
    }
     
    void ImprimirListaVariables(){
        System.out.println("\nTabla de Simbolos");
        for (TablaSimbolo elemento : simbolosTab) {
            System.out.println(elemento.toString());
        }
    }
    
    private void InsertarNodo(){
        Nodo nodo = new Nodo(lexema, valorMT, numLinea);

        if(head == null){
            head = nodo;
            puntero = head;
            puntero_Ant = null;
        }else{
            if(puntero != null){
                puntero.next = nodo;
            }
            puntero_Ant = puntero;
            puntero = nodo;
        }
    }

    //? Troubleshooting================================================================================================================================================
    private void ImprimirNodo(){
        puntero = head;
        while (puntero != null){
            if(!puntero.lexema.isBlank()){
                System.out.println(puntero.lexema + " " + puntero.token + " " + puntero.linea);
                
            }
            puntero = puntero.next;
        }
    }
    
    //!===================================================================================================================================================
    //!Parte para el Sintactica del Automata

    public void Sintaxis(){
        LimpiarArchivo();
        ifcount=1;
        System.out.println("Analizador Sintactico\n");
        puntero = head;

        //*Inicio Sintactico
        while (puntero != null){
            if (puntero.token == 202){// main
                variables.add(puntero.lexema);
                PalRes_Semantico();
                puntero = puntero.next;  
                if (puntero.token == 120){// (
                    contadorParentesis++;
                    puntero = puntero.next;
                    if (puntero.token == 121){// ) 
                        contadorParentesis--;
                        if(contadorParentesis == 0){
                            errorSinFound = false;
                            puntero = puntero.next;
                        }
                        if(contadorParentesis < 0){
                            errorSinFound = true;
                            listaDeErrores.add("Error 521: Sobran parentesis o corchetes");
                        }
                        if (puntero.token == 122){// {
                            contadorCorchetes++;
                            puntero = puntero.next;
                            if(puntero.token == 122){
                                errorSinFound = true;
                                listaDeErrores.add("Error 510: Se espera '}'");
                            }else{
                                if(puntero.token == 123){

                                }else{
                                    Declarar_Var();
                                    SegmentoDatosIntermedio();
                                    puntero = puntero.next;
                                    GenerarIntermedio(rutaArchivo, "Segmento Codigo");
                                    statement();
                                }
                            }
                            if (puntero != null && puntero.token==123 ){ // }
                                contadorCorchetes--;
                                if(puntero.next != null){
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 521: Tokens fuera de lugar");
                                }
                                if(contadorCorchetes < 0){
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 521: Sobran parentesis o corchetes");
                                }
                                if(contadorCorchetes > 0){
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 510: Se espera '}'");
                                }
                            }else if (puntero == null){
                                errorSinFound = true;
                                listaDeErrores.add("Error 510: Se espera '}'");
                            }
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 509: Se espera '{'");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 508: Se espera ')'");
                    }
                }else{
                    errorSinFound = true;
                    listaDeErrores.add("Error 507: Se espera'('");
                }
            }else{
                errorSinFound = true;
                listaDeErrores.add("Error 506: Se espera un main");
            }
           break;
        } 
    }

    //!==============================================================================================================================================================
    //!Metodos usados para el sintactico 

    private void Declarar_Var(){
        while (puntero != null){
            if (puntero.token == 205){// new
                Type();
                if (puntero.token == 100 || puntero.token > 199){// id
                        variables.add(puntero.lexema);
                        line = puntero.linea;
                        PalRes_Semantico();
                        InsertarListSimbolos();
                        puntero = puntero.next;
                    while(  puntero.token == 117){// ,
                        if (puntero.token == 117){// ,
                            puntero = puntero.next;
                            if (puntero.token == 100){// id
                                variables.add(puntero.lexema);
                                InsertarListSimbolos();
                                puntero = puntero.next;
                            }else{
                                errorSinFound = true;
                                listaDeErrores.add("Error 514: Se espera idetificador despues de \",\"");
                            }
                        }
                    }

                    if (puntero.token == 119){// ;  
                        if (puntero.next.token == 205) {// new
                            puntero = puntero.next;
                            Declarar_Var();
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 513: Se espera ;");
                    }

                }else{
                    errorSinFound = true;
                    listaDeErrores.add("Error 512: Identificador no encontrado");
                }
            }else{
                errorSinFound = true;
                listaDeErrores.add("Error 511: Se espera 'new'");
            } 
            break;
        } 
    }

    private void Type(){
        puntero = puntero.next;
        //         float                     int                       string
        if((puntero.token == 206) || (puntero.token == 207) || (puntero.token == 208) ){
            tipo = puntero.lexema;
            InsertarListSimbolos();          
            puntero = puntero.next;
        }else{
            errorSinFound = true;
            listaDeErrores.add("Error 515: Se espera tipo de variable");
        }
    }

    private void PalRes_Semantico(){
        switch (puntero.token) {

            case 202:
            line = 1;
            tipo = "Palabra Reservada";
            InsertarListSimbolos();           
            break;

            case 200:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;

            case 201:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;

            case 203:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;

            case 204:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;

            case 205:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;

            case 206:
            tipo = "Palabra Reservada";
            InsertarListSimbolos(); 
            break;

            case 207:
            tipo = "Palabra Reservada";
            InsertarListSimbolos(); 
            break;

            case 208:
            tipo = "Palabra Reservada";
            InsertarListSimbolos(); 
            break;

            case 209:
            tipo = "Palabra Reservada";
            InsertarListSimbolos();  
            break;
        
            default:
            break;
        }
        
    }

    private void statement(){
        while (puntero != null){
            switch (puntero.token){
                case 100: //id
                    exp  = "";
                    valorVariable = puntero.lexema;
                    resTemp = puntero.lexema;
                    System.out.println(valorVariable);
                    ValidarVariableDeclarada();
                    puntero_Ant = puntero;
                    puntero = puntero.next;
                    exp = exp + puntero.lexema;
                    if (puntero.token == 125){ // =
                        exp  = "";
                        puntero_Ant = puntero;
                        puntero = puntero.next;
                        if(puntero.next.token == 103 || puntero.next.token == 104 || puntero.next.token == 105  ||
                           puntero.next.token == 106 || puntero.next.token == 101 || puntero.next.token == 102 || 
                           puntero.next.token == 100 || puntero.next.token == 120){
                            exp = exp + puntero.lexema;
                        }
                        Expresion_Simple();

                        if (!op) {
                            InsertarValorVariable();
                        }

                        val1=null;
                        val2=null;
                        
                        if (puntero.token == 119){ //;
                            if(Condition){
                                System.out.println("L"+(ifcount-1));   
                            }
                            infx = exp;
                            psfx = ProcOperaciones.ConversionIntoPostFix(infx);
                            if(psfx.contains("+")||psfx.contains("-")||psfx.contains("*")||psfx.contains("/")){
                                System.out.println("Expresion Infix: " + infx);
                                System.out.println("Expresion Postfix: " + psfx); 
                                ProcOperaciones.generateQuadruples(psfx);

                                System.out.println(resTemp +" = "+ ProcOperaciones.res);
                                resTemp = ProcOperaciones.res;  
                                if(Condition||(Loop&&ifcount<2)){
                                    System.out.println("L"+(ifcount));   
                                }                            
                            }else{
                                System.out.println(resTemp + " = " + varAux);
                            }

                            if(op){
                                for (int i = 0; i < simbolosTab.size(); i++){ 
                                    if (valorVariable.equals(simbolosTab.get(i).lexema)){
                                        TablaSimbolo sb = new TablaSimbolo(simbolosTab.get(i).numLine,simbolosTab.get(i).lexema,simbolosTab.get(i).tipoDato,resTemp);
                                        simbolosTab.set(i,sb);
                                        String contenido = "asigna"+"," + simbolosTab.get(i).lexema+","+ " " + "," + resTemp;
                                        GenerarIntermedio(rutaArchivo, contenido);
                                    }    
                                }
                                valoresComparados.put(valorVariable, Integer.parseInt(resTemp));
                                actualizarComparados(valorVariable);
                                op=false;
                            }
                            
                            ProcOperaciones.quadruples.clear();
                            exp = "";
                            puntero = puntero.next;
                            exp = exp + puntero.lexema;
                            statement();
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 513: Se espera ;");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 516: Se espera = u operador relacional");
                    }       
                break;

                case 204: //print
                    exp = "";
                    variables.add(puntero.lexema);
                    line = puntero.linea;
                    PalRes_Semantico();
                    puntero = puntero.next;
                    if (puntero.token == 120){// (
                        contadorParentesis++;
                        puntero = puntero.next;
                        if (puntero.token == 100 || puntero.token == 124) {// id o "caracter"
                            GenerarIntermedio(rutaArchivo, "Imprime"+ "," + " " + "," + " " + "," + puntero.lexema);
                            
                            puntero = puntero.next;
                            if (puntero.token == 121){// )
                                contadorParentesis--;
                                puntero = puntero.next;
                                if (puntero.token == 119){// ;
                                    puntero = puntero.next;
                                    statement();
                                }
                                else{
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 513: Se espera ;");
                                }
                            }
                            else{
                                errorSinFound = true;
                                listaDeErrores.add("Error 508: Se espera ')'");
                            }
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 512: Identificador no encontrado");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 507: Se espera'('");
                    }       
                break;

                case 209: //read
                    exp = "";
                    variables.add(puntero.lexema);
                    line = puntero.linea;
                    PalRes_Semantico();
                    puntero = puntero.next;
                    if (puntero.token == 120){// (
                        contadorParentesis++;
                        puntero = puntero.next;
                        if (puntero.token == 100){// id
                            puntero = puntero.next;
                            if (puntero.token == 121){// )
                                contadorParentesis--;
                                puntero = puntero.next;
                                if (puntero.token == 119){// ;
                                    puntero = puntero.next;
                                    statement();
                                }else{
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 513: Se espera ;");
                                }
                            }else{
                                errorSinFound = true;
                                listaDeErrores.add("Error 508: Se espera ')'");
                            }
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 512: Identificador no encontrado");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 507: Se espera'('");
                    }       
                break;

                case 200: //if
                    exp = "";
                    variables.add(puntero.lexema);
                    line = puntero.linea;
                    PalRes_Semantico();
                    puntero = puntero.next;
                    if (puntero.token == 120){// (
                        contadorParentesis++;
                        puntero = puntero.next;
                        valorVariable = puntero.lexema;
                        esif=true;
                        Expresion_Condicional();
                        if (puntero.token == 121){// )
                            exp = "";
                            contadorParentesis--;
                            puntero = puntero.next;
                            if (puntero.token == 122){// {
                                GenerarIntermedio(rutaArchivo,"{");
                                if(puntero.next == null){
                                }else{
                                    FlujoControlIfElse();
                                    exp = exp + puntero.lexema;
                                    contadorCorchetes++; 
                                }
                                op = false;
                                val1=null;
                                val2=null;
                        
                                statement();
                                if (puntero.token == 123){// }
                                    GenerarIntermedio(rutaArchivo,"}");
                                    if(puntero.next == null){
                                        errorSinFound = true;
                                    }else{ 
                                        contadorCorchetes--; 
                                        puntero = puntero.next;
                                    }
                                    if(puntero.token == 122){
                                        errorSinFound = true;
                                        listaDeErrores.add("Error 517: Se espera else");
                                    }else{
                                        if (puntero.token == 201){// else
                                            GenerarIntermedio(rutaArchivo,"else");
                                            variables.add(puntero.lexema);
                                            line = puntero.linea;
                                            PalRes_Semantico();
                                            puntero = puntero.next;
                                            if (puntero.token == 122){// {
                                                GenerarIntermedio(rutaArchivo,"{");
                                                exp = "";
                                                contadorCorchetes++;
                                                puntero = puntero.next;
                                                exp = exp + puntero.lexema;
                                                op = false;
                                                val1=null;
                                                val2=null;
                                                if(Condition == true){
                                                    while(!puntero.lexema.equals("}")){
                                                        puntero = puntero.next;
                                                    }
                                                }else{
                                                    statement();
                                                }
                                                if (puntero.token == 123){// }
                                                    GenerarIntermedio(rutaArchivo,"}");
                                                    contadorCorchetes--;
                                                    puntero = puntero.next;
                                                    statement();
                                                }else{
                                                    errorSinFound = true;
                                                    listaDeErrores.add("Error 510: Se espera '}'");
                                                }
                                            }else{
                                                errorSinFound = true;
                                                listaDeErrores.add("Error 509: Se espera '{'");
                                            }
                                        }else{
                                            statement();
                                        }
                                    }
                                }else{
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 510: Se espera '}'");
                                }
                            }else{
                                errorSinFound = true;
                                listaDeErrores.add("Error 509: Se espera '{'");
                            }
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 508: Se espera ')'");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 507: Se espera'('");
                    }   
                break;

                case 203: //while
                    exp = "";
                    esWhile = true;
                    variables.add(puntero.lexema);
                    line = puntero.linea;
                    PalRes_Semantico();
                    puntero = puntero.next;
                    if (puntero.token == 120){// (
                        contadorParentesis++;
                        puntero = puntero.next;
                        valorVariable = puntero.lexema;
                        esWhile = true;
                        Expresion_Condicional();
                        if (puntero.token==121){// )
                            exp = "";
                            contadorParentesis--;
                            puntero = puntero.next;
                            if (puntero.token==122){// {
                                puntero_While = puntero;
                                GenerarIntermedio(rutaArchivo,"{");
                                FlujoControlWhile();
                                contadorCorchetes++;
                                op = false;
                                val1=null;
                                val2=null;
                                while(Loop == true){
                                    statement(); 
                                    FlujoControlWhile();
                                }
                                if (puntero.token==123) {// }
                                    GenerarIntermedio(rutaArchivo,"}");
                                    contadorCorchetes--;
                                    puntero = puntero.next;
                                    statement();
                                }else{
                                    errorSinFound = true;
                                    listaDeErrores.add("Error 510: Se espera '}'");
                                }
                            }else{
                                errorSinFound = true;
                                listaDeErrores.add("Error 509: Se espera '{'");
                            }
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 508: Se espera ')'");
                        }
                    }else{
                        errorSinFound = true;
                        listaDeErrores.add("Error 507: Se espera'('"); 
                    }   
                break;

                default:
                if(puntero.token == 202){// main
                    errorSemFound = true;
                    listaDeErrores.add("Error 530: Main no debe estar en el bloque del programa."); 
                }
                if(puntero.token != 123){// = 
                    errorSinFound = true;
                    listaDeErrores.add("Error 523: Statement invalido");
                }
                break;
            }
            break;
        }
    }

    private void Expresion_Simple(){
        if ((puntero.token==101) || (puntero.token==102)) {
            Termino();
            Expresion_SimplePrime();
            if(puntero.token == 119){
                if(puntero_Ant.token == 125){
                    errorSinFound = true;
                    listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
                }
                if(puntero_Ant.token > 102 && puntero_Ant.token < 114){
                    errorSinFound = true;
                    listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
                }
            }
        }else if (puntero.token==124){
            ChecarTipo();
            Termino();
            Expresion_SimplePrime();
        }else if(puntero.token==120){
            Termino();   
            Expresion_SimplePrime();
            Expresion_Simple();
        }  
        else if (puntero.token==100){
            ValidarVariableDeclarada(); //*cadena variable o String
            if((puntero.next.token== 103) || (puntero.next.token==104) || (puntero.next.token== 105) || (puntero.next.token==106)){
                ChecarTipo();
                Termino(); // - + 
                Expresion_SimplePrime(); 
                ChecarTipo();
                if(puntero.token==120){
                    Termino();   
                    Expresion_SimplePrime();
                }
                if(puntero.next == null){
                    errorSinFound = true;
                    listaDeErrores.add("Error 513: Se espera ;");
                }else{
                    if((puntero.next.token== 103) || (puntero.next.token==104) || (puntero.next.token== 105) || (puntero.next.token==106)){
                        Expresion_SimplePrime(); 
                    }else{
                        if((puntero.token== 103) || (puntero.token==104) || (puntero.token== 105) || (puntero.token==106)){
                            Expresion_SimplePrime(); 
                        }
                        if(puntero.token == 101 || puntero.token == 102){
                            Expresion_Simple();
                        }
                    }
                }
            }else{
                ChecarTipo();
                Expresion_SimplePrime(); 
            }    
                   
        }else if (puntero.token==116){
            Termino();
            Expresion_SimplePrime();
        }else if ((puntero.token==103)||(puntero.token==104)) {
            NextNo();
            Termino();
            Expresion_SimplePrime();
        }else if(puntero.token == 121){
            if(puntero_Ant.token > 102 && puntero_Ant.token < 114){
                errorSinFound = true;
                listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
            }else{
                puntero = puntero.next;
                if(puntero.token == 121){
                    puntero = puntero.next;
                }
            }
        }else{
            if(puntero.token == 119){
                if(puntero_Ant.token == 125){
                    errorSinFound = true;
                    listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
                }
                if(puntero_Ant.token > 102 && puntero_Ant.token < 114){
                    errorSinFound = true;
                    listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
                }
            }else{
                errorSinFound = true;
                listaDeErrores.add("Error 518: Sintaxis invalida en expresion simple");
            }
        }
    }

    private void Termino(){
        Factor();
        TerminoPrime();
    }

    private void Factor(){
        switch (puntero.token){
            case 100: // id
                datosVariables.add(puntero.lexema);
                if (val1==null){
                    val1=puntero.lexema;
                }else{
                    val2=puntero.lexema;
                }   
                varAux=puntero.lexema;
                puntero_Ant = puntero;
                puntero = puntero.next;
                if(puntero.token != 119){
                    exp = exp + puntero.lexema; 
                }
            break;

            case 101:// digito entero
                datosVariables.add(puntero.lexema);
                if (val1==null){
                    val1=puntero.lexema;
                }else{
                    val2=puntero.lexema;
                }   
                varAux=puntero.lexema;
                puntero_Ant = puntero;
                puntero = puntero.next;
                if(puntero.token != 119){
                    exp = exp + puntero.lexema; 
                }      
            break;

            case 102:// digito decimal
                datosVariables.add(puntero.lexema);
                if (val1==null){
                    val1=puntero.lexema;
                }else{
                    val2=puntero.lexema;
                }   
                varAux=puntero.lexema;
                puntero_Ant = puntero;
                puntero = puntero.next;
                if(puntero.token != 119){
                    exp = exp + puntero.lexema; 
                }
            break;
            
            case 124: // "caracter"
                datosVariables.add(puntero.lexema);
                if (asigVa){
                    InsertarValorVariable();
                }   
                varAux=puntero.lexema;
                puntero_Ant = puntero;
                puntero = puntero.next;     
            break;

            case 120: //(
                contadorParentesis++;
                puntero_Ant = puntero;
                puntero = puntero.next;
                exp = exp + puntero.lexema;
                Expresion_Simple();
                if (puntero.token==121){
                    contadorParentesis++;
                    puntero_Ant = puntero;
                    puntero = puntero.next;
                    if(puntero.token != 119){
                        exp = exp + puntero.lexema; 
                    }
                }else{
                    if(puntero.token == 119 && puntero_Ant.token == 121){

                    }else{
                        if(puntero.token == 103 || puntero.token == 104){
                            puntero = puntero.next;
                            exp = exp + puntero.lexema;
                            Expresion_Simple();
                        }else if(puntero.token == 100 || puntero.token == 101 || puntero.token == 102){
                            Expresion_Simple();
                        }else{
                            errorSinFound = true;
                            listaDeErrores.add("Error 508: Se espera ')'");
                        }
                    }
                }   
            break;

            case 116: //* !
                puntero_Ant = puntero;
                puntero = puntero.next;
                Factor();
            break;

            default:
                errorSinFound = true;
                listaDeErrores.add("Error 519: Factor invalido");
            break;
        }
    }

    private void Expresion_SimplePrime(){
        if ((puntero.token== 103) || (puntero.token==104)) {
            Operador_Add();
            ChecarTipo();
            Termino();
            Expresion_SimplePrime();
        }else{
            if(puntero.token == 100 || puntero.token == 101 || puntero.token == 102){
                op=true;
                Termino();
                Expresion_SimplePrime();
            }
        }
    }

    private void TerminoPrime() {
        if ((puntero.token== 105) || (puntero.token==106)) {
            Operador_Mult();
            ChecarTipo();
            op=true;
            TerminoPrime();
        }
    }

    private void Expresion_Condicional() {
        Expresion_Simple();
        if(esWhile == true){
            Operador_RelacionalWhile();
        }else{
            Operador_Relacional();
        }
        Expresion_Simple();
        if (esif) {
            GenerarIntermedio(rutaArchivo,"if,"+oprel+","+ val1 + "," + val2);
            esif=false;
        }
        if (esWhile) {
            GenerarIntermedio(rutaArchivo,"while,"+oprelWhile+","+ val1 + "," + val2);
            esWhile=false;
        }     
    }

    private void Operador_Relacional() {
        if (puntero.token==108) {// <
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==110){//<=
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==111){// >=
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==109){//>
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==113){/* != */
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==112){//==
            oprel=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else{
            errorSinFound = true;
            listaDeErrores.add("Error 520: Falta un operador relacional");
        }
    }

    private void Operador_RelacionalWhile() {
        if (puntero.token==108) {// <
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==110){//<=
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==111){// >=
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==109){//>
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==113){/* != */
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else if(puntero.token==112){//==
            oprelWhile=puntero.lexema;
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
        else{
            errorSinFound = true;
            listaDeErrores.add("Error 520: Falta un operador relacional");
        }
    }

    private void NextNo() {
        if ((puntero.token==103)||(puntero.token==104)) {
            puntero_Ant = puntero;
            puntero = puntero.next;
        }
    }

    private void Operador_Add() {
        op=true;
        if (puntero.token==103) {
            operacion="suma";
            puntero_Ant = puntero;
            puntero = puntero.next;
            exp = exp + puntero.lexema; 
        }else if(puntero.token==104){
            operacion="resta";
            puntero_Ant = puntero;
            puntero = puntero.next;
            exp = exp + puntero.lexema;
        }
    }

    private void Operador_Mult() {
        op=true;
        if (puntero.token== 105) {
           operacion="multi";
           puntero_Ant = puntero;
           puntero = puntero.next;
           exp = exp + puntero.lexema;
        }else if(puntero.token==106){
            operacion="divi";
            puntero_Ant = puntero;
            puntero = puntero.next;
            exp = exp + puntero.lexema;
        }
    }

    //todo===================================================================================================================================================
    //todo Parte para el Semantica del Automata

    private void InsertarListSimbolos(){
        boolean noExiste = true;

        for (int i = 0;i < variables.size(); i++){
            
           TablaSimbolo sb = new TablaSimbolo(line, variables.get(i),tipo,"null");
            
            for (int j = 0;j < simbolosTab.size(); j++){
                
                if (!variables.get(i).equals(simbolosTab.get(j).lexema)){
                    noExiste=true;
                }else{
                    noExiste=false;
                    break;
                }  
            }
            
            if (noExiste==true) {
                if("Palabra Reservada".equals(tipo)){
                    if (puntero.linea == 1 && "main".equals(puntero.lexema)){
                        simbolosTab.add(sb);
                    }else{
                        if (puntero.next.token == 119){
                            errorSemFound = true;
                            listaDeErrores.add("Error 531: No pueden declararse Palabras Reservadas");
                        }else{
                            simbolosTab.add(sb);
                        }
                    }
                }else{
                    simbolosTab.add(sb);
                }
            }else{
                if(variables.get(i).equals(simbolosTab.get(0).lexema)){
                    errorSemFound = true;
                    listaDeErrores.add("Error 530: Main no debe estar en el bloque del programa.");
                }else{
                    if(!"Palabra Reservada".equals(tipo)){
                        errorSemFound = true;
                        listaDeErrores.add("Error 524: Esta variable ya existe: " + sb.lexema);
                    }   
                }
            }
        }
        
        variables.clear();
    }

    private void ValidarVariableDeclarada(){    
        boolean variableExiste = false;

        for (int i = 0; i < simbolosTab.size(); i++) {
            if (puntero.lexema.equals(simbolosTab.get(i).lexema)) {
                variableExiste = true;
            }
        }
        if (!variableExiste) {
            errorSemFound = true;
            listaDeErrores.add("Error 525: Variable no declarada: " + puntero.lexema);
        }
    }

    private void InsertarValorVariable(){
        for (int i = 0; i < simbolosTab.size(); i++){ 
            if (valorVariable.equals(simbolosTab.get(i).lexema)){
                String tipo = simbolosTab.get(i).tipoDato;
                if ("int".equals(tipo)){
                    if (esNumeroEntero(varAux)){
                        if (Integer.parseInt(varAux) > -2147483646  && Integer.parseInt(varAux) < 2147483646){
                            TablaSimbolo sb = new TablaSimbolo(simbolosTab.get(i).numLine,simbolosTab.get(i).lexema,simbolosTab.get(i).tipoDato,varAux);
                            simbolosTab.set(i,sb);
                            String contenido = "asigna"+"," + simbolosTab.get(i).lexema+"," + " " + "," + varAux;
                            GenerarIntermedio(rutaArchivo, contenido);
                            valoresComparados.put(valorVariable, Integer.parseInt(varAux));
                            actualizarComparados(valorVariable);
                        }else{
                            errorSemFound = true;
                            listaDeErrores.add("Error 526: Valor demasiado grande: " + varAux);
                        }  

                    } else {
                        errorSemFound = true;
                        listaDeErrores.add("Error 527: No es un numero entero: " + varAux);
                    } 
                }else if ("float".equals(tipo)) {
                    if (esNumeroDecimal(varAux)) {
                        if (Float.parseFloat(varAux) > -1000000.1000000  && Float.parseFloat(varAux) < 1000000.1000000) {
                            System.out.println(varAux);
                            TablaSimbolo sb = new TablaSimbolo(simbolosTab.get(i).numLine,simbolosTab.get(i).lexema,simbolosTab.get(i).tipoDato,varAux);
                            simbolosTab.set(i,sb);
                            String contenido = "asigna"+"," + simbolosTab.get(i).lexema+","+ " " + "," + varAux;
                            GenerarIntermedio(rutaArchivo, contenido);
                            valoresComparados.put(valorVariable, Float.parseFloat(varAux)); 
                            actualizarComparados(valorVariable);
                        }else{
                            errorSemFound = true;
                            listaDeErrores.add("Error 526: Valor demasiado grande: " + varAux);
                        } 
                    } else {
                        errorSemFound = true;
                        listaDeErrores.add("Error 528: No es un numero decimal: " + varAux);
                    }   
                }else if("string".equals(tipo)){
                    if(esNumeroEntero(varAux) || esNumeroDecimal(varAux)){
                        errorSemFound = true;
                        listaDeErrores.add("Error 533: No es un valor String: " + varAux);
                    }else{
                        simbolosTab.get(i).valor = varAux;
                    }
                }
            }
        }
    }

    private void actualizarComparados(String variable) {
        Number valor = valoresComparados.get(variable);
        
        if (valor != null) {
            if (comparadoStr1 != null && comparadoStr1.equals(variable)) {
                if (valor instanceof Integer) {
                    comparado1 = valor.intValue();
                }
            } else if (comparadoStr2 != null && comparadoStr2.equals(variable)) {
                if (valor instanceof Integer) {
                    comparado2 = valor.intValue();
                }
            } else {
                if (comparadoStr1 == null) {
                    comparadoStr1 = variable;
                    if (valor instanceof Integer) {
                        comparado1 = valor.intValue();
                    }
                } else if (comparadoStr2 == null) {
                    comparadoStr2 = variable;
                    if (valor instanceof Integer) {
                        comparado2 = valor.intValue();
                    }
                }
            }
        }
    }
    
    public static boolean esNumeroEntero(String texto) {
        return texto.matches("^\\d+$");
    }

    public static boolean esNumeroDecimal(String texto) {
        return texto.matches("^\\d*\\.\\d+$");
    }

    private void ChecarTipo(){
        for (int i = 0; i < simbolosTab.size(); i++){ 
            if (valorVariable.equals(simbolosTab.get(i).lexema)){
                String tipoLvalues = simbolosTab.get(i).tipoDato;
                if("int".equals(tipoLvalues)){
                    valorVariableTemp = valorVariable;
                    valorVariableTemp = puntero.lexema;
                    for (int j = 0; j < simbolosTab.size(); j++){ 
                        if (valorVariableTemp.equals(simbolosTab.get(j).lexema)){
                            String tipoVar = simbolosTab.get(j).tipoDato;
                            if ("float".equals(tipoVar)){
                                if (esNumeroEntero(simbolosTab.get(j).valor)){
                                } else {
                                    errorSemFound = true;
                                    listaDeErrores.add("Error 529: Variable incompatible: " + simbolosTab.get(j).lexema);
                                } 
                            }
                        }
                    }
                }else{
                    if("float".equals(tipoLvalues)){
                        valorVariableTemp = valorVariable;
                        valorVariableTemp = puntero.lexema;
                        for (int j = 0; j < simbolosTab.size(); j++){ 
                            if (valorVariableTemp.equals(simbolosTab.get(j).lexema)){
                                String tipoVar = simbolosTab.get(j).tipoDato;
                                if ("int".equals(tipoVar)){
                                    if (esNumeroDecimal(simbolosTab.get(j).valor)){
                                        System.out.println("valor valido");
                                    } else {
                                        errorSemFound = true;
                                        listaDeErrores.add("Error 529: Variable incompatible: " + simbolosTab.get(j).lexema);
                                    } 
                                }
                            }
                        }
                    }else{
                        if ("string".equals(tipoLvalues)) {
                            valorVariableTemp = valorVariable;
                            valorVariableTemp = puntero.lexema;
                            for (int j = 0; j < simbolosTab.size(); j++){ 
                                if (valorVariableTemp.equals(simbolosTab.get(j).lexema)){
                                    String tipoVar = simbolosTab.get(j).tipoDato;
                                    if ("float".equals(tipoVar)){
                                        if (esNumeroEntero(simbolosTab.get(j).valor)){
                                        } else {
                                            listaDeErrores.add("Error 529: Variable incompatible: " + simbolosTab.get(j).lexema);
                                        } 
                                    }else{
                                        if ("int".equals(tipoVar)) {
                                            if (esNumeroDecimal(simbolosTab.get(j).valor)){
                                            } else {
                                                errorSemFound = true;
                                                listaDeErrores.add("Error 529: Variable incompatible: " + simbolosTab.get(j).lexema);
                                            }  
                                        }else{
                                            if("string".equals(tipoVar)){
                                                if (puntero.next.token == 103 || puntero.next.token == 104 || puntero.next.token == 105 || puntero.next.token == 106 ) {
                                                    errorSemFound = true;
                                                    listaDeErrores.add("Error 532: No se pueden realizar operaciones con String");
                                                }else{

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }    
        }
    }

    private void FlujoControlIfElse(){
        switch (oprel) {
            case ">":
            System.out.println(">, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 > comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }

            }
            ifcount ++;
            break;

            case ">=":
            System.out.println(">=, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 >= comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "<":
            System.out.println("<, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 < comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "<=":
            System.out.println("<=, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 <= comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "!=":
            System.out.println("!=, "+comparado1+", "+comparado2+", t"+ifcount);    
            if(comparado1 != comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "==":
            System.out.println("==, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 == comparado2){
                Condition = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero.next;
            }else{
                Condition = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            default:
                break;
        }
    }

    
    private void FlujoControlWhile(){
        switch (oprelWhile) {
            case "==":
            System.out.println("L"+(ifcount));   
            System.out.println("==, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 == comparado2){
                Loop = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "!=":
            System.out.println("L"+(ifcount));   
            System.out.println("!=, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 != comparado2){
                Loop = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case ">":
            System.out.println("L"+(ifcount));   
            System.out.println(">, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 > comparado2){
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                Loop = true;
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
 
            ifcount ++;
            break;

            case ">=":
            System.out.println("L"+(ifcount));   
            System.out.println(">=, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 >= comparado2){
                Loop = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;
            
            case "<":
            System.out.println("L"+(ifcount));   
            System.out.println("<, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 < comparado2){
                Loop = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            case "<=":
            System.out.println("L"+(ifcount));   
            System.out.println("<=, "+comparado1+", "+comparado2+", t"+ifcount);
            if(comparado1 <= comparado2){
                Loop = true;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                puntero = puntero_While;
                puntero = puntero.next;
            }else{
                Loop = false;
                System.out.println("if_false, "+"t"+ifcount+", - , "+"L"+(ifcount+1));
                while(!puntero.lexema.equals("}")){
                    puntero = puntero.next;
                }
            }
            ifcount ++;
            break;

            default:
                break;
        }
    }

    //?===================================================================================================================================================
    //? Metodos para la Generacion de Codigo Intermedio

    public static void GenerarIntermedio(String rutaArchivo, String contenido) {
        try {
            File archivo = new File(rutaArchivo);
    
            List<String> lineas = new ArrayList<>();
            if (archivo.exists()) {
                BufferedReader lector = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = lector.readLine()) != null) {
                    lineas.add(linea);
                }
                lector.close();
            }
    
            String[] partes = contenido.split(",");
            boolean esAsignacion = partes.length >= 3 && partes[0].equals("asigna");
    
            boolean dentroDeBloque = false;
            int nivelBloque = 0;
            Stack<Integer> inicioBloque = new Stack<>();
            boolean dentroDeWhile = false;
    
            if (esAsignacion) {
                String variable = partes[1];
                String nuevoValor = partes[3];
                boolean asignacionActualizada = false;
    
                for (int i = 0; i < lineas.size(); i++) {
                    String linea = lineas.get(i).trim();
                    if (linea.equals("{")) {
                        nivelBloque++;
                        dentroDeBloque = true; 
                        inicioBloque.push(i);
                    } else if (linea.equals("}")) {
                        nivelBloque--;
                        inicioBloque.pop();
                        if (nivelBloque == 0) {
                            dentroDeBloque = false; 
                        }
                    } else if (linea.startsWith("while")) {
                        dentroDeWhile = true;
                    }
    
                    if (dentroDeBloque && !dentroDeWhile && linea.startsWith("asigna," + variable + ",")) {
                        String[] partesLinea = linea.split(",");
                        partesLinea[3] = nuevoValor; 
                        lineas.set(i, String.join(",", partesLinea)); 
                        asignacionActualizada = true;
                        break; 
                    }
                }

                if (esAsignacion && !dentroDeBloque) {
                    variable = partes[1];
                    nuevoValor = partes[3];
        
                    for (int i = 0; i < lineas.size(); i++) {
                        String[] lineaPartes = lineas.get(i).split(",");
                        if (lineaPartes.length >= 3 && lineaPartes[0].equals("asigna") && lineaPartes[1].equals(variable)) {
                            lineaPartes[3] = nuevoValor; 
                            lineas.set(i, String.join(",", lineaPartes)); 
                            asignacionActualizada = true;
                            break;
                        }
                    }
                }
    
                if (!asignacionActualizada) {
                    lineas.add(contenido);
                }
            } else {
                lineas.add(contenido);
            }

            BufferedWriter bufferEscritor = new BufferedWriter(new FileWriter(archivo));
            for (String linea : lineas) {
                bufferEscritor.write(linea);
                bufferEscritor.newLine();
            }
            bufferEscritor.close();
    
        } catch (IOException e) {
            System.out.println("Ocurrió un error al crear o escribir en el archivo.");
            e.printStackTrace();
        }
    }

    public static void LimpiarArchivo(){
        try {
            File archivo = new File("CodigoIntermedio.txt");
            FileWriter escritor = new FileWriter(archivo, false); 
            BufferedWriter bufferEscritor = new BufferedWriter(escritor);

            bufferEscritor.write("");
            bufferEscritor.close();

            System.out.println("Contenido del archivo eliminado.");

        } catch (IOException e) {
            System.out.println("Ocurrió un error al limpiar el archivo.");
            e.printStackTrace();
        }
    }
    
    public void SegmentoDatosIntermedio(){
        GenerarIntermedio(rutaArchivo, "Segmento dato");
        for (int i = 0; i < simbolosTab.size(); i++) {
            if(!simbolosTab.get(i).tipoDato.equals("Palabra Reservada")) {
                String contenido = "Declarar"+"," + simbolosTab.get(i).tipoDato+ ","+ " " + "," +simbolosTab.get(i).lexema ;
                GenerarIntermedio(rutaArchivo, contenido);
            }
        }
    }
}