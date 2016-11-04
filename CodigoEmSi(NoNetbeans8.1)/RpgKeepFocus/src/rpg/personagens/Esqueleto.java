/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.personagens;

/**
 *
 * @author paulo
 */
public class Esqueleto extends Inimigo {
    
    private static int numInstancias = 0;
    
    /**
     * Construtor da classe Esqueleto
     * @param pontoForte Int com qual sera o ponto forte do personagem
     * @param arma Int dizendo qual o tipo de arma o personagem usara
     * @param armadura Int com qual o tipo de armadura o personagem usara
     */
    public Esqueleto(Foco pontoForte, int arma, int armadura) {
        super("Esqueleto "+String.valueOf(numInstancias+1),"Esqueleto",pontoForte,arma,armadura);
        numInstancias++;
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Esqueleto(Personagem p){
        super(p);
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    @Override
    public void diminuiNumInstancias () {
        numInstancias--;
    }
}
