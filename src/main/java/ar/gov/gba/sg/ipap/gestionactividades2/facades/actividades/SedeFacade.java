/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Sede;
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
public class SedeFacade extends AbstractFacade<Sede> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SedeFacade() {
        super(Sede.class);
    }
 
    /**
     * Método que verifica si la Sede puede ser eliminada
     * @param id: Id de la Sede que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT sede.nombre FROM Sede sede "
                + "INNER JOIN sede.actividades actImp "
                + "WHERE sede.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista una Sede con este nombre ya ingresada
     * @param nombre: nombre de la Sede
     * @return 
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();
        String queryString = "SELECT sede FROM Sede sede "
                + "WHERE sede.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene una Sede existente según los datos recibidos como parámetro
     * @param nombre: nombre de la Sede
     * @return 
     */
    public Sede getExistente(String nombre){
        em = getEntityManager();
        String queryString = "SELECT sede FROM Sede sede "
                + "WHERE sede.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return (Sede)q.getSingleResult();
    }    
    
    /**
     * Método que devuelve todas las Sedes habilitadas
     * @return 
     */
    public List<Sede> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT sede FROM Sede sede "
                + "WHERE sede.admin.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas las Sedes deshabilitadas
     * @return 
     */
    public List<Sede> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT sede FROM Sede sede "
                + "WHERE sede.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
}
