/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Noe Martinez 
 * gpmarsan@gmail.com
 * 18/junio/2014
 */
@Controller
public class HomeController {
    
    ResourceProject resource = new ResourceProject();
    
    public ResourceProject getResource() {
        return resource;
    }
    
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public ModelAndView IndexController(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        resource.setLayout(resource.getDirLayout() +"home.vm");
        ModelAndView x = new ModelAndView("index", "title", "Home - Portal Perseo");
        x = x.addObject("layout", resource.getLayout());
        x = x.addObject("view",resource.getDirViews()+"commercial/home.vm" );
        x = x.addObject("data", "aqui puede ir datos extras");
        
        return x;
    }
}