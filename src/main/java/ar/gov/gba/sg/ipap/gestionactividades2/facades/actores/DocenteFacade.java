/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocenteFacade() {
        super(Docente.class);
    }
    
   /**
     * Método que verifica si el Docente puede ser eliminado
     * @param id: Id de la Docente que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT doc.id FROM Docente doc "
                + "INNER JOIN doc.usuario us "
                + "INNER JOIN doc.clases cls "
                + "INNER JOIN doc.actividades act "
                + "WHERE doc.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Docente con la Persona o el Agente cuyos id son recibidos como parámetros
     * @param idAgente
     * @param idPersona
     * @return 
     */
    public boolean noExiste(Long idAgente, Long idPersona){
        em = getEntityManager();
        String queryString = "SELECT doc FROM Docente doc "
                + "WHERE doc.persona.id = :idPersona "
                + "OR doc.agente.id = :idAgente";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona)
                .setParameter("idAgente", idAgente);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Docente existente según los datos recibidos como parámetro
     * @param idAgente
     * @param idPersona
     * @return 
     */
    public Docente getExistente(Long idAgente, Long idPersona){
        List<Docente> lDoc;
        em = getEntityManager();
        String queryString = "SELECT doc FROM Docente doc "
                + "WHERE doc.persona.id = :idPersona "
                + "OR doc.agente.id = :idAgente";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona)
                .setParameter("idAgente", idAgente);
        lDoc = q.getResultList();
        if(!lDoc.isEmpty()){
            return lDoc.get(0);
        }else{
            return null;
        }
    }   
    
    /**
     * Método que devuelve todas los Docentes habilitadas
     * @return 
     */
    public List<Docente> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT doc FROM Docente doc "
                + "WHERE doc.admin.habilitado = true "
                + "ORDER BY doc.persona.apellidos, doc.persona.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Docentes deshabilitadas
     * @return 
     */
    public List<Docente> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT doc FROM Docente doc "
                + "WHERE doc.admin.habilitado = false "
                + "ORDER BY doc.persona.apellidos, doc.persona.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
    
    /**
     * Método que devuelve true si el docente está disponible en esa fecha y horarios
     * y devuelve false si no lo está
     * @param doc
     * @param fecha
     * @param horaInicio
     * @param horaFin
     * @param curso
     * @return 
     */
    public boolean isDisponible(Docente doc, Date fecha, Date horaInicio, Date horaFin, ActividadImplementada curso){
        em = getEntityManager();
        String queryString = "SELECT clase.id FROM Clase clase "
                + "WHERE clase.docente = :doc "
                + "AND clase.fechaRealizacion = :fecha "
                + "AND clase.actividad != :curso "
                + "AND (clase.horaFin > :horaInicio "
                + "OR clase.horaInicio < :horaFin)";
        Query q = em.createQuery(queryString)
                .setParameter("doc", doc)
                .setParameter("fecha", fecha)
                .setParameter("horaInicio", horaInicio)
                .setParameter("horaFin", horaFin)
                .setParameter("curso", curso);
        return q.getResultList().isEmpty();
    }
}