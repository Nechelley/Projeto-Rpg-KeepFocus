package rpg.golpes;

/**
 * Golpe do tipo ataque magico, sem nenhum atributo elemental
 * dano = (d6 + 1)
 * chance = 8
 * custo = 1
 * 
 * @author Nechelley Alves
 */
public class GolpeArcano1 extends GolpeMagico{
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeArcano1(String nome){
        super(nome,1,8,1);
    }

    @Override
    public int getDanoEmBatalha(int valorDoDado) {
        return (getDanoBase() + valorDoDado);
    }
}
