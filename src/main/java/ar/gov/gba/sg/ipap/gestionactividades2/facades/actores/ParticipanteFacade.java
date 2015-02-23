/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
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
public class ParticipanteFacade extends AbstractFacade<Participante> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParticipanteFacade() {
        super(Participante.class);
    }
    
    /**
     * Método que verifica si el Participante puede ser eliminado
     * @param id: Id del Participante que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT clase.id FROM Calse clase "
                + "INNER JOIN clase.participantes part "
                + "WHERE part.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Participante con este nombre ya ingresado
     * @param agente
     * @param actImp
     * @return 
     */
    public boolean noExiste(Agente agente, ActividadImplementada actImp){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.agente = :agente "
                + "AND part.actividad = :actImp";
        Query q = em.createQuery(queryString)
                .setParameter("agente", agente)
                .setParameter("actImp", actImp);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene un Participante existente según los datos recibidos como parámetro
     * @param agente
     * @param actImp
     * @return 
     */
    public Participante getExistente(Agente agente, ActividadImplementada actImp){
        List<Participante> lPart;
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.agente = :agente "
                + "AND part.actividad = :actImp";
        Query q = em.createQuery(queryString)
                .setParameter("agente", agente)
                .setParameter("actImp", actImp);
        lPart = q.getResultList();
        if(!lPart.isEmpty()){
            return lPart.get(0);
        }else{
            return null;
        }
    }    
    
    /**
     * Método que devuelve todas los Participantes autorizados
     * @return 
     */
    public List<Participante> getAutorizados(){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.estado.nombre = 'Inscripto' "
                + "AND part.admin.habilitado = true "
                + "AND part.actividad.fechaFin >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Participantes autorizados para la interfase de Coordinador
     * @param us: Coordinador del curso
     * @return 
     */
    public List<Participante> getAutorizadosXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.estado.nombre = 'Inscripto' "
                + "AND part.admin.habilitado = true "
                + "AND part.actividad.fechaFin >= CURRENT_DATE "
                + "AND part.actividad.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Participantes provisorios
     * @return 
     */
    public List<Participante> getProvisiorios(){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.estado.nombre = 'Provisorio' "
                + "AND part.admin.habilitado = true "
                + "AND part.actividad.fechaFin >= CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 
    
    /**
     * Método que devuelve todas los Participantes provisorios para la interfase de Coordinador
     * @param us: Coordinador del curso
     * @return 
     */
    public List<Participante> getProvisioriosXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.estado.nombre = 'Provisorio' "
                + "AND part.admin.habilitado = true "
                + "AND part.actividad.fechaFin >= CURRENT_DATE "
                + "AND part.actividad.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    } 
    
    /**
     * Método que devuelve todas los Participantes deshabilitados
     * @return 
     */
    public List<Participante> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.admin.habilitado = false";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      
    
    /**
     * Método que devuelve todas los Participantes deshabilitados para la interfase de Coordinador
     * @param us: Coordinador del curso
     * @return 
     */
    public List<Participante> getDeshabilitadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.admin.habilitado = false "
                + "AND part.actividad.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }      
    
    /**
     * Método que devuelve los Participantes de Actividades vencidas
     * @return 
     */
    public List<Participante> getVencidos(){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.admin.habilitado = true "
                + "AND part.actividad.fechaFin < CURRENT_DATE";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 
    
    /**
     * Método que devuelve los Participantes de Actividades vencidas para la interfase de Coordinador
     * @param us: Coordinador del curso
     * @return 
     */
    public List<Participante> getVencidosXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.admin.habilitado = true "
                + "AND part.actividad.fechaFin < CURRENT_DATE "
                + "AND part.actividad.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }     
    
    /**
     * Método que obtiene todos los participantes de un curso
     * @param curso
     * @return 
     */    
    public List<Participante> getParticipantesXCurso(ActividadImplementada curso){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.actividad = :curso "
                + "AND part.estado.nombre = 'Inscripto' "
                + "ORDER BY part.agente.persona.apellidos";
        Query q = em.createQuery(queryString)
                .setParameter("curso", curso);
        return q.getResultList();
    }
    
    /**
     * Método que obtiene las inscripciones vigentes por Referente
     * @param referente
     * @return 
     */
    public List<Participante> getVigentesXReferente(Agente referente){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.agente.referente = :referente "
                + "AND part.actividad.fechaFin >= CURRENT_DATE";
        Query q = em.createQuery(queryString)
                .setParameter("referente", referente);
        return q.getResultList();
    }
    
    /**
     * Método que obtiene las inscripciones vencidos por Referente
     * @param referente
     * @return 
     */
    public List<Participante> getVencidosXReferente(Agente referente){
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.agente.referente = :referente "
                + "AND part.actividad.fechaFin < CURRENT_DATE";
        Query q = em.createQuery(queryString)
                .setParameter("referente", referente);
        return q.getResultList();
    }    
}
