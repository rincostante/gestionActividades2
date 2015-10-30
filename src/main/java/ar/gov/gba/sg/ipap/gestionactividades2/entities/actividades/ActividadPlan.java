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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que modela las distintas Actividades planificadas que conforman los diferentes Sub Programas.
 * Cada una de estas Actividades podrá implementarse todas las veces que sea necesario, siendo cada una de estas una Actividad Implementada.
 * Se vincula con:
 *      SubPrograma,
 *      ActividadImplementada,
 *      ADmEntidad
 * @author rincostante
 */
@Entity
public class ActividadPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre de la Actividad. El mismo también será invocado por cada una de sus implementaciones.
     */    
    @Column (nullable=false, length=200, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 200)
    private String nombre;

    /**
     * Campo binario que indica si la actividad está o no suspendida
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean suspendido = false;
    
    /**
     * Campo de texto que registra las observaciones de la Actividad
     */
    @Column (nullable=true, length=1000)
    @Size(message = "{endidades.stringSizeError}", max = 1000)
    private String observaciones;
    
    /**
     * Campo de texto que indica el resumen académico del contenido de la actividad.
     */    
    @Column (nullable=false, length=1500)
    @NotNull(message = "Este campo es obligatorio")
    @Size(message = "El campo no puede excederse de  los 1500 caracteres", max = 1500)
    private String resumenAcademico;    
    
    /**
     * Campo de texto que contiene la url a la informaciación general de la actividad en el centro documental
     */    
    @Column (nullable=true, length=200)
    private String verMas;        
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;

    /**
     * Campo de tipo Array que contiene el conjunto de los Subprogramas que contienen esta Actividad
     */
    @ManyToMany
    @JoinTable(
            name = "actividadesXSubProgramas",
            joinColumns = @JoinColumn(name = "actividad_fk"),
            inverseJoinColumns = @JoinColumn(name = "subprograma_fk")
    )
    private List<SubPrograma> subprogramas;
    
    /**
     * Campo de tipo Array que contiene las implementaciones de la Actividad
     */
    @OneToMany(mappedBy="actividadPlan")
    private List<ActividadImplementada> actividadesImplementadas;  
    
    @Transient
    private String link;
    
    /**
     * Constructor
     */
    public ActividadPlan(){
        actividadesImplementadas = new ArrayList();
        subprogramas = new ArrayList();
    }

    public String getLink() {
        if(this.verMas.equals("")){
            this.link = "http://www.ipap.sg.gba.gov.ar/";
        }else{
            this.link = this.verMas;
        }
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getVerMas() {
        return verMas;
    }

    public void setVerMas(String verMas) {
        this.verMas = verMas;
    }

    public String getResumenAcademico() {
        return resumenAcademico;
    }

    public void setResumenAcademico(String resumenAcademico) {
        this.resumenAcademico = resumenAcademico;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<ActividadImplementada> getActividadesImplementadas() {
        return actividadesImplementadas;
    }

    /**
     *
     * @param actividadesImplementadas
     */
    public void setActividadesImplementadas(List<ActividadImplementada> actividadesImplementadas) {
        this.actividadesImplementadas = actividadesImplementadas;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<SubPrograma> getSubprogramas() {
        return subprogramas;
    }

    /**
     *
     * @param subprogramas
     */
    public void setSubprogramas(List<SubPrograma> subprogramas) {
        this.subprogramas = subprogramas;
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
    public boolean isSuspendido() {
        return suspendido;
    }

    /**
     *
     * @param suspendido
     */
    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
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
        if (!(object instanceof ActividadPlan)) {
            return false;
        }
        ActividadPlan other = (ActividadPlan) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.ActividadPlan[ id=" + id + " ]";
    }
    
}
