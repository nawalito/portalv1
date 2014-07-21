/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.helpers.SendEmailHelper;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.User;
import com.ms.portalv1.validators.UserValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 26/junio/2014
 * 
 */
@Controller
@RequestMapping("/create_account")
public class UserController {
    ResourceProject resource = new ResourceProject();
    UserValidator userValidator = new UserValidator();
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;
    
    @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao GralDao;
    
    public GralInterfaceDao getGralDao() {
        return GralDao;
    }
    
    public UserValidator getUserValidator() {
        return userValidator;
    }
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }
    
    @Autowired
    public UserController(UserValidator userValidator) {
        this.userValidator = userValidator;
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    @RequestMapping( method = RequestMethod.POST)
    public ModelAndView processSubmit(@ModelAttribute("user") User user, BindingResult result, SessionStatus status,HttpServletRequest request) {
        ModelAndView x = null;
        HashMap<String, Object> data = new HashMap<>();
        
        this.getUserValidator().initUserValidator(this.getUserDao());
        
        this.getUserValidator().validate(user, result);
        
        if (result.hasErrors()) {
           //if validator failed
            
            //Mostrar aciones
            data.put("create_account", false);
            data.put("login", true);
            data.put("panel", false);
            data.put("profile", false);
            data.put("logout", false);
        
            x = new ModelAndView("index", "title", "Registro de Usuario - Portal Perseo");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"user/create.vm" );
            x = x.addObject("data",data);

            return x;
           
        } else {
            status.setComplete();
            System.out.println("SIN errores");
            
            String retorno="";
            String actualizo="1";
            String commandd_selected = "new";
            //Generar codigo de verificacion
            String milis = String.valueOf(System.currentTimeMillis());                
            String code=milis.substring(milis.length()-4, milis.length());
            
            String data_string = commandd_selected +"___"+ user.getId()+"___"+user.getEmail()+"___"+user.getAlias()+"___"+user.getUserName()+"___"+user.getPass()+"___"+user.getNoRef()+"___"+code;
            
            actualizo = this.getUserDao().callStoredProcedureAddUser(data_string);
            
            if(actualizo.equals("1")){
                retorno="Registro exitoso!";
                //form success
                x = new ModelAndView("index");
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/success.vm" );
                x = x.addObject("data","register");
            }else{
                retorno="Fallo el intento de registro!";
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", true);
                data.put("panel", false);
                data.put("profile", false);
                data.put("logout", false);
                
                //if validator failed
                x = new ModelAndView("index", "title", "Registro de Usuario - Portal Perseo");
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/create.vm" );
                x = x.addObject("data","register");
            }
            
            return x;
        }
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model){
        HashMap<String, Object> data = new HashMap<>();
        System.out.println("initForm");
        
        User us = new User();
        us.setId("0");
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", true);
        data.put("panel", false);
        data.put("profile", false);
        data.put("logout", false);

        //command object
        model.addAttribute("user", us);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"user/create.vm" );
        model.addAttribute("data",data);
        
        //return form view
        return "index";
    }
    
}
