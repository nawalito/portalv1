/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.common.cofig;

import com.ms.common.helpers.DatagridHelper;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 02/febrero/2014
 * 
 */
public class ResourceProject {
    
    private String layoutheader = "./vm/layouts/header.vm";
    private String layoutfooter = "./vm/layouts/footer.vm";
    public String dirLayout="./vm/layouts/";

    public String getDirLayout() {
        return dirLayout;
    }

    public void setDirLayout(String dirLayout) {
        this.dirLayout = dirLayout;
    }

    public String getDirViews() {
        return DirViews;
    }

    public void setDirViews(String DirViews) {
        this.DirViews = DirViews;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
    public String DirViews="./vm/";
    public String layout="./vm/layouts/default.vm";
    
    public String getLayoutfooter() {
        return layoutfooter;
    }

    public String getLayoutheader() {
        return layoutheader;
    }
    
    public String generaGrid(LinkedHashMap<String,String> infoConstruccionTabla){
        DatagridHelper grid = new DatagridHelper();
        grid.setConfigHeader(infoConstruccionTabla);
        grid.formaTabla();
        return grid.getGridString();
    }
    
    public int calculaTotalPag(int totalItems, int rowsperpag){
        int residuo = totalItems % rowsperpag;
        int total_pag = totalItems/rowsperpag;
        if(residuo > 0){
            total_pag++;
        }
        return total_pag;
    }

    //calcula a partir de dondes cortara el query (offset) para el paginador
    public int __get_inicio_offset(int items_por_pagina, int pag_start){
        int total = items_por_pagina * pag_start;
        total = total - items_por_pagina;
        return total;
    }
    
    
    public String getUrl(HttpServletRequest httpservletrequest){
        //String scheme = httpservletrequest.getScheme();
        //String serverName = httpservletrequest.getServerName();
        //int serverPort = httpservletrequest.getServerPort();
        String contextPath = httpservletrequest.getContextPath();
        //return scheme+"://"+serverName+":"+serverPort+contextPath;
        
        return contextPath;
    }
    
    //Lee los datos para un Proceso
    public ResourceBundle readPropertiesLanguage(String resource){
        
        ResourceBundle recursos = ResourceBundle.getBundle(resource);
        return recursos;
        
    }
    
}