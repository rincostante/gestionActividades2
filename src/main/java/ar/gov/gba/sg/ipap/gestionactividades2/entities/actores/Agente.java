/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.entities.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.AdmEntidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula la información de los agentes públicos vinculados a la aplicación
 * Se vincula con:
 *      Persona,
 *      EstudiosCursados,
 *      SituacionRevista,
 *      Usuario,
 *      Docente,
 *      NivelIpap,
 *      Participante,
 *      Cargo,
 *      Titulo,
 *      AdmEntidad
 * @author rincostante
 */
@Entity
public class Agente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Campo de texto que indica el nombre de la calle del lugar de trabajo del agente
     */        
    @Column (nullable=true, length=30)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 30)
    private String domCalle;    
    
    /**
     * Campo de texto que indica el nombre del número de puerta del lugar de trabajo del agente
     */        
    @Column (nullable=true, length=10)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 10)
    private String domNumero;        
    
    /**
     * Campo de texto que indica el teléfono del lugar de trabajo del agente
     */        
    @Column (nullable=true, length=20)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 20)
    private String telefono;    
    
    /**
     * Campo de texto que indica el correo electrónico del lugar de trabajo del agente
     */        
    @Column (nullable=true, length=60)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 60)
    private String email;  
    
    /**
     * Campo de tipo booleano que indica si el agente es referente
     */  
    @Column(nullable=false)
    @NotNull(message = "{entidades.fieldNotNullError}")
    private boolean esReferente = true;   
    
    /**
     * Campo de texto que indica los cursos realizados por el agente en el IPAP
     */        
    @Column (nullable=true, length=500)
    @Size(message = "{endidades.stringSizeError}", min = 1, max = 500)
    private String cursosRealizados;  
    
    /**
     * Campo entero que indica los años de antigüedad del agente en su lugar de trabajo
     */
    @Column (nullable=true)
    private int antiguedad;
    
    /**
     * Campo de tipo Persona que contiene los datos básicos de la persona del agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="persona_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Persona persona;   
    
    /**
     * Campo de tipo EstudiosCursados que contiene los datos de los estudios del agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estudiosCursados_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private EstudiosCursados estudiosCursados;   
    
    /**
     * Campo de tipo Organismo que contiene los datos del organismo en que se desempeña el agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="organismo_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Organismo organismo;
    
    /**
     * Campo de tipo NivelIpap que contiene los datos de los estudios realizados en el IPAP por el agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="nivelipap_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private NivelIpap nivelIpap;
    
    /**
     * Campo de tipo Titulo que contiene el título de educación formal alcanzado por el agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="titulo_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Titulo titulo;
    
    /**
     * Campo de tipo Agente que contiene al agente referente del actual.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="agente_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Agente referente;    
    
    /**
     * Campo de tipo Cargo que contiene el cargo que ocupa el agente en su lugar de trabajo.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cargo_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private Cargo cargo;      
    
    /**
     * Campo de tipo SituacionRevista que contiene la situación de revista del agente.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="situacionRevista_id", nullable=false)
    @NotNull(message = "{entidades.objectNotNullError}")
    private SituacionRevista situacionRevista; 
    
    /**
     * Campo que guarda las participaciones que ha tenido el agente
     */
    @OneToMany(mappedBy="agente")
    private List<Participante> participaciones;
    
    /**
     * Campo de tipo AdmEntidad que encapsula los datos propios para su trazabilidad.
     */
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @NotNull(message = "{enitdades.objectNotNullError}") 
    private AdmEntidad admin; 
    
    /**
     * Constructor
     */
    public Agente(){
        participaciones = new ArrayList();
    }

    /**
     *
     * @return
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     *
     * @param persona
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<Participante> getParticipaciones() {
        return participaciones;
    }

    /**
     *
     * @param participaciones
     */
    public void setParticipaciones(List<Participante> participaciones) {
        this.participaciones = participaciones;
    }

    /**
     *
     * @return
     */
    public String getDomCalle() {
        return domCalle;
    }

    /**
     *
     * @param domCalle
     */
    public void setDomCalle(String domCalle) {
        this.domCalle = domCalle;
    }

    /**
     *
     * @return
     */
    public String getDomNumero() {
        return domNumero;
    }

    /**
     *
     * @param domNumero
     */
    public void setDomNumero(String domNumero) {
        this.domNumero = domNumero;
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
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public boolean isEsReferente() {
        return esReferente;
    }

    /**
     *
     * @param esReferente
     */
    public void setEsReferente(boolean esReferente) {
        this.esReferente = esReferente;
    }

    /**
     *
     * @return
     */
    public String getCursosRealizados() {
        return cursosRealizados;
    }

    /**
     *
     * @param cursosRealizados
     */
    public void setCursosRealizados(String cursosRealizados) {
        this.cursosRealizados = cursosRealizados;
    }

    /**
     *
     * @return
     */
    public int getAntiguedad() {
        return antiguedad;
    }

    /**
     *
     * @param antiguedad
     */
    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    /**
     *
     * @return
     */
    public EstudiosCursados getEstudiosCursados() {
        return estudiosCursados;
    }

    /**
     *
     * @param estudiosCursados
     */
    public void setEstudiosCursados(EstudiosCursados estudiosCursados) {
        this.estudiosCursados = estudiosCursados;
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
    public NivelIpap getNivelIpap() {
        return nivelIpap;
    }

    /**
     *
     * @param nivelIpap
     */
    public void setNivelIpap(NivelIpap nivelIpap) {
        this.nivelIpap = nivelIpap;
    }

    /**
     *
     * @return
     */
    public Titulo getTitulo() {
        return titulo;
    }

    /**
     *
     * @param titulo
     */
    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * @return
     */
    public Agente getReferente() {
        return referente;
    }

    /**
     *
     * @param referente
     */
    public void setReferente(Agente referente) {
        this.referente = referente;
    }

    /**
     *
     * @return
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     *
     * @param cargo
     */
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    /**
     *
     * @return
     */
    public SituacionRevista getSituacionRevista() {
        return situacionRevista;
    }

    /**
     *
     * @param situacionRevista
     */
    public void setSituacionRevista(SituacionRevista situacionRevista) {
        this.situacionRevista = situacionRevista;
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
        if (!(object instanceof Agente)) {
            return false;
        }
        Agente other = (Agente) object;
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
        return "ar.gov.gba.sg.ipap.gestionactividades.entities.actores.Agente[ id=" + id + " ]";
    }
    
}
