
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Automata;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.awt.Image;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Componentes.TextLineNumber;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.border.Border;
/**
 *
 * @author Umizooft
 */
public class InterfazCompilador extends javax.swing.JFrame{

    /**
     * Creates new form InterfazCompilador
     */
    public static String ruta;
    
    public InterfazCompilador(){
        initComponents();
        TextLineNumber tln = new TextLineNumber(txtEntrada);
        jScrollPane1.setRowHeaderView( tln );
    }
    public Image getIconImage() { 
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("IMG/Umizooft.jpg"));
        return retValue;
    }
    
    
    class RoundedBorder implements Border{
        private int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        public Insets getBorderInsets(Component c){
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }
        public boolean isBorderOpaque(){
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    //?@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCompilar = new javax.swing.JButton();
        btnEjecutar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEntrada = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSalida = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxSem = new javax.swing.JCheckBox();
        jCheckBoxSin = new javax.swing.JCheckBox();
        jCheckBoxLex = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador Umizooft");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(getIconImage());
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(222, 222, 252));

        btnNuevo.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(89, 116, 255));
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnBuscar.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(89, 116, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(89, 116, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCompilar.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        btnCompilar.setForeground(new java.awt.Color(89, 116, 255));
        btnCompilar.setText("Compilar");
        btnCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompilarActionPerformed(evt);
            }
        });

        btnEjecutar.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        btnEjecutar.setForeground(new java.awt.Color(89, 116, 255));
        btnEjecutar.setText("Ejecutar");
        btnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarActionPerformed(evt);
            }
        });

        txtEntrada.setColumns(20);
        txtEntrada.setRows(5);
        jScrollPane1.setViewportView(txtEntrada);

        txtSalida.setEditable(false);
        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        txtSalida.setToolTipText("");
        txtSalida.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtSalida.setFocusable(false);
        jScrollPane2.setViewportView(txtSalida);

        jLabel1.setFont(new java.awt.Font("Lexend", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lexico");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Lexend", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sintactico");

        jLabel3.setFont(new java.awt.Font("Lexend", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Semantico");

        jCheckBoxSem.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        jCheckBoxSem.setText("Errores");
        jCheckBoxSem.setEnabled(false);

        jCheckBoxSin.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        jCheckBoxSin.setText("Errores");
        jCheckBoxSin.setEnabled(false);

        jCheckBoxLex.setFont(new java.awt.Font("Lexend", 0, 14)); // NOI18N
        jCheckBoxLex.setText("Errores");
        jCheckBoxLex.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(btnEjecutar, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(btnCompilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jCheckBoxLex, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jCheckBoxSin, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(250, 250, 250)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxSem, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCompilar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEjecutar)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxSem)
                    .addComponent(jCheckBoxSin)
                    .addComponent(jCheckBoxLex, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Compilador PAWN");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //! Boton de Nuevo
    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showSaveDialog(null);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            try {
                FileWriter escritor = new FileWriter(archivo);
                String contenido = "//Este es el contenido del archivo.//";
                escritor.write(contenido);
                ruta = archivo.getAbsolutePath(); // Obtenemos la ruta del archivo
                JOptionPane.showMessageDialog(null, "Archivo creado en: " + ruta);
                escritor.close();
                JOptionPane.showMessageDialog(null, "Archivo creado con exito");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al crear el archivo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Operación Cancelada");
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    //! Boton de Busqueda
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        FileDialog AbrirArchivo = new FileDialog(new Frame(),"Abrir Archivo", FileDialog.LOAD);
        AbrirArchivo.setFile("*.txt");
        AbrirArchivo.setModal(true);
        AbrirArchivo.setVisible(true);
        AbrirArchivo.getFile();
        ruta = AbrirArchivo.getDirectory() + AbrirArchivo.getFile();
        File Archivo = new File(ruta);
        try {
            txtEntrada.setText("");
            BufferedReader leer = new BufferedReader(new FileReader(Archivo));
            String linea = leer.readLine();
            while(linea != null)
            {
                txtEntrada.append(linea + "\n");
                linea = leer.readLine();
            }

            //*Agregue esto por el warning */
            leer.close();
        }
        catch (IOException ex) {
            Logger.getLogger(InterfazCompilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    //! Boton para el Guardado
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try{
            FileWriter SourceCode = new FileWriter(ruta);
            String code = txtEntrada.getText().replace("\n", "\r\n");
            PrintWriter compi = new PrintWriter(SourceCode);
            compi.print(code);
            SourceCode.close();
            JOptionPane.showMessageDialog(null, "Los datos se han guardado con exito");
        }catch(IOException ex){
            Logger.getLogger(InterfazCompilador.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Los datos no se pudieron guardar");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    //! Boton para la Compilacion
    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompilarActionPerformed
        try {
            txtSalida.setText("");
            FileWriter SourceCode = new FileWriter(ruta);
            String code = txtEntrada.getText().replace("\n", "\r\n");
            PrintWriter compi = new PrintWriter(SourceCode);
            compi.print(code);
            SourceCode.close();

            Compilador automata = new Compilador();

            if (!automata.errorFound) {
                for (int i = 0; i < automata.listaDeErrores.size(); i++) {
                    String Error = automata.listaDeErrores.get(i);
                    txtSalida.append(Error + "\n");
                }

                for (int i = 0; i < ProcOperaciones.listaErrores2.size(); i++) {
                    String Error = ProcOperaciones.listaErrores2.get(i);
                    txtSalida.append(Error + "\n");
                }

                if (!automata.errorFound) {
                    txtSalida.append("Análisis Léxico Completado Exitosamente \n");
                    jCheckBoxLex.setEnabled(false);
                    jCheckBoxLex.setSelected(false);
                    btnEjecutar.setEnabled(true);
                }

                if(!ProcOperaciones.errorSinFound2 && !automata.errorSinFound){
                    txtSalida.append("Análisis Sintáctico Completado Exitosamente \n");
                    jCheckBoxSin.setEnabled(false);
                    jCheckBoxSin.setSelected(false);
                    btnEjecutar.setEnabled(true);
                }

                if (!automata.errorSemFound) {
                    txtSalida.append("Análisis Semántico Completado Exitosamente \n");
                    jCheckBoxSem.setEnabled(false);
                    jCheckBoxSem.setSelected(false);
                    btnEjecutar.setEnabled(true);
                }
            } else {
                for (int i = 0; i < automata.listaDeErrores.size(); i++) {
                    String Error = automata.listaDeErrores.get(i);
                    txtSalida.append(Error + "\n");
                }
                txtSalida.append("Análisis Léxico Completado con errores");
            }

            // Actualiza los checkboxes según los errores encontrados
            if (automata.errorFound) {
                jCheckBoxLex.setEnabled(true);
                jCheckBoxLex.setSelected(true);
                btnEjecutar.setEnabled(false);
            }

            if (automata.errorSinFound || ProcOperaciones.errorSinFound2) {
                jCheckBoxSin.setEnabled(true);
                jCheckBoxSin.setSelected(true);
                btnEjecutar.setEnabled(false);
            }

            if (automata.errorSemFound) {
                jCheckBoxSem.setEnabled(true);
                jCheckBoxSem.setSelected(true);
                btnEjecutar.setEnabled(false);
            }

            JOptionPane.showMessageDialog(null, "Código compilado");
        } catch (Exception ex) {
            Logger.getLogger(InterfazCompilador.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Hubo un Error al compilar");
        }
    }//GEN-LAST:event_btnCompilarActionPerformed

    //! Boton de Ejecutar
    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarActionPerformed   
        try {
            Assembly.Traducir();
            Ejecutar.Asm();
        } catch (IOException ex) {
            Logger.getLogger(InterfazCompilador.class.getName()).log(Level.SEVERE, "Error de IO", ex);
            JOptionPane.showMessageDialog(null, "No se puede ejecutar: Error de IO");
        }
    }//GEN-LAST:event_btnEjecutarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazCompilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazCompilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCompilar;
    private javax.swing.JButton btnEjecutar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox jCheckBoxLex;
    private javax.swing.JCheckBox jCheckBoxSem;
    private javax.swing.JCheckBox jCheckBoxSin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtEntrada;
    private javax.swing.JTextArea txtSalida;
    // End of variables declaration//GEN-END:variables
}