/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.common.helpers.CalculaDigitoVerificador;
import com.ms.common.helpers.SendEmailHelper;
import com.ms.common.helpers.StringHelper;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import com.ms.portalv1.models.ServiceRequestRenew;
import com.ms.portalv1.validators.ServiceRequestRenewValidator;
import com.ms.portalv1.validators.ServiceRequestValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 13/julio/2014
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/servicerequest_renew")
public class ServiceRequestRenewController {
    private static final Logger log = Logger.getLogger(ServiceRequestRenewController.class.getName());
    ResourceProject resource = new ResourceProject();
    ServiceRequestRenewValidator serviceRequestRenewValidator = new ServiceRequestRenewValidator();
    
    @Autowired
    @Qualifier("daoRequest")
    private RequestInterfaceDao RequestDao;
    
    @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao GralDao;
    
    
    @Autowired
    public ServiceRequestRenewController(ServiceRequestValidator serviceRequestValidator) {
        //this.setServiceRequestValidator(serviceRequestValidator);
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(
            HttpServletRequest request, 
            @ModelAttribute("user") UserSessionData userSessionData, 
            ModelMap model
    ){
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> dataRenew = new HashMap<>();
        ServiceRequestRenew srr = new ServiceRequestRenew();
        HashMap<String, Object> elementHtml = new HashMap<>();
        
        System.out.println("getUserIdCod: "+userSessionData.getUserIdCod());
        System.out.println("getUserId: "+userSessionData.getUserId());
        System.out.println("ParameterFolio: "+request.getParameter("folio"));
        
        srr.setFolio(request.getParameter("folio"));
        srr.setCodeArea(request.getParameter("codeArea"));
        srr.setPhone(request.getParameter("phone"));
        
        srr.setFolio(request.getParameter("folio"));
        dataRenew = this.getRequestDao().getDataRenew(request.getParameter("folio"), String.valueOf(userSessionData.getUserId()));
        
        data.put("dataRenew", dataRenew);
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", true);
        data.put("logout", true);
        
        data.put("title", "Renovaci&oacute;n de servicio");
        
        //Command object
        model.addAttribute("renew", srr);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"request/servicerequestrenew.vm" );
        model.addAttribute("data",data);
        
        
        //return form view
        return "index";
    }
    
    
    
    
    //******Retorna ArrayList****************************************/
    @ModelAttribute("ListaPaises")
    public ArrayList<LinkedHashMap<String,String>>  countryList() {
        ArrayList<LinkedHashMap<String,String>> country = new ArrayList<LinkedHashMap<String,String>>();
        country = this.getRequestDao().getPaises();
        return country;
    }
    
    @ModelAttribute("ListaTiposSolicitud")
    public ArrayList<LinkedHashMap<String,String>>  notTrialLicence() {
        ArrayList<LinkedHashMap<String,String>> lista = new ArrayList<LinkedHashMap<String,String>>();
        //Se le pasa parametro false para que no incluya tipos de contratos de prueba
        lista = this.getRequestDao().getTiposSolicitud(false);
        return lista;
    }
    
    @ModelAttribute("ListaFrecuenciaMsjs")
    public ArrayList<LinkedHashMap<String,String>>  frecuenciaMensajes() {
        ArrayList<LinkedHashMap<String,String>> lista = new ArrayList<LinkedHashMap<String,String>>();
        lista = this.getRequestDao().getFrecuenciaMsjs();
        return lista;
    }
    
    @ModelAttribute("ListaCantidadMsjs")
    public ArrayList<LinkedHashMap<String,String>>  cantidadMsjs() {
        ArrayList<LinkedHashMap<String,String>> lista = new ArrayList<LinkedHashMap<String,String>>();
        lista = this.getRequestDao().getCantidadMsjs();
        return lista;
    }
    
    
    
    
    
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processSubmit(
            @ModelAttribute("user") UserSessionData userSessionData, 
            @ModelAttribute("renew") ServiceRequestRenew serviveRequestRenew, 
            
            @RequestParam(value = "pais", required = true) String pais, 
            @RequestParam(value = "typeLicence", required = true) String typeLicence, 
            @RequestParam(value = "totalMessage", required = true) String totalMessage, 
            @RequestParam(value = "frecuencyMessage", required = true) String frecuencyMessage, 
            
            BindingResult result, 
            SessionStatus status, 
            Model model
    ) {
        ModelAndView x = null;
        HashMap<String, Object> data = new HashMap<>();
        serviveRequestRenew.setFolio("0");
        
        
        this.getServiceRequestRenewValidator().validate(serviveRequestRenew, result);
        
        if (result.hasErrors()) {
            
            
            //Mostrar aciones
            data.put("create_account", false);
            data.put("login", false);
            data.put("panel", true);
            data.put("profile", true);
            data.put("logout", true);
            data.put("title", "Renovaci&oacute;n de servicio");
        
            x = new ModelAndView("index", "title", "Renovaci&oacute;n - Portal Perseo");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"request/servicerequestrenew.vm" );
            x = x.addObject("data",data);
            
            return x;
        }else{
            
                //Actualizar
                //status.setComplete();
                Map<String, Object> success = new HashMap<>();
                Map<String, Object> success2 = new HashMap<>();
                ArrayList<LinkedHashMap<String, String>> dataRequest = new ArrayList<> ();
                String numeroReferencia="";
                String retorno="";
                String actualizo="0";
                String msj_envio="";
                String folio="";
                String commandd_selected = "new_request";
                
                String data_string = commandd_selected+"___"+userSessionData.getUserIdCod()+"___"+serviveRequestRenew.getFolio()+"___"+numeroReferencia+"___"+pais+"___"+serviveRequestRenew.getCodeArea()+"___"+serviveRequestRenew.getPhone()+"___"+typeLicence+"___"+totalMessage+"___"+frecuencyMessage;
                
                System.out.println("data_string: "+data_string);
                
                success = this.getRequestDao().callStoredProcedureAdmServiceRequest(data_string);
                
                if(success.get("success_").equals("1")){
                    System.out.println("SE HA DADO DE ALTA LA SOLICITUD");
                    
                    folio = String.valueOf(success.get("folio_"));
                    
                    System.out.println("folio: "+folio);
                    System.out.println("getUserIdCod: "+userSessionData.getUserIdCod());
                    dataRequest = this.getRequestDao().getDataServiceRequest(folio, userSessionData.getUserIdCod());
                    
                    System.out.println("Tipo de paquete: "+dataRequest.get(0).get("clase_paquete").toUpperCase());
                    
                    if(dataRequest.size()>0){
                        
                        if(!dataRequest.get(0).get("clase_paquete").toUpperCase().trim().equals("FREE")){
                            //Si es diferente de FREE  entonces se debe generar numero de referencia y enviar correo
                            
                            CalculaDigitoVerificador dv = new CalculaDigitoVerificador();
                            
                            numeroReferencia = String.valueOf(success.get("noref_")) + String.valueOf(dv.modulo10(numeroReferencia));
                            
                            data_string = "update_noref" +"___"+ userSessionData.getUserIdCod() +"___"+String.valueOf(success.get("folio_"))+"___"+numeroReferencia+"___"+""+"___"+""+"___"+""+"___"+"0"+"___"+"0"+"___"+"0";
                            
                            success2 = this.getRequestDao().callStoredProcedureAdmServiceRequest(data_string);
                            
                            if(success2.get("success_").equals("1")){
                                retorno="Registro de Solicitud exitoso!";
                            }
                        }
                        
                        

                        //Enviar correo electronico
                        HashMap<String, String> dataSend = new HashMap<>();
                        ArrayList<LinkedHashMap<String, String>> adjuntos = new ArrayList<>();
                        ArrayList<LinkedHashMap<String, String>> destinatarios = new ArrayList<>();
                        ArrayList<LinkedHashMap<String, String>> destinatario_copia_oculta = new ArrayList<>();
                        LinkedHashMap<String, String> correo_copia_oculta = new LinkedHashMap<>();
                        ArrayList<HashMap<String, String>> datosEnvio = new ArrayList<> ();
                        ArrayList<LinkedHashMap<String, String>> datosBanco = new ArrayList<> ();
                        String emailVentas = "";
                        String emailUspport="";
                        
                        
                        
                        datosEnvio = this.getGralDao().getEmailEnvio();
                        destinatarios = this.getGralDao().getEmailUser(userSessionData.getUserName());
                        destinatario_copia_oculta = this.getGralDao().getEmailCopiaOculta();
                        emailVentas = this.getGralDao().getEmailSale();
                        //Seimpre se seleccionara el id del banco 1
                        datosBanco = this.getGralDao().getBanco("1");
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
                        
                        
                        
                        
                        String htmlText = "<H3>Solicitud de renovacion registrado!</H3>";
                        htmlText += "Datos de la solicitud: ";
                        htmlText += "<br/>";
                        htmlText += "<b>Folio</b>: "+dataRequest.get(0).get("folio");
                        htmlText += "<br/>";
                        htmlText += "<b>Tipo de Licencia</b>: "+dataRequest.get(0).get("tipo_solicitud").replace("ñ", "&ntilde;");
                        
                        if(!dataRequest.get(0).get("clase_paquete").toUpperCase().trim().equals("FREE")){
                            //Diferente de FREE
                            /*
                            htmlText += "<br/>";
                            htmlText += "<b>Fecha</b>:  +"+dataRequest.get(0).get("fecha_solicitud");
                            htmlText += "<br/>";
                            htmlText += "<b>Pais</b>:  +"+dataRequest.get(0).get("pais");
                            htmlText += "<br/>";
                            htmlText += "<b>Codigo de Pais</b>: "+dataRequest.get(0).get("codigo_pais");
                            htmlText += "<br/>";
                            htmlText += "<b>Codigo de area</b>: "+dataRequest.get(0).get("codigo_area");
                            htmlText += "<br/>";
                            htmlText += "<b>Frecuencia de mensajes</b>: "+dataRequest.get(0).get("frecuencia_msj");
                            htmlText += "<br/>";
                            htmlText += "<b>Numero de mensajes</b>: "+dataRequest.get(0).get("total_msj");
                            */
                            htmlText += "<br/>";
                            htmlText += "<br/>";
                            htmlText += "Favor de realizar deposito:";
                            htmlText += "<br/>";
                            htmlText += "<b>Banco</b>: "+datosBanco.get(0).get("banco");
                            htmlText += "<br/>";
                            htmlText += "<b>Titular</b>: "+datosBanco.get(0).get("titular_cuenta");
                            htmlText += "<br/>";
                            htmlText += "<b>Numero de cuenta</b>: "+datosBanco.get(0).get("numero_cuenta");
                            htmlText += "<br/>";
                            htmlText += "<b>Numero de Referencia</b>: "+numeroReferencia;
                            htmlText += "<br/>";
                            htmlText += "<b>Cantidad</b>:  $ "+StringHelper.AgregaComas(dataRequest.get(0).get("precio_paquete"));
                            htmlText += "<br/>";
                            htmlText += "<br/>";
                            htmlText += "Una vez hecho el deposito favor de enviar la ficha escaneada a la siguiente cuenta de correo electronico para que la proteccion sea activada:";
                            if(!emailVentas.equals("")){
                                htmlText += "<br/>";
                                htmlText += emailVentas;
                            }
                        }else{
                            //FREE
                            htmlText += "<br/>";
                            htmlText += "<br/>";
                            htmlText += "Su solicitud debe pasar a una revision y posteriormente sera activado. La activacion del periodo de prueba se le confirmara mediante correo electronico.";
                            htmlText += "<br/>";
                            htmlText += "<br/>";
                            if(!emailVentas.equals("")){
                                htmlText += "Para informacion de paquetes contacte a ";
                                htmlText += emailVentas;
                            }
                        }

                        htmlText += "<br/>";
                        htmlText += "<br/>";
                        htmlText += "<i>";
                        htmlText += "-----------------------";
                        htmlText += "<br/>";
                        htmlText += "Perseo seguridad movil";
                        htmlText += "<br/>";
                        htmlText += "Contacto: ";
                        htmlText += "<br/>";
                        htmlText += emailUspport;
                        htmlText += "<br/>";
                        htmlText += "</i>";


                        dataSend.put("asunto", "Registro de solicitud de proteccion");
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
                }
                
                //form success
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", true);
                data.put("profile", true);
                data.put("logout", true);
                
                //data.put("new", "true");
                //data.put("panel", "false");
                data.put("msj", msj_envio);
                
                
                
                x = new ModelAndView("index");
                x = x.addObject("userp", data);
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"user/panel.vm" );
                x = x.addObject("data",data);
                
                return x;
            
        }
        
        //return x;
    }
    
    
    
    
    public ResourceProject getResource() {
        return resource;
    }

    public void setResource(ResourceProject resource) {
        this.resource = resource;
    }


    public ServiceRequestRenewValidator getServiceRequestRenewValidator() {
        return serviceRequestRenewValidator;
    }

    public void setServiceRequestRenewValidator(ServiceRequestRenewValidator serviceRequestRenewValidator) {
        this.serviceRequestRenewValidator = serviceRequestRenewValidator;
    }

    public RequestInterfaceDao getRequestDao() {
        return RequestDao;
    }

    public void setRequestDao(RequestInterfaceDao RequestDao) {
        this.RequestDao = RequestDao;
    }
    
    public GralInterfaceDao getGralDao() {
        return GralDao;
    }
    
}
