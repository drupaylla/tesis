/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import DAO.Conexion;
import DAO.Libro;
import DAO.Prediccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DSRS
 */
public class UsuarioxLibro {

    private int idUsuario;
    private int idLibro;
    private double nota;
    private boolean evaluacion;
    private double puntuacion;

    public UsuarioxLibro() {
    }

    public UsuarioxLibro(int idUsuario, int idLibro, double nota, boolean evaluacion, double puntaje) {
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.nota = nota;
        this.evaluacion = evaluacion;
        this.puntuacion = puntaje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public boolean isEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(boolean evaluacion) {
        this.evaluacion = evaluacion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    //EXISTE PUNTUACION?
    public boolean existePuntuacion(int idUsuario, int idLibro) {
        //System.out.println("idUsuario: "+idUsuario+" - idLibro: "+idLibro);
        boolean sw = false;
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        Double punt = 0.0;
        ResultSet rs;
        int cont = 0;
        String sql = "select * from usuarioxlibro libro where idusuario =" + idUsuario + " and idlibro =" + idLibro;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cont++;
            }
            if (cont != 0) {
                sw = true;
            } else {
                sw = false;
            }
        } catch (SQLException ex) {
        }
        return sw;
    }

    //TRAE TODO DEL USUARIO
    public List<UsuarioxLibro> todoPorUsuario(int idUsuario) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        List<UsuarioxLibro> uxl = new ArrayList<>();
        String sql = "select * from usuarioxlibro where idusuario =" + idUsuario;
        UsuarioxLibro usli;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                usli = new UsuarioxLibro(idUsuario, rs.getInt("idlibro"), rs.getDouble("nota"), rs.getBoolean("evaluacion"), rs.getDouble("puntuacion"));
                uxl.add(usli);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return uxl;
    }

    //INSERTAR UNA PUNTUACION
    public String insertarNuevaPuntuacion(int idUsuario, int idLibro, double puntuacion) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        String sql = "insert into usuarioxlibro (idusuario, idlibro, puntuacion) values (?,?,?)";
        String respuesta = "";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idLibro);
            ps.setDouble(3, puntuacion);
            ps.executeUpdate();
            respuesta = "Puntuacion registrada";
        } catch (Exception e) {
            System.out.println(e);
            respuesta = "No se pudo registrar la puntuacion";
        }
        return respuesta;
    }

    //SOPORTE. _ID DEL NUEVO REGISTRO DE PUNTUACION DE USUARIO POR LIBRO
    public int obtenerUltimoIDUsuLib() {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        int ultimo = 0;
        String sql = "select idusuario from usuarioxlibro order by idusuario desc limit 1";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ultimo = rs.getInt(1);
            if (ultimo == 0) {
                ultimo = 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return ultimo;
    }

    //ACTULIZAR PUNUTACION DE UN USUARIO A UN LIBRO (QUE YA EVALUO ANTES)
    public String actualizarPuntuacion(int idUsuario, int idLibro, double puntuacion) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps1;
        PreparedStatement ps2;
        PreparedStatement ps3;
        PreparedStatement ps4;
        ResultSet rs1;
        ResultSet rs2;
        ResultSet rs3;
        ResultSet rs4;
        String rpta = "";
        String sql1 = "select puntuacion from usuarioxlibro where idusuario =" + idUsuario
                + " and idlibro = " + idLibro;
        String sql3 = "update usuarioxlibro set puntuacion = ? where idusuario= " + idUsuario
                + " and idlibro = " + idLibro;
        String sql2 = "select vez from libro where idlibro =" + idLibro;
        String sql4 = "update libro set vez = ? where idlibro =" + idLibro;
        double puntAnt = 0;
        int vez = 0;
        try {
            ps1 = con.prepareStatement(sql1);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                puntAnt = rs1.getDouble(1);
            }

            ps2 = con.prepareStatement(sql2);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                vez = rs2.getInt(1);
            }

            double nuevaPunt = puntuacionNueva(puntAnt, puntuacion, vez);
            ps3 = con.prepareStatement(sql3);
            ps3.setDouble(1, nuevaPunt);
            rpta = "Puntuación registrada";
            ps3.executeUpdate();

            ps4 = con.prepareStatement(sql4);
            ps4.setInt(1, vez + 1);
            ps4.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            rpta = "No se pudo registrar la puntuacion";
        }
        return rpta;
    }

    //SOPORTE
    private double puntuacionNueva(double puntAnt, double puntuacion, int vez) {
        double punt = 0;
        punt = (puntAnt * (vez - 1) + puntuacion) / (vez);
        return punt;
    }

//    //VER RECOMENDACIONES POR USUARIOS
//    public List<Libro> verRocomendacionesPorUsuario(int idUsuario) {
//        Connection con = Conexion.getConnection();
//        PreparedStatement ps;
//        ResultSet rs;
//        List<Libro> librosRecomendados = new ArrayList<>();
//        Prediccion p = new Prediccion();
//        double puntaje = p.hallarPrediccion(idUsuario);
//        //System.out.println("Prediccion del Usuario: " + idUsuario + " es: " + puntaje);
//        Libro lib = new Libro();
//        if (String.valueOf(puntaje).equals("NaN")) {
//            //System.out.println("No hay puntuaciones registrados ya que es un usuario nuevo");
//            librosRecomendados = lib.obtener5Leidos();
//        } else {
//            //System.out.println("Se recomiendan los libros de mayor puntaje que el puntaje de la predicción del usuario " + idUsuario);
//            String sql = "select titulo, resumen, categoria, puntuacion, autor, url from libro where puntuacion >=" + puntaje;
//            try {
//                ps = con.prepareStatement(sql);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    lib = new Libro();
//                    lib.setTitulo(rs.getString(1));
//                    lib.setResumen(rs.getString(2));
//                    lib.setCategoria(rs.getString(3));
//                    lib.setPuntuacion(rs.getDouble(4));
//                    lib.setAutor(rs.getString(5));
//                    lib.setUrl(rs.getString(6));
//                    librosRecomendados.add(lib);
//                }
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
//        for (Libro l : librosRecomendados) {
//            System.out.println(l.getTitulo() + " - " + l.getPuntuacion());
//        }
//
//        return librosRecomendados;
//    }
}
