package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo gelo
 * dano = ((d6/3) + 2)
 * chance = 10
 * custo = 1
 * 
 * @author Nechelley Alves
 */
public class GolpeFogo1 extends GolpeMagico{
    
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeFogo1(String nome){
        super(nome,2,10,1);
    }

    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public int getDanoEmBatalha(int valorDoDado) {
        return (getDanoBase() + Math.round(valorDoDado/3));
    }
}
