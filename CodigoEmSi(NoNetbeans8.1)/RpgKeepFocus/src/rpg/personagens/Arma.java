package rpg.personagens;

/**
 * Representa um atributo (a armadura) do personagem:
 *  Pequena ou Media ou Grande
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
     * @return String com o id da arma
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome da arma
     */
    public String getString(){
        return nome;
    }
}
