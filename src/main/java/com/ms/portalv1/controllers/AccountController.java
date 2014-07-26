/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * 19/junio/2014
 */
@Controller
@SessionAttributes({"user"})
public class AccountController {
    ResourceProject resource = new ResourceProject();
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;

    public ResourceProject getResource() {
        return resource;
    }
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("msj", "");
        
        //Mostrar aciones
        data.put("create_account", true);
        data.put("login", false);
        data.put("panel", false);
        data.put("profile", false);
        data.put("logout", false);
        
        //System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        ModelAndView x = new ModelAndView("index", "title", "Login - Portal Perseo");
        x = x.addObject("layout", resource.getLayout());
        x = x.addObject("view",resource.getDirViews()+"user/login.vm" );
        x = x.addObject("data",data);
        
        return x;
    }
    

    //Cuando no existe usuario o contraseña
    @RequestMapping(value="/error", method=RequestMethod.GET)
    public ModelAndView loginError(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("msj", "Usuario o Contrase&ntildea incorrecto");
        
        //Mostrar aciones
        data.put("create_account", true);
        data.put("login", false);
        data.put("panel", false);
        data.put("profile", false);
        data.put("logout", false);
        
        //System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        ModelAndView x = new ModelAndView("index", "title", "Login - Portal Perseo");
        x = x.addObject("layout", resource.getLayout());
        x = x.addObject("view",resource.getDirViews()+"user/login.vm" );
        x = x.addObject("data",data);
        
        return x;
    }
    
    
    //Cuando el usuario no tiene permiso para acceso a una url
    @RequestMapping(value="/403", method=RequestMethod.GET)
    public ModelAndView accessDenied(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();
        data.put("msj", "El usuario no tiene acceso a este elemento");
        
        //Mostrar aciones
        data.put("create_account", true);
        data.put("login", false);
        data.put("panel", false);
        data.put("profile", false);
        data.put("logout", false);
        
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        ModelAndView x = new ModelAndView("index", "title", "Login - Portal Perseo");
        
        x = x.addObject("layout", resource.getLayout());
        x = x.addObject("view",resource.getDirViews()+"user/login.vm" );
        x = x.addObject("data",data);
        
        return x;
    }
    
    
}
