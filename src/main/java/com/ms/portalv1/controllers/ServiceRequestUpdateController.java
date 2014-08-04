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
 * 02/julio/2014
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/servicerequest_update")
public class ServiceRequestUpdateController {
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
    public ServiceRequestUpdateController(ServiceRequestValidator serviceRequestValidator) {
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
        HashMap<String, Object> dataUpdate = new HashMap<>();
        ServiceRequestRenew srr = new ServiceRequestRenew();
        HashMap<String, Object> elementHtml = new HashMap<>();
        
        System.out.println("getUserIdCod: "+userSessionData.getUserIdCod());
        System.out.println("getUserId: "+userSessionData.getUserId());
        System.out.println("ParameterFolio: "+request.getParameter("folio"));
        
        
        
        
        dataUpdate = this.getRequestDao().getDataRenew(request.getParameter("folio"), String.valueOf(userSessionData.getUserId()));
        
        srr.setCodeArea(dataUpdate.get("area_code").toString());
        srr.setPhone(dataUpdate.get("phone").toString());
        srr.setFolio(request.getParameter("folio"));
        
        data.put("dataUpdate", dataUpdate);
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", true);
        data.put("profile", false);
        data.put("logout", true);
        
        data.put("title", "actualizar servicio");
        
        //Command object
        model.addAttribute("update", srr);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"request/update.vm" );
        model.addAttribute("data",data);
        
        
        //return form view
        return "index";
    }
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processSubmit(
            @ModelAttribute("user") UserSessionData userSessionData, 
            @ModelAttribute("update") ServiceRequestRenew serviceRequestUpdate, 
            @RequestParam(value = "folio", required = true) String folio,
            @RequestParam(value = "pais", required = true) String pais, 
            @RequestParam(value = "codeArea", required = true) String codeArea, 
            @RequestParam(value = "phone", required = true) String phone, 
            @RequestParam(value = "typeLicence", required = true) String typeLicence, 
            @RequestParam(value = "totalMessage", required = true) String totalMessage, 
            @RequestParam(value = "frecuencyMessage", required = true) String frecuencyMessage, 
            
            BindingResult result, 
            SessionStatus status, 
            Model model
    ) {
        ModelAndView viewUpdateRequest;
        HashMap<String, Object> data = new HashMap<>();
//        serviveRequestUpdate.setFolio("0");
        serviceRequestUpdate.setIdent(userSessionData.getUserIdCod());
        this.getServiceRequestRenewValidator().validate(serviceRequestUpdate, result);
        
        if (result.hasErrors()) {
            //Mostrar aciones
            data.put("create_account", false);
            data.put("login", false);
            data.put("panel", true);
            data.put("profile", true);
            data.put("logout", true);
            
            
            viewUpdateRequest= new ModelAndView("index");

            viewUpdateRequest.addObject("layout", resource.getLayout());
            viewUpdateRequest.addObject("view",resource.getDirViews()+"request/update.vm" );
            viewUpdateRequest.addObject("data",data);
            
            return viewUpdateRequest;
        }else{
             if(!serviceRequestUpdate.getIdent().equals("")){
                Map<String, Object> success = new HashMap<>();
                Map<String, Object> success2 = new HashMap<>();
                ArrayList<LinkedHashMap<String, String>> dataRequest = new ArrayList<> ();
                String numeroReferencia="";
                String retorno="";
                String actualizo="0";
                String msj_envio="";
                
                String commandd_selected = "update_request";
                String data_string = commandd_selected+"___"+serviceRequestUpdate.getIdent()+"___"+serviceRequestUpdate.getFolio()+"___"+numeroReferencia+"___"+serviceRequestUpdate.getPais()+"___"+serviceRequestUpdate.getCodeArea()+"___"+serviceRequestUpdate.getPhone()+"___"+serviceRequestUpdate.getTypeLicence()+"___"+serviceRequestUpdate.getTotalMessage()+"___"+serviceRequestUpdate.getFrecuencyMessage();
                success = this.getRequestDao().callStoredProcedureAdmServiceRequest(data_string);
                
                if(success.get("success_").equals("1")){
                    System.out.println("SE HA actualizado la SOLICITUD");
                    folio = String.valueOf(success.get("folio_"));
                    
                    System.out.println("folio: "+folio);
                    
                }
             }
              //Mostrar aciones
                data = new HashMap<>();
                data.put("create_account", false);
                data.put("login", false);
                data.put("panel", true);
                data.put("profile", true);
                data.put("logout", true);
                
                //data.put("new", "true");
                //data.put("panel", "false");
                data.put("msj", "Proceso realizado correctamente");
                
                
                
                viewUpdateRequest = new ModelAndView("index");
                viewUpdateRequest.addObject("userp", data);
                viewUpdateRequest.addObject("layout", resource.getLayout());
                viewUpdateRequest.addObject("view",resource.getDirViews()+"request/searchrequest.vm" );
                viewUpdateRequest.addObject("data",data);
                
                return viewUpdateRequest;
         }
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
