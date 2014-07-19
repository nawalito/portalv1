/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.interfacedaos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 28/junio/2014
 */
public interface GralInterfaceDao {
    public ArrayList<HashMap<String, String>> getEmailEnvio();
    public ArrayList<LinkedHashMap<String, String>> getEmailCopiaOculta();
    public ArrayList<LinkedHashMap<String, String>> getEmailUser(String username);
    public String getEmailSale();
    public ArrayList<LinkedHashMap<String, String>> getBanco(String id);
    public String getEmailSupport();
}
