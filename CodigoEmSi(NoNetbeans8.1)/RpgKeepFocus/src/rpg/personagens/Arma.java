package rpg.personagens;

/**
 * Representa um atributo (a armadura) do personagem:
 * 0-Pequena ou 1-Media ou 2-Grande
 * 
 * @author Nechelley Alves
 */
public enum Arma {
    PEQUENA(0,"Pequena"),MEDIA(1,"Média"),GRANDE(2,"Grande");
    
    private final int id;
    private final String nome;
    
    private Arma(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return Int com o id da arma
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome do tipo da arma
     */
    public String getString(){
        return nome;
    }
}
