/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

/**
 *
 * @author neche
 */
public class InfJaExistenteException extends RuntimeException{
    public InfJaExistenteException(String inf){
        super(inf + " ja existente.");
    }
}
