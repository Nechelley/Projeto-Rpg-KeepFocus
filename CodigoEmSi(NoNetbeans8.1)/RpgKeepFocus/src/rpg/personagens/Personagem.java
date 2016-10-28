package rpg.personagens;

import rpg.xbuff.XBuff;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rpg.AcaoInvalidaException;
import rpg.Dado;
import rpg.golpes.Golpe;
import rpg.golpes.GolpeFisico;
import rpg.InfInvalidoException;
import rpg.InfJaExistenteException;
import rpg.golpes.GolpeMagicoBolaDeFogo;
import rpg.golpes.GolpeMagicoBasico;
import rpg.golpes.GolpeMagicoLancaDeGelo;
import rpg.golpes.GolpeMagicoMeteoro;
import rpg.golpes.GolpeMagicoNevasca;

/**
 * Classe que representa um personagem, tendo suas caracteristicas(id, nome, classe, ponto forte, 
 pontos de saude, dano recebido, dano recebido maximo, situacao da vida, arma, armadura, iniciativa, 
 dado proprio e se é ou nao heroi), alem das ações que o mesmo pode executar durante o jogo.
 * 
 * @author Nechelley Alves
 */
public abstract class Personagem implements Comparable<Personagem>, Serializable{
    //String com o nome do personagem
    private final String nome;
    //String com a classe do personagem esta podendo ser mago, clerigo, guerreiro.
    private final String classe;
    //ponto forte que pode ser: 
    //0-Força 1-Destreza 2-Constituição 3-Carisma
    private final int pontoForte;
    //representa quantos pontos o personagem tem para gastar com acoes
    private int pontosDeAcao;
    //diz se o personagem pode estar esquivando ou nao de um golpe
    private boolean estaEsquivando;
    //diz se o personagem pode estar defendendo ou nao um golpe
    private boolean estaDefendendo;
    //diz se o personagem esta congelado
    private boolean estaCongelado;
    //quantos pontos o personagem tem de saude
    private int pontoSaude;
    //quantidade de dano recebido pelo personagem em certo momento
    private int danoRecebido;
    //quantidade dano necessario para que o personagem morra
    private final int danoRecebidoMaximo;
    //situação do estado de vida do personagem com base no danoRecebido podendo ser: 
    //0-Saudável     1-Atordoado     2-Desesperado     3-Inconsciente   4-Morto
    //20% dano       40% dano        60% dano          99% dano         100% dano
    private int situacaoDeVida;
    
    //quantos pontos o personagem tem de arma podendo ser: 0-Pequena 1-Média 2-Grande
    private final int arma;
    //quantos pontos o personagem tem de armadura
    private final int armadura;
    //quantos pontos o personagem tem de iniciativa, que determina quem começa a ação em uma batalha
    private int iniciativa;
    //lista com os golpes que o personagem pode usar
    private List<Golpe> golpes;
    //lista de XBuffs
    private List<XBuff> xbuffs;
    //dado proprio do personagem
    private Dado d6;
    
    
     
    //CONSTRUTORES
    
    /**
     * Construtor da classe Personagem
     * 
     * @param nome String com o nome do personagem
     * @param classe String com qual sera a classe do personagem
     * @param pontoForte Int com qual sera o ponto forte do personagem
     * @param arma Int dizendo qual o tipo de arma o personagem usara
     * @param armadura Int com qual o tipo de armadura o personagem usara
     */
    public Personagem(String nome, String classe, int pontoForte, int arma, int armadura){
            if(!verificaNome(nome))
                throw new InfInvalidoException("Nome",nome);
            this.nome = nome;
            if(!verificaClasse(classe))
                throw new InfInvalidoException("Classe",classe);
            this.classe = classe;
            if(!(pontoForte >= 0 && pontoForte <= 3))
                throw new InfInvalidoException("Ponto forte",Integer.toString(pontoForte));
            this.pontoForte = pontoForte;
            
            calculaPontoSaude();
            
            danoRecebido = 0;
            danoRecebidoMaximo = 5*pontoSaude;
            situacaoDeVida = 0;
                    
            if(!(arma >= 0 && arma <= 2))
                throw new InfInvalidoException("Arma",Integer.toString(arma));
            this.arma = arma;
            if(!(armadura >= 0 && armadura <= 2))
                throw new InfInvalidoException("Armadura",Integer.toString(armadura));
            this.armadura = armadura;
            
            iniciativa = 0;
            
            golpes = new ArrayList<Golpe>();
            xbuffs = new ArrayList<XBuff>();
            
            d6 = new Dado(6);
            
            pontosDeAcao = 2;
            estaEsquivando = false;
            estaDefendendo = false;
            estaCongelado = false;
    }
    /**
     * Verifica se um nome é valido, ou seja, composto de apenas letras(maiusculas e minusculas), numeros(0-9) e spaces
     * 
     * @param nome String com o nome a ser verificado
     * @return true se o nome for valido, false caso contrario
     */
    private boolean verificaNome(String nome){
        if(nome.isEmpty())
            return false;
        String valoresValidos[] = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9"," "
        };
        boolean letraValida = false;
        for(int i = 0; i < nome.length(); i++){
            letraValida = false;
            for(String v : valoresValidos){
                if(nome.substring(i, i + 1).equals(v)){
                    letraValida = true;
                    break;
                }
            }
            if(!letraValida)
                return false;
        }
        return true;
    }
    /**
     * Verifica se uma classe é valida, ou seja, ela é Guerreiro, Mago ou Clerigo, ou uma das dos inimigos
     * 
     * @param nome String com a classe a ser verificada
     * @return true se a classe for valida, false caso contrario
     */
    private boolean verificaClasse(String classe){
        if(classe.isEmpty())
            return false;
        String valoresValidos[] = new String[8];
        if(getEhHeroi()){
            valoresValidos[0] = "Guerreiro";
            valoresValidos[1] = "Mago";
            valoresValidos[2] = "Clerigo";
        }
        else{
            valoresValidos[0] = "Zumbi";
            valoresValidos[1] = "Ogro";
            valoresValidos[2] = "Esqueleto";
            valoresValidos[3] = "Dragao";
            valoresValidos[4] = "Goblin";
            valoresValidos[5] = "Cultista";
        }
        boolean palavraValida = false;
        for(String v : valoresValidos){
            if(classe.equals(v)){
                palavraValida = true;
                break;
            }

        }
        return palavraValida;
    }
    /**
     * Define os pontos de saúde do personagem de acordo com a classe e ponto forte.
     */
    private void calculaPontoSaude() {
        //Pontos de saúde padrão para as classes não listadas
        pontoSaude = 4;
        
        if (classe.equals("Dragao"))
                pontoSaude = 4;
        if(classe.equals("Guerreiro") || classe.equals("Ogro"))
            pontoSaude = 3;
        else if (classe.equals("Goblin") || classe.equals("Cultista") ||
                classe.equals("Zumbi"))
            pontoSaude = 2;
        
        if(pontoForte == 2)//isso é para o caso do personagem tiver ponto forte em constituição
            pontoSaude++;
    }
    
    /**
     * Construtor que retorna um clone do Personagem p
     * 
     * @param p Personagem que sera clonado
     */
    public Personagem(Personagem p){
        this.nome = p.nome;
        this.classe = p.classe;
        this.pontoForte = p.pontoForte;
        this.pontoSaude = p.pontoSaude;
        this.danoRecebido = p.danoRecebido;
        this.danoRecebidoMaximo = p.danoRecebidoMaximo;
        this.situacaoDeVida = p.situacaoDeVida;
        this.arma = p.arma;
        this.armadura = p.armadura;
        this.iniciativa = p.iniciativa;
        this.golpes = p.golpes;
        this.d6 = p.d6;
    }
    
    
    
    //ACAO ESQUIVAR
    
    public void esquivar(){
        if(pontosDeAcao == 0)
            throw new AcaoInvalidaException("esquivar",0);
        pontosDeAcao--;
        estaEsquivando = true;
    }
    
    
    
    //ACAO DEFENDER
    
    public void defender(){
        if(pontosDeAcao == 0)
            throw new AcaoInvalidaException("defender",0);
        pontosDeAcao--;
        estaDefendendo = true;
    }
        
        
        
        
    //ACAO ATACAR
    
    /**
     * Ação atacar, o personagem que executa esta ação escolhe um alvo e causa dano nele.
     * 
     * @param alvo Personagem que recebera o ataque
     * @param golpe String com o nome do golpe que esta sendo executado
     * @return int onde (-1) significa que o ataque nao acertou o alvo,
     * (-2) significa que o alvo esquivou, (100 + dano) signifa que o alvo defendendeu
     * e qualquer outro valor significa que o ataque acertou e o metodo esta retornando o dano
     */
    public String atacar(Personagem alvo, String golpe){
        Golpe g = getGolpePeloNome(golpe);
        if(g == null)//vejo se o golpe e valido
            throw new AcaoInvalidaException("atacar",1);
        int custoDaAcao = g.getCustoDeAcao();
        if(custoDaAcao > pontosDeAcao)// vejo se o personagem tem pontos de acao suficientes
            throw new AcaoInvalidaException("atacar",0);
        //cobro o custo
        pontosDeAcao -= custoDaAcao;
        
        String relatorio = getNome() + " atacou " + alvo.getNome() + " usando " + golpe;
        
        //verifico se o alvo esta esquivando
        if(alvo.estaEsquivando){
            alvo.estaEsquivando = false;
            //o alvo roda dois dados
            int dado1chance = alvo.rodarDado();
            int dado2chance = alvo.rodarDado();
            
            if(dado1chance + dado2chance > 9){//se a soma dos dados for maior que 9, 27% de chance disso ocorrer
                return relatorio + " porem o alvo se esquivou e portanto nao recebeu dano.";
            }
        }
            
        //agora verifica-se se o atacante acertou o alvo
        if( !golpeAcertou(golpe) ){
            //se o atacante errar
            return relatorio + " porém não conseguiu acertar.";
        }
        else{
            //se o atacante acertar o ataque
            
            //calculo do dano inicial
            g.setDanoEmBatalha(d6.rodarDado());
            int dano = g.getDano();
            
            //aumento o dano em +1 se o personagem possuir ponto forte em força
            if(pontoForte == 0)
                dano++;

            //reduzo o dano de acordo com a armadura do alvo
            dano -= alvo.armadura;
            
            //garantia que o dano sera no minimo zero
            if(dano < 0)
                dano = 0;
            
            //modificador se o alvo esta defendendo
            if(alvo.estaDefendendo){
                alvo.estaDefendendo = false;
                dano *= 0.5;//ou seja, reduzo o dano na metade
                alvo.receberDano(dano,g);
                return relatorio + " porem o alvo se defendeu e recebeu apenas " + dano + " de dano.";
            }
            else{
                alvo.receberDano(dano,g);
                return relatorio + " causando " + dano + " de dano.";
            }
        }     
    }
    
    /**
     * Verifica se o golpe vai acertar, true para acertou e false para errou
     * 
     * @param golpe: String com o nome do golpe
     * @return Boolean com true se acertou e false se errou
     */
    protected boolean golpeAcertou(String golpe){
        //o personagem roda dois dados
        int dado1chance = rodarDado();
        int dado2chance = rodarDado();
        //return getGolpePeloNome(golpe).getChanceDeAcerto() < (dado1chance + dado2chance);
        return true;//true por motivos de teste
        
    }
    
    /**
     * Retorna o Golpe procurado
     * 
     * @param nome String com o nome do golpe procurado
     * @return Golpe preocurado ou null se nao encontrar
     */
    public Golpe getGolpePeloNome(String nome){
        for(Golpe g : golpes){
            if(g.getNome().equals(nome))
                return g;
        }
        return null;
    }
    
    /**
     * faz as atualizacoes necessarias quando o personagem recebe dano
     * 
     * @param dano int com quantidade de dano tomado
     * @param golpe Golpe que esta recebendo
     */
    protected void receberDano(int dano,Golpe golpe){
        //acrescenta-se o dano calculado ao dano recebido do alvo
        danoRecebido += dano ;
        
        //adiciono o Xbuff se existir
        XBuff xbuff = golpe.getXBuff();
        if(xbuff != null){
            xbuffs.add(xbuff);
        }
        
        //atualizao a situação de vida do alvo
        defineSituacaoDeVida();
    }
    
    public String receberXBuffs(){
        if(xbuffs.isEmpty()){
            return "";
        }
        
        String retorno = nome + " recebeu as seguintes xbuffs:\n";
        for(int i = 0; i < xbuffs.size(); i++){
            XBuff x = xbuffs.get(i);
            //se for um xbuff referente a vida
            if(x.getLocalDeEfeito() == 0){
                //somo ou tiro da vida
                danoRecebido += x.getPontosDeEfeito();
                //faco o turno passar para o xbuff
                x.rodadaPassou();
                
                //add no relatorio
                retorno += "\t" + x.getNome() + " que acrescentou " + x.getPontosDeEfeito() + 
                        " aos danos recebidos, e ira durar mais " + x.getDuracao() + " turnos.\n";
                
                //garanto que o danoRecebido n fique em um valor invalido
                if(danoRecebido < 0)
                    danoRecebido = 0;
                if(danoRecebido > danoRecebidoMaximo)
                    danoRecebido = danoRecebidoMaximo;
                //atualizo a situacao da vida
                defineSituacaoDeVida();
                //se a duracao zerou o xbuff acabou
                if(x.getDuracao() == 0){
                    xbuffs.remove(x);
                }
            }
            if(x.getLocalDeEfeito() == 1){
                estaCongelado = true;
                x.rodadaPassou();
                //se a duracao zerou o xbuff acabou
                if(x.getDuracao() == -1){
                    estaCongelado = false;
                    xbuffs.remove(x);
                }
                else{
                    retorno += "\t" + x.getNome() + " esta ativo, e ira durar mais " + x.getDuracao() + " turnos.\n";
                }
            }
            
            
        }//fecha loop dos xbuffs
        return retorno;   
    }
    
    /**
     * Define a situacao de vida do personagem com base no dano recebido
     */
    protected void defineSituacaoDeVida(){
        //0-Saudável     1-Atordoado     2-Desesperado     3-Inconsciente   4-Morto
        //20% dano       40% dano        60% dano          99% dano         100% dano
        
        if (danoRecebido < danoRecebidoMaximo * 0.2)
            situacaoDeVida = 0;
        else if (danoRecebido >= danoRecebidoMaximo * 0.2 && danoRecebido < danoRecebidoMaximo * 0.4)
            situacaoDeVida = 1;
        else if (danoRecebido >= danoRecebidoMaximo * 0.4 && danoRecebido < danoRecebidoMaximo * 0.6)
            situacaoDeVida = 2;
        else if (danoRecebido >= danoRecebidoMaximo * 0.6 && danoRecebido < danoRecebidoMaximo)
            situacaoDeVida = 3;
        else if (danoRecebido >= danoRecebidoMaximo){
            situacaoDeVida = 4;
            diminuiNumInstancias();
        }
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    public abstract void diminuiNumInstancias ();
    
    
    /**
     * Adiciona um golpes fisicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeFisico(String nome){
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeFisico(nome,arma));
    }
    
    public boolean verificaClasseMagica() {
        return (classe.equals("Mago") ||
                classe.equals("Clerigo") ||
                classe.equals("Cultista") ||
                classe.equals("Dragao"));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoBasico(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe);
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoBasico(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoBolaDeFogo(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe);
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoBolaDeFogo(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoMeteoro(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe);
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoMeteoro(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoLancaDeGelo(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe);
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoLancaDeGelo(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoNevasca(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe);
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoNevasca(nome));
    }
    
    //GETS
    
    public abstract boolean getEhHeroi();
    
    public String getNome(){
        return nome;
    }
    
    public String getClasse(){
        return classe;
    }
    
    public String getPontoForte(){
        return pontoForteString();
    }
    
    public int getPontoSaude(){
        return pontoSaude;
    }
    
    public int getDanoRecebido(){
        return danoRecebido;
    }
    
    public int getDanoRecebidoMaximo(){
        return danoRecebidoMaximo;
    }
    
    public String getSituacaoDeVida(){
        return situacaoDeVidaString();
    }
    
    public int getArma(){
        return arma;
    }
    
    public int getArmadura(){
        return armadura;
    }
    
    public int getIniciativa(){
        return iniciativa;
    }
    
    public int getPontosDeAcao(){
        return pontosDeAcao;
    }
    
    public void setPontosDeAcao(int pontosDeAcao){
        if(pontosDeAcao < 0 || pontosDeAcao > 2)
            throw new InfInvalidoException("Pontos de ação",String.valueOf(pontosDeAcao));
        this.pontosDeAcao = pontosDeAcao;
    }
    
    public int getNumGolpes() {
        return golpes.size();
    }
    
    /**
     * Retorna o nome do golpe na posicao pos da lista
     * @param pos int com a posicao na lista
     * @return String com o nome do golpe
     */
    public String getGolpe(int pos) {
        if(pos < 0 || pos > golpes.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em golpes.");
        return golpes.get(pos).getNome();
    }
    
    /**
     * Retorna o custo de um golpe na posicao pos da lista
     * 
     * @param pos int com a posicao na lista
     * @return int com o custo
     */
    public int getGolpeCusto(int pos) {
        if(pos < 0 || pos > golpes.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " inválida.");
        return golpes.get(pos).getCustoDeAcao();
    }
    
    /**
     * Retorna o tipo de um golpe na posicao pos da lista
     * @param pos int com a posicao na lista
     * @return String com o tipo
     */
    public String getGolpeTipo(int pos) {
        if(pos < 0 || pos > golpes.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " inválida.");
        return golpes.get(pos).getTipo();
    }
    
    /**
     * Retorna se o personagem esta vivo(true) ou morto(false)
     * 
     * @return Boolean com true se vivo e false se morto
     */
    public boolean estaVivo(){
        return situacaoDeVida != 4;
    } 
    
    public boolean getEstaDefendendo(){
        return estaDefendendo;
    }
    
    public boolean getEstaEsquivando(){
        return estaEsquivando;
    }
    
    public boolean getEstaCongelado(){
        return estaCongelado;
    }
    
    //OUTROS
    
    /**
     * Compara dois personagens com base nas sua iniciativas
     * 
     * @param outroPersonagem Personagem que sera comparado
     * @return Int sendo (-1) sem vem antes, (+1) se vem depois e (0) se igual
     */
    @Override
    public int compareTo(Personagem outroPersonagem) {
        if (this.iniciativa > outroPersonagem.iniciativa)
            return -1;
        if (this.iniciativa < outroPersonagem.iniciativa)
            return 1;
        return 0;
    }
    
    public void setIniciativa(int iniciativa){
        if(iniciativa < 0 || iniciativa > 6)
            throw new InfInvalidoException("Pontos de ação",String.valueOf(pontosDeAcao));
        if(pontoForte == 1)
            //se tiver ponto forte em destreza
            this.iniciativa = iniciativa + 1;
        else
            //se nao tiver ponto forte em destreza
            this.iniciativa = iniciativa;
    }
    
    
    /**
     * Retorna a situacao de vida como forma de uma String representativa
     * 
     * @return String da situacao de vida
     */
    protected String situacaoDeVidaString(){
        String texto;
        texto = "Erro";
        
        switch(situacaoDeVida){
            case 0:
                texto = "Saudável";
                break;
            case 1:
                texto = "Atordoado";
                break;
            case 2:
                texto = "Desesperado";
                break;
            case 3:
                texto = "Inconsciente";
                break;
            case 4:
                texto = "Morto";
                break;
        }
        return texto;
    }
    
    /**
     * Retorna o ponto forte como forma de uma String representativa
     * 
     * @return String do ponto forte
     */
    protected String pontoForteString(){
        String texto;
        texto = "Erro";
        
        switch(this.pontoForte){
            case 0:
                texto = "Força";
                break;
            case 1:
                texto = "Destreza";
                break;
            case 2:
                texto = "Constituição";
                break;
            case 3:
                texto = "Carisma";
                break;
        }
        return texto;
    }
    
    /**
     * Roda o dado do personagem e retorna o valor.
     * 
     * @return Int com o número randomico gerado pelo dado.
     */
    public int rodarDado() {
        return d6.rodarDado();
    }
}
