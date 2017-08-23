package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo fogo
 * dano = ((d6/2) + 3)
 * chance = 8
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeArcano2 extends GolpeMagico{
    /**
     * Construtor do golpe
     * 
     * @param nome Nome do golpe
     */
    public GolpeArcano2(String nome) {
        super(nome,3,8,2);
    }
    
    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public int getDanoEmBatalha(int valorDoDado){
        return (Math.round(valorDoDado/2) + getDanoBase());
    }    
}
