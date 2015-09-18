/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Orientacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Resolucion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Sede;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstadoParticipante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Rol;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadPlanFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ModalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrientacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ResolucionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SedeFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SubProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoOrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.CargoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ClaseFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstadoParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstudiosCursadosFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.LocalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.NivelIpapFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.PersonaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.RolFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.SituacionRevistaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TipoDocumentoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Edad;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbActividadImpl implements Serializable{
    
    private ActividadImplementada current;
    private List<ActividadImplementada> listado;
    private List<ActividadImplementada> listadoFilter;
    private List<Participante> listPart;
    private List<Participante> listPartFilter;
    private List<Clase> listClasesFilter;
    private List<Organismo> listOrgDisp;
    private List<Organismo> listOrgFilter;
    private List<Docente> listDocDisp;
    private List<Docente> listDocFilter;
    
    // variables para la registración de Clases
    private Clase clase;
    private List<Clase> listClaseNew;
    
    // variables para la registración de Participantes
    private Participante participante;
    private List<Participante> listPartNew;
    private List<Clase> listClasesXPart;
    
    // variables para la registración a asistencias
    private List<Participante> listPartDisp;
    private boolean regAsist;
    
    // variables para el formulario de Agentes
    private Agente agente;
    private List<EstudiosCursados> listaEstudios;
    private List<NivelIpap> listaNivelIpap;
    private List<Titulo> listaTitulos;
    private List<SituacionRevista> listaSitRev;
    private List<Agente> listaReferentes;
    private List<Cargo> listaCargo;
    private boolean esReferente;
    
    // variables para el formulario de datos personales
    private Persona persona;
    private List<TipoDocumento> listaTipoDocs; 
    private List<Localidad> listaLocalidades;
    private Map<String,String> sexos;
    private Edad edad;
    
    @EJB
    private PersonaFacade personaFacade;
    @EJB
    private AgenteFacade agenteFacade;
    @EJB
    private EstudiosCursadosFacade estCurFacade;
    @EJB
    private NivelIpapFacade nivelIpapFacade;
    @EJB
    private TituloFacade tituloFacade;
    @EJB
    private SituacionRevistaFacade sitRevFacade;
    @EJB
    private CargoFacade cargoFacade;
    @EJB
    private TipoDocumentoFacade tipoDocFacade;
    @EJB
    private LocalidadFacade localidadFacade;       
    @EJB
    private ActividadImplementadaFacade actImpFacade;
    @EJB
    private ActividadPlanFacade actPlanFacade;
    @EJB
    private OrganismoFacade organismoFacade;
    @EJB
    private ResolucionFacade resFacade;
    @EJB
    private SedeFacade sedeFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private RolFacade rolFacade;
    @EJB
    private OrientacionFacade orientacionFacade;
    @EJB
    private SubProgramaFacade subprogramaFacade;
    @EJB
    private TipoOrganismoFacade tipoOrgFacade;
    @EJB
    private ClaseFacade claseFacade;
    @EJB
    private ModalidadFacade modalidadFacade;
    @EJB
    private ParticipanteFacade participanteFacade;
    @EJB
    private EstadoParticipanteFacade estPartFacade;    
    
    private ActividadImplementada actImpSelected;
    private Usuario usLogeado;        
    private List<ActividadPlan> listActPlan;
    private List<TipoOrganismo> listTipoOrganismo;    
    private List<Organismo> listOrganismos;
    private List<Resolucion> listResoluciones;
    private List<Sede> listSedes;
    private List<Usuario> listCoordinadores;
    private List<Orientacion> listOrientaciones;
    private List<SubPrograma> listSubprogramas;
    private List<Modalidad> listModalidades;
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;          
    private int tipoList; //1=habilitadas | 2=finalizadas | 3=suspendidas | 4=deshabilitadas 
    private boolean esCoordinador;
    private boolean iniciado;
    private TipoOrganismo tipoOrg;
    private boolean asignaDisp; 
    
    /**
     * Creates a new instance of MbActividadImpl
     */
    public MbActividadImpl() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        iniciado = false;
        tipoList = 1;
        sexos  = new HashMap<>();
        sexos.put("Femenino", "F");
        sexos.put("Masculino", "M");          
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        esCoordinador = usLogeado.getRol().getNombre().equals("Coordinador");
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/   
    
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

    public Map<String, String> getSexos() {
        return sexos;
    }

    public void setSexos(Map<String, String> sexos) {
        this.sexos = sexos;
    }

    public Edad getEdad() {
        return edad;
    }

    public void setEdad(Edad edad) {
        this.edad = edad;
    }
    
    public List<Participante> getListPartDisp() {
        return listPartDisp;
    }

    public void setListPartDisp(List<Participante> listPartDisp) {
        this.listPartDisp = listPartDisp;
    }
    
    public List<Clase> getListClasesXPart() {
        return listClasesXPart;
    }

    public void setListClasesXPart(List<Clase> listClasesXPart) {
        this.listClasesXPart = listClasesXPart;
    }
    
    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public List<Participante> getListPartNew() {
        return listPartNew;
    }

    public void setListPartNew(List<Participante> listPartNew) {
        this.listPartNew = listPartNew;
    }
    
    public List<Clase> getListClaseNew() {
        return listClaseNew;
    }

    public void setListClaseNew(List<Clase> listClaseNew) {
        this.listClaseNew = listClaseNew;
    }
    
    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
    
    public boolean isAsignaDisp() {
        return asignaDisp;
    }

    public void setAsignaDisp(boolean asignaDisp) {
        this.asignaDisp = asignaDisp;
    }
    
    public List<Docente> getListDocDisp() {
        return listDocDisp;
    }

    public void setListDocDisp(List<Docente> listDocDisp) {
        this.listDocDisp = listDocDisp;
    }

    public List<Docente> getListDocFilter() {
        return listDocFilter;
    }

    public void setListDocFilter(List<Docente> listDocFilter) {
        this.listDocFilter = listDocFilter;
    }
    
    public List<TipoOrganismo> getListTipoOrganismo() {
        return listTipoOrganismo;
    }

    public void setListTipoOrganismo(List<TipoOrganismo> listTipoOrganismo) {
        this.listTipoOrganismo = listTipoOrganismo;
    }

    public TipoOrganismo getTipoOrg() {
        return tipoOrg;
    }

    public void setTipoOrg(TipoOrganismo tipoOrg) {
        this.tipoOrg = tipoOrg;
    }
    
    public List<Clase> getListClasesFilter() {
        return listClasesFilter;
    }

    public void setListClasesFilter(List<Clase> listClasesFilter) {
        this.listClasesFilter = listClasesFilter;
    }

    public List<SubPrograma> getListSubprogramas() {
        if(listSubprogramas == null){
            listSubprogramas = subprogramaFacade.getPorActFormativa(current.getActividadPlan());
        }
        return listSubprogramas;
    }

    public void setListSubprogramas(List<SubPrograma> listSubprogramas) {
        this.listSubprogramas = listSubprogramas;
    }
   
    public List<Orientacion> getListOrientaciones() {
        return listOrientaciones;
    }

    public void setListOrientaciones(List<Orientacion> listOrientaciones) {
        this.listOrientaciones = listOrientaciones;
    }    
    
    public List<ActividadImplementada> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<ActividadImplementada> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }
   
    public List<Participante> getListPartFilter() {
        return listPartFilter;
    }

    public void setListPartFilter(List<Participante> listPartFilter) {
        this.listPartFilter = listPartFilter;
    }

    public List<Participante> getListPart() {
        return listPart;
    }

    public void setListPart(List<Participante> listPart) {
        this.listPart = listPart;
    }
   
    public boolean isEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }
    
    public ActividadImplementada getActImpSelected() {
        return actImpSelected;
    }

    public void setActImpSelected(ActividadImplementada actImpSelected) {
        this.actImpSelected = actImpSelected;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public List<ActividadPlan> getListActPlan() {
        return listActPlan;
    }

    public void setListActPlan(List<ActividadPlan> listActPlan) {
        this.listActPlan = listActPlan;
    }

    public List<Organismo> getListOrganismos() {
        return listOrganismos;
    }

    public void setListOrganismos(List<Organismo> listOrganismos) {
        this.listOrganismos = listOrganismos;
    }

    public List<Resolucion> getListResoluciones() {
        return listResoluciones;
    }

    public void setListResoluciones(List<Resolucion> listResoluciones) {
        this.listResoluciones = listResoluciones;
    }

    public List<Sede> getListSedes() {
        return listSedes;
    }

    public void setListSedes(List<Sede> listSedes) {
        this.listSedes = listSedes;
    }

    public List<Usuario> getListCoordinadores() {
        return listCoordinadores;
    }

    public void setListCoordinadores(List<Usuario> listCoordinadores) {
        this.listCoordinadores = listCoordinadores;
    }

    public Date getfAntesDe() {
        return fAntesDe;
    }

    public void setfAntesDe(Date fAntesDe) {
        this.fAntesDe = fAntesDe;
    }

    public Date getfDespuesDe() {
        return fDespuesDe;
    }

    public void setfDespuesDe(Date fDespuesDe) {
        this.fDespuesDe = fDespuesDe;
    }

    public int getTipoList() {
        return tipoList;
    }

    public void setTipoList(int tipoList) {
        this.tipoList = tipoList;
    }
    
    public List<Organismo> getListOrgDisp() {
        return listOrgDisp;
    }

    public void setListOrgDisp(List<Organismo> listOrgDisp) {
        this.listOrgDisp = listOrgDisp;
    }

    public List<Organismo> getListOrgFilter() {
        return listOrgFilter;
    }

    public void setListOrgFilter(List<Organismo> listOrgFilter) {
        this.listOrgFilter = listOrgFilter;
    }

    public List<Modalidad> getListModalidades() {
        return listModalidades;
    }

    public void setListModalidades(List<Modalidad> listModalidades) {
        this.listModalidades = listModalidades;
    }    
    
    public boolean isRegAsist() {
        return regAsist;
    }

    public void setRegAsist(boolean regAsist) {
        this.regAsist = regAsist;
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

    public List<SituacionRevista> getListaSitRev() {
        return listaSitRev;
    }

    public void setListaSitRev(List<SituacionRevista> listaSitRev) {
        this.listaSitRev = listaSitRev;
    }

    public List<Agente> getListaReferentes() {
        return listaReferentes;
    }

    public void setListaReferentes(List<Agente> listaReferentes) {
        this.listaReferentes = listaReferentes;
    }

    public List<Cargo> getListaCargo() {
        return listaCargo;
    }

    public void setListaCargo(List<Cargo> listaCargo) {
        this.listaCargo = listaCargo;
    }

    public boolean isEsReferente() {
        return esReferente;
    }

    public void setEsReferente(boolean esReferente) {
        this.esReferente = esReferente;
    }    
   
    
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public ActividadImplementada getSelected() {
        if (current == null) {
            current = new ActividadImplementada();
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public List<ActividadImplementada> getListado() {
        if (listado == null) {
            if(esCoordinador){
                switch(tipoList){
                    case 1: listado = getFacade().getHabilitadasXCoor(usLogeado);
                        break;
                    case 2: listado = getFacade().getFinalizadasXCoor(usLogeado);
                        break;
                    case 3: listado = getFacade().getSuspendidasXCoor(usLogeado);
                        break;
                    default: listado = getFacade().getDeshabilitadasXCoor(usLogeado);
                }
            }else{
                switch(tipoList){
                    case 1: listado = getFacade().getHabilitadas();
                        break;
                    case 2: listado = getFacade().getFinalizadas();
                        break;
                    case 3: listado = getFacade().getSuspendidas();
                        break;
                    default: listado = getFacade().getDeshabilitadas();
                }
            }
        }
        return listado;
    }  
    
    public void setListado(List<ActividadImplementada> listado){
        this.listado = listado;
    }
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de las Actividad Implementadas habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        asignaDisp = false;
        iniciado = true;
        tipoList = 1;
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de las Actividad Implementadas finalizadas
     * @return acción para el listado de entidades
     */
    public String prepareListFin() {
        asignaDisp = false;
        tipoList = 2;
        recreateModel();
        return "listFin";
    }    
    
    /**
     * Método para inicializar el listado de las Actividad Implementadas suspendidas
     * @return acción para el listado de entidades
     */
    public String prepareListSusp() {
        asignaDisp = false;
        tipoList = 3;
        recreateModel();
        return "listSusp";
    }    
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        asignaDisp = false;
        tipoList = 4;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        // preparo variables para agregar Clases
        clase = new Clase();        
        if(listClaseNew == null){
            listClaseNew = new ArrayList();
        }
        
        // preparo variables para agregar participantes     
        if(listPartNew == null){
            listPartNew = new ArrayList();
        }
        
        listModalidades = modalidadFacade.findAll();
        asignaDisp = false;
        current = actImpSelected;
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad finalizada
     */
    public String prepareViewFin() {
        asignaDisp = false;
        current = actImpSelected;
        return "viewFin";
    }    
    
    /**
     * @return acción para el detalle de la entidad suspendida
     */
    public String prepareViewSusp() {
        asignaDisp = false;
        current = actImpSelected;
        return "viewSusp";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        asignaDisp = false;
        current = actImpSelected;
        return "viewDes";
    }
    
    /**
     * Método para abrir el diálogo que muestra el detalle de la clase
     */
    public void prepareViewClase(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        options.put("contentHeight", 450);
        RequestContext.getCurrentInstance().openDialog("dlgViewClase", options, null);
    }
    
    /**
     * Método para preparar la asistencia a las clases
     */
    public void prepareAddAsistencia(){
        regAsist = true;
        cargarParticipantesDisponibles();
        if(listPartDisp.isEmpty()){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Asistencias", "La Actividad Dispuesta no dispone de más "
                    + "Participantes inscriptos para registrarlos como asistentes a la Clase.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }else{
            Map<String,Object> options = new HashMap<>();
            options.put("contentWidth", 700);
            options.put("contentHeight", 450); 
            RequestContext.getCurrentInstance().openDialog("addAsistencia", options, null);
        }
    }
    
    /**
     * Método para abrir el diálogo que muestra el detalle del Participante
     */
    public void prepareViewParticipante(){
        cargarClasesXParticipante();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgViewPart", options, null);
    }
    
    /**
     * Método para abrir el diálogo que permitirá dar de alta un nuevo agente
     */
    public void prepareCreateAgente(){
        // instancio el objeto principal
        agente = new Agente();
        // instancio la Personal
        persona = new Persona();
        // seteo los listados para el formulario
        listaEstudios = estCurFacade.findAll();
        listaNivelIpap = nivelIpapFacade.findAll();
        listaTitulos = tituloFacade.findAll();
        listaSitRev = sitRevFacade.findAll();
        listaCargo = cargoFacade.findAll();  
        esReferente = false;
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("agentes/dlgNewAgente", options, null);
    }
    
    /**
     * Método para abrir el diálogo que permitirá registrar los datos personales del agente que se está creando
     */
    public void prepareCreatePersona(){
        // seteo los elementos para el formulario
        listaTipoDocs = tipoDocFacade.findAll();
        listaLocalidades = localidadFacade.findAll();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("persona/dlgNewPersona", options, null);
    }
    
    

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        asignaDisp = true;
        //cargo los list para los combos
        listResoluciones = resFacade.getHabilitadas();
        listActPlan = actPlanFacade.getHabilitadas();
        listTipoOrganismo = tipoOrgFacade.findAll();
        listSedes = sedeFacade.getHabilitados();
        listOrientaciones = orientacionFacade.findAll();
        // cargo los listados de los disponibles
        listDocDisp = docenteFacade.getHabilitadas();
        listOrgDisp = organismoFacade.getHabilitados();
        
        //identifico el rol para la selección del Coordinador solo si no es la interfase de coordinador
        if(!esCoordinador){
            List<Rol> roles = rolFacade.getXString("Coordinador");
            listCoordinadores = usuarioFacade.getUsuarioXRol(roles.get(0).getId());   
        }
        
        // intancio el current
        current = new ActividadImplementada();
        
        // seteo los elementos para agregar clases a la Actividad
        listModalidades = modalidadFacade.findAll();
        clase = new Clase();        
        if(listClaseNew == null){
            listClaseNew = new ArrayList();
        }
        
        participante = new Participante();        
        if(listPartNew == null){
            listPartNew = new ArrayList();
        }
        
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        prepararEdiciones();
        return "edit";
    }   
    
    /**
     * @return acción para la edición de la entidad suspendida
     */
    public String prepareEditSusp() {
        prepararEdiciones();     
        return "editSusp";
    }     
    
    /**
     * @return acción para la edición de la entidad finalizada
     */
    public String prepareEditFinal() {
        prepararEdiciones(); 
        return "editFinal";
    }     
    
    /**
     * Método para abrir el díálogo para la edición de Clases
     */
    public void prepareEditClase(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgEditClase", options, null);
    }
    
    /**
     * Método para abrir el díálogo para la edición de Participantes
     */
    public void prepareEditParticipante(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 850);
        options.put("contentHeight", 650);
        RequestContext.getCurrentInstance().openDialog("dlgEditPart", options, null);
    }
    
    /**
     * Método para abrir el diálogo que permitirá editar los datos de un Agente existente
     */
    public void prepareEditAgente(){
        // paso el Agente a actualizar a la propiedad del bean solo si se trata de una Actividad Dispuesta existente
        if(current.getId() != null){
            agente = participante.getAgente();
        }
                
        // seteo los listados para el formulario
        listaEstudios = estCurFacade.findAll();
        listaNivelIpap = nivelIpapFacade.findAll();
        listaTitulos = tituloFacade.findAll();
        listaSitRev = sitRevFacade.findAll();
        listaCargo = cargoFacade.findAll();  
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("agentes/dlgEditAgente", options, null);
    }    
    
    /**
     * Método para abrir el diálogo que permitirá editar los datos personales del Agente que se está actualizando
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
     *
     * @return
     */
    public String prepareInicio(){
        recreateModel();
        return "/faces/index";
    }
    
    /**
     * Método que verifica que el Actividad Implementada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = actImpSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = actImpSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplHabilitadaErrorOccured"));
            return null; 
        }
    }        
    
    /**
     * Método para suspender Implementaciones
     * @return 
     */
    public String prepareSuspender(){
        current = actImpSelected;
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setSuspendido(true);

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplSuspendida"));
            return "viewSusp";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplSuspendidaErrorOccured"));
            return null; 
        }
    }
    
    /**
     * Método para activar Implementaciones suspendidas
     * @return 
     */
    public String prepareActivar(){
        current = actImpSelected;
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setSuspendido(false);
            
            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplActivada"));
            return "view";
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplActivadaErrorOccured"));
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
    
    /**
     * Método para actualizar los subrpogramas disponibles según la Actividad Formativa seleccionada
     * @param event 
     */
    public void actFormChangeListener(ValueChangeEvent event) {
        ActividadPlan act = (ActividadPlan) event.getNewValue();
        listSubprogramas = act.getSubprogramas();
    }      
    
    public void organismoChangeListener(ValueChangeEvent event) {
        Organismo selectOrg = (Organismo)event.getNewValue();
        
        listaReferentes = new ArrayList();
        Iterator itAg = agenteFacade.getHabilitados().iterator();
        
        // recorro el dadamodel
        while(itAg.hasNext()){
            Agente ag = (Agente)itAg.next();
            if(ag.getOrganismo().equals(selectOrg) && ag.isEsReferente()){
                listaReferentes.add(ag);
            }          
        }        
    }    
    
    public void referenteChangeListener(ValueChangeEvent event){
        esReferente = !(boolean)event.getNewValue();
    }
    
    
   /*********************************************
     ** Métodos de inicialización de búsquedas **
     ********************************************/
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectHab(){
        buscarEntreFechas();
        return "list";
    }
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectFin(){
        buscarEntreFechas();
        return "listFin";
    }    
    
    /**
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectSusp(){
        buscarEntreFechas();
        return "listSusp";
    }        
    
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
     * Méto que inserta una nueva Actividad Implementada en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            if(getFacade().noExiste(current.getActividadPlan().getNombre(), current.getFechaInicio(), current.getFechaFin(), current.getSede().getId())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // Si se trata de un coordinador, lo asigno directamente
                if(usLogeado.getRol().getNombre().equals("Coordinador")){
                   current.setCoordinador(usLogeado);
                }
                
                // inserto las Clases que se cargaron
                current.setClases(listClaseNew);
                
                // inserto los Participantes que se cargaron
                current.setParticipantes(listPartNew);
                
                // Inserto la entidad
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplCreated"));
                tipoOrg = null;
                asignaDisp = false;
                limpiarListados();
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplCreatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * Método para agregar una Clase a la Actividad Dispuesta
     * @param event
     */
    public void createClase(ActionEvent event){
        // guardo la Clase en el listado si el docente esté disponible
        if(validarDocente(clase.getDocente(), clase.getFechaRealizacion(), clase.getHoraInicio(), clase.getHoraFin())){
            try{
                // asigno número de orden
                clase.setNumOrden(getNumOrdenClase(false));

                // asigno Actividad Dispuesta 
                clase.setActividad(current);
                
                // Agrego la Clase en el listado a insertar en la Actividad
                listClaseNew.add(clase);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseCreated"));

                // reseteo la variable para volverla a poblar con la próxima, si hubiera.
                clase = null;
                clase = new Clase();
            }catch(Exception e){
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseCreatedErrorOccured"));
            }
        }else{
            JsfUtil.addErrorMessage("El docente asignado tiene cubierta esta fecha y rango horario con otra clase, por favor, seleccione otro docente.");
        }        
    }
    
    /**
     * Método para agregar un Participante a la Actividad Dispuesta
     * @param event
     */
    public void createParticipante(ActionEvent event){
        try{
            // Creación de la entidad de administración y asignación
            Date date = new Date(System.currentTimeMillis());
            AdmEntidad admEnt = new AdmEntidad();
            admEnt.setFechaAlta(date);
            admEnt.setHabilitado(true);
            admEnt.setUsAlta(usLogeado);
            participante.setAdmin(admEnt);

            // asigno Actividad Dispuesta 
            participante.setActividad(current);

            // inserto el estado por defecto: Inscripto
            List<EstadoParticipante> estParts = estPartFacade.getXString("Inscripto");
            participante.setEstado(estParts.get(0));

            // Agrego el Participante en el listado a insertar en la Actividad
            listPartNew.add(participante);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteCreated"));

            // reseteo la variable para volverla a poblar con la próxima, si hubiera.
            participante = null;
            participante = new Participante();
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteCreatedErrorOccured"));
        } 
    }
    
    /**
     * Método para la persistencia del Agente creado
     */
    public void createAgente(){
        try{
            // valido que no haya exista un Agente con los mismos datos personales
            if(agenteFacade.noExiste(agente.getPersona().getId())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEntAg = new AdmEntidad();
                admEntAg.setFechaAlta(date);
                admEntAg.setHabilitado(true);
                admEntAg.setUsAlta(usLogeado);
                agente.setAdmin(admEntAg);
                
                // Seteo la condición de referente
                agente.setEsReferente(esReferente);
                
                // Inserción
                agenteFacade.create(agente);
                
                /*********************************************************************
                 ** Agrego al Agente como nuevo participante en el listado temporal **
                 *********************************************************************/ 
                // Creación de la entidad de administración y asignación
                AdmEntidad admEntPart = new AdmEntidad();
                admEntPart.setFechaAlta(date);
                admEntPart.setHabilitado(true);
                admEntPart.setUsAlta(usLogeado);
                participante.setAdmin(admEntPart);
                
                // Asigno el Agente
                participante.setAgente(agente);
                
                // Asigno Actividad Dispuesta 
                participante.setActividad(current);    
                
                // Inserto el estado por defecto: Inscripto
                List<EstadoParticipante> estParts = estPartFacade.getXString("Inscripto");
                participante.setEstado(estParts.get(0));     
                
                // Agrego el Participante en el listado
                listPartNew.add(participante);

                JsfUtil.addSuccessMessage("Se creó el Agente y se lo agregó a la Actividad Dispuesta."
                        + "Por favor, cierre la ventana correspondiente y actualice el listado de Participantes de la Actividad.");
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AgenteExistente"));
            }
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AgenteCreatedErrorOccured"));
        }
    }
    
    /**
     * Método para guardar una Persona durante el registro de un nuevo Agente
     */
    public void createPersona(){
        if(agente.getPersona() == null){
            if(personaFacade.noExiste(persona.getTipoDocumento().getId(), persona.getDocumento())){

                // Formateo el apellido
                String tempApp = persona.getApellidos();
                persona.setApellidos(tempApp.toUpperCase());

                // Agrego la persona al Agente
                agente.setPersona(persona);

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
     * Método para agregar una Clase a la Actividad Dispuesta
     * @param event
     */
    public void addClase(ActionEvent event){
        // guardo la Clase en el listado si el docente esté disponible
        if(validarDocente(clase.getDocente(), clase.getFechaRealizacion(), clase.getHoraInicio(), clase.getHoraFin())){
            try{
                // asigno número de orden
                clase.setNumOrden(getNumOrdenClase(true));

                // asigno Actividad Dispuesta 
                clase.setActividad(current);
                
                // Agrego la Clase al listado de Clases de la Actividad
                current.getClases().add(clase);
                
                // Actualizo la Actividad
                getFacade().edit(current);
                JsfUtil.addSuccessMessage("La nueva Clase se agregó a la Actividad Dispuesta, si lo desea puede seguir agregando, "
                        + "de lo contrario, por favor cierre el diálogo y actulice el listado de Clases.");

                // reseteo la variable para volverla a poblar con la próxima, si hubiera.
                clase = null;
                clase = new Clase();
            }catch(Exception e){
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseCreatedErrorOccured"));
            }
        }else{
            JsfUtil.addErrorMessage("El docente asignado tiene cubierta esta fecha y rango horario con otra clase, por favor, seleccione otro docente.");
        }  
    }    
    
    /**
     * Método para agregar un Participante a la Actividad Dispuesta
     * @param event
     */
    public void addParticipante(ActionEvent event){
        // validar que el paricipante no esté ya inscripto
        boolean repite = false;
        for(Participante part : current.getParticipantes()){
            if(part.getAgente().getId().equals(participante.getAgente().getId())){
                repite = true;
            }
        }
        if(!repite){
            try{
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                participante.setAdmin(admEnt);

                // asigno Actividad Dispuesta 
                participante.setActividad(current);

                // inserto el estado por defecto: Inscripto
                List<EstadoParticipante> estParts = estPartFacade.getXString("Inscripto");
                participante.setEstado(estParts.get(0));

                // Agrego el Participante al listado de Participantes de la Actividad
                current.getParticipantes().add(participante);

                // Actualizo la Actividad
                getFacade().edit(current);
                JsfUtil.addSuccessMessage("El nuevo Participante se inscribió en la Actividad Dispuesta, si lo desea puede seguir inscribiendo, "
                        + "de lo contrario, por favor cierre el diálogo y actulice el listado de Participantes.");

                // reseteo la variable para volverla a poblar con la próxima, si hubiera.
                participante = null;
                participante = new Participante();
            }catch(Exception e){
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteCreatedErrorOccured"));
            }
        }else{
            JsfUtil.addErrorMessage("El Agente que intenta inscribir, ya está inscripto como Paricipante de la Actividad Dispuesta.");
        }
    }    

    /**
     * Método que actualiza una nueva Actividad Implementada en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        ActividadImplementada res;
        String retorno = "";
        try {
            res = getFacade().getExistente(current.getActividadPlan().getNombre(), current.getFechaInicio(), current.getFechaFin(), current.getSede().getId());
            if(res == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Si se trata de un coordinador, lo asigno directamente
                if(usLogeado.getRol().getNombre().equals("Coordinador")){
                   current.setCoordinador(usLogeado);
                }

                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplUpdated"));
                tipoOrg = null;
                asignaDisp = false;
                limpiarListados();
                if(tipoList == 1){
                    retorno = "view";  
                }   
                if(tipoList == 3){
                    retorno = "viewSusp";  
                }      
                return retorno;
            }else{
                if(res.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);
                    
                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplUpdated"));
                    asignaDisp = false;
                    limpiarListados();
                    if(tipoList == 1){
                        retorno = "view";  
                    }   
                    if(tipoList == 3){
                        retorno = "viewSusp";  
                    }                         
                    return retorno;                   
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * Método para actualizar una Clase.
     * Recorro el listado de Clases de la Actividad dispuesta y las voy guardando en un listado de intercambio,
     * cuando me encuentro con la clase actualiza, la agrego en lugar de la versión original.
     * Finalmente reemplazo el listado original de Clases de la Actividad dispuesta, por el listado de intercambio.
     */
    public void updateClase(){
        boolean docenteOcupado = false;
        List<Clase> clasesSwap = new ArrayList<>();
        for (Clase cls : current.getClases()) {
            if(!cls.getId().equals(clase.getId())){
                // valido la disponibilidad del docente contra todas las clases persistidas
                if(!validarDocente(clase.getDocente(), clase.getFechaRealizacion(), clase.getHoraInicio(), clase.getHoraFin())){
                    docenteOcupado = true;
                }
                
                clasesSwap.add(cls);
            }else{
                clasesSwap.add(clase);
            }
        }
        if(!docenteOcupado){
            current.setClases(clasesSwap);
            getFacade().edit(current);
            // reseteo la variable para volverla a poblar con la próxima, si hubiera.
            clase = null;
            clase = new Clase();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseUpdated"));
        }else{
            JsfUtil.addErrorMessage("El docente seleccionado no está disponible para la fecha y horarios seleccionados, por favor, cierre el formulario y vuelvalo a abrir.");
        }   
    } 
    
    /**
     * Método para actualizar un Participante
     */
    public void updateParticipante(){
        boolean partInscripto = false;
        List<Participante> partSwap = new ArrayList<>();
        for (Participante part : current.getParticipantes()) {
            if(!part.getId().equals(participante.getId())){
                // valido que el Agente actualizado no esté registrado ya como Participante
                if(validarAgente(part.getAgente(), false)){
                    partInscripto = true;
                }
                partSwap.add(part);
            }else{
                partSwap.add(participante);
            }
        }
        if(!partInscripto){
            try{
                current.setParticipantes(partSwap);
                getFacade().edit(current);
                // reseteo la variable para volverla a poblar con la próxima, si hubiera.
                participante = null;
                participante = new Participante();
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteUpdated"));
            }catch(Exception e){
                JsfUtil.addErrorMessage("Hubo un error actualizando el Participante. " + e.getMessage());
            }
        }
    }
    
    /**
     * Método para actualizar un Agente seleccionado como Participante
     * Verifico si se trata de una nueva Actividad Dispuesta o de una edición.
     * Si la AD es nueva, el Agente llegará a la edición ya habiéndose inscripto
     * temporalmente como Participante de la AD, por lo tanto, luego de actualizarlo
     * en la BD, debo actualizarlo en el participante.
     * Si se trata de una AD existente, no será necesario actualizar el Agente del Participante
     */
    public void updateAgente(){
        if(current.getId() != null){
            // si es una edición, actualizo directamente
            try{
                agenteFacade.edit(agente);
                JsfUtil.addSuccessMessage("El Agente se actualizó correctamente. Por favor cierre la ventana corresponiente.");
            }catch(Exception e){
                JsfUtil.addErrorMessage("Hubo un error actualizando el Agente. " + e.getMessage());
            }
        }else{
            // si estoy dando de alta una AD, primero actualizo la BD y luego el Participante
            try{
                // actualizo la BD
                agenteFacade.edit(agente);
                
                // actualizo el Agente del Participante de la lista que estoy creando para la AD
                for(Participante part : listPartNew){
                    if(part.getAgente().getId().equals(agente.getId())){
                        part.setAgente(agente);
                    }
                }
                
                JsfUtil.addSuccessMessage("El Agente se actualizó correctamente. Por favor cierre la ventana corresponiente.");
            }catch(Exception e){
                JsfUtil.addErrorMessage("Hubo un error actualizando el Agente. " + e.getMessage());
            }
        }
    }
    
    /**
     * Método para actualizar los datos personales del Agente que se está actualiznado
     */
    public void updatePersona(){
        Persona per = personaFacade.getExistente(agente.getPersona().getTipoDocumento().getId(), agente.getPersona().getDocumento());
        if(per == null){
            // Formateo el apellido
            String tempApp = agente.getPersona().getApellidos();
            agente.getPersona().setApellidos(tempApp.toUpperCase());
            
            // Muestro mensaje de actualización
            JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Agente que está editando. Por favor, cierre la ventana correspondiente.");
        }else{
            if(per.getId().equals(agente.getPersona().getId())){
                // Formateo el apellido
                String tempApp = agente.getPersona().getApellidos();
                agente.getPersona().setApellidos(tempApp.toUpperCase());

                // Muestro mensaje de actualización
                JsfUtil.addSuccessMessage("Se actualizaron los datos personales al Agente que está editando. Por favor, cierre la ventana correspondiente.");
            }else{
                JsfUtil.addErrorMessage("Ya existe otro Agente con estos datos personales.");
            }
        }
    }
    
    /**
     * Método para eliminar Clases
     */
    public void deleteClase(){
        if(!clase.getParticipantes().isEmpty()){
            // no elimino
            JsfUtil.addErrorMessage("La clase que quiere eliminar tiene asistentes.");
        }else{
            try{
                current.getClases().remove(clase);
                claseFacade.remove(clase);
                getFacade().edit(current);
                JsfUtil.addSuccessMessage("Clase eliminada. Por favor, actualice el lsitado de Clases");
            }catch(Exception e){
                JsfUtil.addErrorMessage("Hubo un error eliminando la Clase. " + e.getMessage());
            }
        }
    }        
    
    /**
     * Método para deshabilitar Inscripciones de Participantes
     */
    public void deleteParticipante(){
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            participante.getAdmin().setFechaBaja(date);
            participante.getAdmin().setUsBaja(usLogeado);
            participante.getAdmin().setHabilitado(false);

            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Inscripción deshabilitada. Por favor, actualice el lsitado de Participantes");
        }catch(Exception e){
            JsfUtil.addErrorMessage("Hubo un error deshabilitando el Participante. " + e.getMessage());
        }
    }
    
    /**
     * Método para rehabilitar Inscripciones de Participantes
     */
    public void habilitarParticipante(){
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            participante.getAdmin().setFechaModif(date);
            participante.getAdmin().setUsModif(usLogeado);
            participante.getAdmin().setHabilitado(true);
            participante.getAdmin().setUsBaja(null);
            participante.getAdmin().setFechaBaja(null);

            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Inscripción deshabilitada. Por favor, actualice el lsitado de Participantes");
        }catch(Exception e){
            JsfUtil.addErrorMessage("Hubo un error deshabilitando el Participante. " + e.getMessage());
        }
    }
    
    /**
     * Método para registrar las asistencias a la Clase correspondiente a la Actividad Dispuesta seleccionada.
     * Realizo el mismo procedimiento que para la actualización de la clase.
     */
    public void registrarAsistencias(){
        List<Clase> clasesSwap = new ArrayList<>();
        for (Clase cls : current.getClases()) {
            if(!cls.getId().equals(clase.getId())){
                clasesSwap.add(cls);
            }else{
                clasesSwap.add(clase);
            }
        }
        try{
            current.setClases(clasesSwap);
            getFacade().edit(current);
            // reseteo la variable para volverla a poblar con la próxima, si hubiera.
            clase = null;
            clase = new Clase();
            JsfUtil.addSuccessMessage("Las asistencias se registraron correctamente.");
        }catch(Exception e){
            JsfUtil.addErrorMessage("Hubo un error registrando las asistencias. " + e.getMessage());
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = actImpSelected;
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
    
    /**
     * Método para actualizar los Organismos según el tipo seleccionado
     * @param event
     */
    public void tipoOrgChangeListener(ValueChangeEvent event) {   
        tipoOrg = (TipoOrganismo)event.getNewValue();
        listOrganismos = organismoFacade.getXTipo(tipoOrg);
    } 
    
    /**
     * Método para asignar un Organismo destinatario a la Actividad Dispuesta durante su edición
     * @param org
     */
    public void asignarOrgDestinatario(Organismo org){
        current.getOrganismosDestinatarios().add(org);
        listOrgDisp.remove(org);
        if(listOrgFilter != null){
            listOrgFilter.clear();
        }
    }  
    
    /**
     * Método para asignar un Docente a la Actividad Dispuesta durante su edición
     * @param doc
     */
    public void asignarDocente(Docente doc){
        current.getDocentesVinculados().add(doc);
        listDocDisp.remove(doc);
        if(listDocFilter != null){
            listDocFilter.clear();
        }
    }     
    
    /**
     * Mpetodo para asignar un asistente a la Clase
     * @param part
     */
    public void asignarAsistencia(Participante part){
        clase.getParticipantes().add(part);
        listPartDisp.remove(part);
        if(listPartFilter != null){
            listPartFilter.clear();
        }
    }
    
    /**
     * Método para desvincular un Organismo destinatario a la Actividad Dispuesta durante su edición
     * @param org
     */
    public void quitarOrgDestinatario(Organismo org){
        current.getOrganismosDestinatarios().remove(org);
        listOrgDisp.add(org);
        if(listOrgFilter != null){
            listOrgFilter.clear();
        }
    }     
    
    /**
     * Método para desvincular un Docente a la Actividad Dispuesta durante su edición
     * @param doc
     */
    public void quitarDocente(Docente doc){
        current.getDocentesVinculados().remove(doc);
        listDocDisp.add(doc);
        if(listDocFilter != null){
            listDocFilter.clear();
        }
    }     
    
    /**
     * Método para desvincular un Participante como asistente a la Clase de la Actividad Dispuesta actual
     * @param part
     */
    public void quitarAsistencia(Participante part){
        clase.getParticipantes().remove(part);
        listPartDisp.add(part);
        if(listPartFilter != null){
            listPartFilter.clear();
        }
    }
    
    /**
     * Método para mostrar el diálogo que perimtirá crear las Clases y agregarlas a la Actividad Dispuesta que se está creando
     */
    public void cargarClases(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("clases/dlgNewClase", options, null);
    }    
    
    /**
     * Método para mostrar el diálogo que perimtirá crear los Participantes y agregarlos a la Actividad Dispuesta que se está creando
     */
    public void cargarParticipantes(){
        if(!current.getOrganismosDestinatarios().isEmpty()){
            Map<String,Object> options = new HashMap<>();
            options.put("contentWidth", 850);
            options.put("contentHeight", 650);
            RequestContext.getCurrentInstance().openDialog("participantes/dlgNewPart", options, null);
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscripciones", "La Actividad debe tener al menos un "
                    + "Organismo destinatario seleccionado para poder inscibir los respectivos Agentes.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }   
    
    /**
     * Método para cargar el listado con los Participantes disponibles para agregarlos como asistentes a una Clase
     */
    private void cargarParticipantesDisponibles(){
        listPartDisp = new ArrayList<>();
        List<Participante> parts = current.getParticipantes();
        for(Participante part : parts){
            if(!clase.getParticipantes().contains(part)){
                listPartDisp.add(part);
            }
        }
    }
    
    /**
     * Método para mostrar el diálogo que perimtirá agregar Clases a la Actividad Dispuesta existente
     */
    public void agregarClases(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgAddClase", options, null);
    }      
    
    /**
     * Método para mostrar el diálogo que perimtirá agregar Participantes a la Actividad Dispuesta existente
     */
    public void agregarParticipantes(){
        participante = new Participante();   
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 850);
        options.put("contentHeight", 650);
        RequestContext.getCurrentInstance().openDialog("dlgAddPart", options, null);
    }      
    
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public ActividadImplementada getActividadImplementada(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbActividadImpl");
     
        return "inicio";
    }      
    
    public void verParticipantes(){
        listPart = current.getParticipantes();
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        options.put("contentHeight", 700);
        RequestContext.getCurrentInstance().openDialog("participantes/dlgParticipantes", options, null);
    }    
    
    public void verClases(){
        regAsist = false;
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 1100);
        options.put("contentHeight", 500);
        RequestContext.getCurrentInstance().openDialog("clases/dlgClases", options, null);
    }      
    
    public void verClasesXParticipante(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 550);
        RequestContext.getCurrentInstance().openDialog("dlgClasesXActividad", options, null);
    }     
    
    public void verOrgDisp(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgOrgDisp", options, null);
    }
    
    public void verOrgVinc(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgOrgVinc", options, null);
    }    
    
    public void verDocDisp(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgDocDisp", options, null);
    }  
    
    public void verDocVinc(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgDocVinc", options, null);
    }        
    
    /**
     * Método que abre el diálogo para gestionar los asistentes a una Clase vinculada a la Actividad Dispuesta
     */
    public void verAsistentes(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 650);
        RequestContext.getCurrentInstance().openDialog("dlgAsistVinc", options, null); 
    }  
    
    /**
     * Método para ver los Participantes de la Actividad Dispuesta disponibles para asignarlos como asistentes a la Clase
     */
    public void verParticipantesDisp(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 650);
        RequestContext.getCurrentInstance().openDialog("dlgAsistDisp", options, null);        
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
                    if(!s.equals("mbActividadImpl") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    
    
     /**
     * Método para la fecha de realización de la clase
     * No deberá ser anterior a la fecha de inicio de la vigencia del curso
     * ni anterior a la fecha de la clase inmediatemente anterior
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void validarFechaRelizacion(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{   
        Date fechaInicioCurso = current.getFechaInicio();
        Date fechaFinCurso = current.getFechaFin();
        SimpleDateFormat form_1 = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        String strFechaInicioCurso = form_1.format(fechaInicioCurso);
        String strFechaFinCurso = form_1.format(fechaFinCurso);
        Date fechaPropuesta = (Date)arg2;
        
        if(claseFacade.isClasesEmpty(current)){
            if(fechaInicioCurso.after(fechaPropuesta)){
                FacesMessage message = new FacesMessage("La fecha de realización de la clase no debe ser anterior a la de inicio del curso " + strFechaInicioCurso);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        }else{
            Date ultimaFecha = claseFacade.getLastFechaRelizacion(current);
            if(!fechaPropuesta.after(ultimaFecha)){
                SimpleDateFormat form_2 = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
                String strUltimaFecha = form_2.format(ultimaFecha);
                FacesMessage message = new FacesMessage("La fecha de realización de la clase debe ser posterior a la fecha de la clase anterior: " + strUltimaFecha);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        }
        if(fechaPropuesta.after(fechaFinCurso)){
            FacesMessage message = new FacesMessage("La fecha de realización de la clase no debe ser posterior al fin del curso " + strFechaFinCurso);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
    
    /**
     * Método para validar que el porcentaje de asistencia sea entre 0,00 y 1,00
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void validarPorcAsist(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        Double valor = (Double)arg2;
        if(valor < 0 || valor > 1){
            throw new ValidatorException(new FacesMessage("El valor del porcentaje de asistencia debe ser entre 0,00 y 1,00"));
        }
    }    
    
    /**
     * Método para el autocompletado de Agentes para la selección de Participantes para una Actividad existente
     * @param query: caracter ingresado en el input
     * @return 
     */
    public List<Agente> completeAgenteEdit(String query){
        List<Agente> result = new ArrayList<>();
        List<Agente> agentesVinculados = getFacade().getAgentesXOrganismos(current.getOrganismosDestinatarios());
        if(!agentesVinculados.isEmpty()){
            String dni;
            for(Agente ag : agentesVinculados){
                if(validarAgente(ag, false)){
                    dni = Integer.toString(ag.getPersona().getDocumento());
                    if(dni.startsWith(query))
                        result.add(ag);
                }
            }
            // Si no encontré Agentes disponibles lo comunico al usuario
            if(result.isEmpty()){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscripciones", "No hay Agentes disponibles para la inscripción que respondan a la búsqueda, "
                            + "pertenecientes a los Organismos destinatarios seleccionados.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscripciones", "No hay Agentes registrados en los Organismos destinatarios de la Actividad");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        return result;
    }
    
    /**
     * Método para el autocompletado de Agentes para la selección de Participantes para una Actividad durante su creación
     * @param query: caracter ingresado en el input
     * @return 
     */
    public List<Agente> completeAgenteNew(String query){
        List<Agente> result = new ArrayList<>();
        List<Agente> agentesVinculados = getFacade().getAgentesXOrganismos(current.getOrganismosDestinatarios());
        if(!agentesVinculados.isEmpty()){
            String dni;
            for(Agente ag : agentesVinculados){
                if(validarAgente(ag, true)){
                    dni = Integer.toString(ag.getPersona().getDocumento());
                    if(dni.startsWith(query))
                        result.add(ag);
                }
            }
            // Si no encontré Agentes disponibles lo comunico al usuario
            if(result.isEmpty()){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscripciones", "Ya no quedan Agentes disponibles para la inscripción, "
                            + "pertenecientes a los Organismos destinatarios seleccionados.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscripciones", "No hay Agentes registrados en los Organismos destinatarios de la Actividad");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        return result;
    }  
    
    /**
     * Método para limpiar los datos del Participante seleccionado
     * @param envent
     */
    public void limpiarParticipante(ActionEvent envent){
        participante = null;
        participante = new Participante();
    }
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ActividadImplementadaFacade getFacade() {
        return actImpFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        if(listSubprogramas != null){
            listSubprogramas.clear();
            listSubprogramas = null;
        }
        if(listado != null){
            listado.clear();
            listado = null;
        }
        if(listClaseNew != null){
            listClaseNew = null;
        }
        if(listPartNew != null){
            listPartNew = null;
        }
        if(listClasesXPart != null){
            listClasesXPart = null;
        }     
        if(listPart != null){
            listPart = null;
        }
        if(listPartDisp != null){
            listPartDisp = null;
        }
        actImpSelected = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadImplDeletedErrorOccured"));
        }
    }        
    
    /**
     * Método para llenar los listados de elementos disponibles para su asignación
     */
    private void cargarListados(){
        cargarOrganismosDisponibles();
        cargarDocentesDisponibles();
    }
    
    /**
     * Método para limpiar los listados poblados para la edición de la entidad
     */
    private void limpiarListados(){
        listResoluciones.clear();
        listActPlan.clear();
        tipoOrg = null;
        listTipoOrganismo.clear();
        listOrganismos.clear();
        listSedes.clear();
        listOrientaciones.clear();
        listSubprogramas.clear();
        listDocDisp.clear();
    }
    
    /**
     * Mátodo para cargar los Organismos disponibles para asignarlos a una Actividad Dispuesta
     */
    private void cargarOrganismosDisponibles(){
        if(!current.getOrganismosDestinatarios().isEmpty()){
            listOrgDisp = new ArrayList<>();
            List<Organismo> orgs = organismoFacade.getHabilitados();
            Iterator itOrg = orgs.listIterator();
            while(itOrg.hasNext()){
                Organismo org = (Organismo)itOrg.next();
                if(!current.getOrganismosDestinatarios().contains(org)){
                    listOrgDisp.add(org);
                }
            }
        }else{
            listOrgDisp = organismoFacade.getHabilitados();
        }
    }  
    
    /**
     * Mátodo para cargar los Docentes disponibles para asignarlos a una Actividad Dispuesta
     */
    private void cargarDocentesDisponibles(){
        if(!current.getDocentesVinculados().isEmpty()){
            listDocDisp = new ArrayList<>();
            List<Docente> docs = docenteFacade.getHabilitadas();
            Iterator itDoc = docs.listIterator();
            while(itDoc.hasNext()){
                Docente doc = (Docente)itDoc.next();
                if(!current.getDocentesVinculados().contains(doc)){
                    listDocDisp.add(doc);
                }
            }     
        }else{
            listDocDisp = docenteFacade.getHabilitadas();
        }
    }
    
    /**
     * Método para validad que el docente de la Clase no esté ocupado en este horario
     * Valido tanto en la BD como en el listado de nuevos
     * 
     * @param docente
     * @param fecha
     * @param horaInicio
     * @param horaFin
     * @return 
     */
    private boolean validarDocente(Docente docente, Date fecha, Date horaInicio, Date horaFin) {
        boolean result = true;
        if(!docenteFacade.isDisponible(docente, fecha, horaInicio, horaFin, current)){
            result = false;
        }else{
            for (Clase cls : listClaseNew) {
                if(!cls.getId().equals(clase.getId())){
                    // valido si tienen el mismo docente y fecha de realización
                    if(cls.getDocente().equals(clase.getDocente()) && cls.getFechaRealizacion().equals(clase.getFechaRealizacion())){
                        // si hay coincidencia verifico que no se superpongan horarios
                        if(cls.getHoraFin().after(clase.getHoraInicio()) || cls.getHoraInicio().before(clase.getHoraFin())){
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Metodo para validar que el Participante no haya sido inscripto en la Actividad Dispuesta
     * Valido tanto en la BD como en el listado de nuevos
     */
    private boolean validarAgente(Agente agente, boolean lista){
        boolean result = true;
        if(lista){
            for(Participante part : listPartNew){
                if(part.getAgente().equals(agente)){
                    result = false;
                }
            }  
        }else{
            if(!participanteFacade.noExiste(agente, current)){
                result = false;
            } 
        }
        return result;
    }
    
    /**
     * Método para obtener el número de orden para la nueva clase
     * Si update = 1 => la Actividad Dispuesta existe, el número se obtiene de las clases del current.
     * Si update = 0 => la Actividad Dispuesta está en proceso de registración, el número se obtiene del listado con las Clases ya creadas.
     */
    private int getNumOrdenClase(boolean update){
        int result;
        if(update){
            result = current.getClases().size() + 1;
        }else{
            if(listClaseNew.isEmpty()){
                result = 1;
            }else{
                result = listClaseNew.size() + 1;
            }
        }
        return result;
    }    
    
    /**
     * Método para operar los diferentes prepareEdit
     */
    private void prepararEdiciones(){
        asignaDisp = true;
        
        //cargo los list para los combos
        listResoluciones = resFacade.getHabilitadas();
        listActPlan = actPlanFacade.getHabilitadas();
        listOrganismos = organismoFacade.getHabilitados();
        listSedes = sedeFacade.getHabilitados();
        listOrientaciones = orientacionFacade.findAll();
        
        //identifico el rol para la selección del Coordinador solo si no es la interfase de coordinador
        if(!esCoordinador){
            List<Rol> roles = rolFacade.getXString("Coordinador");
            listCoordinadores = usuarioFacade.getUsuarioXRol(roles.get(0).getId());   
        }
        
        current = actImpSelected;
        cargarListados();
        listTipoOrganismo = tipoOrgFacade.findAll();
        if(current.getOrganismo() != null){
            tipoOrg = current.getOrganismo().getTipoOrganismo();
        }
    }
    
    /**
     * Matodo para setear las Clases tomadas por el Participante para una Activiadad Dispuesta
     */
    private void cargarClasesXParticipante(){
        listClasesXPart = new ArrayList<>();
        for (Clase cls : participante.getClases()) {
            if(cls.getActividad().equals(current)){
                listClasesXPart.add(cls);
            }
        }
    }
    
    /******************************
     * Método para limpiar listados
     ******************************/
    
    private void limpiarListadosAgente(){
        if(listaEstudios != null){
            listaEstudios = null;
        }
        if(listaNivelIpap != null){
            listaNivelIpap = null;
        }
        if(listaTitulos != null){
            listaTitulos = null;
        }
        if(listaSitRev != null){
            listaSitRev = null;
        }
        if(listaCargo != null){
            listaCargo = null;   
        }
        if(listaReferentes != null){
            listaReferentes = null;   
        }
        if(listaReferentes != null){
            listaReferentes = null;   
        }
    }
    
    private void limpiarListadosPersona(){
        if(listaTipoDocs != null){
            listaTipoDocs = null;
        }
        if(listaLocalidades != null){
            listaLocalidades = null;
        }
    }

    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<ActividadImplementada> actImpls = new ArrayList();
        for (ActividadImplementada actImp : listado) {
            if(actImp.getFechaInicio().after(fDespuesDe) && actImp.getFechaInicio().before(fAntesDe)){
                actImpls.add(actImp);
            }
        }        
        listado = actImpls; 
    }     

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public EstudiosCursadosFacade getEstCurFacade() {
        return estCurFacade;
    }

    public void setEstCurFacade(EstudiosCursadosFacade estCurFacade) {
        this.estCurFacade = estCurFacade;
    }

    public AgenteFacade getAgenteFacade() {
        return agenteFacade;
    }

    public void setAgenteFacade(AgenteFacade agenteFacade) {
        this.agenteFacade = agenteFacade;
    }

    public NivelIpapFacade getNivelIpapFacade() {
        return nivelIpapFacade;
    }

    public void setNivelIpapFacade(NivelIpapFacade nivelIpapFacade) {
        this.nivelIpapFacade = nivelIpapFacade;
    }

    public TituloFacade getTituloFacade() {
        return tituloFacade;
    }

    public void setTituloFacade(TituloFacade tituloFacade) {
        this.tituloFacade = tituloFacade;
    }

    public SituacionRevistaFacade getSitRevFacade() {
        return sitRevFacade;
    }

    public void setSitRevFacade(SituacionRevistaFacade sitRevFacade) {
        this.sitRevFacade = sitRevFacade;
    }

    public CargoFacade getCargoFacade() {
        return cargoFacade;
    }

    public void setCargoFacade(CargoFacade cargoFacade) {
        this.cargoFacade = cargoFacade;
    }

 
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = ActividadImplementada.class)
    public static class ActividadImplementadaControllerConverter implements Converter {

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
            MbActividadImpl controller = (MbActividadImpl) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbActividadImpl");
            return controller.getActividadImplementada(getKey(value));
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
            if (object instanceof ActividadImplementada) {
                ActividadImplementada o = (ActividadImplementada) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ActividadImplementada.class.getName());
            }
        }
    }             
}