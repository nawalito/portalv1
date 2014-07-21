/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.portalv1.controllers;

import com.ms.common.cofig.UserSessionData;
import com.ms.common.helpers.SendEmailHelper;
import com.ms.common.helpers.generaMD5;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author alex
 */
@Controller
@RequestMapping(value = "/services")
@SessionAttributes({"user"})

public class ActionsServiceRequestController {
    @Autowired
    @Qualifier("daoUser")
    private UserInterfaceDao UserDao;
    
    @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao GralDao;
    
    @Autowired
    @Qualifier("daoRequest")
    private RequestInterfaceDao RequestDao;
    
    
    
    public ActionsServiceRequestController() {
        System.out.println("init RestController");
    }
    
    
    @RequestMapping(value = "/verify.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> verifyJson(
        HttpSession session,
        @RequestParam(value = "user", required = true) String username,
        @RequestParam(value = "password", required = true) String password,
        @ModelAttribute("user") UserSessionData userData
    ) throws Exception {
        
        System.out.println("verify.json");
        
        HashMap<String, Object> jsonretorno = new HashMap<>();
        
        //llamar validador usuario, generar un token cada vez que se el usuario sea autenticado   guardarlo en session y devolverlo en la vista
        if (this.getUserDao().verifUsernamePasswd(username, password)>0) {
            jsonretorno.put("success", true);
            jsonretorno.put("token", generaMD5.MD5(session.getId()+userData.getUserName()));//generar token y guardarlo en session
        } else {
            jsonretorno.put("success", false);
            jsonretorno.put("message", "usuario o contrase&ntildea incorrecto");
        }
        
        System.out.println("jsonretorno.success: "+jsonretorno.get("success"));
        
        return jsonretorno;
    }
    
    
    @RequestMapping(value = "/canceled.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> cancel(
            HttpSession session,
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "token", required = true) String token,
            @ModelAttribute("user") UserSessionData userData
        ) {
        System.out.println("canceled.json");
        System.out.println("folio:"+folio+"        token:"+token);
        
        HashMap<String, Object> jsonretorno = new HashMap<>();
        Map<String, Object> success = new HashMap<>();
        
        //validar que el token de usuario de la vista sea igual a la que esta almacenado en session
        if (token.equals(generaMD5.MD5(session.getId()+userData.getUserName()))) {
            
            //llamar funcion que cancela  el servicio
            success = this.getRequestDao().callStoredProcedurActionsRequest("cancel", userData.getUserIdCod(), folio);
            
            if(success.get("success_").equals("1")){
                //si laoperacion fue extosa
                jsonretorno.put("success", true);
                jsonretorno.put("message", "Solicitud cancelado");
            }else{
                jsonretorno.put("success", false);
                jsonretorno.put("message", "Error al cancelar. Intente de nuevo.");
            }
        } else {//si el token no coincide con lo obtenido de la vista no se realiza la operacion
            jsonretorno.put("success", false);
            jsonretorno.put("message", "Credenciales no validas");
        }
        
        return jsonretorno;
    }
    
    @RequestMapping(value = "/suspend.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> suspend(
            HttpSession session,
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "token", required = true) String token,
            @ModelAttribute("user") UserSessionData userData
    ) {
        System.out.println("suspend.json");
        System.out.println("folio:"+folio+"        token:"+token);
        
        HashMap<String, Object> jsonretorno = new HashMap<>();
        Map<String, Object> success = new HashMap<>();
        
        //validar que el token de usuario de la vista sea igual a la que esta almacenado en session
        if (token.equals(generaMD5.MD5(session.getId()+userData.getUserName()))) {
            
            //llamar funcion que suspende y envia correo de notificacion  de la accion 
            success = this.getRequestDao().callStoredProcedurActionsRequest("suspend", userData.getUserIdCod(), folio);
            
            if(success.get("success_").equals("1")){
                //si laoperacion fue extosa
                jsonretorno.put("success", true);
                jsonretorno.put("message", "Servicio suspendido.");
            }else{
                jsonretorno.put("success", false);
                jsonretorno.put("message", "Error al suspender. Intente de nuevo.");
            }
            
        } else {//si el token no coincide con lo obtenido de la vista no se realiza la operacion
            jsonretorno.put("success", false);
            jsonretorno.put("message", "Credenciales no validas");
        }
        
        return jsonretorno;
    }
    
    @RequestMapping(value = "/reactivate.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> activate(
            HttpSession session,
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "token", required = true) String token,
            @ModelAttribute("user") UserSessionData userData
    ) {
        System.out.println("reactivate.json");
        System.out.println("folio:"+folio+"        token:"+token);
        
        HashMap<String, Object> jsonretorno = new HashMap<>();
        Map<String, Object> success = new HashMap<>();

        //validar que el token de usuario de la vista sea igual a la que esta almacenado en session
        if (token.equals(generaMD5.MD5(session.getId()+userData.getUserName()))) {
            
            //llamar funcion que reactiva y envia correo de notificacion  de la accion 
            success = this.getRequestDao().callStoredProcedurActionsRequest("reactivate", userData.getUserIdCod(), folio);
            
            if(success.get("success_").equals("1")){
                //si laoperacion fue extosa
                jsonretorno.put("success", true);
                jsonretorno.put("message", "Servicio reactivado");
            }else{
                jsonretorno.put("success", false);
                jsonretorno.put("message", "Error al reactivar. Intente de nuevo.");
            }

        } else {//si el token no coincide con lo obtenido de la vista no se realiza la operacion
            jsonretorno.put("success", false);
            jsonretorno.put("message", "Credenciales no validas");
        }
        
        return jsonretorno;
    }
    
    @RequestMapping(value = "/renew.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> renew(
            HttpSession session,
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "token", required = true) String token,
            @ModelAttribute("user") UserSessionData userData
    ) {
        HashMap<String, Object> jsonretorno = new HashMap<>();
        System.out.println("renew.json");
        System.out.println("folio:"+folio+"        token:"+token);
        
        //validar que el token de usuario de la vista sea igual a la que esta almacenado en session
        if (token.equals(generaMD5.MD5(session.getId()+userData.getUserName()))) {
            //llamar funcion que renova y envia correo de notificacion  de la accion 
            //si laoperacion fue extosa
            jsonretorno.put("success", true);
            jsonretorno.put("message", "Servicio reactivado");

        } else {//si el token no coincide con lo obtenido de la vista no se realiza la operacion
            jsonretorno.put("success", false);
            jsonretorno.put("message", "Credenciales no validas");
        }

        return jsonretorno;
    }
    
    
    @RequestMapping(value = "/send_request.json", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody HashMap<String, Object> sendRequest(
            HttpSession session,HttpServletRequest request,
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "token", required = true) String token,
            @ModelAttribute("user") UserSessionData userData
    ) {
        HashMap<String, Object> jsonretorno = new HashMap<>();
        ArrayList<LinkedHashMap<String, String>> arrayData = new ArrayList<>();
        String msj_envio="";
        
        System.out.println("send_request.json");
        
        //validar que el token de usuario de la vista sea igual a la que esta almacenado en session
        if (token.equals(generaMD5.MD5(session.getId()+userData.getUserName()))) {
            //llamar funcion que que obtien los folios  y enviar correo el correo de los folios
            arrayData = this.getRequestDao().getFoliosUser(userData.getUserIdCod());
            
            if(arrayData.size()>0){
                        
                //Enviar correo electronico
                HashMap<String, String> dataSend = new HashMap<>();
                ArrayList<LinkedHashMap<String, String>> adjuntos = new ArrayList<>();
                ArrayList<LinkedHashMap<String, String>> destinatarios = new ArrayList<>();
                ArrayList<LinkedHashMap<String, String>> destinatario_copia_oculta = new ArrayList<>();
                LinkedHashMap<String, String> correo_copia_oculta = new LinkedHashMap<>();
                ArrayList<HashMap<String, String>> datosEnvio = new ArrayList<> ();
                String emailUspport="";
                
                datosEnvio = this.getGralDao().getEmailEnvio();
                destinatarios = this.getGralDao().getEmailUser(userData.getUserName());
                destinatario_copia_oculta = this.getGralDao().getEmailCopiaOculta();
                emailUspport = this.getGralDao().getEmailSupport();
                
                if(destinatario_copia_oculta.size()>0){
                    //Agregar correo del destinatario para copia oculta
                    correo_copia_oculta.put("recipient", destinatario_copia_oculta.get(0).get("recipient"));
                    correo_copia_oculta.put("type", destinatario_copia_oculta.get(0).get("type"));
                    destinatarios.add(correo_copia_oculta);   
                }
                
                if(datosEnvio.size()>0){
                    dataSend.put("hostname", datosEnvio.get(0).get("host"));
                    dataSend.put("username", datosEnvio.get(0).get("email"));
                    dataSend.put("password", datosEnvio.get(0).get("passwd"));
                    dataSend.put("puerto", datosEnvio.get(0).get("port"));
                    dataSend.put("tls", datosEnvio.get(0).get("tls"));
                }
                
                String url_logo = "http://"+request.getServerName() +":"+ request.getLocalPort() + request.getContextPath() + "/img/perseo/logo_perseo.png";
                
                
                String htmlText = "<H3>Folios registrados en portal Perseo:</H3>";
                htmlText += "<table>";
                htmlText += "<thead><tr><th>FOLIO</th><th>ESTADO</th></tr></thead>";
                htmlText += "<tbody>";
                for( LinkedHashMap<String,String> i : arrayData){
                    htmlText += "<tr><td>"+i.get("folio")+"</td><td>&nbsp;&nbsp;&nbsp;"+i.get("estatus")+"</td></tr>";
                }
                htmlText += "</tbody>";
                htmlText += "</table>";
                
                htmlText += "<br/>";
                htmlText += "<br/>";
                htmlText += "<i>";
                htmlText += "<font color=\"#0b0b3b\">";
                htmlText += "-------------------------------------------------------------";
                htmlText += "</font>";
                htmlText += "<br/>";
                htmlText += "<table style=\"font-style:italic; font-size:small; color:#0b0b3b;\">";
                htmlText += "<tbody>";
                htmlText += "<tr><td rowspan='3'><img class=\"img-responsive\" src=\""+url_logo+"\" alt=\"Perseo\"></td><td>Perseo seguridad movil</td></tr>";
                htmlText += "<tr><td>Contacto:</td></tr>";
                htmlText += "<tr><td>"+emailUspport+"</td></tr>";
                htmlText += "</tbody>";
                htmlText += "</table>";
                htmlText += "</i>";
                
                
                
                dataSend.put("asunto", "Solicitud de folios");
                dataSend.put("mensaje", htmlText);
                dataSend.put("codeverif", "");
                dataSend.put("urlverif", "");
                
                System.out.println("INICIANDO ENVIAR CORREO");
                SendEmailHelper sender = new SendEmailHelper(dataSend, adjuntos, destinatarios);
                
                msj_envio = sender.Validar();
                
                if(msj_envio.equals("true")){
                    msj_envio = sender.enviarEmail();
                }
                System.out.println("msj_envio: "+msj_envio);
            }
            
            //si laoperacion fue exitosa
            jsonretorno.put("success", true);
            jsonretorno.put("message", "Folios enviados al correo de usuario");
            
        } else {//si el token no coincide con lo obtenido de la vista no se realiza la operacion
            jsonretorno.put("success", false);
            jsonretorno.put("message", "Credenciales no validas");
        }

        return jsonretorno;

    }
    
    
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public GralInterfaceDao getGralDao() {
        return GralDao;
    }
    
    public RequestInterfaceDao getRequestDao() {
        return RequestDao;
    }
    
}
