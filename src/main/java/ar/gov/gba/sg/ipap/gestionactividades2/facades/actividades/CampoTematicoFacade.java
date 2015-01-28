/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.CampoTematico;
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
public class CampoTematicoFacade extends AbstractFacade<CampoTematico> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CampoTematicoFacade() {
        super(CampoTematico.class);
        //ñaokndfño
          //      añsdknf
    }
 
    /**
     * Método que verifica si la Campo Temático puede ser eliminado
     * @param id: Id del Campo Tematico que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT campo.nombre FROM CampoTematico campo "
                + "INNER JOIN campo.actividades actPlan "
                + "WHERE campo.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Campo Temático con ese nombre ya ingresado
     * @param nombre: nombre del Campo Temático
     * @return 
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();
        String queryString = "SELECT campo FROM CampoTematico campo "
                + "WHERE campo.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Campo Temático existente según los datos recibidos como parámetro
     * @param nombre: nombre del Campo Temático
     * @return 
     */
    public CampoTematico getExistente(String nombre){
        em = getEntityManager();
        String queryString = "SELECT campo FROM CampoTematico campo "
                + "WHERE campo.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return (CampoTematico)q.getSingleResult();
    }    
    
    /**
     * Método que devuelve todas los Campos Tematicos habilitados y vigentes
     * @return 
     */
    public List<CampoTematico> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT campo FROM CampoTematico campo "
                + "WHERE campo.admin.habilitado = true "
                + "AND campo.fechaFinVigencia >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve los Campos Temáticos habilitados vencidos
     * @return 
     */
    public List<CampoTematico> getVencidos(){
        em = getEntityManager();
        String queryString = "SELECT campo FROM CampoTematico campo "
                + "WHERE campo.admin.habilitado = true "
                + "AND campo.fechaFinVigencia < CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Campos Tematicos deshabilitadas
     * @return 
     */
    public List<CampoTematico> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT campo FROM CampoTematico campo "
                + "WHERE campo.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
    
    /**
     * Método que devuelve los Campos temáticos
     */
}
