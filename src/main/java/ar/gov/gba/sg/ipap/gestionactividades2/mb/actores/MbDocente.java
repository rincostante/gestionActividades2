/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.LocalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TipoDocumentoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Edad;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;


/**
 *
 * @author rincostante
 */
public class MbDocente implements Serializable{

    private Docente current;
    private DataModel items = null;
    private List<Docente> listFilter;
    
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private PersonaFacade personaFacade;
    @EJB
    private AgenteFacade agenteFacade;  
    @EJB
    private TituloFacade tituloFacade;      
    
    private String selectParam;
    private List<Agente> listaAgentes;
    private List<Titulo> listaTitulos;
    private boolean habilitadas;
    private Docente docSelected;
    private int antigAnios;
    private int antigMeses;
    private Date fDespuesDe;
    private Date fAntesDe;
    boolean esAgente;
    boolean esPersona;   
    private Usuario usLogeado;
    private MbLogin login;   
    private ListDataModel listDMActImp;
    private List<ActividadImplementada> listActImpFilter;
    private ListDataModel listDMClases;
    private List<Clase> listClasesFilter;
    private boolean iniciado;
    
    // datos para el formulario de datos personales
    private Persona persona;
    private List<TipoDocumento> listaTipoDocs; 
    private List<Localidad> listaLocalidades;
    private Map<String,String> sexos;
    private Edad edad;
    @EJB
    private TipoDocumentoFacade tipoDocFacade;
    @EJB
    private LocalidadFacade localidadFacade;        
    
    /**
     * Creates a new instance of MbDocente
     */
    public MbDocente() {
    }
    
    @PostConstruct
    public void init(){
        iniciado = false;
        listaTitulos = tituloFacade.findAll();
        habilitadas = true;
        sexos  = new HashMap<>();
        sexos.put("Femenino", "F");
        sexos.put("Masculino", "M"); 
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();    
    }   

    /********************************
     ** Getters y Setters *********** 
     ********************************/

    public Map<String, String> getSexos() {
        return sexos;
    }

    public void setSexos(Map<String, String> sexos) {
        this.sexos = sexos;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<TipoDocumento> getListaTipoDocs() {
        return listaTipoDocs;
    }

    public void setListaTipoDocs(List<TipoDocumento> listaTipoDocs) {
        this.listaTipoDocs = listaTipoDocs;
    }

    public List<Localidad> getListaLocalidades() {
        return listaLocalidades;
    }

    public void setListaLocalidades(List<Localidad> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }

    public Edad getEdad() {
        return edad;
    }

    public void setEdad(Edad edad) {
        this.edad = edad;
    }


    public List<ActividadImplementada> getListActImpFilter() {
        return listActImpFilter;
    }

    public void setListActImpFilter(List<ActividadImplementada> listActImpFilter) {
        this.listActImpFilter = listActImpFilter;
    }

    public List<Clase> getListClasesFilter() {
        return listClasesFilter;
    }

    public void setListClasesFilter(List<Clase> listClasesFilter) {
        this.listClasesFilter = listClasesFilter;
    }

    public List<Docente> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Docente> listFilter) {
        this.listFilter = listFilter;
    }

    public ListDataModel getListDMClases() {
        return listDMClases;
    }

    public void setListDMClases(ListDataModel listDMClases) {
        this.listDMClases = listDMClases;
    }

    
    public ListDataModel getListDMActImp() {
        return listDMActImp;
    }

    public void setListDMActImp(ListDataModel listDMActImp) {
        this.listDMActImp = listDMActImp;
    }

    
    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }
    
    public boolean isEsAgente() {
        return esAgente;
    }

    public void setEsAgente(boolean esAgente) {
        this.esAgente = esAgente;
    }

    public boolean isEsPersona() {
        return esPersona;
    }

    public void setEsPersona(boolean esPersona) {
        this.esPersona = esPersona;
    }

    
    public Date getfDespuesDe() {
        return fDespuesDe;
    }

    public void setfDespuesDe(Date fDespuesDe) {
        this.fDespuesDe = fDespuesDe;
    }

    public Date getfAntesDe() {
        return fAntesDe;
    }

    public void setfAntesDe(Date fAntesDe) {
        this.fAntesDe = fAntesDe;
    }

    
    public int getAntigAnios() {
        return antigAnios;
    }

    public void setAntigAnios(int antigAnios) {
        this.antigAnios = antigAnios;
    }

    public int getAntigMeses() {
        return antigMeses;
    }

    public void setAntigMeses(int antigMeses) {
        this.antigMeses = antigMeses;
    }

    public Docente getDocSelected() {
        return docSelected;
    }

    public void setDocSelected(Docente docSelected) {
        this.docSelected = docSelected;
    }

    public String getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam;
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
     ** Métodos para el datamodel ***
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Docente getSelected() {
        if (current == null) {
            current = new Docente();
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
     * Método para inicializar el listado de los Docentes habilitados
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
        current = docSelected;
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = docSelected;
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        current = new Docente();
        persona = new Persona();
        esAgente = true;
        esPersona = true;
        listaAgentes = agenteFacade.getHabilitados();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = docSelected;
        if(current.getAgente() != null){
            esAgente = true;
            esPersona = false;
        }else{
            esAgente = false;
            esPersona = true;
        }        
        listaAgentes = agenteFacade.findAll();        
        return "edit";
    }
    
    /**
     * Método para abrir el diálogo para la creación de una Persona
     */
    public void prepareCreatePersona(){
        // seteo los elementos para el formulario
        listaTipoDocs = tipoDocFacade.findAll();
        listaLocalidades = localidadFacade.findAll();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("persona/dlgNewPersona", options, null);
    }
    
    /**
     * Método para abrir el diálogo para la edición de una Persona
     */
    public void prepareEditPersona(){
        // como no sé si los datos personales corresponden al Docente directamente o al Agente, 
        // en el caso en que el Docente sea Agente también, llevo todo al campo persona del bean
        if(current.getPersona() != null){
            persona = current.getPersona();
        }else{
            persona = current.getAgente().getPersona();
        }        
        
        // seteo los elementos para el formulario
        listaTipoDocs = tipoDocFacade.findAll();
        listaLocalidades = localidadFacade.findAll();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("persona/dlgEditPersona", options, null);
    }
    
    /**
     * Método para abrir el diálogo para la vista detalle de una Persona
     */
    public void prepareViewPersona(){
        // como no sé si los datos personales corresponden al Docente directamente o al Agente, 
        // en el caso en que el Docente sea Agente también, llevo todo al campo persona del bean
        if(current.getPersona() != null){
            persona = current.getPersona();
        }else{
            persona = current.getAgente().getPersona();
        }
        
        // seteo la edad de la persona si existe la fecha de nacimiento
        if(persona.getFechaNacimiento() != null){
            Edad edadUtil = new Edad();
            edad = edadUtil.calcularEdad(persona.getFechaNacimiento());
        }else{
            edad = new Edad();
            edad.setYear(0);
        }

        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("persona/dlgViewPersona", options, null);
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
        current = docSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
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
        current = docSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteHabilitada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteHabilitadaErrorOccured"));
            return null; 
        }
    }    
    
    /**
     * 
     */
    public void resetFechas(){
        fDespuesDe = null;
        fAntesDe = null;
    }
    
    public void preProcessPDF(Object document) throws DocumentException, IOException {
        Document pdf = (Document) document;    
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.newPage();
    }    
    
   /*************************************************************
     ** Métodos de inicialización de búsquedas para habilitados **
     *************************************************************/
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectHab(){
        buscarEntreFechas();
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
        buscarEntreFechas();
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
                admEnt.setUsAlta(usLogeado);
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
     * Método para guardar una Persona durante el registro de un Agente
     */
    public void createPersona(){
        if(current.getPersona() == null){
            if(personaFacade.noExiste(persona.getTipoDocumento().getId(), persona.getDocumento())){

                // Formateo el apellido
                String tempApp = persona.getApellidos();
                persona.setApellidos(tempApp.toUpperCase());

                // Agrego la persona al Agente
                current.setPersona(persona);

                // Muestro el mensaje correspondiente
                JsfUtil.addSuccessMessage("Se agregaron los datos personales al Docente que está creando. Por favor, cierre la ventana correspondiente.");
            }else{
                JsfUtil.addErrorMessage("Ya se encuentra registrado un Docente con estos datos personales.");
            }
        }else{
            JsfUtil.addErrorMessage("El Agente que está creando ya tiene sus datos personales completos.");
        }
    }    

    /**
     * Método que actualiza un nuevo Docente en la base de datos.
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
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteUpdated"));
                return "view";
            }else{
                if(doc.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

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
     * Método para editar los datos personales del Agente
     */
    public void updatePersona(){
        Persona per = personaFacade.getExistente(current.getPersona().getTipoDocumento().getId(), current.getPersona().getDocumento());
        if(per == null){
            // Formateo el apellido
            String tempApp = current.getPersona().getApellidos();
            current.getPersona().setApellidos(tempApp.toUpperCase());
            
            // Muestro mensaje de actualización
            JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Docente que está editando. Por favor, cierre la ventana correspondiente.");
        }else{
            if(per.getId().equals(current.getPersona().getId())){
                // Formateo el apellido
                String tempApp = current.getPersona().getApellidos();
                current.getPersona().setApellidos(tempApp.toUpperCase());

                // Muestro mensaje de actualización
                JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Docente que está editando. Por favor, cierre la ventana correspondiente.");
            }else{
                JsfUtil.addErrorMessage("Ya existe otro Docente con estos datos personales.");
            }
        }
    }    

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = docSelected;
        performDestroy();
        recreateModel();
        return "view";
    }    
    
    /*************************
    ** Métodos de selección **
    **************************/
    
    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Docente getDocente(java.lang.Long id) {
        return getFacade().find(id);
    }    
    
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
    
    /**
     * Método para mostrar las Actividades Implementadas vinculadas a este Docente
     */
    public void verActividadesImp(){
        listDMActImp = new ListDataModel(current.getActividades());
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgActividadesImp", options, null);
    }     
    
    /**
     * Método para mostrar las Clases vinculadas a este Docente
     */
    public void verClases(){
        listDMClases = new ListDataModel(current.getClases());
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgClases", options, null);  
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
                    if(!s.equals("mbDocente") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    
    
    /*********************
    ** Desencadenadores **
    **********************/    
    
    /**
     * 
     * @param event
     */
    public void agenteChangeListener(ValueChangeEvent event){
        Agente ag = (Agente)event.getNewValue();
        if(ag != null){
            esAgente = true;
            esPersona = false;
        }else{
            esAgente = false;
            esPersona = true;
        }
    }
    
    /**
     * 
     * @param event
     */
    public void personaActionListener(ActionEvent event){
        esPersona = true;
        esAgente = false;
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
        listDMActImp = null;
        listDMClases = null;
        if(selectParam != null){
            selectParam = null;
        }
        if(listFilter != null){
            listFilter = null;
        }
        if(listActImpFilter != null){
            listActImpFilter = null;
        }
        if(listClasesFilter != null){
            listClasesFilter = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DocenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("DocenteDeletedErrorOccured"));
        }
    }    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<Docente> docentes = new ArrayList();
        Iterator itRows = items.iterator();
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            Docente doc = (Docente)itRows.next();
            if(doc.getFechaInicioDocencia().after(fDespuesDe) && doc.getFechaInicioDocencia().before(fAntesDe)){
                docentes.add(doc);
            }          
        }        
        items = null;
        items = new ListDataModel(docentes); 
    } 
    
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
            if (object instanceof Docente) {
                Docente o = (Docente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Docente.class.getName());
            }
        }
    }       
}
