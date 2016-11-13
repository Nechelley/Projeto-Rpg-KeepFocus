package rpg.personagens;

/**
 * Representa um atributo (a armadura) do personagem:
 * 0-Nada ou 1-Leve ou 2-Pesada
 * 
 * @author Nechelley Alves
 */
public enum Armadura {
    NADA(0,"Nada",0),LEVE(1,"Leve",1),PESADA(2,"Pesada",2);
    
    private final int id;
    private final String nome;
    private final int valor;
    
    private Armadura(int id, String nome,int valor){
        this.id = id;
        this.nome = nome;
        this.valor= valor;
    }
    
    /**
     * Retorna a Armadura passando um id
     * 
     * @param id int com o numero de identificacao da armadura
     * @return Armadura procurado, ou null caso nao encontre
     */
    public static Armadura getArmaduraPorId(int id){
        if(id > 2 || id < 0)
            return null;
        Armadura a = null;
        switch (id) {
            case 0:
                a = Armadura.NADA;
                break;
            case 1:
                a = Armadura.LEVE;
                break;
            case 2:
                a = Armadura.PESADA;
                break;
            default:
                break;
        }
        return a;
    }
    
    /**
     * @return Int com o id da armadura
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome do tipo da armadura
     */
    public String getString(){
        return nome;
    }
    
    /**
     * @return Int com o valor da armadura
     */
    public int getValor(){
        return valor;
    }
}
