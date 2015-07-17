/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rincostante
 */
@Entity
public class Orientacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre de la Orientación
     */    
    @Column (nullable=false, length=200, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 200)
    private String nombre;    
    
    /**
     * Campo de tipo Array que contiene el conjunto de Subprogramas con esta Orientación
     */
    @OneToMany(mappedBy="orientacion")
    private List<SubPrograma> subprogramas;
    
    /**
     * Contructor
     */
    public Orientacion(){
        subprogramas = new ArrayList();
    }    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<SubPrograma> getSubprogramas() {
        return subprogramas;
    }

    public void setSubprogramas(List<SubPrograma> subprogramas) {
        this.subprogramas = subprogramas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orientacion)) {
            return false;
        }
        Orientacion other = (Orientacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Orientacion[ id=" + id + " ]";
    }
    
}
