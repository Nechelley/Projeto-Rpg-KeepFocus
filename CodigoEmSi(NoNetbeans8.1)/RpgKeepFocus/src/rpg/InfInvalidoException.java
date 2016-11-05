package rpg;

/**
 *
 * @author Nechelley Alves
 */
public class InfInvalidoException extends RuntimeException{
    /**
     * Construtor da excecao
     * 
     * @param algo String dizendo o que ests invalido
     * @param valor valor passado errado
     */
    public InfInvalidoException(String algo,String valor){
        super(algo + " invalid@ (" + valor + ")");
    }
}
