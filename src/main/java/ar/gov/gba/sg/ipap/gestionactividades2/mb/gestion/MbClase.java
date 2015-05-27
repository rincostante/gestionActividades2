/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.gestion;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ModalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ClaseFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
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
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbClase implements Serializable{
    
    private Clase current;
    private List<Clase> listado; 
    private List<Clase> listadoFilter;  
    
    @EJB
    private ClaseFacade claseFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private ActividadImplementadaFacade cursoFacade;
    @EJB
    private ModalidadFacade modalidadFacade;
    @EJB
    private ParticipanteFacade participanteFacade;
    
    private Usuario usLogeado;     
    private List<Docente> listDocentes;
    private List<ActividadImplementada> listActImp;
    private List<Modalidad> listModalidades;
    private List<Participante> listPartVinc;
    private List<Participante> listPartDisp;
    private List<Participante> listPartVincFilter;
    private List<Participante> listPartDispFilter;
    private ActividadImplementada cursoSelected;
    private int ultimoNumOrdenAsignado;
    private Date fAntesDe;
    private Date fDespuesDe;
    private boolean asignaPart; 
    private MbLogin login;
    private int tipoList; //1=vigentes | 2=finalizadas | 3=deshabilitados   
    private boolean esCoordinador;
    private boolean iniciado;

    /**
     * Creates a new instance of MbClase
     */
    public MbClase(){
    }
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa los datos del usuario
     */
    @PostConstruct
    public void init(){
        tipoList = 1;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        esCoordinador = usLogeado.getRol().getNombre().equals("Coordinador");
    }

    /********************************
     ** Getters y Setters ***********
     ********************************/ 
    
    public List<Clase> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<Clase> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }
 
    public List<Participante> getListPartVincFilter() {
        return listPartVincFilter;
    }

    public void setListPartVincFilter(List<Participante> listPartVincFilter) {
        this.listPartVincFilter = listPartVincFilter;
    }

    public List<Participante> getListPartDispFilter() {
        return listPartDispFilter;
    }

    public void setListPartDispFilter(List<Participante> listPartDispFilter) {
        this.listPartDispFilter = listPartDispFilter;
    }
 
    
    public boolean isEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }
 
    
    public List<Participante> getListPartDisp() {
        return listPartDisp;
    }

    public void setListPartDisp(List<Participante> listPartDisp) {
        this.listPartDisp = listPartDisp;
    }
 
    public boolean isAsignaPart() {
        return asignaPart;
    }

    public void setAsignaPart(boolean asignaPart) {
        this.asignaPart = asignaPart;
    }
 
    
    public List<Participante> getListPartVinc() {
        return listPartVinc;
    }

    public void setListPartVinc(List<Participante> listPartVinc) {
        this.listPartVinc = listPartVinc;
    }
    
    public ActividadImplementada getCursoSelected() {
        return cursoSelected;
    }

    public void setCursoSelected(ActividadImplementada cursoSelected) {
        this.cursoSelected = cursoSelected;
    }
 
    
    public Clase getCurrent() {
        return current;
    }

    public void setCurrent(Clase current) {
        this.current = current;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public List<Docente> getListDocentes() {
        return listDocentes;
    }

    public void setListDocentes(List<Docente> listDocentes) {
        this.listDocentes = listDocentes;
    }

    public List<ActividadImplementada> getListActImp() {
        return listActImp;
    }

    public void setListActImp(List<ActividadImplementada> listActImp) {
        this.listActImp = listActImp;
    }

    public List<Modalidad> getListModalidades() {
        return listModalidades;
    }

    public void setListModalidades(List<Modalidad> listModalidades) {
        this.listModalidades = listModalidades;
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
 
    
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Clase getSelected() {
        if (current == null) {
            current = new Clase();
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public List<Clase> getListado() {
        if (listado == null) {
            if(esCoordinador){
                switch(tipoList){
                    case 1: listado = getFacade().getHabilitadasXCoor(usLogeado);
                        break;
                    case 2: listado = getFacade().getFinalizadasXCoor(usLogeado);
                        break;
                    default: listado = getFacade().getDeshabilitadasXCoor(usLogeado);  
                }
            }else{
                switch(tipoList){
                    case 1: listado = getFacade().getHabilitadas();
                        break;
                    case 2: listado = getFacade().getFinalizadas();
                        break;
                    default: listado = getFacade().getDeshabilitadas();
                }
            }
        }
        return listado;
    }    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de las Clases habilitadas y vigentes
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        asignaPart = false;
        tipoList = 1;
        recreateModel();
        if(listPartVinc != null){
            listPartVinc.clear();
        }
        if(listPartDisp != null){
            listPartDisp.clear();
        }        
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de las Clases finalizadas
     * @return acción para el listado de entidades
     */
    public String prepareListFin() {
        tipoList = 2;
        asignaPart = false;
        recreateModel();
        if(listPartVinc != null){
            listPartVinc.clear();
        }
        if(listPartDisp != null){
            listPartDisp.clear();
        } 
        return "listFin";
    }          
    
    /**
     * Método para inicializar el listado de las Clases deshabilitadas
     * @return 
     */
    public String prepareListDes() {
        tipoList = 3;
        asignaPart = false;
        recreateModel();
        if(listPartVinc != null){
            listPartVinc.clear();
        }
        if(listPartDisp != null){
            listPartDisp.clear();
        } 
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        asignaPart = false;
        listPartVinc = current.getParticipantes();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewFin() {
        asignaPart = false;
        listPartVinc = current.getParticipantes();
        return "viewFin";
    }        
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        asignaPart = false;
        listPartVinc = current.getParticipantes();
        return "viewDes";
    }

    /**
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        //cargo los list para los combos
        listDocentes = docenteFacade.getHabilitadas();
        // si es coordinador solo levanto sus cursos
        if(esCoordinador){
            listActImp = cursoFacade.getHabilitadasXCoor(usLogeado);
        }else{
            listActImp = cursoFacade.getHabilitadas();
        }
        listModalidades = modalidadFacade.findAll();
        current = new Clase();
        ultimoNumOrdenAsignado = 0;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listDocentes = docenteFacade.getHabilitadas();
        // si es coordinador solo levanto sus cursos
        if(esCoordinador){
            listActImp = cursoFacade.getHabilitadasXCoor(usLogeado);
        }else{
            listActImp = cursoFacade.getHabilitadas();
        }
        listModalidades = modalidadFacade.findAll();
        return "edit";
    }
    
    /**
     * @return acción para la edición de la entidad finalizada
     */
    public String prepareEditFin() {
        //cargo los list para los combos
        listDocentes = docenteFacade.getHabilitadas();
        // si es coordinador solo levanto sus cursos
        if(esCoordinador){
            listActImp = cursoFacade.getHabilitadasXCoor(usLogeado);
        }else{
            listActImp = cursoFacade.getHabilitadas();
        }
        listModalidades = modalidadFacade.findAll();
        return "editFin";
    }     
    
    /**
     * @return acción para la edición de la entidad finalizada
     */
    public String prepareEditProv() {
        //cargo los list para los combos
        listDocentes = docenteFacade.getHabilitadas();
        // si es coordinador solo levanto sus cursos
        if(esCoordinador){
            listActImp = cursoFacade.getHabilitadasXCoor(usLogeado);
        }else{
            listActImp = cursoFacade.getHabilitadas();
        }
        listModalidades = modalidadFacade.findAll();
        return "editProv";
    }    
    
    /**
     * Método para preparar la asistencia a las clases
     * @return 
     */
    public String prepareAddAsistencia(){
        asignaPart = true;
        listPartVinc = current.getParticipantes();
        listPartDisp = cargarParticipantesDisponibles();
        return "addAsistencia";
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
     * Método que verifica que el Clase que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            // No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseNonDeletable"));
        }
        listPartVinc = current.getParticipantes();
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseHabilitado"));
            listPartVinc = current.getParticipantes();
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseHabilitadaErrorOccured"));
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
     * Méto que inserta un nuevo Clase en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        if(!validarDocente(current.getDocente(), current.getFechaRealizacion(), current.getHoraInicio(), current.getHoraFin())){
            JsfUtil.addErrorMessage("El docente asignado tiene cubierta esta fecha y rango horario con otra clase, por favor, seleccione otro docente.");
            return null;
        }
        try {
            if(getFacade().noExiste(current.getActividad(), current.getNumOrden())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);

                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseCreated"));
                listDocentes.clear();
                listActImp.clear();
                listModalidades.clear();
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza una nueva Clase en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        Clase res;
        String retorno = "";
        try {
            res = getFacade().getExistente(current.getActividad(), current.getNumOrden());
            if(res == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseUpdated"));
                listDocentes.clear();
                listActImp.clear();
                listModalidades.clear();
                if(tipoList == 1){
                    retorno = "view";  
                }
                if(tipoList == 2){
                    retorno = "viewFin";  
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
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseUpdated"));
                    listDocentes.clear();
                    listActImp.clear();
                    listModalidades.clear();
                    if(tipoList == 1){
                        retorno = "view";  
                    }
                    if(tipoList == 2){
                        retorno = "viewFin";  
                    }  
                    return retorno;                   
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * Método para registrar asistencias a la clase
     * @return 
     */
    public String registrarAsistencias(){
        try{
            // Actualizo los datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);

            // Persisto
            getFacade().edit(current);
            
            // Resteo los list
            listPartVinc.clear();
            listPartDisp.clear();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseAsistenciaRegistrada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseAsistenciaErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
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
    public Clase getClase(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbClase");

        return "inicio";
    }    
    
    /**
     * Método para ver los Participantes del curso disponebles para asgnarles asistencia
     */
    public void verParticipantesDisp(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgAsistDisp", options, null);        
    }
    
    public void verAsistentes(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgAsistVinc", options, null); 
    }
    
    public void asignarAsistencia(Participante part){
        listPartVinc.add(part);
        listPartDisp.remove(part);
        if(listPartVincFilter != null){
            listPartVincFilter = null;
        }
        if(listPartDispFilter != null){
            listPartDispFilter = null;
        }        
        RequestContext.getCurrentInstance().closeDialog("dlgAsistDisp");
    }
    
    public void quitarAsistencia(Participante part){
        listPartVinc.remove(part);
        listPartDisp.add(part);
        if(listPartVincFilter != null){
            listPartVincFilter = null;
        }
        if(listPartDispFilter != null){
            listPartDispFilter = null;
        }
        RequestContext.getCurrentInstance().closeDialog("dlgAsistVinc");
    }    
    
    public void limpiarParticipantes(){   
        listPartVinc = current.getParticipantes();
        listPartDisp = cargarParticipantesDisponibles();
        if(listPartVincFilter != null){
            listPartVincFilter = null;
        }
        if(listPartDispFilter != null){
            listPartDispFilter = null;
        }
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
                    if(!s.equals("mbClase") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }    
    
    /*********************
     *** Validaciones ****
     *********************/
    
    /**
     * Método para validad que el docente no esté ocupado en este horario
     * @param docente
     * @param fecha
     * @param horaInicio
     * @param horaFin
     * @return 
     */
    private boolean validarDocente(Docente docente, Date fecha, Date horaInicio, Date horaFin) {
        return docenteFacade.isDisponible(docente, fecha, horaInicio, horaFin);
    }

    /**
     * Método que lee el curso seleccionado y obtiene el último número de orden adjudicado a una clase
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void actualizarCurso(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        cursoSelected = (ActividadImplementada)arg2;
        ultimoNumOrdenAsignado = claseFacade.getUltimoNumeroDeOrden(cursoSelected);
    }
    
    /**
     * Método para validad el número de orden
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void validarNumeroDeOrden(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        Long numCorrespondiente = Long.valueOf(ultimoNumOrdenAsignado + 1);
        Long selectNum = (Long)arg2;
        if(!arg2.equals(numCorrespondiente)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseValidation_numOrden") + numCorrespondiente));
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
        Date fechaInicioCurso = cursoSelected.getFechaInicio();
        Date fechaFinCurso = cursoSelected.getFechaFin();
        SimpleDateFormat form_1 = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        String strFechaInicioCurso = form_1.format(fechaInicioCurso);
        String strFechaFinCurso = form_1.format(fechaFinCurso);
        Date fechaPropuesta = (Date)arg2;
        
        if(claseFacade.isClasesEmpty(cursoSelected)){
            if(fechaInicioCurso.after(fechaPropuesta)){
                FacesMessage message = new FacesMessage("La fecha de realización de la clase no debe ser anterior a la de inicio del curso " + strFechaInicioCurso);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(message);
            }
        }else{
            Date ultimaFecha = claseFacade.getLastFechaRelizacion(cursoSelected);
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

    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ClaseFacade getFacade() {
        return claseFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        listado.clear();
        listado = null;
        if(listPartVincFilter != null){
            listPartVincFilter = null;
        }
        if(listPartDispFilter != null){
            listPartDispFilter = null;
        }
        if(listadoFilter != null){
            listadoFilter = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ClaseDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ClaseDeletedErrorOccured"));
        }
    }        
    
   
    /**
     * 
     */
    private List<Participante> cargarParticipantesDisponibles(){
        List<Participante> parts = participanteFacade.getParticipantesXCurso(current.getActividad());
        List<Participante> partSelect = new ArrayList();
        Iterator itSubs = parts.listIterator();
        while(itSubs.hasNext()){
            Participante part = (Participante)itSubs.next();
            if(!listPartVinc.contains(part)){
                partSelect.add(part);
            }
        }
        return partSelect;
    }    
    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<Clase> clases = new ArrayList();
        for (Clase clase : listado) {
            if(clase.getFechaRealizacion().after(fDespuesDe) && clase.getFechaRealizacion().before(fAntesDe)){
                clases.add(clase);
            }
        }        
        listado = clases; 
    }     
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Clase.class)
    public static class ClaseControllerConverter implements Converter {

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
            MbClase controller = (MbClase) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbClase");
            return controller.getClase(getKey(value));
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
            if (object instanceof Clase) {
                Clase o = (Clase) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Clase.class.getName());
            }
        }
    }                 
}
