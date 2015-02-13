/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoOrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
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
public class MbOrganismo implements Serializable{
    
    private Organismo current;
    private DataModel items = null;    
    
    @EJB
    private OrganismoFacade organismoFacade;
    @EJB
    private TipoOrganismoFacade tipoOrgFacade;
    
    private int selectedItemIndex;
    private String selectParam;
    private boolean habilitadas;
    private Organismo orgSelected;
    private List<TipoOrganismo> listaTipoOrg;
    private Usuario usLogeado;
    private MbLogin login;    

    /**
     * Creates a new instance of MbOrganismo
     */
    public MbOrganismo() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        listaTipoOrg = tipoOrgFacade.findAll();
        habilitadas = true;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();     
    }      

    /********************************
     ** Getters y Setters *********** 
     ********************************/    
    
    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }
    
    
    public boolean isHabilitadas() {
        return habilitadas;
    }

    public void setHabilitadas(boolean habilitadas) {
        this.habilitadas = habilitadas;
    }

    public Organismo getOrgSelected() {
        return orgSelected;
    }

    public void setOrgSelected(Organismo orgSelected) {
        this.orgSelected = orgSelected;
    }

    public List<TipoOrganismo> getListaTipoOrg() {
        return listaTipoOrg;
    }

    public void setListaTipoOrg(List<TipoOrganismo> listaTipoOrg) {
        this.listaTipoOrg = listaTipoOrg;
    }
    
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Organismo getSelected() {
        if (current == null) {
            current = new Organismo();
            selectedItemIndex = -1;
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
            if(habilitadas){
                items = new ListDataModel(getFacade().getHabilitados());
            }else{
                items = new ListDataModel(getFacade().getDeshabilitados());
            }
        }
        return items;
    }    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Organismos habilitados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        habilitadas = true;
        recreateModel();
        return "list";
    } 
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        habilitadas = false;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = orgSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (Organismo) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Organismo();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = orgSelected;
        // cargo los list para los combos     
        selectedItemIndex = getItems().getRowIndex();
        return "edit";
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
     * Método que verifica que el Nivel Ipap que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = orgSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = orgSelected;
        selectedItemIndex = getItems().getRowIndex();
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.getAdmin().setHabilitado(true);
            current.getAdmin().setUsBaja(null);
            current.getAdmin().setFechaBaja(null);

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrganismoHabilitadaErrorOccured"));
            return null; 
        }
    }  
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta uno nuevo Organismo en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            if(getFacade().noExiste(current.getNombre(), current.getTipoOrganismo().getId())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrganismoCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza un nuevo Organismo en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        Organismo org;
        try {
            org = getFacade().getExistente(current.getNombre(), current.getTipoOrganismo().getId());
            if(org == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoUpdated"));
                return "view";
            }else{
                if(org.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrganismoUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = orgSelected;
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
        return JsfUtil.getSelectItems(getFacade().findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getFacade().findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Organismo getOrganismo(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbOrganismo");
  
        return "inicio";
    }  
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private OrganismoFacade getFacade() {
        return organismoFacade;
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
     * Método que deshabilita la entidad
     */
    private void performDestroy() {
        try {
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaBaja(date);
            current.getAdmin().setUsBaja(usLogeado);
            current.getAdmin().setHabilitado(false);
            
            // Deshabilito la entidad
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("OrganismoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("OrganismoDeletedErrorOccured"));
        }
    }    
 
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Organismo.class)
    public static class OrganismoControllerConverter implements Converter {

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
            MbOrganismo controller = (MbOrganismo) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbOrganismo");
            return controller.getOrganismo(getKey(value));
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
            if (object instanceof Organismo) {
                Organismo o = (Organismo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Organismo.class.getName());
            }
        }
    }           
}
