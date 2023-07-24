/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionSQL;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Ragnar
 */
public class ConexionSQL {
    
    Connection conectar = null;
    
    
    public Connection conexion()
    {
        try
        {
        Class.forName("com.mysql.jdbc.Driver");
        conectar = DriverManager.getConnection("jdbc:mysql://localhost:3306/residencia","root","");        
        }
        catch(ClassNotFoundException | SQLException e)
        {
        JOptionPane.showMessageDialog(null,"Error al cargar la base de datos de MySQL,reeinstale la aplicación, si el problema persiste contacte al desarrollador","Error",JOptionPane.ERROR_MESSAGE);        

        }
        return conectar;
    }
    
}

