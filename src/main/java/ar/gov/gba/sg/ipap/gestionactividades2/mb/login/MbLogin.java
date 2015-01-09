/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.login;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbLogin implements Serializable{
    
    private static final long serialVersionUID = -2152389656664659476L;
    private String nombre;
    private static final String persona = "Rubén Incostante";
    private String clave;
    private boolean logeado = false;   
    private String rol;
    
    /**
     * Creates a new instance of MbLogin
     */
    public MbLogin() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public String getPersona() {
        return persona;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public void login(ActionEvent actionEvent){
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        
        if (nombre != null && clave != null){
            if (nombre.equals(clave)){
                logeado = true;
                if ("admin".equals(nombre)){
                    rol = "admin";
                }
                else{
                    rol = "usuario";
                }
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", nombre);
            }else{
                logeado = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de sesión", "Usuario y/o contraseña invalidos");
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("estaLogeado", logeado);
        
        if (logeado){
            context.addCallbackParam("view", ResourceBundle.getBundle("/Bundle").getString("RutaAplicacion"));
        }
    }
    
    public void logout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        logeado = false;
    }    
}
