/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidad que modela las Rsoluciones que dan marco institucional al conjunto de Programas y Actividades gestionadas
 * Se vincula a:
 *      Programa,
 *      SubPrograma,
 *      ActividadPlan,
 *      ActividadImplementada,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Resolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre de la Resolucion
     */ 
    @Column (nullable=false, length=20)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String resolucion;
    
    /**
     * Campo de tipo entero que indica el año de la resolución
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private int anio;
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos de administración y trazabilidad de la Resolucion
     */  
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private AdmEntidad admin;     

    /**
     *
     * @return
     */
    public String getResolucion() {
        return resolucion;
    }

    /**
     *
     * @param resolucion
     */
    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    /**
     *
     * @return
     */
    public int getAnio() {
        return anio;
    }

    /**
     *
     * @param anio
     */
    public void setAnio(int anio) {
        this.anio = anio;
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
        if (!(object instanceof Resolucion)) {
            return false;
        }
        Resolucion other = (Resolucion) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.Resolucion[ id=" + id + " ]";
    }
    
}
