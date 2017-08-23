package rpg.personagens.inimigos;

import rpg.personagens.Personagem;
import rpg.personagens.enums.Arma;
import rpg.personagens.enums.Armadura;
import rpg.personagens.enums.Classe;
import rpg.personagens.enums.Foco;

/**
 * Classe que representa os inimigos do jogador, inimigos estes controlados 
 * por uma "IA"
 * 
 * @author Nechelley Alves
 */
public abstract class Inimigo extends Personagem{
    
    /**
     * Construtor da classe Inimigo
     * 
     * @param nome String com o nome do personagem
     * @param classe Classe com qual sera a classe do personagem
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Inimigo(String nome, Classe classe, Foco foco, Arma arma, Armadura armadura){
        super(nome,classe,foco,arma,armadura);
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Inimigo(Personagem p){
        super(p);
    }
    
    /**
     * Verifica se o personagem e ou nao heroi
     * 
     * @return True se for heroi, false caso contrario
     */
    @Override
    public boolean getEhHeroi(){
        return false;
    }
    
    /**
     * Verifica quantos tipos de inimigos existem
     * 
     * @return Int com a quantidade de classes de inimigos existentes
     */
    public static int numDeClassesDeInimigos(){
        return 10;
    }
}
