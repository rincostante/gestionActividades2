/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.inicio;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstadoParticipante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstadoParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private List<ActividadImplementada> listadoFilter;  
    private String mensaje;
    private String respuesta;
    private int registra; // 0=no puede registrar | 1=registra | 2=registrado
    private boolean resultado; //0=exitoso | 1=error
    
    @EJB
    private ActividadImplementadaFacade cursoFacade;
    @EJB
    private EstadoParticipanteFacade estPartFacade;
    @EJB
    private ParticipanteFacade partFacade;

    /**
     * Creates a new instance of MbCursosPublic
     */
    public MbCursosPublic() {
    }
    
    /**
     * Método para la inicialización de la clase
     */
    @PostConstruct
    public void init(){
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
    
    public List<ActividadImplementada> getListadoFilter(){
        return listadoFilter;
    }

    public void setListadoFilter(List<ActividadImplementada> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }

    
    public boolean isResultado(){
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    
    public int getRegistra(){
        return registra;
    }

    public void setRegistra(int registra) {
        this.registra = registra;
    }

    
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

    public DataModel getItems(){
        if(items == null){
            items = new ListDataModel(getFacade().getHabilitadas());
        }
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
     * Método para inicializar el listado de las Actividades
     * @return acción para el listado de entidades
     */
    public String prepareList() {  
        recreateModel();
        return "index";
    }
    
    /**
     * Métodos de alistamientos previos a operaciones 
     */
    public void prepareInscripcion(){
        resultado = false;
        if(agente != null){
            part = getFacade().getParticipacion(current, agente);
            if(part == null){
                // verifico si el agente además es un docente
                if(usLogeado.getRol().getNombre().equals("Docente")){
                    // si es docente verifico que no lo sea de esta actividad
                    listAct = getFacade().getHabilitadasXDocente(agente.getDocente());
                    if(listAct.contains(current)){
                        mensaje = "No es posible registrar esta inscripción dado que usted se encuentra registrado como docente de la Actividad. "
                                + "¡Muchas gracias por su participación!";
                        registra = 0;
                    }else{
                        mensaje = "Para registrar su inscripción, "
                                + "por favor, haga clic en el botón 'Registrar inscripción', o en 'Cancelar' para cancelar el proceso de inscripción.";
                        
                        registra = 1;
                    }
                }
                // Si no es docente, lo registro
                mensaje = "Para registrar su inscripción, "
                        + "por favor, haga clic en el botón 'Registrar inscripción', o en 'Cancelar' para cancelar el proceso de inscripción.";
                
                registra = 1;
            }else{
                if(part.getEstado().getNombre().equals("Provisorio")){
                    mensaje = "Regitramos a su favor una inscripción, "
                            + "con fecha " + part.getAdmin().getStrFechaAlta() + ", la misma continúa a la espera de la validación "
                            + "de su Referente. ¡Muchas gracias por su participación!";
                    
                    registra = 2;
                }else{
                    if(!part.getAdmin().isHabilitado()){
                        mensaje = "Registramos a su favor una inscripción deshabilitada con fecha: " + part.getAdmin().getStrFechaBaja() + ", "
                                + "para volver a habilitarla, contacte a su Referente, por favor. "
                                + "¡Muchas gracias por su participación!";
                        
                        registra = 0;
                    }else{
                        mensaje = "Registramos a su favor una inscripción vigente, "
                                + "validada por su Referente con fecha " + part.getStrFechaAutoriz() + ". "
                                + "Lo esperamos. ¡Muchas gracias por su participación!";
                        
                        registra = 2;
                    }
                }
            }
        }
        if(docente != null){
            mensaje = "La inscripción a Actividades formativas solo es posible para los Agentes de la Administración Pública provincial "
                    + "registrados, si Ud. es Agentes de la Administración Pública y no se encuentra registrado, puede hacerlo solicitandolo"
                    + "a su referente por las vías administrativas correspondientes. ¡Muchas gracias por su participación!";
            registra = 0;
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
        Participante participante = new Participante();
        Date date = new Date(System.currentTimeMillis());
        try{
            // asigno los atributos principales de la inscripción
            participante.setActividad(current);
            participante.setAgente(agente);
            
            // asigno la entidad administrativa
            AdmEntidad admEnt = new AdmEntidad();
            admEnt.setFechaAlta(date);
            admEnt.setHabilitado(true);
            admEnt.setUsAlta(usLogeado);
            participante.setAdmin(admEnt);

            // asigo el estado: Provisorio
            List<EstadoParticipante> estParts = estPartFacade.getXString("Provisorio");
            participante.setEstado(estParts.get(0));
            
            // inserto
            partFacade.create(participante);
            
        resultado = false;
        respuesta = "Se ha registrado su inscripción con éxito, "
                + "la misma se encuentra en estado provisoria, a la espera de ser validada por su Referente. "
                + "¡Muchas gracias por su participación!";
            
        }catch (Exception e) {
            resultado = true;
            respuesta = "Hubo un error registrando la inscripción: " + e.getMessage() + ". Contacte al Administrador "
                    + "¡Muchas gracias por su participación!";
        }
    }
    
    /**
     * 
     */
    public void eliminarInscripcion(){
        Date date = new Date(System.currentTimeMillis());
        try{

            // actualizo los datos de administración de la entidad
            part.getAdmin().setFechaBaja(date);
            part.getAdmin().setUsBaja(usLogeado);
            part.getAdmin().setHabilitado(false);
            
            // actualizo la inscripción
            partFacade.edit(part);
            
            resultado = false;
            respuesta = "Se ha eliminado su inscripción con éxito, "
                    + "lo esperamos en otra oportunidad. "
                    + "¡Muchas gracias por su participación!";
            
        }catch (Exception e) {
            resultado = true;
            respuesta = "Hubo un error eliminado la inscripción: " + e.getMessage() + ". Contacte al Administrador "
                    + "¡Muchas gracias por su participación!";
        }
    }
    
    /**
     * 
     */
    public void cerrar(){
        RequestContext.getCurrentInstance().closeDialog("dlgInscrip");
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
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
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
