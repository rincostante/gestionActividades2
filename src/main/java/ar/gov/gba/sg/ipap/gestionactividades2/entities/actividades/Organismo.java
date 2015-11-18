/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que modela los Organimos vinculados a las distintas Actividades gestionadas
 * Se vincula a:
 *      ActividadPlan,
 *      ActividadImplementada,
 *      TipoOrganismo,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Organismo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre del Organismo
     */
    @Column (nullable=false, length=250, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 250)
    private String nombre;
    
    /**
     * Campo de tipo TipoOrganismo que indica el tipo del mismo
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tipo_id", nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private TipoOrganismo tipoOrganismo;
    
    /**
     * Campo de tipo Array que contiene el conjunto de las Actividades implementadas que tienen al Organismo como destinatario
     */
    @OneToMany(mappedBy="organismo")
    private List<ActividadImplementada> actividadesImplementadas;   
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos de administración y trazabilidad del Organismo
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private AdmEntidad admin;   

    /**
     *
     * @return
     */
    @XmlTransient
    public List<ActividadImplementada> getActividadesImplementadas() {
        return actividadesImplementadas;
    }
    
    /**
     * Campo que registra la cantidad de AD recibidas por el organismo para una consulta
     */
    @Transient
    private int adRecibidas;       
    
    /**
     * Campo que registra la cantidad de agentes del organismo inscriptos para las AD de una consulta
     */
    @Transient
    private int agInscriptos;      
    
    /**
     * Campo que registra la cantidad de agentes del organismo participantes para las AD de una consulta 
     * (que al menos estuvieron presentes en una clase)
     */
    @Transient
    private int agParticipantes;  
    
    /**
     * Campo que registra la cantidad de agentes del organismo que aprobaron para una sAD de una consulta
     */
    @Transient
    private int agAprobados;          

    /**
     *
     * @param actividadesImplementadas
     */
    public int getAdRecibidas() {
        return adRecibidas;
    }

    public void setAdRecibidas(int adRecibidas) {
        this.adRecibidas = adRecibidas;
    }

    public int getAgInscriptos() {
        return agInscriptos;
    }

    public void setAgInscriptos(int agInscriptos) {
        this.agInscriptos = agInscriptos;
    }

    public int getAgParticipantes() {
        return agParticipantes;
    }

    public void setAgParticipantes(int agParticipantes) {
        this.agParticipantes = agParticipantes;
    }

    public int getAgAprobados() {
        return agAprobados;
    }

    public void setAgAprobados(int agAprobados) {
        this.agAprobados = agAprobados;
    }

    public void setActividadesImplementadas(List<ActividadImplementada> actividadesImplementadas) {
        this.actividadesImplementadas = actividadesImplementadas;
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
    public TipoOrganismo getTipoOrganismo() {
        return tipoOrganismo;
    }

    /**
     *
     * @param tipoOrganismo
     */
    public void setTipoOrganismo(TipoOrganismo tipoOrganismo) {
        this.tipoOrganismo = tipoOrganismo;
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
        if (!(object instanceof Organismo)) {
            return false;
        }
        Organismo other = (Organismo) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.Organismo[ id=" + id + " ]";
    }
    
}
