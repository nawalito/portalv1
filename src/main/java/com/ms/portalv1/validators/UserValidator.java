/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 26/junio/2014
 * 
 */
public class UserValidator implements Validator{
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PATTERN_NUMERIC = "[0-9]*";
    
    private UserInterfaceDao UserDao;

    public UserInterfaceDao getUserDao() {
        return UserDao;
    }
    
    public void initUserValidator(UserInterfaceDao userdao){
        this.UserDao = userdao;
    }
    
    @Override
    public boolean supports(Class clazz) {
        return User.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","required.email", "El campo Email es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alias","required.alias", "El campo Alias es Necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName","required.userName", "El campo Nombre de Usuario es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pass", "required.pass", "El campo Password es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPass","required.confirmPass", "El campo Confirmar Password es necesario.");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "noRef", "required.noRef", "El campo Numero de Referencia es necesario.");
        
        User usr = (User)target;
        
        if(!(usr.getPass().equals(usr.getConfirmPass()))){
            errors.rejectValue("confirmPass", "notmatch.confirmPass");
        }
        
        if(!usr.getEmail().isEmpty() && !usr.getEmail().trim().equals("")){
            if(!usr.getEmail().matches(PATTERN_EMAIL)){
                errors.rejectValue("email", "notvalid.email");
            }else{
                //Validar que no se repita
                if(this.getUserDao().countEmail(usr.getEmail())>0){
                    errors.rejectValue("email", "alreadyexists.email");
                }
            }
        }
        
        if(!usr.getAlias().isEmpty() && !usr.getAlias().trim().equals("")){
            //Validar que no se repita
            if(this.getUserDao().countAlias(usr.getAlias())>0){
                errors.rejectValue("alias", "alreadyexists.alias");
            }
        }
        
        if(!usr.getUserName().isEmpty() && !usr.getUserName().trim().equals("")){
            //Validar que no se repita
            if(this.getUserDao().countUsername(usr.getUserName())>0){
                errors.rejectValue("userName", "alreadyexists.userName");
            }
        }
        
//        if(!usr.getNoRef().isEmpty() && !usr.getNoRef().trim().equals("")){
//            if(!usr.getNoRef().matches(PATTERN_NUMERIC)){
//                errors.rejectValue("noRef", "notvalid.noRef");
//            }else{
//                //Valida que exista un numero de referencia valido
//                if(this.getUserDao().countNumeroReferecia(usr.getNoRef())<=0){
//                    errors.rejectValue("noRef", "notexists.noRef");
//                }
//            }
//        }

        
    }
    
}
