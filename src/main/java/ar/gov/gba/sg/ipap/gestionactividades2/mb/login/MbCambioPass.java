/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.login;

import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Correo;
import ar.gov.gba.sg.ipap.gestionactividades2.util.CorreoEnvios;
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
import javax.servlet.http.HttpSession;


public class MbCambioPass implements Serializable{
    
    private String claveAnterior;
    private String claveNueva_1;
    private String claveNueva_2;
    @EJB
    private UsuarioFacade usuarioFacade;
    private Usuario usLogeado;
    private MbLogin login;  

    /**
     * Creates a new instance of MbCambioPass
     */
    public MbCambioPass() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
    }

    public String getClaveNueva_2() {
        return claveNueva_2;
    }

    public void setClaveNueva_2(String claveNueva_2) {
        this.claveNueva_2 = claveNueva_2;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public String getClaveAnterior() {
        return claveAnterior;
    }

    public void setClaveAnterior(String claveAnterior) {
        this.claveAnterior = claveAnterior;
    }

    public String getClaveNueva_1() {
        return claveNueva_1;
    }

    public void setClaveNueva_1(String claveNueva_1) {
        this.claveNueva_1 = claveNueva_1;
    }
    
    /**
     * Método para la actualización de la contraseña
     * @return 
     */
    public String update(){
        String claveEncriptada = CriptPass.encriptar(claveNueva_2);
        try {
            usLogeado.setCalve(claveEncriptada);
            if(usLogeado.isPrimeraVez()){
                usLogeado.setPrimeraVez(false);
                usuarioFacade.edit(usLogeado);
                
                /**
                 * temporalmente volvemos a mostrar la clave generada hasta resolver el problema del envío de correos
                 * rincostante 20150702
                if(!enviarCorreo()){
                    JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario. Consulte el log del servidor.");
                    return null;
                }
                 */

                JsfUtil.addSuccessMessage("Contraseña actualizada con exito");
                return "inicio";   
            }else{
                usuarioFacade.edit(usLogeado);
                if(!enviarCorreo()){
                    JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario. Consulte el log del servidor.");
                    return null;
                }else{
                    JsfUtil.addSuccessMessage("Contraseña actualizada con exito");
                    return "viewDatos";   
                }
            }
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
        return "/actores/usuario/editDatos";
    }
    
    /**
     * Método para validar que la clave a modificar estrita por primera vez, sea la que corresponda
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveVieja(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String claveEncriptada = CriptPass.encriptar((String)arg2); 
        if(!usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaInvalida")));
        }
    }    

    /**
     * Método para validar que la clave nueva no esté siento utilizada ni sea igual a la anterior
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveNueva(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        int i = 0;
        int num = 0;        
        String pass = (String)arg2;
        String claveEncriptada = CriptPass.encriptar(pass); 
        if(usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaIgual")));
        }else{
            if(!usuarioFacade.verificarContrasenia(claveEncriptada)){
                throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaExistente")));
            }else{
                if(pass.length() == 8){
                    while(i < pass.length()){
                        if(pass.charAt(i) > 47 && pass.charAt(i) < 58){
                            num ++;
                        }
                        i ++;
                    }

                    if(num < 2){
                        throw new ValidatorException(new FacesMessage("La contraseña debe incluir al menos 2 números."));
                    }else{
                        claveNueva_1 = (String)arg2;
                    }
                }else{
                    throw new ValidatorException(new FacesMessage("La contraseña debe tener 8 dígitos."));
                }
            }
        }
    }
    
    /**
     * Método para validar que la clave repetida sea igual a la primera
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveNueva_2(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(claveNueva_1 != null){
            if(!claveNueva_1.equals((String)arg2)){
                throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContrasenia2Distinta")));
            }   
        }
    }    
    
    /**
     * Método para salir del formulario de modificación de contraseña
     * @return 
     */
    public String salir(){
        claveAnterior = "";
        claveNueva_1 = "";
        claveNueva_2 = "";
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbCambioPass");
    
        return "inicio";
    }
    
    private boolean enviarCorreo(){
        String correo;
        String nombre;      
        String bodyMessage;
        Correo c = new Correo();
        CorreoEnvios envio = new CorreoEnvios();
        
        if(usLogeado.getAgente() != null){
            correo = usLogeado.getAgente().getPersona().getEmail_1();
            nombre = usLogeado.getAgente().getPersona().getNombres() + " " + usLogeado.getAgente().getPersona().getApellidos();
        }else{
            correo = usLogeado.getDocente().getPersona().getEmail_1();
            nombre = usLogeado.getDocente().getPersona().getNombres() + " " + usLogeado.getDocente().getPersona().getApellidos();
        }
        
        bodyMessage = "<p>Estimado/a " + nombre + "</p> "
                + "<p>Ha cambiado su contraseña de acceso al Sistema " + ResourceBundle.getBundle("/Bundle").getString("Aplicacion") + ". Sus nuevas credenciales son:</p> "
                + "<p><strong>usuario:</strong> " + usLogeado.getNombre() + "<br/> "
                + "<strong>contraseña:</strong> " + claveNueva_2 + "</p> "
                + "<p>Por favor, no responda este correo. No divulgue ni comparta sus credenciales de acceso.</p> "
                + "<p>Saludos cordiales</p> "
                + "<p>Instituto Provincial de la Administración Pública<br/> "
                + "Subsecretaría para la Modernización del Estado<br/> "
                + "Calle 12 y 53 - Torre Gubernamental II - Piso 11. Código Postal 1900. La Plata, Provincia de Buenos Aires, República Argentina<br/> "
                + "Teléfono: (0221) 429 5574/76<br /> "
                + "Correo electrónico: <a href=\"mailto:privadaipap@ipap.sg.gba.gov.ar\">privadaipap@ipap.sg.gba.gov.ar</a></p>";        
        
        c.setContrasenia("usgpxriehulvqxmz");
        c.setUsuarioCorreo("gestionipap@gmail.com");
        c.setAsunto(ResourceBundle.getBundle("/Bundle").getString("Aplicacion") + ": Actualización de contraseña de acceso al Sistema");
        c.setMensaje(bodyMessage);
        c.setDestino(correo);

        return envio.enviarCorreo(c);
    }    
}
