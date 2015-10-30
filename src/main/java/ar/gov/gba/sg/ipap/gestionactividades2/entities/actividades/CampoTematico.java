/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad paramétrica que indica el campo temático de las Actividades 
 * Se vincula a:
 *      ActividadImpl,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class CampoTematico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre del Campo temático
     */
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 100)
    private String nombre;
    
    /**
     * Campo de tipo fecha que indica el comienzo de vigencia del Campo temático
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private Date fechaInicioVigencia;    

    /**
     * Campo de tipo fecha que indica el fin de la vigencia del Campo temático
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private Date fechaFinVigencia;  
    
    /**
     * Campo de tipo Array que contiene las ACtividades implementadas de este Campo temático
     */
    @OneToMany(mappedBy="campoTematico")
    private List<ActividadImplementada> actividades;  
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos de administración y trazabilidad del Campo temático
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
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
     * Contructor
     */
    public CampoTematico(){
        actividades = new ArrayList();
    }
    
    /**
     * 
     * @return 
     */
    public String getStrFechaIniVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaIniVig = formateador.format(fechaInicioVigencia);
        return strFechaIniVig;
    }

    public void setStrFechaIniVig(String strFechaIniVig) {
        this.strFechaIniVig = strFechaIniVig;
    }

    public String getStrFechaFinVig() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
        strFechaFinVig = formateador.format(fechaFinVigencia);
        return strFechaFinVig;
    }

    public void setStrFechaFinVig(String strFechaFinVig) {
        this.strFechaFinVig = strFechaFinVig;
    }
    
    /**
     * 
     * @return 
     */
    @XmlTransient
    public List<ActividadImplementada> getActividades() {
        return actividades;
    }

    /**
     *
     * @param actividades
     */
    public void setActividades(List<ActividadImplementada> actividades) {
        this.actividades = actividades;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public Date getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }  

    /**
     *
     * @param fechaInicioVigencia
     */
    public void setFechaInicioVigencia(Date fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    /**
     *
     * @return
     */
    public Date getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    /**
     *
     * @param fechaFinVigencia
     */
    public void setFechaFinVigencia(Date fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
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
        if (!(object instanceof CampoTematico)) {
            return false;
        }
        CampoTematico other = (CampoTematico) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.CampoTematico[ id=" + id + " ]";
    }
    
}
