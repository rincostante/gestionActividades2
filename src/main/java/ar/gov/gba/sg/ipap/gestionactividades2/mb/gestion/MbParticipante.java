/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.gestion;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstadoParticipante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstadoParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.ParticipanteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbParticipante implements Serializable{
    
    private Participante current;
    private DataModel items = null;   
    private DataModel listDMClases = null;
    
    @EJB
    private ParticipanteFacade participanteFacade;
    @EJB
    private AgenteFacade agenteFacade;
    @EJB
    private ActividadImplementadaFacade actImpFacade;
    @EJB
    private EstadoParticipanteFacade estPartFacade;
    
    private int selectedItemIndex;
    private Participante partSelected;
    private Usuario usLogeado;     
    private EstadoParticipante estPart;
    private List<Agente> listAgentes;
    private List<ActividadImplementada> listActImp;
    private Date fAntesDe;
    private Date fDespuesDe;
    private MbLogin login;
    private int tipoList; //1=autorizados | 2=provisorios | 3=vencidos | 4=deshabilitados  
    private boolean iniciado;
    private boolean esCoordinador;

    /**
     * Creates a new instance of MbParticipante
     */
    public MbParticipante() {
    }
 
    /**
     *
     */
    @PostConstruct
    private void init(){
        tipoList = 1;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        esCoordinador = usLogeado.getRol().getNombre().equals("Coordinador");
        iniciado = false;
    }
    
    /********************************
     ** Getters y Setters ***********
     ********************************/ 
    
    public DataModel getListDMClases() {
        return listDMClases;
    }

    public void setListDMClases(DataModel listDMClases) {
        this.listDMClases = listDMClases;
    }
 
    
    public boolean isEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }
 
    
    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }
    
    public Participante getPartSelected() {
        return partSelected;
    }

    public void setPartSelected(Participante partSelected) {
        this.partSelected = partSelected;
    }

    public List<Agente> getListAgentes() {
        return listAgentes;
    }

    public void setListAgentes(List<Agente> listAgentes) {
        this.listAgentes = listAgentes;
    }

    public List<ActividadImplementada> getListActImp() {
        return listActImp;
    }

    public void setListActImp(List<ActividadImplementada> listActImp) {
        this.listActImp = listActImp;
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
    
    /**
     * Inicio
     * @return 
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
                    if(!s.equals("mbParticipante") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                        System.out.println("Se limpió " + s + " !!!");
                    }
                }
            }
        }
    }
 
    
    /********************************
     ** Métodos para el datamodel **
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Participante getSelected() {
        if (current == null) {
            current = new Participante();
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
                    case 1: items = new ListDataModel(getFacade().getAutorizadosXCoor(usLogeado));
                        break;
                    case 2: items = new ListDataModel(getFacade().getProvisioriosXCoor(usLogeado));
                        break;
                    case 3: items = new ListDataModel(getFacade().getVencidosXCoor(usLogeado));
                        break;
                    default: items = new ListDataModel(getFacade().getDeshabilitadasXCoor(usLogeado));
                }
            }else{
                switch(tipoList){
                    case 1: items = new ListDataModel(getFacade().getAutorizados());
                        break;
                    case 2: items = new ListDataModel(getFacade().getProvisiorios());
                        break;
                    case 3: items = new ListDataModel(getFacade().getVencidos());
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
     * Método para inicializar el listado de los Participantes autorizados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        tipoList = 1;
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para inicializar el listado de las Inscripciones vencidas
     * @return acción para el listado de entidades
     */
    public String prepareListVenc() {
        tipoList = 3;
        recreateModel();
        return "listVenc";
    }       
    
    /**
     * Método para inicializar el listado de las Inscripciones provisorias
     * @return acción para el listado de entidades
     */
    public String prepareListProv() {
        tipoList = 2;
        recreateModel();
        return "listProv";
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
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewVenc() {
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "viewVenc";
    }  
    
    /**
     * @return acción para el detalle de la entidad vencida
     */
    public String prepareViewProv() {
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "viewProv";
    }        
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        //cargo los list para los combos
        listAgentes = agenteFacade.getHabilitados();
        listActImp = actImpFacade.getHabilitadasXCoor(usLogeado);
        current = new Participante();
        selectedItemIndex = -1;
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        //cargo los list para los combos
        listAgentes = agenteFacade.getHabilitados();
        listActImp = actImpFacade.getHabilitadasXCoor(usLogeado);
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        return "edit";
    }
    
    /**
     * @return acción para la edición de la entidad vencida
     */
    public String prepareEditVenc() {
        //cargo los list para los combos
        listAgentes = agenteFacade.getHabilitados();
        listActImp = actImpFacade.getHabilitadasXCoor(usLogeado); 
        current = partSelected;
        // cargo los list para los combos     
        selectedItemIndex = getItems().getRowIndex();
        return "editVenc";
    }     
    
    /**
     * @return acción para la edición de la entidad vencida
     */
    public String prepareEditProv() {
        //cargo los list para los combos
        listAgentes = agenteFacade.getHabilitados();
        listActImp = actImpFacade.getHabilitadasXCoor(usLogeado);     
        current = partSelected;
        // cargo los list para los combos     
        selectedItemIndex = getItems().getRowIndex();
        return "editProv";
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
     * Método que verifica que el Participante que se quiere eliminar no esté siento utilizada por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        current = partSelected;
        boolean libre = getFacade().getUtilizado(current.getId());

        if (libre){
            // Elimina
            selectedItemIndex = getItems().getRowIndex();
            performDestroy();
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteNonDeletable"));
        }
        return "view";
    }  
    
    /**
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = partSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitadaErrorOccured"));
            return null; 
        }
    }         
    
    /**
     * 
     * @return 
     */
    public String prepareAutorizar(){
        current = partSelected;
        selectedItemIndex = getItems().getRowIndex();
        List<EstadoParticipante> estParts = estPartFacade.getXString("Inscripto");
        try{
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);
            current.setEstado(estParts.get(0));

            // Actualizo
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitado"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteHabilitadaErrorOccured"));
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
     * Método para preparar la búsqueda
     * @return la ruta a la vista que muestra los resultados de la consulta en forma de listado
     */
    public String prepareSelectProv(){
        buscarEntreFechas();
        return "listProv";
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
     * Méto que inserta un nuevo Participante en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        try {
            if(getFacade().noExiste(current.getAgente(), current.getActividad())){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // inserto el estado por defecto: Provisorio
                List<EstadoParticipante> estParts = estPartFacade.getXString("Provisorio");
                current.setEstado(estParts.get(0));
                
                // Inserción
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteCreated"));
                listAgentes.clear();
                listActImp.clear();
                return "viewProv";
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza un nuevo Participante en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {    
        Participante res;
        String retorno = "";
        try {
            res = getFacade().getExistente(current.getAgente(), current.getActividad());
            if(res == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteUpdated"));
                listAgentes.clear();
                listActImp.clear();
                if(tipoList == 1){
                    retorno = "view";  
                }
                if(tipoList == 3){
                    retorno = "viewVenc";  
                }     
                if(tipoList == 2){
                    retorno = "viewProv";  
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
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteUpdated"));
                    listAgentes.clear();
                    listActImp.clear();
                    if(tipoList == 1){
                        retorno = "view";  
                    }
                    if(tipoList == 3){
                        retorno = "viewVenc";  
                    }  
                    if(tipoList == 2){
                        retorno = "viewProv";  
                    }   
                    return retorno;                   
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteUpdatedErrorOccured"));
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = partSelected;
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
    public Participante getParticipante(java.lang.Long id) {
        return getFacade().find(id);
    }  
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbParticipante");
     
        return "inicio";
    }      
    
    public void verClases(){
        listDMClases = new ListDataModel(current.getClases());
        RequestContext.getCurrentInstance().openDialog("dlgClases");
    }      

    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private ParticipanteFacade getFacade() {
        return participanteFacade;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ParticipanteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("ParticipanteDeletedErrorOccured"));
        }
    }        
    
    
    /*****************************************************************************
     **** métodos privados para la búsqueda de habiliados por fecha de inicio ****
     *****************************************************************************/

    private void buscarEntreFechas(){
        List<Participante> parts = new ArrayList();
        Iterator itRows = items.iterator();
        
        // recorro el dadamodel
        while(itRows.hasNext()){
            Participante part = (Participante)itRows.next();
            if(part.getActividad().getFechaFin().after(fDespuesDe) && part.getActividad().getFechaFin().before(fAntesDe)){
                parts.add(part);
            }          
        }        
        items = null;
        items = new ListDataModel(parts); 
    }     
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Participante.class)
    public static class ParticipanteControllerConverter implements Converter {

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
            MbParticipante controller = (MbParticipante) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbParticipante");
            return controller.getParticipante(getKey(value));
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
            if (object instanceof Participante) {
                Participante o = (Participante) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Participante.class.getName());
            }
        }
    }             
}
