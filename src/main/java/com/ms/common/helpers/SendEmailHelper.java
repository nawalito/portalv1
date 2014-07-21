/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.common.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 28/junio/2014
 */
public class SendEmailHelper {
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String host;
    private String emailOrigen;
    private String emailDestino;
    private String contrasenia;
    private String mensaje;
    private ArrayList<LinkedHashMap<String,String>> adjuntos;
    private ArrayList<LinkedHashMap<String,String>> destinatarios;
    private String asunto;
    private String puerto;
    private String tls;
    
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getEmailDestino() {
        return emailDestino;
    }
    
    public void setEmailDestino(String emailDestino) {
        this.emailDestino = emailDestino;
    }
    
    
    public String getEmailOrigen() {
        return emailOrigen;
    }
    
    public void setEmailOrigen(String emailOrigen) {
        this.emailOrigen = emailOrigen;
    }
    
    public String getTls() {
        return tls;
    }
    
    public void setTls(String tls) {
        this.tls = tls;
    }
    
    public String getPuerto() {
        return puerto;
    }
    
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getContrasenia() {
        return contrasenia;
    }
    
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public ArrayList<LinkedHashMap<String, String>> getAdjuntos() {
        return adjuntos;
    }
    
    public void setAdjuntos(ArrayList<LinkedHashMap<String, String>> adjuntos) {
        this.adjuntos = adjuntos;
    }
    
    public ArrayList<LinkedHashMap<String, String>> getDestinatarios() {
        return destinatarios;
    }
    
    public void setDestinatarios(ArrayList<LinkedHashMap<String, String>> destinatarios) {
        this.destinatarios = destinatarios;
    }
    
    
    public SendEmailHelper(HashMap<String, String> data, ArrayList<LinkedHashMap<String, String>> adjuntos, ArrayList<LinkedHashMap<String, String>> destinatarios){
        this.setHost(data.get("hostname"));
        this.setContrasenia(data.get("password"));
        this.setEmailOrigen(data.get("username"));
        this.setPuerto(data.get("puerto"));
        this.setTls(data.get("tls"));
        this.setAdjuntos(adjuntos);
        this.setDestinatarios(destinatarios);
        this.setAsunto(data.get("asunto"));
        this.setMensaje(data.get("mensaje"));
        
        
        for( LinkedHashMap<String,String> i : this.getDestinatarios() ){
            if(i.get("type").toUpperCase().trim().equals("TO")){
                this.setEmailDestino(i.get("recipient"));
            }
        }
    }
    
    
    
    public String Validar() {
        String valor_retorno = new String();
        boolean emailDestinoCorrecto=false;
        boolean emailOrigenCorrecto=false;
        boolean emailDestinoCcoCorrecto=false;
        
        valor_retorno="true";
        
        //Validar correo del destinatario
        emailDestinoCorrecto = this.getEmailDestino().matches(PATTERN_EMAIL);
        
        if(emailDestinoCorrecto){
                if(this.getEmailOrigen()!=null){
                    if(!this.getEmailOrigen().equals("")){
                        
                        //Validar correo de envio
                        emailOrigenCorrecto = this.getEmailOrigen().matches(PATTERN_EMAIL);
                        
                        if(emailOrigenCorrecto){
                            if(!this.getContrasenia().equals("")){
                                if(!this.getPuerto().equals("")){
                                    if(!this.getHost().equals("")){
                                        valor_retorno="true";
                                    }else{
                                        valor_retorno="No se ha definido el Servidor SMTP para el env&iacute;o.";
                                    }
                                }else{
                                    valor_retorno="No se ha definido el Puerto para el env&iacute;o.";
                                }
                            }else{
                                valor_retorno="Falta la contrase&ntilde;a del correo de env&iacute;o.";
                            }
                        }else{
                            valor_retorno="El correo de env&iacute;o es invalido: "+this.getEmailOrigen();
                        }
                    }else{
                        valor_retorno="No se ha definido un correo de env&iacute;o.";
                    }
                }else{
                    valor_retorno="No existe una configuraci&oacute;n para el env&iacute;o de correos.";
                }
        }else{
            valor_retorno="Email de destinatario es incorrecto: "+ this.getEmailDestino();
        }
        
        System.out.println("VALIDACION: "+valor_retorno);
        return valor_retorno;
    }
    
    
    
    
    public String enviarEmail() {
        String retorno="";
        try {
            retorno="Correo enviado!";
            //ESTABLECER LA SESION JAVAMAIL
            Properties props = System.getProperties();
            
            //props.clear();
            
            // Nombre del host de correo, es smtp.gmail.com
            props.setProperty("mail.smtp.host", this.getHost());
            
            // Puerto para envio de correos
            props.setProperty("mail.smtp.port",this.getPuerto());
            
            // Si requiere o no usuario y password para conectarse.
            props.setProperty("mail.smtp.auth", "true");
            
            //props.setProperty("mail.smtp.ssl.trust", this.getIpServidor());
            
            if(this.getTls().equals("true")){
                // TLS si esta disponible
                props.setProperty("mail.smtp.starttls.enable", "true");
            }else{
                props.put("mail.smtp.ssl.enable", false);
                props.put("mail.smtp.starttls.enable", false);
            }
            
            
            // Nombre del usuario
            props.setProperty("mail.smtp.user", this.getEmailOrigen());
            
            Session session = Session.getDefaultInstance(props);
            
            // Para obtener un log de salida mas extenso
            //session.setDebug(true);
            
            //CONSTRUIR EL MENSAJE
            MimeMessage message = new MimeMessage(session);
            
            // Se rellena el From
            message.setFrom(new InternetAddress(this.getEmailOrigen()));
            
            // Se rellena el subject
            message.setSubject(this.getAsunto());
            
            
            //CONSTRUIR UN MENSAJE COMPLEJO CON ADJUNTOS
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            
            // Texto del mensaje
            //messageBodyPart.setText(this.getMensaje());
            
            //String htmlText = "<H3>Hello</H3><img src=\"cid:image\">";
            
            messageBodyPart.setContent(this.getMensaje(), "text/html");
            
            File ruta_class = new File (SendEmailHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String parent_dir = ruta_class.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().toString();
            
            System.out.println("parent_dir: "+parent_dir);
            
            //Agregar imagen
            MimeBodyPart imagen1 =new MimeBodyPart();
            imagen1.attachFile(parent_dir+"/img/perseo/logo_perseo.png");
            imagen1.setHeader("Content-ID","<logo1>");  
            
            MimeBodyPart imagen2 =new MimeBodyPart();
            imagen2.attachFile(parent_dir+"/img/perseo/encryption.png");
            imagen2.setHeader("Content-ID","<logo2>"); 
            
            
            
            //JUNTAMOS AMBAS PARTES EN UNA SOLA
            MimeMultipart multiParte = new MimeMultipart("related");
            multiParte.addBodyPart(messageBodyPart);
            multiParte.addBodyPart(imagen1);
            multiParte.addBodyPart(imagen2);
            
            if(this.getAdjuntos().size()>0){
                //Adjuntar archivos
                for( LinkedHashMap<String,String> i : this.getAdjuntos() ){
                    BodyPart adjunto = new MimeBodyPart();
                    
                    //Pasamos la ruta del archivo a adjuntar
                    adjunto.setDataHandler(new DataHandler(new FileDataSource(i.get("path_file"))));
                    
                    // Opcional. De esta forma transmitimos al receptor el nombre original del fichero
                    adjunto.setFileName(i.get("file_name"));
                    //adjunto.setFileName(adjunto.getFileName());
                    
                    multiParte.addBodyPart(adjunto);
                }
            }
            
            
            System.out.println("DESTINATARIOS...................................");
            if(this.getDestinatarios().size()>0){
                //Se cargan los destinatarios
                for( LinkedHashMap<String,String> i : this.getDestinatarios() ){
                    
                    System.out.println("type: "+i.get("type").toUpperCase() + "            recipient:"+ i.get("recipient"));
                    
                    if(i.get("type").toUpperCase().trim().equals("TO")){
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(i.get("recipient")));
                    }
                    if(i.get("type").toUpperCase().trim().equals("CC")){
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(i.get("recipient")));
                    }
                    //Copia oculta
                    if(i.get("type").toUpperCase().trim().equals("BCC")){
                        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(i.get("recipient")));
                    }
                }
            }
            
            /*
            System.out.println("getAsunto: "+this.getAsunto());
            System.out.println("getContrasenia: "+this.getContrasenia());
            System.out.println("getEmailDestino: "+this.getEmailDestino());
            System.out.println("getEmailOrigen: "+this.getEmailOrigen());
            System.out.println("getHost: "+this.getHost());
            System.out.println("getMensaje: "+this.getMensaje());
            System.out.println("getPuerto: "+this.getPuerto());
            System.out.println("getTls: "+this.getTls());
            */
            
            // Se mete el texto y la foto adjunta.
            message.setContent(multiParte);
            
            //ENVIAR EL MENSAJE
            Transport t = session.getTransport("smtp");
            
            // Aqui usuario y password
            t.connect(this.getEmailOrigen(), this.getContrasenia());
            
            t.sendMessage(message,message.getAllRecipients());
            
            t.close();
            
        }catch (Exception ex) {
            ex.printStackTrace();
            retorno="No fue posible eviar el correo. ["+ ex.getMessage() +"]" ;
        }
        
        return retorno;
    }
}
