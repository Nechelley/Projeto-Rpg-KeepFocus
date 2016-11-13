package rpg.personagens;

/**
 * Uma classe de Inimigo 
 * 
 * @author Paulo, Nechelley Alves
 */
public class Chefe extends Inimigo {
    
    private static int numInstancias = 0;
    
    /**
     * Construtor da classe Chefe
     * 
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Chefe(Foco foco, Arma arma, Armadura armadura) {
        super("Chefe "+String.valueOf(numInstancias+1),Classe.CHEFE,foco,arma,armadura);
        numInstancias++;
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Chefe(Personagem p){
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
