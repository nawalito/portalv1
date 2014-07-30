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
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 20/junio/2014
 */
@Controller
@SessionAttributes({"user"})
public class PanelController {
    ResourceProject resource = new ResourceProject();
    String view = "";
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;

    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }
    
    @RequestMapping(value="/panel", method=RequestMethod.GET)
    public ModelAndView panel(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws ServletException, IOException {
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        
        System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        
        HashMap<String, String> succes = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        String username = new String();
        UserSessionData userdata = null;
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        succes = this.getUserDao().getUserData(1,username);
        userdata = new UserSessionData(succes);
        view=resource.getDirViews()+"user/panel.vm";
        ModelAndView x=null;
        
        if(request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_ADMIN2")){
            x = new ModelAndView("admin/paneladmin", "title", "Panel de Administrador - Portal Perseo");
            x = x.addObject("user", userdata);
            x = x.addObject("username", userdata.getUserName());
            x = x.addObject("uuid", userdata.getUserIdCod());
            System.out.println("ROLE_ADMIN");
        }else{
            if(request.isUserInRole("ROLE_USER")){
                System.out.println("ee"+this.getUserDao().countUserRequest(userdata.getUserIdCod()));
                if(this.getUserDao().countUserRequest(userdata.getUserIdCod())>0){
                    view=resource.getDirViews()+"request/searchrequest.vm";
                }
               
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", false);
                data.put("profile", true);
                data.put("logout", true);
                
                //data.put("panel", "true");
                data.put("msj", "");
                
                //Ya tiene solicitudes
                x = new ModelAndView("index", "title", "Panel de Usuario - Portal Perseo");
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",view);
                x = x.addObject("user", userdata);
                x = x.addObject("data",data);
                x = x.addObject("username", userdata.getUserName());
                    
                System.out.println("ROLE_USER");
                
            }else{
                System.out.println("ERROR ROLE");
                /*
                x = new ModelAndView("admin/paneluser", "title", "Error de Autenticaci&oacute;n - Portal Perseo");
                x = x.addObject("layoutheader", resource.getLayoutheader());
                x = x.addObject("layoutfooter", resource.getLayoutfooter());
                x = x.addObject("username", userdata.getUserName());
                //x = x.addObject("uuid", userdata.getUserIdCod());
                */
            }
        }
        
        return x;
    }
 }
