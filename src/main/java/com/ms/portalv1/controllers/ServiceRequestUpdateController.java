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
import com.ms.portalv1.models.ServiceRequest;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 02/julio/2014
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/servicerequest_update")
public class ServiceRequestUpdateController {
    private static final Logger log = Logger.getLogger(ServiceRequestController.class.getName());
    ResourceProject resource = new ResourceProject();
    ServiceRequestValidator serviceRequestValidator = new ServiceRequestValidator();
    
    @Autowired
    @Qualifier("daoRequest")
    private RequestInterfaceDao RequestDao;
    
    @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao GralDao;
    
    
    @Autowired
    public ServiceRequestUpdateController(ServiceRequestValidator serviceRequestValidator) {
        this.setServiceRequestValidator(serviceRequestValidator);
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, @ModelAttribute("user") UserSessionData userData, ModelMap model){
        HashMap<String, Object> data = new HashMap<>();
        LinkedHashMap<String,String>  typeLicence = new LinkedHashMap<>();
        ServiceRequest sr = new ServiceRequest();
        
        
        if(this.getRequestDao().countUserRequest(userData.getUserIdCod())>0){
            typeLicence = this.getRequestDao().getTypeLicence(false);
        }else{
            typeLicence = this.getRequestDao().getTypeLicence(true);
        }
        
        data.put("typeLicence", typeLicence);
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", false);
        data.put("logout", true);
        
        
        //Command object
        model.addAttribute("servicer", sr);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"request/update.vm" );
        model.addAttribute("data",data);
        
        //return form view
        return "index";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processSubmit(@ModelAttribute("user") UserSessionData userSessionData, @ModelAttribute("servicer") ServiceRequest serviveRequest, BindingResult result, SessionStatus status, Model model) {
        HashMap<String, Object> data = new HashMap<>();
        LinkedHashMap<String,String>  typeLicence = new LinkedHashMap<>();
        
        ModelAndView x = null;
        
        serviveRequest.setIdent(userSessionData.getUserIdCod());
        serviveRequest.setFolio("0");
        
        this.getServiceRequestValidator().initValidator(this.getRequestDao());
        
        this.getServiceRequestValidator().validate(serviveRequest, result);
        
        if (result.hasErrors()) {
            
            if(this.getRequestDao().countUserRequest(userSessionData.getUserIdCod())>0){
                typeLicence = this.getRequestDao().getTypeLicence(false);
            }else{
                typeLicence = this.getRequestDao().getTypeLicence(true);
            }
            
            data.put("typeLicence", typeLicence);
            
            x = new ModelAndView("index");
            x = x.addObject("layout", resource.getLayout());
            x = x.addObject("view",resource.getDirViews()+"request/update.vm" );
            x = x.addObject("data",data);
            
            return x;
        }else{
            if(!serviveRequest.getIdent().equals("")){
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
                
                String data_string = commandd_selected+"___"+serviveRequest.getIdent()+"___"+serviveRequest.getFolio()+"___"+numeroReferencia+"___"+serviveRequest.getPais()+"___"+serviveRequest.getCodeArea()+"___"+serviveRequest.getPhone()+"___"+serviveRequest.getTypeLicence()+"___"+serviveRequest.getTotalMessage()+"___"+serviveRequest.getFrecuencyMessage();
                
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
                            
                            String noRefSinDv= String.valueOf(success.get("noref_"));
                            
                            numeroReferencia = noRefSinDv + String.valueOf(dv.modulo10(noRefSinDv));
                            
                            data_string = "update_noref" +"___"+ serviveRequest.getIdent()+"___"+String.valueOf(success.get("folio_"))+"___"+numeroReferencia+"___"+""+"___"+""+"___"+""+"___"+"0"+"___"+"0"+"___"+"0";
                            
                            success2 = this.getRequestDao().callStoredProcedureAdmServiceRequest(data_string);
                            
                            if(success2.get("success_").equals("1")){
                                retorno="Registro de Solicitud exitoso!";
                            }
                        }
                    }
                }
                
                
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
                x = x.addObject("view",resource.getDirViews()+"request/searchrequest.vm" );
                x = x.addObject("data",data);
                
                return x;
            }
        }
        
        return x;
    }
    
    //******Retorna ArrayList****************************************/
    
    @ModelAttribute("ListaPaises")
    public ArrayList<LinkedHashMap<String,String>>  countryList() {
        ArrayList<LinkedHashMap<String,String>> country = new ArrayList<LinkedHashMap<String,String>>();
        country = this.getRequestDao().getPaises();
        return country;
    }
    
    @ModelAttribute("ListaTiposSolicitud")
    public ArrayList<LinkedHashMap<String,String>>  typeLicenceList() {
        ArrayList<LinkedHashMap<String,String>> lista = new ArrayList<LinkedHashMap<String,String>>();
        lista = this.getRequestDao().getTiposSolicitud(true);
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
    
    
    
    //******Retorna HashMap****************************************/
    /*
    @ModelAttribute("TypeLicence")
    public LinkedHashMap<String,String>  typeLicence() {
        LinkedHashMap<String,String> lista = new LinkedHashMap<String,String>();
        lista = this.getRequestDao().getTypeLicence();
        return lista;
    }
    */
    @ModelAttribute("Countrys")
    public LinkedHashMap<String,String>  countrys() {
        LinkedHashMap<String,String> lista = new LinkedHashMap<String,String>();
        lista = this.getRequestDao().getCountrys();
        return lista;
    }
    
    @ModelAttribute("FrecuencyNotification")
    public LinkedHashMap<String,String>  frecuencyNotification() {
        LinkedHashMap<String,String> lista = new LinkedHashMap<String,String>();
        lista = this.getRequestDao().getFrecuencyNotification();
        return lista;
    }
    
    @ModelAttribute("TotalMessage")
    public LinkedHashMap<String,String>  totalMessage() {
        LinkedHashMap<String,String> lista = new LinkedHashMap<String,String>();
        lista = this.getRequestDao().getTotalMessage();
        return lista;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public ResourceProject getResource() {
        return resource;
    }

    public void setResource(ResourceProject resource) {
        this.resource = resource;
    }

    public ServiceRequestValidator getServiceRequestValidator() {
        return serviceRequestValidator;
    }

    public void setServiceRequestValidator(ServiceRequestValidator serviceRequestValidator) {
        this.serviceRequestValidator = serviceRequestValidator;
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
