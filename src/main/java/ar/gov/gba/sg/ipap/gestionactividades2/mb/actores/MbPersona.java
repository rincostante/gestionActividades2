/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.LocalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TipoDocumentoFacade;
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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author rincostante
 */
public class MbPersona implements Serializable{

    private Persona current;
    private DataModel items = null;
    
    @EJB
    private PersonaFacade estudiosCursadosFacade;
    @EJB
    private TipoDocumentoFacade tipoDocFacade;
    @EJB
    private LocalidadFacade localidadFacade;
    private int selectedItemIndex;
    private String selectParam; 
    private List<String> listaNombres; 
    private List<TipoDocumento> listaTipoDocs; 
    private List<Localidad> listaLocalidades;
    private Map<String,String> sexos;   
    private int edad;
    
    /**
     * Creates a new instance of MbPersona
     */
    public MbPersona() {
    }
    
   @PostConstruct
    public void init(){
        listaTipoDocs = tipoDocFacade.findAll();
        listaLocalidades = localidadFacade.findAll();
        sexos  = new HashMap<>();
        sexos.put("Femenino", "F");
        sexos.put("Masculino", "M");         
    }     

    public Map<String, String> getSexos() {
        return sexos;
    }

    public void setSexos(Map<String, String> sexos) {
        this.sexos = sexos;
    }
    
    public List<Localidad> getListaLocalidades() {
        return listaLocalidades;
    }

    public void setListaLocalidades(List<Localidad> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<TipoDocumento> getListaTipoDocs() {
        return listaTipoDocs;
    }

    public void setListaTipoDocs(List<TipoDocumento> listaTipoDocs) {
        this.listaTipoDocs = listaTipoDocs;
    }
    
    /********************************
     ** Métodos para la navegación **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Persona getSelected() {
        if (current == null) {
            current = new Persona();
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
        current = (Persona) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Persona();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (Persona) getItems().getRowData();
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
        buscarPersonaXApYNom();
        return "list";
    }
    
    /**
     * Método que verifica que el Nivel Ipap que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = (Persona) getItems().getRowData();
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaNonDeletable"));
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
            if(getFacade().noExiste(current.getTipoDocumento().getId(), current.getDocumento())){
                //getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersonaCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Persona per;
        try {
            per = getFacade().getExistente(current.getTipoDocumento().getId(), current.getDocumento());
            if(per == null){
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosUpdated"));
                return "view";
            }else{
                if(per.getId().equals(current.getId())){
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EstudiosCursadosCreated"));
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
        current = (Persona) getItems().getRowData();
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
    public Persona getPersona(java.lang.Long id) {
        return estudiosCursadosFacade.find(id);
    }    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private PersonaFacade getFacade() {
        return estudiosCursadosFacade;
    }
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersonaDeletedErrorOccured"));
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
    
    private void buscarPersonaXApYNom(){
        items = new ListDataModel(getFacade().getXApYNom(selectParam)); 
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
    
    /**
     * Método para validad cantidad de caracteres del número de documento
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarLargoNumDoc(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(arg2.toString().length() < 7){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaLargoCaracteresNumDocMessage")));
        }
    }
    
    /**
     * Método para validad cantidad de caracteres de los números de teléfono
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */    
    public void validarLargoTel(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(arg2.toString().length() < 10){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaLargoTelefonosMessage")));
        } 
    }
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Persona.class)
    public static class PersonaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbPersona controller = (MbPersona) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbPersona");
            return controller.getPersona(getKey(value));
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
            if (object instanceof Persona) {
                Persona o = (Persona) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Persona.class.getName());
            }
        }
    }                 
}
