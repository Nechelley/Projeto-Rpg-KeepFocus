package rpg.golpes;

import rpg.xbuff.XBuff;

/**
 *
 * @author neche
 */
public class GolpeMagicoBolaDeFogo extends GolpeMagico{
    private XBuff queimacao;
    public GolpeMagicoBolaDeFogo(String nome) {
        super(nome);
        setDano(1);
        setChanceDeAcerto(5);
        setCustoDeAcao(2);
        queimacao = new XBuff("queimacao",2, 0, 2);
    }
    
    @Override
    public XBuff getXBuff(){
        return queimacao;
    }
    
    @Override
    public void setDanoEmBatalha(int valorDoDado){
        setDano(Math.round(valorDoDado/2) + getDano());
    }
    
    @Override
    public String getTipo() {
        return "fogo";
    }
    
}
