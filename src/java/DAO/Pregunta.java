/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DSRS
 */
public class Pregunta {

    private int idPregunta;
    private String descripcion;

    public Pregunta() {
    }

    public Pregunta(int idPregunta, String descripcion) {
        this.idPregunta = idPregunta;
        this.descripcion = descripcion;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> mostrarOpciones(int idPregunta) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select descripcion from opcion where idpregunta=" + idPregunta;
        List<String> opciones = new ArrayList<>();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String opcion = rs.getString("descripcion");
                opciones.add(opcion);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("No hay conexion");
        }
        return opciones;
    }

    //LISTAR PREGUNTAS DEL LIBRO
    public List<Pregunta> obtenerPreguntas(int idLibro) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select idpregunta,descripcion from pregunta where idLibro = " + idLibro;
        List<Pregunta> preguntas = new ArrayList<Pregunta>();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Pregunta p = new Pregunta();
                p.setDescripcion(rs.getString(2));
                p.setIdPregunta(rs.getInt(1));
                preguntas.add(p);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return preguntas;
    }

}
