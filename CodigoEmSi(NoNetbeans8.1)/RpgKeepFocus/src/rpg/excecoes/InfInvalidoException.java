package rpg.excecoes;

/**
 * Excecao para quando alguma informacao passada nao esta dentro dos padros, 
 * por exemplo quando o nome de um golpe tem caracteres não aceitos no sistema
 * 
 * @author Nechelley Alves
 */
public class InfInvalidoException extends RuntimeException{
    /**
     * Construtor da excecao
     * 
     * @param algo String dizendo o que ests invalido
     * @param valor Valor passado errado
     */
    public InfInvalidoException(String algo,String valor){
        super(algo + " invalid@ (" + valor + ")");
    }
}
