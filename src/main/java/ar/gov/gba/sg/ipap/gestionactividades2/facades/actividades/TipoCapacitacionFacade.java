/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoCapacitacion;
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
public class TipoCapacitacionFacade extends AbstractFacade<TipoCapacitacion> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoCapacitacionFacade() {
        super(TipoCapacitacion.class);
    }
    
    /**
     * Método que devuelve todos los Tipos de capacitación que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<TipoCapacitacion> getXString(String stringParam){
        em = getEntityManager();
        List<TipoCapacitacion> result;
        String queryString = "SELECT * FROM tipocapacitacion WHERE nombre LIKE '%" + stringParam + "%'";
        Query q = em.createNativeQuery(queryString, TipoCapacitacion.class);
        result = q.getResultList();
        //em.close();
        return result;
    }     
}
