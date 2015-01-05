/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoCapacitacion;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoCapacitacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
//import ar.gov.gba.sg.ipap.gestionactividades2.util.PaginationHelper;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbTipoCapacitacion implements Serializable{
    
    private TipoCapacitacion current;
    private DataModel items = null;
    
    @EJB
    private TipoCapacitacionFacade tipoCapacitacionFacade;
    //private PaginationHelper pagination;
    private int selectedItemIndex;
    private String selectParam;    

    /**
     * Creates a new instance of MbTipoCapacitacion
     */
    public MbTipoCapacitacion() {
    }
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public TipoCapacitacion getSelected() {
        if (current == null) {
            current = new TipoCapacitacion();
            selectedItemIndex = -1;
        }
        return current;
    }   
    
    /**
     * @return Paginación
     */
    /*
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(20) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize() - 1}));
                }
            };
        }
        return pagination;
    }
    */
    
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
    
     /**
     * @return avanza al paginado siguiente
     */
    /*
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "list";
    }
    */

    /**
     * @return retrocede al paginado anterior 
     */
    /*
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "list";
    }
    */
    
    
  
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
    
    public String iniciarList(){
        String redirect = "";
        if(selectParam != null){
            redirect = "list";
        }else{
            redirect = "administracion/tipoCapacitacion/list";
        }
        recreateModel();
        return redirect;
    }

    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = (TipoCapacitacion) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new TipoCapacitacion();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (TipoCapacitacion) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
        buscarTipoCapacitacion();
        return "list";
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
     * resetea la paginación
     */
    /*
    private void recreatePagination() {
        pagination = null;
    }  
    */
    
    
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionCreated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionUpdated"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = (TipoCapacitacion) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        selectedItemIndex = getItems().getRowIndex();
        performDestroy();
        //recreatePagination();
        recreateModel();
        return "view";
    }

    /**
     * @return mensaje que notifica la inserción
     */
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "view";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "list";
        }
    }    
    
    
    
    
    /*************************
    ** Métodos de selección **
    **************************/
    /**
     * @return la totalidad de las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(tipoCapacitacionFacade.findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(tipoCapacitacionFacade.findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public TipoCapacitacion getTipoCapacitacion(java.lang.Long id) {
        return tipoCapacitacionFacade.find(id);
    }    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private TipoCapacitacionFacade getFacade() {
        return tipoCapacitacionFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("TipoCapacitacionDeletedErrorOccured"));
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
            // go to previous page if last page disappeared:
            /*
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
            */
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
    
    private void buscarTipoCapacitacion(){
        items = new ListDataModel(getFacade().getXString(selectParam)); 
    }    
        
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = TipoCapacitacion.class)
    public static class EdificioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbTipoCapacitacion controller = (MbTipoCapacitacion) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbTipoCapacitacion");
            return controller.getTipoCapacitacion(getKey(value));
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
            if (object instanceof TipoCapacitacion) {
                TipoCapacitacion o = (TipoCapacitacion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoCapacitacion.class.getName());
            }
        }
    }    
}
