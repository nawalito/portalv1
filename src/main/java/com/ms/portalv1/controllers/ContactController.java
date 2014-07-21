package com.ms.portalv1.controllers;

import com.ms.common.cofig.ResourceProject;
import com.ms.common.helpers.SendEmailHelper;
import com.ms.portalv1.interfacedaos.GralInterfaceDao;
import com.ms.portalv1.models.Contact;
import com.ms.portalv1.validators.ContactValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author alex
 */
@Controller
public final class ContactController {

    ResourceProject resource = new ResourceProject();
     @Autowired
    @Qualifier("daoGral")
    private GralInterfaceDao gralDao;

    public GralInterfaceDao getGralDao() {
        return gralDao;
    }

    public void setGralDao(GralInterfaceDao gralDao) {
        this.gralDao = gralDao;
    }

    public ResourceProject getResource() {
        return resource;
    }

    public void setResource(ResourceProject resource) {
        this.resource = resource;
    }

    public ContactController() {
        resource.setLayout(resource.getDirLayout() + "perseo.vm");
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public ModelAndView initForm(@ModelAttribute("contact") Contact contact, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ModelAndView ModelViewContact;
        ModelViewContact = new ModelAndView("index");
        ModelViewContact.addObject("layout", resource.getLayout());
        ModelViewContact.addObject("view", resource.getDirViews() + "user/contact.vm");

        return ModelViewContact;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public ModelAndView contactSave(@ModelAttribute("contact") Contact contact, BindingResult resultValidate, HttpServletRequest request) throws ServletException, IOException {

        ContactValidator contactValidation = new ContactValidator();
        contactValidation.validate(contact, resultValidate);

        ModelAndView ModelViewContact;
        ModelViewContact = new ModelAndView("index");

        if (resultValidate.hasErrors()) {
            ModelViewContact.addObject("layout", resource.getLayout());
            ModelViewContact.addObject("view", resource.getDirViews() + "user/contact.vm");
        } else {
            if(this.procesaFormulario(contact)){
                ModelViewContact.addObject("layout",resource.getDirLayout() +"home.vm");
                ModelViewContact.addObject("view", resource.getDirViews()+"commercial/home.vm");
            
            }else{
                ModelViewContact.addObject("layout", resource.getLayout());
                ModelViewContact.addObject("view", resource.getDirViews() + "user/contact.vm");
                ModelViewContact.addObject("data","No se pudo enviar el mensaje, intente de nuevo");
            }
        }

        return ModelViewContact;

    }

    private boolean procesaFormulario(Contact contact) {
        Boolean retorno;
        String htmlText;
        String msj_envio;
        ArrayList<LinkedHashMap<String, String>> adjuntos;
        HashMap<String, String> dataSend = new HashMap<>();
        HashMap<String, String> hashMapData = new HashMap<>();
        LinkedHashMap<String, String> correo_cliente = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, String>> destinatarios;
        ArrayList<HashMap<String, String>> datosEnvio;
        
        retorno=false;
        String emailUspport;
        
        datosEnvio = this.getGralDao().getEmailEnvio();
        emailUspport = this.getGralDao().getEmailSupport();
        destinatarios=new ArrayList<>();
        adjuntos = new ArrayList<>();
        
        if (emailUspport.length() > 0) {
            correo_cliente.put("recipient", emailUspport);
            correo_cliente.put("type", "TO");
            destinatarios.add(correo_cliente);

        }
        if (datosEnvio.size() > 0) {
            dataSend.put("hostname", datosEnvio.get(0).get("host"));
            dataSend.put("username", datosEnvio.get(0).get("email"));
            dataSend.put("password", datosEnvio.get(0).get("passwd"));
            dataSend.put("puerto", datosEnvio.get(0).get("port"));
            dataSend.put("tls", datosEnvio.get(0).get("tls"));
        }

        hashMapData.put("email", contact.getEmail());
        hashMapData.put("subject", contact.getSubject());
        hashMapData.put("message", contact.getMessage());

        htmlText=this.bodyMessage(hashMapData);
        System.out.println(htmlText);
        
        dataSend.put("asunto", "Soporte a Clientes");
        dataSend.put("mensaje", htmlText);
        dataSend.put("codeverif", "");
        dataSend.put("urlverif", "");
        
        System.out.println("INICIANDO ENVIAR CORREO");
        SendEmailHelper sender = new SendEmailHelper(dataSend, adjuntos, destinatarios);

        msj_envio = sender.Validar();
        
        if(msj_envio.equals("true")){
            msj_envio = sender.enviarEmail();
            retorno = msj_envio.equals("Correo enviado!");
        }
        
        
        return retorno;
    }

    private String bodyMessage(HashMap<String, String> hashMapData) {
        String htmlText = ""
                + "<html lan=\"es\">"
                + "<head><style>body {font-family:\"Helvetica\",\"Arial\",sans-serif; font-weight:normal; padding:0px; margin:0px; text-align:left; line-height:1.3;}.panel {background:#ECF8FF; border-color: #b9e5ff; } </style></head>"
                + "<body style=\"background:#394149\">"
                + "<center>"
                + "<table style=\"width:700px; background-color:#ffffff;\">"
                + "<tr>"
                + "<td Style=\"border-bottom: medium solid #E8670E; border-bottom-width:medium; border-bottom-style:solid; border-bottom-color:#E8670E;\">"
                + "<table style=\"width:700px;\">"
                + "<tr>"
                + "<td ><img src=\"cid:logo1\"></td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + "<table style=\"width:700px;\">"
                + "<tr>"
                + "<td><H3>Soporte a clientes</H3></td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + "<table>"
                + "<thead>"
                + "<tr>"
                + "<th></th><th></th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";

        htmlText += "<tr><td><b>Remitente:</b></td><td>&nbsp;&nbsp;&nbsp;" + hashMapData.get("email") + "</td></tr>";
        htmlText += "<tr><td><b>Asunto:</b></td><td>&nbsp;&nbsp;&nbsp;" + hashMapData.get("subject") + "</td></tr>";
        htmlText += "<tr><td><b>Mensaje:</b></td><td>&nbsp;&nbsp;&nbsp;" + hashMapData.get("message") + "</td></tr>";

        htmlText += ""
                + "<tr><td></td><td>&nbsp;<br>&nbsp;<br>&nbsp;</td></tr>"
                + "</tbody>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "<td >"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + "<table style=\"width:700px; background-color:#ebebeb; font-style:italic; font-size:small;\">"
                + "<tr>"
                + "<td rowspan=\"2\" width=\"160\"><img src=\"cid:logo2\"></td>"
                + "<td><p style=\"text-align:left;\"><b>Perseo Seguridad Movil</b><br></p></td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>";
        htmlText += ""
                + "<tr>"
                + "<td>"
                + "<table style=\"width:700px;\">"
                + "<tr>"
                + "<td>"
                + "<p style=\"text-align:center;\"><a href=\"#\">Terminos y Condiciones</a> | <a href=\"#\">Privacidad</a></p>"
                + "<td>"
                + "</tr>"
                + "</table>"
                + "</td>"
                + "</tr>"
                + "</table>";
        htmlText += ""
                + "</center>"
                + "</body>"
                + "</html>";

        return htmlText;
    }

}
