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
 * 18/junio/2014
 */
@Controller
@SessionAttributes({"user"})
public class HomeController {
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
    
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public ModelAndView IndexController(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        HashMap<String, String> succes = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        UserSessionData userdata;
        
        String username = new String();
        
        
        
        resource.setLayout(resource.getDirLayout() +"home.vm");
        ModelAndView modelViewHome = new ModelAndView("index", "title", "Home - Portal Perseo");
        
        if(request.isUserInRole("ROLE_USER")){
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }


            succes = this.getUserDao().getUserData(1,username);
            userdata = new UserSessionData(succes);
        
            data.put("profile", true);
            data.put("logout", true);
            data.put("solicitudes", true);
            
            modelViewHome.addObject("layout", resource.getLayout());
            modelViewHome.addObject("view",resource.getDirViews()+"commercial/home.vm");
            modelViewHome.addObject("user", userdata);
            modelViewHome.addObject("data",data);
            modelViewHome.addObject("username", userdata.getUserName());
            
        }else{
            
            modelViewHome.addObject("layout", resource.getLayout());
            modelViewHome.addObject("view",resource.getDirViews()+"commercial/home.vm" );
            modelViewHome.addObject("data", "aqui puede ir datos extras");
        }
        
        return modelViewHome;
    }
    
    @RequestMapping(value="/ayuda", method=RequestMethod.GET)
    public ModelAndView helpandsupport(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        HashMap<String, String> succes = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        UserSessionData userdata;
        
        String username = new String();
        
        
        
        resource.setLayout(resource.getDirLayout() +"home.vm");
        ModelAndView modelViewhelpAndSupport = new ModelAndView("index");
        
        if(request.isUserInRole("ROLE_USER")){
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }


            succes = this.getUserDao().getUserData(1,username);
            userdata = new UserSessionData(succes);
        
            data.put("profile", true);
            data.put("logout", true);
            data.put("solicitudes", true);
            
            modelViewhelpAndSupport.addObject("layout", resource.getLayout());
            modelViewhelpAndSupport.addObject("view",resource.getDirViews()+"user/helpandsupport.vm" );
            modelViewhelpAndSupport.addObject("user", userdata);
            modelViewhelpAndSupport.addObject("data",data);
            modelViewhelpAndSupport.addObject("username", userdata.getUserName());
            
        }else{
            
            modelViewhelpAndSupport.addObject("layout", resource.getLayout());
            modelViewhelpAndSupport.addObject("view",resource.getDirViews()+"user/helpandsupport.vm" );
        }
        
        
        return modelViewhelpAndSupport;
    }
    
    @RequestMapping(value="/docs", method=RequestMethod.GET)
    public ModelAndView docs(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        HashMap<String, String> succes = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        UserSessionData userdata;
        
        String username = new String();
        
        
        
        resource.setLayout(resource.getDirLayout() +"home.vm");
        ModelAndView modelViewDocs = new ModelAndView("index");
        
        if(request.isUserInRole("ROLE_USER")){
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }


            succes = this.getUserDao().getUserData(1,username);
            userdata = new UserSessionData(succes);
        
            data.put("profile", true);
            data.put("logout", true);
            data.put("solicitudes", true);
            
            modelViewDocs.addObject("layout", resource.getLayout());
            modelViewDocs.addObject("view",resource.getDirViews()+"user/docs.vm" );
            modelViewDocs.addObject("user", userdata);
            modelViewDocs.addObject("data",data);
            modelViewDocs.addObject("username", userdata.getUserName());
            
        }else{
            modelViewDocs.addObject("layout", resource.getLayout());
            modelViewDocs.addObject("view",resource.getDirViews()+"user/docs.vm" );
        }
        
        
        
        return modelViewDocs;
    }
}