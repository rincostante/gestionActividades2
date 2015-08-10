/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.misInscripciones;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstadoParticipante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstadoParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbMisInscripciones implements Serializable{
    
    private Participante current;
    private List<Participante> listado;
    private List<Participante> listadoFilter;
    private List<Clase> listClases;
    private List<Clase> listClasesFilter;
    private Agente referente;
    private List<String> listEstPart;
    
    @EJB
    private ParticipanteFacade participanteFacade;
    
    @EJB
    private EstadoParticipanteFacade estPartFacade;    
    
    private Usuario usLogeado; 
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;
    private boolean iniciado;
    private int tipoList; //1=vigentes | 2=vencidos

    /**
     * Creates a new instance of MbMisInscripciones
     */
    public MbMisInscripciones() {
    }
    
    /**
     * Método para la inicialización de la clase
     */
    @PostConstruct
    public void init(){
        tipoList = 1;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        referente = login.getUsLogeado().getAgente();
        usLogeado = login.getUsLogeado();
        iniciado = false;
        listEstPart = new ArrayList();
        List<EstadoParticipante> estPart = estPartFacade.findAll();
        for (EstadoParticipante estado : estPart) {
            listEstPart.add(estado.getNombre());
        }
    }
    
    /**
     * Geters y Seters
     */
    
    public List<String> getListEstPart() {
        return listEstPart;
    }

    public void setListEstPart(List<String> listEstPart) {
        this.listEstPart = listEstPart;
    }

    
    public Participante getCurrent() {
        return current;
    }

    public void setCurrent(Participante current) {
        this.current = current;
    }

    public List<Participante> getListado() {
        if (listado == null) {
            switch(tipoList){
                case 1: listado = getFacade().getVigentesXReferente(referente);
                    break;
                default: listado = getFacade().getVencidosXReferente(referente);
            }
        }
        return listado;
    }

    public void setListado(List<Participante> listado) {
        this.listado = listado;
    }

    public List<Participante> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<Participante> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }

    public List<Clase> getListClases() {
        return listClases;
    }

    public void setListClases(List<Clase> listClases) {
        this.listClases = listClases;
    }

    public List<Clase> getListClasesFilter() {
        return listClasesFilter;
    }

    public void setListClasesFilter(List<Clase> listClasesFilter) {
        this.listClasesFilter = listClasesFilter;
    }

    public Date getfAntesDe() {
        return fAntesDe;
    }

    public void setfAntesDe(Date fAntesDe) {
        this.fAntesDe = fAntesDe;
    }

    public Date getfDespuesDe() {
        return fDespuesDe;
    }

    public void setfDespuesDe(Date fDespuesDe) {
        this.fDespuesDe = fDespuesDe;
    }

    
    /**
     * Método que borra de la memoria los MB innecesarios al cargar el listado 
     */
    public void iniciar(){
        if(!iniciado){
            String s;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(true);
            Enumeration enume = session.getAttributeNames();
            while(enume.hasMoreElements()){
                s = (String)enume.nextElement();
                if(s.substring(0, 2).equals("mb")){
                    if(!s.equals("mbMisInscripciones") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    
    
    
    /**
     * @return La entidad gestionada
     */
    public Participante getSelected() {
        if (current == null) {
            current = new Participante();
        }
        return current;
    }    
    
    
    /**
     * Método para inicializar el listado de los Participantes autorizados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        tipoList = 1;
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de las Inscripciones vencidas
     * @return acción para el listado de entidades
     */
    public String prepareListVenc() {
        tipoList = 2;
        recreateModel();
        return "listVenc";
    }       
       
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewVenc() {
        return "viewVenc";
    }  
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewProv() {
        return "viewProv";
    }        
    
    
    /**
     *
     * @return
     */
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
  
    
    /**
     * 
     * @return 
     */
    public String prepareAutorizar(){
        List<EstadoParticipante> estParts = estPartFacade.getXString("Inscripto");
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setEstado(estParts.get(0));

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitadaErrorOccured"));
            return null; 
        }
    }        
    
    /**
     * 
     */
    public void resetFechas(){
        fDespuesDe = null;
        fAntesDe = null;
    }        
    
    
   /*********************************************
     ** Métodos de inicialización de búsquedas **
     ********************************************/
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectHab(){
        buscarEntreFechas();
        return "list";
    }
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectVenc(){
        buscarEntreFechas();
        return "listVenc";
    } 
    

    /*************************
    ** Métodos de operación **
    **************************/
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Participante getParticipante(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbMisInscripciones");
     
        return "inicio";
    }      
    
    public void verClases(){
        listClases = current.getClases();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgClases", options, null);
    }      

    public void preProcessPDF(Object document) throws DocumentException, IOException {
        Document pdf = (Document) document;    
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.newPage();
    }
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ParticipanteFacade getFacade() {
        return participanteFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        if(listado != null){
            listado.clear();
            listado = null;
        }
        if(listadoFilter != null){
            listadoFilter = null;
        }
        if(listClasesFilter != null){
            listClasesFilter = null;
        }
    }   
    
    private void buscarEntreFechas(){
        List<Participante> parts = new ArrayList();
        for (Participante part : listado) {
            if(part.getActividad().getFechaInicio().after(fDespuesDe) && part.getActividad().getFechaInicio().before(fAntesDe)){
                parts.add(part);
            }
        }        
        listado = parts; 
    }     
    
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    
    @FacesConverter(forClass = Participante.class)
    public static class InscripcionesControllerConverter implements Converter {

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
            MbMisInscripciones controller = (MbMisInscripciones) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbMisInscripciones");
            return controller.getParticipante(getKey(value));
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
            if (object instanceof Participante) {
                Participante o = (Participante) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Participante.class.getName());
            }
        }
    }                 
}
