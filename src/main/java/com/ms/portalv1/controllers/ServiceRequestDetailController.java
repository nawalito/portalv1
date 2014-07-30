/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.cofig.UserSessionData;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import com.ms.portalv1.models.ServiceRequestDetail;
import com.ms.portalv1.validators.ServiceRequestValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
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
 * @author alex
 */
@Controller
@SessionAttributes({"user"})
@RequestMapping("/service_detail")
public class ServiceRequestDetailController {
    private static final Logger log = Logger.getLogger(ServiceRequestController.class.getName());
    ResourceProject resource = new ResourceProject();
    //ServiceRequestValidator serviceRequestValidator = new ServiceRequestValidator();
    
    @Autowired
    @Qualifier("daoRequest")
    private RequestInterfaceDao RequestDao;
    
    @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao GralDao;

    public ResourceProject getResource() {
        return resource;
    }

    public RequestInterfaceDao getRequestDao() {
        return RequestDao;
    }

    public GralInterfaceDao getGralDao() {
        return GralDao;
    }
    
    
    @Autowired
    public ServiceRequestDetailController(ServiceRequestValidator serviceRequestValidator) {
       // this.setServiceRequestValidator(serviceRequestValidator);
        resource.setLayout(resource.getDirLayout() +"perseo.vm");
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String initForm(HttpServletRequest request, ModelMap model, @ModelAttribute("user") UserSessionData userData){
        ServiceRequestDetail srd = new ServiceRequestDetail();
        ArrayList<LinkedHashMap<String, String>> arrayData = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();
        
        System.out.println("getUserName: "+userData.getUserName());
        System.out.println("folio: "+request.getParameter("folio"));
        System.out.println("token: "+request.getParameter("token"));
        
        arrayData = this.getRequestDao().getDataServiceRequest(request.getParameter("folio"), userData.getUserIdCod());
        
        srd.setFolio(request.getParameter("folio"));
        srd.setStatus("El folio no fue encontrado");
        if(arrayData.size()>0){
            srd.setCodeArea(arrayData.get(0).get("codigo_area"));
            srd.setCodecountry(arrayData.get(0).get("codigo_pais"));
            srd.setCountry(arrayData.get(0).get("pais"));
            srd.setPhone(arrayData.get(0).get("telefono"));
            srd.setTotalMsj(arrayData.get(0).get("total_msj"));
            srd.setTypeLicense(arrayData.get(0).get("tipo_solicitud"));
            srd.setContractDate(arrayData.get(0).get("fcontrato"));
            srd.setExpirationDate(arrayData.get(0).get("fvencimiento"));
            srd.setStatus(arrayData.get(0).get("estatus"));
            data.put("statuscode", arrayData.get(0).get("status_code"));
        }
        
        //Mostrar aciones
        data.put("create_account", false);
        data.put("login", false);
        data.put("panel", false);
        data.put("profile", true);
        data.put("logout", true);
        
        //Command object
        model.addAttribute("detail", srd);
        model.addAttribute("layout", resource.getLayout());
        model.addAttribute("view",resource.getDirViews()+"request/requestdetail.vm" );
        model.addAttribute("data",data);
        
        //return form view
        return "index";
    }
@RequestMapping(method = RequestMethod.GET)

    public String redirectSearch(HttpServletRequest request, ModelMap model, @ModelAttribute("user") UserSessionData userData){
        
         return "redirect:panel";
    }
}

