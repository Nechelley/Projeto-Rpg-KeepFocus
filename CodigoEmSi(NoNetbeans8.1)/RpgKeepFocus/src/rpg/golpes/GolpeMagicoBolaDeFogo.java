package rpg.golpes;

/**
 * Golpe do tipo ataque magico, coma atributo fogo
 * dano = ((d6/2) + 1)
 * chance = 10
 * custo = 2
 * 
 * @author Nechelley Alves
 */
public class GolpeMagicoBolaDeFogo extends GolpeMagico{
    /**
     * Construtor do golpe
     * 
     * @param nome Nome do golpe
     */
    public GolpeMagicoBolaDeFogo(String nome) {
        super(nome);
        setDanoBase(1);
        setChanceDeAcerto(10);
        setCustoDeAcao(2);
    }
    
    /**
     * Calculo do dano do ataque
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(Math.round(valorDoDado/2) + getDanoBase());
    }    
}
