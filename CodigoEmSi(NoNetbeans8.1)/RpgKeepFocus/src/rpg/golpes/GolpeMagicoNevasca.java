package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo gelo
 * dano = (d6/6 + 4)
 * chance = 6
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeMagicoNevasca extends GolpeMagico{
    /**
     * Construtor onde defino como o golpe sera executado.
     * 
     * @param nome Nome do golpe
     */
    public GolpeMagicoNevasca(String nome){
        super(nome);
        setDanoBase(4);
        setChanceDeAcerto(6);
        setCustoDeAcao(2);
    }
    
    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public void setDanoEmBatalha(int valorDoDado) {
        setDano(getDanoBase() + Math.round(valorDoDado/6));
    }
}
