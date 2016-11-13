package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo fogo
 * dano = d6
 * chance = 8
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeMagicoMeteoro extends GolpeMagico{
    
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome Nome do golpe
     */
    public GolpeMagicoMeteoro(String nome) {
        super(nome);
        setDanoBase(0);
        setChanceDeAcerto(8);
        setCustoDeAcao(2);
    }
    
    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(valorDoDado);
    }
}
