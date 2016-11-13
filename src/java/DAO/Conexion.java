/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DSRS
 */
public class Conexion {
    
  
    public static Connection getConnection() {
        
        //String url = "jdbc:mysql://eu-cdbr-west-01.cleardb.com:3306/heroku_14f902afc2d889d?user=b2b1fcab390651&password=07633c3f";
        String url = "jdbc:mysql://localhost:3308/tesis_bd?user=root&password=root";
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("error de conexion: "+e);
        }
        return con;
    }
    
    
}
