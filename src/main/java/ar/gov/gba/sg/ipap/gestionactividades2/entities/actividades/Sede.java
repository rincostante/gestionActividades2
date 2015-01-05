/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad paramétrica que indica la Sede en las que se desarrollan las distintas Actividades
 * Se vincula a:
 *      ActividadImplementada,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Sede implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre de la Sede
     */    
    @Column (nullable=false, length=255, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 255)
    private String nombre;
    
    /**
     * Campo de tipo Array que contiene el conjunto de las Actividades Implementadas que se desarrollan en la Sede
     */
    @OneToMany(mappedBy="sede")
    private List<ActividadImplementada> actividades;  
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos de administración y trazabilidad de la Sede
     */  
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private AdmEntidad admin;    
    
    /**
     * Constructor
     */
    public Sede(){
        actividades = new ArrayList();
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
        if (!(object instanceof Sede)) {
            return false;
        }
        Sede other = (Sede) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.Sede[ id=" + id + " ]";
    }
    
}
