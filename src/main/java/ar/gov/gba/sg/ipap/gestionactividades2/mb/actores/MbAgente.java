/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoOrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.CargoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstudiosCursadosFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.LocalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.NivelIpapFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.SituacionRevistaFacade;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbAgente implements Serializable{

    private Agente current;
    private DataModel items = null;
    private List<Agente> listFilter;
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
    
    private String selectParam;    
    private boolean habilitadas;
    private List<EstudiosCursados> listaEstudios;
    private List<NivelIpap> listaNivelIpap;
    private List<Titulo> listaTitulos;
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
    private ListDataModel listDMPart;
    private List<Participante> listPartFilter;
    private boolean iniciado;
    private TipoOrganismo tipoOrg;
    private List<TipoOrganismo> listTipoOrganismo; 
    
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
    @EJB
    private TipoOrganismoFacade tipoOrgFacade;    
    
    
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
        iniciado = false;
        listaEstudios = estCurFacade.findAll();
        listaNivelIpap = nivelIpapFacade.findAll();
        listaTitulos = tituloFacade.findAll();
        listaSitRev = sitRevFacade.findAll();
        listaCargo = cargoFacade.findAll();
        listTipoOrganismo = tipoOrgFacade.findAll();
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
    
    public TipoOrganismo getTipoOrg() {
        return tipoOrg;
    }

    public void setTipoOrg(TipoOrganismo tipoOrg) {
        this.tipoOrg = tipoOrg;
    }

    public List<TipoOrganismo> getListTipoOrganismo() {
        return listTipoOrganismo;
    }

    public void setListTipoOrganismo(List<TipoOrganismo> listTipoOrganismo) {
        this.listTipoOrganismo = listTipoOrganismo;
    }

    
    public Edad getEdad() {
        return edad;
    }

    public void setEdad(Edad edad) {
        this.edad = edad;
    }
    
    public List<Agente> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Agente> listFilter) {
        this.listFilter = listFilter;
    }

    public List<Participante> getListPartFilter() {
        return listPartFilter;
    }

    public void setListPartFilter(List<Participante> listPartFilter) {
        this.listPartFilter = listPartFilter;
    }

    public ListDataModel getListDMPart() {
        return listDMPart;
    }

    public void setListDMPart(ListDataModel listDMPart) {
        this.listDMPart = listDMPart;
    }

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

    public List<Organismo> getListaOrganismos() {
        return listaOrganismos;
    }

    public void setListaOrganismos(List<Organismo> listaOrganismos) {
        this.listaOrganismos = listaOrganismos;
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

    public Map<String,String> getSexos() {
        return sexos;
    }

    public void setSexos(Map<String,String> sexos) {
        this.sexos = sexos;
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
        iniciado = true;
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
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (Agente) getItems().getRowData();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        esReferente = false;
        current = new Agente();
        persona = new Persona();
        tipoOrg = null;
        listaOrganismos = null;

        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = agSelected;
        persona = new Persona();
        
        // cargo los list pesados para los combos
        listaOrganismos = organismoFacade.getHabilitados();
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
        // seteo la edad de la persona si existe la fecha de nacimiento
        if(current.getPersona().getFechaNacimiento() != null){
            Edad edadUtil = new Edad();
            edad = edadUtil.calcularEdad(current.getPersona().getFechaNacimiento());
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
        current = agSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
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
                
                // Seteo la condición de referente
                current.setEsReferente(esReferente);
                
                // Inserción
                getFacade().create(current);
                tipoOrg = null;
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
                JsfUtil.addSuccessMessage("Se agregaron los datos personales al Agente que está creando. Por favor, cierre la ventana correspondiente.");
            }else{
                JsfUtil.addErrorMessage("Ya se encuentra registrado un Agente con estos datos personales.");
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
                tipoOrg = null;
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
     * Método para editar los datos personales del Agente
     */
    public void updatePersona(){
        Persona per = personaFacade.getExistente(current.getPersona().getTipoDocumento().getId(), current.getPersona().getDocumento());
        if(per == null){
            // Formateo el apellido
            String tempApp = current.getPersona().getApellidos();
            current.getPersona().setApellidos(tempApp.toUpperCase());
            
            // Muestro mensaje de actualización
            JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Agente que está editando. Por favor, cierre la ventana correspondiente.");
        }else{
            if(per.getId().equals(current.getPersona().getId())){
                // Formateo el apellido
                String tempApp = current.getPersona().getApellidos();
                current.getPersona().setApellidos(tempApp.toUpperCase());

                // Muestro mensaje de actualización
                JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Agente que está editando. Por favor, cierre la ventana correspondiente.");
            }else{
                JsfUtil.addErrorMessage("Ya existe otro Agente con estos datos personales.");
            }
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = agSelected;
        performDestroy();
        recreateModel();
        return "view";
    }    
    
    public void preProcessPDF(Object document) throws DocumentException, IOException {
        Document pdf = (Document) document;    
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.newPage();
    }    
    
    /*************************
    ** Métodos de selección **
    **************************/

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
                    if(!s.equals("mbAgente") && !s.equals("mbLogin")){
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
    
    /**
     * Método para actualizar los Organismos según el tipo seleccionado
     * @param event
     */
    public void tipoOrgChangeListener(ValueChangeEvent event) {   
        tipoOrg = (TipoOrganismo)event.getNewValue();
        listaOrganismos = organismoFacade.getXTipo(tipoOrg);
    }     
    
    public void verParticipantes(){
        listDMPart = new ListDataModel(current.getParticipaciones());
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgParticipantes", options, null);
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
        listDMPart = null;
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
        if(listPartFilter != null){
            listPartFilter = null;
        }
        if(listFilter != null){
            listFilter = null;
        } 
        if(listaOrganismos != null){
            listaOrganismos = null;
        }  
        tipoOrg = null;
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
