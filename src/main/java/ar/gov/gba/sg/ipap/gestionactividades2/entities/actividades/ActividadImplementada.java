/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
     * Campo solo utilizables para actividades que no sean cursos
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true)
    private Date fecha;  
    
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
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 120)
    private String aula;    
    
    /**
     * Campo de tipo binario que indica si la Actividad está o no publicada
     */
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean publicado = true;
    
    /**
     * Campo entero que indica el tamaño de archvos (heredado para migrar)
     */
    @Column (nullable=true)
    private int fileSize;
    
    /**
     * Campo de texto indefinido (heredado para migrar)
     */
    @Column (nullable=true, length=40)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 40)
    private String mime; 
    
    /**
     * Campo de texto que indica a quién está dirigida la Actividad
     */
    @Column (nullable=true, length=1000)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 1000)
    private String dirigidoa; 
    
    /**
     * Campo de texto que registra observaciones para la Actividad
     */
    @Column (nullable=true, length=1000)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 1000)
    private String observaciones;    
    
    /**
     * Campo de tipo Organismo que registra el Organismo destinatario de la ACtividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="organismo_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Organismo organismo;
    
    /**
     * Campo de tipo Sede que registra la Sede en la cual se llevará a cabo la Actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sede_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Sede sede;
    
    /**
     * Campo que indica el estado de la Actividad, en caso que la misma esté suspendida o similar
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estadoActividad_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private EstadoActividad estadoActividad;
    
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
    private Agente coordinador;
    
    /**
     * Campo que indica el docente de la actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="docente_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Docente docente; 
    
    /**
     * 
     */
    @OneToMany(mappedBy="actividad")
    private List<Participante> participantes;    
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;    
    
    /**
     * Constructor 
     */
    public ActividadImplementada(){
        participantes = new ArrayList();
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
    public Agente getCoordinador() {
        return coordinador;
    }

    /**
     *
     * @param coordinador
     */
    public void setCoordinador(Agente coordinador) {
        this.coordinador = coordinador;
    }

    /**
     *
     * @return
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     *
     * @param docente
     */
    public void setDocente(Docente docente) {
        this.docente = docente;
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
    public EstadoActividad getEstadoActividad() {
        return estadoActividad;
    }

    /**
     *
     * @param estadoActividad
     */
    public void setEstadoActividad(EstadoActividad estadoActividad) {
        this.estadoActividad = estadoActividad;
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
    public Date getFecha() {
        return fecha;
    }

    /**
     *
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    public int getFileSize() {
        return fileSize;
    }

    /**
     *
     * @param fileSize
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     *
     * @return
     */
    public String getMime() {
        return mime;
    }

    /**
     *
     * @param mime
     */
    public void setMime(String mime) {
        this.mime = mime;
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
