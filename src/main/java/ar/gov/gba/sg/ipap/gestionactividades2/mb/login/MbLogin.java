/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.login;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.util.CriptPass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private String clave;
    private boolean logeado = false;   
    private String ambito;
    private Usuario usLogeado;
    private String claveAnterior_1;
    private String claveAnterior_2;
    private String claveNueva;
    @EJB
    private UsuarioFacade usuarioFacade;
    private List<String> listMbActivos;
    
    /**
     * Creates a new instance of MbLogin
     */
    public MbLogin() {
    }
    
    /**
     * Método para inicializar el listado de los Mb activos
     */
    @PostConstruct
    public void init(){
        listMbActivos = new ArrayList();
    }

    public List<String> getListMbActivos() {
        return listMbActivos;
    }

    public void setListMbActivos(List<String> listMbActivos) {
        this.listMbActivos = listMbActivos;
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

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String rol) {
        this.ambito = rol;
    }
    
    public void login(ActionEvent actionEvent){
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;

        if (nombre != null && clave != null){
            // valido las credenciales recibidas
            if(validar()){
                logeado = true;

                if(usLogeado.getRol().getNombre().equals("Administrador")
                        || usLogeado.getRol().getNombre().equals("Coordinador")
                        || usLogeado.getRol().getNombre().equals("Supervisor")){
                    ambito = "admin";
                }else{
                    ambito = "externo";
                }
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", usLogeado.getApYNom());
            }else{
                logeado = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de sesión", "Usuario y/o contraseña invalidos");
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("estaLogeado", logeado);
        clave = "";
        
        if (logeado){
            context.addCallbackParam("view", ResourceBundle.getBundle("/Bundle").getString("RutaAplicacion"));
        }
    }
    
    public void logout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        logeado = false;
    }  
    
    private boolean validar(){
        String claveEnc = CriptPass.encriptar(clave);
        usLogeado = usuarioFacade.validar(nombre, claveEnc);
        return usLogeado.getId() != null;
    }
    
    private boolean validarInt(){
        return "admin".equals(nombre) && "admin".equals(clave);
    }

}
