/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.portalv1.springdaos;

import com.ms.common.helpers.generaMD5;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
 * 21/junio/2014
 */
public class UserSpringDao implements UserInterfaceDao {

    private static final Logger log = Logger.getLogger(UserSpringDao.class.getName());
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    
    //Este metodo es para obtener datos directamente de la vista de usuarios
    @Override
    public HashMap<String, String> getUserData(int type, String param) {
        String cadenaWhere = new String();

        if (type == 1) {
            //Busqueda por UserName
            cadenaWhere = "WHERE upper(username)='" + param.toUpperCase() + "'";
        }

        if (type == 2) {
            //Busqueda por ID de usuario
            cadenaWhere = "WHERE id=" + param + "";
        }

        if (type == 3) {
            //Busqueda por ID_CODIFICADO de usuario
            cadenaWhere = "WHERE upper(encod)='" + param.toUpperCase() + "'";
        }

        String sql_to_query = ""
                + "SELECT * FROM ("
                + "SELECT id,encod AS encod_id,username,status FROM sys_adm "
                + "UNION "
                + "SELECT id,encod AS encod_id,username,status FROM sys_usr"
                + ") AS sbt "
                + "" + cadenaWhere + ";";

        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);

        HashMap<String, String> hm = (HashMap<String, String>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        HashMap<String, String> row = new HashMap<String, String>();
                        row.put("id", String.valueOf(rs.getInt("id")));
                        row.put("encod_id", rs.getString("encod_id"));
                        row.put("username", rs.getString("username"));
                        row.put("status", rs.getString("status"));
                        return row;
                    }
                }
        );

        //Actualiza ultimo acceso
        //this.getJdbcTemplate().execute("UPDATE gral_usr SET fecha_acceso=now() "+cadenaWhere+";");
        return hm;
    }

    @Override
    public HashMap<String, String> getUserRol(String user) {
        String sql_to_query = "SELECT authority FROM authorities WHERE username=?;";

        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);

        HashMap<String, String> hm = (HashMap<String, String>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{new String(user)}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        HashMap<String, String> row = new HashMap<String, String>();
                        row.put("rol", String.valueOf(rs.getString("authority")));
                        return row;
                    }
                }
        );
        return hm;
    }

    @Override
    public String addUser(String data) {
        String element[];
        element = data.split("___");
        /*
         System.out.println("element[0]: "+element[0]);
         System.out.println("element[1]: "+element[1]);
         System.out.println("element[2]: "+element[2]);
         System.out.println("element[3]: "+element[3]);
         System.out.println("element[4]: "+element[4]);
         System.out.println("element[5]: "+element[5]);
         System.out.println("element[6]: "+element[6]);
         System.out.println("element[7]: "+element[7]);
         */
        String sql_insert = "CALL user_adm_procesos('" + element[0] + "','" + element[1] + "','" + element[4] + "','" + element[5] + "','" + element[2] + "','" + element[3] + "','" + element[7] + "'," + element[6] + ",0);";

        System.out.println("Call procedure: " + sql_insert);

        this.getJdbcTemplate().execute(sql_insert);

        return "1";
    }

    @Override
    public String callStoredProcedureAddUser(String data) {
        String element[];
        element = data.split("___");
        
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.getJdbcTemplate());
        simpleJdbcCall.withProcedureName("user_adm_procesos");
        Map<String, Object> inParamMap = new HashMap<String, Object>();
        
        inParamMap.put("command_", element[0]);
        inParamMap.put("id_", element[1]);
        inParamMap.put("username_", element[4]);
        inParamMap.put("password_", element[5]);
        inParamMap.put("email_", element[2]);
        inParamMap.put("alias_", element[3]);
        inParamMap.put("codev_", element[7]);
        inParamMap.put("noref_", element[6]);
        inParamMap.put("success_", "0");
        
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
        
        System.out.println(String.valueOf(simpleJdbcCallResult.get("success_")));
        
        return String.valueOf(simpleJdbcCallResult.get("success_"));
    }
    
    @Override
    public int countUsername(String username) {
        return this.getJdbcTemplate().queryForInt("select count(username) from users where lower(username)='" + username.toLowerCase() + "';");
    }

    @Override
    public int countAlias(String alias) {
        return this.getJdbcTemplate().queryForInt("select count(alias) from sys_usr where lower(alias)='" + alias.toLowerCase() + "';");
    }
    
    @Override
    public int countAliasIsNotUser(String alias, String idCod) {
        return this.getJdbcTemplate().queryForInt("select count(alias) from sys_usr where lower(alias)='" + alias.toLowerCase() + "' and encod!='" + idCod.trim()+"';");
    }

    @Override
    public int countEmail(String email) {
        return this.getJdbcTemplate().queryForInt("select count(email) from sys_usr where lower(email)='" + email.toLowerCase() + "';");
    }
    
    @Override
    public int countEmailIsNotUser(String email, String idCod) {
        return this.getJdbcTemplate().queryForInt("select count(email) from sys_usr where lower(email)='" + email.toLowerCase() + "' and encod!='" + idCod.trim()+"'");
    }
    
    @Override
    public int countNumeroReferecia(String no_ref) {
        return this.getJdbcTemplate().queryForInt("select count(noref) from sys_usr where noref=" + no_ref + ";");
    }
    
    @Override
    public int countUserCode(String idcod, String codeverif) {
        return this.getJdbcTemplate().queryForInt("select count(id) from sys_usr where encod='" + idcod + "' and codev='" + codeverif + "';");
    }
    
    @Override
    public int countCurrentPasswordUser(String currentPasswd, String idCod) {
        return this.getJdbcTemplate().queryForInt("select count(id) from sys_usr where encod='" + idCod.trim() + "' and password='" + generaMD5.MD5(currentPasswd) + "';");
    }
    
    @Override
    public String getUserIdEncod(String usename) {
        String retorno = "";
        String sql_to_query = "SELECT encod FROM sys_usr WHERE username=? LIMIT 1;";

        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);

        HashMap<String, String> hm = (HashMap<String, String>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{new String(usename)}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        HashMap<String, String> row = new HashMap<String, String>();
                        row.put("user_encod", String.valueOf(rs.getString("encod")));
                        return row;
                    }
                }
        );

        if (!hm.isEmpty()) {
            retorno = hm.get("user_encod");
        }

        return retorno;
    }
    
    
    
    @Override
    public HashMap<String, String> getUserDataByIdEncod(String idEncod) {
        String retorno = "";
        String sql_to_query = "SELECT username, email, alias, noref FROM sys_usr WHERE encod=? LIMIT 1;";

        log.log(Level.INFO, "Ejecutando query de {0}", sql_to_query);

        HashMap<String, String> hm = (HashMap<String, String>) this.jdbcTemplate.queryForObject(
                sql_to_query,
                new Object[]{new String(idEncod)}, new RowMapper() {
                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        HashMap<String, String> row = new HashMap<String, String>();
                        row.put("usrname", String.valueOf(rs.getString("username")));
                        row.put("email", String.valueOf(rs.getString("email")));
                        row.put("alias", String.valueOf(rs.getString("alias")));
                        row.put("noref", String.valueOf(rs.getString("noref")));
                        return row;
                    }
                }
        );
        
        return hm;
    }
    
    @Override
    public int countUserRequest(String idCod) {
        return this.getJdbcTemplate().queryForInt("select count(usr_request.id) from sys_usr join usr_request on usr_request.sys_usr_id=sys_usr.id where sys_usr.encod='" + idCod + "';");
    }
    
    @Override
    public int verifUsernamePasswd(String username, String passwd) {
        return this.getJdbcTemplate().queryForInt("select count(id) from sys_usr where username='" + username.trim() + "' and password='" + generaMD5.MD5(passwd) + "';");
    }
    
    
}
