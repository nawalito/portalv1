/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;

import com.ms.portalv1.interfacedaos.UserInterfaceDao;
import com.ms.portalv1.models.User;
import com.ms.portalv1.models.UserProfile;
import com.ms.portalv1.models.UserProfilePasswd;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 01/julio/2014
 */
public class UserProfilePasswdValidator implements Validator{
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
        return UserProfilePasswd.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPass", "required.currentPass", "El campo Password es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPass", "required.newPass", "El campo nuevo Password es necesario.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPass","notmatchnew.confirmPass", "El campo Confirmar Password es necesario.");
        
        UserProfilePasswd usprofpwd = (UserProfilePasswd)target;
        
        if(!usprofpwd.getCurrentPass().isEmpty() && !usprofpwd.getCurrentPass().trim().equals("")){
            //Validar que no se repita
            if(this.getUserDao().countCurrentPasswordUser(usprofpwd.getCurrentPass(), usprofpwd.getId())<=0){
                errors.rejectValue("currentPass", "verifcorrect.currentPass");
            }
        }
        
        if(!(usprofpwd.getNewPass().equals(usprofpwd.getConfirmPass()))){
            errors.rejectValue("confirmPass", "notmatchnew.confirmPass");
        }
    }
}
