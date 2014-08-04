package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.RequestInterfaceDao;
import com.ms.portalv1.models.ServiceRequestRenew;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author agnux
 */
public class ServiceRequestRenewValidator implements Validator{
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
        return ServiceRequestRenew.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codeArea", "required.codeArea", "Se requiere el c&oacute;digo de area.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "required.phone", "Se requiere el n&uacute;mero de tel&eacute;fono.");
        
        ServiceRequestRenew srr = (ServiceRequestRenew) target;
        
        try {
            if (!srr.getPhone().equals("") && !srr.getPhone().isEmpty()) {
                if(!srr.getPhone().matches(PATTERN_NUMERIC)){
                    errors.rejectValue("phone", "notnumeric.phone");
                }else{
                    if(srr.getPhone().length()!=8){
                        errors.rejectValue("phone", "length.phone");
                    }
                }
            }
        } catch (Exception e) {
            errors.rejectValue("phone", "required.phone");
        }
        
        try {
            if (!srr.getCodeArea().equals("") && !srr.getCodeArea().isEmpty()) {
                if(!srr.getCodeArea().matches(PATTERN_NUMERIC)){
                    errors.rejectValue("codeArea", "notnumeric.codeArea");
                }else{
                    if(srr.getCodeArea().length()!=2){
                        errors.rejectValue("codeArea", "length.codeArea");
                    }
                }
            }
        } catch (Exception e) {
            errors.rejectValue("codeArea", "required.codeArea");
        }
        
        if(!errors.hasFieldErrors("codeArea") && !errors.hasFieldErrors("phone")){
            
            System.out.println("srr.getCodeArea(): "+srr.getCodeArea());
            System.out.println("srr.getPhone(): "+srr.getPhone());
            System.out.println("srr.getIdent(): "+srr.getIdent());
            
            if(this.getRequestDao().getCountPhoneForUser(srr.getCodeArea()+srr.getPhone(),srr.getIdent(), false)>0){
                errors.rejectValue("phone", "repeateduser.phone");
            }else{
                if(this.getRequestDao().getCountPhoneForOtherUser(srr.getCodeArea()+srr.getPhone(),srr.getIdent(), false)>0){
                    errors.rejectValue("phone", "repeateduser.phone");
                }
            }
        }
    }
}