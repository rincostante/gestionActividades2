/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Orientacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrientacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbOrientacion implements Serializable{

    private Orientacion current;
    private List<Orientacion> listado;
    private List<Orientacion> listFilter;    
    private List<SubPrograma> listSubpFilter;
    private boolean iniciado;    
    
    @EJB
    private OrientacionFacade orientacionFacade;
    
    public MbOrientacion() {
    }
    
    @PostConstruct
    public void init() {
        iniciado = false;
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
                    if(!s.equals("mbOrientacion") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }        
    
    /*********************
     ** Geters y Seters **
     *********************/    
    public Orientacion getCurrent() {
        return current;
    }

    public void setCurrent(Orientacion current) {
        this.current = current;
    }

    public List<Orientacion> getListado() {
        
        if (listado == null) {
            listado = getFacade().findAll();
        }        
        
        return listado;
    }

    public void setListado(List<Orientacion> listado) {
        this.listado = listado;
    }

    public List<Orientacion> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Orientacion> listFilter) {
        this.listFilter = listFilter;
    }

    public List<SubPrograma> getListSubpFilter() {
        return listSubpFilter;
    }

    public void setListSubpFilter(List<SubPrograma> listSubpFilter) {
        this.listSubpFilter = listSubpFilter;
    }
    
    
    /*******************************
     ** Métodos para la selección **
     *******************************/
    /**
     * @return La entidad gestionada
     */
    public Orientacion getSelected() {
        if (current == null) {
            current = new Orientacion();
        }
        return current;
    }       
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Orientacion getOrientacion(java.lang.Long id) {
        return orientacionFacade.find(id);
    }       

    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
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
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Orientacion();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        return "edit";
    }  
    
    /**
     * Método que verifica que el Orientación que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("OrientacionNonDeletable"));
        }
        return "view";
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        if(listado != null){
            listado.clear();
            listado = null;
        }
        if(listSubpFilter != null){
            listSubpFilter = null;
        }
        if(listFilter != null){
            listFilter = null;
        }
    }    
    
    /******************
     ** Validaciones **
     ******************/    
    
    /**
     * Método para validar que no exista ya una entidad con este nombre al momento de crearla
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarInsert(FacesContext arg0, UIComponent arg1, Object arg2){
        validarExistente(arg2);
    }
    
    /**
     * Método para validar que no exista una entidad con este nombre, siempre que dicho nombre no sea el que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdate(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarExistente(arg2);
        }
    }
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateOrientacionNombreExistente")));
        }
    } 
    
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return 
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrientacionCreated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrientacionCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrientacionUpdated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrientacionUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        performDestroy();
        recreateModel();
        return "view";
    }    
    
    /**
     * Método para mostrar los Subprogramas con esta Orientación
     */
    public void verSubprogramas(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 1200);
        RequestContext.getCurrentInstance().openDialog("dlgSubprogramas", options, null);
    }     
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private OrientacionFacade getFacade() {
        return orientacionFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrientacionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrientacionDeletedErrorOccured"));
        }
    }
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Orientacion.class)
    public static class OrientacionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbOrientacion controller = (MbOrientacion) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbOrientacion");
            return controller.getOrientacion(getKey(value));
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

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Orientacion) {
                Orientacion o = (Orientacion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Orientacion.class.getName());
            }
        }
    }        
}
