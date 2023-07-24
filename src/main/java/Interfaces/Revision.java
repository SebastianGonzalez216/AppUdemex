package Interfaces;

//importa el paquete y libreria para la conexon con la BD
import ConexionSQL.ConexionSQL;
import Render.WebPanel;
import Render.Render;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import java.awt.Color;
import java.sql.Statement;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
//--------------------------------------
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;
import tomas.garza.SubrayadorTexto;
import org.languagetool.tools.ContextTools;
//--------------------------------------------


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */







/**
 *
 * @author Ragnar
 */
public class Revision extends javax.swing.JFrame {

    /**
     * Creates new form PROMEDIOS
     */
    WebPanel browser = new WebPanel();
 
    
    ConexionSQL cc = new ConexionSQL();  
    Connection con = cc.conexion();
    
    FileInputStream gflujo;
    long glongitud;    
    byte[] documento = null;
    

        
    public Revision()
    {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/ICONO.png")).getImage());

        mostrarDatos();
        this.setLocationRelativeTo(null); 

        ((JTextField) this.txtFecha.getDateEditor()).setEditable(false); 
        TablaActividades.getTableHeader().setResizingAllowed(false);
        
        txtTexto.setLineWrap(true);
        txtTexto.setWrapStyleWord(true);
        txtErrores.setLineWrap(true);
        txtErrores.setWrapStyleWord(true);
        
        ConsultarNoactividad(comboNoactividad);
        ConsultarNombreactividad(txtNombreactividad);
        ConsultarMatricula(comboMatricula);
        ConsultarNombrealumno(txtNombreAlumno);
        ConsultarGrupo(txtGrupo);
        
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.yesButtonText", "Si");
        
        


    }


    @SuppressWarnings("empty-statement")  
    
    public void mostrarDatos()
    {

        TablaActividades.setDefaultRenderer(Object.class, new Render());
        JButton btnPDF = new JButton("Ver archivo...");
        btnPDF.setName("botonPDF");
        TablaActividades.setRowHeight(25);
        
        
        String[] titulos={"ID Carga","No.Actividad","Nombre de la actividad","Matrícula","Nombre del alumno","Grupo","Fecha de entrega","Ortografía","Originalidad","Archivo"};
        Object registros[]=new Object[10];

    
        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        for (int c = 0; c < TablaActividades.getColumnCount(); c++)
        {
        Class<?> col_class = TablaActividades.getColumnClass(c);
        TablaActividades.setDefaultEditor(col_class, null); 
        }
    
        String SQL= "select * from cargas";

        try
        {
        Statement st=(Statement) con.createStatement();
        ResultSetImpl rs=(ResultSetImpl) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Id_carga");
        registros[1]=rs.getString("No_actividad");
        registros[2]=rs.getString("Nombre_actividad");
        registros[3]=rs.getString("Matricula");
        registros[4]=rs.getString("Nombre_completo");
        registros[5]=rs.getString("Id_grupo");
        registros[6]=rs.getString("Fecha_entrega");
        registros[7]=rs.getString("Ortografia");
        registros[8]=rs.getString("Originalidad");
        
        registros[9]=btnPDF;
        
        documento=rs.getBytes("Archivo_actividad");;
        
        modelo.addRow(registros);

        }
        TablaActividades.setModel(modelo);
        } 
        catch(Exception e)
        {
        }
    } 

    
        public void ConsultarNoactividad(JComboBox Noactividades)
        {
             
        String SQL = "SELECT No_actividad from actividades";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        Noactividades.addItem(rs.getString("No_actividad"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }
    
        public void ConsultarNombreactividad(JTextField Nombreactividades)
        {
        String n = comboNoactividad.getItemAt(comboNoactividad.getSelectedIndex());
        String SQL = "SELECT Nombre_actividad from actividades WHERE No_actividad ='"+n+"'";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        Nombreactividades.setText(rs.getString("Nombre_actividad"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }

        
        
        public void ConsultarMatricula(JComboBox Matricula)
        {
             
        String SQL = "SELECT Matricula from alumnos";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        Matricula.addItem(rs.getString("Matricula"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }
        
        
        public void ConsultarNombrealumno(JTextField Nombrealumno)
        {
        String n = comboMatricula.getItemAt(comboMatricula.getSelectedIndex());
        String SQL = "SELECT Nombre_completo from alumnos WHERE Matricula ='"+n+"'";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        Nombrealumno.setText(rs.getString("Nombre_completo"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }
    
        
        public void ConsultarGrupo(JTextField Grupo)
        {
        String n = comboMatricula.getItemAt(comboMatricula.getSelectedIndex());
        String SQL = "SELECT Id_grupo from alumnos WHERE Matricula ='"+n+"'";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        Grupo.setText(rs.getString("Id_grupo"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }
    public void VerActividad()
    {

        int filSeleccionada=TablaActividades.getSelectedRow();

        byte[] lectura = null;

        try 
        {
                
        String SQL = ("SELECT Archivo_actividad FROM cargas WHERE Id_carga ='"+TablaActividades.getValueAt(filSeleccionada,0)+"'");
            
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
                                      
            
        while (rs.next()) 
        {
        lectura=rs.getBytes("Archivo_actividad");;
        }
            
        InputStream bos = new ByteArrayInputStream(lectura);

        int tamanoInput = bos.available();
        byte[] datosPDF = new byte[tamanoInput];
        bos.read(datosPDF, 0, tamanoInput);

        String ruta = System.getProperty("user.home");

        
        OutputStream out = new FileOutputStream(ruta + "\\Documents\\previsualizacion.pdf");
        out.write(datosPDF);
           
        Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + ruta + "\\Documents\\previsualizacion.pdf");

        out.close();
        bos.close();

        } 
        catch (IOException | NumberFormatException | SQLException ex) {
        System.out.println("Error al abrir archivo PDF " + ex.getMessage());
        }
    }

    public void BuscarDatos()
    {
        
        TablaActividades.setDefaultRenderer(Object.class, new Render());
        JButton btnPDF = new JButton("Ver archivo...");
        btnPDF.setName("botonPDF");
        TablaActividades.setRowHeight(25);
        
        String[] titulos={"ID Carga","No.Actividad","Nombre de la actividad","Matrícula","Nombre del alumno","Grupo","Fecha de entrega","Ortografía","Originalidad","Archivo"};
        Object registros[]=new Object[10];


        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        String id = txtBuscar.getText();
     
        String SQL="SELECT Id_carga, No_actividad, Nombre_actividad, Matricula, Nombre_completo, Id_grupo, Fecha_entrega,Ortografia, Originalidad, Archivo_actividad "
        + "FROM cargas WHERE Matricula LIKE '%"+id+"%'";

        try
        {

        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Id_carga");
        registros[1]=rs.getString("No_actividad");
        registros[2]=rs.getString("Nombre_actividad");
        registros[3]=rs.getString("Matricula");
        registros[4]=rs.getString("Nombre_completo");
        registros[5]=rs.getString("Id_grupo");
        registros[6]=rs.getString("Fecha_entrega");
        registros[7]=rs.getString("Ortografia");
        registros[8]=rs.getString("Originalidad");
        
        registros[9]=btnPDF;
        
        documento=rs.getBytes("Archivo_actividad");;

        modelo.addRow(registros);
        }

        TablaActividades.setModel(modelo);
        } 
        catch(Exception e)
        {
        }  
      
    }
    
    public void insertarDatos()
    {
        
        try
        { 
        String SQL="insert into cargas (Id_carga,No_actividad,Nombre_actividad,Matricula,Nombre_completo,Id_grupo,Fecha_entrega,Archivo_actividad) values (?,?,?,?,?,?,?,?)";
        
        PreparedStatement pat=(PreparedStatement) con.prepareStatement (SQL);
        
        pat.setString(1, txtIDCarga.getText());
        
        pat.setString(2, comboNoactividad.getItemAt(comboNoactividad.getSelectedIndex()));
        
        pat.setString(3, txtNombreactividad.getText());
        
        pat.setString(4, comboMatricula.getItemAt(comboMatricula.getSelectedIndex()));
        
        pat.setString(5, txtNombreAlumno.getText());
        
        pat.setString(6, txtGrupo.getText());

        pat.setString(7, ((JTextField)txtFecha.getDateEditor().getUiComponent()).getText());
    
        pat.setBinaryStream(8, gflujo);

        pat.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Actividad añadida con éxito","Actividad cargada",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, llene todos los campos, carge la actividad correspondiente y verifique la integridad de los datos","No se pudo insertar el registro",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void actualizarDatos()
    {

        try
        { 
        String SQL="update cargas set Id_carga=?,No_actividad=?,Nombre_actividad=? ,Matricula=?,Nombre_completo=?,Id_grupo=?,Fecha_entrega=?,Archivo_actividad=? where Id_carga=?";

        int filaseleccionado=TablaActividades.getSelectedRow();
        String saoko=(String)TablaActividades.getValueAt(filaseleccionado, 0);
    
        PreparedStatement patr=(PreparedStatement) con.prepareStatement (SQL);

        patr.setString(1, txtIDCarga.getText());
        
        patr.setString(2, comboNoactividad.getItemAt(comboNoactividad.getSelectedIndex()));
        
        patr.setString(3, txtNombreactividad.getText());
        
        patr.setString(4, comboMatricula.getItemAt(comboMatricula.getSelectedIndex()));
        
        patr.setString(5, txtNombreAlumno.getText());
        
        patr.setString(6, txtGrupo.getText());

        patr.setString(7, ((JTextField)txtFecha.getDateEditor().getUiComponent()).getText());
    
        patr.setBinaryStream(8, gflujo);
        
        patr.setString(9,saoko);
    
        patr.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro actualizado correctamente","Datos actualizados",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione el registro a actualizar, llene todos los campos, carge la actividad nueva y verifique la integridad de los datos","No se pudo actualizar el registro",JOptionPane.ERROR_MESSAGE);        }
    }
        
    
    public void eliminarRegistros()
    {
        
        int filSeleccionada=TablaActividades.getSelectedRow();
        
        try 
        {
        String SQL="delete from cargas where Id_carga='"+TablaActividades.getValueAt(filSeleccionada,0)+"'";
        Statement st= (Statement) con.createStatement();
            
        int n= st.executeUpdate (SQL);
            
        if(n>=0)
        {
        JOptionPane.showMessageDialog(null,"Registro eliminado correctamente","Datos eliminados",JOptionPane.INFORMATION_MESSAGE);
        }
        }
        
        catch (Exception e)
        {    
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione el registro a eliminar ","No se pudo eliminar el registro",JOptionPane.ERROR_MESSAGE); 
        }
    }
        
    
    public void eliminarTodosRegistros()
    {
 
        try 
        {
        String SQL="delete from cargas";
        Statement st= (Statement) con.createStatement();
            
        int n= st.executeUpdate (SQL);
            
        if(n>=0)
        {
        JOptionPane.showMessageDialog(null,"Registros eliminados correctamente","Datos eliminados",JOptionPane.INFORMATION_MESSAGE);
        }
        }
        catch (Exception e)
        {     
        JOptionPane.showMessageDialog(null,"No hay registros a eliminar en la tabla","No se pudieron eliminar los registros",JOptionPane.ERROR_MESSAGE); 
        }
    }
      
    
    public void limpiarCajas()
    {
        txtIDCarga.setText("");
        txtFecha.setCalendar(null);
        gflujo = null;
        glongitud = 0;
    }
    
    public void habilitarBtonesyCajas()
    {
    txtIDCarga.setEnabled(true);
    txtFecha.setEnabled(true);
    comboMatricula.setEnabled(true);
    comboNoactividad.setEnabled(true);
    btnCancelar.setEnabled(true);
    btnGuardar.setEnabled(true);
    btnActualizar.setEnabled(true);
    btnCargar.setEnabled(true);
    btnNuevo.setEnabled(false);
    }
    
    public void deshabilitarBtonesyCajas()
    {
    txtIDCarga.setEnabled(false);
    txtFecha.setEnabled(false);
    comboMatricula.setEnabled(false);
    comboNoactividad.setEnabled(false);
    btnCancelar.setEnabled(false);
    btnGuardar.setEnabled(false);
    btnActualizar.setEnabled(false);
    btnCargar.setEnabled(false);
    btnNuevo.setEnabled(true);

    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        Pestañas = new javax.swing.JTabbedPane();
        Panel1 = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtIDCarga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        comboNoactividad = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtNombreactividad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        comboMatricula = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtNombreAlumno = new javax.swing.JTextField();
        txtGrupo = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnEliminarTodo = new javax.swing.JButton();
        Check = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        Panel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTexto = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtErrores = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        btnRevisarOrt = new javax.swing.JButton();
        btnLimpiarTexto = new javax.swing.JButton();
        btnCopiarTexto = new javax.swing.JButton();
        btnPegarTexto = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblCantidadPalabras = new javax.swing.JLabel();
        lblPorcentajeErrores = new javax.swing.JLabel();
        lblErroresEncontrados = new javax.swing.JLabel();
        Panel3 = new javax.swing.JPanel();
        btnReloadWeb = new javax.swing.JButton();
        PanelNavegador = new javax.swing.JPanel();
        Panel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        comboOriginalidad = new javax.swing.JComboBox<>();
        comboOrtografia = new javax.swing.JComboBox<>();
        btnAsignarCalificacion = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaActividades = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Carga y revisión de actividades");
        setResizable(false);

        jLabel1.setBackground(new java.awt.Color(51, 204, 0));
        jLabel1.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        jLabel1.setText("CARGA Y REVISIÓN DE ACTIVIDADES");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        Pestañas.setBorder(new javax.swing.border.MatteBorder(null));

        Panel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Panel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                Panel1MouseMoved(evt);
            }
        });

        btnCargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnPDFCarga.png"))); // NOI18N
        btnCargar.setText("Seleccionar archivo...");
        btnCargar.setToolTipText("Presione aquí para cargar la actividad en formato PDF");
        btnCargar.setEnabled(false);
        btnCargar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCargar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setText("ID Carga:");

        txtIDCarga.setEnabled(false);
        txtIDCarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDCargaKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel5.setText("Actividad:");

        txtFecha.setDateFormatString("yyyy-MM-dd");
        txtFecha.setEnabled(false);

        comboNoactividad.setBackground(new java.awt.Color(255, 255, 204));
        comboNoactividad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        comboNoactividad.setEnabled(false);
        comboNoactividad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNoactividadActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setText("No. Actividad:");

        txtNombreactividad.setBackground(new java.awt.Color(255, 255, 204));
        txtNombreactividad.setBorder(null);
        txtNombreactividad.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setText("Nombre de la actividad:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel8.setText("Grupo:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel9.setText("Matrícula:");

        comboMatricula.setBackground(new java.awt.Color(204, 255, 204));
        comboMatricula.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        comboMatricula.setEnabled(false);
        comboMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMatriculaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel10.setText("Nombre del alumno:");

        txtNombreAlumno.setEditable(false);
        txtNombreAlumno.setBackground(new java.awt.Color(204, 255, 204));
        txtNombreAlumno.setBorder(null);
        txtNombreAlumno.setFocusable(false);

        txtGrupo.setEditable(false);
        txtGrupo.setBackground(new java.awt.Color(204, 255, 204));
        txtGrupo.setBorder(null);
        txtGrupo.setFocusable(false);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn1 .png"))); // NOI18N
        btnNuevo.setToolTipText("Registrar o actualizar datos de actividad");
        btnNuevo.setBorder(null);
        btnNuevo.setContentAreaFilled(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn3 .png"))); // NOI18N
        btnNuevo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn2 .png"))); // NOI18N
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc1 .png"))); // NOI18N
        btnCancelar.setToolTipText("Cancelar operación");
        btnCancelar.setBorder(null);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setEnabled(false);
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc3 .png"))); // NOI18N
        btnCancelar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc2 .png"))); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng1.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar nueva actividad");
        btnGuardar.setBorder(null);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setEnabled(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng3 .png"))); // NOI18N
        btnGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng2.png"))); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna1.png"))); // NOI18N
        btnActualizar.setToolTipText("Actualizar actividad seleccionada");
        btnActualizar.setBorder(null);
        btnActualizar.setContentAreaFilled(false);
        btnActualizar.setEnabled(false);
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna3 .png"))); // NOI18N
        btnActualizar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna2.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne1 .png"))); // NOI18N
        btnEliminar.setToolTipText("Eliminar actividad seleccionada");
        btnEliminar.setBorder(null);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne3 .png"))); // NOI18N
        btnEliminar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne2 .png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnEliminarTodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet1 .png"))); // NOI18N
        btnEliminarTodo.setToolTipText("Eliminar todas las actividades registradas");
        btnEliminarTodo.setBorder(null);
        btnEliminarTodo.setContentAreaFilled(false);
        btnEliminarTodo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarTodo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet3.png"))); // NOI18N
        btnEliminarTodo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet2 .png"))); // NOI18N
        btnEliminarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodoActionPerformed(evt);
            }
        });

        Check.setBorder(null);
        Check.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/CheckIncorrect.png"))); // NOI18N
        Check.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/CheckCorrect.png"))); // NOI18N
        Check.setEnabled(false);
        Check.setFocusable(false);
        Check.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/CheckIncorrect.png"))); // NOI18N
        Check.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/CheckCorrect.png"))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel19.setText("Fecha:");

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(231, 231, 231)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtNombreAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel1Layout.createSequentialGroup()
                                    .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)
                                        .addComponent(txtIDCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(39, 39, 39)
                                    .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(comboMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7)
                                    .addComponent(txtNombreactividad, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                    .addComponent(comboNoactividad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(39, 39, 39)
                                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))))
                        .addGap(53, 53, 53)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel5))
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addComponent(btnCargar)
                                .addGap(18, 18, 18)
                                .addComponent(Check)))
                        .addGap(0, 187, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIDCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addComponent(comboNoactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNombreactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel5)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(Check, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        Pestañas.addTab("Carga de actividades", Panel1);

        Panel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel6.setText("RESULTADOS:");

        txtTexto.setColumns(20);
        txtTexto.setRows(5);
        txtTexto.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        txtTexto.setSelectionColor(new java.awt.Color(51, 204, 255));
        txtTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTextoKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtTexto);

        txtErrores.setEditable(false);
        txtErrores.setBackground(new java.awt.Color(0, 0, 0));
        txtErrores.setColumns(20);
        txtErrores.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        txtErrores.setForeground(new java.awt.Color(204, 204, 204));
        txtErrores.setRows(5);
        txtErrores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtErrores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtErroresKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(txtErrores);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel12.setText("INGRESE EL TEXTO A REVISAR AQUÍ:");

        btnRevisarOrt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnRevisarTexto.png"))); // NOI18N
        btnRevisarOrt.setToolTipText("Revisar texto");
        btnRevisarOrt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevisarOrtActionPerformed(evt);
            }
        });

        btnLimpiarTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnLimpiarTexto.png"))); // NOI18N
        btnLimpiarTexto.setToolTipText("Nuevo/Limpiar");
        btnLimpiarTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTextoActionPerformed(evt);
            }
        });

        btnCopiarTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnCopiar.png"))); // NOI18N
        btnCopiarTexto.setToolTipText("Copiar texto seleccionado");
        btnCopiarTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarTextoActionPerformed(evt);
            }
        });

        btnPegarTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnPegar.png"))); // NOI18N
        btnPegarTexto.setToolTipText("Pegar texto");
        btnPegarTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPegarTextoActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("No. De errores:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Porcentaje de errores:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Cantidad de palabras en el texto:");

        lblCantidadPalabras.setText("-");

        lblPorcentajeErrores.setText("-");

        lblErroresEncontrados.setText("-");

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCantidadPalabras, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12)
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRevisarOrt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiarTexto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCopiarTexto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPegarTexto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErroresEncontrados, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPorcentajeErrores, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2Layout.createSequentialGroup()
                        .addComponent(btnLimpiarTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnCopiarTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPegarTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(btnRevisarOrt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lblCantidadPalabras)
                    .addComponent(jLabel13)
                    .addComponent(lblErroresEncontrados)
                    .addComponent(jLabel15)
                    .addComponent(lblPorcentajeErrores))
                .addGap(19, 19, 19))
        );

        Pestañas.addTab("Revisión de ortografía", Panel2);

        btnReloadWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnReload.png"))); // NOI18N
        btnReloadWeb.setToolTipText("Nuevo/Recargar ");
        btnReloadWeb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadWebActionPerformed(evt);
            }
        });

        PanelNavegador.setBackground(new java.awt.Color(255, 255, 255));
        PanelNavegador.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelNavegador.setLayout(new javax.swing.BoxLayout(PanelNavegador, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout Panel3Layout = new javax.swing.GroupLayout(Panel3);
        Panel3.setLayout(Panel3Layout);
        Panel3Layout.setHorizontalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(PanelNavegador, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReloadWeb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        Panel3Layout.setVerticalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnReloadWeb, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(PanelNavegador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Pestañas.addTab("Detección de plagio", Panel3);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel17.setText("CALIFICACIÓN DE ORIGINALIDAD DEL TRABAJO:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel18.setText("CALIFICACIÓN DE ORTOGRAFÍA:");

        comboOriginalidad.setBackground(new java.awt.Color(204, 255, 255));
        comboOriginalidad.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        comboOriginalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100" }));
        comboOriginalidad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        comboOrtografia.setBackground(new java.awt.Color(204, 255, 255));
        comboOrtografia.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        comboOrtografia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100" }));
        comboOrtografia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAsignarCalificacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnRevisarTexto.png"))); // NOI18N
        btnAsignarCalificacion.setText("   Asignar calificaciones");
        btnAsignarCalificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarCalificacionActionPerformed(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Notes.png"))); // NOI18N

        javax.swing.GroupLayout Panel4Layout = new javax.swing.GroupLayout(Panel4);
        Panel4.setLayout(Panel4Layout);
        Panel4Layout.setHorizontalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboOrtografia, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnAsignarCalificacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboOriginalidad, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(52, 52, 52))
            .addGroup(Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel4Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(643, Short.MAX_VALUE)))
        );
        Panel4Layout.setVerticalGroup(
            Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboOrtografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboOriginalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(btnAsignarCalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(Panel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel20)
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(Panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel4Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(284, Short.MAX_VALUE)))
        );

        Pestañas.addTab("Asignación de calificaciones", Panel4);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/UDEMEX.png"))); // NOI18N

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        TablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nombre completo", "Fecha de entrega", "Actividad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaActividades.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TablaActividades.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TablaActividades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaActividadesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaActividades);
        if (TablaActividades.getColumnModel().getColumnCount() > 0) {
            TablaActividades.getColumnModel().getColumn(0).setResizable(false);
            TablaActividades.getColumnModel().getColumn(1).setResizable(false);
            TablaActividades.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Search.png"))); // NOI18N
        jLabel14.setText("  Buscar por matrícula:");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(50, 50, 50))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 865, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addComponent(jLabel11))
                .addComponent(Pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    
    System.exit(0);
    
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
    dispose();
        
    Menu verformulario=new Menu(); 
    verformulario.setVisible(true);

    }//GEN-LAST:event_btnVolverActionPerformed

    private void Panel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel1MouseMoved
        // TODO add your handling code here:

        if (gflujo != null)
        {
        Check.setSelected(true);
        }
        else
        {
        Check.setSelected(false);
        }
    }//GEN-LAST:event_Panel1MouseMoved

    private void btnEliminarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodoActionPerformed

        if(JOptionPane.showConfirmDialog(null,"Se eliminarán todos los registros de la tabla, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarTodosRegistros();
            limpiarCajas();
            mostrarDatos();
        }
    }//GEN-LAST:event_btnEliminarTodoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed

        if(JOptionPane.showConfirmDialog(null,"Se eliminará el registro seleccionado, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarRegistros();
            limpiarCajas();
            mostrarDatos();
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        actualizarDatos();
        limpiarCajas();
        mostrarDatos();
        deshabilitarBtonesyCajas();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        insertarDatos();
        limpiarCajas();
        mostrarDatos();
        deshabilitarBtonesyCajas();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
       deshabilitarBtonesyCajas();
       limpiarCajas();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       habilitarBtonesyCajas();
       limpiarCajas();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void comboMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMatriculaActionPerformed

        ConsultarNombrealumno(txtNombreAlumno);
        ConsultarGrupo(txtGrupo);

    }//GEN-LAST:event_comboMatriculaActionPerformed

    private void comboNoactividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNoactividadActionPerformed
        // TODO add your handling code here:
        ConsultarNombreactividad(txtNombreactividad);
    }//GEN-LAST:event_comboNoactividadActionPerformed

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped

        if(txtBuscar.getText().length() >= 10)
        {
            evt.consume();
        }

        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void txtIDCargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDCargaKeyTyped

        if(txtIDCarga.getText().length() >= 10)
        {
            evt.consume();
        }

        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);

    }//GEN-LAST:event_txtIDCargaKeyTyped

    private void TablaActividadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaActividadesMouseClicked
        // TODO add your handling code here:

        int filaSeleccionada=TablaActividades.rowAtPoint(evt.getPoint());

        txtIDCarga.setText(TablaActividades.getValueAt(filaSeleccionada, 0).toString());
        comboNoactividad.setSelectedItem(TablaActividades.getValueAt(filaSeleccionada, 1).toString());
        txtNombreactividad.setText(TablaActividades.getValueAt(filaSeleccionada, 2).toString());
        comboMatricula.setSelectedItem(TablaActividades.getValueAt(filaSeleccionada, 3).toString());
        txtNombreAlumno.setText(TablaActividades.getValueAt(filaSeleccionada, 4).toString());
        txtGrupo.setText(TablaActividades.getValueAt(filaSeleccionada, 5).toString());


        int fila = TablaActividades.getSelectedRow();
        //obtenemos la fecha de dicha fila
        String fecha = TablaActividades.getValueAt(fila, 6).toString();
        //creamos el formato en el que deseamos mostrar la fecha
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        //creamos una variable tipo Date y la ponemos NULL
        Date fechaN = null;

        try
        {
            //parseamos de String a Date usando el formato
            fechaN = formatoDelTexto.parse(fecha);
            //seteamos o mostramos la fecha en el JDateChooser
            txtFecha.setDate(fechaN);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        //--------------------------

        int column = TablaActividades.getColumnModel().getColumnIndexAtX(evt.getX());
        int row = evt.getY()/TablaActividades.getRowHeight();

        if(row < TablaActividades.getRowCount() && row >= 0 && column < TablaActividades.getColumnCount() && column >=0)
        {
            Object value = TablaActividades.getValueAt(row, column);

            if(value instanceof JButton)
            {
                ((JButton)value).doClick();
                JButton boton = (JButton) value;

                if(boton.getName().equals("botonPDF"))
                {
                    VerActividad();
                }
            }
        }
    }//GEN-LAST:event_TablaActividadesMouseClicked

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed

        UIManager.put("FileChooser.acceptAllFileFilterText", "Directorios");
        UIManager.put("FileChooser.lookInLabelText", "Directorio");
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Cancelar");
        UIManager.put("FileChooser.openButtonText", "Cargar archivo");
        UIManager.put("FileChooser.openButtonToolTipText", "Agregar archivo");
        UIManager.put("FileChooser.directoryOpenButtonMnemonic","Abrir carpeta");
        UIManager.put("FileChooser.directoryOpenButtonText","Abrir carpeta");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo:");
        UIManager.put("FileChooser.fileNameLabelText", "Archivo:");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
        UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalles");
        UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Detalles");
        UIManager.put("FileChooser.upFolderToolTipText", "Regresar");
        UIManager.put("FileChooser.upFolderAccessibleName", "Regresar");
        UIManager.put("FileChooser.homeFolderToolTipText", "Escritorio");
        UIManager.put("FileChooser.homeFolderAccessibleName", "Escritorio");
        UIManager.put("FileChooser.fileNameHeaderText", "Escritorio");
        UIManager.put("FileChooser.fileSizeHeaderText", "Tamaño");
        UIManager.put("FileChooser.fileTypeHeaderText", "Tipo");
        UIManager.put("FileChooser.fileDateHeaderText", "Fecha");
        UIManager.put("FileChooser.fileAttrHeaderText", "Propiedades");
        UIManager.put("FileChooser.openDialogTitleText","Seleccionar archivo");
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        // muestra el cuadro de diálogo de archivos, para que el usuario pueda elegir el archivo a abrir
        JFileChooser selectorArchivos = new JFileChooser();
        // Eliminando el filtro 'Todos los archivos (*.*)'

        selectorArchivos.removeChoosableFileFilter(selectorArchivos.getAcceptAllFileFilter());
        //Crea el filtro con los tipos de archivo deseados
        FileFilter ff=new FileNameExtensionFilter("Documentos pdf", "pdf");
        //Añade el filtro con los tipos de archivo deseados
        selectorArchivos.addChoosableFileFilter(ff);
        //Establece el filtro como selección predeterminada
        selectorArchivos.setFileFilter(ff);
        //Permite solo seleccionar archivos, no carpetas
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Permite solo seleccionar un archivo a la vez
        selectorArchivos.setMultiSelectionEnabled(false);
        // indica cual fue la accion de usuario sobre el jfilechooser
        int estado = selectorArchivos.showOpenDialog(null);

        if(estado == JFileChooser.APPROVE_OPTION)
        {
            File archivo = selectorArchivos.getSelectedFile();
            long longitud = archivo.length();
            glongitud = longitud;

            try
            {
                FileInputStream flujo = new FileInputStream(archivo);
                gflujo = flujo;
                //---aqui carga el archivo
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(Revision.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnCargarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
    if(txtBuscar.getText().equals(""))
        {
            mostrarDatos();
        }
        else
        {
            BuscarDatos();
        }
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtErroresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtErroresKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtErroresKeyTyped

    private void btnRevisarOrtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevisarOrtActionPerformed
   
        JLanguageTool langTool = new JLanguageTool(new Spanish());
        // comment in to use statistical ngram data:
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        ContextTools conTools = new ContextTools();   
        // Obten este objeto de tu JTextPane
        SubrayadorTexto estiloTextoIncorrecto = new SubrayadorTexto(Color.YELLOW, Color.RED);
        Highlighter controladorSubrayado = txtTexto.getHighlighter();
        
        String texto = txtTexto.getText();
        int contador = 0;

    
    List<RuleMatch> matches = null;
        try {
         
        
        matches = langTool.check(texto);
       
                       
        } catch (IOException ex) {
            Logger.getLogger(Revision.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        txtErrores.setText(null);
        
    for (RuleMatch match : matches)
    {
    
    contador = contador+1;
      
      txtErrores.append("*=============================================*\n");
      txtErrores.append(contador +".-" + " POSIBLE ERROR EN LOS CARACTERES " 
         + match.getFromPos() + "-" + match.getToPos() + " :\n" + match.getMessage());
      txtErrores.append("\n*=============================================*\n\n");

      
      String contexto = conTools.getPlainTextContext(match.getFromPos(),
      match.getToPos(), txtTexto.getText());
      
      txtErrores.append("                                 ----------Contexto:----------\n"
              + contexto + "\n");
      
      txtErrores.append("                         -----Corrección(es) sugerida(s):-----\n"
              + match.getSuggestedReplacements() +"\n\n");  
      
    }
    
    if (txtTexto.getText().equals(""))
    {
    txtErrores.setText("El área de texto a revisar esta vacía"); 
    }
    else if(contador == 0)
    {
    txtErrores.setText("No se han encontrado errores en el texto");
    }
    
    
            try 
            {
            // Revisa el contenido del text pane y subraya los errores
            langTool.check(txtTexto.getText()).stream().forEach(error -> 
            {
                try 
                {
                controladorSubrayado.addHighlight(error.getFromPos(), error.getToPos(), estiloTextoIncorrecto);
                } 
                catch (BadLocationException ex) 
                {
                Logger.getLogger(Revision.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            );
            } catch (IOException ex) {
            Logger.getLogger(Revision.class.getName()).log(Level.SEVERE, null, ex);
            }
    
       if(contador != 0)
       {
       StringTokenizer st = new StringTokenizer(texto);
       lblCantidadPalabras.setText(String.valueOf(st.countTokens()));            
       lblErroresEncontrados.setText(String.valueOf(contador));
       
       double porcentaje = ((contador*100)/st.countTokens());
       
       lblPorcentajeErrores.setText(String.valueOf(porcentaje)+"%");
       }
              
    }//GEN-LAST:event_btnRevisarOrtActionPerformed

    private void btnPegarTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPegarTextoActionPerformed
        // TODO add your handling code here:
        txtTexto.paste();
    }//GEN-LAST:event_btnPegarTextoActionPerformed

    private void btnLimpiarTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTextoActionPerformed
        // TODO add your handling code here:
        txtTexto.setText(null);
        txtErrores.setText(null);
        
        lblCantidadPalabras.setText("-");
        lblErroresEncontrados.setText("-");
        lblPorcentajeErrores.setText("-");
             
    }//GEN-LAST:event_btnLimpiarTextoActionPerformed

    private void btnCopiarTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarTextoActionPerformed
        // TODO add your handling code here:
        txtTexto.copy();
    }//GEN-LAST:event_btnCopiarTextoActionPerformed

    private void txtTextoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTextoKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTextoKeyReleased

    private void btnReloadWebActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadWebActionPerformed
        // TODO add your handling code here:


        browser.loadURL("https://edubirdie.com/detector-de-plagio");
        PanelNavegador.add(browser);
        


    }//GEN-LAST:event_btnReloadWebActionPerformed

    private void btnAsignarCalificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarCalificacionActionPerformed
        // TODO add your handling code here:
        
        try
        { 
        String SQL="update cargas set Ortografia=?,Originalidad=? where Id_carga=?";

        int filaseleccionado=TablaActividades.getSelectedRow();
        String saoko=(String)TablaActividades.getValueAt(filaseleccionado, 0);
    
        PreparedStatement patr=(PreparedStatement) con.prepareStatement (SQL);

        
        patr.setString(1, comboOrtografia.getItemAt(comboOrtografia.getSelectedIndex()));
        
        patr.setString(2, comboOriginalidad.getItemAt(comboOriginalidad.getSelectedIndex()));       
        
        patr.setString(3,saoko);
    
        patr.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Calificaciones actualizadas correctamente","Actividad calificada",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione la actividad a calificar","No se pudieron añadir las calificaciones",JOptionPane.ERROR_MESSAGE);       
        }
        
        mostrarDatos();
        comboOriginalidad.setSelectedIndex(0);
        comboOrtografia.setSelectedIndex(0);


    }//GEN-LAST:event_btnAsignarCalificacionActionPerformed

    
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
            java.util.logging.Logger.getLogger(Revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Revision.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                new Revision().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Check;
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel Panel3;
    private javax.swing.JPanel Panel4;
    private javax.swing.JPanel PanelNavegador;
    private javax.swing.JTabbedPane Pestañas;
    private javax.swing.JTable TablaActividades;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAsignarCalificacion;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCopiarTexto;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarTodo;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiarTexto;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnPegarTexto;
    private javax.swing.JButton btnReloadWeb;
    private javax.swing.JButton btnRevisarOrt;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> comboMatricula;
    private javax.swing.JComboBox<String> comboNoactividad;
    private javax.swing.JComboBox<String> comboOriginalidad;
    private javax.swing.JComboBox<String> comboOrtografia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCantidadPalabras;
    private javax.swing.JLabel lblErroresEncontrados;
    private javax.swing.JLabel lblPorcentajeErrores;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextArea txtErrores;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtGrupo;
    private javax.swing.JTextField txtIDCarga;
    private javax.swing.JTextField txtNombreAlumno;
    private javax.swing.JTextField txtNombreactividad;
    private javax.swing.JTextArea txtTexto;
    // End of variables declaration//GEN-END:variables


}
