package rpg.personagens;

/**
 * Representa o estado de um personagem que pode estar em estado de:
 * 0-Esquiva ou 1-Defendendo
 * 
 * @author Nechelley Alves
 */
public enum Estado {
    ESQUIVANDO(0,"Esquivando"),DEFENDENDO(1,"Defendendo");
    
    private final int id;
    private final String nome;
    
    private Estado(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return Int com o id do estado
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome do estado
     */
    public String getString(){
        return nome;
    }
}
