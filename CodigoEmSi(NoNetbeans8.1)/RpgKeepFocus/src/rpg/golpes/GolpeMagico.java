package rpg.golpes;

/**
 * Representa os golpes magicos que possuem "extras"
 * 
 * @author Nechelley Alves
 */
public abstract class GolpeMagico extends Golpe{
    
    /**
     * Construtor onde defino como o golpe sera executado.
     * 
     * @param nome Nome do golpe
     * @param danoBase dano base do golpe
     * @param chanceDeAcerto chance de acerto do golpe
     * @param custoDeAcao custo para executar o golpe
     */
    public GolpeMagico(String nome, int danoBase, int chanceDeAcerto, int custoDeAcao){
        super(nome,danoBase,chanceDeAcerto,custoDeAcao);
    }
}
