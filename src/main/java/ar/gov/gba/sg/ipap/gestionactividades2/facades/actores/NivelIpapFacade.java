/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
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
public class NivelIpapFacade extends AbstractFacade<NivelIpap> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NivelIpapFacade() {
        super(NivelIpap.class);
    }
    
    @Override
    public List<NivelIpap> findAll(){
        em = getEntityManager();
        String queryString = "SELECT nivIpap FROM NivelIpap nivIpap "
                + "ORDER BY nivIpap.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
    
   /**
     * Método que devuelve todos la Niveles Ipap que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<NivelIpap> getXString(String stringParam){
        em = getEntityManager();
        List<NivelIpap> result;
        String queryString = "SELECT nivIpap FROM NivelIpap nivIpap "
                + "WHERE nivIpap.nombre LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }    
    
    /**
     * Método que verifica si la Nivel Ipap puede ser eliminado
     * @param id: Id de la NivelIpap que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.nivelIpap FROM Agente ag "
                + "WHERE ag.nivelIpap.id = :id";
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
        String queryString = "SELECT nivIpap.nombre FROM NivelIpap nivIpap";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método para validad que no exista un par estudio/estado ya ingresado
     * @param nombre: nombre del estudio
     * @param estado: niver alcanzado
     * @return 
     */
    public boolean noExiste(String nombre, String estado){
        em = getEntityManager();
        String queryString = "SELECT nivIpap FROM NivelIpap nivIpap "
                + "WHERE nivIpap.nombre = :nombre "
                + "AND nivIpap.estado = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", estado);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Título existente según los datos recibidos como parámetro
     * @param nombre: nombre del título
     * @param estado: niver alcanzado
     * @return 
     */
    public NivelIpap getExistente(String nombre, String estado){
        em = getEntityManager();
        String queryString = "SELECT nivIpap FROM NivelIpap nivIpap "
                + "WHERE nivIpap.nombre = :nombre "
                + "AND nivIpap.estado = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", estado);
        return (NivelIpap)q.getSingleResult();
    }    
}
