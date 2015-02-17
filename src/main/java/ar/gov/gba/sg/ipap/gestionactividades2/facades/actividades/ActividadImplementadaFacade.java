/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
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
public class ActividadImplementadaFacade extends AbstractFacade<ActividadImplementada> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadImplementadaFacade() {
        super(ActividadImplementada.class);
    }
    
  /**
     * Método que verifica si la Actividad Implementada puede ser eliminada
     * @param id: Id de la ActividadImplementada que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT part.id FROM Participante part "
                + "INNER JOIN part.actividad actImp "
                + "WHERE actImp.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validar que no exista una Implementación de la misma Actividad para un mismo período en una misma sede
     * @param nombre
     * @param fechaInio
     * @param fechaFin
     * @param idSede
     * @return 
     */
    public boolean noExiste(String nombre, Date fechaInio, Date fechaFin, Long idSede){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE (actImp.fechaInicio >= :fechaInio "
                + "AND actImp.fechaFin <= :fechaFin) "
                + "AND actImp.sede.id = :idSede "
                + "AND actImp.actividadPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("fechaInio", fechaInio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("idSede", idSede)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene una Actividad Implementada existente según los datos recibidos como parámetro
     * @param nombre
     * @param fechaInio
     * @param fechaFin
     * @param idSede
     * @return 
     */
    public ActividadImplementada getExistente(String nombre, Date fechaInio, Date fechaFin, Long idSede){
        List<ActividadImplementada> lProg;
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE (actImp.fechaInicio >= :fechaInio "
                + "AND actImp.fechaFin <= :fechaFin) "
                + "AND actImp.sede.id = :idSede "
                + "AND actImp.actividadPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("fechaInio", fechaInio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("idSede", idSede)
                .setParameter("nombre", nombre);
        lProg = q.getResultList();
        if(!lProg.isEmpty()){
            return lProg.get(0);
        }else{
            return null;
        }
    }    
    
    /**
     * Método que devuelve todas los Actividades Implementadas habilitadas y vigentes
     * @return 
     */
    public List<ActividadImplementada> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = false "
                + "AND actImp.fechaFin >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Actividades Implementadas habilitadas y vigentes para la interfase de coordinador
     * @param us: Agente que actúa como coordinador del curso
     * @return 
     */
    public List<ActividadImplementada> getHabilitadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = false "
                + "AND actImp.fechaFin >= CURRENT_DATE "
                + "AND actImp.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Actividades Implementadas deshabilitadas
     * @return 
     */
    public List<ActividadImplementada> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
    
    /**
     * Método que devuelve todas los Actividades Implementadas deshabilitadas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getDeshabilitadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = false "
                + "AND actImp.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }         
    
    /**
     * Método que devuelve los Actividades Implementadas finalizadas
     * @return 
     */
    public List<ActividadImplementada> getFinalizadas(){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.fechaFin < CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método que devuelve los Actividades Implementadas finalizadas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getFinalizadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.fechaFin < CURRENT_DATE "
                + "AND actImp.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }     
    
    /**
     * Método que devuelve los Actividades Implementadas suspendidas
     * @return 
     */
    public List<ActividadImplementada> getSuspendidas(){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }     
    
    /**
     * Método que devuelve los Actividades Implementadas suspendidas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getSuspendidasXCoor(Usuario us){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = true "
                + "AND actImp.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }       
}
