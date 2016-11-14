package rpg.personagens.enums;

/**
 * Representa as condicoes de vida que o personagem pode estar, com base no dano recebido
 * 
 * 0-Saudável ou 1-Atordoado ou 2-Desesperado ou 3-Inconsciente ou 4-Morto
 * 20% dano      40% dano       60% dano         99% dano          100% dano
 * 
 * @author Nechelley Alves
 */
public enum SituacaoDeVida {
    SAUDAVEL(0,"Saudável",0.2),ATORDOADO(1,"Atordoado",0.4),DESESPERADO(2,"Desesperado",0.6),
    INCONSCIENTE(3,"Inconsciente",0.99),MORTO(4,"Morto",1);
    
    private final int id;
    private final String nome;
    private final double porcentagem;
    
    private SituacaoDeVida(int id, String nome, double porcentagem){
        this.id = id;
        this.nome = nome;
        this.porcentagem = porcentagem;
    }
    
    /**
     * @return Int com o id da situacao de vida
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return Double com a porcentagem da situacao de vida
     */
    public double getPorcentagem(){
        return porcentagem;
    }

    /**
     * @return String com o nome da situacao de vida
     */
    public String getString(){
        return nome;
    }
}
