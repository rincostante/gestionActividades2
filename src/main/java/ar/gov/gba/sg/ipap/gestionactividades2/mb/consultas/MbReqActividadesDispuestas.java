
package ar.gov.gba.sg.ipap.gestionactividades2.mb.consultas;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Programa;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Sede;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoCapacitacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadPlanFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ModalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SedeFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SubProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoCapacitacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoOrganismoFacade;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 * Bean de respaldo para gestionar las consultas sobre Actividades Dispuestas
 * @author rincostante
 */
public class MbReqActividadesDispuestas implements Serializable{
    /**
     * campos para los parámetros de búsqueda
     */
    private Programa programa;
    private SubPrograma subprograma;
    private ActividadPlan actForm;
    private TipoOrganismo tipoOrg;
    private Organismo organismoSol;
    private Sede sede;
    private Modalidad modalidad;
    private TipoCapacitacion tipoCap;
    private Date fDespuesDe;
    private Date fAntesDe;
    
    /**
     * listados para poblar los combos que permitirán la selección de parámetros
     */
    private List<Programa> listProgramas;
    private List<SubPrograma> listSubProgramas;
    private List<ActividadPlan> listActForm;
    private List<TipoOrganismo> listTipoOrg;
    private List<Organismo> listOrgSol;
    private List<Sede> listSedes;
    private List<Modalidad> listModalidades;
    private List<TipoCapacitacion> listTipoCap;
    
    /**
     * Campos para gestionar las Actividades Dispuestas consultadas
     */
    private List<ActividadImplementada> listActDisp;
    private List<ActividadImplementada> listActDispFilter;
    private ActividadImplementada current;
    private boolean consultado;
    
    /**
     * Campos para los totales del resumen general
     */
    private int subProgramas;
    private int sedes;
    private int clases;
    private int inscriptos;
    private int participantes;
    private int aprobados;
    private int finalizadas;
    private int orgDestinatarios;
    private int orgSolicitantes;
    private int modalidades;
    private int tiposCapacitacion;
    
    /**
     * Campos para el listado de participantes de las AD seleccionadas
     */
    private List<Participante> listAgList;
    private List<Participante> listAgListFilter;
    private Participante partSel;
    
    /**
     * Campos para el listado de organismos de las AD seleccionadas  
     */ 
    private List<Organismo> listOrgList;
    private List<Organismo> listOrgListFilter;
    private Organismo orgSel;    
    
    /**
     * Campos para el listado de clases de una AD seleccionada del total consultado
     */
    private Clase clase;
    private List<Clase> listClasesFilter;
    
    /**
     * Campos para el listado de Organismos para una AD seleccionada
     */
    private boolean porAd;
    
    /**
     * Campos de uso interno
     */
    private boolean iniciado;

    /**
     * Inyección de EJB's
     */
    @EJB
    private ActividadImplementadaFacade actImpFacade;    
    @EJB
    private OrganismoFacade organismoFacade;    
    @EJB
    private ProgramaFacade programaFacade;   
    @EJB
    private SubProgramaFacade subProgramaFacade;
    @EJB
    private ActividadPlanFacade actPlanFacade;       
    @EJB
    private TipoOrganismoFacade tipoOrgFacade;   
    @EJB
    private SedeFacade sedeFacade;   
    @EJB
    private ModalidadFacade modalidadFacade;
    @EJB
    private TipoCapacitacionFacade tipoCapFacade;       
    
    /**
     * Constructor
     */
    public MbReqActividadesDispuestas() {
    }

    /***********************************
     * métodos de acceso a los campos **
     * @return 
     ***********************************/
    
    public boolean isPorAd() {
        return porAd;
    }

    public void setPorAd(boolean porAd) {
        this.porAd = porAd;
    }
    
    public List<Organismo> getListOrgList() {
        return listOrgList;
    }

    public void setListOrgList(List<Organismo> listOrgList) {
        this.listOrgList = listOrgList;
    }

    public List<Organismo> getListOrgListFilter() {
        return listOrgListFilter;
    }

    public void setListOrgListFilter(List<Organismo> listOrgListFilter) {
        this.listOrgListFilter = listOrgListFilter;
    }

    public Organismo getOrgSel() {
        return orgSel;
    }

    public void setOrgSel(Organismo orgSel) {
        this.orgSel = orgSel;
    }
    
    public Participante getPartSel() {
        return partSel;
    }

    public void setPartSel(Participante partSel) {
        this.partSel = partSel;
    }

    public boolean isConsultado() {
        return consultado;
    }

    public void setConsultado(boolean consultado) {
        this.consultado = consultado;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public SubPrograma getSubprograma() {
        return subprograma;
    }

    public void setSubprograma(SubPrograma subprograma) {
        this.subprograma = subprograma;
    }

    public ActividadPlan getActForm() {
        return actForm;
    }

    public void setActForm(ActividadPlan actForm) {
        this.actForm = actForm;
    }

    public Organismo getOrganismoSol() {
        return organismoSol;
    }

    public void setOrganismoSol(Organismo organismoSol) {
        this.organismoSol = organismoSol;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public TipoCapacitacion getTipoCap() {
        return tipoCap;
    }

    public void setTipoCap(TipoCapacitacion tipoCap) {
        this.tipoCap = tipoCap;
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
    
    public ActividadImplementada getActividadImplementada(java.lang.Long id) {
        return getFacade().find(id);
    }      

    public TipoOrganismo getTipoOrg() {
        return tipoOrg;
    }

    public void setTipoOrg(TipoOrganismo tipoOrg) {
        this.tipoOrg = tipoOrg;
    }

    public List<Programa> getListProgramas() {
        return listProgramas;
    }

    public void setListProgramas(List<Programa> listProgramas) {
        this.listProgramas = listProgramas;
    }

    public List<SubPrograma> getListSubProgramas() {
        return listSubProgramas;
    }

    public void setListSubProgramas(List<SubPrograma> listSubProgramas) {
        this.listSubProgramas = listSubProgramas;
    }

    public List<ActividadPlan> getListActForm() {
        return listActForm;
    }

    public void setListActForm(List<ActividadPlan> listActForm) {
        this.listActForm = listActForm;
    }

    public List<TipoOrganismo> getListTipoOrg() {
        return listTipoOrg;
    }

    public void setListTipoOrg(List<TipoOrganismo> listTipoOrg) {
        this.listTipoOrg = listTipoOrg;
    }

    public List<Organismo> getListOrgSol() {
        return listOrgSol;
    }

    public void setListOrgSol(List<Organismo> listOrgSol) {
        this.listOrgSol = listOrgSol;
    }

    public List<Sede> getListSedes() {
        return listSedes;
    }

    public void setListSedes(List<Sede> listSedes) {
        this.listSedes = listSedes;
    }

    public List<Modalidad> getListModalidades() {
        return listModalidades;
    }

    public void setListModalidades(List<Modalidad> listModalidades) {
        this.listModalidades = listModalidades;
    }

    public List<TipoCapacitacion> getListTipoCap() {
        return listTipoCap;
    }

    public void setListTipoCap(List<TipoCapacitacion> listTipoCap) {
        this.listTipoCap = listTipoCap;
    }
    
    public List<ActividadImplementada> getListActDisp() {
        return listActDisp;
    }

    public void setListActDisp(List<ActividadImplementada> listActDisp) {
        this.listActDisp = listActDisp;
    }

    public List<ActividadImplementada> getListActDispFilter() {
        return listActDispFilter;
    }

    public void setListActDispFilter(List<ActividadImplementada> listActDispFilter) {
        this.listActDispFilter = listActDispFilter;
    }

    public ActividadImplementada getCurrent() {
        return current;
    }

    public void setCurrent(ActividadImplementada current) {
        this.current = current;
    }    
    
    /**
     * Método para gestionar la Actividad Dispuesta seleccionada
     * @return
     */
    public ActividadImplementada getSelected() {
        if (current == null) {
            current = new ActividadImplementada();
        }
        return current;
    }    
    
    public int getSubProgramas() {
        return subProgramas;
    }

    public void setSubProgramas(int subProgramas) {
        this.subProgramas = subProgramas;
    }

    public int getSedes() {
        return sedes;
    }

    public void setSedes(int sedes) {
        this.sedes = sedes;
    }

    public int getClases() {
        return clases;
    }

    public void setClases(int clases) {
        this.clases = clases;
    }

    public int getInscriptos() {
        return inscriptos;
    }

    public void setInscriptos(int inscriptos) {
        this.inscriptos = inscriptos;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public int getAprobados() {
        return aprobados;
    }

    public void setAprobados(int aprobados) {
        this.aprobados = aprobados;
    }

    public int getFinalizadas() {
        return finalizadas;
    }

    public void setFinalizadas(int finalizadas) {
        this.finalizadas = finalizadas;
    }    

    public int getOrgDestinatarios() {
        return orgDestinatarios;
    }

    public void setOrgDestinatarios(int orgDestinatarios) {
        this.orgDestinatarios = orgDestinatarios;
    }
    
    public int getOrgSolicitantes() {
        return orgSolicitantes;
    }

    public void setOrgSolicitantes(int orgSolicitantes) {
        this.orgSolicitantes = orgSolicitantes;
    }

    public int getModalidades() {
        return modalidades;
    }

    public void setModalidades(int modalidades) {
        this.modalidades = modalidades;
    }

    public int getTiposCapacitacion() {
        return tiposCapacitacion;
    }

    public void setTiposCapacitacion(int tiposCapacitacion) {
        this.tiposCapacitacion = tiposCapacitacion;
    }    
    
    public List<Participante> getListAgList() {
        return listAgList;
    }

    public void setListAgList(List<Participante> listAgList) {
        this.listAgList = listAgList;
    }

    public List<Participante> getListAgListFilter() {
        return listAgListFilter;
    }

    public void setListAgListFilter(List<Participante> listAgListFilter) {
        this.listAgListFilter = listAgListFilter;
    }
    
    
    /******************************
     * Métodos de inicialización **
     ******************************/
    
    /**
     * Método para inicializar el Bean
     */
    @PostConstruct
    public void init(){
        iniciado = false;
        consultado = false;
        cargarListados();
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
                    if(!s.equals("mbReqActividadesDispuestas") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }        
    
    /**
     * @return El participante seleccionado
     */
    public Participante getPartSelected() {
        if (partSel == null) {
            partSel = new Participante();
        }
        return partSel;
    }      
    
    /**
     * @return El organismo seleccionado
     */
    public Organismo getOrgSelected() {
        if (orgSel == null) {
            orgSel = new Organismo();
        }
        return orgSel;
    }     
    
    /**
     * Método para cargar los listados de los combos
     */
    public void cargarListados(){
        listProgramas = programaFacade.findAll();
        listSubProgramas = subProgramaFacade.findAll();
        listActForm = actPlanFacade.findAll();
        listTipoOrg = tipoOrgFacade.findAll();
        listSedes = sedeFacade.findAll();
        listModalidades = modalidadFacade.findAll();
        listTipoCap = tipoCapFacade.findAll();
    }

    /**
     *
     * @param document
     * @throws DocumentException
     * @throws IOException
     */
    public void preProcessPDF(Object document) throws DocumentException, IOException {
        Document pdf = (Document) document;    
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.newPage();
    }       
    
    /***********************************
     * Métodos para actualizar combos **
     ***********************************/
    
    /**
     * Método para actualizar los Subprogramas si se selecciona un Programa
     * @param event 
     */
    public void progChangeListener(ValueChangeEvent event) {
        Programa prog = (Programa) event.getNewValue();
        if(prog != null){listSubProgramas = prog.getSubProgramas();}
    }       
    
    /**
     * Método para actualizar las Actividades Formativas si se selecciona un Subprograma
     * @param event 
     */
    public void subChangeListener(ValueChangeEvent event) {
        SubPrograma sub = (SubPrograma) event.getNewValue();
        if(sub != null){listActForm = sub.getActividadesPlan();}
    }     
    
    /**
     * Método para actualizar los Organismos según el tipo seleccionado
     * @param event
     */
    public void tipoOrgChangeListener(ValueChangeEvent event) {   
        tipoOrg = (TipoOrganismo)event.getNewValue();
        if(tipoOrg != null){listOrgSol = organismoFacade.getXTipo(tipoOrg);}
    }     
    
    
    /**************************
     * Métodos de navegación **
     **************************/
    /**
     * Método para ver el resumen de la consulta general
     */
    public void verResGral(){        
        // reseteo los totales del resumen
        resetTotales();
        
        // inicializo los totalse del resumen
        inicTotalesGrales();        
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("dlgResumenGral", options, null);
    }      
    
    /**
     * Método para ver el listado de los participantes de las AD seleccionadas en el consulta general
     */
    public void verListPartGral(){
        // reseteo el listado
        if(listAgList != null) listAgList = null;
        if(listAgListFilter != null) listAgListFilter = null;
        
        // inicializo los agentes capacitados por las AD seleccionadas
        inicAgList();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 1100);
        options.put("contentHeight", 600);
        RequestContext.getCurrentInstance().openDialog("agentes/dlgAgList", options, null);
    }
    
    /**
     * Método para ver el listado de los organismos con agentes capacitados de las AD seleccionadas en el consulta general
     */
    public void verListOrgGral(){    
        // reseteo el listado
        if(listOrgList != null) listOrgList = null;
        if(listOrgListFilter != null) listOrgList = null;
        
        // seteo el flag
        porAd = false;
        
        // inicializo los organismos capacitados por las AD seleccionadas
        inicOrgList();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        options.put("contentHeight", 600);
        RequestContext.getCurrentInstance().openDialog("organismos/dlgOrgList", options, null);
    }
    
    /**
     * Método para ver el listado de los organismos con agentes capacitados para una AD seleccionada
     */
    public void verListOrgPorAd(){    
        // reseteo el listado
        if(listOrgList != null) listOrgList = null;
        if(listOrgListFilter != null) listOrgList = null;
        
        // seteo el flag
        porAd = true;
        
        // inicializo los organismos capacitados de la AD seleccionada
        inicOrgListPorAd();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        options.put("contentHeight", 600);
        RequestContext.getCurrentInstance().openDialog("organismos/dlgOrgList", options, null);
    }    
    
    /**
     * Método para ver el listado de las clases de la AD seleccionada
     */
    public void verListClases(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 1100);
        options.put("contentHeight", 600);
        RequestContext.getCurrentInstance().openDialog("clases/dlgClaseList", options, null);
    }    
    
    /**
     * Método para ver el resumen del participante seleccionado
     */
    public void verAgSel(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("dlgAgView", options, null);
    }
    
    /**
     * Método para ver el resumen del organismo seleccionado
     */
    public void verOrgSel(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgOrgView", options, null);
    }    
    
    /**
     * Método para ver el resumen del organismo seleccionado
     */
    public void verClaseSel(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("dlgClaseView", options, null);
    }      
    
    /**
     * Método para ver el resumen de una AD
     */
    public void verResGralAD(){
        // inicializar aprobados para la AD
        //inicAprobadosAD();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("dlgResGralAd", options, null);
    }

    
    /*************************
     * Métodos de operación **
     *************************/
    
    /**
     * Método para resetear elementos
     */
    public void reset(){
        fDespuesDe = null;
        fAntesDe = null;
        tipoOrg = null;
        programa = null;
        subprograma = null;
        actForm = null;
        organismoSol = null;
        sede = null;
        modalidad = null;
        tipoCap = null;
        consultado = false;
        resetTotales();
        cargarListados();
    }  
    
    /**
     * Método para realizar la búsqueda
     * @param event
     */
    public void buscar(ActionEvent event){
        if(programa == null && subprograma == null && actForm == null && organismoSol == null && sede == null && modalidad == null && tipoCap == null && fDespuesDe == null && fAntesDe == null){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ReqActividades_consultaIncompleta"));
        }else{
            listActDisp = actImpFacade.getXConsulta(programa, subprograma, actForm, organismoSol, sede, modalidad, tipoCap, fDespuesDe, fAntesDe);    
            consultado = true;
        }
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
     * Método para inicializar el listado de Participantes de las AD seleccionadas
     */
    private void inicAgList(){
        List<Participante> listPartXAD = new ArrayList<>();
        int clasesATomar;
        int clasesCursadas;
        int adCursadas;
        int adAprobadas;
        double porcAsist;
        double dPorcAsistAD;
        
        if(listAgList == null) listAgList = new ArrayList<>();
        
        // recorro el conjunto de AD seleccionadas
        for(ActividadImplementada ad : listActDisp){
            if(!ad.getClases().isEmpty()){
                for(Clase clase : ad.getClases()){
                    // de cada clase de la AD obtengo los participantes que cursaron
                    if(!clase.getParticipantes().isEmpty()){
                        for(Participante part : clase.getParticipantes()){
                            if(!listPartXAD.contains(part)){
                                // agrego al participante si no lo agregué ya
                                listPartXAD.add(part);
                            }
                        }   
                    }
                }   
            }
            // recorro el listado temporal de participantes de la AD 
            // para setearle a cada uno las AD cursadas y las clases tomadas
            // según se encuentre o no en el listado a mostrar
            if(!listPartXAD.isEmpty()){
                for(Participante part : listPartXAD){
                    if(listAgList.contains(part)){
                        // si está ya en el listado, lo obtengo y actualizo
                        int id = listAgList.indexOf(part);
                        
                        clasesATomar = listAgList.get(id).getClasesATomar();
                        listAgList.get(id).setClasesATomar(clasesATomar + ad.getClases().size());
                                
                        clasesCursadas = listAgList.get(id).getClasesTomadas();
                        listAgList.get(id).setClasesTomadas(clasesCursadas + getFacade().getClasesXInscripto(ad, part));

                        adCursadas = listAgList.get(id).getAdCursadas();
                        listAgList.get(id).setAdCursadas(adCursadas + 1);
                        
                        if(ad.getPorcAsistencia() > 0){
                            porcAsist = (double)clasesCursadas / ad.getClases().size();
                            dPorcAsistAD = (double)ad.getPorcAsistencia() / 100;
                            if(porcAsist >= (dPorcAsistAD)){
                                adAprobadas = listAgList.get(id).getAdAprobadas();
                                listAgList.get(id).setAdAprobadas(adAprobadas + 1);
                            }
                        }else{
                            // si no tiene porcentaje de asistencia la doy por aprobada
                            adAprobadas = listAgList.get(id).getAdAprobadas();
                            listAgList.get(id).setAdAprobadas(adAprobadas + 1);
                        }

                    }else{
                        // si no está incluido lo seteo y agrego
                        part.setClasesTomadas(getFacade().getClasesXInscripto(ad, part));
                        part.setClasesATomar(ad.getClases().size());
                        part.setAdCursadas(1);

                        if(ad.getPorcAsistencia() > 0){
                            porcAsist = (double)part.getClasesTomadas() / ad.getClases().size();
                            dPorcAsistAD = (double)ad.getPorcAsistencia() / 100;
                            if(porcAsist >= (dPorcAsistAD)){
                                part.setAdAprobadas(1);
                            } 
                        }else{
                            // si no tiene porcentaje de asistencia la doy por aprobada
                            part.setAdAprobadas(1);
                        }
                        listAgList.add(part);
                    }
                }
            }
            // vacío el list temporal
            listPartXAD.clear();
        }
    }
    
    /**
     * Método para inicializar el listado de Organismos participantes de las AD seleccionadas
     */
    private void inicOrgList(){
        int agInscriptos;
        int agParticipantes;
        int agAprobados;
        List<Participante> listPartXAD = new ArrayList<>();
        List<Participante> listAprobXAD = new ArrayList<>();
        List<ActividadImplementada> listAD = new ArrayList<>();
        double porcAsist;
        double dPorcAsistAD;
        Organismo organismo;
        
        if(listOrgList == null) listOrgList = new ArrayList<>();

        // recorro el conjunto de AD seleccionadas para obtener los organismos que tienen inscriptos
        for(ActividadImplementada ad : listActDisp){
            // recorro los inscriptos para poblar la lista de organismos
            if(!ad.getParticipantes().isEmpty()){
                for(Participante part : ad.getParticipantes()){
                    // si no guardé el organismo del participante, lo agrego a la lista
                    if(!listOrgList.contains(part.getAgente().getOrganismo())){
                        organismo = part.getAgente().getOrganismo();
                        // reseteo los totales del organismo
                        organismo.setAdRecibidas(0);
                        organismo.setAgAprobados(0);
                        organismo.setAgInscriptos(0);
                        organismo.setAgParticipantes(0);
                        listOrgList.add(organismo);
                    }
                }
            }
            
        }
        
        // recorro cada AD y obtengo los participantes y los guardo en sendas listas
        for(ActividadImplementada ad : listActDisp){
            // ontengo las clases da la AD
            if(!ad.getClases().isEmpty()){
                for(Clase cls : ad.getClases()){
                    // de cada clase de la AD obtengo los participantes que cursaron
                    if(!cls.getParticipantes().isEmpty()){
                        for(Participante part : cls.getParticipantes()){
                            // si no está, agrego al participante a la lita temporal
                            if(!listPartXAD.contains(part)){
                                listPartXAD.add(part);
                            }
                        }   
                    }
                }
            }
        }
        
        // vuelvo a recorrer las AD para obtener los aprobados y guardarlos en la lista temporal
        for(ActividadImplementada ad : listActDisp){
            // recorro la lista de participantes para buscar los que aprobaron y agregarlos a la lista temporal
            if(!listPartXAD.isEmpty()){
                for(Participante part : listPartXAD){
                    // solo continúo si el participante está inscripto en la AD
                    if(ad.getParticipantes().contains(part)){
                        // verifico si aprobó la AD. En caso que la AD no tenga porcentaje de asistencia, la doy por aprobada
                        if(ad.getPorcAsistencia() > 0){
                            // obtengo las clases tomadas por el participante
                            part.setClasesTomadas(getFacade().getClasesXInscripto(ad, part));
                            // calculo el porcentaje de asistencia del participante
                            porcAsist = (double)part.getClasesTomadas() / ad.getClases().size();
                            // formateo el porcentaje de asistencia de la AD
                            dPorcAsistAD = (double)ad.getPorcAsistencia() / 100;
                            // valido los porcentajes
                            if(porcAsist >= (dPorcAsistAD)){
                                // si aprobó lo agrego a la lista
                                listAprobXAD.add(part);
                            } 
                        }else{
                            // si no tiene porcentaje de asistencia la doy por aprobada
                            listAprobXAD.add(part);
                        }
                    }
                }
            }
        }
        
        // seteo los inscriptos del organismo
        for(ActividadImplementada ad : listActDisp){
            if(!listOrgList.isEmpty()){
                for(Organismo org : listOrgList){
                    // recorro los inscriptos para asignarlos a cada organismo
                    if(!ad.getParticipantes().isEmpty()){
                        for(Participante part : ad.getParticipantes()){
                            // si el inscripto pertenece al organismo, lo agrego a la cuenta
                            if(part.getAgente().getOrganismo().equals(org)){
                                agInscriptos = org.getAgInscriptos();
                                org.setAgInscriptos(agInscriptos + 1);
                            }
                        }
                    }
                }
            }
        }

        // recorro los organismos
        if(!listOrgList.isEmpty()){
            for(Organismo org : listOrgList){
                // reseteo el listado de AD
                if(!listAD.isEmpty()){
                    listAD.clear();
                }
                // recorro el listado de participantes
                if(!listPartXAD.isEmpty()){
                    for(Participante part : listPartXAD){
                        // si el participante pertenece al organismo, acutalizo los totales
                        if(part.getAgente().getOrganismo().equals(org)){
                            agParticipantes = org.getAgParticipantes();
                            org.setAgParticipantes(agParticipantes + 1);

                            // si hay al menos un participante del organismo guardo la AD si no está ya guardada
                            if(!listAD.contains(part.getActividad())){
                                listAD.add(part.getActividad());
                            }
                        }
                    }
                    // actualizo las AD del organismo
                    org.setAdRecibidas(listAD.size());
                }
            }
        }


        // seteo los aprobados
        if(!listAprobXAD.isEmpty()){
            for(Participante part : listAprobXAD){
                // con el participante recorro los organismos
                if(!listOrgList.isEmpty()){
                    for(Organismo org : listOrgList){
                        // el participante pertenece al organismo, acutalizo los totales
                        if(part.getAgente().getOrganismo().equals(org)){
                            agAprobados = org.getAgAprobados();
                            org.setAgAprobados(agAprobados + 1);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Método para inicializar el listado de Organismos participantes de la AD seleccionada
     */    
    private void inicOrgListPorAd(){
        int agInscriptos;
        int agParticipantes;
        int agAprobados;
        List<Participante> listPartXAD = new ArrayList<>();
        List<Participante> listAprobXAD = new ArrayList<>();
        double porcAsist;
        double dPorcAsistAD;
        Organismo organismo;
        
        if(listOrgList == null) listOrgList = new ArrayList<>();

        // recorro los inscriptos para poblar la lista de organismos
        if(!current.getParticipantes().isEmpty()){
            for(Participante part : current.getParticipantes()){
                // si no guardé el organismo del participante, lo agrego a la lista
                if(!listOrgList.contains(part.getAgente().getOrganismo())){
                    organismo = part.getAgente().getOrganismo();
                    // reseteo los totales del organismo
                    organismo.setAdRecibidas(0);
                    organismo.setAgAprobados(0);
                    organismo.setAgInscriptos(0);
                    organismo.setAgParticipantes(0);
                    listOrgList.add(organismo);
                }
            }
        }
        
        // ontengo las clases da la AD
        if(!current.getClases().isEmpty()){
            for(Clase cls : current.getClases()){
                // de cada clase de la AD obtengo los participantes que cursaron
                if(!cls.getParticipantes().isEmpty()){
                    for(Participante part : cls.getParticipantes()){
                        // si no está, agrego al participante a la lita temporal
                        if(!listPartXAD.contains(part)){
                            listPartXAD.add(part);
                        }
                    }   
                }
            }
        }
        
        // recorro la lista de participantes para buscar los que aprobaron y agregarlos a la lista temporal
        if(!listPartXAD.isEmpty()){
            for(Participante part : listPartXAD){
                // solo continúo si el participante está inscripto en la AD
                if(current.getParticipantes().contains(part)){
                    // verifico si aprobó la AD. En caso que la AD no tenga porcentaje de asistencia, la doy por aprobada
                    if(current.getPorcAsistencia() > 0){
                        // obtengo las clases tomadas por el participante
                        part.setClasesTomadas(getFacade().getClasesXInscripto(current, part));
                        // calculo el porcentaje de asistencia del participante
                        porcAsist = (double)part.getClasesTomadas() / current.getClases().size();
                        // formateo el porcentaje de asistencia de la AD
                        dPorcAsistAD = (double)current.getPorcAsistencia() / 100;
                        // valido los porcentajes
                        if(porcAsist >= (dPorcAsistAD)){
                            // si aprobó lo agrego a la lista
                            listAprobXAD.add(part);
                        } 
                    }else{
                        // si no tiene porcentaje de asistencia la doy por aprobada
                        listAprobXAD.add(part);
                    }
                }
            }
        }
        
        // seteo los inscriptos del organismo
        if(!listOrgList.isEmpty()){
            for(Organismo org : listOrgList){
                // recorro los inscriptos para asignarlos a cada organismo
                if(!current.getParticipantes().isEmpty()){
                    for(Participante part : current.getParticipantes()){
                        // si el inscripto pertenece al organismo, lo agrego a la cuenta
                        if(part.getAgente().getOrganismo().equals(org)){
                            agInscriptos = org.getAgInscriptos();
                            org.setAgInscriptos(agInscriptos + 1);
                        }
                    }
                }
            }
        }

        // recorro los organismos
        if(!listOrgList.isEmpty()){
            for(Organismo org : listOrgList){
                // recorro el listado de participantes
                if(!listPartXAD.isEmpty()){
                    for(Participante part : listPartXAD){
                        // si el participante pertenece al organismo, acutalizo los totales
                        if(part.getAgente().getOrganismo().equals(org)){
                            agParticipantes = org.getAgParticipantes();
                            org.setAgParticipantes(agParticipantes + 1);
                        }
                    }
                }
            }
        }


        // seteo los aprobados
        if(!listAprobXAD.isEmpty()){
            for(Participante part : listAprobXAD){
                // con el participante recorro los organismos
                if(!listOrgList.isEmpty()){
                    for(Organismo org : listOrgList){
                        // el participante pertenece al organismo, acutalizo los totales
                        if(part.getAgente().getOrganismo().equals(org)){
                            agAprobados = org.getAgAprobados();
                            org.setAgAprobados(agAprobados + 1);
                        }
                    }
                }
            }
        }
    }    
    
    /**
     * Método para inicializar la cantidad de Agentes aprobados para la AD
     */
    private void inicAprobadosAD(){
        List<Participante> listPart = new ArrayList<>();
        List<Participante> listAprob = new ArrayList<>();
        int clasesCursadas;
        double porcAsist;
        double dPorcAsistAD;
        
        current.setAprobados(0);
        
        if(!current.getClases().isEmpty()){
            for(Clase cls : current.getClases()){
                if(!cls.getParticipantes().isEmpty()){
                    for(Participante part : cls.getParticipantes()){
                        if(!listPart.contains(part)){
                            // verifico si aprobó solo si la AD tiene porcentaje de asistencia
                            if(current.getPorcAsistencia() > 0){
                                clasesCursadas = getFacade().getClasesXInscripto(current, part);
                                // calculo el porcentaje de asistencia del participante
                                porcAsist = (double)clasesCursadas / current.getClases().size();
                                dPorcAsistAD = (double)current.getPorcAsistencia() / 100;
                                if(porcAsist >= (dPorcAsistAD)){
                                    // si no está en la lista, lo agrego
                                    if(!listAprob.contains(part)){
                                        listAprob.add(part);
                                    }
                                }
                            }
                            // agrego el participante al listado
                            listPart.add(part);
                        }
                    }   
                }
            }   
        }        
        //current.setAprobados(listAprob.size());
    }

    /**
     * Método para inicializar los totales del resumen general
     */
    private void inicTotalesGrales(){
        if(subProgramas == 0){
            inicSubProgramas();
        }
        if(sede == null){
            inicSedes();
        }
        if(organismoSol == null){
            inicOrgSolicitante();
        }
        if(modalidad == null){
            inicModalidades();
        }
        if(tipoCap == null){
            inicTipoCap();
        }
        inicClases();
        inicInscriptos();
        inicParticipantes();
        inicFinalizadas();
        inicOrgDestinatarios();
    }

    /**
     * Método para inicializar los subprogramas
     */
    private void inicSubProgramas(){
        List<SubPrograma> listSubp = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!listSubp.contains(ad.getSubprograma())){
                listSubp.add(ad.getSubprograma());
            }
        }
        subProgramas = listSubp.size();
    }
    
    /**
     * Método para inicializar las sedes
     */
    private void inicSedes(){
        List<Sede> listSd = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!listSd.contains(ad.getSede())){
                listSd.add(ad.getSede());
            }
        }
        sedes = listSd.size();
    }    
    
    /**
     * Método para inicializar las clases
     */
    private void inicClases(){
        for(ActividadImplementada ad : listActDisp){
            clases = clases + ad.getClases().size();
        }
    }        
    
    /**
     * Método para inicializar los inscriptos
     */
    private void inicInscriptos(){
        for(ActividadImplementada ad : listActDisp){
            inscriptos = inscriptos + ad.getParticipantes().size();
        }
    }        
    
    /**
     * Método para inicializar los participantes
     */
    private void inicParticipantes(){
        int clasesCursadas;
        double porcAsist;
        double dPorcAsistAD;
        List<Participante> listPart = new ArrayList<>();
        List<Participante> listAprob = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!ad.getClases().isEmpty()){
                for(Clase clase : ad.getClases()){
                    if(!clase.getParticipantes().isEmpty()){
                        for(Participante part : clase.getParticipantes()){
                            if(!listPart.contains(part)){
                                // verifico si aprobó solo si la AD tiene porcentaje de asistencia
                                if(ad.getPorcAsistencia() > 0){
                                    clasesCursadas = getFacade().getClasesXInscripto(ad, part);
                                    // calculo el porcentaje de asistencia del participante
                                    porcAsist = (double)clasesCursadas / ad.getClases().size();
                                    dPorcAsistAD = (double)ad.getPorcAsistencia() / 100;
                                    if(porcAsist >= (dPorcAsistAD)){
                                        // si no está en la lista, lo agrego
                                        if(!listAprob.contains(part)){
                                            listAprob.add(part);
                                        }
                                    }
                                }
                                // agrego el participante al listado
                                listPart.add(part);
                            }
                        }   
                    }
                }   
            }
        }
        participantes = listPart.size();
        aprobados = listAprob.size();
    }     
    
    /**
     * Método para inicializar las AD finalizadas
     */
    private void inicFinalizadas(){
        for(ActividadImplementada ad : listActDisp){
            if(ad.isFinalizada()){
                finalizadas += 1;
            }
        }
    }        
    
    /**
     * Método para inicializar los organismos destinatarios
     */
    private void inicOrgDestinatarios(){
        List<Organismo> listOrgDes = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            for(Organismo org : ad.getOrganismosDestinatarios()){
                if(!listOrgDes.contains(org)){
                    listOrgDes.add(org);
                }
            }
        }
        orgDestinatarios = listOrgDes.size();
    }       
    
    /**
     * Método para inicializar los organismos solicitantes
     */
    private void inicOrgSolicitante(){
        List<Organismo> listOs = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!listOs.contains(ad.getOrganismo())){
                listOs.add(ad.getOrganismo());
            }
        }
        orgSolicitantes = listOs.size();
    }     
    
    /**
     * Método para inicializar las modalidades
     */
    private void inicModalidades(){
        List<Modalidad> listMod = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!listMod.contains(ad.getModalidad())){
                listMod.add(ad.getModalidad());
            }
        }
        modalidades = listMod.size();
    }     
    
    /**
     * Método para inicializar los tipos de capacitación
     */
    private void inicTipoCap(){
        List<TipoCapacitacion> listTC = new ArrayList<>();
        for(ActividadImplementada ad : listActDisp){
            if(!listTC.contains(ad.getTipoCapacitacion())){
                listTC.add(ad.getTipoCapacitacion());
            }
        }
        tiposCapacitacion = listTC.size();
    }         
    
    /**
     * Método para resetear los totales del resumen general
     */
    private void resetTotales(){
        subProgramas = 0;
        sedes = 0;
        clases = 0;
        inscriptos = 0;
        participantes = 0;
        aprobados = 0;
        finalizadas = 0;
        orgDestinatarios = 0;
        orgSolicitantes = 0;
        modalidades = 0;
        tiposCapacitacion = 0; 
    }    

    public List<Clase> getListClasesFilter() {
        return listClasesFilter;
    }

    public void setListClasesFilter(List<Clase> listClasesFilter) {
        this.listClasesFilter = listClasesFilter;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }



    
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = ActividadImplementada.class)
    public static class ReqActDispControllerConverter implements Converter {

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
            MbReqActividadesDispuestas controller = (MbReqActividadesDispuestas) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbReqActividadesDispuestas");
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

