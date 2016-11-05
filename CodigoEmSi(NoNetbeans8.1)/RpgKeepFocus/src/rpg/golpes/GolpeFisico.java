package rpg.golpes;

import rpg.InfInvalidoException;
import rpg.personagens.Arma;

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
        super(nome);
        if(arma == null)
            throw new InfInvalidoException("Arma","NULL");
        setDanoBase(arma.getValor());
        setChanceDeAcerto(6);
        setCustoDeAcao(1);
    }
    
    /**
     * Calculo do dano do ataque do tipo ataque basico
     * 
     * @param valorDoDado Valor do dado jogado
     */
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(Math.round(valorDoDado/2) + getDanoBase());
    }
}
