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
import static javafx.scene.input.KeyCode.A;

/**
 *
 * @author DSRS
 */
public class Libro {

    private int idLibro;
    private String titulo;
    private String autor;
    private String categoria;
    private String resumen;
    private double puntuacion;
    private String url;
    private int vez;
    private List<String> pregunta;
    private List<String> respuesta;

    public Libro() {
    }

    public Libro(int idLibro, String titulo, String autor, String categoria, String resumen, double puntuacion, String url, int vez, List<String> pregunta, List<String> respuesta) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.resumen = resumen;
        this.puntuacion = puntuacion;
        this.url = url;
        this.vez = vez;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVez() {
        return vez;
    }

    public void setVez(int vez) {
        this.vez = vez;
    }

    public List<String> getPregunta() {
        return pregunta;
    }

    public void setPregunta(List<String> pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(List<String> respuesta) {
        this.respuesta = respuesta;
    }

    //ACTUALIZAR TIEMPO INICIO DE PDF
    public String tiempoInicio(int idUsuario, int idLibro, int tiempoI) {
        int n = 0;
        String rpta = "";
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "insert into tiempo (inicio,idusuario,idlibro) values (?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, tiempoI);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idLibro);
            ps.executeUpdate();
            rpta = "Hora inicio actualizado";
        } catch (Exception e) {
            System.out.println(e);
        }
        return rpta;
    }

    //ACTUALIZAR TIEMPO FIN DE PDF
    public String tiempoFin(int idUsuario, int idLibro, int tiempoF) {
        int n = 0;
        String rpta = "";
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "update tiempo set fin =" + tiempoF + " where idusuario =" + idUsuario + " and idlibro =" + idLibro;
        try {
            ps = con.prepareStatement(sql);
            n = ps.executeUpdate();
            rpta = "Hora fin actualizado";
        } catch (Exception e) {
            System.out.println(e);
        }
        return rpta;
    }

    //CALCULAR TIEMPO DIFERENCIADO
    public String calcularTiempoLectura() {
        int n = 0;
        int dif = 0;
        String rpta = "";
        Connection con = Conexion.getConnection();
        PreparedStatement ps1;
        PreparedStatement ps2;
        ResultSet rs1;
        ResultSet rs2;
        String sql1 = "select (fin - inicio) as diferencia from tiempo";
        String sql2 = "update tiempo set diferencia =?";
        try {
            ps1 = con.prepareStatement(sql1);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                dif=rs1.getInt("diferencia");
            }
            ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, dif);
            n = ps2.executeUpdate();
            rpta = "Tiempo calculado" + dif;
        } catch (Exception e) {
            System.out.println(e);
        }
        return rpta;
    }

    //OBTENER 5 PRIMEROS LIBROS
    public List<Libro> obtener5Leidos() {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from libro order by puntuacion desc limit 5";
        List<Libro> titulos5 = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lib = new Libro();
                lib.setIdLibro(rs.getInt("idlibro"));
                lib.setPuntuacion(rs.getDouble("puntuacion"));
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setAutor(rs.getString("autor"));
                lib.setUrl(rs.getString("url"));
                titulos5.add(lib);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return titulos5;
    }

    //OBTENER MAS DETALLE DE UN LIBRO SEGUN SU ID
    public Libro obtenerDetalleLibro(int idLibro) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        Libro rpta = new Libro();
        String sql = "select * from libro where idlibro = " + idLibro;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                rpta.setTitulo(rs.getString("titulo"));
                rpta.setAutor(rs.getString("autor"));
                rpta.setCategoria(rs.getString("categoria"));
                rpta.setResumen(rs.getString("resumen"));
                rpta.setPuntuacion(rs.getInt("puntuacion"));
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rpta;
    }

    //BUSCAR LIBROS POR BUSQUEDA 
    public List<Libro> buscar(String titulo, String autor, String categoria) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select titulo, categoria from libro where titulo = ' " + titulo + " ' , autor = ' " + autor
                + " ' , categoria = ' " + categoria + " '";
        List<Libro> libros = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                lib = new Libro();
                lib.setTitulo(rs.getString(1));
                lib.setCategoria(rs.getString(2));
                libros.add(lib);
                if (libros == null) {
                    System.out.println("No hay libros con esta busqueda. Intenta con otros filtros");
                }
            }
        } catch (Exception e) {
            System.out.println("try total: " + e);
        }
        return libros;
    }

    //LISTAR TODOS LOS LIBROS
    public List<Libro> obtenerTodosTitulosLibros() {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from libro";
        List<Libro> titulos = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lib = new Libro();
                lib.setIdLibro(rs.getInt("idlibro"));
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setAutor(rs.getString("autor"));
                lib.setUrl(rs.getString("url"));

                titulos.add(lib);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return titulos;
    }
    
    public List<Libro> searchTitulo(String titulo) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from libro where titulo like '%"+titulo+"%'";
        List<Libro> titulos = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lib = new Libro();
                lib.setIdLibro(rs.getInt("idlibro"));
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setAutor(rs.getString("autor"));
                lib.setUrl(rs.getString("url"));

                titulos.add(lib);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return titulos;
    }
    
    public List<Libro> searchCategoria(String categoria) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from libro where categoria like '%"+categoria+"%'";
        List<Libro> titulos = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lib = new Libro();
                lib.setIdLibro(rs.getInt("idlibro"));
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setAutor(rs.getString("autor"));
                lib.setUrl(rs.getString("url"));

                titulos.add(lib);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return titulos;
    }
    
    public List<Libro> searchAutor(String autor) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from libro where autor like '%"+autor+"%'";
        List<Libro> titulos = new ArrayList<Libro>();
        Libro lib;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lib = new Libro();
                lib.setIdLibro(rs.getInt("idlibro"));
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setAutor(rs.getString("autor"));
                lib.setUrl(rs.getString("url"));

                titulos.add(lib);
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return titulos;
    }

    public List<Pregunta> listarPreguntas(int idLibro) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "select * from pregunta where idlibro = " + idLibro;
        List<Pregunta> preguntas = new ArrayList<Pregunta>();
        Pregunta p;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                p = new Pregunta();
                p.setIdPregunta(rs.getInt("idpregunta"));
                p.setDescripcion(rs.getString("descripcion"));
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return preguntas;
    }

    //EVALUAR EVALUACION
    public boolean evaluar(int idpregunta, String rpta) {
        Connection con = Conexion.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        boolean sw = false;
        String sql = "select descripcion from respuesta where idpregunta =" + idpregunta;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(rpta)) {
                    sw = true;
                } else {
                    sw = false;
                }
                i++;
            }
            ps.close();
            rs.close();
            con.close();
        } catch (Exception e) {
            sw = false;
            System.out.println(e);
        }
        return sw;
    }

}
