/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.inicio;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbCursosPublic implements Serializable{
    
    private DataModel items;
    private ActividadImplementada current;
    private String ambito;
    private MbLogin login;
    private Agente agente;
    private Docente docente;
    private Usuario usLogeado;
    private Participante part;
    private List<ActividadImplementada> listAct;
    private String tipoUsuario;
    private String mensaje;
    private String respuesta;
    private boolean registra;
    private boolean registrado;
    
    @EJB
    private ActividadImplementadaFacade cursoFacade;

    /**
     * Creates a new instance of MbCursosPublic
     */
    public MbCursosPublic() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(true);
        Enumeration enume = session.getAttributeNames();
        while(enume.hasMoreElements()){
            System.out.println(enume.nextElement());
        }
        
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        agente = usLogeado.getAgente();
        docente = usLogeado.getDocente();
    }
    

    /*********************
     ** geters y seters **
     *********************/
    
    public String getRespuesta(){
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    
    public String getMensaje(){
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isRegistra() {
        return registra;
    }

    public void setRegistra(boolean registra) {
        this.registra = registra;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public DataModel getItems(){
        items = new ListDataModel(getFacade().getHabilitadas());
        return items;
    }
    
    public ActividadImplementada getCurrent() {
        return current;
    }

    public void setCurrent(ActividadImplementada current) {
        this.current = current;
    }

    public String getAmbito() {
        return ambito;
    }

    public MbLogin getLogin() {
        return login;
    }

    public void setLogin(MbLogin login) {
        this.login = login;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
    
    /**
     * Métodos de alistamientos previos a operaciones 
     */
    public void prepareInscripcion(){
        if(agente != null){
            tipoUsuario = "agente";
            part = getFacade().getParticipacion(current, agente);
            if(part == null){
                // verifico si el agente además es un docente
                if(usLogeado.getRol().getNombre().equals("Docente")){
                    // si es docente verifico que no lo sea de esta actividad
                    listAct = getFacade().getHabilitadasXDocente(agente.getDocente());
                    if(listAct.contains(current)){
                        mensaje = "No es posible registrar esta inscripción dado que usted se encuentra registrado como docente de la Actividad. "
                                + "¡Muchas gracias por su participación!";
                    }else{
                        mensaje = "Para registrar su inscripción, "
                                + "por favor, haga clic en el botón 'Registrar inscripción', o en 'Cancelar' para cancelar el proceso de inscripción.";
                        
                        respuesta = "Se ha registrado su inscripción, "
                                + "la misma se encuentra en estado provisoria, a la espera de ser validada por su Referente. "
                                + "¡Muchas gracias por su participación!";
                        registra = true;
                    }
                }
                // Si no es docente, lo registro
                mensaje = "Para registrar su inscripción, "
                        + "por favor, haga clic en el botón 'Registrar inscripción', o en 'Cancelar' para cancelar el proceso de inscripción.";
                
                respuesta = "Se ha registrado su inscripción, "
                        + "la misma se encuentra en estado provisoria, a la espera de ser validada por su Referente. "
                        + "¡Muchas gracias por su participación!";
                registra = true;
            }else{
                if(part.getEstado().getNombre().equals("Provisorio")){
                    mensaje = "Regitramos a su favor una inscripción, "
                            + "con fecha " + part.getAdmin().getStrFechaAlta() + ", la misma continúa a la espera de la validación "
                            + "de su Referente. ¡Muchas gracias por su participación!";
                }else{
                    mensaje = "Registramos a su favor una inscripción vigente, "
                            + "validada por su Referente con fecha " + part.getStrFechaAutoriz() + ". "
                            + "Lo esperamos. ¡Muchas gracias por su participación!";
                }
                registra = false;
            }
        }
        if(docente != null){
            mensaje = "La inscripción a Actividades formativas solo es posible para los Agentes de la Administración Pública provincial "
                    + "registrados, si Ud. es Agentes de la Administración Pública y no se encuentra registrado, puede hacerlo solicitandolo"
                    + "a su referente por las vías administrativas correspondientes. ¡Muchas gracias por su participación!";
            registra = false;
        }
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgInscrip", options, null); 
    }

    /**
     * Método para cancelar el registro de la inscripción
     */
    public void cancelar(){
        RequestContext.getCurrentInstance().closeDialog("dlgInscrip");
    }

    
    /**
     * Métodos de operaciones
     */
    public void registrarInscripcion(){
        FacesMessage msg = null;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Chagracia!!!", " Pepe");
        //FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
        //RequestContext.getCurrentInstance().closeDialog("dlgInscrip");
    }
          
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public ActividadImplementada getActividadImp(java.lang.Long id) {
        return getFacade().find(id);
    }      
    
    /*********************
    ** Métodos privados **
    **********************/
    private ActividadImplementadaFacade getFacade() {
        return cursoFacade;
    }      
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = ActividadImplementada.class)
    public static class ActividadPlanControllerConverter implements Converter {

        /**
         *
         * @param facesContext
         * @param component
         * @param value
         * @return
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbCursosPublic controller = (MbCursosPublic) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbCursosPublic");
            return controller.getActividadImp(getKey(value));
        }

        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ActividadImplementada) {
                ActividadImplementada o = (ActividadImplementada) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ActividadImplementada.class.getName());
            }
        }
    }                 
}
