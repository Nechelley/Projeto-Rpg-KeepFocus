package rpg;

import java.io.Serializable;
import java.util.Random;

/**
 * Classe para representar um dado que pode ter qualquer valor inteiro como numero de lados
 * 
 * @author Nechelley Alves
 */
public class Dado implements Serializable{
    //numero de faces do dado
    private int lado;
    
    //obj de Random
    private Random randomico;
    
    /**
     * Construtor da classe Dado, pede o numero de faces que tera o dado
     * 
     * @param quantidadeDeLados Int com o numero de faces que o dado terá
     */
    public Dado(int quantidadeDeLados){
        //defino lado como a quantidade de lados do dado
        lado = quantidadeDeLados;
        
        //instancio a variavel randomica
        randomico = new Random();
    }
    
    /**
     * Roda o dado retornando um numero de 1 ate o numero de lados do dado
     * 
     * @return Int com o numero randomico gerado pelo dado
     */
    public int rodarDado(){
        return ( randomico.nextInt(lado) + 1);
    }
}
