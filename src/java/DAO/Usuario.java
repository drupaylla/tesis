/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.servlet.jsp.jstl.sql.Result;

/**
 *
 * @author DSRS
 */
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String username;
    private String clave;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String apellido, String username, String clave) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.clave = clave;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    //VALIDAR USUARIO
    public boolean validarUsuario(String username, String clave) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from usuario where username = '" + username + "' and clave = '" + clave + "'";
        boolean sw = false;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("antes del while");
            while (rs.next()) {       
//                System.out.println("IDUsuario logueado: " + rs.getInt("idusuario"));
                if (rs.getInt("idusuario") > 0) {
                    sw = true;
                } else {
                    sw = false;
                }
            }
            ps.close();
            rs.close();
            con.close();
            System.out.println("bien: " + sw);
        } catch (Exception e) {

            sw = false;
            System.out.println("catch: " + sw);
            System.out.println(e);
        }
        return sw;
    }

    //REGISTRAR USUARIO 
    public boolean registrarUsuario(String nombre, String apellido, String username, String clave) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        String sql = "insert into usuario values (?,?,?,?,?)";
        int num = 0;
        boolean rpta = false;
        try {
            if (this.validarUsuario(username, clave) == false) {
                int id = this.obtenerUltimoIDUsuarios();
                ps = con.prepareStatement(sql);
                ps.setInt(1, id + 1);
                ps.setString(2, nombre);
                ps.setString(3, apellido);
                ps.setString(4, username);
                ps.setString(5, clave);
                num = ps.executeUpdate();
                System.out.println(num);
                rpta = true;
                ps.close();
                con.close();
            } else {
                rpta = false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return rpta;
    }

    //SOPORTE. BUSCAR ULITMO _ID 
    public int obtenerUltimoIDUsuarios() {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        int ultimo = 0;
        String sql = "select idusuario from usuario order by idusuario desc limit 1";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ultimo = rs.getInt(1);
            }
            con.close();
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ultimo;
    }

    //MODIFICAR DATOS DE USUARIO
    public String modificarDatosUsuario(int _id, String nombre, String apellido, String username) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        int num = 0;
        String rpta = "";
        String sql = "update usuario set nombre=?, apellido=?, username=? where idusuario = " + _id;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(2, nombre);
            ps.setString(3, apellido);
            ps.setString(4, username);
            num = ps.executeUpdate();
            System.out.println(num);
            return rpta = "Datos del Usuario actualizados";
        } catch (Exception e) {
            System.out.println(e);
            return rpta = "No se pudo modificar los datos";
        }
    }

    public int obtenerIDUsuario(String username) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select idusuario from usuario where username='" + username + "'";
        int id = 0;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            id = 0;
            System.out.println(e);
        }
        return id;
    }

    public int insertarNota(int idUsuario, int idLibro, int nota) {
        int msg = 0;
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        String sql = "update usuarioxlibro set nota=?,evaluacion=? where idusuario="+idUsuario+" and idlibro="+idLibro;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, nota);
            ps.setBoolean(2, true);
            msg = ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return msg;
    }

}
