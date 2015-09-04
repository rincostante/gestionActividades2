/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
 * Entidad que modela las distintas Implementaciones de una misma actividad.
 * Se vincula con:
 *      ActividadPlan,
 *      Organismo,
 *      Sede,
 *      EstadoActividad,
 *      Resolucion,
 *      Agente,
 *      Docente,
 *      Participante,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class ActividadImplementada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo solo utilizables para actividades de tipo curso cursos
     */    
    @Temporal(TemporalType.DATE)
    @Column(nullable=true)
    private Date fechaInicio;  
    
    /**
     * Campo solo utilizables para actividades de tipo curso cursos
     */   
    @Temporal(TemporalType.DATE)
    @Column(nullable=true)
    private Date fechaFin;  

    /**
     * Campo de texto que registra el aula en la cual se desarrolla la actividad
     */      
    @Column (nullable=true, length=120)
    @Size(message = "El campo Aula no puede tener más de 120 caracteres", max = 120)
    private String aula;    
    
    /**
     * Campo de tipo binario que indica si la Actividad está o no publicada
     */
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean publicado = false;
    
    /**
     * Campo de texto que indica a quién está dirigida la Actividad
     */
    @Column (nullable=true, length=1000)
    @Size(message = "El campo Dirigido a, no puede tener más de 1000 caracteres", max = 1000)
    private String dirigidoa; 
    
    /**
     * Campo de texto que registra observaciones para la Actividad
     */
    @Column (nullable=true, length=1000)
    @Size(message = "{endidades.stringSizeError}", max = 1000)
    private String observaciones;    
    
    /**
     * Campo de tipo Organismo que registra el Organismo destinatario de la Actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="organismo_id", nullable=true)
    private Organismo organismo;
    
    /**
     * Campo de tipo Resolucion que contiene la resolución que da marco institucional a la Actividad.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="resolucion_id", nullable=true)
    private Resolucion resolucion;     
    
    /**
     * Campo de tipo Sede que registra la Sede en la cual se llevará a cabo la Actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sede_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Sede sede;
    
    /**
     * Campo que indica la actividad que implementa
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="actividadPlan_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private ActividadPlan actividadPlan;

    /**
     * Campo que indica el agente coordinador de la actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="coordinador_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Usuario coordinador;
    
    /**
     * Campo que indica la orientación de la actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orientacion_id", nullable=true)
    private Orientacion orientacion; 
    
    /**
     * Campo que indica el Subprograma de la actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="subprograma_id", nullable=true)
    private SubPrograma subprograma;     
    
    /**
     * Campo de tipo Array que contiene el conjunto de los Subprogramas que contienen esta Actividad
     */
    @ManyToMany
    @JoinTable(
            name = "actividadesXOrganismos",
            joinColumns = @JoinColumn(name = "actividad_fk"),
            inverseJoinColumns = @JoinColumn(name = "organismo_fk")
    )
    private List<Organismo> organismosDestinatarios;    
    
    /**
     * Campo de tipo Array que contiene el conjunto de los Subprogramas que contienen esta Actividad
     */
    @ManyToMany
    @JoinTable(
            name = "actividadesXDocentes",
            joinColumns = @JoinColumn(name = "actividad_fk"),
            inverseJoinColumns = @JoinColumn(name = "docente_fk")
    )
    private List<Docente> docentesVinculados;      
    
    /**
     * Campo que indica la colección de los participantes de la Actividad
     */
    @OneToMany(mappedBy="actividad")
    private List<Participante> participantes;  
    
    /**
     * Campo que indica la colección de las Clases de la Actividad
     */
    @OneToMany(mappedBy="actividad")
    private List<Clase> clases;       
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;    
    
    /**
     * Campo que muestra la fecha de inicio de vigencia como string
     */
    @Transient
    String strFechaIniVig;    
    
    /**
     * Campo que muestra la fecha de fin de vigencia como string
     */
    @Transient
    String strFechaFinVig;   
    
    /**
     * Campo binario que indica si la actividad está o no suspendida
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean suspendido = false;   
    
    
    /**
     * Constructor 
     */
    public ActividadImplementada(){
        participantes = new ArrayList();
        clases = new ArrayList();
    }

    public List<Docente> getDocentesVinculados() {
        return docentesVinculados;
    }

    public void setDocentesVinculados(List<Docente> docentesVinculados) {
        this.docentesVinculados = docentesVinculados;
    }

    public List<Organismo> getOrganismosDestinatarios() {
        return organismosDestinatarios;
    }

    public void setOrganismosDestinatarios(List<Organismo> organismosDestinatarios) {
        this.organismosDestinatarios = organismosDestinatarios;
    }

    public SubPrograma getSubprograma() {
        return subprograma;
    }

    public void setSubprograma(SubPrograma subprograma) {
        this.subprograma = subprograma;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    public Orientacion getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
    }
    
    public boolean isSuspendido() {
        return suspendido;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }    
    
    public String getStrFechaIniVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaIniVig = formateador.format(fechaInicio);
        return strFechaIniVig;
    }

    public void setStrFechaIniVig(String strFechaIniVig) {
        this.strFechaIniVig = strFechaIniVig;
    }

    public String getStrFechaFinVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaFinVig = formateador.format(fechaFin);
        return strFechaFinVig;
    }

    public void setStrFechaFinVig(String strFechaFinVig) {
        this.strFechaFinVig = strFechaFinVig;
    }

    /**
     * 
     * @return 
     */
    public Resolucion getResolucion() {
        return resolucion;
    }

    /**
     * 
     * @param resolucion 
     */
    public void setResolucion(Resolucion resolucion) {
        this.resolucion = resolucion;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<Participante> getParticipantes() {
        return participantes;
    }

    /**
     *
     * @param participantes
     */
    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
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
    public Usuario getCoordinador() {
        return coordinador;
    }

    /**
     *
     * @param coordinador
     */
    public void setCoordinador(Usuario coordinador) {
        this.coordinador = coordinador;
    }
    
    /**
     *
     * @return
     */
    public ActividadPlan getActividadPlan() {
        return actividadPlan;
    }

    /**
     *
     * @param actividadPlan
     */
    public void setActividadPlan(ActividadPlan actividadPlan) {
        this.actividadPlan = actividadPlan;
    }

    /**
     *
     * @return
     */
    public Sede getSede() {
        return sede;
    }

    /**
     *
     * @param sede
     */
    public void setSede(Sede sede) {
        this.sede = sede;
    }

    /**
     *
     * @return
     */
    public Organismo getOrganismo() {
        return organismo;
    }

    /**
     *
     * @param organismo
     */
    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    /**
     *
     * @return
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     *
     * @param fechaInicio
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     *
     * @return
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     *
     * @param fechaFin
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     *
     * @return
     */
    public String getAula() {
        return aula;
    }

    /**
     *
     * @param aula
     */
    public void setAula(String aula) {
        this.aula = aula;
    }

    /**
     *
     * @return
     */
    public boolean isPublicado() {
        return publicado;
    }

    /**
     *
     * @param publicado
     */
    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }

    /**
     *
     * @return
     */
    public String getDirigidoa() {
        return dirigidoa;
    }

    /**
     *
     * @param dirigidoa
     */
    public void setDirigidoa(String dirigidoa) {
        this.dirigidoa = dirigidoa;
    }

    /**
     *
     * @return
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     *
     * @param observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        if (!(object instanceof ActividadImplementada)) {
            return false;
        }
        ActividadImplementada other = (ActividadImplementada) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.ActividadImplementada[ id=" + id + " ]";
    }
    
}
