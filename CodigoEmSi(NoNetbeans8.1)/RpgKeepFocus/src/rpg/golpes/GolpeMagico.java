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
     */
    public GolpeMagico(String nome){
        super(nome);
    }
}
