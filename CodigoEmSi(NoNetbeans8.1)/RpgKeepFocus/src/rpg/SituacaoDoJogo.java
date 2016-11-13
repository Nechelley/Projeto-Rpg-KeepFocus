package rpg;

/**
 * Diz em qual momento(fase) o jogo esta:
 * 0-
 * 
 * @author Nechelley Alves
 */
public enum SituacaoDoJogo {
    CRIANDOHEROIS(0,"Criando Heróis"),BATALHASCOMECARAM(1,"Batalhas Ja Começaram"),ACABOU(2,"Jogo Acabou");
    
    private final int id;
    private final String nome;
    
    private SituacaoDoJogo(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return Int com o id da situacao do jogo
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome da situacao do jogo
     */
    public String getString(){
        return nome;
    }
}
