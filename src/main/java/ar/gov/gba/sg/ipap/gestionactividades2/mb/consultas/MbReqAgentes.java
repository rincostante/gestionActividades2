

package ar.gov.gba.sg.ipap.gestionactividades2.mb.consultas;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.OrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.TipoOrganismoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.CargoFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.EstudiosCursadosFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.NivelIpapFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.SituacionRevistaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.TituloFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

/**
 * Bean de respaldo para gestionar las consultas sobre Agentes capacitados
 * @author rincostante
 */
public class MbReqAgentes implements Serializable{
    
    /**
     * campos para los parámetros de búsqueda
     */
    private TipoOrganismo tipoOrg;
    private Organismo organismo;
    private SituacionRevista sitRevista;
    private Cargo cargo;
    private Date fInicioAct;
    private NivelIpap nivelIpap;
    private EstudiosCursados estCursados;
    private Titulo titulo;
    private boolean esReferente;
    private Date fDespuesDe;
    private Date fAntesDe;

    /**
     * listados para poblar los combos que permitirán la selección de parámetros
     */
    private List<TipoOrganismo> listTipoOrg;
    private List<Organismo> listOrg;
    private List<SituacionRevista> listSitRevista;
    private List<Cargo> listCargo;
    private List<NivelIpap> listNivelIpap;
    private List<EstudiosCursados> listEstCursados;
    private List<Titulo> listTitulos;
    
    /**
     * Campos para gestionar los Agentes consultados
     */
    private List<Agente> listAgentes;
    private List<Agente> listAgentesFilter;
    private Agente current;
    private boolean consultado;
    
    /**
     * Campos de uso interno
     */
    private boolean iniciado;    
    
    /**
     * Inyección de EJB's
     */    
    @EJB
    private AgenteFacade agenteFacade;     
    @EJB
    private TipoOrganismoFacade tipoOrgFacade;
    @EJB
    private OrganismoFacade orgFacade;
    @EJB
    private SituacionRevistaFacade sitRevFacade;
    @EJB
    private CargoFacade cargoFacade;
    @EJB
    private NivelIpapFacade nivelIpapFacade;
    @EJB
    private EstudiosCursadosFacade estCurFacade;
    @EJB
    private TituloFacade titulosFacade;
    
    public MbReqAgentes() {
        
    }

    /***********************************
     * métodos de acceso a los campos **
     * @return 
     ***********************************/
    
    public boolean isConsultado() {
        return consultado;
    }

    public void setConsultado(boolean consultado) {
        this.consultado = consultado;
    }
    
    public EstudiosCursados getEstCursados() {
        return estCursados;
    }

    public void setEstCursados(EstudiosCursados estCursados) {
        this.estCursados = estCursados;
    }

    public TipoOrganismo getTipoOrg() {
        return tipoOrg;
    }

    public void setTipoOrg(TipoOrganismo tipoOrg) {
        this.tipoOrg = tipoOrg;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public SituacionRevista getSitRevista() {
        return sitRevista;
    }

    public void setSitRevista(SituacionRevista sitRevista) {
        this.sitRevista = sitRevista;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Date getfInicioAct() {
        return fInicioAct;
    }

    public void setfInicioAct(Date fInicioAct) {
        this.fInicioAct = fInicioAct;
    }

    public NivelIpap getNivelIpap() {
        return nivelIpap;
    }

    public void setNivelIpap(NivelIpap nivelIpap) {
        this.nivelIpap = nivelIpap;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public boolean isEsReferente() {
        return esReferente;
    }

    public void setEsReferente(boolean esReferente) {
        this.esReferente = esReferente;
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

    public List<TipoOrganismo> getListTipoOrg() {
        return listTipoOrg;
    }

    public void setListTipoOrg(List<TipoOrganismo> listTipoOrg) {
        this.listTipoOrg = listTipoOrg;
    }

    public List<Organismo> getListOrg() {
        return listOrg;
    }

    public void setLsitOrg(List<Organismo> listOrg) {
        this.listOrg = listOrg;
    }

    public List<SituacionRevista> getListSitRevista() {
        return listSitRevista;
    }

    public void setListSitRevista(List<SituacionRevista> listSitRevista) {
        this.listSitRevista = listSitRevista;
    }

    public List<Cargo> getListCargo() {
        return listCargo;
    }

    public void setListCargo(List<Cargo> listCargo) {
        this.listCargo = listCargo;
    }

    public List<NivelIpap> getListNivelIpap() {
        return listNivelIpap;
    }

    public void setListNivelIpap(List<NivelIpap> listNivelIpap) {
        this.listNivelIpap = listNivelIpap;
    }

    public List<EstudiosCursados> getListEstCursados() {
        return listEstCursados;
    }

    public void setListEstCursados(List<EstudiosCursados> listEstCursados) {
        this.listEstCursados = listEstCursados;
    }

    public List<Titulo> getListTitulos() {
        return listTitulos;
    }

    public void setListTitulos(List<Titulo> listTitulos) {
        this.listTitulos = listTitulos;
    }

    public List<Agente> getListAgentes() {
        return listAgentes;
    }

    public void setListAgentes(List<Agente> listAgentes) {
        this.listAgentes = listAgentes;
    }

    public List<Agente> getListAgentesFilter() {
        return listAgentesFilter;
    }

    public void setListAgentesFilter(List<Agente> listAgentesFilter) {
        this.listAgentesFilter = listAgentesFilter;
    }

    public Agente getCurrent() {
        return current;
    }

    public void setCurrent(Agente current) {
        this.current = current;
    }
    
    public Agente getAgente(java.lang.Long id) {
        return getFacade().find(id);
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
                    if(!s.equals("mbReqAgentes") && !s.equals("mbLogin")){
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
        listTipoOrg = tipoOrgFacade.findAll();
        listSitRevista = sitRevFacade.findAll();
        listCargo = cargoFacade.findAll();
        listNivelIpap = nivelIpapFacade.findAll();
        listEstCursados = estCurFacade.findAll();
        listTitulos = titulosFacade.findAll();
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
     * Método para actualizar los Organismos según el tipo seleccionado
     * @param event
     */
    public void tipoOrgChangeListener(ValueChangeEvent event) {   
        tipoOrg = (TipoOrganismo)event.getNewValue();
        if(tipoOrg != null){listOrg = orgFacade.getXTipo(tipoOrg);}
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
        organismo = null;
        sitRevista = null;
        cargo = null;
        fInicioAct = null;
        nivelIpap = null;
        estCursados = null;
        titulo = null;
        esReferente = false;
        consultado = false;
        //resetTotales();
        cargarListados();
    }     
    
    /**
     * Método para realizar la búsqueda
     * @param event
     */
    public void buscar(ActionEvent event){
        if(organismo == null && sitRevista == null && cargo == null && fInicioAct == null && nivelIpap == null && estCursados == null && titulo == null && fDespuesDe == null && fAntesDe == null){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ReqActividades_consultaIncompleta"));
        }else{
            //listActDisp = actImpFacade.getXConsulta(programa, subprograma, actForm, organismoSol, sede, modalidad, tipoCap, fDespuesDe, fAntesDe);    
            consultado = true;
        }
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
    
    
    
    
    
    
    
    
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Agente.class)
    public static class ReqAgenteConverter implements Converter {

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
            MbReqAgentes controller = (MbReqAgentes) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbReqAgentes");
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
