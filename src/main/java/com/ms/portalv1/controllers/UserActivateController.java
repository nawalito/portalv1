/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.UserActivate;
import com.ms.portalv1.validators.UserActivateValidator;
import java.util.HashMap;
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
 * 29/junio/2014
 * 
 */
@Controller
@RequestMapping("/activate")
public class UserActivateController {
    ResourceProject resource = new ResourceProject();
    UserActivateValidator userActiValidator = new UserActivateValidator();
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }

    public UserActivateValidator getUserActiValidator() {
        return userActiValidator;
    }
    
    @Autowired
    public UserActivateController(UserActivateValidator userActivateValidator) {
        this.userActiValidator = userActivateValidator;
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    @RequestMapping( method = RequestMethod.POST)
    public ModelAndView processSubmit(@ModelAttribute("activate") UserActivate usera, BindingResult result, SessionStatus status) {
        ModelAndView x = null;
        HashMap<String, Object> data = new HashMap<>();
        
        this.getUserActiValidator().initValidator(this.getUserDao());
        
        this.getUserActiValidator().validate(usera, result);
        
        if (result.hasErrors()) {
            //if validator failed
            x = new ModelAndView("index", "title", "Activacion de Usuario - Portal Perseo");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"user/activate.vm" );
            x = x.addObject("data","activate");
           
           return x;
           
        } else {
            status.setComplete();
            
            String retorno="";
            String actualizo="1";
            String commandd_selected = "activar";
            
            String data_string = commandd_selected +"___"+ usera.getUser() +"___"+""+"___"+""+"___"+""+"___"+""+"___"+""+"___"+usera.getCode();
            System.out.println(data_string);
            actualizo = this.getUserDao().callStoredProcedureAddUser(data_string);
            
            if(actualizo.equals("1")){
                retorno="Registro exitoso!";
                String msj_envio="";
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", true);
                data.put("panel", false);
                data.put("profile", false);
                data.put("logout", false);
                
                //form success
                x = new ModelAndView("index", "title", "Home - Portal Perseo");
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/login.vm" );
                x = x.addObject("data",data);
                
            }else{
                retorno="Fallo el intento de activacion!";
                //if validator failed
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", true);
                data.put("panel", false);
                data.put("profile", false);
                data.put("logout", false);
                
                x = new ModelAndView("index", "title", "Activacion de Usuario - Portal Perseo");
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/activate.vm" );
                x = x.addObject("data",data);
            }
            
            return x;
        }
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model){
        HashMap<String, Object> data = new HashMap<>();
        System.out.println("initFormActivate");
        Boolean existUser=false;
        Boolean accountIsActivated=false;
        existUser = this.getUserDao().accountActivatedOrExist(request.getParameter("user"),true);
        if(existUser){//se verifica que el identifier del usario exista
            accountIsActivated = this.getUserDao().accountActivatedOrExist(request.getParameter("user"),false);
            if(accountIsActivated==false){//se verifica que la cuenta no este activa
                UserActivate usa = new UserActivate();
                usa.setUser(request.getParameter("user"));
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", false);
                data.put("profile", false);
                data.put("logout", false);
                data.put("activate",true);

                //Command object
                model.addAttribute("activate", usa);
                model.addAttribute("layout", resource.getLayout());
                model.addAttribute("view",resource.getDirViews()+"user/activate.vm" );
                model.addAttribute("data",data);

                //return form view
                return "index";
            }
        }
        
        return "redirect:home";
        
    }
    
    
    
    
}
