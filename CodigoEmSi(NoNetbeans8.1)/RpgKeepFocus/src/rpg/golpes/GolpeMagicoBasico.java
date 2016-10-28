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
public class GolpeMagicoBasico extends GolpeMagico{
    /**
     * Construtor onde defino como o golpe sera executado.
     * @param nome nome do golpe
     */
    public GolpeMagicoBasico(String nome){
        super(nome);
        setDano(1);
        setChanceDeAcerto(5);
        setCustoDeAcao(2);
    }

    @Override
    public void setDanoEmBatalha(int valorDoDado) {
        setDano(getDano() + valorDoDado);
    }

    @Override
    public XBuff getXBuff() {
        return null;
    }
    
    @Override
    public String getTipo() {
        return "magico";
    }
}
