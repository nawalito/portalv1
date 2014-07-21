/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.validators;
import com.ms.portalv1.models.Contact;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author alex
 */
public class ContactValidator implements Validator{
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PATTERN_NUMERIC = "[0-9]*";
    private static final String PATTERN_ALPHA="^[a-zA-Z\\ \\']+$";
    private static final String PATTERN_ALPHANUMERIC_SP="^[0-9a-zA-Z\\ \\']+$";
    
    @Override
    public boolean supports(Class clazz) {
        return Contact.class.equals(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","required.email", "El email es requerido.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject","required.subject", "El campo es requerido.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message","required.message", "Es requerido un mesaje");
        
        Contact contact = (Contact) target;
        
        if(!contact.getEmail().isEmpty() && !contact.getEmail().trim().equals("")){
            if(!contact.getEmail().matches(PATTERN_EMAIL)){
                errors.rejectValue("email", "notvalid.email");
            }
            
        }
        
        if(!contact.getSubject().isEmpty() && !contact.getSubject().trim().equals("")){
            if(!contact.getSubject().matches(PATTERN_ALPHANUMERIC_SP)){
                errors.rejectValue("subject", "notvalid.aphanumericsp");
            }
            if(contact.getSubject().length()< 5){
                errors.rejectValue("getSubject", "notvalid.mincharacter");
            }
            if(contact.getSubject().length()> 80){
                errors.rejectValue("getSubject", "notvalid.maxcharacter");
            }
        }
        
        if(!contact.getMessage().isEmpty() && !contact.getMessage().trim().equals("")){
            if(!contact.getMessage().matches(PATTERN_ALPHANUMERIC_SP)){
                errors.rejectValue("message", "notvalid.aphanumericsp");
            }
            if(contact.getMessage().length()< 15){
                errors.rejectValue("message", "notvalid.mincharacter");
            }
            if(contact.getMessage().length()> 500){
                errors.rejectValue("message", "notvalid.maxcharacter");
            }
        }
        
    }
}
