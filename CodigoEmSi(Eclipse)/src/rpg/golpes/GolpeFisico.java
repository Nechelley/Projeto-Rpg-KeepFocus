package rpg.golpes;

import rpg.xbuff.XBuff;
import rpg.Dado;
import rpg.InfInvalidoException;

/**
 *
 * @author Nechelley Alves
 */
public class GolpeFisico extends Golpe{
    /**
     * Construtor onde defino como o golpe sera executado, com base na arma do personagem
     */
    public GolpeFisico(String nome, int arma){
        super(nome);
        Dado d6 = new Dado(6);
        if(arma < 0 || arma > 2)
            throw new InfInvalidoException("Arma",String.valueOf(arma));
        switch(arma){
            case 0:
                setDano(0);
                break;
            case 1:
                setDano(1);
                break;
            case 2:
                setDano(2);
                break;
        }
        
        setChanceDeAcerto(6);
        setCustoDeAcao(1);
    }
    
    public XBuff getXBuff(){
        return null;
    }
    
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(Math.round(valorDoDado/2) + getDano());
    }
    
    /**
     * Retorna o tipo do golpe
     * @return String com o tipo do golpe
     */
    @Override
    public String getTipo() {
        return "fisico";
    }
}
