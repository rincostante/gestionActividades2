/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Orientacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rincostante
 */
@Stateless
public class OrientacionFacade extends AbstractFacade<Orientacion> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrientacionFacade() {
        super(Orientacion.class);
    }
 
    @Override
    public List<Orientacion> findAll(){
        em = getEntityManager();
        String queryString = "SELECT orien FROM Orientacion orien "
                + "ORDER BY orien.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método que verifica si la Orientación puede ser eliminada
     * @param id: Id de la Orientación que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT subProg.orientacion FROM SubPrograma subProg "
                + "WHERE subProg.orientacion.id = :id";
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
        String queryString = "SELECT orien FROM Orientacion orien WHERE orien.nombre = :unique";
        Query q = em.createQuery(queryString)
                .setParameter("unique", unique);
        return q.getResultList().isEmpty();
    }      
    
    /**
     * Método que retorna, si existe, el objeto cuyo campo unique se recibe como parámetro
     * @param unique
     * @return 
     */
    public Orientacion getExistente(String unique){
        em = getEntityManager();
        List<Orientacion> orients;
        String queryString = "SELECT orien FROM Orientacion orien WHERE orien.nombre = :unique";
        Query q = em.createQuery(queryString)
                .setParameter("unique", unique);
        orients = q.getResultList();
        if(orients.isEmpty()){
            return null;
        }else{
            return orients.get(0);
        }
    }      
}
