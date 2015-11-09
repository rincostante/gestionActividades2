
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
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

/**
 * Bean de respaldo para gestionar las consultas sobre Actividades Dispuestas
 * @author rincostante
 */
public class MbReqActividadesDispuestas implements Serializable{
    // campos para los parámetros de búsqueda
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
    
    // listados para poblar los combos que permitirán la selección de parámetros
    private List<Programa> listProgramas;
    private List<SubPrograma> listSubProgramas;
    private List<ActividadPlan> listActForm;
    private List<TipoOrganismo> listTipoOrg;
    private List<Organismo> listOrgSol;
    private List<Sede> listSedes;
    private List<Modalidad> listModalidades;
    private List<TipoCapacitacion> listTipoCap;
    
    // Campos para gestionar las Actividades Dispuestas consultadas
    private List<ActividadImplementada> listActDisp;
    private List<ActividadImplementada> listActDispFilter;
    private ActividadImplementada current;
    
    // Campos de uso interno
    private boolean iniciado;

    // Inyección de EJB's
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
     ***********************************/
    
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
    
    
    /******************************
     * Métodos de inicialización **
     ******************************/
    
    /**
     * Método para inicializar el Bean
     */
    @PostConstruct
    public void init(){
        iniciado = false;
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
    
    /***********************************
     * Métodos para actualizar combos **
     ***********************************/
    
    /**
     * Método para actualizar los Subprogramas si se selecciona un Programa
     * @param event 
     */
    public void progChangeListener(ValueChangeEvent event) {
        Programa prog = (Programa) event.getNewValue();
        listSubProgramas = prog.getSubProgramas();
    }       
    
    /**
     * Método para actualizar las Actividades Formativas si se selecciona un Subprograma
     * @param event 
     */
    public void subChangeListener(ValueChangeEvent event) {
        SubPrograma sub = (SubPrograma) event.getNewValue();
        listActForm = sub.getActividadesPlan();
    }     
    
    /**
     * Método para actualizar los Organismos según el tipo seleccionado
     * @param event
     */
    public void tipoOrgChangeListener(ValueChangeEvent event) {   
        tipoOrg = (TipoOrganismo)event.getNewValue();
        listOrgSol = organismoFacade.getXTipo(tipoOrg);
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
        cargarListados();
    }  
    
    /**
     * Método para realizar la búsqueda
     */
    public void buscar(){
        if(programa == null && subprograma == null && actForm == null && organismoSol == null && sede == null && modalidad == null && tipoCap == null && fDespuesDe == null && fAntesDe == null){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ReqActividades_consultaIncompleta"));
        }else{
            listActDisp = actImpFacade.getXConsulta(programa, subprograma, actForm, organismoSol, sede, modalidad, tipoCap, fDespuesDe, fAntesDe);    
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
