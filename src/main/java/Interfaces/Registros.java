package Interfaces;

//importa el paquete y libreria para la conexon con la BD
import ConexionSQL.ConexionSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;





/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */



/**
 *
 * @author Ragnar
 */
public class Registros extends javax.swing.JFrame {

    /**
     * Creates new form PROMEDIOS
     */
    ConexionSQL cc = new ConexionSQL();  
    Connection con = cc.conexion();
      
    
    public Registros()
    {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/ICONO.png")).getImage());

        this.setLocationRelativeTo(null); 

        deshabilitarBtonesyCajas();
        mostrarDatos();
        
        deshabilitarBtonesyCajasA();
        mostrarDatosA();
        
        deshabilitarBtonesyCajasG();
        mostrarDatosG();
        
        ((JTextField) this.txtFecha.getDateEditor()).setEditable(false); 
        
        TablaActividades.getTableHeader().setResizingAllowed(false);
        TablaAlumnos.getTableHeader().setResizingAllowed(false);
        TablaGrupos.getTableHeader().setResizingAllowed(false);
        
        ConsultarIDGrupo(comboGrupo);

        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.yesButtonText", "Si");
    }

    @SuppressWarnings("empty-statement")  
    
    public void deshabilitarBtonesyCajas()
    {
    txtNoactividad.setEnabled(false);
    txtNombre.setEnabled(false);
    txtFecha.setEnabled(false);
    txtValor.setEnabled(false);
    btnCancelar.setEnabled(false);
    btnGuardar.setEnabled(false);
    btnActualizar.setEnabled(false);
    btnNuevo.setEnabled(true);

    }
    
    public void habilitarBtonesyCajas()
    {
    txtNoactividad.setEnabled(true);
    txtNombre.setEnabled(true);
    txtFecha.setEnabled(true);
    txtValor.setEnabled(true);
    btnCancelar.setEnabled(true);
    btnGuardar.setEnabled(true);
    btnActualizar.setEnabled(true);
    btnNuevo.setEnabled(false);
    }
    
    
        public void ConsultarIDGrupo(JComboBox IDGrupo)
        {
             
        String SQL = "SELECT Id_grupo from grupos";
            
        try 
        {           
        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);
        
        while (rs.next()) 
        {
        IDGrupo.addItem(rs.getString("Id_grupo"));
        }
        } 
        catch (NumberFormatException | SQLException ex) {
        System.out.println("error combo" + ex.getMessage());
        }
    }
    
        
    public void mostrarDatos()
    {


        TablaActividades.setRowHeight(25);
        
        
        String[] titulos={"No.Actividad","Nombre de la actividad","Fecha de asignación","Valor de la actividad %"};
        Object registros[]=new Object[4];

    
        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        for (int c = 0; c < TablaActividades.getColumnCount(); c++)
        {
        Class<?> col_class = TablaActividades.getColumnClass(c);
        TablaActividades.setDefaultEditor(col_class, null); 
        }
    
        String SQL= "select * from actividades";

        try
        {
        Statement st=(Statement) con.createStatement();
        ResultSetImpl rs=(ResultSetImpl) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("No_actividad");
        registros[1]=rs.getString("Nombre_actividad");
        registros[2]=rs.getString("Fecha_asignacion");
        registros[3]=rs.getString("Valor_actividad");

  
        
        modelo.addRow(registros);

        }
        TablaActividades.setModel(modelo);
        } 
        catch(Exception e)
        {
        }
    } 

   
    public void BuscarDatos()
    {
        

        TablaActividades.setRowHeight(25);
        
        String[] titulos={"No.Actividad","Nombre de la actividad","Fecha de asignación","Valor de la actividad %"};
        Object registros[]=new Object[4];

        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        String id = txtBuscar.getText();
     
        String SQL="SELECT No_actividad, Nombre_actividad, Fecha_asignacion, Valor_actividad "
        + "FROM actividades WHERE No_actividad LIKE '%"+id+"%'";

        try
        {

        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("No_actividad");
        registros[1]=rs.getString("Nombre_actividad");
        registros[2]=rs.getString("Fecha_asignacion");
        registros[3]=rs.getString("Valor_actividad");

        
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
        String SQL="insert into actividades (No_actividad,Nombre_actividad,Fecha_asignacion,Valor_actividad) values (?,?,?,?)";

        PreparedStatement pat=(PreparedStatement) con.prepareStatement (SQL);

        pat.setString(1, txtNoactividad.getText());
        
        pat.setString(2, txtNombre.getText());

        pat.setString(3, ((JTextField)txtFecha.getDateEditor().getUiComponent()).getText());
        
        pat.setString(4, txtValor.getText());

        
        pat.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro añadido correctamente","Datos añadidos",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, llene todos los campos y verifique la integridad de los datos","No se pudo insertar el registro",JOptionPane.ERROR_MESSAGE);
        }
    }


    public void actualizarDatos()
    {

        try
        { 
        String SQL="update actividades set No_actividad=?,Nombre_actividad=?,Fecha_asignacion=?,Valor_actividad=? where No_actividad=?";

        int filaseleccionado=TablaActividades.getSelectedRow();
        String saoko=(String)TablaActividades.getValueAt(filaseleccionado, 0);
    
        PreparedStatement patr=(PreparedStatement) con.prepareStatement (SQL);

        patr.setString(1, txtNoactividad.getText());
        
        patr.setString(2, txtNombre.getText());

        patr.setString(3, ((JTextField)txtFecha.getDateEditor().getUiComponent()).getText());
        
        patr.setString(4, txtValor.getText());

        patr.setString(5,saoko);
    
        patr.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro actualizado correctamente","Datos actualizados",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione el registro a actualizar, llene todos los campos y verifique la integridad de los datos","No se pudo actualizar el registro",JOptionPane.ERROR_MESSAGE);        }
    }
        
    
    public void eliminarRegistros()
    {
        
        int filSeleccionada=TablaActividades.getSelectedRow();
        
        try 
        {
        String SQL="delete from actividades where No_actividad='"+TablaActividades.getValueAt(filSeleccionada,0)+"'";
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
        String SQL="delete from actividades";
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
        txtNoactividad.setText("");
        txtNombre.setText("");
        txtFecha.setCalendar(null);
        txtValor.setText("");

    }
    
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------

    public void deshabilitarBtonesyCajasA()
    {
    txtMatricula.setEnabled(false);
    txtNombreAl.setEnabled(false);
    comboGrupo.setEnabled(false);
    btnCancelarA.setEnabled(false);
    btnGuardarA.setEnabled(false);
    btnActualizarA.setEnabled(false);
    btnNuevoA.setEnabled(true);

    }
    
    public void habilitarBtonesyCajasA()
    {
    txtMatricula.setEnabled(true);
    txtNombreAl.setEnabled(true);
    comboGrupo.setEnabled(true);
    btnCancelarA.setEnabled(true);
    btnGuardarA.setEnabled(true);
    btnActualizarA.setEnabled(true);
    btnNuevoA.setEnabled(false);
    }

public void mostrarDatosA()
    {


        TablaAlumnos.setRowHeight(25);
        
        
        String[] titulos={"Matrícula","Nombre completo","Grupo"};
        Object registros[]=new Object[3];

    
        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        for (int c = 0; c < TablaAlumnos.getColumnCount(); c++)
        {
        Class<?> col_class = TablaAlumnos.getColumnClass(c);
        TablaAlumnos.setDefaultEditor(col_class, null); 
        }
    
        String SQL= "select * from alumnos";

        try
        {
        Statement st=(Statement) con.createStatement();
        ResultSetImpl rs=(ResultSetImpl) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Matricula");
        registros[1]=rs.getString("Nombre_completo");
        registros[2]=rs.getString("Id_grupo");


        
        modelo.addRow(registros);

        }
        TablaAlumnos.setModel(modelo);
        } 
        catch(Exception e)
        {
        }
    } 

public void BuscarDatosA()
    {
        

        TablaAlumnos.setRowHeight(25);
        
        String[] titulos={"Matrícula","Nombre completo","Grupo"};
        Object registros[]=new Object[3];

        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        String id = txtBuscarA.getText();
     
        String SQL="SELECT Matricula, Nombre_completo, Id_grupo "
        + "FROM alumnos WHERE Matricula LIKE '%"+id+"%'";

        try
        {

        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Matricula");
        registros[1]=rs.getString("Nombre_completo");
        registros[2]=rs.getString("Id_grupo");
        
        modelo.addRow(registros);
        }

        TablaAlumnos.setModel(modelo);
        } 
        catch(Exception e)
        {
        }  
      
    }

  public void insertarDatosA()
    {
        
        try
        { 
        String SQL="insert into alumnos (Matricula,Nombre_completo,Id_grupo) values (?,?,?)";

        PreparedStatement pat=(PreparedStatement) con.prepareStatement (SQL);

        pat.setString(1, txtMatricula.getText());
        
        pat.setString(2, txtNombreAl.getText());
        
        pat.setString(3, comboGrupo.getItemAt(comboGrupo.getSelectedIndex()));
 
        pat.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro añadido correctamente","Datos añadidos",JOptionPane.INFORMATION_MESSAGE);        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, llene todos los campos y verifique la integridad de los datos","No se pudo insertar el registro",JOptionPane.ERROR_MESSAGE);
        }
    }
  
   public void actualizarDatosA()
    {

        try
        { 
        String SQL="update alumnos set Matricula=?,Nombre_completo=?,Id_grupo=? where Matricula=?";

        int filaseleccionado=TablaAlumnos.getSelectedRow();
        String saoko=(String)TablaAlumnos.getValueAt(filaseleccionado, 0);
    
        PreparedStatement patr=(PreparedStatement) con.prepareStatement (SQL);

        patr.setString(1, txtMatricula.getText());
        
        patr.setString(2, txtNombreAl.getText());
        
        patr.setString(3, comboGrupo.getItemAt(comboGrupo.getSelectedIndex()));

        patr.setString(4,saoko);
    
        patr.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro actualizado correctamente","Datos actualizados",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione el registro a actualizar, llene todos los campos y verifique la integridad de los datos","No se pudo actualizar el registro",JOptionPane.ERROR_MESSAGE);        }
    }
   
     
    public void eliminarRegistrosA()
    {
        
        int filSeleccionada=TablaAlumnos.getSelectedRow();
        
        try 
        {
        String SQL="delete from alumnos where Matricula='"+TablaAlumnos.getValueAt(filSeleccionada,0)+"'";
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
    
     public void eliminarTodosRegistrosA()
    {
 
        try 
        {
        String SQL="delete from alumnos";
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
     
    public void limpiarCajasA()
    {
        txtMatricula.setText("");
        txtNombreAl.setText("");
    }
    
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------
    
    public void deshabilitarBtonesyCajasG()
    {
    txtIDGrupo.setEnabled(false);
    txtCarrera.setEnabled(false);
    btnCancelarG.setEnabled(false);
    btnGuardarG.setEnabled(false);
    btnActualizarG.setEnabled(false);
    btnNuevoG.setEnabled(true);

    }
    
    public void habilitarBtonesyCajasG()
    {
    txtIDGrupo.setEnabled(true);
    txtCarrera.setEnabled(true);
    btnCancelarG.setEnabled(true);
    btnGuardarG.setEnabled(true);
    btnActualizarG.setEnabled(true);
    btnNuevoG.setEnabled(false);
    }
    
        public void mostrarDatosG()
    {


        TablaGrupos.setRowHeight(25);
        
        
        String[] titulos={"ID Grupo","Carrera"};
        Object registros[]=new Object[2];

    
        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        for (int c = 0; c < TablaGrupos.getColumnCount(); c++)
        {
        Class<?> col_class = TablaGrupos.getColumnClass(c);
        TablaGrupos.setDefaultEditor(col_class, null); 
        }
    
        String SQL= "select * from grupos";

        try
        {
        Statement st=(Statement) con.createStatement();
        ResultSetImpl rs=(ResultSetImpl) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Id_grupo");
        registros[1]=rs.getString("Carrera");

        
        modelo.addRow(registros);

        }
        TablaGrupos.setModel(modelo);
        } 
        catch(Exception e)
        {
        }
    } 
        
         public void BuscarDatosG()
    {
        

        TablaGrupos.setRowHeight(25);
        
        String[] titulos={"ID Grupo","Carrera"};
        Object registros[]=new Object[2];

        DefaultTableModel modelo=new DefaultTableModel(null,titulos);

        String id = txtBuscarG.getText();
     
        String SQL="SELECT Id_grupo, carrera "
        + "FROM grupos WHERE Id_grupo LIKE '%"+id+"%'";

        try
        {

        Statement st=(Statement) con.createStatement();
        ResultSet rs=(ResultSet) st.executeQuery(SQL);

        while(rs.next())
        {
        registros[0]=rs.getString("Id_grupo");
        registros[1]=rs.getString("Carrera");
        
        modelo.addRow(registros);
        }

        TablaGrupos.setModel(modelo);
        } 
        catch(Exception e)
        {
        }  
      
    }
    
    public void insertarDatosG()
    {
        
        try
        { 
        String SQL="insert into grupos (Id_grupo,Carrera) values (?,?)";

        PreparedStatement pat=(PreparedStatement) con.prepareStatement (SQL);

        pat.setString(1, txtIDGrupo.getText());
        
        pat.setString(2, txtCarrera.getText());
 
        pat.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro añadido correctamente","Datos añadidos",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, llene todos los campos y verifique la integridad de los datos","No se pudo insertar el registro",JOptionPane.ERROR_MESSAGE);
        }
    }
    
     public void actualizarDatosG()
    {

        try
        { 
        String SQL="update grupos set Id_grupo=?,Carrera=? where Id_grupo=?";

        int filaseleccionado=TablaGrupos.getSelectedRow();
        String saoko=(String)TablaGrupos.getValueAt(filaseleccionado, 0);
    
        PreparedStatement patr=(PreparedStatement) con.prepareStatement (SQL);

        patr.setString(1, txtIDGrupo.getText());
        
        patr.setString(2, txtCarrera.getText());

        patr.setString(3,saoko);
    
        patr.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"Registro actualizado correctamente","Datos actualizados",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
        JOptionPane.showMessageDialog(null,"Por favor, Seleccione el registro a actualizar, llene todos los campos y verifique la integridad de los datos","No se pudo actualizar el registro",JOptionPane.ERROR_MESSAGE);        }
    }
        
    
    public void eliminarRegistrosG()
    {
        
        int filSeleccionada=TablaGrupos.getSelectedRow();
        
        try 
        {
        String SQL="delete from grupos where Id_grupo='"+TablaGrupos.getValueAt(filSeleccionada,0)+"'";
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
    
    public void eliminarTodosRegistrosG()
    {
 
        try 
        {
        String SQL="delete from grupos";
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
      
    
    public void limpiarCajasG()
    {
        txtIDGrupo.setText("");
        txtCarrera.setText("");

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
        btnEliminar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaActividades = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtNoactividad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminarTodo = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        Panel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtIDGrupo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCarrera = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaGrupos = new javax.swing.JTable();
        btnNuevoG = new javax.swing.JButton();
        btnCancelarG = new javax.swing.JButton();
        btnGuardarG = new javax.swing.JButton();
        btnActualizarG = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtBuscarG = new javax.swing.JTextField();
        btnEliminarG = new javax.swing.JButton();
        btnEliminarTodoG = new javax.swing.JButton();
        Panel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMatricula = new javax.swing.JTextField();
        txtNombreAl = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaAlumnos = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtBuscarA = new javax.swing.JTextField();
        btnNuevoA = new javax.swing.JButton();
        btnCancelarA = new javax.swing.JButton();
        btnGuardarA = new javax.swing.JButton();
        btnActualizarA = new javax.swing.JButton();
        btnEliminarA = new javax.swing.JButton();
        btnEliminarTodoA = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        comboGrupo = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registros");
        setName("frmRegistros"); // NOI18N
        setResizable(false);

        jLabel1.setBackground(new java.awt.Color(51, 204, 0));
        jLabel1.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        jLabel1.setText("REGISTRO DE DATOS");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        Pestañas.setBorder(new javax.swing.border.MatteBorder(null));
        Pestañas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PestañasFocusGained(evt);
            }
        });

        Panel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Panel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                Panel1MouseMoved(evt);
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

        TablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.Actividad", "Nombre de la actividad", "Fecha de asignación", "Valor de la actividad %"
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
            TablaActividades.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel3.setText("Valor de la actividad:");

        txtNoactividad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoactividadKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel4.setText("Fecha de asignación:");

        txtFecha.setDateFormatString("yyyy-MM-dd");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng1.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar nueva actividad");
        btnGuardar.setBorder(null);
        btnGuardar.setContentAreaFilled(false);
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
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna3 .png"))); // NOI18N
        btnActualizar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna2.png"))); // NOI18N
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
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

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Search.png"))); // NOI18N
        jLabel6.setText("  Buscar por número:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setText("Nombre de la actividad:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel5.setText("No. Actividad:");

        txtValor.setName(""); // NOI18N
        txtValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValorKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("%");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc3 .png"))); // NOI18N
        btnCancelar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc2 .png"))); // NOI18N
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNoactividad)
                                .addComponent(txtNombre))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(103, 103, 103)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel1Layout.createSequentialGroup()
                                .addComponent(txtNoactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        Pestañas.addTab("Actividades", Panel1);

        Panel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel12.setText("ID Grupo:");

        txtIDGrupo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDGrupoKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel13.setText("Carrera:");

        txtCarrera.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCarreraKeyTyped(evt);
            }
        });

        TablaGrupos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Grupo", "Carrera"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaGrupos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TablaGrupos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TablaGrupos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaGruposMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TablaGrupos);

        btnNuevoG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn1 .png"))); // NOI18N
        btnNuevoG.setToolTipText("Registrar o actualizar datos de grupo");
        btnNuevoG.setBorder(null);
        btnNuevoG.setContentAreaFilled(false);
        btnNuevoG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevoG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn3 .png"))); // NOI18N
        btnNuevoG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn2 .png"))); // NOI18N
        btnNuevoG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoGActionPerformed(evt);
            }
        });

        btnCancelarG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc1 .png"))); // NOI18N
        btnCancelarG.setToolTipText("Cancelar operación");
        btnCancelarG.setBorder(null);
        btnCancelarG.setContentAreaFilled(false);
        btnCancelarG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelarG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc3 .png"))); // NOI18N
        btnCancelarG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc2 .png"))); // NOI18N
        btnCancelarG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarGActionPerformed(evt);
            }
        });

        btnGuardarG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng1.png"))); // NOI18N
        btnGuardarG.setToolTipText("Guardar nuevo grupo");
        btnGuardarG.setBorder(null);
        btnGuardarG.setContentAreaFilled(false);
        btnGuardarG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardarG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng3 .png"))); // NOI18N
        btnGuardarG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng2.png"))); // NOI18N
        btnGuardarG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarGActionPerformed(evt);
            }
        });

        btnActualizarG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna1.png"))); // NOI18N
        btnActualizarG.setToolTipText("Actualizar grupo seleccionado");
        btnActualizarG.setBorder(null);
        btnActualizarG.setContentAreaFilled(false);
        btnActualizarG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizarG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna3 .png"))); // NOI18N
        btnActualizarG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna2.png"))); // NOI18N
        btnActualizarG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarGActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Search.png"))); // NOI18N
        jLabel14.setText("  Buscar por ID:");

        txtBuscarG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarGKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarGKeyTyped(evt);
            }
        });

        btnEliminarG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne1 .png"))); // NOI18N
        btnEliminarG.setToolTipText("Eliminar grupo seleccionado");
        btnEliminarG.setBorder(null);
        btnEliminarG.setContentAreaFilled(false);
        btnEliminarG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne3 .png"))); // NOI18N
        btnEliminarG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne2 .png"))); // NOI18N
        btnEliminarG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarGActionPerformed(evt);
            }
        });

        btnEliminarTodoG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet1 .png"))); // NOI18N
        btnEliminarTodoG.setToolTipText("Eliminar todos los grupos registrados");
        btnEliminarTodoG.setBorder(null);
        btnEliminarTodoG.setContentAreaFilled(false);
        btnEliminarTodoG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarTodoG.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet3.png"))); // NOI18N
        btnEliminarTodoG.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet2 .png"))); // NOI18N
        btnEliminarTodoG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodoGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel3Layout = new javax.swing.GroupLayout(Panel3);
        Panel3.setLayout(Panel3Layout);
        Panel3Layout.setHorizontalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel3Layout.createSequentialGroup()
                        .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarG, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel3Layout.createSequentialGroup()
                        .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIDGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txtCarrera))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel3Layout.createSequentialGroup()
                                .addComponent(btnNuevoG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)
                                .addComponent(btnGuardarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(105, 105, 105)
                                .addComponent(btnEliminarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarTodoG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(30, 30, 30))
        );
        Panel3Layout.setVerticalGroup(
            Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel3Layout.createSequentialGroup()
                .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtBuscarG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Panel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel3Layout.createSequentialGroup()
                                .addComponent(txtIDGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnNuevoG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarTodoG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnGuardarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarG, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        Pestañas.addTab("Grupos", Panel3);

        Panel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel9.setText("Matrícula:");

        txtMatricula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMatriculaKeyTyped(evt);
            }
        });

        txtNombreAl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreAlKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel10.setText("Nombre completo del alumno:");

        TablaAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nombre completo del alumno"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaAlumnos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TablaAlumnos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TablaAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaAlumnosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TablaAlumnos);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Search.png"))); // NOI18N
        jLabel11.setText("  Buscar por matrícula:");

        txtBuscarA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarAKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarAKeyTyped(evt);
            }
        });

        btnNuevoA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn1 .png"))); // NOI18N
        btnNuevoA.setToolTipText("Registrar o actualizar datos de alumno");
        btnNuevoA.setBorder(null);
        btnNuevoA.setContentAreaFilled(false);
        btnNuevoA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevoA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn3 .png"))); // NOI18N
        btnNuevoA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnn2 .png"))); // NOI18N
        btnNuevoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoAActionPerformed(evt);
            }
        });

        btnCancelarA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc1 .png"))); // NOI18N
        btnCancelarA.setToolTipText("Cancelar operación");
        btnCancelarA.setBorder(null);
        btnCancelarA.setContentAreaFilled(false);
        btnCancelarA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelarA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc3 .png"))); // NOI18N
        btnCancelarA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnc2 .png"))); // NOI18N
        btnCancelarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAActionPerformed(evt);
            }
        });

        btnGuardarA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng1.png"))); // NOI18N
        btnGuardarA.setToolTipText("Guardar nuevo alumno");
        btnGuardarA.setBorder(null);
        btnGuardarA.setContentAreaFilled(false);
        btnGuardarA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardarA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng3 .png"))); // NOI18N
        btnGuardarA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btng2.png"))); // NOI18N
        btnGuardarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarAActionPerformed(evt);
            }
        });

        btnActualizarA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna1.png"))); // NOI18N
        btnActualizarA.setToolTipText("Actualizar alumno seleccionado");
        btnActualizarA.setBorder(null);
        btnActualizarA.setContentAreaFilled(false);
        btnActualizarA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActualizarA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna3 .png"))); // NOI18N
        btnActualizarA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btna2.png"))); // NOI18N
        btnActualizarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarAActionPerformed(evt);
            }
        });

        btnEliminarA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne1 .png"))); // NOI18N
        btnEliminarA.setToolTipText("Eliminar alumno seleccionado");
        btnEliminarA.setBorder(null);
        btnEliminarA.setContentAreaFilled(false);
        btnEliminarA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne3 .png"))); // NOI18N
        btnEliminarA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btne2 .png"))); // NOI18N
        btnEliminarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarAActionPerformed(evt);
            }
        });

        btnEliminarTodoA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet1 .png"))); // NOI18N
        btnEliminarTodoA.setToolTipText("Eliminar todos los alumnos registrados");
        btnEliminarTodoA.setBorder(null);
        btnEliminarTodoA.setContentAreaFilled(false);
        btnEliminarTodoA.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminarTodoA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet3.png"))); // NOI18N
        btnEliminarTodoA.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/btnet2 .png"))); // NOI18N
        btnEliminarTodoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTodoAActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel15.setText("Grupo:");

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Panel2Layout.createSequentialGroup()
                            .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNombreAl, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addComponent(jLabel9))
                .addGap(12, 12, 12)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarA, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2Layout.createSequentialGroup()
                        .addComponent(btnNuevoA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(btnEliminarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarTodoA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel2Layout.createSequentialGroup()
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtBuscarA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Panel2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel2Layout.createSequentialGroup()
                                .addComponent(txtMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreAl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevoA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnGuardarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarTodoA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCancelarA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        Pestañas.addTab("Alumnos", Panel2);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/UDEMEX.png"))); // NOI18N

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(178, 178, 178)
                        .addComponent(jLabel8)
                        .addGap(45, 45, 45))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addComponent(Pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    
    System.exit(0);
    
    }//GEN-LAST:event_btnSalirActionPerformed

    private void Panel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Panel1MouseMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_Panel1MouseMoved

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped


        int key = evt.getKeyChar();

        boolean numeros = key >= 48 && key <= 57;
        
        if (!numeros)
        {
        evt.consume();
        }
        
        if(txtBuscar.getText().length() >= 3)
        {
            evt.consume();
        }
        
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void btnEliminarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodoActionPerformed

        if(JOptionPane.showConfirmDialog(null,"Se eliminarán todos los registros de la tabla, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarTodosRegistros();
            limpiarCajas();
            mostrarDatos();
        }
    }//GEN-LAST:event_btnEliminarTodoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        actualizarDatos();
        limpiarCajas();
        mostrarDatos();
        deshabilitarBtonesyCajas();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        insertarDatos();
        limpiarCajas();
        mostrarDatos();
        deshabilitarBtonesyCajas();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNoactividadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoactividadKeyTyped
    
        int key = evt.getKeyChar();

        boolean numeros = key >= 48 && key <= 57;
        
        if (!numeros)
        {
        evt.consume();
        }
        
        if(txtNoactividad.getText().length() >= 3)
        {
            evt.consume();
        }

    }//GEN-LAST:event_txtNoactividadKeyTyped

    private void TablaActividadesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaActividadesMouseClicked
        // TODO add your handling code here:

        int filaSeleccionada=TablaActividades.rowAtPoint(evt.getPoint());

        txtNoactividad.setText(TablaActividades.getValueAt(filaSeleccionada, 0).toString());
        
        txtNombre.setText(TablaActividades.getValueAt(filaSeleccionada, 1).toString());


        int fila = TablaActividades.getSelectedRow();
        //obtenemos la fecha de dicha fila
        String fecha = TablaActividades.getValueAt(fila, 2).toString();
        //creamos el formato en el que deseamos mostrar la fecha
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        //creamos una variable tipo Date y la ponemos NULL
        Date fechaN = null;
        
        txtValor.setText(TablaActividades.getValueAt(filaSeleccionada, 3).toString());

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

   
    }//GEN-LAST:event_TablaActividadesMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    
        if(JOptionPane.showConfirmDialog(null,"Se eliminará el registro seleccionado, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarRegistros();
            limpiarCajas();
            mostrarDatos();
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        
        if(txtBuscar.getText().equals(""))
        {
        mostrarDatos();    
        }
        else
        {
        BuscarDatos();
        }
        
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
 
        if(txtNombre.getText().length() >= 150)
        {
            evt.consume();
        }    
        
        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
    
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtValorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyTyped
            
        int key = evt.getKeyChar();

        boolean numeros = key >= 48 && key <= 57;
        
        if (!numeros)
        {
        evt.consume();
        }
        
        if(txtValor.getText().length() >= 3)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtValorKeyTyped

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       habilitarBtonesyCajas();
       limpiarCajas();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
       deshabilitarBtonesyCajas();
       limpiarCajas();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtMatriculaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatriculaKeyTyped

        if(txtMatricula.getText().length() >= 16)
        {
            evt.consume();
        }

        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
    }//GEN-LAST:event_txtMatriculaKeyTyped

    private void txtNombreAlKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAlKeyTyped

        if(txtNombreAl.getText().length() >= 150)
        {
            evt.consume();
        }

        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);

        int key = evt.getKeyChar();

        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;
        boolean espacio = key == 32;

        if (!(minusculas || mayusculas || espacio))
        {
            evt.consume();
        }

    }//GEN-LAST:event_txtNombreAlKeyTyped

    private void TablaAlumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaAlumnosMouseClicked
        // TODO add your handling code here:

        int filaSeleccionada=TablaAlumnos.rowAtPoint(evt.getPoint());

        txtMatricula.setText(TablaAlumnos.getValueAt(filaSeleccionada, 0).toString());

        txtNombreAl.setText(TablaAlumnos.getValueAt(filaSeleccionada, 1).toString());
        
        comboGrupo.setSelectedItem(TablaAlumnos.getValueAt(filaSeleccionada, 2).toString());


    }//GEN-LAST:event_TablaAlumnosMouseClicked

    private void txtBuscarAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAKeyReleased

        if(txtBuscarA.getText().equals(""))
        {
            mostrarDatosA();
        }
        else
        {
            BuscarDatosA();
        }

    }//GEN-LAST:event_txtBuscarAKeyReleased

    private void txtBuscarAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAKeyTyped

        if(txtBuscarA.getText().length() >= 16)
        {
            evt.consume();
        }

        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);

    }//GEN-LAST:event_txtBuscarAKeyTyped

    private void btnNuevoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoAActionPerformed
        habilitarBtonesyCajasA();
        limpiarCajasA();
    }//GEN-LAST:event_btnNuevoAActionPerformed

    private void btnCancelarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAActionPerformed
        deshabilitarBtonesyCajasA();
        limpiarCajasA();
    }//GEN-LAST:event_btnCancelarAActionPerformed

    private void btnGuardarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAActionPerformed

        insertarDatosA();
        limpiarCajasA();
        mostrarDatosA();
        deshabilitarBtonesyCajasA();
    }//GEN-LAST:event_btnGuardarAActionPerformed

    private void btnActualizarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarAActionPerformed
        // TODO add your handling code here:
        actualizarDatosA();
        limpiarCajasA();
        mostrarDatosA();
        deshabilitarBtonesyCajasA();
    }//GEN-LAST:event_btnActualizarAActionPerformed

    private void btnEliminarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarAActionPerformed
 
        if(JOptionPane.showConfirmDialog(null,"Se eliminará el registro seleccionado, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarRegistrosA();
            limpiarCajasA();
            mostrarDatosA();
        }
    }//GEN-LAST:event_btnEliminarAActionPerformed

    private void btnEliminarTodoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodoAActionPerformed

        if(JOptionPane.showConfirmDialog(null,"Se eliminarán todos los registros de la tabla, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarTodosRegistrosA();
            limpiarCajasA();
            mostrarDatosA();
        }
    }//GEN-LAST:event_btnEliminarTodoAActionPerformed

    private void txtIDGrupoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDGrupoKeyTyped
        // TODO add your handling code here:
        if(txtIDGrupo.getText().length() >= 3)
        {
            evt.consume();
        }
        
        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
    }//GEN-LAST:event_txtIDGrupoKeyTyped

    private void txtCarreraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCarreraKeyTyped
        // TODO add your handling code here:
        if(txtCarrera.getText().length() >= 150)
        {
            evt.consume();
        }    
        
        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
        
        int key = evt.getKeyChar();

        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;
        boolean espacio = key == 32;
            
        if (!(minusculas || mayusculas || espacio))
        {
        evt.consume();
        }
    }//GEN-LAST:event_txtCarreraKeyTyped

    private void TablaGruposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaGruposMouseClicked
        // TODO add your handling code here:
        int filaSeleccionada=TablaGrupos.rowAtPoint(evt.getPoint());

        txtIDGrupo.setText(TablaGrupos.getValueAt(filaSeleccionada, 0).toString());
        
        txtCarrera.setText(TablaGrupos.getValueAt(filaSeleccionada, 1).toString());
    }//GEN-LAST:event_TablaGruposMouseClicked

    private void btnNuevoGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoGActionPerformed
        // TODO add your handling code here:
       habilitarBtonesyCajasG();
       limpiarCajasG();
    }//GEN-LAST:event_btnNuevoGActionPerformed

    private void btnCancelarGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarGActionPerformed
        // TODO add your handling code here:
       deshabilitarBtonesyCajasG();
       limpiarCajasG();
    }//GEN-LAST:event_btnCancelarGActionPerformed

    private void btnGuardarGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarGActionPerformed
        // TODO add your handling code here:
        insertarDatosG();
        limpiarCajasG();
        mostrarDatosG();
        deshabilitarBtonesyCajasG();

    }//GEN-LAST:event_btnGuardarGActionPerformed

    private void btnActualizarGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarGActionPerformed
        // TODO add your handling code here:
        actualizarDatosG();
        limpiarCajasG();
        mostrarDatosG();
        deshabilitarBtonesyCajasG();

    }//GEN-LAST:event_btnActualizarGActionPerformed

    private void txtBuscarGKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarGKeyReleased
        // TODO add your handling code here:
                if(txtBuscarG.getText().equals(""))
        {
        mostrarDatosG();    
        }
        else
        {
        BuscarDatosG();
        }
    }//GEN-LAST:event_txtBuscarGKeyReleased

    private void txtBuscarGKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarGKeyTyped
        // TODO add your handling code here:
        if(txtBuscarG.getText().length() >= 16)
        {
            evt.consume();
        }
        
        char c = evt.getKeyChar();
        String cad = ("" + c).toUpperCase();
        c = cad.charAt(0);
        evt.setKeyChar(c);
    }//GEN-LAST:event_txtBuscarGKeyTyped

    private void btnEliminarGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarGActionPerformed
        // TODO add your handling code here:
  
        if(JOptionPane.showConfirmDialog(null,"Se eliminará el registro seleccionado, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarRegistrosG();
            limpiarCajasG();
            mostrarDatosG();

        }
    }//GEN-LAST:event_btnEliminarGActionPerformed

    private void btnEliminarTodoGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTodoGActionPerformed
        // TODO add your handling code here:
        if(JOptionPane.showConfirmDialog(null,"Se eliminarán todos los registros de la tabla, ¿Desea continuar?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminarTodosRegistrosG();
            limpiarCajasG();
            mostrarDatosG();

        }
    }//GEN-LAST:event_btnEliminarTodoGActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Menu verformulario=new Menu(); 
        verformulario.setVisible(true);
    }//GEN-LAST:event_btnVolverActionPerformed

    private void PestañasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PestañasFocusGained
        // TODO add your handling code here:
        comboGrupo.removeAllItems();

        ConsultarIDGrupo(comboGrupo);
    }//GEN-LAST:event_PestañasFocusGained

    
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
            java.util.logging.Logger.getLogger(Registros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Registros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Registros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Registros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
 
                new Registros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel Panel3;
    private javax.swing.JTabbedPane Pestañas;
    private javax.swing.JTable TablaActividades;
    private javax.swing.JTable TablaAlumnos;
    private javax.swing.JTable TablaGrupos;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnActualizarA;
    private javax.swing.JButton btnActualizarG;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelarA;
    private javax.swing.JButton btnCancelarG;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarA;
    private javax.swing.JButton btnEliminarG;
    private javax.swing.JButton btnEliminarTodo;
    private javax.swing.JButton btnEliminarTodoA;
    private javax.swing.JButton btnEliminarTodoG;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarA;
    private javax.swing.JButton btnGuardarG;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnNuevoA;
    private javax.swing.JButton btnNuevoG;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> comboGrupo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtBuscarA;
    private javax.swing.JTextField txtBuscarG;
    private javax.swing.JTextField txtCarrera;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIDGrupo;
    private javax.swing.JTextField txtMatricula;
    private javax.swing.JTextField txtNoactividad;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreAl;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
