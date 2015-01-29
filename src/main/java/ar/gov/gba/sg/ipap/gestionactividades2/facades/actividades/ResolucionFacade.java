/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Resolucion;
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
public class ResolucionFacade extends AbstractFacade<Resolucion> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResolucionFacade() {
        super(Resolucion.class);
    }
  
    /**
     * Método que verifica si la Resolucion puede ser eliminada
     * @param id: Id de la Resolucion que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT actImp.nombre FROM ActividadImplementada actImp "
                + "INNER JOIN actImp.actividadPlan actPlan "
                + "INNER JOIN actPlan.subprogramas sub "
                + "INNER JOIN sub.programa prog "
                + "WHERE prog.resolucion.id = :id "
                + "OR sub.resolucion.id = :id "
                + "OR actPlan.resolucion.id = :id "
                + "OR actImp.resolucion.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista una Resolucion con este nombre ya ingresada
     * @param res
     * @param anio
     * @return 
     */
    public boolean noExiste(String res, int anio){
        em = getEntityManager();
        String queryString = "SELECT res FROM Resolucion res "
                + "WHERE res.resolucion = :res "
                + "res.anio = :anio";
        Query q = em.createQuery(queryString)
                .setParameter("res", res)
                .setParameter("anio", anio);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene una Resolucion existente según los datos recibidos como parámetro
     * @param res
     * @param anio
     * @return 
     */
    public Resolucion getExistente(String res, int anio){
        em = getEntityManager();
        String queryString = "SELECT res FROM Resolucion res "
                + "WHERE res.resolucion = :res "
                + "res.anio = :anio";
        Query q = em.createQuery(queryString)
                .setParameter("res", res)
                .setParameter("anio", anio);
        return (Resolucion)q.getSingleResult();
    }    
    
    /**
     * Método que devuelve todas las Resoluciones habilitadas
     * @return 
     */
    public List<Resolucion> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT res FROM Resolucion res "
                + "WHERE res.admin.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas las Resoluciones deshabilitadas
     * @return 
     */
    public List<Resolucion> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT res FROM Resolucion res "
                + "WHERE res.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
}
