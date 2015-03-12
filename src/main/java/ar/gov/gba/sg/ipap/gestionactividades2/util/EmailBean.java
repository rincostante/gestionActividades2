/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.util;

import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author rincostante
 */
@Stateless
public class EmailBean {

    @Resource(name = "mail/gestionipap")
    private Session mailSession;
    
    public void sendMessage(String email, String subject, String bodyMessage) throws MessagingException{
        Message message = new MimeMessage(mailSession);
        try{
            message.setSubject(subject);
            message.setRecipient(RecipientType.TO, new InternetAddress(email)); 
            message.setText(bodyMessage); 
            message.setContent(bodyMessage, "text/html; charset=utf-8");
            Date timeStamp = new Date();
            message.setSentDate(timeStamp);
            
            Transport.send(message);
            
        }catch(MessagingException ex){
            throw ex;
        }
    }
}
