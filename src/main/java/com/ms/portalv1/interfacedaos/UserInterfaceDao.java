/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.portalv1.interfacedaos;

import java.util.HashMap;

/**
 *
 * @author Noe Martinez 
 * gpmarsan@gmail.com
 * 21/junio/2014
 */
public interface UserInterfaceDao {
    public HashMap<String, String> getUserData(int type, String param);
    public HashMap<String, String> getUserRol(String user);
    public String addUser(String data);
    public String callStoredProcedureAddUser(String data);
    public int countUsername(String username);
    public int countAlias(String alias);
    public int countAliasIsNotUser(String alias, String idCod);
    public int countEmail(String email);
    public int countEmailIsNotUser(String email, String idCod);
    public int countNumeroReferecia(String no_ref);
    public int countUserCode(String username, String codeverif);
    public String getUserIdEncod(String usename);
    public int countCurrentPasswordUser(String currentPasswd, String idcod);
    public HashMap<String, String> getUserDataByIdEncod(String idEncod);
    public int countUserRequest(String idCod);
    
    public int verifUsernamePasswd(String username, String passwd);
}
