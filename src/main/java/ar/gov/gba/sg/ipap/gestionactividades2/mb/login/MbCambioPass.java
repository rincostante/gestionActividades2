/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.login;

import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.util.CriptPass;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;


public class MbCambioPass implements Serializable{
    
    private String claveAnterior_1;
    private String claveAnterior_2;
    private String claveNueva;
    @EJB
    private UsuarioFacade usuarioFacade;
    private Usuario usLogeado;

    /**
     * Creates a new instance of MbCambioPass
     */
    public MbCambioPass() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        MbLogin login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public String getClaveAnterior_1() {
        return claveAnterior_1;
    }

    public void setClaveAnterior_1(String claveAnterior_1) {
        this.claveAnterior_1 = claveAnterior_1;
    }

    public String getClaveAnterior_2() {
        return claveAnterior_2;
    }

    public void setClaveAnterior_2(String claveAnterior_2) {
        this.claveAnterior_2 = claveAnterior_2;
    }
    
    /**
     * Método para la actualización de la contraseña
     * @return 
     */
    public String update(){
        String claveEncriptada = CriptPass.encriptar(claveNueva);
        try {
            usLogeado.setCalve(claveEncriptada);
            usuarioFacade.edit(usLogeado);
            JsfUtil.addSuccessMessage("Contraseña actualizada con exito");
            return "viewDatos";            
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Hubo un error actualizando la contraseña");
            return null;
        }
    }

    
    /**
     * Método para redireccionar al cambio de contraseña
     * @return 
     */
    public String prepareCambioClave(){
        return "/seguridad/usuario/editDatos";
    }
    
    /**
     * Método para validar que la clave a modificar estrita por primera vez, sea la que corresponda
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveVieja_1(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String claveEncriptada = CriptPass.encriptar((String)arg2); 
        if(!usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaInvalida")));
        }
    }    
    
    /**
     * Método para validar que la clave repetida sea igual a la primera
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveVieja_2(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String claveEncriptada = CriptPass.encriptar((String)arg2); 
        if(!usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContrasenia2Distinta")));
        }
    }

    /**
     * Método para validar que la clave nueva no esté siento utilizada ni sea igual a la anterior
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveNueva(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String claveEncriptada = CriptPass.encriptar((String)arg2); 
        if(usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaIgual")));
        }else{
            if(!usuarioFacade.verificarContrasenia(claveEncriptada)){
                throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaExistente")));
            }
        }
    }
    
    /**
     * Método para salir del formulario de modificación de contraseña
     * @return 
     */
    public String salir(){
        claveAnterior_1 = "";
        claveAnterior_2 = "";
        claveNueva = "";
        return "inicio";
    }    
}
