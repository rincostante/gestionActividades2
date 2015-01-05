/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidad que encapsula los datos de los usuarios
 * Se vincula con:
 *      Agente,
 *      Docente,
 *      Rol,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre del usuario
     */        
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String nombre;     
    
    /**
     * Campo de texto que indica la clave de acceso del usuario
     */        
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 50)
    private String calve; 
    
    /**
     * Campo de tipo Rol que contiene el rol del usuario.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="rol_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Rol rol;
    
    /**
     * Campo de tipo Docente que contiene los datos del docente, en el caso que el usuario lo sea.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="docente_id")
    private Docente docente;
    
    /**
     * Campo de tipo Agente que contiene los datos del agente, en el caso que el usuario no sea docente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="agente_id")
    private Agente agente;
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;

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
    public Rol getRol() {
        return rol;
    }

    /**
     *
     * @param rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
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
    public String getCalve() {
        return calve;
    }

    /**
     *
     * @param calve
     */
    public void setCalve(String calve) {
        this.calve = calve;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Usuario[ id=" + id + " ]";
    }
    
}
