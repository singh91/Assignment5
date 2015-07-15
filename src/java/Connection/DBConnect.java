/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author c0655613
 */
public class DBConnect {
    
    public static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://localhost/database";
            String user = "root";
            String pass = "";
             conn = DriverManager.getConnection(jdbc, user, pass);
            
        }catch(ClassNotFoundException | SQLException ex){
            System.err.println(ex.getMessage());
        }
        
        return conn;
    }
    
}
