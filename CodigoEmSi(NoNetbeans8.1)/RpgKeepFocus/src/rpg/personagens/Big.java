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
public class Big extends Inimigo {
    
    private static int numInstancias = 0;
    
    /**
     * Construtor da classe Big
     * 
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Big(Foco foco, Arma arma, Armadura armadura) {
        super("Big "+String.valueOf(numInstancias+1),Classe.BIG,foco,arma,armadura);
        numInstancias++;
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Big(Personagem p){
        super(p);
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    @Override
    public void diminuiNumInstancias () {
        numInstancias--;
    }
    @Override
    public int getInstancias(){
        return numInstancias;
    }
}
