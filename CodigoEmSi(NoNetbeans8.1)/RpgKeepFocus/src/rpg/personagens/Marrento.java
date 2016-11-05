package rpg.personagens;

/**
 * Uma classe de Inimigo
 * 
 * @author Paulo, Nechelley Alves
 */
public class Marrento extends Inimigo {
    
    private static int numInstancias = 0;
    
    /**
     * Construtor da classe Marrento
     * 
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Marrento(Foco foco, Arma arma, Armadura armadura) {
        super("Marrento "+String.valueOf(numInstancias+1),Classe.MARRENTO,foco,arma,armadura);
        numInstancias++;
    }
    
    /**
     * Construtor que retorna um clone do Inimigo p
     * 
     * @param p Personagem que sera clonado
     */
    public Marrento(Personagem p){
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
