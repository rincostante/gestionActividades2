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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que modela los Sub Programas que agrupan todas las Actividades gestionadas
 * Se vincula a:
 *      Programa,
 *      Resolucion,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class SubPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre del SubPrograma
     */  
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 100)
    private String nombre;
    
    /**
     * Campo de tipo fecha que indica el comienzo de vigencia del SubPrograma
     */
    @Temporal(TemporalType.DATE)
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private Date fechaInicioVigencia;    

    /**
     * Campo de tipo fecha que indica el fin de la vigencia del SubPrograma
     */ 
    @Temporal(TemporalType.DATE)
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private Date fechaFinVigencia; 
    
    /**
     * Campo de tipo boolean que indica si el subprograma es completable, 
     * como en el caso de las Diplomaturas y Especializaciones
     */
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean completable;    
    
    /**
     * Campo de tipo SubPrograma que indica si la entidad depende de
     * otro SubPrograma para ser completado
     */
    @OneToOne
    @JoinColumn(name="subproganterior_id")    
    private SubPrograma dependeDe;
    
    /**
     * Campo de tipo array que contiene los serviocios de atención que dependen de este
     */            
    @OneToOne(mappedBy="dependeDe")
    private SubPrograma continuadoPor;     
    
    /**
     * Campo de tipo Programa que contiene el Programa al cual pertenece el SubPrograma
     */
    @ManyToOne
    @JoinColumn(name="programa_id")
    private Programa programa;
    
    /**
     * Campo de tipo Resolucion que contiene la Resolución que le da marco institucional al SubPrograma
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="resolucion_id", nullable=true)
    private Resolucion resolucion;
    
    /**
     * Campo de tipo TipoEspecializacion que contiene el Tipo de Especialización del el SubPrograma
     * en el caso que fuera una Especialización
     */
    @ManyToOne
    @JoinColumn(name="tipoespecializacion_id")
    private TipoEspecializacion tipoEspecializacion;      
    
    /**
     * Campo de tipo array que contiene las Actividades planificadas que se vinculen con el subprograma
     */    
    @ManyToMany(mappedBy = "subprogramas")
    private List<ActividadPlan> actividadesPlan;    
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos de administración y trazabilidad del Programa
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
     * Constructor
     */
    public SubPrograma(){
        actividadesPlan = new ArrayList();
    }    

    public boolean isCompletable() {
        return completable;
    }

    public void setCompletable(boolean completable) {
        this.completable = completable;
    }

    public SubPrograma getDependeDe() {
        return dependeDe;
    }

    public void setDependeDe(SubPrograma dependeDe) {
        this.dependeDe = dependeDe;
    }

    public SubPrograma getContinuadoPor() {
        return continuadoPor;
    }

    public void setContinuadoPor(SubPrograma continuadoPor) {
        this.continuadoPor = continuadoPor;
    }

    public TipoEspecializacion getTipoEspecializacion() {
        return tipoEspecializacion;
    }

    public void setTipoEspecializacion(TipoEspecializacion tipoEspecializacion) {
        this.tipoEspecializacion = tipoEspecializacion;
    }

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
    public List<ActividadPlan> getActividadesPlan() {
        return actividadesPlan;
    }
    
    /**
     *
     * @param actividadesPlan
     */
    public void setActividadesPlan(List<ActividadPlan> actividadesPlan) {
        this.actividadesPlan = actividadesPlan;
    }

    /**
     *
     * @return
     */
    public Programa getPrograma() {
        return programa;
    }

    /**
     *
     * @param programa
     */
    public void setPrograma(Programa programa) {
        this.programa = programa;
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
        if (!(object instanceof SubPrograma)) {
            return false;
        }
        SubPrograma other = (SubPrograma) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actividades.SubPrograma[ id=" + id + " ]";
    }
    
}
