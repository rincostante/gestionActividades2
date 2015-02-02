/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Programa;
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
public class ProgramaFacade extends AbstractFacade<Programa> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramaFacade() {
        super(Programa.class);
    }
    
    /**
     * Método que verifica si el Programa puede ser eliminado
     * @param id: Id de la Programa que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT sub.id FROM SubPrograma sub "
                + "INNER JOIN sub.programa prog "
                + "WHERE prog.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Programa con este nombre ya ingresado
     * @param nombre
     * @return 
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();
        String queryString = "SELECT prog FROM Programa prog "
                + "WHERE prog.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Programa existente según los datos recibidos como parámetro
     * @param nombre
     * @return 
     */
    public Programa getExistente(String nombre){
        List<Programa> lProg;
        em = getEntityManager();
        String queryString = "SELECT prog FROM Programa prog "
                + "WHERE prog.nombre = :nombre";
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
     * Método que devuelve todas los Programas habilitados y vigentes
     * @return 
     */
    public List<Programa> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT prog FROM Programa prog "
                + "WHERE prog.admin.habilitado = true "
                + "AND prog.fechaFinVigencia >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Programas deshabilitados
     * @return 
     */
    public List<Programa> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT prog FROM Programa prog "
                + "WHERE prog.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }          
    
    /**
     * Método que devuelve los Programas habilitados vencidos
     * @return 
     */
    public List<Programa> getVencidos(){
        em = getEntityManager();
        String queryString = "SELECT prog FROM Programa prog "
                + "WHERE prog.admin.habilitado = true "
                + "AND prog.fechaFinVigencia < CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
}
