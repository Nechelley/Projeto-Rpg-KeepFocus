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
 * Classe que representa um personagem, tendo suas caracteristicas:
 * (id, nome, classe, foco, pontos de saude, dano recebido, dano recebido maximo, 
 *  situacao da vida, arma, armadura, iniciativa e dado proprio),
 * alem das ações que o mesmo pode executar durante o jogo.
 * 
 * @author Nechelley Alves
 */
public abstract class Personagem implements Comparable<Personagem>, Serializable{
    //String com o nome do personagem
    private final String nome;
    //classe do personagem esta podendo ser:
    //mago ou clerigo ou guerreiro.
    private final Classe classe;
    //foco que pode ser: 
    //Força ou Destreza ou Constituição ou Carisma
    private final Foco foco;
    //nivel de saudedo personagem, sera usado para calculo de quanto de dano o personagem aguenta
    private final int nivelDeSaude;
    //tipo de arma que o personagem usa: 
    //Pequena ou Média ou Grande
    private final Arma arma;
    //tipo de armadura que o personagem usa:
    //Nada ou Leve ou Pesada
    private final Armadura armadura;
    //dado proprio do personagem
    private final Dado d6;
    
    //quantidade dano necessario para que o personagem morra
    private final int danoRecebidoMaximo;
    //quantidade de dano recebido pelo personagem em certo momento
    private int danoRecebido;
    //situação do estado de vida do personagem com base no danoRecebido podendo ser: 
    //0-Saudável     1-Atordoado     2-Desesperado     3-Inconsciente   4-Morto
    //20% dano       40% dano        60% dano          99% dano         100% dano
    private int situacaoDeVida;
    
    //quantos pontos o personagem tem de iniciativa, que determina quem começa a ação em uma batalha
    private int iniciativa;
    //representa quantos pontos o personagem tem para gastar com acoes
    private int pontosDeAcao;
    //lista que diz quais os estados do personagem em dado momento
    private List<Estado> estados;
    //lista com os golpes que o personagem pode usar
    private List<Golpe> golpes;
    //lista de XBuffs
    private List<XBuff> xbuffs;
    
    
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
    public Personagem(String nome, Classe classe, Foco pontoForte, Arma arma, Armadura armadura){
            if(!verificaNome(nome))
                throw new InfInvalidoException("Nome",nome);
            this.nome = nome;
            this.classe = classe;
            this.foco = pontoForte;
            
            this. nivelDeSaude = calculaPontoSaude();
            
            danoRecebido = 0;
            danoRecebidoMaximo = nivelDeSaude*5;
            situacaoDeVida = 0;
                    
            this.arma = arma;
            this.armadura = armadura;
            
            iniciativa = 0;
            
            estados = new ArrayList<Estado>();
            golpes = new ArrayList<Golpe>();
            xbuffs = new ArrayList<XBuff>();
            
            d6 = new Dado(6);
            
            pontosDeAcao = 2;
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
     * Define os pontos de saúde do personagem de acordo com a classe e ponto forte.
     * @return quantos pontos o personagem tera de pontos de saude
     */
    private int calculaPontoSaude() {
        //Pontos de saúde padrão para as classes não listadas
        int ps = 4;
        
        if(classe == Classe.GUERREIRO || classe == Classe.OGRO)
            ps = 3;
        else if (classe == Classe.GOBLIN || classe == Classe.CULTISTA ||
                classe == Classe.ZUMBI)
            ps = 2;
        
        if(foco == Foco.CONSTITUICAO)//isso é para o caso do personagem tiver ponto forte em constituição
            ps++;
        
        return ps;
    }
    
    /**
     * Construtor que retorna um clone do Personagem p
     * 
     * @param p Personagem que sera clonado
     */
    public Personagem(Personagem p){
        this.nome = p.nome;
        this.classe = p.classe;
        this.foco = p.foco;
        this.nivelDeSaude = p.nivelDeSaude;
        this.danoRecebido = p.danoRecebido;
        this.danoRecebidoMaximo = p.danoRecebidoMaximo;
        this.situacaoDeVida = p.situacaoDeVida;
        this.arma = p.arma;
        this.armadura = p.armadura;
        this.iniciativa = p.iniciativa;
        this.golpes = p.golpes;
        this.estados = p.estados;
        this.xbuffs = p.xbuffs;
        this.d6 = p.d6;
        this.pontosDeAcao = p.pontosDeAcao;
    }
    
    
    
    //ACAO ESQUIVAR
    /**
     * Faz com que o personagem entre em estado de esquiva
     */
    public void esquivar(){
        if(pontosDeAcao <= 0)
            throw new AcaoInvalidaException("esquivar",0);
        pontosDeAcao--;
        estados.add(Estado.ESQUIVANDO);
    }
    
    
    
    //ACAO DEFENDER
    /**
     * Faz com que o personagem entre em estado de defesa
     */
    public void defender(){
        if(pontosDeAcao <= 0)
            throw new AcaoInvalidaException("defender",0);
        pontosDeAcao--;
        estados.add(Estado.DEFENDENDO);
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
        for(Estado d : estados){
            if(d == Estado.ESQUIVANDO){
                alvo.estados.remove(d);
                //o alvo roda dois dados
                int dado1chance = alvo.rodarDado();
                int dado2chance = alvo.rodarDado();

                if(dado1chance + dado2chance > 9){//se a soma dos dados for maior que 9, 27% de chance disso ocorrer
                    return relatorio + " porém o alvo se esquivou e portanto nao recebeu dano.";
                }
                break;
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
            if(foco == Foco.FORCA)
                dano++;

            //reduzo o dano de acordo com a armadura do alvo
            dano -= alvo.armadura.getId();
            
            //garantia que o dano sera no minimo zero
            if(dano < 0)
                dano = 0;
            
            //modificador se o alvo esta defendendo
            for(Estado d : estados){
                if(d == Estado.DEFENDENDO){
                    alvo.estados.remove(d);
                    dano *= 0.5;//ou seja, reduzo o dano na metade
                    alvo.receberDano(dano,g);
                    return relatorio + " porem o alvo se defendeu e recebeu apenas " + dano + " de dano.";
                }else{
                    alvo.receberDano(dano,g);
                    return relatorio + " causando " + dano + " de dano.";
                }
            }
        }
        return relatorio;
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
     * @return Golpe procurado ou null se nao encontrar
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
        
        //atualizo a situação de vida do alvo
        defineSituacaoDeVida();
    }
    
    /**
     * Faz com que o personagem sinta os efeitos dos xbuffs
     * 
     * @return String dizendo oque ocorreu com o personagem
     */
    public String receberXBuffs(){
        if(xbuffs.isEmpty()){
            return null;
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
                estados.add(Estado.CONGELADO);
                x.rodadaPassou();
                //se a duracao zerou o xbuff acabou
                if(x.getDuracao() == 0){
                    estados.add(Estado.CONGELADO);
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
    
    /**
     * Diz se uma classe é ou nao magica
     * 
     * @return true se sim, false se nao
     */
    public boolean verificaClasseMagica() {
        return (classe == Classe.MAGO ||
                classe == Classe.CLERIGO||
                classe == Classe.CULTISTA ||
                classe == Classe.DRAGAO);
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeMagicoBasico(String nome){
        if(!verificaClasseMagica())
            throw new InfInvalidoException("Classe",classe.getString());
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
            throw new InfInvalidoException("Classe",classe.getString());
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
            throw new InfInvalidoException("Classe",classe.getString());
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
            throw new InfInvalidoException("Classe",classe.getString());
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
            throw new InfInvalidoException("Classe",classe.getString());
        if(getGolpePeloNome(nome) != null)
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeMagicoNevasca(nome));
    }
    
    //GETS
    
    public abstract boolean getEhHeroi();
    
    public String getNome(){
        return nome;
    }
    
    public Classe getClasse(){
        return classe;
    }
    
    public String getPontoForte(){
        return pontoForteString();
    }
    
    public int getNivelDeSaude(){
        return nivelDeSaude;
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
    
    public Arma getArma(){
        return arma;
    }
    
    public Armadura getArmadura(){
        return armadura;
    }
    
    public int getIniciativa(){
        return iniciativa;
    }
    
    public int getPontosDeAcao(){
        return pontosDeAcao;
    }
    
    /**
     * Reseta os pontos para ficarem iguais a 2 no inicio do turno
     */
    public void resetarPontosDeAcao(){
        this.pontosDeAcao = 2;
    }
    
    public int getNumGolpes() {
        return golpes.size();
    }
    
    /**
     * Retorna o nome do golpe na posicao pos da lista
     * 
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
     * Retorna se o personagem esta vivo(true) ou morto(false)
     * 
     * @return Boolean com true se vivo e false se morto
     */
    public boolean estaVivo(){
        return situacaoDeVida != 4;
    } 
    
    /**
     * @return true se esta defendendo, false se nao
     */
    public boolean getEstaDefendendo(){
        for(Estado d : estados){
            if(d == Estado.DEFENDENDO){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return true se esta esquivando, false se nao
     */
    public boolean getEstaEsquivando(){
        for(Estado d : estados){
            if(d == Estado.ESQUIVANDO){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return true se esta congelado, false se nao
     */
    public boolean getEstaCongelado(){
        for(Estado d : estados){
            if(d == Estado.CONGELADO){
                return true;
            }
        }
        return false;
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
    
    /**
     * Seta a iniciativa, considerando que esta recebendo o valor de um dado d6,
     * na implementacao é somado +1 de iniciativa se o personagem tiver ponto forte em destreza
     * 
     * @param iniciativa valor do dado
     */
    public void setIniciativa(int iniciativa){
        if(iniciativa < 0 || iniciativa > 6)
            throw new InfInvalidoException("Pontos de ação",String.valueOf(pontosDeAcao));
        if(foco == Foco.DESTREZA)
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
        return foco.getString();
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
