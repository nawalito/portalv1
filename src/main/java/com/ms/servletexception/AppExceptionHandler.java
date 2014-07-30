/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.servletexception;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Noe Martinez 
 * gpmarsan@gmail.com
 * 24/julio/2014
 */
@WebServlet("/AppExceptionHandler")
public class AppExceptionHandler extends HttpServlet{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        
        
        
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.write("<html><head>\n" +
"        <title>perseo</title>\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"        <meta charset=\"UTF-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n" +
"        <meta name=\"description\" content=\"Perseo - Seguridad Movil\">\n" +
"        <link rel=\"shortcut icon\" href=\"../img/perseo/favicon2-01.png\">\n" +
"	\n" +
"        <!-- css-->\n" +
"        <link rel=\"stylesheet\" href=\"../css/bootstrap-3.1.1/bootstrap.min.css\" type=\"text/css\">\n" +
"        <link rel=\"stylesheet\" href=\"../css/perseomx/style.css\" type=\"text/css\">\n" +
"        \n" +
"            <!-- libray js-->\n" +
"            <script src=\"../js/libs/modernizr2.8.1.min.js\"></script>\n" +
"            <script src=\"../js/libs/jquery1.11.1.min.js\"></script>\n" +
"            <!-- load assets css and js-->\n" +
"        \n" +
"        <!--[if lt IE 9]>\n" +
"            <script src=\"https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js\"></script>\n" +
"            <script src=\"https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js\"></script>\n" +
"        <![endif]-->\n" +
"    </head></head><body><header>\n" +
"            <nav class=\"navbar navbar-default\" role=\"navigation\">\n" +
"                <div class=\"container-fluid \">\n" +
"                    <div class=\"navbar-header\">\n" +
"                        <button type=\"button\" class=\"navbar-toggle hidden-xs\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\">\n" +
"                            <span class=\"sr-only\">Toggle navigation</span>\n" +
"                            <span class=\"icon-bar\"></span>\n" +
"                            <span class=\"icon-bar\"></span>\n" +
"                            <span class=\"icon-bar\"></span>\n" +
"                          </button>\n" +
"                        <div class=\"navbar-logo\">\n" +
"                            <a class=\"navbar-brand\" href=\"home\">\n" +
"                                <img class=\"img-responsive\" src=\"../img/perseo/logo_perseo.png\" alt=\"perseo\">\n" +
"                            </a>\n" +
"                        </div>\n" +

"                    </div>\n" +
"                </div>\n" +
"            </nav>\n" +
"        </header><div class=\"row container-body\"><div class=\"row section-title\">\n" +
"    <div class=\"col-sm-12\">\n" +
"        <h1>Error </h1><h1>\n" +
"    </h1></div>\n" +
"</div><div class=\"container\">");
        if(statusCode != 500){
            out.write("<h3>Error Details</h3>");
            out.write("<strong>Status Code</strong>:"+statusCode+"<br>");
            out.write("<strong>Requested URI</strong>:"+requestUri);
        }else{
            out.write("<h3>Exception Details</h3>");
            out.write("<ul><li>Servlet Name:"+servletName+"</li>");
            out.write("<li>Exception Name:"+throwable.getClass().getName()+"</li>");
            out.write("<li>Requested URI:"+requestUri+"</li>");
            out.write("<li>Exception Message:"+throwable.getMessage()+"</li>");
            out.write("</ul>");
        }
        
        out.write("<br><br>");
        out.write("<a href=\"panel\">PANEL USER</a>");
        out.write("<div></div></body></html>");
    }
}
