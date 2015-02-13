/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbTitulo implements Serializable{

    private Titulo current;
    private DataModel items = null;
    
    @EJB
    private TituloFacade tituloFacade;
    private int selectedItemIndex;
    private String selectParam; 
    private List<String> listaNombres;    
    private MbLogin login; 
    
    /**
     * Creates a new instance of MbTitulo
     */
    public MbTitulo() {
    }
    
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Titulo getSelected() {
        if (current == null) {
            current = new Titulo();
            selectedItemIndex = -1;
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
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
        current = (Titulo) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Titulo();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (Titulo) getItems().getRowData();
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
        buscarTitulo();
        return "list";
    }
    
    /**
     * Método que verifica que el Título que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = (Titulo) getItems().getRowData();
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TituloNonDeletable"));
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
   
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            if(getFacade().noExiste(current.getNombre(), current.getEpedidoPor())){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TituloCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TituloExistentes"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TituloCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Titulo titulo;
        try {
            titulo = getFacade().getExistente(current.getNombre(), current.getEpedidoPor());
            if(titulo == null){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TituloUpdated"));
                return "view";
            }else{
                if(titulo.getId().equals(current.getId())){
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TituloUpdated"));
                    return "view";
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("TituloExistentes"));
                    return null;   
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TituloUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = (Titulo) getItems().getRowData();
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
        return JsfUtil.getSelectItems(tituloFacade.findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(tituloFacade.findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Titulo getTitulo(java.lang.Long id) {
        return tituloFacade.find(id);
    }    
    
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbTitulo");

        return "inicio";
    } 
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private TituloFacade getFacade() {
        return tituloFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TituloDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TituloDeletedErrorOccured"));
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
    
    private void buscarTitulo(){
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
    @FacesConverter(forClass = Titulo.class)
    public static class TituloControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbTitulo controller = (MbTitulo) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbTitulo");
            return controller.getTitulo(getKey(value));
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
            if (object instanceof Titulo) {
                Titulo o = (Titulo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Titulo.class.getName());
            }
        }
    }                   
}
