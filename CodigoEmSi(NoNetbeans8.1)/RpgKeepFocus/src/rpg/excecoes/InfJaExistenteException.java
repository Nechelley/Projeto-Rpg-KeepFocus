package rpg.excecoes;

/**
 * Excecao para quando alguma informacao existente nao pode se repetir, 
 * por exemplo quando cria-se um golpe, este golpe nao pode ter o mesmo nome de 
 * outro golpe do mesmo personagem
 * 
 * @author Nechelley Alves
 */
public class InfJaExistenteException extends RuntimeException{
    public InfJaExistenteException(String inf){
        super("(" + inf + ") ja existente.");
    }
}
