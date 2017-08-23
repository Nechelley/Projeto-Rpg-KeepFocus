package rpg.golpes;

import rpg.personagens.enums.Arma;

/**
 * Golpe do tipo ataque basico, pois depende so da arma e do lancamento do dado
 * dano = ((d6/2) + base)
 * chance = 6
 * custo = 1
 * 
 * @author Nechelley Alves
 */
public class GolpeFisico extends Golpe{
    
    /**
     * Construtor onde defino como o golpe sera executado, com base na arma do personagem
     * @param nome Nome do Golpe
     * @param arma Arma utilizada para fazer o ataque
     */
    public GolpeFisico(String nome, Arma arma){
        super(nome,0,6,1);
        if(arma == null)
            throw new RuntimeException("Arma inválida ( NULL ).");
        this.setDanoBase(arma.getValor());
        
    }
    
    /**
     * Calculo do dano do ataque do tipo ataque basico
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public int getDanoEmBatalha(int valorDoDado){
        return (Math.round(valorDoDado/2) + getDanoBase());
    }
}
