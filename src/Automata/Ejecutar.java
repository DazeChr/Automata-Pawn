package Automata;

import java.io.*;
import java.nio.file.*;

public class Ejecutar {
    public static void Asm() throws IOException{
        //* Ruta al archivo .asm
        String asmFilePath = "CodeAsm.asm";
        //* Ruta al ejecutable de DOSBox
        String dosBoxPath = "C:\\Program Files (x86)\\DOSBox-0.74-3\\DOSBox.exe";
        //* Ruta al directorio de MASM en DOSBox
        String masmDirectory = "C:\\MASM611\\MASM611\\BIN";
        //* Nombre del archivo sin la extensión .asm
        String fileNameWithoutExtension = "CodeAsm";

         try {
            Path sourcePath = Paths.get(asmFilePath);
            Path destinationPath = Paths.get(masmDirectory, fileNameWithoutExtension + ".asm");

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            
            //* Comandos
            String[] commands = {
                dosBoxPath,
                "-c", "mount c " + masmDirectory,
                "-c", "c:",
                "-c", "ml " + asmFilePath,
                "-c", fileNameWithoutExtension + ".exe"
            };
            
            //* Ejecutar los comandos
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            Process process = processBuilder.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            //* Esperar a que DOSBox termine de ejecutar
            process.waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }
}