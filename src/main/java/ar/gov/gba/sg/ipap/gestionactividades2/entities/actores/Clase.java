/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
    @Column (nullable=false, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private int numOrden;    
    
    /**
     * Campo que indica el docente asociado a la clase
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="docente_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Docente docente;
    
    /**
     * Campo que indica los participantes de la clase
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="actividad_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private ActividadImplementada actividad;   
    
    /**
     * Campo que indica la actividad que compone
     */    
    @ManyToMany(mappedBy = "clases")
    private List<Participante> participantes;    
    
    /**
     * Constructor
     */
    public Clase(){
        participantes = new ArrayList();
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
