/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
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
public class SituacionRevistaFacade extends AbstractFacade<SituacionRevista> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SituacionRevistaFacade() {
        super(SituacionRevista.class);
    }
    
    @Override
    public List<SituacionRevista> findAll(){
        em = getEntityManager();
        String queryString = "SELECT sitRev FROM SituacionRevista sitRev "
                + "ORDER BY sitRev.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
 
    /**
     * Método que devuelve todas las Situaciones de Revista que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<SituacionRevista> getXString(String stringParam){
        em = getEntityManager();
        List<SituacionRevista> result;
        String queryString = "SELECT sitRev FROM SituacionRevista sitRev "
                + "WHERE sitRev.nombre LIKE :sParam";
        Query q = em.createQuery(queryString)
                .setParameter("sParam", "%" + stringParam + "%");
        result = q.getResultList();
        return result;
    }     
    
    /**
     * Método que verifica si la Situación de Revista puede ser eliminado
     * @param id: Id del Tipo de Capacitación que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.situacionRevista FROM Agente ag "
                + "WHERE ag.situacionRevista.id = :id";
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
        String queryString = "SELECT sitRev FROM SituacionRevista sitRev WHERE sitRev.nombre = :unique";
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
        String queryString = "SELECT sitRev.nombre FROM SituacionRevista sitRev";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
}
