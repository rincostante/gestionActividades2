

package ar.gov.gba.sg.ipap.gestionactividades2.mb.consultas;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Programa;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
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
     * Campos para los totales del resumen general
     */
    private int programas;
    private int subprogramas;
    private int actividadesDispuestas;     
    private int agentes;
    private Map<String, Integer> mSitRevista;
    private Map<String, Integer> mCargo;
    private Map<String, Integer> mEstCur;
    private Map<String, Integer> mTitulos;
    private List<ActividadImplementada> listADTotal;
    
    /**
     * Campos para el listado de Actividades Dispuestas de los Agentes seleccionados
     */
    private List<ActividadImplementada> listAdList;
    private List<ActividadImplementada> listAdListFilter;
    private ActividadImplementada aDSel;    
    
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
    
    public List<ActividadImplementada> getListAdList() {
        return listAdList;
    }

    public void setListAdList(List<ActividadImplementada> listAdList) {
        this.listAdList = listAdList;
    }

    public List<ActividadImplementada> getListAdListFilter() {
        return listAdListFilter;
    }

    public void setListAdListFilter(List<ActividadImplementada> listAdListFilter) {
        this.listAdListFilter = listAdListFilter;
    }

    public ActividadImplementada getaDSel() {
        return aDSel;
    }

    public void setaDSel(ActividadImplementada aDSel) {
        this.aDSel = aDSel;
    }

    public Map<String, Integer> getmTitulos() {
        return mTitulos;
    }

    public void setmTitulos(Map<String, Integer> mTitulos) {
        this.mTitulos = mTitulos;
    }
    
    public Map<String, Integer> getmEstCur() {
        return mEstCur;
    }

    public void setmEstCur(Map<String, Integer> mEstCur) {
        this.mEstCur = mEstCur;
    }
    
    public List<ActividadImplementada> getListADTotal() {
        return listADTotal;
    }

    public void setListADTotal(List<ActividadImplementada> listADTotal) {
        this.listADTotal = listADTotal;
    }
    
    public int getProgramas() {
        return programas;
    }

    public void setProgramas(int programas) {
        this.programas = programas;
    }

    public int getSubprogramas() {
        return subprogramas;
    }

    public void setSubprogramas(int subprogramas) {
        this.subprogramas = subprogramas;
    }

    public int getActividadesDispuestas() {
        return actividadesDispuestas;
    }

    public void setActividadesDispuestas(int actividadesDispuestas) {
        this.actividadesDispuestas = actividadesDispuestas;
    }

    public int getAgentes() {
        return agentes;
    }

    public void setAgentes(int agentes) {
        this.agentes = agentes;
    }

    public Map<String, Integer> getmSitRevista() {
        return mSitRevista;
    }

    public void setmSitRevista(Map<String, Integer> mSitRevista) {
        this.mSitRevista = mSitRevista;
    }

    public Map<String, Integer> getmCargo() {
        return mCargo;
    }

    public void setmCargo(Map<String, Integer> mCargo) {
        this.mCargo = mCargo;
    }

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
     * Método para ver el listado de las Actividades Dispuestas recibidas por los Agentes seleccionados en la consulta general
     */
    public void verListAdGral(){    
        // reseteo el listado
        if(listAdList != null) listAdList = null;
        if(listAdListFilter != null) listAdListFilter = null;
        
        // seteo el flag
        //porAd = false;
        
        // inicializo los organismos capacitados por las AD seleccionadas
        inicActList();
        
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        options.put("contentHeight", 600);
        RequestContext.getCurrentInstance().openDialog("actividades/dlgActList", options, null);
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
        resetTotales();
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
            listAgentes = getFacade().getXConsulta(organismo, sitRevista, cargo, fInicioAct, nivelIpap, estCursados, titulo, esReferente, fDespuesDe, fAntesDe);    
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
    
    /**
     * Método para resetear los totales del resumen general
     */
    private void resetTotales(){
        programas = 0;
        subprogramas = 0;
        actividadesDispuestas = 0;
        agentes = 0;

        if(mSitRevista != null){
            mSitRevista.clear();
        }
        if(mCargo != null){
            mCargo.clear();
        }
        if(mEstCur != null){
            mEstCur.clear();
        }
    } 
    
    /**
     * Método para inicializar los totales del resumen general
     */
    private void inicTotalesGrales(){
        inicProgramas();
        inicSubprogramas();
        inicAD();
        inicAgentes();
        inicMSitRevista();
        inicMCargo();
        inicMEstCur();
        inicMTitulos();
    }    
    
    /**
     * Método para inicializar total de Programas de la consulta general
     */
    private void inicProgramas(){
        List<Programa> tempProgramas = new ArrayList<>();
        
        // recorro los Agentes obtenidos en la consulta general
        for(Agente ag : listAgentes){
            // por cada Agente recorro sus participaciones
            for(Participante part : ag.getParticipaciones()){
                // leo el Programa de su AD, si no está incluido en la lista temporal de programas, lo agrego
                if(!tempProgramas.contains(part.getActividad().getSubprograma().getPrograma())){
                    tempProgramas.add(part.getActividad().getSubprograma().getPrograma());
                }
            }
        }
        programas = tempProgramas.size();
    }
    
    /**
     * Método para inicializar total de Subprogramas de la consulta general
     */
    private void inicSubprogramas(){
        List<SubPrograma> tempSub = new ArrayList<>();
        
        // recorro los Agentes obtenidos en la consulta general
        for(Agente ag : listAgentes){
            // por cada Agente recorro sus participaciones
            for(Participante part : ag.getParticipaciones()){
                // leo el SubPrograma de su AD, si no está incluido en la lista temporal de SubProgramas, lo agrego
                if(!tempSub.contains(part.getActividad().getSubprograma())){
                    tempSub.add(part.getActividad().getSubprograma());
                }
            }
        }
        subprogramas = tempSub.size();
    }  
    
    /**
     * Método para inicializar total de Actividades Dispuestas de la consulta general
     */
    private void inicAD(){
        // inicializo la lista
        listADTotal = new ArrayList<>();
        
        // recorro los Agentes obtenidos en la consulta general
        for(Agente ag : listAgentes){
            // por cada Agente recorro sus participaciones
            for(Participante part : ag.getParticipaciones()){
                // leo el SubPrograma de su AD, si no está incluido en la lista temporal de SubProgramas, lo agrego
                if(!listADTotal.contains(part.getActividad())){
                    listADTotal.add(part.getActividad());
                }
            }
        }
        actividadesDispuestas = listADTotal.size();
    }    
    
    /**
     * Método para inicializar total de Agentes de la consulta general
     */
    private void inicAgentes(){
        agentes = listAgentes.size();
    }   
    
    /**
     * Método para inicializar total de Agentes según su situación de revista, de la consulta general
     */
    private void inicMSitRevista(){
        mSitRevista = new HashMap<>();
        int i = 0;
        
        // recorro las distintas situaciones de revista
        for(SituacionRevista st : listSitRevista){
            // por cada situación de revista recorro el listado de Agentes para ir cargando el map
            i = 0;
            for(Agente ag : listAgentes){
                if(ag.getSituacionRevista().equals(st)){
                    i += 1;
                }
            }
            // si se encontró algún Agente con esta situación, la guardo en el map con sus totales
            if(i > 0) mSitRevista.put(st.getNombre(), i);
        }
    }     
    
    /**
     * Método para inicializar total de Agentes según su cargo, de la consulta general
     */
    private void inicMCargo(){
        mCargo = new HashMap<>();
        int i = 0;
        
        // recorro los distintos cargos
        for(Cargo crg : listCargo){
            // por cada cargo recorro el listado de Agentes para ir cargando el map
            i = 0;
            for(Agente ag : listAgentes){
                if(ag.getCargo().equals(crg)){
                    i += 1;
                }
            }
            // si se encontró algún Agente con este Cargo, lo guardo en el map con sus totales
            if(i > 0) mCargo.put(crg.getNombre(), i);
        }
    }       
    
    /**
     * Método para inicializar total de Agentes según el estado alcanzado de sus estudios secundarios, de la consulta general
     */
    private void inicMEstCur(){
        mEstCur = new HashMap<>();
        int i = 0;
        
        // recorro los distintos estudios cursados
        for(EstudiosCursados ec : listEstCursados){
            // por cada estudio cursado recorro el listado de Agentes para ir cargando el map
            i = 0;
            for(Agente ag : listAgentes){
                if(ag.getEstudiosCursados().equals(ec)){
                    i += 1;
                }
            }
            // si se encontró algún Agente con este Estudio cursado, lo guardo en el map con sus totales
            if(i > 0) mEstCur.put(ec.getNombre() + " " + ec.getEstado(), i);
        }        
    }        
    
    /**
     * Método para inicializar total de Agentes según su Título oficial, de la consulta general
     */
    private void inicMTitulos(){
        mTitulos = new HashMap<>();
        int i = 0;
        
        // recorro los distintos Titulos registrados
        for(Titulo tit : listTitulos){
            // por cada Título recorro el listado de Agentes para ir cargando el map
            i = 0;
            for(Agente ag : listAgentes){
                if(ag.getTitulo() != null){
                    if(ag.getTitulo().equals(tit)){
                        i += 1;
                    }
                }
            }
            // si se encontró algún Agente con este Título, lo guardo en el map con sus totales
            if(i > 0) mTitulos.put(tit.getNombre(), i);
        }        
    }       
    
    /**
     * Método para inicializar el listado de Actividades Dispuestas recibidas por los Agentes seleccionados
     */   
    private void inicActList(){
        
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
