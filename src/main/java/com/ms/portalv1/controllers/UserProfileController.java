/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/julio/2014
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/profile")
public class UserProfileController {
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
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, @ModelAttribute("user") UserSessionData userSessionData, ModelMap model){
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, String> dataUser = new HashMap<String, String>();
        dataUser = this.getUserDao().getUserDataByIdEncod(userSessionData.getUserIdCod());
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", false);
        data.put("logout", true);
        
        model.addAttribute("userp", dataUser);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"user/profile.vm" );
        model.addAttribute("data",data);
        
        //return form view
        return "index";
    }
    
    
}
