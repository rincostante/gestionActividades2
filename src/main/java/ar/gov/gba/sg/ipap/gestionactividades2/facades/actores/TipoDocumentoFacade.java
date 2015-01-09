/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
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
public class TipoDocumentoFacade extends AbstractFacade<TipoDocumento> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDocumentoFacade() {
        super(TipoDocumento.class);
    }
    
    /**
     * Método que devuelve todos los Tipos de Documento que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<TipoDocumento> getXString(String stringParam){
        em = getEntityManager();
        List<TipoDocumento> result;
        String queryString = "SELECT * FROM tipodocumento WHERE nombre LIKE '%" + stringParam + "%' OR sigla LIKE '%" + stringParam + "%'";
        Query q = em.createNativeQuery(queryString, TipoDocumento.class);
        result = q.getResultList();
        return result;
    }    
    
    /**
     * Método que verifica si la Tipo de Documento puede ser eliminado
     * @param id: Id de la TipoDocumento que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT * FROM persona WHERE tipodocumento_id = " + id;
        Query q = em.createNativeQuery(queryString, TipoDocumento.class);
        return q.getResultList().isEmpty();
    } 
    
   /**
     * Método uitilizado para verificar que el campo unique que se desea insertar no exista ya
     * @param unique: string con el valor que se desea verificar
     * @return 
     */
    public boolean noExiste(String unique){
        em = getEntityManager();
        String queryString = "SELECT * FROM tipodocumento WHERE nombre = '" + unique + "'";
        Query q = em.createNativeQuery(queryString, TipoDocumento.class);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Metodo para el autocompletado de la búsqueda por nombre
     * @return 
     */
    public List<String> getNombres(){
        em = getEntityManager();
        String queryString = "SELECT nombre FROM tipodocumento";
        Query q = em.createNativeQuery(queryString);
        return q.getResultList();
    }      
}
