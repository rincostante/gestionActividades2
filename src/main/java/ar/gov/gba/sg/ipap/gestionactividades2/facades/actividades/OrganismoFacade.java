/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
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
public class OrganismoFacade extends AbstractFacade<Organismo> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrganismoFacade() {
        super(Organismo.class);
    }
    
    /**
     * Método que verifica si la Título puede ser eliminado
     * @param id: Id de la Organismo que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT org.nombre FROM Organismo org "
                + "INNER JOIN org.actividadesImplementadas actImp "
                + "INNER JOIN org.actividadesPlan actPlan "
                + "WHERE org.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un par estudio/estado ya ingresado
     * @param nombre: nombre del estudio
     * @param idTipoOrg: id del tipo de organismo cuya existencia se está verificando
     * @return 
     */
    public boolean noExiste(String nombre, Long idTipoOrg){
        em = getEntityManager();
        String queryString = "SELECT org FROM Organismo org "
                + "WHERE org.nombre = :nombre "
                + "AND org.tipoOrganismo.id = :idTipoOrg";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("idTipoOrg", idTipoOrg);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Título existente según los datos recibidos como parámetro
     * @param nombre: nombre del título
     * @param idTipoOrg: id del tipo de organismo cuya existencia se está verificando
     * @return 
     */
    public Organismo getExistente(String nombre, Long idTipoOrg){
        em = getEntityManager();
        String queryString = "SELECT org FROM Organismo org "
                + "WHERE org.nombre = :nombre "
                + "AND org.tipoOrganismo.id = :idTipoOrg";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("idTipoOrg", idTipoOrg);
        return (Organismo)q.getSingleResult();
    }    
    
    /**
     * Método que devuelve todas los Organismos habilitadas
     * @return 
     */
    public List<Organismo> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT org FROM Organismo org "
                + "WHERE org.admin.habilitado = true "
                + "ORDER BY org.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Organismos deshabilitadas
     * @return 
     */
    public List<Organismo> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT org FROM Organismo org "
                + "WHERE org.admin.habilitado = false "
                + "ORDER BY org.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }       
}
