package rpg.personagens;

import java.util.ArrayList;
import rpg.personagens.enums.Arma;
import rpg.personagens.enums.Armadura;
import rpg.personagens.enums.Classe;
import rpg.personagens.enums.Estado;
import rpg.personagens.enums.Foco;
import rpg.personagens.enums.SituacaoDeVida;

/**
 * Representa um heroi, que é o tipo de personagem que o jogador cria 
 * e joga com ele(s)
 * 
 * @author Nechelley Alves
 */
public class Heroi extends Personagem{
    
    /**
     * Construtor da classe Heroi
     * 
     * @param nome String com o nome do personagem
     * @param classe Classe com qual sera a classe do personagem
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Heroi(String nome, Classe classe, Foco foco, Arma arma, Armadura armadura){
        super(nome,classe,foco,arma,armadura);
    }
    
    /**
     * Construtor que retorna um clone do Heroi p
     * 
     * @param p Personagem que sera clonado
     */
    public Heroi(Personagem p){
        super(p);
    }
    
    /**
     * Verifica se o personagem e ou nao heroi
     * 
     * @return True se for heroi, false caso contrario
     */
    @Override
    public boolean getEhHeroi(){
        return true;
    }
    
    @Override
    public void diminuiNumInstancias() {
        //IMPEDIR QUE HEROI SE TORNE ABSTRACT
    }
    @Override
    public int getInstancias(){
        return 0;
    }
}
