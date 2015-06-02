/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
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
public class TipoOrganismoFacade extends AbstractFacade<TipoOrganismo> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoOrganismoFacade() {
        super(TipoOrganismo.class);
    }
    
    @Override
    public List<TipoOrganismo> findAll(){
        em = getEntityManager();
        String queryString = "SELECT tipo FROM TipoOrganismo tipo "
                + "ORDER BY tipo.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
 
    /**
     * Método que devuelve todos los Tipos de capacitación que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<TipoOrganismo> getXString(String stringParam){
        em = getEntityManager();
        List<TipoOrganismo> result;
        String queryString = "SELECT tipo FROM TipoOrganismo tipo "
                + "WHERE tipo.nombre LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }     
    
    /**
     * Método que verifica si el Tipo de Capacitación puede ser eliminado
     * @param id: Id del Tipo de Capacitación que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT org.tipoOrganismo FROM Organismo org "
                + "WHERE org.tipoOrganismo.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    }  
    
    /**
     * Método uitilizado para verificar que el campo unique que se desea insertar no exista ya
     * @param unique: string con el valor que se desea verificar
     * @return 
     */
    public boolean noExiste(String unique){
        em = getEntityManager();
        String queryString = "SELECT tipo FROM TipoOrganismo tipo WHERE tipo.nombre = :unique";
        Query q = em.createQuery(queryString)
                .setParameter("unique", unique);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT tipo.nombre FROM TipoOrganismo tipo";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
}
