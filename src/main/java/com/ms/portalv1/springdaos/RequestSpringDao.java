/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.springdaos;

import com.ms.common.helpers.StringHelper;
import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 02/julio/2014
 */
public class RequestSpringDao implements RequestInterfaceDao{
    private static final Logger log = Logger.getLogger(RequestSpringDao.class.getName());
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    
    @Override
    public Map<String, Object> callStoredProcedureAdmServiceRequest(String data) {
        String element[];
        element = data.split("___");
        Map<String, Object> simpleJdbcCallResult=null;
        try{
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.getJdbcTemplate());
        simpleJdbcCall.withProcedureName("user_request_adm_procesos");
        Map<String, Object> inParamMap = new HashMap<>();
        
        inParamMap.put("command_", element[0]);
        inParamMap.put("id_", element[1]);
        inParamMap.put("folio_", element[2]);
        inParamMap.put("noref_", element[3]);
        inParamMap.put("coutrycode_", element[4]);
        inParamMap.put("codearea_", element[5]);
        inParamMap.put("phone_", element[6]);
        inParamMap.put("typelicense_", element[7]);
        inParamMap.put("totalmsj_", element[8]);
        inParamMap.put("frecuencymsj_", element[9]);
        inParamMap.put("success_", "0");
        
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        
         simpleJdbcCallResult= simpleJdbcCall.execute(in);
        
        System.out.println("VALORES RETORNADOS POR EL PROCEDIMIENTO: ");
        System.out.println("success_: "+String.valueOf(simpleJdbcCallResult.get("success_")));
        System.out.println("folio_: "+String.valueOf(simpleJdbcCallResult.get("folio_")));
        System.out.println("noref_: "+String.valueOf(simpleJdbcCallResult.get("noref_")));
        
        return simpleJdbcCallResult;
        
        }catch(Exception excepcion){
            log.log(Level.INFO, "Ejecutando query de {0}", excepcion.getMessage());
            return simpleJdbcCallResult;
        }
    }
    
    
    
    @Override
    public Map<String, Object> callStoredProcedurActionsRequest(String command, String userid, String folio) {
        
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.getJdbcTemplate());
        simpleJdbcCall.withProcedureName("user_actions_request");
        Map<String, Object> inParamMap = new HashMap<>();
        System.out.println("command:"+command+"|userid:"+userid+"|folio:"+folio);
        inParamMap.put("command_", command);
        inParamMap.put("userid_", userid);
        inParamMap.put("folio_", folio);
        inParamMap.put("success_", "0");
        
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
        
        //System.out.println("VALORES RETORNADOS POR EL PROCEDIMIENTO: ");
        //System.out.println("success_: "+String.valueOf(simpleJdbcCallResult.get("success_")));
        
        return simpleJdbcCallResult;
    }
    
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getPaises() {
        String sql_to_query = "SELECT id, code, concat('+',code,' ',title) as pais FROM gral_country where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("id",String.valueOf(rs.getInt("id")));
                    row.put("code",rs.getString("code"));
                    row.put("pais",rs.getString("pais"));
                    return row;
                }
            }
        );
        return hm;
    }
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getTiposSolicitud(boolean incluir_prueba) {
        
        String sql_to_query = "";
        if(incluir_prueba){
            sql_to_query = "select id, title from usr_type_request where enabled=true;";
        }else{
            sql_to_query = "select id, title from usr_type_request where enabled=true and lower(class)<>'free';";
        }
        
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("id",String.valueOf(rs.getInt("id")));
                    row.put("title",rs.getString("title"));
                    return row;
                }
            }
        );
        return hm;
    }
    
    
    
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getFrecuenciaMsjs() {
        String sql_to_query = "select id, title from usr_frecuency_msj where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("id",String.valueOf(rs.getInt("id")));
                    row.put("title",rs.getString("title"));
                    return row;
                }
            }
        );
        return hm;
    }
    
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getCantidadMsjs() {
        String sql_to_query = "select quantity from usr_total_msj where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("quantity",String.valueOf(rs.getInt("quantity")));
                    return row;
                }
            }
        );
        return hm;
    }
    
    
    
    
    //Obtiene lista de paises y los agrega en un HashMap
    @Override
    public LinkedHashMap<String, String> getCountrys() {
        String retorno = "";
        String sql_to_query = "SELECT id, code, concat('+',code,' ',title) as title FROM gral_country where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> arrayhm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("code",String.valueOf(rs.getString("code")));
                    row.put("title",String.valueOf(rs.getString("title")));
                    return row;
                }
            }
        );
        
        LinkedHashMap<String, String> hm = new LinkedHashMap<>();
        for( LinkedHashMap<String,String> i : arrayhm ){
           hm.put(i.get("code"), i.get("title"));
        }
        return hm;
    }
    
    
    
    
    //Obtiene lista de Tipos de Licencia y los agrega en un HashMap
    @Override
    public LinkedHashMap<String, String> getTypeLicence() {
        String retorno = "";
        String sql_to_query = "select id, title from usr_type_request where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> arrayhm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("id",String.valueOf(rs.getInt("id")));
                    row.put("title",rs.getString("title"));
                    return row;
                }
            }
        );
        
        LinkedHashMap<String, String> hm = new LinkedHashMap<>();
        for( LinkedHashMap<String,String> i : arrayhm ){
           hm.put(i.get("id"), i.get("title"));
        }
        return hm;
    }
    
    
    //Obtiene lista de Frecuencia de Mensajes y los agrega en un HashMap
    @Override
    public LinkedHashMap<String, String> getFrecuencyNotification() {
        String retorno = "";
        String sql_to_query = "select id, title from usr_frecuency_msj where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> arrayhm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("id",String.valueOf(rs.getInt("id")));
                    row.put("title",rs.getString("title"));
                    return row;
                }
            }
        );
        
        LinkedHashMap<String, String> hm = new LinkedHashMap<>();
        for( LinkedHashMap<String,String> i : arrayhm ){
           hm.put(i.get("id"), i.get("title"));
        }
        return hm;
    }
    
    
    //Obtiene lista de diferentes cantidades de Mensajes y los agrega en un HashMap
    @Override
    public LinkedHashMap<String, String> getTotalMessage() {
        String retorno = "";
        String sql_to_query = "select quantity from usr_total_msj where enabled=true;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> arrayhm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("quantity",String.valueOf(rs.getInt("quantity")));
                    return row;
                }
            }
        );
        
        LinkedHashMap<String, String> hm = new LinkedHashMap<>();
        for( LinkedHashMap<String,String> i : arrayhm ){
           hm.put(i.get("quantity"), i.get("quantity"));
        }
        return hm;
    }
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getDataServiceRequest(String folio, String idUserCod) {
        String sql_to_query = ""
        + "select "
            + "gral_country.title as pais,"
            + "concat('+',gral_country.code) as codigo_pais,"
            + "usr_request.area_code as codigo_area,"
            + "usr_request.phone as telefono,"
            + "usr_request.folio,"
            + "usr_request.ref_number as numero_referencia,"
            + "usr_type_request.title as tipo_solicitud,"
            + "usr_type_request.amount as precio_paquete,"
            + "usr_type_request.class as clase_paquete,"
            + "usr_frecuency_msj.title as frecuencia_msj,"
            + "(CASE WHEN usr_request.create is null THEN '' ELSE DATE(usr_request.create) END) AS fecha_solicitud,"
            + "usr_request.total_msj,"
            + "(CASE WHEN usr_contract.contract_date is null THEN '' ELSE DATE(usr_contract.contract_date) END) AS fcontrato,"
            + "(CASE WHEN usr_contract.expiration_date is null THEN '' ELSE DATE(usr_contract.expiration_date) END) AS fvencimiento,"
            + "(CASE usr_request.estatus WHEN 0 then 'Nuevo' WHEN 1 then 'Activo' WHEN 2 then 'Suspendido' WHEN 3 then 'Vencido' WHEN 4 then 'Cancelado' WHEN 5 then 'En proceso de Activacion' END) AS estatus,"
            + "usr_request.estatus AS status_code "
        + "from usr_request "
        + "join sys_usr on sys_usr.id=usr_request.sys_usr_id "
        + "join gral_country on gral_country.code=usr_request.coutry_code "
        + "join usr_type_request on usr_type_request.id=usr_request.usr_type_request_id "
        + "join usr_frecuency_msj on usr_frecuency_msj.id=usr_request.usr_frecuency_msj_id "
        + "left join usr_contract on usr_contract.request_id=usr_request.id "
        + "where usr_request.folio=? and sys_usr.encod=? limit 1;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{new String(folio), new String(idUserCod)}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<String, String>();
                    row.put("pais",rs.getString("pais"));
                    row.put("codigo_pais",rs.getString("codigo_pais"));
                    row.put("codigo_area",rs.getString("codigo_area"));
                    row.put("telefono",rs.getString("telefono"));
                    row.put("folio",rs.getString("folio"));
                    row.put("numero_referencia",rs.getString("numero_referencia"));
                    row.put("tipo_solicitud",rs.getString("tipo_solicitud"));
                    row.put("frecuencia_msj",rs.getString("frecuencia_msj"));
                    row.put("fecha_solicitud",String.valueOf(rs.getDate("fecha_solicitud")));
                    row.put("total_msj",String.valueOf(rs.getInt("total_msj")));
                    row.put("precio_paquete",StringHelper.roundDouble(rs.getDouble("precio_paquete"),2));
                    row.put("clase_paquete",String.valueOf(rs.getString("clase_paquete")));
                    row.put("fcontrato",String.valueOf(rs.getString("fcontrato")));
                    row.put("fvencimiento",String.valueOf(rs.getString("fvencimiento")));
                    row.put("estatus",rs.getString("estatus"));
                    row.put("status_code",String.valueOf(rs.getInt("status_code")));
                    return row;
                }
            }
        );
        return hm;
    }
    
    @Override
    public ArrayList<LinkedHashMap<String, String>> getFoliosUser(String idUserCod) {
        String sql_to_query = ""
        + "select "
            + "usr_request.folio,"
            + "(CASE usr_request.estatus WHEN 0 then 'Nuevo' WHEN 1 then 'Activo' WHEN 2 then 'Suspendido' WHEN 3 then 'Vencido' WHEN 4 then 'Cancelado' WHEN 5 then 'En proceso de Activacion' END) AS estatus "
        + "from usr_request "
        + "join sys_usr on sys_usr.id=usr_request.sys_usr_id "
        + "where sys_usr.encod=?;";
        
        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);
        ArrayList<LinkedHashMap<String, String>> hm = (ArrayList<LinkedHashMap<String, String>>) this.getJdbcTemplate().query(
            sql_to_query,
            new Object[]{new String(idUserCod)}, new RowMapper(){
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    LinkedHashMap<String, String> row = new LinkedHashMap<>();
                    row.put("folio",rs.getString("folio"));
                    row.put("estatus",rs.getString("estatus"));
                    return row;
                }
            }
        );
        return hm;
        
    }

    @Override
    public HashMap<String, Object> getDataRenew(String folio, String idUser) {
        String sql_to_query = ""
                + "select "
                    + "coutry_code, "
                    + "area_code, "
                    + "phone, "
                    + "usr_type_request_id as type_request_id, "
                    + "usr_frecuency_msj_id as frecuency_msj_id,"
                    + "total_msj "
                + "from usr_request "
                + "where folio=? and sys_usr_id=? LIMIT 1;";
        
        System.out.println(sql_to_query);
        
        HashMap<String, Object> hm = (HashMap<String, Object>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{new String(folio), new String(idUser)}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        HashMap<String, Object> row = new HashMap<String, Object>();
                        row.put("coutry_code", rs.getString("coutry_code"));
                        row.put("area_code", rs.getString("area_code"));
                        row.put("phone", rs.getString("phone"));
                        row.put("type_request_id", rs.getString("type_request_id"));
                        row.put("frecuency_msj_id", rs.getString("frecuency_msj_id"));
                        row.put("total_msj", rs.getString("total_msj"));
                        return row;
                    }
                }
        );
        
        return hm;
    }
    
}
