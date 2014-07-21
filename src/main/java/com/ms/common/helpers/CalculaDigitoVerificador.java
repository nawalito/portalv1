/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ms.common.helpers;

/**
 *
 * @author Noe Martinez
 * gpmarsan@gmail.com
 * 04/07/2014
 */
public class CalculaDigitoVerificador {
    public int modulo10(String num) {
        int divisor=10;
        int minuendo = 10;
        int constante1 = 00;
        int suma = 0;
        int resto = 0;
        int dv = 0;
        String[] numeros = new String[num.length()+1];
        int multiplicador = 2;
        String aux;
        String aux2;
        String aux3;
        
        for (int i = num.length(); i > 0; i--) {
            //Multiplicar multiplicar de derecha a izquierda
            if(multiplicador%2 == 0){
                //Numero par multiplicar por 2 de acuerdo a la regla
                numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*2);
                multiplicador = 1;
            }else{
                //Numero impar multiplicar por 1 de acuerdo a la regla
                numeros[i] = String.valueOf(Integer.valueOf(num.substring(i-1,i))*1);
                multiplicador = 2;
            }
        }
        
        // Realiza la suma de cada resultado de la multiplicacion del paso anterior
        for(int i = (numeros.length-1); i > 0; i--){
            aux = String.valueOf(Integer.valueOf(numeros[i]));
            if(aux.length()>1){
                aux2 = aux.substring(0,aux.length()-1);
                aux3 = aux.substring(aux.length()-1,aux.length());
                numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));
            }else{
                numeros[i] = aux;
            }
        }
        
        //Realiza la suma de cada elemento del array y calcula el digito verificador en base 10 de acuerdo a la regla  
        for(int i = numeros.length; i > 0 ; i--){
            if(numeros[i-1] != null){
                suma += Integer.valueOf(numeros[i-1]);
            }
        }  
        resto = suma%divisor;
        dv = minuendo - resto;
        
        dv = dv + constante1;
        
        if(dv==10) dv=0;
        
        return dv;
    }
}
