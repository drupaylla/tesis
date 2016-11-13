/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.UsuarioxLibro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author DSRS
 */
public class Prediccion {

    UsuarioxLibro uxl = new UsuarioxLibro();
    Correlacion c = new Correlacion();
    public float promedio;
    public float desviacion;

    public List<Libro> getLibrosRecomendados(int idUsuario) {
        List<Libro> rpta = new ArrayList<>();
        List<PuntuacionxLibro> puntuacion = hallarPrediccion(idUsuario);
        System.out.println("punt.size arriba: " + puntuacion.size());
        List<Integer> co = puntuados(idUsuario);
        int sum = puntuacion.size();
        System.out.println("mis libros");
        for (int u : co) {
            System.out.println(u);
        }
        System.out.println("co.size: " + co.size());
        int i = 0;
        try {
            //de los mios
            for (int j = 0; j < co.size(); j++) {
                System.out.println("idCO: " + co.get(j));
//                //de los obtenidos arriba
                for (i = 0; i < puntuacion.size(); i++) {                    
                    System.out.println("punt: " + puntuacion.size());
                    System.out.println(co.get(j) + " - " + puntuacion.get(i).getLib());
                    System.out.println("validando: " + Objects.equals(co.get(j), puntuacion.get(i).getLib()));
                    if (Objects.equals(co.get(j), puntuacion.get(i).getLib())) {
                        System.out.println("eliminando: " + puntuacion.get(i).getLib());
                        puntuacion.remove(i);
                        //puntuacion contendra las predicciones d elibros diferenciales (osea los demas), mis libros puntuados no.
                    }
                }
            }
            rpta = listarPred(puntuacion);
            if (rpta.size() == 0) {
                rpta = listar5Primeros();
            }
        } catch (Exception e) {
            System.out.println(e);
            rpta = listar5Primeros();
        }
        System.out.println("recomendaciones");
        for (PuntuacionxLibro puntuacion1 : puntuacion) {
            System.out.println(puntuacion1.getLib());
        }
        System.out.println("rpta: " + rpta);
        return rpta;
    }

    public List<PuntuacionxLibro> hallarPrediccion(int idUsuario) {
        float prediccion = 0;
        List<UsuarioxLibro> uxr = uxl.todoPorUsuario(idUsuario);
        List<UsuarioxLibro> uxr2 = c.traerTodos();
        //la nueva lista de predicciones mias hacia libros que no haya puntuado yo y que sean mis vecinos (osea que al menos en 1 hemos coincidido).
        //OSEA SOLO MIS PUNTUACIONES. PUEDE QUE HAYA LIBROS QUE YO HAYA LEIDO PERO AUN ESTAN PORQUE OTROS USUARIOS LO HAN PUTUADO
        List<PuntuacionxLibro> pred = new ArrayList<>();
        //de la tabla cruzada, son todos menos yo. 
        promedio(idUsuario);
        List<UsuarioxLibro> idLibs = idCoincidentes(idUsuario);
        for (int i = 0; i < idLibs.size(); i++) {
            PuntuacionxLibro pxl = new PuntuacionxLibro();
            //preiccion para cada uno de esos doble filtro
            prediccion = promedio + desviacion * this.hallarNumerador(idUsuario, uxr2, idLibs.get(i).getIdLibro())
                    / this.hallarDenominador(idUsuario, uxr2);
            System.out.println("idLibro: " + idLibs.get(i).getIdLibro());
            pxl.setLib(idLibs.get(i).getIdLibro());
            pxl.setPunt(prediccion);
            pred.add(pxl);
        }
        System.out.println("pred.size(): " + pred.size());
        //lista de preicciones para cada libro que no haya sido puntuado por mi y que hayan piuntuados por mis vecinos
        return pred;
    }

    public void promedio(int id) {       
        //conexion a bd la lista de usuario x libro, sus puntaje del usuaruio
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        int cont=0;
        float sum=0;
        PreparedStatement pr;
        try {
            cn = con.getConnection();
            String sql = "SELECT puntuacion FROM usuarioxlibro where idusuario=" + id;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                sum += rs.getFloat(1);
                cont++;
            }
            promedio = sum / cont;
            System.out.println(promedio);
            rs = pr.executeQuery();
            while (rs.next()) {
                sum += (rs.getFloat(1)-promedio)*(rs.getFloat(1)-promedio);
                cont++;
            }
            desviacion = (float) Math.pow(sum / (cont), 0.5);        
            rs.close();
            pr.close();
            cn.close();
            System.out.println(desviacion);
        } catch (Exception ex) {
            System.out.println(ex);
        }               
    }

    public List<UsuarioxLibro> idCoincidentes(int idUsuario) {
        List<UsuarioxLibro> l = new ArrayList<>();
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConnection();
            //DONDE YO NO ESTOY
            String sql = "SELECT uxl.idusuario,uxl.idlibro,uxl.puntuacion FROM usuario u "
                    + "join usuarioxlibro uxl "
                    + "on u.idusuario=uxl.idusuario where uxl.idusuario not in (" + idUsuario + ")";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                if (idUsuario != rs.getInt(1)) {
                    double cor = c.correlacion(idUsuario, rs.getInt(1));
                    boolean enc = false;
                    //a traves del filtro NAN elimino los que no son mis vecinos 
                    if (String.valueOf(cor) != "NaN") {
                        for (int i = 0; i < l.size(); i++) {
                            if (l.get(i).getIdLibro() == rs.getInt(2)) {
                                enc = true;
                                break;
                            }
                        }
                        if (!enc) {
                            UsuarioxLibro uxl = new UsuarioxLibro();
                            uxl.setIdUsuario(rs.getInt("idusuario"));
                            uxl.setIdLibro(rs.getInt("idlibro"));
                            uxl.setPuntuacion(rs.getFloat("puntuacion"));
                            l.add(uxl);
                        }
                    }
                }
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    private float hallarNumerador(int idUsuario, List<UsuarioxLibro> uxr, int idLibro) {
        float sum = 0.0f;
        double s = 0.0f;
        promedio(idUsuario);
        UsuarioxLibro uxl = new UsuarioxLibro();
        List<UsuarioxLibro> uxl1 = idCoincidentes(idUsuario);
        for (int i = 0; i < uxl1.size(); i++) {
            s = ((puntuacionCruzada(idUsuario, idLibro) - promedio) / desviacion)
                    * c.correlacion(idUsuario, uxl1.get(i).getIdUsuario());
            if (String.valueOf(s).equals("NaN")) {
                s = 0;
            }
            sum += s;
        }
        return sum;
    }

    public float puntuacionCruzada(int idUsuario, int idLibro) {
        float punt = 0.0f;
        Conexion con = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = con.getConnection();
            String sql = "SELECT puntuacion FROM usuarioxlibro where idusuario=" + idUsuario + " and idlibro=" + idLibro;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                try {
                    punt = rs.getFloat("puntuacion");
                } catch (Exception e) {
                    punt = 0;
                }
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return punt;
    }

    private float hallarDenominador(int idUsuario, List<UsuarioxLibro> uxr) {
        float den = 0.0f;
        UsuarioxLibro uxl = new UsuarioxLibro();
        List<UsuarioxLibro> uxl1 = new ArrayList<>();
        for (int i = 0; i < uxr.size(); i++) {
            uxl1.add(c.idUsuarios(idUsuario, uxr.get(i).getIdUsuario()));
        }
        for (int i = 0; i < uxl1.size(); i++) {
            try {
                den += c.correlacion(idUsuario, uxl1.get(i).getIdUsuario());
            } catch (Exception e) {
                den += 0;
            }
        }
        return den;
    }

    //TRAIGO MIS PUNTUADOS
    public List<Integer> puntuados(int idUsuario) {
        List<Integer> l = new ArrayList<>();
        Conexion c = new Conexion();
        Connection cn;
        ResultSet rs;
        PreparedStatement pr;
        try {
            cn = c.getConnection();
            String sql = "SELECT * FROM usuarioxlibro where idusuario=" + idUsuario;
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                l.add(rs.getInt("idlibro"));
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return l;
    }

    //detalle del id libro almacenado en la lista
    public List<Libro> listarPred(List<PuntuacionxLibro> l) {
        List<Libro> librosRecomendados = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            Connection cn;
            Conexion con = new Conexion();
            ResultSet rs;
            PreparedStatement pr;
            try {
                cn = con.getConnection();
                String sql = "";
                sql = "SELECT * FROM libro where idlibro=" + l.get(i).getLib();
                pr = cn.prepareStatement(sql);
                rs = pr.executeQuery();
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));
                    lib.setPuntuacion(rs.getFloat("puntuacion"));
                    librosRecomendados.add(lib);
                }
                rs.close();
                pr.close();
                cn.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return librosRecomendados;
    }

    //segun gustos? o segun puntuaciones?
    public List<Libro> listar5Primeros() {
        Connection cn;
        Conexion con = new Conexion();
        ResultSet rs;
        PreparedStatement pr;
        List<Libro> librosRecomendados5 = new ArrayList<>();
        try {
            cn = con.getConnection();
            String sql = "";
            sql = "SELECT * FROM libro order by puntuacion desc limit 5";
            pr = cn.prepareStatement(sql);
            rs = pr.executeQuery();
            while (rs.next()) {
                Libro lib = new Libro();
                lib.setTitulo(rs.getString("titulo"));
                lib.setCategoria(rs.getString("categoria"));
                lib.setPuntuacion(rs.getDouble("puntuacion"));
                librosRecomendados5.add(lib);
            }
            rs.close();
            pr.close();
            cn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return librosRecomendados5;
    }

}
