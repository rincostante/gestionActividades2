/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
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
public class TituloFacade extends AbstractFacade<Titulo> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TituloFacade() {
        super(Titulo.class);
    }
    
   /**
     * Método que devuelve todos la Títulos que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Titulo> getXString(String stringParam){
        em = getEntityManager();
        List<Titulo> result;
        String queryString = "SELECT tit FROM Titulo tit "
                + "WHERE tit.nombre LIKE :sParam "
                + "OR tit.expedidoPor LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }    
    
    /**
     * Método que verifica si la Título puede ser eliminado
     * @param id: Id de la Titulo que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT tit.nombre FROM Titulo tit "
                + "INNER JOIN tit.agentes ag "
                + "INNER JOIN tit.docentes doc "
                + "WHERE tit.id = :id ";
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
        String queryString = "SELECT tit.nombre FROM Titulo tit";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método para validad que no exista un par estudio/estado ya ingresado
     * @param nombre: nombre del estudio
     * @param expedidoPor: autoridad que expidió el título
     * @return 
     */
    public boolean noExiste(String nombre, String expedidoPor){
        em = getEntityManager();
        String queryString = "SELECT tit FROM Titulo tit "
                + "WHERE tit.nombre = :nombre "
                + "AND tit.epedidoPor = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", expedidoPor);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Título existente según los datos recibidos como parámetro
     * @param nombre: nombre del título
     * @param expedidoPor: autoridad que expidió el título
     * @return 
     */
    public Titulo getExistente(String nombre, String expedidoPor){
        em = getEntityManager();
        String queryString = "SELECT tit FROM Titulo tit "
                + "WHERE tit.nombre = :nombre "
                + "AND tit.epedidoPor = :estado";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("estado", expedidoPor);
        return (Titulo)q.getSingleResult();
    }
}
