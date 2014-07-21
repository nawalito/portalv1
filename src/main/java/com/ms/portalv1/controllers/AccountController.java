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
    
    /*
    @RequestMapping(value="/create_account", method=RequestMethod.GET)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        ModelAndView x = new ModelAndView("user/create", "title", "Registro de Usuario - Portal Perseo");
        x = x.addObject("layoutheader", resource.getLayoutheader());
        x = x.addObject("layoutfooter", resource.getLayoutfooter());
        
        ArrayList<String> a = new ArrayList();
        
        a.add("valor1:Este es el valor numero1");
        a.add("valor2:Este es el valor numero2");
        a.add("valor3:Este es el valor numero3");
        a.add("valor4:Este es el valor numero4");
        a.add("valor5:Este es el valor numero5");
        a.add("valor6:Este es el valor numero6");
        
        x = x.addObject("arreglo", a);
        
        return x;
    }
    */
    
    
    
    
    /*
    //edicion y nuevo
    @RequestMapping(value="/adduser", method = RequestMethod.POST)
    public @ResponseBody String editJson(
        @RequestParam(value="id", required=true) String id,
        @RequestParam(value="email", required=true) String email,
        @RequestParam(value="alias", required=true) String alias,
        @RequestParam(value="username", required=true) String username,
        @RequestParam(value="pass", required=true) String pass,
        @RequestParam(value="ref", required=true) String ref
    ) {
        
        System.out.println("ADDUSER");
        String retorno="";
        String actualizo="1";
        String commandd_selected = "new";
        
        String pin = "xxx";
        
        String data_string = commandd_selected +"___"+ id+"___"+email+"___"+alias+"___"+username+"___"+pass+"___"+ref+"___"+pin;
        
        //System.out.println("data_string: "+data_string);
        
        //succes = this.getUserDao().selectFunctionValidateAaplicativo(data_string,app_selected,extra_data_array);
        
        if( actualizo.equals("1") ){
            actualizo = this.getUserDao().addUser(data_string);
            retorno="Registro exitoso!";
        }else{
            retorno="Fallo la actualizaci&oacute;n!";
        }

        return retorno;
    }
    */
    
    
    
    
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
        System.out.println("user: "+ request.getContextPath() + "   "+ request.getServletPath() + "    "+ request.getRequestURI());
        ModelAndView x = new ModelAndView("index", "title", "Login - Portal Perseo");
        HashMap<String, Object> data = new HashMap<>();
        
        //Mostrar aciones
        data.put("create_account", true);
        data.put("login", false);
        data.put("panel", false);
        data.put("profile", false);
        data.put("logout", false);
        
        x = x.addObject("layout", resource.getLayout());
        x = x.addObject("view",resource.getDirViews()+"user/login.vm" );
        x = x.addObject("data",data);
//        x = x.addObject("layoutheader", resource.getLayoutheader());
//        x = x.addObject("layoutfooter", resource.getLayoutfooter());
        
        return x;
    }
    

    
    
    @RequestMapping(value="/login_admin", method=RequestMethod.GET)
    public ModelAndView loginAdmin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        ModelAndView x = new ModelAndView("admin/loginadmin", "title", "Login Administrador- Portal Perseo");
        x = x.addObject("layoutheader", resource.getLayoutheader());
        x = x.addObject("layoutfooter", resource.getLayoutfooter());
        
        return x;
    }
    
    
    
}
