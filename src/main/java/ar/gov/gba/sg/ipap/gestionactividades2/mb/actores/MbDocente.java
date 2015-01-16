/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
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
public class MbDocente implements Serializable{

    private Docente current;
    private DataModel items = null;
    
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private PersonaFacade personaFacade;
    @EJB
    private AgenteFacade agenteFacade;  
    @EJB
    private TituloFacade tituloFacade;      
    
    private int selectedItemIndex;
    private String selectParam;
    private List<Persona> listaPersonas;
    private List<Agente> listaAgentes;
    private List<Titulo> listaTitulos;
    private boolean habilitadas;
    private Persona persona;
    private Agente agente;
    private Titulo titulo;
    
    /**
     * Creates a new instance of MbDocente
     */
    public MbDocente() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        listaTitulos = tituloFacade.findAll();
        habilitadas = true;
    }   

    /********************************
     ** Getters y Setters ***********
     ********************************/
    
    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public String getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Agente> getListaAgentes() {
        return listaAgentes;
    }

    public void setListaAgentes(List<Agente> listaAgentes) {
        this.listaAgentes = listaAgentes;
    }

    public List<Titulo> getListaTitulos() {
        return listaTitulos;
    }

    public void setListaTitulos(List<Titulo> listaTitulos) {
        this.listaTitulos = listaTitulos;
    }
    
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Docente getSelected() {
        if (current == null) {
            current = new Docente();
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
        current = (Docente) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (Docente) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Docente();
        // cargo los list para los combos
        listaPersonas = personaFacade.findAll();
        listaAgentes = agenteFacade.findAll();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = (Docente) getItems().getRowData();
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
        current = (Docente) getItems().getRowData();
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = (Docente) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(1);
            current.getAdmin().setHabilitado(true);
            current.getAdmin().setUsBaja(0);
            current.getAdmin().setFechaBaja(null);

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteHabilitada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteHabilitadaErrorOccured"));
            return null; 
        }
    }    
    
   /*************************************************************
     ** Métodos de inicialización de búsquedas para habilitados **
     *************************************************************/
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectHab(){
        //buscarRapida();
        return "list";
    }
    
    /**
     *
     * @return
     */
    public String prepareSelectXLoc(){
        //buscarXLocalidad();
        return "list";
    }
    
    /**
     * 
     * @return 
     */
    public String prepareSelectXDoc(){
        //buscarRapidaXDoc();
        return "list";
    }
    
   /****************************************************************
     ** Métodos de inicialización de búsquedas para DesHabilitados **
     ****************************************************************/   
    
    /**
     * 
     * @return 
     */
    public String prepareSelectDes(){
        //buscarRapida();
        return "listDes";
    }

    /**
     *
     * @return
     */
    public String prepareSelectDesXLoc(){
        //buscarXLocalidad();
        return "listDes";
    }

    /**
     *
     * @return
     */
    public String prepareDesSelectDesXDoc(){
        //buscarRapidaXDoc();
        return "listDes";
    }
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta uno nuevo Docente en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        Long idAgente;
        Long idPersona;
        if(current.getAgente() == null){
            idAgente = Long.valueOf(0);
            idPersona = current.getPersona().getId();
        }else{
            idPersona = Long.valueOf(0);
            idAgente = current.getAgente().getId();
        }
        try {
            if(getFacade().noExiste(idAgente, idPersona)){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(1);
                current.setAdmin(admEnt);
                
                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Méto que actualiza un nuev Docente en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Docente doc;
        Long idAgente;
        Long idPersona;
        if(current.getAgente() == null){
            idAgente = Long.valueOf(0);
            idPersona = current.getPersona().getId();
        }else{
            idPersona = Long.valueOf(0);
            idAgente = current.getAgente().getId();
        }        
        try {
            doc = getFacade().getExistente(idAgente, idPersona);
            if(doc == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(1);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteUpdated"));
                return "view";
            }else{
                if(doc.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(1);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = (Docente) getItems().getRowData();
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
    public Docente getDocente(java.lang.Long id) {
        return getFacade().find(id);
    }    
    
    /*
     * Métodos de búsqueda
     */

    
    /**
     * Método para llenar la lista para el autocompletado de la búsqueda de nombres
     * @param query
     * @return 
     */
    /*
    public List<String> completeNombres(String query){
        List<String> nombres = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Docente doc = (Docente)itRows.next();
            if(doc.getPersona().getApellidos().contains(query) || doc.getPersona().getNombres().contains(query)){
                nombres.add(doc.getPersona().getApellidos() + ", " + doc.getPersona().getNombres() + " - " + doc.getPersona().getDocumento() + " - " + doc.g);
            }
                    
                    
                    
                    || doc.getAgente().getApellidos().contains(query) || doc.getAgente().getNombres().contains(query)){
                
                nombres.add(doc.getApellidos() + ", " + doc.getNombres());
            }
        }
        return nombres;
    }    
    */
    
    /**
     * Método para llenar la lista de autocompletado de la búsqueda por documento
     * @param query
     * @return 
     */
    /*
    public List<String> completeDocum(String query){
        List<String> docs = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Docente doc = (Docente)itRows.next();
            if(Integer.toString(doc.getDocumento()).contains(query)){
                docs.add(Integer.toString(doc.getDocumento()));
            }
        }
        return docs;
    }
    */
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbDocente");
        return "inicio";
    }  
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private DocenteFacade getFacade() {
        return docenteFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        if(selectParam != null){
            selectParam = null;
        }
        
        if(persona != null){
            persona = null;
        }
        
        if(agente != null){
            agente = null;
        }
        
        if(titulo != null){
            titulo = null;
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
            current.getAdmin().setUsBaja(selectedItemIndex);
            current.getAdmin().setHabilitado(false);
            
            // Deshabilito la entidad
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteDeletedErrorOccured"));
        }
    }    
    
    /*********************************************************
     **** métodos privados para la búsqueda de habiliados ****
     *********************************************************/
    /*
    private void buscarRapida(){
        List<Docente> docentes = new ArrayList();
        Iterator itRows = items.iterator();
        String apellidos = "";
        String nombres = "";
        
        // verifico de qué búsqueda se trata, si es completa busco en apellido y en nombre y si no, en uno o en otro
        boolean completa;
        if(selectParam.indexOf(",") > 0){
            apellidos = selectParam.substring(0, selectParam.indexOf(","));
            nombres = selectParam.substring(selectParam.indexOf(",") + 1, selectParam.length());
            completa = true;
        }else{
            completa = false;
        }
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            Persona per = (Persona)itRows.next();
            if(completa){
                if(per.getApellidos().contains(apellidos.trim()) && per.getNombres().contains(nombres.trim())){
                    personas.add(per);
                }
            }else{
                if(per.getApellidos().contains(selectParam) || per.getNombres().contains(selectParam)){
                    personas.add(per);
                }
            }           
        }        
        items = null;
        items = new ListDataModel(personas); 
    } 
    
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
    
    private void buscarRapidaXDoc(){
        List<Persona> personas = new ArrayList();
        Iterator itRows = items.iterator();
        while(itRows.hasNext()){
            Persona per = (Persona)itRows.next();
            if(per.getDocumento() == selectIParam){
                personas.add(per);
            }
        }
        items = null;
        items = new ListDataModel(personas);
    }
    */
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Docente.class)
    public static class DocenteControllerConverter implements Converter {

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
            MbDocente controller = (MbDocente) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbDocente");
            return controller.getDocente(getKey(value));
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
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Docente.class.getName());
            }
        }
    }       
}
