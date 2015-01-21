/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    /**
     * Método para validad que no exista un Usuario con el Agente o el Docente cuyos id es recibidos como parámetro
     * @param idAgente
     * @return 
     */
    public boolean noExiste(Long idDocente, Long idAgente){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.docente.id = :idDocente "
                + "OR us.agente.id = :idAgente";
        Query q = em.createQuery(queryString)
                .setParameter("idDocente", idDocente)
                .setParameter("idAgente", idAgente);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Usuario existente según los datos recibidos como parámetro
     * @param idDocente
     * @param idAgente
     * @return 
     */
    public Usuario getExistente(Long idDocente, Long idAgente){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.docente.id = :idDocente "
                + "OR us.agente.id = :idAgente";
        Query q = em.createQuery(queryString)
                .setParameter("idDocente", idDocente)
                .setParameter("idAgente", idAgente);
        return (Usuario)q.getSingleResult();
    }   
    
    /**
     * Método que devuelve todas los Usuarios habilitados
     * @return 
     */
    public List<Usuario> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.admin.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Usuarios deshabilitados
     * @return 
     */
    public List<Usuario> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }          
}
