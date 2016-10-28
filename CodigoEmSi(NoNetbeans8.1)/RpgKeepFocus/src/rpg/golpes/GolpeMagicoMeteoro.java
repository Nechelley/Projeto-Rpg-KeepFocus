package rpg.golpes;

import rpg.xbuff.XBuff;

/**
 *
 * @author neche
 */
public class GolpeMagicoMeteoro extends GolpeMagico{
    private XBuff queimacao;
    public GolpeMagicoMeteoro(String nome) {
        super(nome);
        setDano(0);
        setChanceDeAcerto(5);
        setCustoDeAcao(2);
        queimacao = new XBuff("queimacao meteorica",3, 0, 1);
    }
    
    @Override
    public XBuff getXBuff(){
        return queimacao;
    }
    
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(Math.round(valorDoDado/3));
    }
    
    @Override
    public String getTipo() {
        return "meteoro";
    }
    
}
