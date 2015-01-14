/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gov.gba.sg.ipap.gestionactividades2.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que encapsula edad en años, meses y días
 * @author rincostante
 */
public class Edad {
    private int year; 
    private int month; 
    private int day; 

    public Edad(){
        
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
    
    /**
     * Método privado que calcula la edad de la persona
     * @param fechaNac: fecha de nacimiento a partir de la cual se calcula la edad
     * @return 
     */
    public Edad calcularEdad(Date fechaNac){
        Date d = new Date(); 

        SimpleDateFormat sdfDia = new SimpleDateFormat("dd"); 
        SimpleDateFormat sdfMes = new SimpleDateFormat("MM"); 
        SimpleDateFormat sdfAño = new SimpleDateFormat("yyyy"); 

        int a = Integer.parseInt(sdfAño.format(d)) - Integer.parseInt(sdfAño.format(fechaNac)); 
        int b = Integer.parseInt(sdfMes.format(d)) - Integer.parseInt(sdfMes.format(fechaNac)); 
        int c = Integer.parseInt(sdfDia.format(d)) - Integer.parseInt(sdfDia.format(fechaNac)); 
        
        if (b < 0){
            a = a - 1; 
            b = 12 + b; 
        }
        
        if (c < 0){
            b = b - 1; 
            switch (Integer.parseInt(sdfMes.format(d))){
                case 2: 
                    int anio = Integer.parseInt(sdfAño.format(d)); 
                    if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))){
                        c = 29 + c; 
                    }else{
                        c = 28 + c; 
                    }
                break;
                case 4:
                case 6: 
                case 9: 
                case 10:
                case 11: c = 30 + c; 
                break;
                case 1:
                case 3: 
                case 5: 
                case 7:
                case 8:
                case 12: c = 31 + c; 
                break;
            }
        }
        Edad edad = new Edad(); 
        edad.setDay(c); 
        edad.setMonth(b); 
        edad.setYear(a); 
        
        return edad;
    }    
}
