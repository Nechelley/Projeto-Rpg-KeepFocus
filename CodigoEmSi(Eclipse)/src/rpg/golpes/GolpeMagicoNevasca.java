/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.golpes;

import rpg.xbuff.XBuff;

/**
 *
 * @author neche
 */
public class GolpeMagicoNevasca extends GolpeMagico{
    private XBuff congelamento;
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome nome do golpe
     */
    public GolpeMagicoNevasca(String nome){
        super(nome);
        setDano(0);
        setChanceDeAcerto(6);
        setCustoDeAcao(2);
        congelamento = new XBuff("congelamento",0, 1, 2);
    }

    @Override
    public void setDanoEmBatalha(int valorDoDado) {
        setDano(valorDoDado/6);
    }

    @Override
    public XBuff getXBuff() {
        return congelamento;
    }
    
    @Override
    public String getTipo() {
        return "nevasca";
    }
}
