package Automata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;


public class Assembly {
    public static void Traducir() throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("CodigoIntermedio.txt"));
            StringBuilder dataSection = new StringBuilder(), codeSection = new StringBuilder();
            int cont = 0;
            
            boolean dataSectionStarted = false;
            boolean codeSectionStarted = false;
            
            String etiquetaUnica = null;
            
            int conIf = 0;
            int conWhile = 0;
            
            boolean dentroDeWhile = false;
            boolean Else = false;
            boolean num = false;

            Stack<String> etiquetaPila = new Stack<>();
            Stack<String> whileEtiquetaPila = new Stack<>();

            dataSection.append(".MODEL SMALL\n");
            dataSection.append(".586\n");
            dataSection.append(".STACK 100h\n");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                switch (tokens[0]) {
                    case "Declarar":
                        String tipoDeclaracion = tokens[1];
                        String variableDeclaracion = tokens[3];

                        if (!dataSectionStarted) {
                            dataSection.append("; variables\n");
                            dataSection.append(".DATA\n");
                            dataSectionStarted = true;
                        }

                        if ("float".equals(tipoDeclaracion)) {
                            dataSection.append(String.format("%s DD 0.0\n", variableDeclaracion));
                        } else if ("int".equals(tipoDeclaracion)) {
                            dataSection.append(String.format("%s DW 0\n", variableDeclaracion));
                        }
                    break;
                    
                    case "asigna":
                        String variableAsignacion = tokens[1];
                        String valorAsignacion = tokens[3];

                        if (!codeSectionStarted) {
                            codeSection.append(".CODE\n");
                            codeSection.append(".STARTUP\n");
                            codeSectionStarted = true;
                        }

                        try {
                            int numericValue = Integer.parseInt(valorAsignacion);
                            codeSection.append(String.format("MOV %s, %d\n", variableAsignacion, numericValue));
                        } catch (NumberFormatException e) {
                            codeSection.append(String.format("MOV %s, EAX\n", variableAsignacion, valorAsignacion));
                        }
                    break;

                    case "Imprime":
                    String mensaje = tokens[3];
                
                    if (!codeSectionStarted) {
                        codeSection.append(".CODE\n");
                        codeSection.append(".STARTUP\n");
                        codeSectionStarted = true;
                    }
                
                    if (mensaje.startsWith("\"") && mensaje.endsWith("\"")) {
                        cont++;
                        String cadena = mensaje.substring(1, mensaje.length() - 1);
                        Cadena(dataSection, cadena, cont);
                        codeSection.append(String.format("MOV AH, 9H\nMOV DX, OFFSET subcadena%d\nINT 21H\n", cont));
                    } else {
                        num = true;
                        codeSection.append(String.format("""
                                                        MOV AX, %s
                                                        CALL IntToStr
                                                        MOV AH, 9H
                                                        LEA DX, OFFSET aaaa
                                                        INT 21H
                                                        """, mensaje));
                        codeSection.append("""
                                                        MOV AH, 9
                                                        LEA DX, OFFSET tempString
                                                        INT 21H
                                                        """);
                    }
                    break;
                

                    case "if":
                        conIf = conIf + 1;
                        String variableCondicion1 = tokens[2];
                        String operador = tokens[1];
                        String variableCondicion2 = tokens[3];

                        codeSection.append(String.format("MOV AX, %s\n", variableCondicion1));
                        codeSection.append(String.format("CMP AX, %s\n", variableCondicion2));

                        switch (operador) {
                            case ">":
                                codeSection.append("JG ");
                                break;
                            case "<":
                                codeSection.append("JL ");
                                break;
                            case ">=":
                                codeSection.append("JGE ");
                                break;
                            case "<=":
                                codeSection.append("JLE ");
                                break;
                            case "==":
                                codeSection.append("JE ");
                                break;
                            case "!=":
                                codeSection.append("JNE ");
                                break;
                            default:
                                break;
                        }

                        etiquetaUnica = "etiqueta_" + conIf;
                        codeSection.append(etiquetaUnica).append("\n");
                        etiquetaPila.push(etiquetaUnica);

                        codeSection.append(String.format("JMP fin_%s\n", etiquetaUnica));
                        codeSection.append(etiquetaUnica).append(":\n");
                    break;

                    case "while":
                        conWhile = conWhile + 1;
                        String variableCondicion1While = tokens[2];
                        String operadorWhile = tokens[1];
                        String variableCondicion2While = tokens[3];

                        codeSection.append("while_").append(conWhile).append(":\n");
                        codeSection.append(String.format("MOV AX, %s\n", variableCondicion1While));
                        codeSection.append(String.format("CMP AX, %s\n", variableCondicion2While));

                        switch (operadorWhile) {
                            case ">":
                                codeSection.append("JG ");
                                break;
                            case "<":
                                codeSection.append("JL ");
                                break;
                            case ">=":
                                codeSection.append("JGE ");
                                break;
                            case "<=":
                                codeSection.append("JLE ");
                                break;
                            case "==":
                                codeSection.append("JE ");
                                break;
                            case "!=":
                                codeSection.append("JNE ");
                                break;
                            default:
                                break;
                        }

                        etiquetaUnica = "etiqueta_while_" + conWhile;
                        codeSection.append(etiquetaUnica).append("\n");
                        etiquetaPila.push(etiquetaUnica);

                        codeSection.append(String.format("JMP fin_%s\n", etiquetaUnica));
                        codeSection.append(etiquetaUnica).append(":\n");
                        break;

                    case "else":
                        Else = true;
                    break;
                    
                    case "}":
                    if (dentroDeWhile) {
                        if (!whileEtiquetaPila.isEmpty()) {
                            String etiquetaWhile = whileEtiquetaPila.pop();
                            codeSection.append("JMP while_").append(etiquetaWhile).append("\n");
                            dentroDeWhile = false;
                        }
                    } else {
                        if(!Else){
                            if (!etiquetaPila.isEmpty()) {
                                etiquetaUnica = etiquetaPila.pop();
                            }
                            codeSection.append("fin_").append(etiquetaUnica).append(":\n");
                        }
                    }
                    Else = false;
                    break;
                }
            }

            codeSection.append(".EXIT\n");
            if (num) {

                dataSection.append("""
                                   aaaa DB "$"
                                   tempString DB 15 DUP('$')

                                   ten DW 10           
                                   tempInt DW ?

                                   """);

                codeSection.append("""

                                    IntToStr PROC NEAR
                                        MOV tempInt, AX  
                                        CALL WordToStr
                                        RET
                                    IntToStr ENDP
                                   
                                    WordToStr PROC NEAR
                                        PUSH AX
                                        PUSH DX
                                        PUSH CX
                                        MOV AX, tempInt  ; Cargamos el valor de tempInt en AX
                                        MOV CX, 0
                                        MOV BX, OFFSET tempString
                                        ADD BX, 14  ; Empezamos desde el final de tempString
                                    NEXT_DIGIT:
                                        XOR DX, DX
                                        DIV ten
                                        ADD DL, '0'
                                        MOV [BX], DL
                                        DEC BX
                                        INC CX
                                        TEST AX, AX
                                        JNZ NEXT_DIGIT
                                        INC BX  ; Ajustamos BX para apuntar al primer dígito
                                        MOV SI, BX
                                        MOV DI, OFFSET tempString
                                    MOVE_DIGITS:
                                        MOV AL, [SI]
                                        MOV [DI], AL
                                        INC SI
                                        INC DI
                                        LOOP MOVE_DIGITS
                                        MOV BYTE PTR [DI], '$'
                                        POP CX
                                        POP DX
                                        POP AX
                                        RET
                                    WordToStr ENDP
                                   """);
            }
            codeSection.append("END\n");

            try {
                FileWriter writer = new FileWriter("CodeAsm.asm");
                writer.write(dataSection.toString());
                writer.write(codeSection.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            reader.close();

        } catch (IOException e) {
        }
    }

    //*============================================================================================================================================================
    //*Metodos Extra de Assembly
    
    private static void Cadena(StringBuilder ensambladorCode, String cadena, int startIndex) {
        int maxLength = 255;
        int numSubcadenas = (int) Math.ceil((double) cadena.length() / maxLength);
        
        for (int i = 0; i < numSubcadenas; i++) {
            int start = i * maxLength;
            int end = Math.min(start + maxLength, cadena.length());
            String subcadena = cadena.substring(start, end);
            
            ensambladorCode.append(String.format("subcadena%d DB \"%s$\"\n", startIndex + i, subcadena));
        }
    }
}
