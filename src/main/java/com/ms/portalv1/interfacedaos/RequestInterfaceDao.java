/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.interfacedaos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 02/julio/2014
 */
public interface RequestInterfaceDao {
    public Map<String, Object> callStoredProcedureAdmServiceRequest(String data);
    public Map<String, Object> callStoredProcedurActionsRequest(String command, String userid, String folio);
    public int getVerifyLicenseClass(String id);
    public int countUserRequest(String idCod);
    public int getCountPhone(String phone, String id_user);
    public ArrayList<LinkedHashMap<String, String>> getPaises();
    public ArrayList<LinkedHashMap<String, String>> getTiposSolicitud(boolean incluir_prueba);
    public ArrayList<LinkedHashMap<String, String>> getFrecuenciaMsjs();
    public ArrayList<LinkedHashMap<String, String>> getCantidadMsjs();
    
    public LinkedHashMap<String, String> getCountrys();
    public LinkedHashMap<String, String> getTypeLicence(boolean incluir_prueba);
    public LinkedHashMap<String, String> getFrecuencyNotification();
    public LinkedHashMap<String, String> getTotalMessage();
    
    public ArrayList<LinkedHashMap<String, String>> getDataServiceRequest(String folio, String idUserCod);
    public ArrayList<LinkedHashMap<String, String>> getFoliosUser(String idUserCod);
    
    public HashMap<String, Object> getDataRenew(String folio, String idUserCod);
}
