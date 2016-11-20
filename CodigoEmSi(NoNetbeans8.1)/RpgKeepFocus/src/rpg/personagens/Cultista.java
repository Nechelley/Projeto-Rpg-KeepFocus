package rpg.personagens;

import rpg.personagens.enums.Arma;
import rpg.personagens.enums.Armadura;
import rpg.personagens.enums.Classe;
import rpg.personagens.enums.Foco;

/**
 * Uma classe de Inimigo
 * 
 * @author Paulo, Nechelley Alves
 */
public class Cultista extends Inimigo {
    
    private static int numInstancias = 0;
    
    /**
     * Construtor da classe Cultista
     * 
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Cultista(Foco foco, Arma arma, Armadura armadura) {
        super("Cultistaa"+numInstancias+"a "+String.valueOf(numInstancias+1),Classe.CULTISTA,foco,arma,armadura);
        numInstancias++;
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Cultista(Personagem p){
        super(p);
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    @Override
    public void diminuiNumInstancias () {
        numInstancias--;
    }
    public int getInstancias(){
        return numInstancias;
    }
}
