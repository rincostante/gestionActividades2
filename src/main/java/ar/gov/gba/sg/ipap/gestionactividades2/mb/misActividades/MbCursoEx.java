/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.misActividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
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
public class MbCursoEx implements Serializable{
    
    private ActividadImplementada current;
    private List<ActividadImplementada> listado;
    private List<ActividadImplementada> listadoFilter;
    private List<Clase> listClases;
    private List<Clase> listClasesFilter;
    
    @EJB
    private ActividadImplementadaFacade actImpFacade;
    
    private Docente docente;
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;          
    private boolean iniciado;  

    /**
     * Creates a new instance of MbCursoEx
     */
    public MbCursoEx() {
    }
    
    /**
     * Método para la inicialización de la clase
     */
    @PostConstruct
    public void init(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        docente = login.getUsLogeado().getDocente();
        iniciado = false;
    }

    /**
     * Geters y Seters
     */
    public ActividadImplementada getCurrent() {
        return current;
    }

    public void setCurrent(ActividadImplementada current) {
        this.current = current;
    }

    public List<ActividadImplementada> getListado() {
        if (listado == null){
            listado = getFacade().getHabilitadasXDocente(docente);
        }
        return listado;
    }

    public void setListado(List<ActividadImplementada> listado) {
        this.listado = listado;
    }

    public List<ActividadImplementada> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<ActividadImplementada> listadoFilter) {
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
                    if(!s.equals("mbCursoEx") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }        
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/    
    
    /**
     * @return La entidad gestionada
     */
    public ActividadImplementada getSelected() {
        if (current == null) {
            current = new ActividadImplementada();
        }
        return current;
    }        
    
    /**
     * Método para inicializar el listado de las Actividad Implementadas habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        recreateModel();
        return "list";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }
    
    /**
     *
     * @return
     */
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
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
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public ActividadImplementada getActividadImplementada(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    
    /******************************
     *** Métodos de operaciones ***
     ******************************/
    
    /**
     * 
     */
    public void resetFechas(){
        fDespuesDe = null;
        fAntesDe = null;
    }             
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbCursoEx");
     
        return "inicio";
    }          
    
    /**
     * 
     */
    public void verClases(){
        listClases = getFacade().getClasesXActDocente(current, docente);
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
    private ActividadImplementadaFacade getFacade() {
        return actImpFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listado.clear();
        listado = null;
    }       
    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<ActividadImplementada> actImpls = new ArrayList();
        for (ActividadImplementada actImp : listado) {
            if(actImp.getFechaInicio().after(fDespuesDe) && actImp.getFechaInicio().before(fAntesDe)){
                actImpls.add(actImp);
            }
        }        
        listado = actImpls; 
    }         
    
    
   /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = ActividadImplementada.class)
    public static class ActividadImplementadaControllerConverter implements Converter {

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
            MbCursoEx controller = (MbCursoEx) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbCursoEx");
            return controller.getActividadImplementada(getKey(value));
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
