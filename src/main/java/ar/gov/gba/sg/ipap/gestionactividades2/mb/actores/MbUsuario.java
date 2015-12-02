/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Rol;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.AgenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.DocenteFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.RolFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actores.UsuarioFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Correo;
import ar.gov.gba.sg.ipap.gestionactividades2.util.CorreoEnvios;
import ar.gov.gba.sg.ipap.gestionactividades2.util.CriptPass;
import ar.gov.gba.sg.ipap.gestionactividades2.util.JsfUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbUsuario implements Serializable{
    
    private Usuario current;
    private DataModel items = null;
    private List<Usuario> listFilter;
    private Usuario usSelected;   
    private String selectParam; 
    private boolean habilitadas;
    private List<Usuario> listaUsuarios;
    private List<Agente> listaAgentes;
    private List<Docente> listaDocentes;
    private List<Rol> listaRoles;
    private Usuario usLogeado;
    private MbLogin login;   
    private boolean iniciado;
    private String clave;
    
    private String claveNueva_1;
    private String claveNueva_2;    
    
    @EJB
    private UsuarioFacade usuarioFacade;   
    @EJB
    private AgenteFacade agenteFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private RolFacade rolFacade;
    
    boolean esAgente;
    boolean esDocente;    

    /**
     * Creates a new instance of MbUsuario
     */
    public MbUsuario() {
    }
    
    /**
     *
     */
    @PostConstruct
    public void init(){
        iniciado = false;
        listaRoles = rolFacade.findAll();
        habilitadas = true;
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();    
    }

    /********************************
     ** Getters y Setters *********** 
     ********************************/
    
    public String getClaveNueva_1() {
        return claveNueva_1;
    }

    public void setClaveNueva_1(String claveNueva_1) {
        this.claveNueva_1 = claveNueva_1;
    }

    public String getClaveNueva_2() {
        return claveNueva_2;
    }

    public void setClaveNueva_2(String claveNueva_2) {
        this.claveNueva_2 = claveNueva_2;
    }
    
    public List<Usuario> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Usuario> listFilter) {
        this.listFilter = listFilter;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    
    public boolean isEsAgente() {
        return esAgente;
    }

    public void setEsAgente(boolean esAgente) {
        this.esAgente = esAgente;
    }

    public boolean isEsDocente() {
        return esDocente;
    }

    public void setEsDocente(boolean esDocente) {
        this.esDocente = esDocente;
    }

    
    public List<Rol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Agente> getListaAgentes() {
        return listaAgentes;
    }

    public void setListaAgentes(List<Agente> listaAgentes) {
        this.listaAgentes = listaAgentes;
    }

    public List<Docente> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<Docente> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    
    public Usuario getUsSelected() {
        return usSelected;
    }

    public void setUsSelected(Usuario usSelected) {
        this.usSelected = usSelected;
    }

    public boolean isHabilitadas() {
        return habilitadas;
    }

    public void setHabilitadas(boolean habilitadas) {
        this.habilitadas = habilitadas;
    }

    
    /********************************
     ** Métodos para el datamodel ***
     ********************************/
    /**
     * @return La entidad gestionada
     */
    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
        }
        return current;
    }    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems() {
        if (items == null) {
            if(habilitadas){
                items = new ListDataModel(getFacade().getHabilitados());
            }else{
                items = new ListDataModel(getFacade().getDeshabilitados());
            }
        }
        return items;
    }   
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    /**
     * Método para inicializar el listado de los Usuarios habilitados
     * @return acción para el listado de entidades
     */
    public String prepareList() {
        iniciado = true;
        habilitadas = true;
        recreateModel();
        return "list";
    } 
    
    /**
     * 
     * @return 
     */
    public String prepareListDes() {
        habilitadas = false;
        recreateModel();
        return "listDes";
    }     
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        current = usSelected;
        return "view";
    }
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareViewDes() {
        current = usSelected;
        return "viewDes";
    }

    /** (Probablemente haya que embeberlo con el listado para una misma vista)
     * @return acción para el formulario de nuevo
     */
    public String prepareCreate() {
        esAgente = true;
        esDocente = true;
        current = new Usuario();
        // cargo los list pesados para los combos
        listaAgentes = agenteFacade.getHabilitados();
        listaDocentes = docenteFacade.getHabilitadas();
        return "new";
    }

    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        current = usSelected;
        if(current.getAgente() != null){
            esAgente = true;
            esDocente = false;
        }else{
            esAgente = false;
            esDocente = true;
        }
        // cargo los list pesados para los combos
        listaAgentes = agenteFacade.getHabilitados();
        listaDocentes = docenteFacade.getHabilitadas();
        return "edit";
    }
    
    /**
     * 
     */
    public String prepareEditPass(){
        current = usSelected;
        return "editPass";
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
     * 
     * @return 
     */
    public String prepareHabilitar(){
        current = usSelected;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioHabilitada"));
            return "view";
        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioHabilitadaErrorOccured"));
            return null; 
        }
    }    
    
    /*************************
    ** Métodos de operación **
    **************************/
    /**
     * Méto que inserta uno nuevo Usuario en la base de datos, previamente genera una entidad de administración
     * con los datos necesarios y luego se la asigna a la persona
     * @return mensaje que notifica la inserción
     */
    public String create() {
        Long idDocente;
        Long idAgente;
        if(current.getAgente() != null){
            idAgente = current.getAgente().getId();
            idDocente = Long.valueOf(0);
        }else{
            idDocente = current.getDocente().getId();
            idAgente = Long.valueOf(0);
        }
        String claveEncriptada = "";
        try {
            if(getFacade().noExiste(idDocente, idAgente)){
                // Creación de la entidad de administración y asignación
                Date date = new Date(System.currentTimeMillis());
                AdmEntidad admEnt = new AdmEntidad();
                admEnt.setFechaAlta(date);
                admEnt.setHabilitado(true);
                admEnt.setUsAlta(usLogeado);
                current.setAdmin(admEnt);
                
                // Generación de clave
                clave = CriptPass.generar();
                
                // la enccripto
                claveEncriptada = CriptPass.encriptar(clave);
                
                // verifico que no esté siendo usada por otro usuario
                while(!usuarioFacade.verificarContrasenia(claveEncriptada)){
                    clave = CriptPass.generar();
                    claveEncriptada = CriptPass.encriptar(clave);
                }
                
                // la asigno
                current.setCalve(claveEncriptada);
                
                // Inserción
                getFacade().create(current);
                
                // envío las credenciales de acceso al correo del usuario
                /*
                if(!enviarCorreo()){
                    JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario. Consulte el log del servidor.");
                    return null;
                }
                
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
                return "view";
                */
                
                /**
                 * temporalmente volvemos a mostrar la clave generada hasta resolver el problema del envío de correos
                 * rincostante 20150702
                 */
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated") + " La clave asignada es: " + clave);
                return "view";                
            }else{
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioExistente"));
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreatedErrorOccured"));
            return null;
        }
    }

    /**
     * Método que actualiza un nuevo Docente en la base de datos.
     * Previamente actualiza los datos de administración
     * @return mensaje que notifica la actualización
     */
    public String update() {
        Usuario us;
        Long idDocente;
        Long idAgente;
        
        if(current.getDocente() != null){
            idDocente = current.getDocente().getId();
            idAgente = Long.valueOf(0);
        }else{
            idAgente = current.getAgente().getId();   
            idDocente = Long.valueOf(0);
        }
           
        try {
            us = getFacade().getExistente(idDocente, idAgente);
            if(us == null){
                // Actualización de datos de administración de la entidad
                Date date = new Date(System.currentTimeMillis());
                current.getAdmin().setFechaModif(date);
                current.getAdmin().setUsModif(usLogeado);
                
                // Actualizo
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
                return "view";
            }else{
                if(us.getId().equals(current.getId())){
                    // Actualización de datos de administración de la entidad
                    Date date = new Date(System.currentTimeMillis());
                    current.getAdmin().setFechaModif(date);
                    current.getAdmin().setUsModif(usLogeado);

                    // Actualizo
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
                    return "view";                    
                }else{
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioExistente"));
                    return null;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdatedErrorOccured"));
            return null;
        }
    }
    
    /**
     * Método para actualizar la contraseña de un usuario
     * @return 
     */
    public String updatePass(){
        String claveEncriptada = CriptPass.encriptar(claveNueva_2);
        try {
            current.setCalve(claveEncriptada);
            
            // Actualización de datos de administración de la entidad
            Date date = new Date(System.currentTimeMillis());
            current.getAdmin().setFechaModif(date);
            current.getAdmin().setUsModif(usLogeado);

            usuarioFacade.edit(current);
            
            /**
             * temporalmente volvemos a mostrar la clave generada hasta resolver el problema del envío de correos
             * rincostante 20150702
             */
            /*
            if(!enviarCorreo()){
                JsfUtil.addErrorMessage("Hubo un error enviando el correo al usuario. Consulte el log del servidor.");
                return null;
            }else{
                JsfUtil.addSuccessMessage("Contraseña actualizada con exito");
                return "view";   
            }*/
            
            JsfUtil.addSuccessMessage("Se actualizó la contraseña del usuario, la nueva clave asignada es: " + claveNueva_2);
            return "view";  

        }catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Hubo un error actualizando la contraseña");
            return null;
        }
    }

    /**
     * @return mensaje que notifica el borrado
     */    
    public String destroy() {
        current = usSelected;
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
     * Método para validar que la clave a modificar estrita por primera vez, sea la que corresponda
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveVieja(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        String claveEncriptada = CriptPass.encriptar((String)arg2); 
        if(!usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaInvalida")));
        }
    }    

    /**
     * Método para validar que la clave nueva no esté siento utilizada ni sea igual a la anterior
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveNueva(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        int i = 0;
        int num = 0;        
        String pass = (String)arg2;
        String claveEncriptada = CriptPass.encriptar(pass); 
        if(usLogeado.getCalve().equals(claveEncriptada)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaIgual")));
        }else{
            if(!usuarioFacade.verificarContrasenia(claveEncriptada)){
                throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContraseniaNuevaExistente")));
            }else{
                if(pass.length() == 8){
                    while(i < pass.length()){
                        if(pass.charAt(i) > 47 && pass.charAt(i) < 58){
                            num ++;
                        }
                        i ++;
                    }

                    if(num < 2){
                        throw new ValidatorException(new FacesMessage("La contraseña debe incluir al menos 2 números."));
                    }else{
                        claveNueva_1 = (String)arg2;
                    }
                }else{
                    throw new ValidatorException(new FacesMessage("La contraseña debe tener 8 dígitos."));
                }
            }
        }
    }
    
    /**
     * Método para validar que la clave repetida sea igual a la primera
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarClaveNueva_2(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException{
        if(claveNueva_1 != null){
            if(!claveNueva_1.equals((String)arg2)){
                throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioContrasenia2Distinta")));
            }   
        }
    } 
    
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Usuario getUsuario(java.lang.Long id) {
        return getFacade().find(id);
    }    
    
    /**
     * Método para revocar la sesión del MB
     * @return 
     */
    public String cleanUp(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbUsuario");
   
        return "inicio";
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
                    if(!s.equals("mbUsuario") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }        
    
    
    /*********************
    ** Desencadenadores **
    **********************/    
    
    /**
     * 
     * @param event
     */
    public void agenteChangeListener(ValueChangeEvent event){
        Agente ag = (Agente)event.getNewValue();
        if(ag != null){
            esAgente = true;
            esDocente = false;
        }else{
            esAgente = false;
            esDocente = true;
        }
    }
    
    /**
     * 
     * @param event
     */
    public void docenteChangeListener(ValueChangeEvent event){
        Docente doc = (Docente)event.getNewValue();
        if(doc != null){
            esDocente = true;
            esAgente = false;
        }else{
            esDocente = false;
            esAgente = true;
        }
    }

    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private UsuarioFacade getFacade() {
        return usuarioFacade;
    }    
    
    /**
     * Restea la entidad
     */
    private void recreateModel() {
        items = null;
        if(selectParam != null){
            selectParam = null;
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsuarioDeletedErrorOccured"));
        }
    }       
    
    private boolean enviarCorreo(){
        String correo;
        String nombre;      
        String bodyMessage;
        Correo c = new Correo();
        CorreoEnvios envio = new CorreoEnvios();
        
        if(current.getAgente() != null){
            correo = current.getAgente().getPersona().getEmail_1();
            nombre = current.getAgente().getPersona().getNombres() + " " + current.getAgente().getPersona().getApellidos();
        }else{
            correo = current.getDocente().getPersona().getEmail_1();
            nombre = current.getDocente().getPersona().getNombres() + " " + current.getDocente().getPersona().getApellidos();
        }
        
        bodyMessage = "<p>Estimado/a " + nombre + "</p> "
                + "<p>Se ha creado una cuenta a su nombre en el Sistema " + ResourceBundle.getBundle("/Bundle").getString("Aplicacion") + " con las siguientes credenciales de acceso:</p> "
                + "<p><strong>usuario:</strong> " + current.getNombre() + "<br/> "
                + "<strong>contraseña:</strong> " + clave + "</p> "
                + "<p>Podrá acceder mediantes esle link " + ResourceBundle.getBundle("/Bundle").getString("Servidor") + ResourceBundle.getBundle("/Bundle").getString("RutaAplicacion") + " <br/> "
                + "Una vez iniciada la sesión, el sistema lo redireccionará para cambiar la contraseña por una de su elección.</p> "
                + "<p>Por favor, no responda este correo. No divulgue ni comparta sus credenciales de acceso.</p> "
                + "<p>Saludos cordiales</p> "
                + "<p>Instituto Provincial de la Administración Pública<br/> "
                + "Subsecretaría para la Modernización del Estado<br/> "
                + "Calle 12 y 53 - Torre Gubernamental II - Piso 11. Código Postal 1900. La Plata, Provincia de Buenos Aires, República Argentina<br/> "
                + "Teléfono: (0221) 429 5574/76<br /> "
                + "Correo electrónico: <a href=\"mailto:privadaipap@ipap.sg.gba.gov.ar\">privadaipap@ipap.sg.gba.gov.ar</a></p>";        
        
        c.setContrasenia("usgpxriehulvqxmz");
        c.setUsuarioCorreo("gestionipap@gmail.com");
        c.setAsunto(ResourceBundle.getBundle("/Bundle").getString("Aplicacion") + ": Nuevo registro de cuenta en el Sistema");
        c.setMensaje(bodyMessage);
        c.setDestino(correo);

        return envio.enviarCorreo(c);
    }
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

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
            MbUsuario controller = (MbUsuario) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbUsuario");
            return controller.getUsuario(getKey(value));
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }
    }           
    
}
