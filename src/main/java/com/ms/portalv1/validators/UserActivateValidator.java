/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.UserActivate;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 29/junio/2014
 * 
 */
public class UserActivateValidator implements Validator{
    
    private UserInterfaceDao UserDao;
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public void initValidator(UserInterfaceDao userdao){
        this.UserDao = userdao;
    }
    
    @Override
    public boolean supports(Class clazz) {
        return UserActivate.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user","userverif.user", "El campo Nombre de Usuario es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code","userverif.code", "El campo Codigo de Verificacion es necesario.");
        
        UserActivate usra = (UserActivate)target;
        
        
        if(!usra.getUser().isEmpty() && !usra.getUser().trim().equals("") && !usra.getCode().isEmpty() && !usra.getCode().trim().equals("")){
            //Validar que no se repita
            if(this.getUserDao().countUserCode(usra.getUser(), usra.getCode())<=0){
                errors.rejectValue("code", "userverifincorrect.code");
            }
        }
    }
}
