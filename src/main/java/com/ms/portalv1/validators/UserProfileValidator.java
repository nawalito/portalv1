/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.UserProfile;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/julio/2014
 */
public class UserProfileValidator implements Validator{
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private UserInterfaceDao UserDao;
    
    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public void initValidator(UserInterfaceDao userdao){
        this.UserDao = userdao;
    }
    
    @Override
    public boolean supports(Class clazz) {
        return UserProfile.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","required.email", "El campo Email es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alias","required.alias", "El campo Alias es Necesario.");
        
        UserProfile usprof = (UserProfile)target;
        
        if(!usprof.getEmail().isEmpty() && !usprof.getEmail().trim().equals("")){
            if(!usprof.getEmail().matches(PATTERN_EMAIL)){
                errors.rejectValue("email", "notvalid.email");
            }else{
                //Validar que no se repita
                if(this.getUserDao().countEmailIsNotUser(usprof.getEmail(), usprof.getId())>0){
                    errors.rejectValue("email", "alreadyexists.email");
                }
            }
        }
        
        if(!usprof.getAlias().isEmpty() && !usprof.getAlias().trim().equals("")){
            //Validar que no se repita
            if(this.getUserDao().countAliasIsNotUser(usprof.getAlias(), usprof.getId())>0){
                errors.rejectValue("alias", "alreadyexists.alias");
            }
        }
    }
}
