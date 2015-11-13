/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula la información relativa a los participantes de las diferentes actividades implementadas
 * Se vincula con:
 *      Clase,
 *      Agente,
 *      EstadoParticipante,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Participante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Campo que indica el agente participante
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="agente_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Agente agente;
    
    /**
     * Campo que indica la actividad de la que participa
     */
    @ManyToOne
    @JoinColumn(name="actividad_id")
    private ActividadImplementada actividad;
    
    /**
     * Campo que indica el estado del participante
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estado_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private EstadoParticipante estado;
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;     
    
    private String autorizacion;
    
    /**
     * Campo que indica las clases del participante
     */    
    @ManyToMany(mappedBy = "participantes")
    private List<Clase> clases;
    
    /**
     * Campo entero que indica la fecha de la autorización
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAutorizacion;    
    
    /**
     * Campo que muestra la fecha de inicio de vigencia de la actividad de la que participa, como string
     */
    @Transient
    String strFechaIniVig;    
    
    /**
     * Campo que muestra la fecha de fin de vigencia de la actividad de la que participa, como string
     */
    @Transient
    String strFechaFinVig;  
    
    /**
     * Campo que muestra la fecha de fin de autorización, como string
     */
    @Transient
    String strFechaAutoriz;          
    
    /**
     * Constructor
     */
    public Participante(){
        clases = new ArrayList();
    }

    public AdmEntidad getAdmin() {
        return admin;
    }

    public void setAdmin(AdmEntidad admin) {
        this.admin = admin;
    }

    public String getStrFechaAutoriz() {
        if(fechaAutorizacion != null){
            SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
            strFechaAutoriz = formateador.format(fechaAutorizacion);
            return strFechaAutoriz;
        }else{
            return "";
        }

    }

    public void setStrFechaAutoriz(String strFechaAutoriz) {
        this.strFechaAutoriz = strFechaAutoriz;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getStrFechaIniVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaIniVig = formateador.format(actividad.getFechaInicio());
        return strFechaIniVig;
    }

    public void setStrFechaIniVig(String strFechaIniVig) {
        this.strFechaIniVig = strFechaIniVig;
    }

    public String getStrFechaFinVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaFinVig = formateador.format(actividad.getFechaFin());
        return strFechaFinVig;
    }

    public void setStrFechaFinVig(String strFechaFinVig) {
        this.strFechaFinVig = strFechaFinVig;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
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
    public EstadoParticipante getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(EstadoParticipante estado) {
        this.estado = estado;
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
        if (!(object instanceof Participante)) {
            return false;
        }
        Participante other = (Participante) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Participante[ id=" + id + " ]";
    }
    
}
