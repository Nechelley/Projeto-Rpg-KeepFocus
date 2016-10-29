package rpg.personagens;

/**
 *
 * @author Nechelley Alves
 */
public abstract class Inimigo extends Personagem{
    
    /**
     * Construtor da classe Inimigo
     * 
     * @param nome String com o nome do personagem
     * @param classe String com qual sera a classe do personagem
     * @param pontoForte Int com qual sera o ponto forte do personagem
     * @param arma Int dizendo qual o tipo de arma o personagem usara
     * @param armadura Int com qual o tipo de armadura o personagem usara
     */
    public Inimigo(String nome, String classe, int pontoForte, int arma, int armadura){
        super(nome,classe,pontoForte,arma,armadura);
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Inimigo(Personagem p){
        super(p);
    }
    
    @Override
    public boolean getEhHeroi(){
        return false;
    }
}
