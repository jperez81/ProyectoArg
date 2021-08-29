/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formulario;

import Metodos.ConexionBD;
import Metodos.MetodosSql;
import claseConectar.conectar;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import static jdk.nashorn.internal.runtime.Debug.id;
import java.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



/**
 *
 * @author José Pérez
 */

public class ConfigurarNegocio extends javax.swing.JInternalFrame {

    public ConfigurarNegocio() {

        initComponents();
        txtruta.setVisible(false);
        contar();
        txtid.setEnabled(false);
        txtid.setText("1");
        btnmodificar.setEnabled(false);

        byte[] encodedBytes = Base64.getEncoder().encode("Test".getBytes());
System.out.println("encodedBytes " + new String(encodedBytes));
byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
System.out.println("decodedBytes " + new String(decodedBytes));
    }
    int cont = 0;
    String id;
    String nom;
    String cuit;
    String dir;
    String tel;
    String correo;

    conectar cc = new conectar();
    Connection cn = cc.conexion();
    File fichero = null;

    public static String encodeToString(BufferedImage image) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageString;

    }

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public void blockear() {

        txtnom.setEnabled(false);
        txtcorreo.setEnabled(false);
        txtcuit.setEnabled(false);
        txtdir.setEnabled(false);
        txttel.setEnabled(false);
        btnguardar.setEnabled(false);

    }

    public void desbloquear() {
        txtnom.setEnabled(true);
        txtcorreo.setEnabled(true);

        txtdir.setEnabled(true);
        txttel.setEnabled(true);
        txtcuit.setEnabled(true);
        btnguardar.setEnabled(true);

    }

   

    void cargar(int limite) {
        BufferedImage img = null;
        String SQL = "SELECT * FROM empresa limit " + limite + ",1";
        String imagen_string = null;

        try {

            PreparedStatement pst = con.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery(SQL);
            while (rs.next()) {
                txtnom.setText(rs.getString("nombres"));
                txtcuit.setText(rs.getString("cuit"));
                txtdir.setText(rs.getString("direccion"));
                txttel.setText(rs.getString("telefono"));
                txtcorreo.setText(rs.getString("correo"));
                imagen_string = rs.getString("imagen");
                Lblfoto.setText(rs.getString("nombre"));
            }
            if (imagen_string == null) {
                cont = cont - 1;
                contar();
            } else {
                img = decodeToImage(imagen_string);
                ImageIcon icon = new ImageIcon(img);
                Icon icono = new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
                Lblfoto.setText(null);
                Lblfoto.setIcon(icono);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void contar() {
        String SQL = "select count(*) as cont from tienda.empresa";
        try {

            PreparedStatement pst = con.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery(SQL);

            int con = 0;
            while (rs.next()) {
                con = rs.getInt("cont");

            }
            if (con == 0) {
                Lblfoto.setText("Cargar logo de su negocio");

            } else {

                cont = con - 1;
                cargar(cont);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void mostar() {
        try {
            BufferedImage img = null;
            Connection cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "mora2017");
            PreparedStatement consulta = cnn.prepareStatement("SELECT * FROM tienda.empresa WHERE id= ?");
            String id = txtid.getText();
            String imagen_String = null;
            consulta.setString(1, id);

            ResultSet resultado = consulta.executeQuery();

            if (resultado.next()) {
                txtnom.setText(resultado.getString("nombres"));
                txtcuit.setText(resultado.getString("cuit"));
                txtdir.setText(resultado.getString("direccion"));
                txttel.setText(resultado.getString("telefono"));
                txtcorreo.setText(resultado.getString("correo"));
                imagen_String = resultado.getString("nombre");
                Lblfoto.setText(resultado.getString("imagen"));

            }
            img = decodeToImage(imagen_String);
            ImageIcon icon = new ImageIcon();
            Icon icono = new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
            Lblfoto.setText(null);
            Lblfoto.setIcon(icono);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos" + e);
        }

    }

    void limpiar() {
        txtid.setText("");
        txtnom.setText("");
        txtdir.setText("");
        txtcuit.setText("");
        txttel.setText("");
        txtcorreo.setText("");

    }

    MetodosSql Met = new MetodosSql();

    Connection con = ConexionBD.conectar();

    public void codigos() {
        // Metodo para generar codigos automanticamente desde la clase GenerarCodigos     
        int j;
        int cont = 1;
        String num = "";
//        String c="";
        String SQL = "select max(id) from tienda.empresa";
        // String SQL="select count(*) from factura";
        //String SQL="SELECT MAX(cod_emp) AS cod_emp FROM empleado";
        //String SQL="SELECT @@identity AS ID";
        try {
            PreparedStatement pst = con.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery(SQL);
            if (rs.next()) {
                num = rs.getString(1);
            }

            if (num == null) {
                txtid.setText("1");
            }

            j = Integer.parseInt(num);
            GenerarCodigos_E gen = new GenerarCodigos_E();
            gen.generar(j);
            txtid.setText(gen.serie());

        } catch (SQLException ex) {
            Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void guardar_imagen(String id, String nom, String cuit, String dir, String tel, String correo, String imagen, String nombre) {
        id = txtid.getText();
        nom = txtnom.getText();
        cuit = txtcuit.getText();
        dir = txtdir.getText();
        tel = txttel.getText();
        correo = txtcorreo.getText();
        String sql = "";

        sql = "INSERT INTO empresa (id,nombres,cuit,direccion,telefono,correo,imagen,nombre) VALUES (?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement pst = cn.prepareStatement(sql);

            pst.setString(1, id);
            pst.setString(2, nom);
            pst.setString(3, cuit);
            pst.setString(4, dir);
            pst.setString(5, tel);
            pst.setString(6, correo);
            pst.setString(7, imagen);
            pst.setString(8, nombre);

            int n = pst.executeUpdate();
            if (n > 0) {

                JOptionPane.showMessageDialog(null, "Registro Guardado con Exito");
                JOptionPane.showMessageDialog(null, "Se cerrara esta ventana para ejecutar los cambios");
                this.dispose();

            } else {
                JOptionPane.showMessageDialog(null, "Cargar todos los campoooos");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Favor de activar los campos y modificarlos");
        }
    }

  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtnom = new javax.swing.JTextField();
        Lblfoto = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtcuit = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtdir = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttel = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnactivar = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        txtcorreo = new javax.swing.JTextField();
        btneliminar = new javax.swing.JButton();
        txtruta = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();

        setClosable(true);
        setTitle("Configure su empresa");

        jPanel3.setBackground(new java.awt.Color(52, 73, 94));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setForeground(new java.awt.Color(0, 102, 204));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/img/new/config.png"))); // NOI18N

        txtid.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txtid.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Lucida Console", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Datos de la Empresa / Negocio :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Nombre :");

        txtnom.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        txtnom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnomActionPerformed(evt);
            }
        });

        Lblfoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lblfoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Lblfoto.setRequestFocusEnabled(false);

        jLabel4.setText("Logo :");

        jButton1.setText("Cargar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("CUIT :");

        txtcuit.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N

        jLabel6.setText("Direccion");

        txtdir.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N

        jLabel7.setText("Telefono :");

        txttel.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N

        btnguardar.setText("Guardar");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnactivar.setText("Activar");
        btnactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactivarActionPerformed(evt);
            }
        });

        btnmodificar.setText("Modificar");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        jLabel8.setText("E-mail :");

        txtcorreo.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N

        btneliminar.setText("Eliminar");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 158, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtruta, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel8)
                                .addComponent(txtcorreo)
                                .addComponent(txttel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                                .addComponent(txtdir)
                                .addComponent(txtnom)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtcuit)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(Lblfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78))))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txttel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(txtruta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtnom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtcuit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Lblfoto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)))))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnactivar)
                    .addComponent(btnmodificar)
                    .addComponent(btneliminar))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed

        if (!txtid.getText().equals("") && !txtnom.getText().equals("") && !txtcuit.getText().equals("") && !txtdir.getText().equals("") && !txtcorreo.getText().equals("") && !txttel.getText().equals("") && !txtruta.getText().equals("") ) {
            
            try {
                BufferedImage img = ImageIO.read(new File(fichero.toString()));
                String image_String = encodeToString(img);
                guardar_imagen(id, nom, cuit, dir, tel, correo, image_String, fichero.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Favor de rellenar todos los campos paara activar este modulo");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Favor de cargar todos los campos y su logo, gracias");

        }

//        String id, nom, cuit, dir, tel, correo;
//        String sql = "";
//        id = txtid.getText();
//        nom = txtnom.getText();
//        cuit = txtcuit.getText();
//        dir = txtdir.getText();
//        tel = txttel.getText();
//        correo = txtcorreo.getText();
//
//        sql = "INSERT INTO empresa (id,nombre,cuit,direccion,telefono,correo) VALUES (?,?,?,?,?,?)";
//
//
//        try {
//
//            PreparedStatement pst = cn.prepareStatement(sql);
//            pst.setString(1, id);
//            pst.setString(2, nom);
//            pst.setString(3, cuit);
//            pst.setString(4, dir);
//            pst.setString(5, tel);
//            pst.setString(6, correo);
//
//            int n = pst.executeUpdate();
//            if (n > 0) {
//                JOptionPane.showMessageDialog(null, "Registro Guardado con Exito");
//                JOptionPane.showMessageDialog(null, "Se cerrara esta ventana para ejecutar los cambios");
//                this.dispose();
//
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "Favor de activar los campos y modificarlos");
//        }
//        if (!txtnom.getText().equals("") && !txtcuit.getText().equals("") || !txtdir.getText().equals("")) {
//
////           if (!txtruta.getText().equals("")) {
//            try {
//
//                BufferedImage img = ImageIO.read(new File(fichero.toString()));
//                String image_String = encodeToString(img);
//                id = txtid.getText();
//                nom = txtnom.getText();
//                cuit = txtcuit.getText();
//                dir = txtdir.getText();
//                tel = txttel.getText();
//                correo = txtcorreo.getText();
//                guardar_imagen(id, nom, cuit, dir, tel, correo, image_String, fichero.getName());
//
//
//
//            } catch (IOException ex) {
//                Logger.getLogger(ConfigurarNegocio.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, "llenar");
//            }

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactivarActionPerformed
        desbloquear();
        btnmodificar.setEnabled(true);
        btnguardar.setEnabled(false);
        btnactivar.setEnabled(false);

    }//GEN-LAST:event_btnactivarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE tienda.empresa SET nombres='"
                    + txtnom.getText() + "',cuit='"
                    + txtcuit.getText() + "',direccion='"
                    + txtdir.getText() + "',telefono='"
                    + txttel.getText() + "',correo='"
                    + txtcorreo.getText() + "' WHERE id='" + txtid.getText() + "'");
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Datos modificados con exito");
            JOptionPane.showMessageDialog(null, "Se cerrara esta ventana para ejecutar los cambios");
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al intentar modificar los datos " + e);
        }
        btnguardar.setEnabled(true);
        btnactivar.setEnabled(true);
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed

        if (txtid.getText().equals("1")) {
            String opcion[] = {"Eliminar", "Cancelar"};

            int eleccion = JOptionPane.showOptionDialog(this, "Esta seguro de eliminar el registro para siempre?. ", "ATENCION!!!", 0, 0, null, opcion, evt);
            if (eleccion == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement pst = cn.prepareStatement("DELETE FROM tienda.empresa WHERE id='" + 1 + "'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Registro eliminado con exito.");
                    JOptionPane.showMessageDialog(null, "Se cerrara esta ventana para ejecutar los cambios");
                    this.dispose();
                } catch (SQLException ex) {
                    Logger.getLogger(IngresoCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vuelva a intentarlo.");
            }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos soportados ", "*.jpg", "jpg", "*.png", "png", "*.ico", "ico");
        file.setFileFilter(filtro);

        int seleccion = file.showDialog(jPanel1, "Seleccione su logo");
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            fichero = file.getSelectedFile();
            txtruta.setText(fichero.getAbsolutePath());
            ImageIcon icon = new ImageIcon(fichero.toString());
            System.out.println(fichero.getName());
            Icon icono = new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
            Lblfoto.setText(null);
            Lblfoto.setIcon(icono);

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtnomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnomActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel Lblfoto;
    private javax.swing.JButton btnactivar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator2;
    public static javax.swing.JTextField txtcorreo;
    public static javax.swing.JTextField txtcuit;
    public static javax.swing.JTextField txtdir;
    public static javax.swing.JTextField txtid;
    public static javax.swing.JTextField txtnom;
    public static javax.swing.JTextField txtruta;
    public static javax.swing.JTextField txttel;
    // End of variables declaration//GEN-END:variables

    private void guardar_imagen(String id, String nom, String cuit, String dir, String tel, String correo, String correo0, String nom0, String image_String, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
