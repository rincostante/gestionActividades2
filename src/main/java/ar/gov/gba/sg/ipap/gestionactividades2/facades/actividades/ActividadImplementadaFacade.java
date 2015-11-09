/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadPlan;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Modalidad;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Programa;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Sede;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.SubPrograma;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoCapacitacion;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.TipoOrganismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Clase;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Docente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Participante;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
public class ActividadImplementadaFacade extends AbstractFacade<ActividadImplementada> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadImplementadaFacade() {
        super(ActividadImplementada.class);
    }
    
  /**
     * Método que verifica si la Actividad Implementada puede ser eliminada
     * @param id: Id de la ActividadImplementada que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT part.id FROM Participante part "
                + "INNER JOIN part.actividad actImp "
                + "WHERE actImp.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validar que no exista una Implementación de la misma Actividad para un mismo período en una misma sede
     * @param nombre
     * @param fechaInio
     * @param fechaFin
     * @param idSede
     * @param listOrgDest
     * @return 
     */
    public boolean noExiste(String nombre, Date fechaInio, Date fechaFin, Long idSede, List<Organismo> listOrgDest){
        List<ActividadImplementada> lProg;
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE (actImp.fechaInicio >= :fechaInio "
                + "AND actImp.fechaFin <= :fechaFin) "
                + "AND actImp.sede.id = :idSede "
                + "AND actImp.actividadPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("fechaInio", fechaInio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("idSede", idSede)
                .setParameter("nombre", nombre);
        lProg = q.getResultList();
        
        if(!lProg.isEmpty()){
            if(listOrgDest != null){
                return !lProg.get(0).getOrganismosDestinatarios().equals(listOrgDest);
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    
    /**
     * Método que obtiene una Actividad Implementada existente según los datos recibidos como parámetro
     * @param nombre
     * @param fechaInio
     * @param fechaFin
     * @param idSede
     * @param listOrgDest
     * @return 
     */
    public ActividadImplementada getExistente(String nombre, Date fechaInio, Date fechaFin, Long idSede, List<Organismo> listOrgDest){
        List<ActividadImplementada> lProg;
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE (actImp.fechaInicio >= :fechaInio "
                + "AND actImp.fechaFin <= :fechaFin) "
                + "AND actImp.sede.id = :idSede "
                + "AND actImp.actividadPlan.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("fechaInio", fechaInio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("idSede", idSede)
                .setParameter("nombre", nombre);
        lProg = q.getResultList();

        if(!lProg.isEmpty()){
            if(listOrgDest != null){
                if(lProg.get(0).getOrganismosDestinatarios().equals(listOrgDest)){
                    return lProg.get(0);
                }else{
                    return null;
                }
            }else{
                return lProg.get(0);
            }
        }else{
            return null;
        }
    }    
    
    /**
     * Método que devuelve todas los Actividades Implementadas habilitadas y vigentes
     * @return 
     */
    public List<ActividadImplementada> getHabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = false "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Actividades Implementadas habilitadas y vigentes para la interfase de coordinador
     * @param us: Agente que actúa como coordinador del curso
     * @return 
     */
    public List<ActividadImplementada> getHabilitadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = false "
                + "AND actImp.coordinador = :us "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Actividades Implementadas deshabilitadas
     * @return 
     */
    public List<ActividadImplementada> getDeshabilitadas(){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = false "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }        
    
    /**
     * Método que devuelve todas los Actividades Implementadas deshabilitadas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getDeshabilitadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = false "
                + "AND actImp.coordinador = :us "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }         
    
    /**
     * Método que devuelve los Actividades Implementadas finalizadas
     * @return 
     */
    public List<ActividadImplementada> getFinalizadas(){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.fechaFin < CURRENT_DATE "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }   
    
    /**
     * Método que devuelve los Actividades Implementadas finalizadas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getFinalizadasXCoor(Usuario us){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.fechaFin < CURRENT_DATE "
                + "AND actImp.coordinador = :us "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }     
    
    /**
     * Método que devuelve los Actividades Implementadas suspendidas
     * @return 
     */
    public List<ActividadImplementada> getSuspendidas(){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }     
    
    /**
     * Método que devuelve los Actividades Implementadas suspendidas para la interfase de coordinador
     * @param us
     * @return 
     */
    public List<ActividadImplementada> getSuspendidasXCoor(Usuario us){
        
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.admin.habilitado = true "
                + "AND actImp.suspendido = true "
                + "AND actImp.coordinador = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }    
    
    /**
     * Método que devuleve las Actividades implementadas por participante
     * @param participante
     * @return 
     */
    public List<ActividadImplementada> getHabilitadasXPart(Agente participante){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "INNER JOIN actImp.participantes part "
                + "WHERE part.agente = :participante "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("participante", participante);
        return q.getResultList();
    }
    
    /**
     * Método que devuleve las Actividades implementadas por docente
     * @param docente
     * @return 
     */   
    public List<ActividadImplementada> getHabilitadasXDocente(Docente docente){
        em = getEntityManager();
        String queryString = "SELECT actImp FROM ActividadImplementada actImp "
                + "WHERE actImp.docente = :docente "
                + "ORDER BY actImp.actividadPlan.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("docente", docente);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve las Clases tomadas por un participante de una Actividad implementada
     * @param act
     * @param participante
     * @return 
     */
    public List<Clase> getClasesXActPart(ActividadImplementada act, Agente participante){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "INNER JOIN clase.participantes part "
                + "WHERE part.agente = :participante "
                + "AND part.actividad = :act";
        Query q = em.createQuery(queryString)
                .setParameter("act", act)
                .setParameter("participante", participante);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve las Clases dictadas por un docente 
     * de una Actividad implementada que lo tenga como titular
     * @param act
     * @param docente
     * @return 
     */
    public List<Clase> getClasesXActDocente(ActividadImplementada act, Docente docente){
        em = getEntityManager();
        String queryString = "SELECT clase FROM Clase clase "
                + "INNER JOIN clase.participantes part "
                + "WHERE part.actividad = :act "
                + "AND clase.docente = :docente";
        Query q = em.createQuery(queryString)
                .setParameter("act", act)
                .setParameter("docente", docente);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve el estado de participación de de un agente en una Actividad implementada
     * @param act
     * @param agente
     * @return 
     */
    public Participante getParticipacion(ActividadImplementada act, Agente agente){
        List<Participante> listPart;
        em = getEntityManager();
        String queryString = "SELECT part FROM Participante part "
                + "WHERE part.agente = :agente "
                + "AND part.actividad = :act";
        Query q = em.createQuery(queryString)
                .setParameter("act", act)
                .setParameter("agente", agente);
        listPart = q.getResultList();
        if(!listPart.isEmpty()){
            return listPart.get(0);
        }else{
            return null;
        }
    }
    
    /**
     * Método que devueve Agentes según los Organismos a los que pertenezcan
     * @param organismos
     * @return 
     */
    public List<Agente> getAgentesXOrganismos(List<Organismo> organismos){
        List<Agente> resultListTotal = new ArrayList<>();
        Collection<Agente> resultListParcial = new ArrayList<>();
        String queryString;
        Query q;
        em = getEntityManager();
        for(Organismo org : organismos){
            resultListParcial.clear();
            queryString = "SELECT ag FROM Agente ag "
                    + "WHERE ag.organismo = :org";
            q = em.createQuery(queryString)
                    .setParameter("org", org);
            resultListParcial = q.getResultList();
            resultListTotal.addAll(resultListParcial);
        }
        return resultListTotal;
    }    
    
    /**
     * Método para la selección de AD seegún parámetros de consulta
     * Primero pregunto por las fechas, si vienen nulas seteo incial 01/01/1900 y final hoy
     * De esta forma siempre habra un parámetro luego del WHERE para después poder incluir AND o OR según corresponda.
     * Para eso debo agregar los if correspondientes a cada parámetro.
     */
    public List<ActividadImplementada> getXConsulta(
        Programa programa,
        SubPrograma subprograma,
        ActividadPlan actForm,
        Organismo organismoSol,
        Sede sede,
        Modalidad modalidad,
        TipoCapacitacion tipoCap,
        Date fDespuesDe,
        Date fAntesDe
    ){    
        List<ActividadImplementada> resultListTotal = new ArrayList<>();
        em = getEntityManager();
        String queryString = "SELECT act FROM ActividadImplementada act WHERE ";  
        int largo = queryString.length();
        
        // armo la query en función de los valores recibidos
        if(programa != null && queryString.length() == 48){
            queryString = queryString + "act.subprograma.programa = :programa ";
        }else if(programa != null && queryString.length() > 48){
            queryString = queryString + "AND act.subprograma.programa = :programa ";
        }
        
        if(subprograma != null && queryString.length() == 48){
            queryString = queryString + "act.subprograma = :subprograma ";
        }else if(subprograma != null && queryString.length() > 48){
            queryString = queryString + "AND act.subprograma = :subprograma ";
        }
        
        Query q = em.createQuery(queryString)
                .setParameter("programa", programa)
                .setParameter("subprograma", subprograma);
        resultListTotal = q.getResultList();
        
        return resultListTotal;
    }
}
