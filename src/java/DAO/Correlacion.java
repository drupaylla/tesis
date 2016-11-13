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

/**
 *
 * @author DSRS
 */
public class Correlacion {

     public double correlacion(int id1, int id2) {
        double correlacion = 0.0;
        UsuarioxLibro uxl = new UsuarioxLibro();
        List<UsuarioxLibro> uxl1 = uxl.todoPorUsuario(id1);
        List<UsuarioxLibro> uxl2 = uxl.todoPorUsuario(id2);
        List<Integer> co = this.coincidencia(uxl1, uxl2);
        float prom1 = this.promedioLista(uxl1);
        float prom2 = this.promedioLista(uxl2);
        float numerador = this.hallarNumerador(prom1, prom2, uxl1, uxl2, co);
        float denominador = this.hallarDenominador(prom1, prom2, co, uxl1, uxl2);
        correlacion = numerador / denominador;
        return correlacion;
    }
     
    public List<Integer> coincidencia(List<UsuarioxLibro> lista1,List<UsuarioxLibro> lista2) {
        List<Integer> co = new ArrayList<>();
        for (int i = 0; i < lista1.size(); i++) {
            if (this.buscar(lista1.get(i).getIdLibro(), lista2)) {
                co.add(lista1.get(i).getIdLibro());
            }
        }
        return co;
    }

    public boolean buscar(int idLib, List<UsuarioxLibro> lista2) {
        boolean enc = false;
        int i = 0;
        while (i < lista2.size() && !enc) {
            if (lista2.get(i).getIdLibro()== idLib) {
                enc = true;
            }
            i++;
        }
        return enc;
    }
    
    public float promedioLista(List<UsuarioxLibro> lista) {
        float suma = 0.0f;
        int cont = 0;
        float prom;
        for (int i = 0; i < lista.size(); i++) {
            suma += lista.get(i).getPuntuacion();
            cont++;
        }
        prom = suma / cont;
        if(String.valueOf(prom).equals("NaN")){
            prom=0;
        }
        return prom;
    }
    
    private float hallarNumerador(float prom1, float prom2,List<UsuarioxLibro> lista1, List<UsuarioxLibro> lista2, 
            List<Integer> co) {
        float num = 0.0f;
        double n1=0.0f;
        double n2=0.0f;
        for (int i = 0; i < co.size(); i++) {
            n1=this.buscarPunt(co.get(i), lista1) - prom1;
            n2=this.buscarPunt(co.get(i), lista2) - prom2;
            if(n1==0){
                n1=1;
            }
            if(n2==0){
                n2=1;
            }
            num += (n1)*(n2);            
        }        
        return num;
    }
    
    public double buscarPunt(int idLib, List<UsuarioxLibro> lista) {
        double punt = 0.0f;
        boolean enc = false;
        for (int i = 0; i < lista.size(); i++) {
            if (idLib == lista.get(i).getIdLibro()) {
                punt = (float) lista.get(i).getPuntuacion();
            }
        }
        return punt;
    }    
    
    private float hallarDenominador(float prom1, float prom2,List<Integer> co, List<UsuarioxLibro> lista1,
            List<UsuarioxLibro> lista2) {
        float deno = 0.0f;
        deno = (float) Math.pow(this.sumCuadrado(prom1, co, lista1,lista2)
                * this.sumCuadrado(prom2, co,lista1, lista2), 0.5);
        return deno;
    }

    private float sumCuadrado(float prom, List<Integer> co, List<UsuarioxLibro> lista, List<UsuarioxLibro> lista2) {
        float sum = 0.0f;
        double n1=0.0f;
        double n2=0.0f;        
        for (int i = 0; i < co.size(); i++) {
            n1=this.buscarPunt(co.get(i), lista) - prom;
            n2=this.buscarPunt(co.get(i), lista2) - prom;
            if(n1==0){
                n1=1;
            }
            if(n2==0){
                n2=1;
            }
            sum += n1* n2;    
        }        
        return sum;
    }
    
    //TRAE TODA TABLA CRUZADA
    public List<UsuarioxLibro> traerTodos() {
        List<UsuarioxLibro> l = new ArrayList<>();
        UsuarioxLibro uxl;
        Conexion c = new Conexion();
        try {
            Connection con = c.getConnection();
            String sql = "SELECT * FROM usuarioxlibro";
            PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                uxl = new UsuarioxLibro();
                uxl.setIdUsuario(rs.getInt("idusuario"));
                uxl.setPuntuacion(rs.getFloat("puntuacion"));
                uxl.setIdLibro(rs.getInt("idlibro"));
                l.add(uxl);
            }
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);        
        }
        return l;
    }
    
    public UsuarioxLibro idUsuarios(int id1, int id2) {
        UsuarioxLibro uxl = new UsuarioxLibro();
        UsuarioxLibro uxl3 = null;
        List<UsuarioxLibro> uxl1 = uxl.todoPorUsuario(id1);
        List<UsuarioxLibro> uxl2 = uxl.todoPorUsuario(id2);
        List<Integer> co = coincidencia(uxl1, uxl2);
        Conexion c = new Conexion();
        for (int coin : co) {
            try {
                Connection con = c.getConnection();
                String strsql = "SELECT * FROM usuarioxlibro where idlibro=" + coin;
                PreparedStatement pstm = con.prepareStatement(strsql);
                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    uxl3 = new UsuarioxLibro();
                    uxl3.setIdUsuario(rs.getInt("idusuario"));
                    uxl3.setPuntuacion(rs.getFloat("puntuacion"));
                    uxl3.setIdLibro(rs.getInt("idlibro"));
                }
                rs.close();
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return uxl3;
    }     
}   