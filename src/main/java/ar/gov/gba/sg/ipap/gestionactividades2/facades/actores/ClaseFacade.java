/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
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
public class ClaseFacade extends AbstractFacade<Clase> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClaseFacade() {
        super(Clase.class);
    }
    
  /**
     * Método que verifica si la Clase puede ser eliminada
     * @param id: Id de la ActividadImplementada que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT part.id FROM Participante part "
                + "INNER JOIN part.clases clase "
                + "WHERE clase.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validar que no exista una Clase con el mismo número de orden para una misma actividad implementada
     * @param actImp
     * @param numOrden
     * @return 
     */
    public boolean noExiste(ActividadImplementada actImp, int numOrden){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.numOrden = :numOrden "
                + "AND clase.actividad = :actImp";
        Query q = em.createQuery(queryString)
                .setParameter("actImp", actImp)
                .setParameter("numOrden", numOrden);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene una Clase con el mismo número de orden para una misma actividad implementada
     * @param actImp
     * @param numOrden
     * @return 
     */
    public Clase getExistente(ActividadImplementada actImp, int numOrden){
        List<Clase> lClases;
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.numOrden = :numOrden "
                + "AND clase.actividad = :actImp";
        Query q = em.createQuery(queryString)
                .setParameter("actImp", actImp)
                .setParameter("numOrden", numOrden);
        lClases = q.getResultList();
        if(!lClases.isEmpty()){
            return lClases.get(0);
        }else{
            return null;
        }
    }    
    
    /**
     * Método que devuelve todas las Clases habilitadas y vigentes,
     * es decir, que conforman una actividad en curso.
     * @return 
     */
    public List<ActividadImplementada> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.admin.habilitado = true "
                + "AND clase.actividad.fechaFin >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas las Clases deshabilitadas
     * @return 
     */
    public List<ActividadImplementada> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }          
    
    /**
     * Método que devuelve las Clases finalizadas
     * @return 
     */
    public List<ActividadImplementada> getFinalizadas(){
        
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.admin.habilitado = true "
                + "AND clase.actividad.fechaFin < CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      
    
    /**
     * Método para validad la disponibilidad del docente que se pretende asignar
     * @param docente
     * @param fecha
     * @param horaInicial
     * @param horaFinal
     * @return Devuelve true si está disponible y false si no lo está
     */
    public boolean getDispoDocente(Docente docente, Date fecha, Date horaInicial, Date horaFinal){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.docente = :docente "
                + "AND clase.fechaRealizacion = :fecha "
                + "AND clase.horaInicio >= :horaInicial "
                + "AND clase.horaFin <= :horaFinal";
        Query q = em.createQuery(queryString)
                .setParameter("docente", docente)
                .setParameter("fecha", fecha)
                .setParameter("horaInicial", horaInicial)
                .setParameter("horaFinal", horaFinal);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que devuelve el último número de orden asignado para una clase de un mismo curso
     * @param curso
     * @return 
     */
    public int getUltimoNumeroDeOrden(ActividadImplementada curso){
        List<Clase> lClases;
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.actividad = :curso "
                + "ORDER BY clase.numOrden";
        Query q = em.createQuery(queryString)
                .setParameter("curso", curso);
        lClases = q.getResultList();
        if(!lClases.isEmpty()){
            return lClases.get(lClases.size() - 1).getNumOrden();
        }else{
            return 0;
        }
    }
    
    /**
     * Método para saber si un curso tiene clases habilitadas registradas
     * @param curso
     * @return 
     */
    public boolean isClasesEmpty(ActividadImplementada curso){
        em = getEntityManager();
        String queryString = "SELECT clase.id FROM Clase clase "
                + "WHERE clase.actividad = :curso";
        Query q = em.createQuery(queryString)
                .setParameter("curso", curso);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método para obtener la fecha de realización de la última clase registrada para un mismo curso
     * @param curso
     * @return 
     */
    public Date getLastFechaRelizacion(ActividadImplementada curso){
        em = getEntityManager();
        List<Clase> listClases;
        String queryString = "SELECT clase FROM Clase clase "
                + "WHERE clase.actividad = :curso "
                + "ORDER BY clase.fechaRealizacion";
        Query q = em.createQuery(queryString)
                .setParameter("curso", curso);
        listClases = q.getResultList();
        Date fecha = listClases.get(listClases.size() - 1).getFechaRealizacion();
        return fecha;
    }
}
