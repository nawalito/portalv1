/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import com.ms.portalv1.models.ServiceRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 02/julio/2014
 */
public class ServiceRequestValidator implements Validator{
    private static final String PATTERN_NUMERIC = "[0-9]*";
    private RequestInterfaceDao RequestDao;

    public RequestInterfaceDao getRequestDao() {
        return RequestDao;
    }

    public void setRequestDao(RequestInterfaceDao RequestDao) {
        this.RequestDao = RequestDao;
    }

    public void initValidator(RequestInterfaceDao RequestDao) {
        this.setRequestDao(RequestDao);
    }

    @Override
    public boolean supports(Class clazz) {
        return ServiceRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pais","required.pais", "Seleccionar pa&iacute;s.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codeArea", "required.codeArea", "Se requiere el c&oacute;digo de area.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "required.phone", "Se requiere el n&uacute;mero de tel&eacute;fono.");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeLicence","required.typeLicence", "Seleccionar tipo de licencia");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "totalMessage", "required.totalMessage", "Se requiere el numero de mensajes.");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "frecuencyMessage", "required.frecuencyMessage", "Seleccionar frecuencia del mensaje");
        
        ServiceRequest sr = (ServiceRequest) target;
        
        try {
            if (!sr.getCodeArea().equals("") && !sr.getCodeArea().isEmpty()) {
                if(!sr.getCodeArea().matches(PATTERN_NUMERIC)){
                    errors.rejectValue("codeArea", "notnumeric.codeArea");
                }else{
                    if(sr.getCodeArea().length()!=2){
                        errors.rejectValue("codeArea", "length.codeArea");
                    }
                }
            }
        } catch (Exception e) {
            errors.rejectValue("codeArea", "required.codeArea");
        }
        
        try {
            if (!sr.getPhone().equals("") && !sr.getPhone().isEmpty()) {
                if(!sr.getPhone().matches(PATTERN_NUMERIC)){
                    errors.rejectValue("phone", "notnumeric.phone");
                }else{
                    if(sr.getPhone().length()!=8){
                        errors.rejectValue("phone", "length.phone");
                    }else{
                        
                    }
                }
            }
        } catch (Exception e) {
            errors.rejectValue("phone", "required.phone");
        }
        
        
        try {
            
            if (sr.getPais().equals("0") || sr.getPais().equals("") || sr.getPais().isEmpty()) {
                errors.rejectValue("pais", "required.pais");
            }
        } catch (Exception e) {
            errors.rejectValue("pais", "required.pais");
        }
        
        
        if(!errors.hasFieldErrors("pais") && !errors.hasFieldErrors("codeArea") && !errors.hasFieldErrors("phone")){
            if(this.getRequestDao().getCountPhoneForUser(sr.getPais()+sr.getCodeArea()+sr.getPhone(),sr.getIdent(), true)>0){
                errors.rejectValue("phone", "repeateduser.phone");
            }else{
                if(this.getRequestDao().getCountPhoneForOtherUser(sr.getPais()+sr.getCodeArea()+sr.getPhone(),sr.getIdent(), true)>0){
                    errors.rejectValue("phone", "repeateduser.phone");
                }
            }
        }
        
        
        try {
            if (sr.getTypeLicence().equals("0") || sr.getTypeLicence().equals("") || sr.getTypeLicence().isEmpty()) {
                errors.rejectValue("typeLicence", "required.typeLicence");
            }else{
                //Verificar si es tipo licencia FREE
                if(this.getRequestDao().getVerifyLicenseClass(sr.getTypeLicence())>0){
                    if(this.getRequestDao().getCountPhone(sr.getPhone(),sr.getIdent())>0){
                        errors.rejectValue("phone", "repeated.phone");
                    }
                }
            }
        } catch (Exception e) {
            errors.rejectValue("typeLicence", "required.typeLicence");
        }

        try {
            if (sr.getTotalMessage().equals("0") || sr.getTotalMessage().equals("") || sr.getTotalMessage().isEmpty()) {
                errors.rejectValue("totalMessage", "required.totalMessage");
            }
        } catch (Exception e) {
            errors.rejectValue("totalMessage", "required.totalMessage");
        }

        try {
            if (sr.getFrecuencyMessage().equals("0") || sr.getFrecuencyMessage().equals("") || sr.getFrecuencyMessage().isEmpty()) {
                errors.rejectValue("frecuencyMessage", "required.frecuencyMessage");
            }
        } catch (Exception e) {
            errors.rejectValue("frecuencyMessage", "required.frecuencyMessage");
        }

    }
}