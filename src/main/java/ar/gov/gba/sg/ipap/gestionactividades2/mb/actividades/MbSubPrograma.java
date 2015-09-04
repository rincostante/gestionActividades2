/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Orientacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Programa;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Resolucion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoEspecializacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrientacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ResolucionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.SubProgramaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoEspecializacionFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;


/**
 *
 * @author Administrador
 */
public class MbSubPrograma implements Serializable{

    private SubPrograma current;
    private DataModel items = null;   
    private List<SubPrograma> listFilter;
    
    @EJB
    private SubProgramaFacade subProgramaFacade;
    @EJB
    private ResolucionFacade resolucionFacade;    
    @EJB
    private ProgramaFacade programaFacade;
    @EJB
    private OrientacionFacade orientacionFacade;
    @EJB
    private TipoEspecializacionFacade especializacionFacade;
    
    private SubPrograma subProgSelected;
    private Usuario usLogeado;     
    private List<Resolucion> listResoluciones;
    private List<Programa> listProgramas;
    private List<Orientacion> listOrientaciones;
    private List<TipoEspecializacion> listEspecializaciones;
    private List<SubPrograma> listSubprogContinuables;
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;
    private int tipoList; //1=habilitados | 2=venidos | 3=deshabilitados 
    private ListDataModel listDMActPlan;
    private List<ActividadPlan> listActPlanFilter;
    private boolean iniciado;
    
    /** Creates a new instance of MbSubPrograma */
    public MbSubPrograma() {
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
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/ 
    
    public List<SubPrograma> getListSubprogContinuables() {
        return listSubprogContinuables;
    }

    public void setListSubprogContinuables(List<SubPrograma> listSubprogContinuables) {
        this.listSubprogContinuables = listSubprogContinuables;
    }
 
    
    public List<Orientacion> getListOrientaciones() {
        return listOrientaciones;
    }

    public void setListOrientaciones(List<Orientacion> listOrientaciones) {
        this.listOrientaciones = listOrientaciones;
    }

    public List<TipoEspecializacion> getListEspecializaciones() {
        return listEspecializaciones;
    }

    public void setListEspecializaciones(List<TipoEspecializacion> listEspecializaciones) {
        this.listEspecializaciones = listEspecializaciones;
    }
 
    
    public List<SubPrograma> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<SubPrograma> listFilter) {
        this.listFilter = listFilter;
    }

    public List<ActividadPlan> getListActPlanFilter() {
        return listActPlanFilter;
    }

    public void setListActPlanFilter(List<ActividadPlan> listActPlanFilter) {
        this.listActPlanFilter = listActPlanFilter;
    }
 
    public ListDataModel getListDMActPlan() {
        return listDMActPlan;
    }

    public void setListDMActPlan(ListDataModel listDMActImp) {
        this.listDMActPlan = listDMActImp;
    }
 
    
    public SubPrograma getSubProgSelected() {
        return subProgSelected;
    }

    public void setSubProgSelected(SubPrograma subProgSelected) {
        this.subProgSelected = subProgSelected;
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

    public List<Programa> getListProgramas() {
        return listProgramas;
    }

    public void setListProgramas(List<Programa> listProgramas) {
        this.listProgramas = listProgramas;
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
    public SubPrograma getSelected() {
        if (current == null) {
            current = new SubPrograma();
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
                case 2: items = new ListDataModel(getFacade().getVencidos());
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
     * Método para inicializar el listado de los Sub Programas habilitados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        tipoList = 1;
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de los Sub Programas vencidos
     * @return acción para el listado de entidades
     */
    public String prepareListVenc() {
        tipoList = 2;
        recreateModel();
        return "listVenc";
    }       
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        tipoList = 3;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = subProgSelected;
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewVenc() {
        current = subProgSelected;
        return "viewVenc";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = (SubPrograma) getItems().getRowData();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        //cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas();
        listProgramas = programaFacade.getHabilitadas();
        listOrientaciones = orientacionFacade.findAll();
        listEspecializaciones = especializacionFacade.findAll();
        listSubprogContinuables = getFacade().getCompletables();
        current = new SubPrograma();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas();
        listProgramas = programaFacade.getHabilitadas();
        listOrientaciones = orientacionFacade.findAll();
        listEspecializaciones = especializacionFacade.findAll();    
        listSubprogContinuables = getFacade().getCompletables();
        current = subProgSelected;
        return "edit";
    }
    
    /**
     * @return acción para la edición de la entidad vencida
     */
    public String prepareEditVenc() {
        //cargo los list para los combos
        listResoluciones = resolucionFacade.getHabilitadas(); 
        listProgramas = programaFacade.getHabilitadas();
        listOrientaciones = orientacionFacade.findAll();
        listEspecializaciones = especializacionFacade.findAll();
        listSubprogContinuables = getFacade().getCompletables();
        current = subProgSelected;
        // cargo los list para los combos     
        return "editVenc";
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
     * Método que verifica que el Sub Programa que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = subProgSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = subProgSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SubProgramaHabilitadaErrorOccured"));
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
    public String prepareSelectVenc(){
        buscarEntreFechas();
        return "listVenc";
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
     * Méto que inserta una nueva Sub Programa en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
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
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaCreated"));
                listResoluciones.clear();
                listProgramas.clear();
                listOrientaciones.clear();
                listEspecializaciones.clear();
                listSubprogContinuables.clear();
                return "view";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SubProgramaCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza una nueva Sub Programa en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        SubPrograma sub;
        String retorno = "";
        try {
            sub = getFacade().getExistente(current.getNombre());
            if(sub == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaUpdated"));
                if(tipoList == 1){
                    retorno = "view";  
                }
                if(tipoList == 2){
                    retorno = "viewVenc";  
                }     
                return retorno;
            }else{
                if(sub.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaUpdated"));
                    listResoluciones.clear();
                    listProgramas.clear();
                    listOrientaciones.clear();
                    listEspecializaciones.clear();
                    listSubprogContinuables.clear();
                    if(tipoList == 1){
                        retorno = "view";  
                    }
                    if(tipoList == 2){
                        retorno = "viewVenc";  
                    }     
                    return retorno;                   
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SubProgramaUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = subProgSelected;
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
    public SubPrograma getSubPrograma(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbSubPrograma");

        return "inicio";
    }     
    
    /**
     * Método para mostrar las Actividades de este Subprograma
     */
    public void verActividades(){
        listDMActPlan = new ListDataModel(current.getActividadesPlan());
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 950);
        RequestContext.getCurrentInstance().openDialog("dlgActividades", options, null);
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
                    if(!s.equals("mbSubPrograma") && !s.equals("mbLogin")){
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
    private SubProgramaFacade getFacade() {
        return subProgramaFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        listDMActPlan = null;
        if(listActPlanFilter != null){
            listActPlanFilter = null;
        }
        if(listFilter != null){
            listFilter = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubProgramaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("SubProgramaDeletedErrorOccured"));
        }
    }        
    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<SubPrograma> campos = new ArrayList();
        Iterator itRows = items.iterator();
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            SubPrograma sub = (SubPrograma)itRows.next();
            if(sub.getFechaFinVigencia().after(fDespuesDe) && sub.getFechaFinVigencia().before(fAntesDe)){
                campos.add(sub);
            }          
        }        
        items = null;
        items = new ListDataModel(campos); 
    }         
    
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = SubPrograma.class)
    public static class SubProgramaControllerConverter implements Converter {

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
            MbSubPrograma controller = (MbSubPrograma) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbSubPrograma");
            return controller.getSubPrograma(getKey(value));
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
            if (object instanceof SubPrograma) {
                SubPrograma o = (SubPrograma) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SubPrograma.class.getName());
            }
        }
    }             
}
