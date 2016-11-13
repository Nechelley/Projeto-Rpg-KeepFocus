package rpg.golpes;

/**
 * Golpe do tipo ataque magico, sem nenhum atributo elemental
 * dano = (d6 + 1)
 * chance = 5
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeMagicoBasico extends GolpeMagico{
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeMagicoBasico(String nome){
        super(nome);
        setDanoBase(1);
        setChanceDeAcerto(5);
        setCustoDeAcao(2);
    }

    @Override
    public void setDanoEmBatalha(int valorDoDado) {
        setDano(getDanoBase() + valorDoDado);
    }
}
