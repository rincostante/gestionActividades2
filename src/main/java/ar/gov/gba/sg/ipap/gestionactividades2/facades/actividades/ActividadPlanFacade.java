/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
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
public class ActividadPlanFacade extends AbstractFacade<ActividadPlan> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadPlanFacade() {
        super(ActividadPlan.class);
    }
 
   /**
     * Método que verifica si la Actividad Planificada puede ser eliminada
     * @param id: Id de la ActividadPlan que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT sub.id FROM SubPrograma sub "
                + "INNER JOIN sub.actividadesPlan actPlan "
                + "INNER JOIN actPlan.actividadesImplementadas actImp "
                + "WHERE actPlan.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista una Actividad Planificada con este nombre ya ingresado
     * @param nombre
     * @return 
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();
        String queryString = "SELECT actPlan FROM ActividadPlan actPlan "
                + "WHERE actPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene una Actividad Planificada existente según los datos recibidos como parámetro
     * @param nombre
     * @return 
     */
    public ActividadPlan getExistente(String nombre){
        List<ActividadPlan> lProg;
        em = getEntityManager();
        String queryString = "SELECT actPlan FROM ActividadPlan actPlan "
                + "WHERE actPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lProg = q.getResultList();
        if(!lProg.isEmpty()){
            return lProg.get(0);
        }else{
            return null;
        }
    }    
    
    /**
     * Método que devuelve todas los Actividades Planificadas habilitadas y vigentes
     * @return 
     */
    public List<ActividadPlan> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actPlan FROM ActividadPlan actPlan "
                + "WHERE actPlan.admin.habilitado = true "
                + "AND actPlan.suspendido = false "
                + "ORDER BY actPlan.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Actividades Planificadas deshabilitadas
     * @return 
     */
    public List<ActividadPlan> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actPlan FROM ActividadPlan actPlan "
                + "WHERE actPlan.admin.habilitado = false "
                + "ORDER BY actPlan.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }          
    
    /**
     * Método que devuelve los Actividades Planificadas suspendidas
     * @return 
     */
    public List<ActividadPlan> getSuspendidas(){
        em = getEntityManager();
        String queryString = "SELECT actPlan FROM ActividadPlan actPlan "
                + "WHERE actPlan.admin.habilitado = true "
                + "AND actPlan.suspendido = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
}
