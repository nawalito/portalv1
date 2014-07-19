/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.UserProfile;
import com.ms.portalv1.validators.UserProfileValidator;
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
@RequestMapping("/edit_profile")
public class UserEditProfileController {
    ResourceProject resource = new ResourceProject();
    UserProfileValidator userProfileValidator = new UserProfileValidator();
    
    
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public ResourceProject getResource() {
        return resource;
    }
    
    
    public UserProfileValidator getUserProfileValidator() {
        return userProfileValidator;
    }

    public void setUserProfileValidator(UserProfileValidator userProfileValidator) {
        this.userProfileValidator = userProfileValidator;
    }
    
    @Autowired
    public UserEditProfileController(UserProfileValidator userProfileValidator) {
        this.setUserProfileValidator(userProfileValidator);
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, @ModelAttribute("user") UserSessionData userSessionData, ModelMap model){
        UserProfile usrProfile = new UserProfile();
        HashMap<String, String> dataUser = new HashMap<String, String>();
        HashMap<String, Object> data = new HashMap<>();
        
        dataUser = this.getUserDao().getUserDataByIdEncod(userSessionData.getUserIdCod());
        usrProfile.setAlias(dataUser.get("alias"));
        usrProfile.setEmail(dataUser.get("email"));
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", true);
        data.put("logout", true);

        //Command object
        model.addAttribute("userp", usrProfile);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"user/editprofile.vm" );
        model.addAttribute("username", userSessionData.getUserName());
        model.addAttribute("data","editpasswd");
        
        //return form view
        return "index";
    }
    
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView updateProfile(@ModelAttribute("user") UserSessionData userSessionData, @ModelAttribute("userp") UserProfile userProfile, BindingResult result, SessionStatus status, Model model) {
        ModelAndView x = null;
        HashMap<String, Object> data = new HashMap<>();
        
        userProfile.setId(userSessionData.getUserIdCod());
        
        this.getUserProfileValidator().initValidator(this.getUserDao());
        
        this.getUserProfileValidator().validate(userProfile, result);
        
        if (result.hasErrors()) {
            //Mostrar aciones
            data.put("create_account", false);
            data.put("login", false);
            data.put("panel", true);
            data.put("profile", true);
            data.put("logout", true);

            x = new ModelAndView("index", "title", "Editar Perfil - Portal Perseo");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"user/editprofile.vm" );
            x = x.addObject("data",data);
            
            return x;
        }else{
            if(!userProfile.getId().equals("0")){
                //Actualizar
                //status.setComplete();
                

                String retorno="";
                String actualizo="0";
                String commandd_selected = "edit_profile";
                //Generar codigo de verificacion
                String milis = String.valueOf(System.currentTimeMillis());                
                String code=milis.substring(milis.length()-4, milis.length());
                
                String data_string = commandd_selected +"___"+ userProfile.getId()+"___"+userProfile.getEmail()+"___"+userProfile.getAlias()+"___"+" "+"___"+" "+"___"+" "+"___"+" ";
                
                actualizo = this.getUserDao().callStoredProcedureAddUser(data_string);
                
                if(actualizo.equals("1")){
                    System.out.println("SE ACTUALIZO EL EMAIL, ALIAS");
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
                x = x.addObject("data","profile");
                
                return x;
            }
        }
        
        return x;
    }
    
}
