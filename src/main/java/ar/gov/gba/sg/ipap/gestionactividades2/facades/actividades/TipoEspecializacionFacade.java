/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoEspecializacion;
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
public class TipoEspecializacionFacade extends AbstractFacade<TipoEspecializacion> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoEspecializacionFacade() {
        super(TipoEspecializacion.class);
    }
    
    @Override
    public List<TipoEspecializacion> findAll(){
        em = getEntityManager();
        String queryString = "SELECT tipoEsp FROM TipoEspecializacion tipoEsp "
                + "ORDER BY tipoEsp.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }     
    
    /**
     * Método que verifica si el Tipo de Especializacion puede ser eliminado
     * @param id: Id del Tipo de Especializacion que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT subProg.tipoEspecializacion FROM SubPrograma subProg "
                + "WHERE subProg.tipoEspecializacion.id = :id";
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
        String queryString = "SELECT tipoEsp FROM TipoEspecializacion tipoEsp WHERE tipoEsp.nombre = :unique";
        Query q = em.createQuery(queryString)
                .setParameter("unique", unique);
        return q.getResultList().isEmpty();
    }  
    
    /**
     * Método que retorna, si existe, el objeto cuyo campo unique se recibe como parámetro
     * @param unique
     * @return 
     */
    public TipoEspecializacion getExistente(String unique){
        em = getEntityManager();
        List<TipoEspecializacion> tipoEsps;
        String queryString = "SELECT tipoEsp FROM TipoEspecializacion tipoEsp WHERE tipoEsp.nombre = :unique";
        Query q = em.createQuery(queryString)
                .setParameter("unique", unique);
        tipoEsps = q.getResultList();
        if(tipoEsps.isEmpty()){
            return null;
        }else{
            return tipoEsps.get(0);
        }
    }  
}
