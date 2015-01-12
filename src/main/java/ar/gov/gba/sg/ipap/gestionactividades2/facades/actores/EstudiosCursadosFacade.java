/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
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
public class EstudiosCursadosFacade extends AbstractFacade<EstudiosCursados> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstudiosCursadosFacade() {
        super(EstudiosCursados.class);
    }
    
    /**
     * Método que devuelve todos la Estudios Cursadoses que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<EstudiosCursados> getXString(String stringParam){
        em = getEntityManager();
        List<EstudiosCursados> result;
        String queryString = "SELECT est FROM EstudiosCursados est "
                + "WHERE est.nombre LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }    
    
    /**
     * Método que verifica si la Estudios Cursados puede ser eliminado
     * @param id: Id de la EstudiosCursados que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.estudiosCursados FROM Agente ag "
                + "WHERE ag.estudiosCursados.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT est.nombre FROM EstudiosCursados est";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método para validad que no exista un par estudio/estado ya ingresado
     * @param nombre: nombre del estudio
     * @param estado: nivel alcanzado
     * @return 
     */
    public boolean noExiste(String nombre, String estado){
        em = getEntityManager();
        String queryString = "SELECT estCur FROM EstudiosCursados estCur "
                + "WHERE estCur.nombre = :nombre "
                + "AND estCur.estado = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", estado);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Título existente según los datos recibidos como parámetro
     * @param nombre: nombre del título
     * @param estado: nivel alcanzado
     * @return 
     */
    public EstudiosCursados getExistente(String nombre, String estado){
        em = getEntityManager();
        String queryString = "SELECT estCur FROM EstudiosCursados estCur "
                + "WHERE estCur.nombre = :nombre "
                + "AND estCur.estado = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", estado);
        return (EstudiosCursados)q.getSingleResult();
    }    
}
