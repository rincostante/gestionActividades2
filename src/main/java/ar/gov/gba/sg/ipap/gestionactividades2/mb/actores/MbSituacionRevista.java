/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.SituacionRevistaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Administrador
 */
public class MbSituacionRevista implements Serializable{
    
    private SituacionRevista current;
    private DataModel items = null;
    
    @EJB
    private SituacionRevistaFacade situacionRevistaFacade;
    //private PaginationHelper pagination;
    private int selectedItemIndex;
    private String selectParam; 
    private List<String> listaNombres;     
    private MbLogin login;  

    /** Creates a new instance of MbSituacionRevista */
    public MbSituacionRevista() {
    }   
    
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public SituacionRevista getSelected() {
        if (current == null) {
            current = new SituacionRevista();
            selectedItemIndex = -1;
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
            //items = getPagination().createPageDataModel();
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        recreateModel();
        return "list";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = (SituacionRevista) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new SituacionRevista();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (SituacionRevista) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "edit";
    }
    
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelect(){
        items = null;
        buscarSituacionRevista();
        return "list";
    }
    
    /**
     * Método que verifica que la Situación de Revista que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = (SituacionRevista) getItems().getRowData();
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaNonDeletable"));
        }
        return "view";
    }
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        if(selectParam != null){
            selectParam = null;
        }
    }    
    
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
     */
    public void validarUpdate(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarExistente(arg2);
        }
    }
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("CreateSituacionRevistaNombreExistente")));
        }
    }    
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaCreated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaUpdated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = (SituacionRevista) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return "view";
    }    
    
    /*************************
    ** Métodos de selección **
    **************************/
    /**
     * @return la totalidad de las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(situacionRevistaFacade.findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(situacionRevistaFacade.findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public SituacionRevista getSituacionRevista(java.lang.Long id) {
        return situacionRevistaFacade.find(id);
    }    
    

    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbSituacionRevista");

        return "inicio";
    }     
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private SituacionRevistaFacade getFacade() {
        return situacionRevistaFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SituacionRevistaDeletedErrorOccured"));
        }
    }

    /**
     * Actualiza el detalle de la entidad si la última se eliminó
     */
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    
    /*
     * Métodos de búsqueda
     */
    public String getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam;
    }
    
    private void buscarSituacionRevista(){
        items = new ListDataModel(getFacade().getXString(selectParam)); 
    }  
    
    /**
     * Método para llegar la lista para el autocompletado de la búsqueda de nombres
     * @param query
     * @return 
     */
    public List<String> completeNombres(String query){
        listaNombres = getFacade().getNombres();
        List<String> nombres = new ArrayList();
        Iterator itLista = listaNombres.listIterator();
        while(itLista.hasNext()){
            String nom = (String)itLista.next();
            if(nom.contains(query)){
                nombres.add(nom);
            }
        }
        return nombres;
    }    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = SituacionRevista.class)
    public static class SituacionRevistaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbSituacionRevista controller = (MbSituacionRevista) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbSituacionRevista");
            return controller.getSituacionRevista(getKey(value));
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
            if (object instanceof SituacionRevista) {
                SituacionRevista o = (SituacionRevista) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SituacionRevista.class.getName());
            }
        }
    }             
}
