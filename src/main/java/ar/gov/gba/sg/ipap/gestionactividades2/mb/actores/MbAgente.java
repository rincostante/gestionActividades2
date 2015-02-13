/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.CargoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstudiosCursadosFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.NivelIpapFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.SituacionRevistaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbAgente implements Serializable{

    private Agente current;
    private DataModel items = null;
    private Agente agSelected;
    
    @EJB
    private AgenteFacade agenteFacade; 
    @EJB
    private EstudiosCursadosFacade estCurFacade;
    @EJB
    private OrganismoFacade organismoFacade;
    @EJB
    private NivelIpapFacade nivelIpapFacade;
    @EJB
    private CargoFacade cargoFacade;
    @EJB
    private TituloFacade tituloFacade;
    @EJB
    private PersonaFacade personaFacade;
    @EJB
    private SituacionRevistaFacade sitRevFacade;
    
    private int selectedItemIndex;
    private String selectParam;    
    private boolean habilitadas;
    private List<EstudiosCursados> listaEstudios;
    private List<NivelIpap> listaNivelIpap;
    private List<Titulo> listaTitulos;
    private List<Persona> listaPersonas;
    private List<Organismo> listaOrganismos;
    private List<SituacionRevista> listaSitRev;
    private List<Agente> listaReferentes;
    private List<Cargo> listaCargo;
    private int antigAnios;
    private int antigMeses;
    private Date fDespuesDe;
    private Date fAntesDe;    
    private Organismo selectOrg;
    private Agente selecReferente;
    private Cargo selectCargo;
    private SituacionRevista selectSitRev;
    private boolean esReferente;
    private Usuario usLogeado;
    private MbLogin login;  
    /**
     * Creates a new instance of MbAgente
     */
    public MbAgente() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        listaEstudios = estCurFacade.findAll();
        listaNivelIpap = nivelIpapFacade.findAll();
        listaTitulos = tituloFacade.findAll();
        listaSitRev = sitRevFacade.findAll();
        listaCargo = cargoFacade.findAll();
        listaOrganismos = organismoFacade.findAll();
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

    
    public boolean isEsReferente() {
        return esReferente;
    }

    public void setEsReferente(boolean esReferente) {
        this.esReferente = esReferente;
    }

    
    public Agente getSelecReferente() {
        return selecReferente;
    }

    public void setSelecReferente(Agente selecReferente) {
        this.selecReferente = selecReferente;
    }

    public Cargo getSelectCargo() {
        return selectCargo;
    }

    public void setSelectCargo(Cargo selectCargo) {
        this.selectCargo = selectCargo;
    }

    public SituacionRevista getSelectSitRev() {
        return selectSitRev;
    }

    public void setSelectSitRev(SituacionRevista selectSitRev) {
        this.selectSitRev = selectSitRev;
    }

    
    public List<Cargo> getListaCargo() {
        return listaCargo;
    }

    public void setListaCargo(List<Cargo> listaCargo) {
        this.listaCargo = listaCargo;
    }

    
    public Organismo getSelectOrg() {
        return selectOrg;
    }

    public void setSelectOrg(Organismo selectOrg) {
        this.selectOrg = selectOrg;
    }

    
    public List<Agente> getListaReferentes() {
        return listaReferentes;
    }

    public void setListaReferentes(List<Agente> listaReferentes) {
        this.listaReferentes = listaReferentes;
    }

    
    public Agente getAgSelected() {
        return agSelected;
    }

    public void setAgSelected(Agente agSelected) {
        this.agSelected = agSelected;
    }

    public List<SituacionRevista> getListaSitRev() {
        return listaSitRev;
    }

    public void setListaSitRev(List<SituacionRevista> listaSitRev) {
        this.listaSitRev = listaSitRev;
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
    
    public boolean isHabilitadas() {
        return habilitadas;
    }

    public void setHabilitadas(boolean habilitadas) {
        this.habilitadas = habilitadas;
    }

    public List<EstudiosCursados> getListaEstudios() {
        return listaEstudios;
    }

    public void setListaEstudios(List<EstudiosCursados> listaEstudios) {
        this.listaEstudios = listaEstudios;
    }

    public List<NivelIpap> getListaNivelIpap() {
        return listaNivelIpap;
    }

    public void setListaNivelIpap(List<NivelIpap> listaNivelIpap) {
        this.listaNivelIpap = listaNivelIpap;
    }

    public List<Titulo> getListaTitulos() {
        return listaTitulos;
    }

    public void setListaTitulos(List<Titulo> listaTitulos) {
        this.listaTitulos = listaTitulos;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Organismo> getListaOrganismos() {
        return listaOrganismos;
    }

    public void setListaOrganismos(List<Organismo> listaOrganismos) {
        this.listaOrganismos = listaOrganismos;
    }

    
    /********************************
     ** Métodos para el datamodel ***
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Agente getSelected() {
        if (current == null) {
            current = new Agente();
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
     * Método para inicializar el listado de los Agentes habilitados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        habilitadas = true;
        esReferente = false;
        recreateModel();
        return "list";
    } 
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        esReferente = false;
        habilitadas = false;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = agSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (Agente) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        esReferente = false;
        current = new Agente();
        // cargo los list pesados para los combos
        listaPersonas = personaFacade.findAll();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = agSelected;
        // cargo los list pesados para los combos
        listaPersonas = personaFacade.findAll();
        listaOrganismos = organismoFacade.findAll();      
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
        current = agSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = agSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteHabilitada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AgenteHabilitadaErrorOccured"));
            return null; 
        }
    }    

    /**
     * 
     */
    public void resetSelect(){
        if(fDespuesDe != null){
            fDespuesDe = null;
        }
        if(fAntesDe != null){
            fAntesDe = null;
        }
        if(selectOrg != null){
            selectOrg = null;
        }
        if(selecReferente != null){
            selecReferente = null;
        }
        if(selectCargo != null){
            selectCargo = null;
        }
        if(selectSitRev != null){
            selectSitRev = null;
        }  
        if(!listaReferentes.isEmpty()){
            listaReferentes.clear();
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
        List<Agente> agentes = new ArrayList();
        Iterator itRows = items.iterator();
        boolean valida = false;
        boolean entro = false;
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            boolean sale = false;
            Agente ag = (Agente)itRows.next();
            if(fDespuesDe != null && fAntesDe != null){
                entro = true;
                if(estaEntreFechas(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }else{
                if(fDespuesDe != null){
                    entro = true;
                    if(esPosteriorA(ag)){
                        valida = true;
                    }else{
                        sale = true;
                    }
                }else{
                    if(fAntesDe != null){
                        entro = true;
                        if(esAnteriorA(ag)){
                            valida = true;
                        }else{
                            sale = true;
                        }
                    }
                }
            }
            if(selectOrg != null && !sale){
                entro = true;
                if(estaOrganismo(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }
            if(selecReferente != null && !sale){
                entro = true;
                if(estaReferente(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }    
            if(selectCargo != null && !sale){
                entro = true;
                if(estaCargo(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            } 
            if(selectSitRev != null && !sale){
                entro = true;
                if(estaSitRev(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }    
            if(!sale && ag.isEsReferente() != esReferente){
                entro = true;
                agentes.add(ag);
            }
        }    
        if(entro){
            items = null;
            items = new ListDataModel(agentes); 
        }
        
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
        List<Agente> agentes = new ArrayList();
        Iterator itRows = items.iterator();
        boolean valida = false;
        boolean entro = false;
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            boolean sale = false;
            Agente ag = (Agente)itRows.next();
            if(fDespuesDe != null && fAntesDe != null){
                entro = true;
                if(estaEntreFechas(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }else{
                if(fDespuesDe != null){
                    entro = true;
                    if(esPosteriorA(ag)){
                        valida = true;
                    }else{
                        sale = true;
                    }
                }else{
                    if(fAntesDe != null){
                        entro = true;
                        if(esAnteriorA(ag)){
                            valida = true;
                        }else{
                            sale = true;
                        }
                    }
                }
            }
            if(selectOrg != null && !sale){
                entro = true;
                if(estaOrganismo(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }
            if(selecReferente != null && !sale){
                entro = true;
                if(estaReferente(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }    
            if(selectCargo != null && !sale){
                entro = true;
                if(estaCargo(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            } 
            if(selectSitRev != null && !sale){
                entro = true;
                if(estaSitRev(ag)){
                    valida = true;
                }else{
                    sale = true;
                }
            }    
            if(!sale && ag.isEsReferente() != esReferente){
                entro = true;
                agentes.add(ag);
            }
        }    
        items = null;
        items = new ListDataModel(agentes); 
        return "listDes";
    }
    
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta uno nuevo Agente en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        Long idPersona = current.getPersona().getId();
        try {
            if(getFacade().noExiste(idPersona)){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteCreated"));
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AgenteCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza un nuevo Docente en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Agente ag;
        Long idPersona = current.getPersona().getId();        
        try {
            ag = getFacade().getExistente(idPersona);
            if(ag == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteUpdated"));
                return "view";
            }else{
                if(ag.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AgenteUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        //current = (Docente) getItems().getRowData();
        current = agSelected;
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
    public Agente getAgente(java.lang.Long id) {
        return getFacade().find(id);
    }    
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbAgente");
      
        return "inicio";
    }  
    
    /*********************
    ** Desencadenadores **
    **********************/    
    
    /**
     * 
     * @param event
     */
    public void organismoChangeListener(ValueChangeEvent event) {
        selectOrg = (Organismo)event.getNewValue();
        
        listaReferentes = new ArrayList();
        Iterator itRows = items.iterator();
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            Agente ag = (Agente)itRows.next();
            if(ag.getOrganismo().equals(selectOrg) && ag.isEsReferente()){
                listaReferentes.add(ag);
            }          
        }        
    }    
    
    public void referenteChangeListener(ValueChangeEvent event){
        esReferente = !(boolean)event.getNewValue();
    }
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private AgenteFacade getFacade() {
        return agenteFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        if(selectParam != null){
            selectParam = null;
        }
        if(fDespuesDe != null){
            fDespuesDe = null;
        }
        if(fAntesDe != null){
            fAntesDe = null;
        }
        if(selectOrg != null){
            selectOrg = null;
        }
        if(selecReferente != null){
            selecReferente = null;
        }
        if(selectCargo != null){
            selectCargo = null;
        }
        if(selectSitRev != null){
            selectSitRev = null;
        }   
        if(listaReferentes != null){
            if(!listaReferentes.isEmpty()){
                listaReferentes.clear();
            }
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AgenteDeletedErrorOccured"));
        }
    }   
    
    /*********************************************
     **** métodos privados para las búsquedas ****
     *********************************************/

    private boolean estaEntreFechas(Agente ag){
        return ag.getFechaInicioActividades().after(fDespuesDe) && ag.getFechaInicioActividades().before(fAntesDe);
    }   
    
    private boolean esPosteriorA(Agente ag){
        return ag.getFechaInicioActividades().after(fDespuesDe);
    }
    
    private boolean esAnteriorA(Agente ag){
        return ag.getFechaInicioActividades().before(fAntesDe);
    }
    
    private boolean estaOrganismo(Agente ag){
        return ag.getOrganismo().equals(selectOrg);
    }
    
    private boolean estaReferente(Agente ag){
        if(ag.getReferente() != null){
            return ag.getReferente().equals(selecReferente);
        }else{
            return false;
        }
        
    }
    
    private boolean estaCargo(Agente ag){
        return ag.getCargo().equals(selectCargo);
    }
    
    private boolean estaSitRev(Agente ag){
        return ag.getSituacionRevista().equals(selectSitRev);
    }
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Agente.class)
    public static class AgenteControllerConverter implements Converter {

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
            MbAgente controller = (MbAgente) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbAgente");
            return controller.getAgente(getKey(value));
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
            if (object instanceof Agente) {
                Agente o = (Agente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Agente.class.getName());
            }
        }
    }           
}
