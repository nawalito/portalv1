/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.models;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 13/julio/2014
 */
public class ServiceRequestRenew {
    String ident;
    String folio;
    String pais;
    String codeArea;
    String phone;
    String typeLicence;
    String totalMessage;
    String frecuencyMessage;
    public String getTypeLicence() {
        return typeLicence;
    }

    public void setTypeLicence(String typeLicence) {
        this.typeLicence = typeLicence;
    }

    public String getTotalMessage() {
        return totalMessage;
    }

    public void setTotalMessage(String totalMessage) {
        this.totalMessage = totalMessage;
    }

    public String getFrecuencyMessage() {
        return frecuencyMessage;
    }

    public void setFrecuencyMessage(String frecuencyMessage) {
        this.frecuencyMessage = frecuencyMessage;
    }
    
    
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    
    public String getIdent() {
        return ident;
    }
    
    public void setIdent(String ident) {
        this.ident = ident;
    }
    
    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(String codeArea) {
        this.codeArea = codeArea;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
