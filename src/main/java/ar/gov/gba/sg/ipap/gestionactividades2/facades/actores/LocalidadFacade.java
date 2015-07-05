/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Localidad;
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
public class LocalidadFacade extends AbstractFacade<Localidad> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LocalidadFacade() {
        super(Localidad.class);
    }
    
    @Override
    public List<Localidad> findAll(){
        em = getEntityManager();
        String queryString = "SELECT loc FROM Localidad loc "
                + "ORDER BY loc.nombre, loc.departamento";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      
    
    /**
     * Método que devuelve todos la Localidades que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Localidad> getXString(String stringParam){
        em = getEntityManager();
        List<Localidad> result;
        String queryString = "SELECT loc FROM Localidad loc "
                + "WHERE loc.nombre LIKE :sParam OR loc.codigoPostal LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }    
    
    /**
     * Método que verifica si la Localidad puede ser eliminado
     * @param id: Id de la Localidad que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT per.localidad FROM Persona per "
                + "WHERE per.localidad.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
   /**
     * Método uitilizado para verificar que la localidad no exista ya
     * @param nombre
     * @param departamento
     * @return 
     */
    public boolean noExiste(String nombre, String departamento){
        em = getEntityManager();
        String queryString = "SELECT loc FROM Localidad loc "
                + "WHERE loc.nombre = :nombre "
                + "AND loc.departamento = :departamento";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("departamento", departamento);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que retorna una localidad con el nombre y departamento recibido como parámetros
     * @param nombre
     * @param departamento
     * @return 
     */
    public Localidad getExistente(String nombre, String departamento){
        List<Localidad> lLoc;
        em = getEntityManager();
        String queryString = "SELECT loc FROM Localidad loc "
                + "WHERE loc.nombre = :nombre "
                + "AND loc.departamento = :departamento";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("departamento", departamento);
        lLoc = q.getResultList();
        if(!lLoc.isEmpty()){
            return lLoc.get(0);
        }else{
            return null;
        }
    }
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT loc.nombre FROM Localidad loc";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }     
}
