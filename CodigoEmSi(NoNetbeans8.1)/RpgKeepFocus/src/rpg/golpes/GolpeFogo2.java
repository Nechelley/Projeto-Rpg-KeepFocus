package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo fogo
 * dano = 3*d6
 * chance = 3
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeFogo2 extends GolpeMagico{
    
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeFogo2(String nome) {
        super(nome,0,3,2);
    }
    
    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public int getDanoEmBatalha(int valorDoDado){
        return (3*valorDoDado);
    }
}
