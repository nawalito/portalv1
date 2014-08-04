/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.common.helpers.CalculaDigitoVerificador;
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
        serviveRequestRenew.setIdent(userSessionData.getUserIdCod());
        
        this.getServiceRequestRenewValidator().initValidator(this.getRequestDao());
        
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
                        
                    }
                }
                
                //form success
                
                //Mostrar aciones
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", true);
                data.put("profile", false);
                data.put("logout", true);
                
                //data.put("new", "true");
                //data.put("panel", "false");
                data.put("msj", msj_envio);
                
                x = new ModelAndView("index");
                x = x.addObject("userp", data);
                x = x.addObject("layout", resource.getLayout());
                x = x.addObject("view",resource.getDirViews()+"request/searchrequest.vm" );
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