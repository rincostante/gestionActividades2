/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
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
public class AgenteFacade extends AbstractFacade<Agente> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgenteFacade() {
        super(Agente.class);
    }
    
   /**
     * Método que verifica si el Agente puede ser eliminado
     * @param id: Id de la Agente que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.id FROM Agente ag "
                + "INNER JOIN ag.docente doc "
                + "INNER JOIN ag.participaciones part "
                + "WHERE ag.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Agente con la Persona cuyo id es recibido como parámetro
     * @param idPersona
     * @return 
     */
    public boolean noExiste(Long idPersona){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.persona.id = :idPersona";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Agente existente según los datos recibidos como parámetro
     * @param idPersona
     * @return 
     */
    public Agente getExistente(Long idPersona){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.persona.id = :idPersona";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona);
        return (Agente)q.getSingleResult();
    }   
    
    /**
     * Método que devuelve todas los Agentes habilitados
     * @return 
     */
    public List<Agente> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.admin.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Agentes deshabilitados
     * @return 
     */
    public List<Agente> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      
}
