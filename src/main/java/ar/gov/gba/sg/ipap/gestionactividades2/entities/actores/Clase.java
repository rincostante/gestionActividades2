/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula la información relativa a las clases que conforman una actividad de tipo curso.
 * Se vincula con:
 *      Agente,
 *      Participante
 * @author rincostante
 */
@Entity
public class Clase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo entero que indica el número de orden de la clase en el total que instrumenta la actividad
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private int numOrden;    
    
    /**
     * Campo de tipo Date que indica la fecha en que se realizará la clase
     */
    @Column (nullable=false)
    @NotNull(message = "La clase debe tener una fecha de realización")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRealizacion;
    
    /**
     * Campo no persistible que muestra la fecha formateada como un string
     */
    @Transient
    private String strFechaRealizacion;
    
    /**
     * Campo de tipo Date indica la hora de inicio de la clase
     */
    @Column (nullable=false)
    @NotNull(message = "La clase debe tener una hora de inicio")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaInicio;
    
    /**
     * Campo no persistible que muestra la hora de inicio de la clase formateada como un string
     */
    @Transient
    private String strHoraInicio;
    
    /**
     * Campo de tipo Date indica la hora de fin de la clase
     */
    @Column (nullable=false)
    @NotNull(message = "La clase debe tener una hora de finalización")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaFin;
    
    /**
     * Campo no persistible que muestra la hora de inicio de la clase formateada como un string
     */
    @Transient
    private String strHoraFin;    
    
    /**
     * Campo que indica la modalidad de la clase
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="modalidad_id", nullable=false)
    @NotNull(message = "La clase debe tener una Modalidad")
    private Modalidad modalidad;
    
    /**
     * Campo que indica el docente asociado a la clase
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="docente_id", nullable=true)
    private Docente docente;
    
    /**
     * Campo que indica la actividad que compone
     */
    @ManyToOne
    @JoinColumn(name="actividad_id")
    private ActividadImplementada actividad;      
    
    /**
     * Campo que indica las los participantes de la clas
     */    
    @ManyToMany
    @JoinTable(
            name = "participantesXClases",
            joinColumns = @JoinColumn(name = "clase_fk"),
            inverseJoinColumns = @JoinColumn(name = "participante_fk")
    )
    private List<Participante> participantes;
    
    
    /**
     * Constructor
     */
    public Clase(){
        participantes = new ArrayList();
    }

    public String getStrFechaRealizacion() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaRealizacion = formateador.format(fechaRealizacion);
        return strFechaRealizacion;
    }

    public void setStrFechaRealizacion(String strFechaRealizacion) {
        this.strFechaRealizacion = strFechaRealizacion;
    }

    public String getStrHoraInicio() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", new Locale("es_ES"));        
        strHoraInicio = timeFormat.format(horaInicio);
        return strHoraInicio;
    }

    public void setStrHoraInicio(String strHoraInicio) {
        this.strHoraInicio = strHoraInicio;
    }

    public String getStrHoraFin() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", new Locale("es_ES"));        
        strHoraFin = timeFormat.format(horaFin);
        return strHoraFin;
    }

    public void setStrHoraFin(String strHoraFin) {
        this.strHoraFin = strHoraFin;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    /**
     *
     * @return
     */
    public int getNumOrden() {
        return numOrden;
    }

    /**
     *
     * @param numOrden
     */
    public void setNumOrden(int numOrden) {
        this.numOrden = numOrden;
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
    public ActividadImplementada getActividad() {
        return actividad;
    }

    /**
     *
     * @param actividad
     */
    public void setActividad(ActividadImplementada actividad) {
        this.actividad = actividad;
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
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Clase[ id=" + id + " ]";
    }
    
}
