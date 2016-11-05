package rpg.personagens;

/**
 * Representa um atributo (a armadura) do personagem:
 * 0-Pequena ou 1-Media ou 2-Grande
 * 
 * @author Nechelley Alves
 */
public enum Arma {
    PEQUENA(0,"Pequena",0),MEDIA(1,"Média",1),GRANDE(2,"Grande",2);
    
    private final int id;
    private final String nome;
    private final int valor;
    
    private Arma(int id, String nome, int valor){
        this.id = id;
        this.nome = nome;
        this.valor = valor;
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
    
    /**
     * @return Int com o valor da força da arma
     */
    public int getValor(){
        return valor;
    }
}
