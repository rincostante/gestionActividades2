/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.facades.actores;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.Organismo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Agente;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Cargo;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.EstudiosCursados;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.NivelIpap;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.SituacionRevista;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.TipoDocumento;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Titulo;
import java.util.ArrayList;
import java.util.Calendar;
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
public class AgenteFacade extends AbstractFacade<Agente> {
    @PersistenceContext(unitName = "Ipap-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgenteFacade() {
        super(Agente.class);
    }
    
   /**
     * Método que verifica si el Agente puede ser eliminado
     * @param id: Id de la Agente que se desea verificar
     * @return
     */
    public boolean getUtilizado(Long id){
        em = getEntityManager();
        String queryString = "SELECT ag.id FROM Agente ag "
                + "INNER JOIN ag.docente doc "
                + "INNER JOIN ag.participaciones part "
                + "WHERE ag.id = :id ";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    } 
    
    /**
     * Método para validad que no exista un Agente con la Persona cuyo id es recibido como parámetro
     * @param idPersona
     * @return 
     */
    public boolean noExiste(Long idPersona){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.persona.id = :idPersona";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona);
        return q.getResultList().isEmpty();
    }    
    
    /**
     * Método que obtiene un Agente existente según los datos recibidos como parámetro
     * @param idPersona
     * @return 
     */
    public Agente getExistente(Long idPersona){
        List<Agente> lAg;
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.persona.id = :idPersona";
        Query q = em.createQuery(queryString)
                .setParameter("idPersona", idPersona);
        lAg = q.getResultList();
        if(!lAg.isEmpty()){
            return lAg.get(0);
        }else{
            return null;
        }
    }   
    
    /**
     * Método que devuelve todas los Agentes habilitados
     * @return 
     */
    public List<Agente> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.admin.habilitado = true "
                + "ORDER BY ag.persona.apellidos, ag.persona.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve todas los Agentes deshabilitados
     * @return 
     */
    public List<Agente> getDeshabilitados(){
        em = getEntityManager();
        String queryString = "SELECT ag FROM Agente ag "
                + "WHERE ag.admin.habilitado = false "
                + "ORDER BY ag.persona.apellidos, ag.persona.nombres";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
    
    /**
     * Método para la selección de Agentes participantes seegún parámetros de consulta
     * Primero pregunto por la fecha inicial de capacitación, si viene nula seteo 01/01/1900
     * Según los parámetros restantes sean nulos o no, los guardo en el listado respectivos
     * para luego asignarlos a la querry
     * @param tipoDocumento
     * @param documento
     * @param organismo: Organismo en el que se desempeña el Agente
     * @param situacionRevista: Situación de revista del Agente
     * @param cargo: Cargo con el que se desempeña el Agente
     * @param fechaInicioActividades: Fecha de inicio de actividades del Agente
     * @param nivelIpap: Nivel alcanzado en el Instituto por el Agente
     * @param estudiosCursados: Estudios regulares cursados por el Agente
     * @param titulo: Título alcanzado por el Agente
     * @param referente: Condición ser o no referente en su Organismo
     * @param fechaInicio: Fecha de inicio de la AD más antigua
     * @param fechaFin: Fecha de finalización de la AD más reciente
     * @return 
     */    
    public List<Agente> getXConsulta(
        TipoDocumento tipoDocumento,
        int documento,
        Organismo organismo,
        SituacionRevista situacionRevista,
        Cargo cargo,
        Date fechaInicioActividades,
        NivelIpap nivelIpap,
        EstudiosCursados estudiosCursados,
        Titulo titulo,
        boolean referente,
        Date fechaInicio,
        Date fechaFin
    ){   
        List<Agente> resultListTotal = new ArrayList<>();
        List<String> paramValor = new ArrayList<>();
        List<String> paramNulos = new ArrayList<>();
        String campoSimple;
        String operador;
        
        // Separo los parámetros nulos de los que vienen con valor
        if(tipoDocumento == null) paramNulos.add("persona.tipoDocumento"); else paramValor.add("persona.tipoDocumento");
        if(documento == 0) paramNulos.add("persona.documento"); else paramValor.add("persona.documento");
        
        
        if(organismo == null) paramNulos.add("organismo"); else paramValor.add("organismo");
        if(situacionRevista == null) paramNulos.add("situacionRevista"); else paramValor.add("situacionRevista");
        if(cargo == null) paramNulos.add("cargo"); else paramValor.add("cargo");
        if(fechaInicioActividades == null) paramNulos.add("fechaInicioActividades"); else paramValor.add("fechaInicioActividades");
        if(nivelIpap == null) paramNulos.add("nivelIpap"); else paramValor.add("nivelIpap");
        if(estudiosCursados == null) paramNulos.add("estudiosCursados"); else paramValor.add("estudiosCursados");
        if(titulo == null) paramNulos.add("titulo"); else paramValor.add("titulo");
        if(fechaFin == null) paramNulos.add("actividad.fechaFin"); else paramValor.add("actividad.fechaFin");    
        
        em = getEntityManager();
        String queryString = "SELECT DISTINCT(agente) FROM Agente agente "
                + "INNER JOIN agente.participaciones part "
                + "INNER JOIN part.clases cls "
                + "WHERE agente.esReferente = :referente ";  
        
        // valido la fecha de inicio, si viene nula seteo 01/01/1900
        if(fechaInicio == null){
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.set(1900, 1, 1);
            fechaInicio = tmpCal.getTime();
        }        
        
        // agrego fDespuesDe a la query
        queryString = queryString + "AND part.actividad.fechaInicio >= :fechaInicio ";        
        
        // agrego los campos que contienen valor, antes del útlimo le abro un paréntesis. 
        //Verifico los objetos de objetos y los operadores.
        for(String campo : paramValor){
            if(campo.equals("actividad.fechaFin")){
                campoSimple = "fechaFin"; 
                operador = " <= :";
                if(!campo.equals(paramValor.get(paramValor.size() - 1))) queryString = queryString + "AND part." + campo + operador + campoSimple + " ";
                else queryString = queryString + "AND (part." + campo + operador + campoSimple + " ";
            }else if(campo.equals("fechaInicioActividades")){
                campoSimple = campo;
                operador = " >= :";
                if(!campo.equals(paramValor.get(paramValor.size() - 1))) queryString = queryString + "AND agente." + campo + operador + campoSimple + " ";
                else queryString = queryString + "AND (agente." + campo + operador + campoSimple + " ";
            }else if(campo.equals("persona.tipoDocumento")){
                campoSimple = "tipoDocumento";
                operador = " = :";
                if(!campo.equals(paramValor.get(paramValor.size() - 1))) queryString = queryString + "AND agente." + campo + operador + campoSimple + " ";
                else queryString = queryString + "AND (agente." + campo + operador + campoSimple + " ";
            }else if(campo.equals("persona.documento")){
                campoSimple = "documento";
                operador = " = :";
                if(!campo.equals(paramValor.get(paramValor.size() - 1))) queryString = queryString + "AND agente." + campo + operador + campoSimple + " ";
                else queryString = queryString + "AND (agente." + campo + operador + campoSimple + " ";
            }
            else{
                campoSimple = campo;
                operador = " = :";
                if(!campo.equals(paramValor.get(paramValor.size() - 1))) queryString = queryString + "AND agente." + campo + operador + campoSimple + " ";
                else queryString = queryString + "AND (agente." + campo + operador + campoSimple + " ";
            }
        } 
        
        // agrego los campos que contienen nulos, al último lo completo cerrando el paréntesis
        for(String campo : paramNulos){
            if(campo.equals("actividad.fechaFin")){
                campoSimple = "fechaFin"; 
                if(!campo.equals(paramNulos.get(paramNulos.size() - 1))) queryString = queryString + "OR part." + campo + " = :" + campoSimple + " ";
                else queryString = queryString + "OR part." + campo + " = :" + campoSimple + ") ";     
            }else if(campo.equals("persona.tipoDocumento")){
                campoSimple = "tipoDocumento";
                if(!campo.equals(paramNulos.get(paramNulos.size() - 1))) queryString = queryString + "OR agente." + campo + " = :" + campoSimple + " ";
                else queryString = queryString + "OR agente." + campo + " = :" + campoSimple + ") ";               
            }else if(campo.equals("persona.documento")){
                campoSimple = "documento";
                if(!campo.equals(paramNulos.get(paramNulos.size() - 1))) queryString = queryString + "OR agente." + campo + " = :" + campoSimple + " ";
                else queryString = queryString + "OR agente." + campo + " = :" + campoSimple + ") "; 
            }
            else{
                campoSimple = campo;
                if(!campo.equals(paramNulos.get(paramNulos.size() - 1))) queryString = queryString + "OR agente." + campo + " = :" + campoSimple + " ";
                else queryString = queryString + "OR agente." + campo + " = :" + campoSimple + ") ";
            }
        }       
        
        // cierro la query ordenando por nombre "ORDER BY agente.apYNom"
        queryString = queryString + "ORDER BY agente.persona.apellidos, agente.persona.nombres";   
        
        // seteo los parámetros
        Query q = em.createQuery(queryString)
                .setParameter("tipoDocumento", tipoDocumento)
                .setParameter("documento", documento)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("organismo", organismo)
                .setParameter("situacionRevista", situacionRevista)
                .setParameter("cargo", cargo)
                .setParameter("fechaInicioActividades", fechaInicioActividades)
                .setParameter("nivelIpap", nivelIpap)
                .setParameter("estudiosCursados", estudiosCursados)
                .setParameter("titulo", titulo)
                .setParameter("referente", referente)
                .setParameter("fechaFin", fechaFin);
        resultListTotal = q.getResultList();
        
        return resultListTotal;        
    }            
}
