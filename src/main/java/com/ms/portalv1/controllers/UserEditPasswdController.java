/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.UserProfilePasswd;
import com.ms.portalv1.validators.UserProfilePasswdValidator;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/julio/2014
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/edit_passwd")
public class UserEditPasswdController {
    ResourceProject resource = new ResourceProject();
    UserProfilePasswdValidator userProfilePasswdValidator = new UserProfilePasswdValidator();
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }
    
    public UserProfilePasswdValidator getUserProfilePasswdValidator() {
        return userProfilePasswdValidator;
    }
    
    public void setUserProfilePasswdValidator(UserProfilePasswdValidator userProfilePasswdValidator) {
        this.userProfilePasswdValidator = userProfilePasswdValidator;
    }
    
    
    
    @Autowired
    public UserEditPasswdController(UserProfilePasswdValidator userProfilePasswdValidator) {
        this.setUserProfilePasswdValidator(userProfilePasswdValidator);
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, @ModelAttribute("user") UserSessionData userdata, ModelMap model){
        UserProfilePasswd usProfilePasswd = new UserProfilePasswd();
        HashMap<String, Object> data = new HashMap<>();
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", false);
        data.put("logout", true);

        //Command object
        model.addAttribute("userpwd", usProfilePasswd);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"user/editpasswd.vm" );
        model.addAttribute("username", userdata.getUserName());
        model.addAttribute("data",data);
        
        //return form view
        return "index";
    }
    
    
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView updatePasswd(@ModelAttribute("user") UserSessionData userSessionData, @ModelAttribute("userpwd") UserProfilePasswd userProfilePasswd, BindingResult result, SessionStatus status, Model model) {
        ModelAndView x = null;
        HashMap<String, Object> data = new HashMap<>();
        
        userProfilePasswd.setId(userSessionData.getUserIdCod());
        
        this.getUserProfilePasswdValidator().initValidator(this.getUserDao());
        
        this.getUserProfilePasswdValidator().validate(userProfilePasswd, result);
        
        if (result.hasErrors()) {
            
            //Mostrar aciones
            data.put("create_account", false);
            data.put("login", false);
            data.put("panel", true);
            data.put("profile", true);
            data.put("logout", true);
        
            x = new ModelAndView("index", "title", "Editar Password - Portal Perseo");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"user/editpasswd.vm" );
            x = x.addObject("data",data);
            
            return x;
            
        }else{
            if(!userProfilePasswd.getId().equals("0")){
                //Actualizar
                String retorno="";
                String actualizo="0";
                String commandd_selected = "edit_passwd";
                
                String data_string = commandd_selected +"___"+ userProfilePasswd.getId()+"___"+""+"___"+""+"___"+""+"___"+userProfilePasswd.getNewPass()+"___"+" "+"___"+" ";
                
                actualizo = this.getUserDao().callStoredProcedureAddUser(data_string);
                
                if(actualizo.equals("1")){
                    System.out.println("SE ACTUALIZO EL PASSWORD DEL USUARIO");
                }
                
                //form success
                HashMap<String, String> dataUser = new HashMap<String, String>();
                dataUser = this.getUserDao().getUserDataByIdEncod(userSessionData.getUserIdCod());
                
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", true);
                data.put("profile", false);
                data.put("logout", true);
                
                x = new ModelAndView("index");
                x = x.addObject("userp", dataUser);
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/profile.vm" );
                x = x.addObject("data",data);
                
                return x;
            }
        }
        
        
        return x;
    }
    
}
