/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
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
public class SubProgramaFacade extends AbstractFacade<SubPrograma> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubProgramaFacade() {
        super(SubPrograma.class);
    }
    
    /**
     * Método que verifica si el Sub Programa puede ser eliminado
     * @param id: Id de la Sub Programa que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT actPlan.id FROM ActividadPlan actPlan "
                + "INNER JOIN actPlan.subprogramas sub "
                + "WHERE sub.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Sub Programa con este nombre ya ingresado
     * @param nombre
     * @return 
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Sub Programa existente según los datos recibidos como parámetro
     * @param nombre
     * @return 
     */
    public SubPrograma getExistente(String nombre){
        List<SubPrograma> lProg;
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.nombre = :nombre";
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
     * Método que devuelve todas los Sub Programas habilitados
     * @return 
     */
    public List<SubPrograma> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.admin.habilitado = true "
                + "AND sub.fechaFinVigencia >= CURRENT_DATE "
                + "ORDER BY sub.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Sub Programas deshabilitados
     * @return 
     */
    public List<SubPrograma> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.admin.habilitado = false "
                + "ORDER BY sub.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }          
    
    /**
     * Método que devuelve los Sub Programas habilitados vencidos
     * @return 
     */
    public List<SubPrograma> getVencidos(){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.admin.habilitado = true "
                + "AND sub.fechaFinVigencia < CURRENT_DATE "
                + "ORDER BY sub.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
    
    /**
     * Método que devuelve los Subrpogramas completables
     * @return 
     */
    public List<SubPrograma> getCompletables(){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "WHERE sub.completable = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve los subrpogramas vinculados a una misma actividad formativa
     * @param actPlan
     * @return 
     */
    public List<SubPrograma> getPorActFormativa(ActividadPlan actPlan){
        em = getEntityManager();
        String queryString = "SELECT sub FROM SubPrograma sub "
                + "INNER JOIN sub.actividadesPlan actPlan "
                + "WHERE actPlan = :actPlan";
        Query q = em.createQuery(queryString)
                .setParameter("actPlan", actPlan);
        return q.getResultList();
    }
}
