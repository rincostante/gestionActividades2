/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Enitdad que encapsula la información básica y principal de las personas que gestiona e interactúan con el sistema
 * Se vincula con:
 *      Localidad,
 *      TipoDocumento,
 *      Agente,
 *      Docente,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el/los apellido/s de las personas
     */        
    @Column (nullable=false, length=100)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 100)
    private String apellidos;      
    
    /**
     * Campo de texto que indica el/los nombres/s de las personas
     */        
    @Column (nullable=false, length=80)
    @NotNull(message = "{entidades.fieldNotNullError}")
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 80)
    private String nombres;   
    
    /**
     * Campo entero que indica el número de documento
     */
    @Column (nullable=false, unique=true)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private int documento;
    
    /**
     * Campo de texto que indica el número de teléfono fijo
     */        
    @Column (nullable=true, length=20)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String telefono;   
    
    /**
     * Campo de texto que indica el número de teléfono celular
     */        
    @Column (nullable=true, length=20)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String celular;   
    
    /**
     * Campo de texto que indica la fecha de nacimiento
     */      
    @Temporal(TemporalType.DATE)
    @Column(nullable=true)
    private Date fechaNacimiento;
    
    /**
     * Caracter que indica el sexo (M/F)
     */        
    @Column (nullable=true, length=1)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 1)
    private String sexo; 
    
    /**
     * Campo de texto que indica el correo electrónico principal
     */        
    @Column (nullable=true, length=50)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 50)
    private String email_1;
    
    /**
     * Campo de texto que indica el correo electrónico de respaldo
     */        
    @Column (nullable=true, length=50)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 50)
    private String email_2;
    
    /**
     * Campo de tipo TipoDocumento que contiene el tipo de documento de la persona.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tipodocumento_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private TipoDocumento tipoDocumento;    
    
    /**
     * Campo de tipo Localidad que contiene la localidad en que que vive la persona.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="localidad_id")
    private Localidad localidad;  

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
    public String getApellidos() {
        return apellidos;
    }

    /**
     *
     * @param apellidos
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     *
     * @return
     */
    public String getNombres() {
        return nombres;
    }

    /**
     *
     * @param nombres
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     *
     * @return
     */
    public int getDocumento() {
        return documento;
    }

    /**
     *
     * @param documento
     */
    public void setDocumento(int documento) {
        this.documento = documento;
    }

    /**
     *
     * @return
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     *
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     *
     * @return
     */
    public String getCelular() {
        return celular;
    }

    /**
     *
     * @param celular
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     *
     * @return
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     *
     * @param fechaNacimiento
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     *
     * @return
     */
    public String getSexo() {
        return sexo;
    }

    /**
     *
     * @param sexo
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     *
     * @return
     */
    public String getEmail_1() {
        return email_1;
    }

    /**
     *
     * @param email_1
     */
    public void setEmail_1(String email_1) {
        this.email_1 = email_1;
    }

    /**
     *
     * @return
     */
    public String getEmail_2() {
        return email_2;
    }

    /**
     *
     * @param email_2
     */
    public void setEmail_2(String email_2) {
        this.email_2 = email_2;
    }

    /**
     *
     * @return
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     *
     * @param tipoDocumento
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     *
     * @return
     */
    public Localidad getLocalidad() {
        return localidad;
    }

    /**
     *
     * @param localidad
     */
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Persona[ id=" + id + " ]";
    }
    
}
