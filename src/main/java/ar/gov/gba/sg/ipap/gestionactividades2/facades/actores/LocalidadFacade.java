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
    
    /**
     * Método que devuelve todos la Localidades que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Localidad> getXString(String stringParam){
        em = getEntityManager();
        List<Localidad> result;
        String queryString = "SELECT * FROM localidad WHERE nombre LIKE '%" + stringParam + "%' OR codigopostal LIKE '%" + stringParam + "%'";
        Query q = em.createNativeQuery(queryString, Localidad.class);
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
        String queryString = "SELECT * FROM persona WHERE localidad_id = " + id;
        Query q = em.createNativeQuery(queryString, Localidad.class);
        return q.getResultList().isEmpty();
    } 
    
   /**
     * Método uitilizado para verificar que el campo unique que se desea insertar no exista ya
     * @param unique: string con el valor que se desea verificar
     * @return 
     */
    public boolean noExiste(String unique){
        em = getEntityManager();
        String queryString = "SELECT * FROM localidad WHERE nombre = '" + unique + "'";
        Query q = em.createNativeQuery(queryString, Localidad.class);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT nombre FROM localidad";
        Query q = em.createNativeQuery(queryString);
        return q.getResultList();
    }     
}
