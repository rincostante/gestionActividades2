/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Resolucion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Sede;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Rol;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadPlanFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ResolucionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SedeFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.RolFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbActividadImpl implements Serializable{
    
    private ActividadImplementada current;
    private DataModel items = null;   
    private DataModel listDMPart = null;

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
    
    private int selectedItemIndex;
    private ActividadImplementada actImpSelected;
    private Usuario usLogeado;        
    private List<ActividadPlan> listActPlan;
    private List<Organismo> listOrganismos;
    private List<Resolucion> listResoluciones;
    private List<Sede> listSedes;
    private List<Usuario> listCoordinadores;
    private List<Docente> listDocentes;
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;          
    private int tipoList; //1=habilitadas | 2=finalizadas | 3=suspendidas | 4=deshabilitadas 
    private boolean esCoordinador;
    private boolean iniciado;
    
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
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        esCoordinador = usLogeado.getRol().getNombre().equals("Coordinador");
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/   
    
    public DataModel getListDMPart() {
        return listDMPart;
    }

    public void setListDMPart(DataModel listDMPart) {
        this.listDMPart = listDMPart;
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

    public List<Docente> getListDocentes() {
        return listDocentes;
    }

    public void setListDocentes(List<Docente> listDocentes) {
        this.listDocentes = listDocentes;
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
    public ActividadImplementada getSelected() {
        if (current == null) {
            current = new ActividadImplementada();
            selectedItemIndex = -1;
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
            if(esCoordinador){
                switch(tipoList){
                    case 1: items = new ListDataModel(getFacade().getHabilitadasXCoor(usLogeado));
                        break;
                    case 2: items = new ListDataModel(getFacade().getFinalizadasXCoor(usLogeado));
                        break;
                    case 3: items = new ListDataModel(getFacade().getSuspendidasXCoor(usLogeado));
                        break;
                    default: items = new ListDataModel(getFacade().getDeshabilitadasXCoor(usLogeado));
                }
            }else{
                switch(tipoList){
                    case 1: items = new ListDataModel(getFacade().getHabilitadas());
                        break;
                    case 2: items = new ListDataModel(getFacade().getFinalizadas());
                        break;
                    case 3: items = new ListDataModel(getFacade().getSuspendidas());
                        break;
                    default: items = new ListDataModel(getFacade().getDeshabilitadas());
                }
            }
        }
        return items;
    }        
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de las Actividad Implementadas habilitadas
     * @return acción para el listado de entidades
     */
    public String prepareList() {
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
        tipoList = 2;
        recreateModel();
        return "listFin";
    }    
    
    /**
     * Método para inicializar el listado de las Actividad Implementadas suspendidas
     * @return acción para el listado de entidades
     */
    public String prepareListSusp() {
        tipoList = 3;
        recreateModel();
        return "listSusp";
    }    
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        tipoList = 4;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = actImpSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad finalizada
     */
    public String prepareViewFin() {
        current = actImpSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "viewFin";
    }    
    
    /**
     * @return acción para el detalle de la entidad suspendida
     */
    public String prepareViewSusp() {
        current = actImpSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "viewSusp";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (ActividadImplementada) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        //cargo los list para los combos
        listResoluciones = resFacade.getHabilitadas();
        listActPlan = actPlanFacade.getHabilitadas();
        listOrganismos = organismoFacade.getHabilitados();
        listSedes = sedeFacade.getHabilitados();
        listDocentes = docenteFacade.getHabilitadas();
        
        //identifico el rol para la selección del Coordinador solo si no es la interfase de coordinador
        if(!esCoordinador){
            List<Rol> roles = rolFacade.getXString("Coordinador");
            listCoordinadores = usuarioFacade.getUsuarioXRol(roles.get(0).getId());   
        }
        
        current = new ActividadImplementada();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listResoluciones = resFacade.getHabilitadas();
        listActPlan = actPlanFacade.getHabilitadas();
        listOrganismos = organismoFacade.getHabilitados();
        listSedes = sedeFacade.getHabilitados();
        listDocentes = docenteFacade.getHabilitadas();
        
        //identifico el rol para la selección del Coordinador solo si no es la interfase de coordinador
        if(!esCoordinador){
            List<Rol> roles = rolFacade.getXString("Coordinador");
            listCoordinadores = usuarioFacade.getUsuarioXRol(roles.get(0).getId());   
        }
        
        current = actImpSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "edit";
    }   
    
    /**
     * @return acción para la edición de la entidad suspendida
     */
    public String prepareEditSusp() {
        //cargo los list para los combos
        listResoluciones = resFacade.getHabilitadas();
        listActPlan = actPlanFacade.getHabilitadas();
        listOrganismos = organismoFacade.getHabilitados();
        listSedes = sedeFacade.getHabilitados();
        listDocentes = docenteFacade.getHabilitadas();
        
        //identifico el rol para la selección del Coordinador solo si no es la interfase de coordinador
        if(!esCoordinador){
            List<Rol> roles = rolFacade.getXString("Coordinador");
            listCoordinadores = usuarioFacade.getUsuarioXRol(roles.get(0).getId());   
        }
        
        current = actImpSelected;   
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
     * Método que verifica que el Actividad Implementada que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = actImpSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
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
        selectedItemIndex = getItems().getRowIndex();
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
        selectedItemIndex = getItems().getRowIndex();
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
                
                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActividadImplCreated"));
                listResoluciones.clear();
                listActPlan.clear();
                listOrganismos.clear();
                listSedes.clear();
                listDocentes.clear();
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
                listResoluciones.clear();
                listActPlan.clear();
                listOrganismos.clear();
                listSedes.clear();
                listDocentes.clear();
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
                    listResoluciones.clear();
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
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = actImpSelected;
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
        listDMPart = new ListDataModel(current.getParticipantes());
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgParticipantes", options, null);
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
        items = null;
        listDMPart = null;
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
    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<ActividadImplementada> actImpls = new ArrayList();
        Iterator itRows = items.iterator();
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            ActividadImplementada actImp = (ActividadImplementada)itRows.next();
            if(actImp.getFechaInicio().after(fDespuesDe) && actImp.getFechaInicio().before(fAntesDe)){
                actImpls.add(actImp);
            }          
        }        
        items = null;
        items = new ListDataModel(actImpls); 
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
