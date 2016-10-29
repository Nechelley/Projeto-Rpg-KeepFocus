package rpg.personagens;

/**
 *
 * @author Nechelley Alves
 */
public class Heroi extends Personagem{
    
    /**
     * Construtor da classe Heroi
     * 
     * @param nome String com o nome do personagem
     * @param classe String com qual sera a classe do personagem
     * @param pontoForte Int com qual sera o ponto forte do personagem
     * @param arma Int dizendo qual o tipo de arma o personagem usara
     * @param armadura Int com qual o tipo de armadura o personagem usara
     */
    public Heroi(String nome, String classe, int pontoForte, int arma, int armadura){
        super(nome,classe,pontoForte,arma,armadura);
    }
    
    /**
     * Construtor que retorna um clone do Heroi p
     * 
     * @param p Personagem que sera clonado
     */
    public Heroi(Personagem p){
        super(p);
    }
    
    @Override
    public boolean getEhHeroi(){
        return true;
    }
    
    @Override
    public void diminuiNumInstancias() {
        //IMPEDIR QUE HEROI SE TORNE ABSTRACT
    }
}
