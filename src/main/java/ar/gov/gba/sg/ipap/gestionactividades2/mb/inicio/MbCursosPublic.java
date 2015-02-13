/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.mb.inicio;

import ar.gov.gba.sg.ipap.gestionactividades2.entities.actividades.ActividadImplementada;
import ar.gov.gba.sg.ipap.gestionactividades2.entities.actores.Usuario;
import ar.gov.gba.sg.ipap.gestionactividades2.facades.actividades.ActividadImplementadaFacade;
import ar.gov.gba.sg.ipap.gestionactividades2.mb.login.MbLogin;
import java.io.Serializable;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbCursosPublic implements Serializable{
    
    private DataModel items;
    private ActividadImplementada current;
    private String ambito;
    private MbLogin login;
    private Usuario usLogeado;     
    
    @EJB
    private ActividadImplementadaFacade cursoFacade;

    /**
     * Creates a new instance of MbCursosPublic
     */
    public MbCursosPublic() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(true);
        Enumeration enume = session.getAttributeNames();
        while(enume.hasMoreElements()){
            System.out.println(enume.nextElement());
        }
        
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        usLogeado = login.getUsLogeado();
        if(usLogeado.getRol().getNombre().equals("Administrador")
                || usLogeado.getRol().getNombre().equals("Coordinador")
                || usLogeado.getRol().getNombre().equals("Supervisor")){
            ambito = "admin";
        }else{
            ambito = "externo";
        }
    }
    
    
    
    /**
     * @return el listado de entidades a mostrar en el list
     */
    public DataModel getItems(){
        items = new ListDataModel(getFacade().getHabilitadas());
        return items;
    }
    
    
    /*********************
     ** geters y seters **
     *********************/
    public String getAmbito() {
        return ambito;
    }

    public MbLogin getLogin() {
        return login;
    }

    public void setLogin(MbLogin login) {
        this.login = login;
    }

    public Usuario getUsLogeado() {
        return usLogeado;
    }

    public void setUsLogeado(Usuario usLogeado) {
        this.usLogeado = usLogeado;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
    
    
    /*********************
    ** Métodos privados **
    **********************/
    private ActividadImplementadaFacade getFacade() {
        return cursoFacade;
    }      
}
