/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.NivelIpapFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbNivelIpap implements Serializable{

    private NivelIpap current;
    private DataModel items = null;
    
    @EJB
    private NivelIpapFacade estudiosCursadosFacade;
    private int selectedItemIndex;
    private String selectParam; 
    private List<String> listaNombres;    
    private Map<String,String> estados;    
    private MbLogin login; 
    /**
     * Creates a new instance of MbNivelIpap
     */
    public MbNivelIpap() {
    }
    
    public Map<String, String> getEstados() {
        return estados;
    }

    public void setEstados(Map<String, String> estados) {
        this.estados = estados;
    }    
    
   @PostConstruct
    public void init(){
        estados  = new HashMap<>();
        estados.put("Incompleto", "Incompleto");
        estados.put("En Curso", "En Curso");
        estados.put("Finalizado", "Finalizado");        
    }  
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public NivelIpap getSelected() {
        if (current == null) {
            current = new NivelIpap();
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
        current = (NivelIpap) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new NivelIpap();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (NivelIpap) getItems().getRowData();
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
        buscarNivelIpap();
        return "list";
    }
    
    /**
     * Método que verifica que el Nivel Ipap que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = (NivelIpap) getItems().getRowData();
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NivelIpapNonDeletable"));
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
            if(getFacade().noExiste(current.getNombre(), current.getEstado())){
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NivelIpapCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NivelIpapExistentes"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("NivelIpapCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        NivelIpap nivIpap;
        try {
            nivIpap = getFacade().getExistente(current.getNombre(), current.getEstado());
            if(nivIpap == null){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosUpdated"));
                return "view";
            }else{
                if(nivIpap.getId().equals(current.getId())){
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosExistentes"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = (NivelIpap) getItems().getRowData();
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
        return JsfUtil.getSelectItems(estudiosCursadosFacade.findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(estudiosCursadosFacade.findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public NivelIpap getNivelIpap(java.lang.Long id) {
        return estudiosCursadosFacade.find(id);
    }    
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbNivelIpap");

        return "inicio";
    }   
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private NivelIpapFacade getFacade() {
        return estudiosCursadosFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NivelIpapDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("NivelIpapDeletedErrorOccured"));
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
    
    private void buscarNivelIpap(){
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
    @FacesConverter(forClass = NivelIpap.class)
    public static class NivelIpapControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbNivelIpap controller = (MbNivelIpap) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbNivelIpap");
            return controller.getNivelIpap(getKey(value));
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
            if (object instanceof NivelIpap) {
                NivelIpap o = (NivelIpap) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + NivelIpap.class.getName());
            }
        }
    }               
}
