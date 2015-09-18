/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.util.Edad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula la información de los docentes vinculados a las actividades formativas del IPAP
 * Se vincula con:
 *      Persona,
 *      Agente,
 *      Usuario,
 *      Titulo,
 *      Clase,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Docente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo que muestra los Apellidos y nombres personales del docente
     */
    @Transient
    String apYNom;
    
    /**
     * Campo que muestra el número de documento personal del docente
     */
    @Transient
    String documento;
    
    /**
     * Campo de texto que indica el teléfono labural del docente
     */        
    @Column (nullable=true, length=20)
    @Size(message = "{endidades.stringSizeErrorMax}", max = 20)
    private String telefonoLaboral; 
    
    /**
     * Campo de texto que indica el correo electrónico labural del docente
     */        
    @Column (nullable=true, length=50)
    @Size(message = "{endidades.stringSizeErrorMax}", max = 50)
    private String emailLaboral; 
    
    /**
     * Campo de texto que indica la especialidad del docente
     */        
    @Column (nullable=true, length=100)
    @Size(message = "{endidades.stringSizeErrorMax}", max = 100)
    private String especialidad; 
    
    /**
     * Campo entero que indica la antigüedad en meses del docente, en el caso en que no llegue a un año
     */
    @Transient
    private int antigMeses;
    
    /**
     * Campo entero que indica la antigüedad en años del docente
     */
    @Transient
    private int antigAnios;
    
    /**
     * Campo entero que indica la antigüedad en años del docente
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private Date fechaInicioDocencia;  
    
    /**
     * Campo de tipo Persona que contiene los datos básicos de la persona del docente.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private Persona persona;          
    
    /**
     * Campo de tipo Agente que contiene los datos básicos de agente del docente que lo es.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="agente_id", nullable=true)
    private Agente agente;  
    
    /**
     * Campo de tipo Titulo que contiene el título de educación formal alcanzado por el docente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="titulo_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Titulo titulo; 
    
    /**
     * Campo de tipo array que contiene las Actividades planificadas que se vinculen con el subprograma
     */    
    @ManyToMany(mappedBy = "docentesVinculados")
    private List<ActividadImplementada> actividadesImp;       
    
    
    /**
     * Campo que guarda las clases que han tenido al docente como profesor
     */
    @OneToMany(mappedBy="docente")
    private List<Clase> clases;       
    
    /**
     * Campo que guarda el usuario que lo toene al docente vinculado
     */
    @OneToOne(mappedBy="docente")
    private Usuario usuario;        
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin; 
    
    /**
     * Constructor
     */
    public Docente(){
        actividadesImp = new ArrayList();
    }

    public String getApYNom() {
        if(persona != null){
            apYNom = persona.getApellidos() + ", " + persona.getNombres();
        }else{
            apYNom = agente.getPersona().getApellidos() + ", " + agente.getPersona().getNombres();
        }
        return apYNom;
    }

    public void setApYNom(String apYNom) {
        this.apYNom = apYNom;
    }

    public String getDocumento() {
        if(persona != null){
            documento = String.valueOf(persona.getDocumento());
        }else{
            documento = String.valueOf(agente.getPersona().getDocumento());
        }        
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<Clase> getClases() {
        return clases;
    }

    /**
     *
     * @param clases
     */
    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<ActividadImplementada> getActividades() {
        return actividadesImp;
    }

    /**
     *
     * @param actividadesImp
     */
    public void setActividades(List<ActividadImplementada> actividadesImp) {
        this.actividadesImp = actividadesImp;
    }

    /**
     *
     * @return
     */
    public String getTelefonoLaboral() {
        return telefonoLaboral;
    }

    /**
     *
     * @param telefonoLaboral
     */
    public void setTelefonoLaboral(String telefonoLaboral) {
        this.telefonoLaboral = telefonoLaboral;
    }

    /**
     *
     * @return
     */
    public String getEmailLaboral() {
        return emailLaboral;
    }

    /**
     *
     * @param emailLaboral
     */
    public void setEmailLaboral(String emailLaboral) {
        this.emailLaboral = emailLaboral;
    }

    /**
     *
     * @return
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     *
     * @param especialidad
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     *
     * @return
     */
    public int getAntigMeses() {
        Edad edadUtil = new Edad();
        antigAnios = edadUtil.calcularEdad(fechaInicioDocencia).getYear();
        if(antigAnios < 1){
            antigMeses = edadUtil.calcularEdad(fechaInicioDocencia).getMonth();
        }else{
            antigMeses = 0;
        }
        return antigMeses;
    }

    /**
     *
     * @param antigMeses
     */
    public void setAntigMeses(int antigMeses) {
        this.antigMeses = antigMeses;
    }

    /**
     *
     * @return
     */
    public int getAntigAnios() {
        Edad edadUtil = new Edad();
        antigAnios = edadUtil.calcularEdad(fechaInicioDocencia).getYear();
        if(antigAnios < 0){
            antigAnios = 0;
        }     
        return antigAnios;
    }

    /**
     *
     * @param antigAnios
     */
    public void setAntigAnios(int antigAnios) {
        this.antigAnios = antigAnios;
    }

    /**
     *
     * @return
     */
    public Date getFechaInicioDocencia() {
        return fechaInicioDocencia;
    }

    /**
     *
     * @param fechaInicioDocencia
     */
    public void setFechaInicioDocencia(Date fechaInicioDocencia) {
        this.fechaInicioDocencia = fechaInicioDocencia;
    }

    /**
     *
     * @return
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     *
     * @param persona
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     *
     * @return
     */
    public Agente getAgente() {
        return agente;
    }

    /**
     *
     * @param agente
     */
    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    /**
     *
     * @return
     */
    public Titulo getTitulo() {
        return titulo;
    }

    /**
     *
     * @param titulo
     */
    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * @return
     */
    public AdmEntidad getAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     */
    public void setAdmin(AdmEntidad admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Docente[ id=" + id + " ]";
    }
    
}
