package rpg;

import rpg.batalhas.Batalha;
import rpg.excecoes.AcaoInvalidaException;
import rpg.excecoes.InfInvalidoException;
import java.io.Serializable;
import rpg.personagens.Dragao;
import rpg.personagens.Ogro;
import rpg.personagens.Inimigo;
import rpg.personagens.Zumbi;
import rpg.personagens.Esqueleto;
import rpg.personagens.Cultista;
import rpg.personagens.Personagem;
import rpg.personagens.Heroi;
import rpg.personagens.Goblin;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import rpg.personagens.Arma;
import rpg.personagens.Armadura;
import rpg.personagens.Classe;
import rpg.personagens.Foco;
import rpg.personagens.SituacaoDeVida;

/**
 *
 * @author Nechelley Alves
 */
public class Jogo implements Serializable{
    //lista com os herois
    private final List<Personagem> timeDosHerois;
    //pontuacao do jogador no jogo, representa quantas partidas o time de herois venceu
    private int pontuacao;
    //Batalha Atual que esta ocorrendo
    private Batalha batalhaAtual;
    //determina a fase em que o jogo esta
    private SituacaoDoJogo situacaoDoJogo;
    
    
    //objeto que esta ouvindo o jogo
    private ObservadorJogo tela;
    //arquivo que salva e carrega o jogo
    private Arquivo arquivo;
    
    //nível de habilidade Do Time De Herois
    private int habilidadeDoTimeDeHerois;
    //nível de habilidade Do Time De Inimigos 
    private int habilidadeDoTimeDeInimigos;
    
    
    /**
     * Cria um novo jogo
     * 
     * @param tela Objeto da classe que implementar ObservadorJogo
     */
    public Jogo(ObservadorJogo tela){
        pontuacao = 0;
        habilidadeDoTimeDeHerois = 0;
        habilidadeDoTimeDeInimigos = 0;
        batalhaAtual = null;
        situacaoDoJogo = SituacaoDoJogo.CRIANDOHEROIS;
                
        timeDosHerois = new ArrayList<Personagem>();
        if(tela == null)
            throw new InfInvalidoException("Tela","NULL");
        this.tela = tela;
    }

    
    
    
    //JOGO EM SI
    /**
     * Metodo principal contendo a principal logica do jogo
     */
    public void iniciar(){
        //primeiramente o jogador cria o time de heroi com que ele jogara a partida
        criandoTimeDeHerois();
        
        //inicio as rodadas de batalhas
        tela.antesDeIniciarRodadaDeBatalhas();
        situacaoDoJogo = SituacaoDoJogo.BATALHASCOMECARAM;
        
        while(situacaoDoJogo != SituacaoDoJogo.ACABOU){
            //comeca a batalha atual
            iniciarBatalha();
            
            //Apresento os inimigos
            tela.apresentandoInimigos(matrizInimigos());
            
            //Inicio o turno atual
            tela.IniciandoTurnos();

            // situaçao de todos os personagens
            tela.exibindoInformacoesResumidasDeTodosOsLutadores(matrizLutadores());
             
            //inicio do loop da batalha atual
            while(!batalhaAtual.verificaSeBatalhaAcabou()){//enquanto a batalha estiver ocorrendo, ou seja, nao acabou
                
                tela.exibindoTurnoAtual();
                            
                //começo o loop do turno
                boolean flagDoTurno = true;
                
                //loop do Turno
                for(int i = 0; i < batalhaAtual.getNumLutadores() && flagDoTurno; i++){
                    String relatorio = new String();
                    
                    Personagem lutadorAtual = batalhaAtual.getLutadores().get(i);
                    String nomeLutador = lutadorAtual.getNome();
                    
                    tela.antesDeExibirVezDoLutador(nomeLutador);
                    
                    //verifico se o lutador esta em condicao de lutar
                    if(lutadorAtual.getSituacaoDeVida() == SituacaoDeVida.INCONSCIENTE || 
                            lutadorAtual.getSituacaoDeVida() == SituacaoDeVida.MORTO){
                        
                        relatorio = nomeLutador + " não esta em condição de executar qualquer ação.";
                        tela.exibindoRelatorio(relatorio);
                        continue;
                    }
                    
                    //neste ponto verifico se o lutador é um Inimigo e ativo a IA se necessario
                    if(!lutadorAtual.getEhHeroi()){
                        relatorio = executarIA(lutadorAtual);
                        tela.exibindoRelatorio(relatorio);
                        
                        if(batalhaAtual.verificaSeBatalhaAcabou()){
                            //se entrou neste if quer dizer que a batlaha acabou no turno de um inimigo, portanto os herois perderam
                            flagDoTurno = false;
                            tela.encerramentoDaBatalha();
                            encerrarJogo();
                            tela.exibirFim();
                        }
                        continue;
                    }
                    
                    //personagem escolhe a acao que deseja executar
                    escolherAcao(i);
                    
                    //escrever o que aconteceu, como se desenrolou a acao
                    if(!getBatalhaEstaOcorrendo()){
                        flagDoTurno = false;
                        tela.encerramentoDaBatalha();
                        ganharPonto();
                        tela.exibirFim();
                        int s = tela.antesDeSair(false);
                        if(s == 0)
                            encerrarJogo();
                        else if (s == 2){
                            salvarJogo();
                            s = tela.antesDeSair(true);
                            if(s == 0)
                                encerrarJogo();
                        }
                    }else{
                        tela.exibindoInformacoesResumidasDeTodosOsLutadores();
                    }
                    
                }//fecha loop do Turno
                
                if (getBatalhaEstaOcorrendo())
                    tela.exibindoRelatorio(passarTurno());
            }//fecha loop da batalha
        }//fecha loop do jogo
    }
    private void criandoTimeDeHerois(){
        int op;
        do{
            String[] heroiInf = tela.lendoHeroi();
            Personagem p = adicionarHeroi(heroiInf[0], heroiInf[1], heroiInf[2],
                    heroiInf[3], heroiInf[4]);
            
            //loop para adicionar os golpes do personagem
            int opg;
            do{
                String[] golpeInf = tela.lendoGolpe();
                int classe = Integer.parseInt(golpeInf[0]);
                String nomeGolpe = golpeInf[1];
                switch(classe){
                    case 1:
                        p.addGolpeFisico(nomeGolpe);
                        break;
                    case 2:
                        p.addGolpeMagicoBasico(nomeGolpe);
                        break;
                    case 3:
                        p.addGolpeMagicoBolaDeFogo(nomeGolpe);
                        break;
                    case 4:
                        p.addGolpeMagicoMeteoro(nomeGolpe);
                        break;
                    case 5:
                        p.addGolpeMagicoLancaDeGelo(nomeGolpe);
                        break;
                    case 6:
                        p.addGolpeMagicoNevasca(nomeGolpe);
                        break;
                }
                opg = tela.confirmarSeTemMaisGolpesDeHerois();
            }while(opg != 2);
            op = tela.confirmarSeTemMaisHerois();
        }while(op != 2);
    }
    /**
     * Matriz de Inimigos na ordem
     * {nome,classe,foco,nivelDeSaude,danoRecebido,danoRecebidoMaximo,situacaoDeVida,
     * Arma,Armadura,Iniciativa}
     * 
     * @return String[][] com as informacoes dos inimigos
     */
    private String[][] matrizInimigos(){
        String[][] inf = null;
        int cont = 0;
        for(Personagem p : batalhaAtual.getInimigos()){
            inf[cont][0] = p.getNome();
            inf[cont][1] = p.getClasse().getString();
            inf[cont][2] = p.getFoco().getString();
            inf[cont][3] = String.valueOf(p.getNivelDeSaude());
            inf[cont][4] = String.valueOf(p.getDanoRecebido());
            inf[cont][5] = String.valueOf(p.getDanoRecebidoMaximo());
            inf[cont][6] = p.getSituacaoDeVida().getString();
            inf[cont][7] = p.getArma().getString();
            inf[cont][8] = p.getArmadura().getString();
            inf[cont][9] = String.valueOf(p.getIniciativa());
            cont++;
        }
        return inf;
    }
    /**
     * Matriz de Lutadores ordem
     * {defendendo,esquivando,nome,danoRecebido,danoRecebidoMaximo,situacaoDeVida,pontosDeAcao}
     * 
     * @return String[][] com as informacoes dos lutadores
     */
    private String[][] matrizLutadores(){
        String[][] inf = null;
        int cont = 0;
        for(Personagem p : batalhaAtual.getLutadores()){
            if(p.getEstaDefendendo())
                inf[cont][0] = " (defendendo)";
            else
                inf[cont][0] = "";
            if(p.getEstaEsquivando())
                inf[cont][1] = " (esquivando)";
            else
                inf[cont][1] = "";
            
            inf[cont][2] = p.getNome();
            inf[cont][3] = String.valueOf(p.getDanoRecebido());
            inf[cont][4] = String.valueOf(p.getDanoRecebidoMaximo());
            inf[cont][5] = p.getSituacaoDeVida().getString();
            inf[cont][6] = String.valueOf(p.getPontosDeAcao());
            cont++;
        }

        return inf;
    }
    /**
     * metodo para escolher o heroi escolher sua acao
     * @param i pos do lutador
     */
    public void escolherAcao(int i){
        //loop do Personagem
        boolean flagPersonagem = true;//flag para a acao do personagem
        String relatorio = new String();
        while(flagPersonagem){
            //Personagem escolhe sua acao
            int acao = tela.antesDeEscolherAcao(i);
            
            switch(acao){
                // Ataque
                case 1:
                    try{
                        relatorio = atacar(i);
                    }
                    catch(AcaoInvalidaException e){
                        relatorio = e.getMessage() + " " + e.getMotivo();
                    }
                    break;
                // Defender
                case 2:
                    if(lutadorGetEstaDefendendo(i))
                        relatorio = " Acão inválida!";
                    else
                        relatorio = defender(i);
                    break;
                //Esquivar
                case 3:
                    if(lutadorGetPontoForte(i).equals("Destreza") && !lutadorGetEstaEsquivando(i))
                        relatorio = esquivar(i);
                    else
                        relatorio = " Acão inválida!";
                    break;
                case 5:
                    relatorio = lutadorGetNome(i) + " encerrou sua vez.";
                    flagPersonagem = false;//sai do loop de personagens
                    break;
                case 6:
                    tela.exibindoInformacoesResumidasDeTodosOsLutadores();
                    break;
                default:
                    relatorio = " Acão inválida!"; 
            }//fecha switch(acao)
            tela.exibindoRelatorio(relatorio);
            
            if(lutadorGetPontosDeAcao(i) == 0 || !getBatalhaEstaOcorrendo())
                flagPersonagem = false;//sai do loop de personagens
        }///fecha loop do Personagem
    }
    
    /**
     * esquiva
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de esquivar
     */
    public String esquivar(Personagem lutador){
        getLutadorNaPosicao(i).esquivar();
        return (lutadorGetNome(i) + " podera tentar esquivar do proximo ataque que receber.");
    }
    
    /**
     * defendender
     * @param lutador lutador na lista de lutadores da batalha
     * @return 
     */
    public String defender(Personagem lutador){
        getLutadorNaPosicao(i).defender();
        return (lutadorGetNome(i) + " podera se defender do proximo ataque que receber.");
    }
    
    public String atacar(Personagem lutador){
        //ler informações necessarias para executar a acao atacar
        
        if(lutadorGetPontosDeAcao(i) == 0){
            return "Voce nao possui pontos de acao suficiente para esse problema";
        }
        
        //escolhendo alvo
        String alvoNome = tela.escolhendoAlvo(i);

        //escolhendo o golpe
        String golpeNome = tela.escolhendoGolpe(i);
        
        //agora que ja foi recebido as informações necessarias, executa-se a ação atacar
        return lutadorAtacaAlvo(i,alvoNome,golpeNome);
    }
    
    

    
    
    //PONTUACAO
    /**
     * Aumenta a pontuacao em um ponto, deve ser usado apos uma vitoria de batalha
     */
    public void ganharPonto(){
        pontuacao++;
    }
    
    /**
     * Retorna o valor da pontuacao atual
     * 
     * @return int com a pontuacao atual
     */
    public int getPontuacao(){
        return pontuacao;
    }
    
    
    
    
    
    
    //COISAS DE HEROI
    
    /**
     * Cria e adiciona um heroi ao time dos herois do jogo
     * 
     * @param nome String com o nome do Heroi
     * @param classe String com qual sera a classe do Heroi
     * @param foco String com qual sera o ponto forte do Heroi
     * @param arma String dizendo qual o tipo de arma o Heroi usara
     * @param armadura String com qual o tipo de armadura o Heroi usara
     * @return Personagem que acabou de ser criado e adicionado ao time dos herois
     */
    public Personagem adicionarHeroi(String nome, String classe, String foco, String arma, String armadura){
        if(situacaoDoJogo != SituacaoDoJogo.CRIANDOHEROIS)
            throw new AcaoInvalidaException("adicionar heroi",2);
        
        Classe classee = Classe.getClassePorId(Integer.parseInt(classe));
        Foco focoo = Foco.getFocoPorId(Integer.parseInt(foco));
        Arma armaa = Arma.getArmaPorId(Integer.parseInt(arma));
        Armadura armaduraa = Armadura.getArmaduraPorId(Integer.parseInt(armadura));
        
        Personagem p = new Heroi(nome, classee, focoo, armaa, armaduraa);
        
        //adiciona a habilidadeDoTimeDeHerois do heroi no índice de habilidadeDoTimeDeHerois total
        habilidadeDoTimeDeHerois += p.getNivelDeSaude() + armaa.getValor() + armaduraa.getValor();
        
        timeDosHerois.add(p);
        
        return p;
    }
    
    
    
    
    
    /**
     * Retorna o vencedor da batalha atual
     * 
     * @return String dizendo quem venceu
     */
    public String getVencedores(){
        if(getBatalhaEstaOcorrendo())
            return "A Batalha ainda não acabou.";
        return batalhaAtual.getVencedores();
    }
     
    
    /**
     * Inicia uma batalha, criando ela e escolhendo aleatoriamente os inimigos que participarao
     * da mesma.
     */
    public void iniciarBatalha(){
        //escolhendo inimigos
        List<Personagem> inimigos = new ArrayList<Personagem>();
        
        Random aleatorio = new Random();
        //Adiciona um número de inimigos na lista
        while (habilidadeDoTimeDeInimigos < habilidadeDoTimeDeHerois) {
            int pos = aleatorio.nextInt(Inimigo.numDeClassesDeInimigos());
            if(pos < 0 || pos > Inimigo.numDeClassesDeInimigos() -1)
                throw new IndexOutOfBoundsException("Posição " + pos + " não existe em inimigos.");
            inimigos.add(adicionarInimigo(pos));
        }
        
        if (inimigos.isEmpty())
            throw new RuntimeException("Nenhum inimigo adicionado na batalha.");
        
        batalhaAtual = new Batalha(timeDosHerois, inimigos);
        batalhaAtual.iniciarBatalha();
    }
    /**
     * Define um inimigo a ser adicionado a partir de um número de índice
     * 
     * @param id Int com o índice do inimigo na lista
     * @return Inimigo a ser adicionado
     */
    private Inimigo adicionarInimigo(int id) {
        Inimigo i = null;
        Random aleatorio = new Random();
        Arma arma = null;
        Armadura armadura = null;
        Foco pontoForte = Foco.getFocoPorId(aleatorio.nextInt(4));
        switch (id) {
            case 0:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Goblin(pontoForte,arma,armadura);
                i.addGolpeFisico("Facada");
                break;
            case 1:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Cultista(pontoForte,arma,armadura);
                i.addGolpeFisico("Facada");
                i.addGolpeMagicoMeteoro("Invocacao");
                break;
            case 2:
                arma = Arma.getArmaPorId(aleatorio.nextInt(3));
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2));
                i = new Dragao(pontoForte,arma,armadura);
                i.addGolpeFisico("Garras");
                i.addGolpeMagicoBolaDeFogo("Bola de fogo");
                break;
            case 3:
                arma = Arma.getArmaPorId(aleatorio.nextInt(3));
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2));
                i = new Ogro(pontoForte,arma,armadura);
                i.addGolpeFisico("Machadada");
                break;
            case 4:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Esqueleto(pontoForte,arma,armadura);
                i.addGolpeFisico("Espadada");
                break;
            case 5:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Zumbi(pontoForte,arma,armadura);
                i.addGolpeFisico("Arranhao");
                break;
            default:
                break;
        }
        
        if (i == null){
            throw new RuntimeException("Inimigo não adicionado, ID inválida");
        }
        
        habilidadeDoTimeDeInimigos += i.getNivelDeSaude() + arma.getValor() + armadura.getValor();
        return i;
        
    }
    
    /**
     * Encerra o jogo 
     */
    public void encerrarJogo(){
        situacaoDoJogo = SituacaoDoJogo.ACABOU;
    }  
    
    
    
    //QUESTAO DOS ALVOS
    
    /**
     * Executa a IA dos inimigos
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return String dizendo oque aconteceu na acao
     */
    public String executarIA(Personagem lutador){
        Classe classeLutador = lutador.getClasse();
        SituacaoDeVida situacaoLutador = lutador.getSituacaoDeVida();
        int alvoPos = 0;
        
        if (situacaoLutador == SituacaoDeVida.SAUDAVEL) {
            //Define a estratégia de luta de acordo com o lutador
            switch(classeLutador){
                case Classe.DRAGAO:
                    alvoPos = definirAlvoMaisForte(lutador);
                    break;
                case Classe.OGRO:
                case Classe.GOBLIN:
                    if (lutador.rodarDado() > 5) {
                        return defender(lutador);
                    } else {
                        alvoPos = definirAlvoMaisFraco(lutador);
                    }
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(lutador);
                    } else {
                        alvoPos = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        } else if (situacaoLutador == SituacaoDeVida.ATORDOADO) {
            switch (classeLutador) {
                case "Dragao":
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        return defender(lutador);
                    else if (chance > 2)
                        alvoPos = definirAlvoMaisForte(lutador);
                    else
                        alvoPos = definirAlvoAleatorio(lutador);
                    break;
                case "Ogro":
                case "Goblin":
                    if (lutador.rodarDado() > 3)
                        alvoPos = definirAlvoMaisForte(lutador);
                    else
                        alvoPos = definirAlvoAleatorio(lutador);
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(lutador);
                    } else {
                        alvoPos = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        } else {
            switch (classeLutador) {
                case "Dragao":
                    if (lutador.rodarDado() > 4)
                        alvoPos = definirAlvoMaisForte(lutador);
                    else
                        alvoPos = definirAlvoAleatorio(lutador);
                    break;
                case "Ogro":
                    if (lutador.rodarDado() == 6)
                        alvoPos = definirAlvoMaisForte(lutador);
                    else
                        alvoPos = definirAlvoAleatorio(lutador);
                    break;
                case "Goblin":
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        alvoPos = definirAlvoMaisForte(lutador);
                    else if (chance > 3)
                        return defender(lutador);
                    else
                        alvoPos = definirAlvoAleatorio(lutador);
                    break;
                default:
                    if (lutador.rodarDado() > 2) {
                        return defender(lutador);
                    } else {
                        alvoPos = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        }
        
        Random aleatorio = new Random();
        int golpe = aleatorio.nextInt(lutador.getNumGolpes());
        String alvoNome = possivelAlvoLutadorGetNome(pos,alvoPos);
        return lutadorAtacaAlvo(pos,alvoNome,lutador.getGolpe(golpe));
    }
    
    /**
     * Define um alvo aleatório a ser atacado.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoAleatorio(Personagem lutador) {
        //meu valores para j variam de 0 ate getNumPossiveisAlvos(i)
        Random random = new Random();
        return random.nextInt(getNumPossiveisAlvos(pos));
    }
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoMaisFraco(Personagem lutador) {
        Personagem atacante = getLutadorNaPosicao(pos);
        
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        //Percorre a lista verificando qual é o alvo com maior dano
        int maiorDano = alvos.get(0).getDanoRecebido();
        int posAlvo = 0;
        for (int i = 1;i < alvos.size();i++) {
            String situacaoAlvo = alvos.get(i).getSituacaoDeVida();
            
            //Verifica se o dano do alvo é menor e se o alvo não está incapaz
            if (maiorDano < alvos.get(i).getDanoRecebido() &&
                    !situacaoAlvo.equals("Inconsciente") &&
                    !situacaoAlvo.equals("Morto")) {
                maiorDano = alvos.get(i).getDanoRecebido();
                posAlvo = i;
            }
        }
        
        return posAlvo;
    }    
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoMaisForte(Personagem lutador) {
        Personagem atacante = getLutadorNaPosicao(pos);
        
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        //Percorre a lista verificando qual é o alvo com menor dano
        int menorDano = alvos.get(0).getDanoRecebido();
        int posAlvo = 0;
        for (int i = 1;i < alvos.size();i++) {
            if (menorDano > alvos.get(i).getDanoRecebido()) {
                menorDano = alvos.get(i).getDanoRecebido();
                posAlvo = i;
            }
        }
        
        return posAlvo;
    }
    
    
    
    /**
     * Retorna o nome do possivel alvo de um atacante
     * 
     * @param posDoAtacante int com a posicao do lutador atacante na lista de todos os lutadores
     * @param posDoAlvo int com a posaicao do alvo na lista de alvos
     * @return String com o nome do possivel alvo
     */
    public String possivelAlvoLutadorGetNome(int posDoAtacante, int posDoAlvo){
        Personagem atacante = getLutadorNaPosicao(posDoAtacante);
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        if(posDoAlvo < 0 || posDoAlvo > alvos.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + posDoAlvo + " não existe em alvos.");
        return alvos.get(posDoAlvo).getNome();
    }
    
    /**
     * Aciona a acao atacar do lutador na posicao posAtacante e seleciona como alvo
     * deste ataque o lutador com o nome de alvoNome
     * 
     * @param posAtacante int com a posicao do lutador atacante na lista de todos os lutadores
     * @param nomeAlvo String com o nome do alvo
     * @param golpe String com o nome do golpe sendo executado
     * @return String dizendo oque aconteceu no ataque
     */
    public String lutadorAtacaAlvo(int posAtacante, String nomeAlvo, String golpe){
        Personagem atacante = getLutadorNaPosicao(posAtacante);
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        Personagem alvo = null;
        for(int i = 0; i < alvos.size(); i++){
            if(nomeAlvo.equals(alvos.get(i).getNome()))
                alvo = alvos.get(i);
        }
        
        return atacante.atacar(alvo,golpe);
    }
    
    
    
    
    
    
    
    //GETS DE QUANTIDADE
    
    /**
     * Retorna a quantidade de alvos do lutador na posição i
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a quantidade de possiveis alvo do lutador
     */
    public int getNumPossiveisAlvos(int pos){
        return batalhaAtual.getNumPossiveisDeAlvos(getLutadorNaPosicao(pos));
    }
    
    
    
    
    //GETS DAS INFORMACOES DOS LUTADORES
    
    /**
     * Metodo auxiliar que dada uma posicao ele retorna o lutador daquela posicao 
     * na lista de todos os lutadores
     * 
     * @param pos int com a posicao do lutador
     * @return Personagem que é o lutador
     */
    private Personagem getLutadorNaPosicao(int pos){
        List<Personagem> list = batalhaAtual.getLutadores();
        if(pos < 0 || pos > list.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe nos lutadores.");
        return list.get(pos);
    }
    
    /**
     * Retorna o nome do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com o nome do lutador
     */
    public String lutadorGetNome(int pos){
        return getLutadorNaPosicao(pos).getNome();
    }
    
    /**
     * Retorna o dano recebido do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido
     */
    public int lutadorGetDanoRecebido(int pos){
       return getLutadorNaPosicao(pos).getDanoRecebido();
    }
    
    /**
     * Retorna o dano recebido maximo do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido maximo
     */
    public int lutadorGetDanoRecebidoMaximo(int pos){
        return getLutadorNaPosicao(pos).getDanoRecebidoMaximo();
    }
    
    /**
     * Retorna a situacao de vida do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com a situacao de vida 
     */
    public String lutadorGetSituacaoDeVida(int pos){
        return getLutadorNaPosicao(pos).getSituacaoDeVida();
    }
    
    /**
     * Retorna se o jogador esta ou nao em condicao de executar ações
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return boolean onde (true)-esta em condicao ou (false)-nao esta e condicao
     */
    public boolean lutadorEstaEmCondicao(int pos){
        Personagem p = getLutadorNaPosicao(pos);
        if(p.getSituacaoDeVida().equals("Inconsciente") || p.getSituacaoDeVida().equals("Morto")){
            return false;
        }
        return true;
    }
    
    /**
     * Retorna se um lutador na posicao pos é ou nao heroi
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return boolean onde (true)-se é heroi ou (false)-caso seja inimigo
     */
    public boolean lutadorGetEhHeroi(int pos){
        return getLutadorNaPosicao(pos).getEhHeroi();
    }
    
    /**
     * Retorna a quantidade de pontos de acao de um lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a quantidade de pontos de acao
     */
    public int lutadorGetPontosDeAcao(int pos){
        return getLutadorNaPosicao(pos).getPontosDeAcao();
    }
    
    /**
     * Retorna quantos golpes tem um lutador na posicao pos
     * 
     * @param pos int com a posicao
     * @return int com num de golpes
     */
    public int lutadorGetNumGolpes(int pos){
        return getLutadorNaPosicao(pos).getNumGolpes();
    }
    
    /**
     * Retorna um golpe de um personagem
     * 
     * @param pos int com a posicao do personagem na lista
     * @param posG int com a posicao do golpes procurado
     * @return String com o nome do golpe
     */
    public String lutadorGetGolpe(int pos, int posG){
        return getLutadorNaPosicao(pos).getGolpe(posG);
    }
    
    /**
     * Retorna o custo do golpe de um personagem
     * 
     * @param pos int com a posicao do personagem na lista
     * @param posG int com a posicao do golpes procurado
     * @return int com o custo do golpe
     */
    public int lutadorGetGolpeCusto(int pos, int posG){
        return getLutadorNaPosicao(pos).getGolpeCusto(posG);
    }
    
    /**
     * Retorna se o lutador esta esquivando
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta esquivando
     */
    public boolean lutadorGetEstaEsquivando(int pos){
        return getLutadorNaPosicao(pos).getEstaEsquivando();
    }
    
    /**
     * Retorna se o lutador esta defendendo
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta defendendo
     */
    public boolean lutadorGetEstaDefendendo(int pos){
        return getLutadorNaPosicao(pos).getEstaDefendendo();
    }
    
    /**
     * Retorna se o lutador esta congelado
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta congelado
     */
    public boolean lutadorGetEstaCongelado(int pos){
        return getLutadorNaPosicao(pos).getEstaCongelado();
    }
    
    /**
     * Retorna o ponto forte do lutador
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna o ponto forte do lutador
     */
    public String lutadorGetPontoForte(int pos){
        return getLutadorNaPosicao(pos).getPontoForte();
    }
    
    //TURNOS
    
    /**
     * Passa o turno da batalha
     * @return String dizendo oque ocorreu
     */
    public String passarTurno(){
        List<Personagem> list = batalhaAtual.getLutadores();
        //reinicio os pontos de acao para o proximo turno //ativo os xbuffs
        String relatorio = "Aconteceu na passagem de turnos:\n";
        for(Personagem l : list){
            l.resetarPontosDeAcao();
            relatorio += l.receberXBuffs();
        }
        batalhaAtual.passarTurno();
        return relatorio;
    }
    
    /**
     * Retorna o turno atual
     * 
     * @return int com o turno atual
     */
    public int getTurno(){
        return batalhaAtual.getTurno();
    }
    
}
