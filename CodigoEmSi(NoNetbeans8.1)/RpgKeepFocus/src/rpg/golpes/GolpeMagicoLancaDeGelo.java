package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo gelo
 * dano = ((d6/3) + 2)
 * chance = 6
 * custo = 1
 * 
 * @author Nechelley Alves
 */
public class GolpeMagicoLancaDeGelo extends GolpeMagico{
    
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeMagicoLancaDeGelo(String nome){
        super(nome);
        setDanoBase(2);
        setChanceDeAcerto(6);
        setCustoDeAcao(1);
    }

    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public void setDanoEmBatalha(int valorDoDado) {
        setDano(getDanoBase() + Math.round(valorDoDado/3));
    }
}
