/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.LocalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TipoDocumentoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Edad;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
public class MbPersona implements Serializable{

    private Persona current;
    private DataModel items = null;
    
    @EJB
    private PersonaFacade personaFacade;
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
    private Edad edad;
    private boolean habilitadas;
    private int selectIParam;
    private Localidad localidad;
    private Usuario usLogeado;
    private MbLogin login; 
    private boolean iniciado;
    
    /**
     * Creates a new instance of MbPersona
     */
    public MbPersona() {
    }

    /**
     *
     */
    @PostConstruct
    public void init(){
        iniciado = false;
        listaTipoDocs = tipoDocFacade.findAll();
        listaLocalidades = localidadFacade.findAll();
        sexos  = new HashMap<>();
        sexos.put("Femenino", "F");
        sexos.put("Masculino", "M");     
        habilitadas = true;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
    }     

    /********************************
     ** Getters y Setters ***********
     ********************************/
    
    public Persona getCurrent() {
        return current;
    }

    public void setCurrent(Persona current) {
        this.current = current;
    }

    
    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    /**
     *
     * @param localidad
     */
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    /**
     *
     * @return
     */
    public int getSelectIParam() {
        return selectIParam;
    }

    /**
     *
     * @param selectIParam
     */
    public void setSelectIParam(int selectIParam) {
        this.selectIParam = selectIParam;
    }

    /**
     *
     * @return
     */
    public boolean isHabilitadas() {
        return habilitadas;
    }

    /**
     *
     * @param habilitadas
     */
    public void setHabilitadas(boolean habilitadas) {
        this.habilitadas = habilitadas;
    }

    /**
     *
     * @return
     */
    public Edad getEdad() {
        return edad;
    }

    /**
     *
     * @param edad
     */
    public void setEdad(Edad edad) {
        this.edad = edad;
    }

    /**
     *
     * @return
     */
    public Map<String, String> getSexos() {
        return sexos;
    }

    /**
     *
     * @param sexos
     */
    public void setSexos(Map<String, String> sexos) {
        this.sexos = sexos;
    }
    
    /**
     *
     * @return
     */
    public List<Localidad> getListaLocalidades() {
        return listaLocalidades;
    }

    /**
     *
     * @param listaLocalidades
     */
    public void setListaLocalidades(List<Localidad> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }

    /**
     *
     * @return
     */
    public List<TipoDocumento> getListaTipoDocs() {
        return listaTipoDocs;
    }

    /**
     *
     * @param listaTipoDocs
     */
    public void setListaTipoDocs(List<TipoDocumento> listaTipoDocs) {
        this.listaTipoDocs = listaTipoDocs;
    }
    
    /********************************
     ** Métodos para el datamodel **
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
            if(habilitadas){
                items = new ListDataModel(getFacade().getHabilitadas());
            }else{
                items = new ListDataModel(getFacade().getDeshabilitadas());
            }
        }
        return items;
    }
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de las Personas habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
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
        selectedItemIndex = getItems().getRowIndex();
        
        // seteo la edad de la persona
        Edad edadUtil = new Edad();
        edad = edadUtil.calcularEdad(current.getFechaNacimiento());
        
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        selectedItemIndex = getItems().getRowIndex();
        
        // seteo la edad de la persona
        Edad edadUtil = new Edad();
        edad = edadUtil.calcularEdad(current.getFechaNacimiento());
        
        return "viewDes";
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
     * 
     * @return 
     */
    public String prepareHabilitar(){
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaHabilitada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersonaHabilitadaErrorOccured"));
            return null; 
        }
    }

    /*************************************************************
     ** Métodos de inicialización de búsquedas para habilitados **
     *************************************************************/
    
    /**
     *
     * @return
     */
    public String prepareSelectXLoc(){
        buscarXLocalidad();
        return "list";
    }
   
      
    /****************************************************************
     ** Métodos de inicialización de búsquedas para DesHabilitados **
     ****************************************************************/   

    /**
     *
     * @return
     */
    public String prepareSelectDesXLoc(){
        buscarXLocalidad();
        return "listDes";
    }
    
        
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta una nueva Persona en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            if(getFacade().noExiste(current.getTipoDocumento().getId(), current.getDocumento())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // Inserción
                getFacade().create(current);
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
     * Méto que actualiza una nueva Persona en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Persona per;
        try {
            per = getFacade().getExistente(current.getTipoDocumento().getId(), current.getDocumento());
            if(per == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaUpdated"));
                return "view";
            }else{
                if(per.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersonaUpdatedErrorOccured"));
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
        return JsfUtil.getSelectItems(personaFacade.findAll(), false);
    }

    /**
     * @return de a una las entidades persistidas formateadas
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(personaFacade.findAll(), true);
    }

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Persona getPersona(java.lang.Long id) {
        return personaFacade.find(id);
    }    
    
    /*
     * Métodos de búsqueda
     */

    /**
     *
     * @return
     */
    
    public String getSelectParam() {
        return selectParam;
    }

    /**
     *
     * @param selectParam
     */
    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam;
    }
    
    /**
     * Método para llenar la lista para el autocompletado de la búsqueda de nombres
     * @param query
     * @return 
     */
    public List<String> completeNombres(String query){
        List<String> nombres = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Persona per = (Persona)itRows.next();
            if(per.getApellidos().contains(query) || per.getNombres().contains(query)){
                nombres.add(per.getApellidos() + ", " + per.getNombres());
            }
        }
        return nombres;
    }    
    
    /**
     * Método para llenar la lista de autocompletado de la búsqueda por documento
     * @param query
     * @return 
     */
    public List<String> completeDocum(String query){
        List<String> docs = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Persona per = (Persona)itRows.next();
            if(Integer.toString(per.getDocumento()).contains(query)){
                docs.add(Integer.toString(per.getDocumento()));
            }
        }
        return docs;
    }
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbPersona");
  
        return "inicio";
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
                    if(!s.equals("mbPersona") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }

    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private PersonaFacade getFacade() {
        return personaFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        if(selectParam != null){
            selectParam = null;
        }
        if(selectIParam != 0){
            selectIParam = 0;
        }
        if(localidad != null){
            localidad = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersonaDeletedErrorOccured"));
        }
    }    
    
    /*********************************************************
     **** métodos privados para la búsqueda de habiliados ****
     *********************************************************/
    
    private void buscarXLocalidad(){
        List<Persona> personas = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Persona per = (Persona)itRows.next();
            if(per.getLocalidad().equals(localidad)){
                personas.add(per);
            }
        items = null;
        items = new ListDataModel(personas);            
        }
    }
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Persona.class)
    public static class PersonaControllerConverter implements Converter {

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
            if (object instanceof Persona) {
                Persona o = (Persona) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Persona.class.getName());
            }
        }
    }                 
}
