/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Persona;
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
public class PersonaFacade extends AbstractFacade<Persona> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonaFacade() {
        super(Persona.class);
    }     
    
    /**
     * Método que verifica si la Personas puede ser eliminada
     * @param id: Id de la Persona que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.nivelIpap FROM Agente ag "
                + "WHERE ag.persona.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista una Persona con el tipo y número de documento recibidos como parámetros
     * @param idTipoDoc: id del tipo de documento
     * @param numDoc: número del documento
     * @return 
     */
    public boolean noExiste(Long idTipoDoc, int numDoc){
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipoDocumento.id = :idTipoDoc "
                + "AND per.documento = :numDoc";
        Query q = em.createQuery(queryString)
                .setParameter("idTipoDoc", idTipoDoc)
                .setParameter("numDoc", numDoc);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Título existente según los datos recibidos como parámetro
     * @param idTipoDoc: id del tipo de documento
     * @param numDoc: número del documento
     * @return 
     */
    public Persona getExistente(Long idTipoDoc, int numDoc){
        List<Persona> lPer;
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.tipoDocumento.id = :idTipoDoc "
                + "AND per.documento = :numDoc";
        Query q = em.createQuery(queryString)
                .setParameter("idTipoDoc", idTipoDoc)
                .setParameter("numDoc", numDoc);
        lPer = q.getResultList();
        if(!lPer.isEmpty()){
            return lPer.get(0);
        }else{
            return null;
        }
    }   
    
    /**
     * Método que devuelve todas las Personas habilitadas
     * @return 
     */
    public List<Persona> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.admin.habilitado = true "
                + "ORDER BY per.apellidos, per.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas las Personas deshabilitadas
     * @return 
     */
    public List<Persona> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT per FROM Persona per "
                + "WHERE per.admin.habilitado = false "
                + "ORDER BY per.apellidos, per.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
}
