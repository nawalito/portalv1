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
                String msj_envio="";
                
                //Enviar correo electronico
                HashMap<String, String> dataSend = new HashMap<>();
                ArrayList<LinkedHashMap<String, String>> adjuntos = new ArrayList<>();
                ArrayList<LinkedHashMap<String, String>> destinatarios = new ArrayList<>();
                LinkedHashMap<String, String> correo_user = new LinkedHashMap<>();
                ArrayList<HashMap<String, String>> envio = new ArrayList<> ();
                
                String userIdEncode = this.getUserDao().getUserIdEncod(user.getUserName());
                String url_activacion = "http://"+request.getServerName() +":"+ request.getLocalPort() + request.getContextPath() + request.getServletPath() +"/activate?user="+userIdEncode;
                
                envio = this.getGralDao().getEmailEnvio();
                destinatarios = this.getGralDao().getEmailCopiaOculta();
                //Agregar correo del nuevo usuario registrado
                correo_user.put("recipient", user.getEmail());
                correo_user.put("type", "TO");
                destinatarios.add(correo_user);
                
                if(envio.size()>0){
                    dataSend.put("hostname", envio.get(0).get("host"));
                    dataSend.put("username", envio.get(0).get("email"));
                    dataSend.put("password", envio.get(0).get("passwd"));
                    dataSend.put("puerto", envio.get(0).get("port"));
                    dataSend.put("tls", envio.get(0).get("tls"));
                }
                
                dataSend.put("asunto", "Bienvenido al Portal Perseo!");
                
                String htmlText = "<H3>Hola! Gracias por registrarse, bienvenido "+user.getUserName()+"!</H3>";
                htmlText += "<br/><br/>";
                htmlText += "C&oacute;digo de activacion: ";
                htmlText += "<b>"+code+"</b>";
                htmlText += "<br/><br/>";
                htmlText += "Haga clic en la siguiente url para activar su cuenta en perseo:";
                htmlText += "<br/>";
                htmlText += "<a href="+url_activacion+">"+url_activacion+"</a>";
                htmlText += "<br/>";
                htmlText += "<br/>";
                htmlText += "<br/>";
                htmlText += "<i>";
                htmlText += "-----------------------";
                htmlText += "<br/>";
                htmlText += "www.perseo.mx";
                htmlText += "<br/>";
                htmlText += "www.perseosolutions.mx";
                htmlText += "<br/>";
                htmlText += "Tel: 0180050505";
                htmlText += "</i>";
                
                //Aqui se agrega el mensaje al correo
                dataSend.put("mensaje", htmlText);
                
                SendEmailHelper sender = new SendEmailHelper(dataSend, adjuntos, destinatarios);
                
                msj_envio = sender.Validar();
                
                if(msj_envio.equals("true")){
                    msj_envio = sender.enviarEmail();
                }
                
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
