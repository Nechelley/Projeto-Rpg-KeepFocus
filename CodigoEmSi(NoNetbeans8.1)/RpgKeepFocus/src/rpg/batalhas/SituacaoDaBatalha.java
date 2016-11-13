package rpg.batalhas;

/**
 * Diz as possiveis situacoes que a batalha pode estar:
 * 0-Ocorrendo ou 1-Nao Ocorrendo
 * 
 * @author Nechelley Alves
 */
public enum SituacaoDaBatalha {
    OCORRENDO(0,"Ocorrendo"),NAOOCORRENDO(1,"Não Ocorrendo");
    
    private final int id;
    private final String nome;
    
    private SituacaoDaBatalha(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return Int com o id da situacao
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome da situacao
     */
    public String getString(){
        return nome;
    }
}
