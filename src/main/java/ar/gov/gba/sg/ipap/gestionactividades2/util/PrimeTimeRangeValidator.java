/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.util;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validador entre rangos horarios
 * @author Administrador
 */
@FacesValidator("primeTimeRangeValidator")
public class PrimeTimeRangeValidator implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        
        //Leave the null handling of startDate to required="true"
        Object startTimeValue = component.getAttributes().get("hinicial");    
        if (startTimeValue==null) {
            return;
        }
        
        Date startTime = (Date)startTimeValue;
        Date endTime = (Date)value;
        if (endTime.before(startTime) || endTime.equals(startTime)){
            FacesMessage message = new FacesMessage("La hora final no puede ser anterior o igual a la hora inicial.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
    
}
