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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que modela las distintas Actividades planificadas que conforman los diferentes Sub Programas.
 * Cada una de estas Actividades podrá implementarse todas las veces que sea necesario, siendo cada una de estas una Actividad Implementada.
 * Se vincula con:
 *      SubPrograma,
 *      TipoCapacitacion,
 *      Modalidad,
 *      CampoTematico,
 *      Organismo,
 *      ActividadImplementada,
 *      Resolucion,
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
     * Campo entero que indica la carga horaria asignada a la actividad
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private int cargaHoraria;
    
    /**
     * Campo binario que indica si la Actividad será con evaluación
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean evalua = false;
    
    /**
     * Campo binario que indica si la Actividad conlleva una certificación
     */
    @Column (nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean certifica = false;
    
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
     * Campo de tipo Resolucion que contiene la resolución que da marco institucional a la Actividad.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="resolucion_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Resolucion resolucion; 
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin;
    
    /**
     * Campo de tipo Organismo que consigna el Organismo que solicitó la Actividad
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="organismo_id", nullable=true)
    private Organismo organismo;

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
     * Campo de tipo TipoCapacitacion que indica su tipo
     */
    @ManyToOne
    @JoinColumn(name="tipocapacitacion_id")
    private TipoCapacitacion tipoCapacitacion;
    
    /**
     * Campo de tipo Modalidad que indica la modalidad de la Actividad
     */
    @ManyToOne
    @JoinColumn(name="modalidad_id")
    private Modalidad modalidad;
    
    /**
     * Campo de tipo CampoTematico que indica el campo temático de la Actividad
     */
    @ManyToOne
    @JoinColumn(name="campotematico_id")
    private CampoTematico campoTematico;
    
    /**
     * Campo de tipo Array que contiene las implementaciones de la Actividad
     */
    @OneToMany(mappedBy="actividadPlan")
    private List<ActividadImplementada> actividadesImplementadas;  
    
    /**
     * Constructor
     */
    public ActividadPlan(){
        actividadesImplementadas = new ArrayList();
        subprogramas = new ArrayList();
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
    public CampoTematico getCampoTematico() {
        return campoTematico;
    }

    /**
     *
     * @param campoTematico
     */
    public void setCampoTematico(CampoTematico campoTematico) {
        this.campoTematico = campoTematico;
    }

    /**
     *
     * @return
     */
    public Modalidad getModalidad() {
        return modalidad;
    }

    /**
     *
     * @param modalidad
     */
    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    /**
     *
     * @return
     */
    public TipoCapacitacion getTipoCapacitacion() {
        return tipoCapacitacion;
    }

    /**
     *
     * @param tipoCapacitacion
     */
    public void setTipoCapacitacion(TipoCapacitacion tipoCapacitacion) {
        this.tipoCapacitacion = tipoCapacitacion;
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
    public int getCargaHoraria() {
        return cargaHoraria;
    }

    /**
     *
     * @param cargaHoraria
     */
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    /**
     *
     * @return
     */
    public boolean isEvalua() {
        return evalua;
    }

    /**
     *
     * @param evalua
     */
    public void setEvalua(boolean evalua) {
        this.evalua = evalua;
    }

    /**
     *
     * @return
     */
    public boolean isCertifica() {
        return certifica;
    }

    /**
     *
     * @param certifica
     */
    public void setCertifica(boolean certifica) {
        this.certifica = certifica;
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
    public Resolucion getResolucion() {
        return resolucion;
    }

    /**
     *
     * @param resolucion
     */
    public void setResolucion(Resolucion resolucion) {
        this.resolucion = resolucion;
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
    public Organismo getOrganismo() {
        return organismo;
    }

    /**
     *
     * @param organismo
     */
    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
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
