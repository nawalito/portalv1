/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.springdaos;

import com.ms.common.helpers.generaMD5;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 28/junio/2014
 */
public class GralSpringDao implements GralInterfaceDao{
    private static final Logger log  = Logger.getLogger(GralSpringDao.class.getName());
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
    @Override
    public ArrayList<HashMap<String, String>> getEmailEnvio() {
        String sql_to_query = "SELECT sys_email.email,sys_email.passwd,sys_email.port,sys_email.host,sys_email.tls FROM sys_email WHERE sys_email.enabled=true and sys_email.send=true order by sys_email.id desc limit 1;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<HashMap<String, String>> hm = (ArrayList<HashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    HashMap<String, String> row = new HashMap<String, String>();
                    row.put("email",rs.getString("email"));
                    row.put("passwd",rs.getString("passwd"));
                    row.put("port",String.valueOf(rs.getString("port")));
                    row.put("host",rs.getString("host"));
                    row.put("tls",String.valueOf(rs.getBoolean("tls")));
                    return row;
                }
            }
        );
        return hm;
    }
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getEmailCopiaOculta() {
        String sql_to_query = "SELECT email FROM sys_email WHERE enabled=true and cco=true order by id desc;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("recipient",rs.getString("email"));
                    row.put("type","BCC");
                    return row;
                }
            }
        );
        return hm;
    }
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getEmailUser(String username) {
        String sql_to_query = "SELECT email FROM sys_usr where username=? and enabled=true limit 1;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{new String(username)}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("recipient",rs.getString("email"));
                    row.put("type","TO");
                    
                    return row;
                }
            }
        );
        return hm;
    }

    /*
    @Override
    public LinkedHashMap<String, String> getEmailSale() {
        String sql_to_query = "SELECT email FROM sys_email WHERE enabled=true and sale=true order by id desc limit 1;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        
        LinkedHashMap<String, String> hm = (LinkedHashMap<String, String>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                        row.put("email", String.valueOf(rs.getString("email")));
                        return row;
                    }
                }
        );
        return hm;
    }
    */
    
    @Override
    public String getEmailSale() {
        String valor_retorno="";
        if(this.getJdbcTemplate().queryForInt("SELECT count(id) FROM sys_email WHERE enabled=true and sale=true order by id desc limit 1")>0){
            Map<String, Object> update = this.getJdbcTemplate().queryForMap("SELECT email FROM sys_email WHERE enabled=true and sale=true order by id desc limit 1");
            valor_retorno = update.get("email").toString();
        }
        return valor_retorno;
    }
    
    
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getBanco(String id) {
        String sql_to_query = "SELECT title as banco, account as numero_cuenta, titular as titular_cuenta from gral_bank where id=? and enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{new String(id)}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("banco",rs.getString("banco"));
                    row.put("numero_cuenta",rs.getString("numero_cuenta"));
                    row.put("titular_cuenta",rs.getString("titular_cuenta"));
                    return row;
                }
            }
        );
        return hm;
    }
    
    @Override
    public String getEmailSupport() {
        String valor_retorno="";
        if(this.getJdbcTemplate().queryForInt("SELECT count(id) FROM sys_email WHERE enabled=true and support=true order by id desc limit 1")>0){
            Map<String, Object> update = this.getJdbcTemplate().queryForMap("SELECT email FROM sys_email WHERE enabled=true and support=true order by id desc limit 1");
            valor_retorno = update.get("email").toString();
        }
        return valor_retorno;
    }
    
}
