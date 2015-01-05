/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
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
public class ModalidadFacade extends AbstractFacade<Modalidad> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModalidadFacade() {
        super(Modalidad.class);
    }
    
    /**
     * Método que devuelve todos los Tipos de capacitación que contienen la cadena recibida como parámetro 
     * dentro de alguno de sus campos string, en este caso el nombre.
     * @param stringParam: cadena que buscará en todos los campos de tipo varchar de la tabla correspondiente
     * @return: El conjunto de resultados provenientes de la búsqueda. 
     */      
    public List<Modalidad> getXString(String stringParam){
        em = getEntityManager();
        List<Modalidad> result;
        String queryString = "SELECT * FROM modalidad WHERE nombre LIKE '%" + stringParam + "%'";
        Query q = em.createNativeQuery(queryString, Modalidad.class);
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
        String queryString = "SELECT * FROM actividadplan WHERE modalidad_id = " + id;
        Query q = em.createNativeQuery(queryString, Modalidad.class);
        return q.getResultList().isEmpty();
    }    
}
