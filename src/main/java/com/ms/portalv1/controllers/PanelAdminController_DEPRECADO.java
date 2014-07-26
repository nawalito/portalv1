/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez 
 * gpmarsan@gmail.com
 * 22/junio/2014
 */
@Controller
@SessionAttributes({"user"})
public class PanelAdminController_DEPRECADO {
    ResourceProject resource = new ResourceProject();
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;

    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }
    
    
    @RequestMapping(value="/paneladm", method=RequestMethod.GET)
    public ModelAndView panel(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws ServletException, IOException {
        //System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        ModelAndView x = new ModelAndView("admin/paneladmin", "title", "Panel de Administrador - Portal Perseo");
        x = x.addObject("layoutheader", resource.getLayoutheader());
        x = x.addObject("layoutfooter", resource.getLayoutfooter());
        //x = x.addObject("grid", resource.generaGrid(infoConstruccionTabla));
        
        UserSessionData userdata = (UserSessionData) session.getAttribute("user");
        
        x = x.addObject("username", userdata.getUserName());
        x = x.addObject("uuid", userdata.getUserIdCod());
        
        //return new ModelAndView("article", model);
        
        return x;
    }
}
