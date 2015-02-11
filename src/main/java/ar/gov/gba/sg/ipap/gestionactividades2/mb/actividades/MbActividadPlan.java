/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.CampoTematico;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Resolucion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoCapacitacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadPlanFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.CampoTematicoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ModalidadFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ResolucionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SubProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoCapacitacionFacade;
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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Administrador
 */
public class MbActividadPlan implements Serializable{
    
    private ActividadPlan current;
    private DataModel items = null;
    private DataModel listSubProgDisp = null;
    private DataModel listSubProgVinc = null;
    private DataModel listSubProgramas = null;
    private List<SubPrograma> subVinc;
    private List<SubPrograma> subDisp;
    private boolean asignaSub; 
    
    @EJB
    private ActividadPlanFacade actividadPlanFacade;
    @EJB
    private SubProgramaFacade subProgramaFacade;
    @EJB
    private ResolucionFacade resolucionFacade; 
    @EJB
    private ModalidadFacade modalidadFacade;
    @EJB
    private TipoCapacitacionFacade tipoCapacitacionFacade;
    @EJB
    private CampoTematicoFacade campoTematicoFacade;
    @EJB
    private OrganismoFacade organismoFacade;
    
    private int selectedItemIndex;
    private ActividadPlan actPlanSelected;
    private Usuario usLogeado;     
    private List<Resolucion> listResoluciones;
    private List<Modalidad> listModalidades;
    private List<TipoCapacitacion> listTipoCapacitaciones;
    private List<CampoTematico> listCamposTematicos;
    private List<Organismo> listOrganismos;
    private MbLogin login;
    private int tipoList; //1=habilitadas | 2=suspendidas | 3=deshabilitadas     

    /** Creates a new instance of MbActividadPlan */
    public MbActividadPlan() {
    }

    /**
     *
     */
    @PostConstruct
    public void init(){
        tipoList = 1;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        
	// recorro los mb que me hayan quedado activos en la session y los voy removiendo
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);

        Iterator iMbActivos = login.getListMbActivos().iterator();
        try{
            while(iMbActivos.hasNext()){
                session.removeAttribute((String)iMbActivos.next());
            }

            // limpio la lista
            if(!login.getListMbActivos().isEmpty()){
                login.getListMbActivos().clear();
            }

            // agrego el mb a la lista de activos
            login.getListMbActivos().add("mbActividadPlan"); 
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, "Hubo un error removiendo Beans de respaldo");
        }
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/ 
 
    public DataModel getListSubProgramas() {
        return listSubProgramas;
    }

    public void setListSubProgramas(DataModel listSubProgramas) {
        this.listSubProgramas = listSubProgramas;
    }
 
 
    public boolean isAsignaSub() {
        return asignaSub;
    }

    public void setAsignaSub(boolean asignaSub) {
        this.asignaSub = asignaSub;
    }
 
 
    public DataModel getListSubProgVinc() {
        return listSubProgVinc;
    }

    public void setListSubProgVinc(DataModel listSubProgVinc) {
        this.listSubProgVinc = listSubProgVinc;
    }
 
 
    public DataModel getListSubProgDisp() {
        return listSubProgDisp;
    }

    public void setListSubProgDisp(DataModel listSubProgDisp) {
        this.listSubProgDisp = listSubProgDisp;
    }
 
    public List<Modalidad> getListModalidades() {
        return listModalidades;
    }

    public void setListModalidades(List<Modalidad> listModalidades) {
        this.listModalidades = listModalidades;
    }

    public List<TipoCapacitacion> getListTipoCapacitaciones() {
        return listTipoCapacitaciones;
    }

    public void setListTipoCapacitaciones(List<TipoCapacitacion> listTipoCapacitaciones) {
        this.listTipoCapacitaciones = listTipoCapacitaciones;
    }

    public List<CampoTematico> getListCamposTematicos() {
        return listCamposTematicos;
    }

    public void setListCamposTematicos(List<CampoTematico> listCamposTematicos) {
        this.listCamposTematicos = listCamposTematicos;
    }

    public List<Organismo> getListOrganismos() {
        return listOrganismos;
    }

    public void setListOrganismos(List<Organismo> listOrganismos) {
        this.listOrganismos = listOrganismos;
    }
 
    
    public ActividadPlan getActPlanSelected() {
        return actPlanSelected;
    }

    public void setActPlanSelected(ActividadPlan actPlanSelected) {
        this.actPlanSelected = actPlanSelected;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public List<Resolucion> getListResoluciones() {
        return listResoluciones;
    }

    public void setListResoluciones(List<Resolucion> listResoluciones) {
        this.listResoluciones = listResoluciones;
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
    public ActividadPlan getSelected() {
        if (current == null) {
            current = new ActividadPlan();
            selectedItemIndex = -1;
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
            switch(tipoList){
                case 1: items = new ListDataModel(getFacade().getHabilitadas());
                    break;
                case 2: items = new ListDataModel(getFacade().getSuspendidas());
                    break;
                default: items = new ListDataModel(getFacade().getDeshabilitadas());
            }
        }
        return items;
    }    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Actividades Planificadass habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        asignaSub = false;
        tipoList = 1;
        recreateModel();
        listSubProgVinc = null;
        listSubProgDisp = null;
        if(subVinc != null){
            subVinc.clear();
        }
        if(subDisp != null){
            subDisp.clear();
        }
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de los Actividades Planificadass suspendidas
     * @return acción para el listado de entidades
     */
    public String prepareListSusp() {
        tipoList = 2;
        recreateModel();
        asignaSub = false;
        listSubProgVinc = null;
        listSubProgDisp = null;
        if(subVinc != null){
            subVinc.clear();
        }
        if(subDisp != null){
            subDisp.clear();
        }
        return "listSusp";
    }       
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        tipoList = 3;
        recreateModel();
        asignaSub = false;
        listSubProgVinc = null;
        listSubProgDisp = null;
        if(subVinc != null){
            subVinc.clear();
        }
        if(subDisp != null){
            subDisp.clear();
        }
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        asignaSub = false;
        current = actPlanSelected;
        subVinc = current.getSubprogramas();
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewSusp() {
        asignaSub = false;
        current = actPlanSelected;
        subVinc = current.getSubprogramas();
        selectedItemIndex = getItems().getRowIndex();
        return "viewSusp";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        asignaSub = false;
        current = actPlanSelected;
        subVinc = current.getSubprogramas();
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        // cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas();
        listModalidades = modalidadFacade.findAll();
        listTipoCapacitaciones = tipoCapacitacionFacade.findAll();
        listCamposTematicos = campoTematicoFacade.getHabilitados();
        listOrganismos = organismoFacade.getHabilitados();
        
        // cargo la tabla de subProgramas
        listSubProgramas = new ListDataModel(subProgramaFacade.getHabilitadas());

        current = new ActividadPlan();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas();
        listModalidades = modalidadFacade.findAll();
        listTipoCapacitaciones = tipoCapacitacionFacade.findAll();
        listCamposTematicos = campoTematicoFacade.getHabilitados();
        listOrganismos = organismoFacade.getHabilitados();
        current = actPlanSelected;
        asignaSub = true;
        subVinc = current.getSubprogramas();
        subDisp = cargarSubProgramasDisponibles();
        selectedItemIndex = getItems().getRowIndex();
        return "edit";
    }
    
    /**
     * @return acción para la edición de la entidad vencida
     */
    public String prepareEditSusp() {
        //cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas(); 
        listModalidades = modalidadFacade.findAll();
        listTipoCapacitaciones = tipoCapacitacionFacade.findAll();
        listCamposTematicos = campoTematicoFacade.getHabilitados();
        listOrganismos = organismoFacade.getHabilitados();
        current = actPlanSelected;
        asignaSub = true;
        subVinc = current.getSubprogramas();
        subDisp = cargarSubProgramasDisponibles();  
        selectedItemIndex = getItems().getRowIndex();
        return "editSusp";
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
     * Método que verifica que la Actividad Planificada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = actPlanSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanNonDeletable"));
        }
        subVinc = current.getSubprogramas();
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = actPlanSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanHabilitado"));
            subVinc = current.getSubprogramas();
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanHabilitadaErrorOccured"));
            return null; 
        }
    }     
    
    /**
     * Método para suspender Actividades
     */
    public String preparSuspender(){
        current = actPlanSelected;
        selectedItemIndex = getItems().getRowIndex();
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setSuspendido(true);
            
            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanSuspendida"));
            subVinc = current.getSubprogramas();
            return "view";
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanSuspendidaErrorOccured"));
            return null; 
        }
    }
    
    /**
     * Método para activar Actividades suspendidas
     * @return 
     */
    public String prepareActivar(){
        current = actPlanSelected;
        selectedItemIndex = getItems().getRowIndex();
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setSuspendido(false);
            
            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanActivada"));
            subVinc = current.getSubprogramas();
            return "view";
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanActivadaErrorOccured"));
            return null; 
        }
    }
    
    /**
     * Método para validar que la carga horaria sea entre 1 y 500
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarCargaHoraria(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        Long valor = (Long) arg2;
        if((valor < 1) || (valor > 500)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanValadationCargaHoraExcedido")));
        }
    }
    

    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta una nueva Sub Programa en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        if(current.getSubprogramas().isEmpty()){
            JsfUtil.addSuccessMessage("La Actividad que está guardando debe estar vinculada al menos a un Sub Programa.");
            return null;
        }else{
            try {
                if(getFacade().noExiste(current.getNombre())){
                    // Creación de la entidad de administración y asignación
                    Date date = new Date(System.currentTimeMillis());
                    AdmEntidad admEnt = new AdmEntidad();
                    admEnt.setFechaAlta(date);
                    admEnt.setHabilitado(true);
                    admEnt.setUsAlta(usLogeado);
                    current.setAdmin(admEnt);

                    // Inserción
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanCreated"));
                    listResoluciones.clear();
                    listModalidades.clear();
                    listTipoCapacitaciones.clear();
                    listCamposTematicos.clear();
                    listOrganismos.clear();
                    subVinc = current.getSubprogramas();
                    return "view";
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanExistente"));
                    return null;
                }
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanCreatedErrorOccured"));
                return null;
            }
        }
    }

    /**
     * Método que actualiza una nueva Sub Programa en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        boolean edito;
        ActividadPlan sub;
        String retorno = "";
        try {
            sub = getFacade().getExistente(current.getNombre());
            if(sub == null){
                edito = true;  
            }else{
                edito = sub.getId().equals(current.getId());
            }
            if(edito){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);

                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanUpdated"));
                listResoluciones.clear();
                listSubProgDisp = null;
                asignaSub = false;
                subDisp.clear();
                listSubProgVinc = null;
                listModalidades.clear();
                listTipoCapacitaciones.clear();
                listCamposTematicos.clear();
                listOrganismos.clear();
                if(tipoList == 1){
                    retorno = "view";  
                }
                if(tipoList == 2){
                    retorno = "viewSusp";  
                }     
                return retorno;
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanExistente"));
                return null; 
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = actPlanSelected;
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
    public ActividadPlan getActividadPlan(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbActividadPlan");
        

        // quito el mb de la lista de beans en memoria
        login.getListMbActivos().remove("mbActividadPlan");
        return "inicio";
    }   
    
    /**
     * Método para manipular los Sub Programas de una Actividad
     */
    public void verSubProgramas(){
        listSubProgVinc = new ListDataModel(subVinc);
        RequestContext.getCurrentInstance().openDialog("dlgSubProgVinc");
    }
    
    public void verSubProgamasDisp(){
        listSubProgDisp = new ListDataModel(subDisp);
        RequestContext.getCurrentInstance().openDialog("dlgSubProgDisp");
    }

    public void asignarSubPrograma(SubPrograma sub){
        subVinc.add(sub);
        subDisp.remove(sub);
        listSubProgDisp = null;
        RequestContext.getCurrentInstance().closeDialog("dlgSubProgDisp");
    }
    
    public void quitarSubPrograma(SubPrograma sub){
        subVinc.remove(sub);
        subDisp.add(sub);
        listSubProgVinc = null;
        RequestContext.getCurrentInstance().closeDialog("dlgSubProgVinc");
    }
    
    public void limpiarSubProg(){
        listSubProgDisp = null;
        listSubProgVinc = null;      
        subVinc = current.getSubprogramas();
        subDisp = cargarSubProgramasDisponibles();
    }
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ActividadPlanFacade getFacade() {
        return actividadPlanFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadPlanDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ActividadPlanDeletedErrorOccured"));
        }
    }             
    
    /**
     * 
     */
    private List<SubPrograma> cargarSubProgramasDisponibles(){
        List<SubPrograma> subs = subProgramaFacade.getHabilitadas();
        List<SubPrograma> subsSelect = new ArrayList();
        Iterator itSubs = subs.listIterator();
        while(itSubs.hasNext()){
            SubPrograma sub = (SubPrograma)itSubs.next();
            if(!subVinc.contains(sub)){
                subsSelect.add(sub);
            }
        }
        return subsSelect;
    }
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = ActividadPlan.class)
    public static class ActividadPlanControllerConverter implements Converter {

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
            MbActividadPlan controller = (MbActividadPlan) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbActividadPlan");
            return controller.getActividadPlan(getKey(value));
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
            if (object instanceof ActividadPlan) {
                ActividadPlan o = (ActividadPlan) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ActividadPlan.class.getName());
            }
        }
    }                 
}
