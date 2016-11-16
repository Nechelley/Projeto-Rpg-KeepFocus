package rpg;

import rpg.batalhas.Batalha;
import rpg.excecoes.AcaoInvalidaException;
import rpg.excecoes.InfInvalidoException;
import java.io.Serializable;
import rpg.personagens.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import rpg.golpes.Golpe;
import rpg.personagens.Chefe;
import rpg.personagens.enums.Arma;
import rpg.personagens.enums.Armadura;
import rpg.personagens.enums.Classe;
import rpg.personagens.enums.Foco;
import rpg.personagens.enums.SituacaoDeVida;

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
        tela.iniciandoRodadaDeBatalhas();
        situacaoDoJogo = SituacaoDoJogo.BATALHASCOMECARAM;
        
        while(situacaoDoJogo != SituacaoDoJogo.ACABOU){
            //comeca a batalha atual
            iniciarBatalha();
            
            //Apresento os inimigos
            tela.apresentandoInimigos(matrizInimigos());
            
            //Inicio o turno atual
            tela.iniciandoTurnos();

            // situaçao de todos os personagens
            tela.exibindoSituacaoDosPersonagens(matrizLutadores());
             
            //inicio do loop da batalha atual
            while(!batalhaAtual.batalhaAcabou()){//enquanto a batalha estiver ocorrendo, ou seja, nao acabou
                
                tela.exibindoTurnoAtual(batalhaAtual.getTurno());
                            
                //começo o loop do turno
                boolean flagDoTurno = true;
                
                //loop do Turno
                for(int i = 0; i < batalhaAtual.getNumLutadores() && flagDoTurno; i++){
                    String relatorio = new String();
                    
                    Personagem lutador = batalhaAtual.getLutadores().get(i);
                    String nomeLutador = lutador.getNome();
                    
                    tela.exibindoVezDoLutador(nomeLutador);
                    
                    //verifico se o lutador esta em condicao de lutar
                    if(lutador.getSituacaoDeVida() == SituacaoDeVida.INCONSCIENTE || 
                            lutador.getSituacaoDeVida() == SituacaoDeVida.MORTO){
                        
                        relatorio = nomeLutador + " não esta em condição de executar qualquer ação.";
                        tela.exibindoRelatorio(relatorio);
                        continue;
                    }

                    //neste ponto verifico se o lutador é um Inimigo e ativo a IA se necessario
                    if(!lutador.getEhHeroi()){
                        do{
                            relatorio = executarIA(lutador);
                            tela.exibindoRelatorio(relatorio);
                        }while(!lutador.getAindaPodeExecutarAlgumaAcao().isEmpty() && !batalhaAtual.batalhaAcabou());
                        
                        if(batalhaAtual.batalhaAcabou()){
                            //se entrou neste if quer dizer que a batlaha acabou no turno de um inimigo, portanto os herois perderam
                            flagDoTurno = false;
                            tela.encerramentoDaBatalha(getVencedores(), pontuacao);
                            encerrarJogo();
                        }else
                            // situaçao de todos os personagens
                            tela.exibindoSituacaoDosPersonagens(matrizLutadores());
                        continue;
                    }
                    
                    //personagem escolhe a acao que deseja executar
                    escolherAcao(lutador);
                    
                    //escrever o que aconteceu, como se desenrolou a acao
                    if(batalhaAtual.batalhaAcabou()){
                        flagDoTurno = false;
                        ganharPonto();
                        tela.encerramentoDaBatalha(getVencedores(),pontuacao);
                    }else{
                        // situaçao de todos os personagens
                        tela.exibindoSituacaoDosPersonagens(matrizLutadores());
                    }
                }//fecha loop do Turno
                
                if (!batalhaAtual.batalhaAcabou())
                    passarTurno();
            }//fecha loop da batalha
            
            //reseto os pontos de acao dos herois
            for(Personagem h : timeDosHerois){
                h.resetarPontosDeAcao();
            }
            
            //verifico se ele quer jogar outra partida
            if(!getVencedores().equals("Herois"))//garanto que a mensagem apareca apenas se o herois perderem
                if(tela.querJogarOutraPartida()){//se ele quiser jogar outra partida
                    //reseto os herois
                    for(Personagem h : timeDosHerois){
                        h.resetarStatus();
                    }
                    for(Personagem p : batalhaAtual.getInimigos()){
                        System.out.println("antes: " + p.getInstancias());
                        p.diminuiNumInstancias();
                        System.out.println("depois: " + p.getInstancias());
                    }
                        
                    situacaoDoJogo = SituacaoDoJogo.BATALHASCOMECARAM;
                }
        }//fecha loop do jogo
    }
    
    /**
     * Cria o time de herois, pedindo suas informacoes e guardando me timDosHerois
     */
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
     * Matriz de Inimigos na ordem
     * {posicaoDoInimigo}{nome,classe,foco,nivelDeSaude,danoRecebido,danoRecebidoMaximo,situacaoDeVida,
     * Arma,Armadura,Iniciativa}
     * 
     * @return String[][] com as informacoes dos inimigos
     */
    private String[][] matrizInimigos(){
        String[][] inf = new String[batalhaAtual.getInimigos().size()][10];
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
     * {posicaoDoLutador}{defendendo,esquivando,nome,danoRecebido,danoRecebidoMaximo,situacaoDeVida,pontosDeAcao}
     * 
     * @return String[][] com as informacoes dos lutadores
     */
    private String[][] matrizLutadores(){
        String[][] inf = new String[batalhaAtual.getLutadores().size()][7];
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
     * Vetor com as informacoes necessarias de um lutador para exibir o menu de 
     * escolher acao na ordem
     * {nome,pontosDeAcao,(estaDefendendo ou ""),(estaEsquivando ou "")}
     * 
     * @param lutador Personagem com as informacoes
     * @return String[] com as informacoes
     */
    private String[] vetorParaEscolherAcao(Personagem lutador){
        String[] inf = new String[4];
        inf[0] = lutador.getNome();
        inf[1] = String.valueOf(lutador.getPontosDeAcao());
        if(!lutador.getEstaDefendendo())
            inf[2] = "podeDefender";
        else
            inf[2] = "";
        if(lutador.getFoco() == Foco.DESTREZA && !lutador.getEstaEsquivando())
            inf[3] = "podeEsquivar";
        else
            inf[3] = "";
        
        return inf;
    }
    /**
     * Vetor com as informacoes necessarias de um lutador para exibir o menu de 
     * escolher alvo, vem com todos os nomes de alvos
     * 
     * @param lutador Personagem com as informacoes
     * @return String[] com as informacoes
     */
    private String[] vetorParaEscolherAlvo(Personagem lutador){
        List<Personagem> lista = batalhaAtual.getPossiveisAlvos(lutador);
        String[] inf = new String[lista.size()];
        int cont = 0;
        for(Personagem p : lista){
            inf[cont] = p.getNome();
            cont++;
        }
        return inf;
    }
    /**
     * Matriz com as informacoes dos golpes na ordem
     * {posicaoDoGolpe}{nome,custo}
     * 
     * @lutador Personagem para se adquirir as informacoes
     * @return String[][] com as informacoes dos golpes
     */
    private String[][] matrizParaEscolherGolpe(Personagem lutador){
        List<Golpe> lista =lutador.getGolpes();
        String[][] inf = new String[lista.size()][2];
        int cont = 0;
        for(Golpe g : lista){
            inf[cont][0] = g.getNome();
            inf[cont][1] = String.valueOf(g.getCustoDeAcao());
            cont++;
        }
        return inf;
    }
    
    
    
    
    /**
     * metodo para escolher o heroi escolher sua acao
     * @param lutador pos do lutador
     */
    public void escolherAcao(Personagem lutador){
        //loop do Personagem
        boolean flagPersonagem = true;//flag para a acao do personagem
        String relatorio = new String();
        while(flagPersonagem){
            //Personagem escolhe sua acao
            int acao = tela.escolhendoAcao(vetorParaEscolherAcao(lutador));
            
            switch(acao){
                // Ataque
                case 1:
                    relatorio = atacar(lutador);
                    break;
                // Defender
                case 2:
                    relatorio = defender(lutador);
                    break;
                //Esquivar
                case 3:
                    relatorio = esquivar(lutador);
                    break;
                case 4:
                    relatorio = lutador.getNome() + " encerrou sua vez.";
                    flagPersonagem = false;//sai do loop de personagens
                    break;
                case 5:
                    tela.exibindoSituacaoDosPersonagens(matrizLutadores());
                    break;
                default:
                    relatorio = " Acão inválida!"; 
            }//fecha switch(acao)
            tela.exibindoRelatorio(relatorio);
            
            if(lutador.getAindaPodeExecutarAlgumaAcao().isEmpty() || batalhaAtual.batalhaAcabou())
                flagPersonagem = false;//sai do loop de personagens
        }///fecha loop do Personagem
    }
    /**
     * Executa a cao de esquivar do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de esquivar
     */
    public String esquivar(Personagem lutador){
        if(!(lutador.getFoco() == Foco.DESTREZA && !lutador.getEstaEsquivando()))
            return " Acão inválida!";
        lutador.esquivar();
        return (lutador.getNome() + " podera tentar esquivar do proximo ataque que receber.");
    }
    /**
     * Executa a cao de defender do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de defender
     */
    public String defender(Personagem lutador){
        if(lutador.getEstaDefendendo())
            return " Acão inválida!";
        lutador.defender();
        return (lutador.getNome() + " podera se defender do proximo ataque que receber.");
    }
    /**
     * Executa a cao de atacado lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de atacar
     */
    public String atacar(Personagem lutador){
        //ler informações necessarias para executar a acao atacar
        
        if(lutador.getPontosDeAcao() == 0){
            return "Voce nao possui pontos de acao suficiente para esse problema";
        }
        
        //escolhendo alvo
        String alvoNome = tela.escolhendoAlvo(vetorParaEscolherAlvo(lutador));

        //escolhendo o golpe
        String golpeNome = tela.escolhendoGolpe(matrizParaEscolherGolpe(lutador));
        
        //agora que ja foi recebido as informações necessarias, executa-se a ação atacar
        return lutadorAtacaAlvo(lutador,alvoNome,golpeNome);
    }
    
    
    /**
     * Aciona a acao atacar do lutador na posicao posAtacante e seleciona como alvo
     * deste ataque o lutador com o nome de alvoNome
     * 
     * @param atacante Personagem que esta atacando
     * @param nomeAlvo String com o nome do alvo
     * @param golpe String com o nome do golpe sendo executado
     * @return String dizendo oque aconteceu no ataque
     */
    public String lutadorAtacaAlvo(Personagem atacante, String nomeAlvo, String golpe){
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        Personagem alvo = null;
        for(int i = 0; i < alvos.size(); i++){
            if(nomeAlvo.equals(alvos.get(i).getNome()))
                alvo = alvos.get(i);
        }
        
        return atacante.atacar(alvo,atacante.getGolpePeloNome(golpe));
    }
    
   
 
     
    
    /**
     * Inicia uma batalha, criando ela e escolhendo aleatoriamente os inimigos que participarao
     * da mesma.
     */
    public void iniciarBatalha(){
        //escolhendo inimigos
        List<Personagem> inimigos = new ArrayList<Personagem>();
        
        //Adiciona inimigos na lista
        do{
            Inimigo i = adicionarInimigo();
            if(i == null)
                break;
            inimigos.add(i);
        }while (habilidadeDoTimeDeInimigos < habilidadeDoTimeDeHerois);
        
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
    private Inimigo adicionarInimigo() {
        //inicializando
        Inimigo i = null;
        Random aleatorio = new Random();
        Arma arma = null;
        Armadura armadura = null;
        Foco foco = Foco.getFocoPorId(aleatorio.nextInt(4));
        int id = aleatorio.nextInt(Inimigo.numDeClassesDeInimigos());
        
        switch (id) {
            case 0://GOBLIN
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.NADA;
                i = new Goblin(foco,arma,armadura);
                i.addGolpeFisico("Facada");
                break;
            case 1://CULTISTA
                arma = Arma.MEDIA;
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2));
                i = new Cultista(foco,arma,armadura);
                i.addGolpeFisico("Facada");
                i.addGolpeMagicoMeteoro("Invocacao meteorica");
                i.addGolpeMagicoNevasca("Nevasca mortal");
                break;
            case 2://DRAGAO
                arma = Arma.getArmaPorId(aleatorio.nextInt(2) + 1);
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2) + 1);
                i = new Dragao(foco,arma,armadura);
                i.addGolpeFisico("Garras");
                i.addGolpeMagicoBolaDeFogo("Bola de fogo");
                break;
            case 3://OGRO
                arma = Arma.GRANDE;
                armadura = Armadura.NADA;
                i = new Ogro(foco,arma,armadura);
                i.addGolpeFisico("Machadada");
                break;
            case 4://ESQUELETO
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(2);
                i = new Esqueleto(foco,arma,armadura);
                i.addGolpeFisico("Espadada");
                break;
            case 5://ZUMBI
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.NADA;
                i = new Zumbi(foco,arma,armadura);
                i.addGolpeFisico("Arranhao");
                break;
            case 6://CHEFE
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.PESADA;
                i = new Chefe(foco,arma,armadura);
                i.addGolpeFisico("Soco fatal");
                break;
            case 7://MARRENTO
                arma = Arma.getArmaPorId(aleatorio.nextInt(2) + 1);
                armadura = Armadura.NADA;
                i = new Marrento(foco,arma,armadura);
                i.addGolpeFisico("Soco forte");
                break;
            case 8://INCENDIARIO
                arma = Arma.PEQUENA;
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2) + 1);
                i = new Incendiario(foco,arma,armadura);
                i.addGolpeFisico("Coronhada");
                i.addGolpeMagicoBolaDeFogo("Chamas gritantes");
                break;
            case 9://BIG
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(3));
                i = new Big(foco,arma,armadura);
                i.addGolpeFisico("Pancada");
                break; 
            default://caso esquecam de colocar uma nova classe de inimigo aqui, defino criacao default
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(2);
                i = new Zumbi(foco,arma,armadura);
                i.addGolpeFisico("Soco");
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
        //verifico se o lutador pode fazer algo
        String podeFazer = lutador.getAindaPodeExecutarAlgumaAcao();
        
        if(podeFazer.isEmpty())
            return "Nada a fazer.";

        //escolho a acao
        switch(lutador.getClasse()){
            case ZUMBI:
            case GOBLIN:
                if(podeFazer.contains("podeDarGolpe"))
                    return lutadorAtacaAlvo(lutador,
                        definirAlvoAleatorio(lutador).getNome(),
                        definirGolpeNoAlvo(lutador).getNome());
                if(podeFazer.contains("podeDefender"))
                    return defender(lutador);
                return esquivar(lutador);
                
            case ESQUELETO:
                if(podeFazer.contains("podeDefender"))
                    return defender(lutador);
                if(podeFazer.contains("podeDarGolpe"))
                    if(lutador.getSituacaoDeVida() == SituacaoDeVida.DESESPERADO)
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoAleatorio(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    else
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoMaisFraco(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                return esquivar(lutador);
                
            case DRAGAO:
                if(lutador.getSituacaoDeVida() == SituacaoDeVida.DESESPERADO){
                    if(podeFazer.contains("podeDarGolpe"))
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoAleatorio(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    if(podeFazer.contains("podeDefender"))
                        return defender(lutador);
                    return esquivar(lutador);
                }
                else{
                    if(podeFazer.contains("podeDarGolpe"))
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoMaisForte(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    if(podeFazer.contains("podeEsquivar"))
                        return esquivar(lutador);
                    return defender(lutador);
                }
                
            case CULTISTA:
                if(podeFazer.contains("podeEsquivar"))
                    return esquivar(lutador);
                if(podeFazer.contains("podeDefender"))
                    return defender(lutador);
                return lutadorAtacaAlvo(lutador,
                    definirAlvoAleatorio(lutador).getNome(),
                    definirGolpeNoAlvo(lutador).getNome());
                
            case OGRO:
            case BIG:
                int dado = lutador.rodarDado();
                if(podeFazer.contains("podeDarGolpe") && dado != 1)
                    return lutadorAtacaAlvo(lutador,
                        definirAlvoMaisForte(lutador).getNome(),
                        definirGolpeNoAlvo(lutador).getNome());
                else
                    if(dado == 1)
                        return lutadorAtacaAlvo(lutador,
                        definirAlvoMaisFraco(lutador).getNome(),
                        definirGolpeNoAlvo(lutador).getNome());
                if(podeFazer.contains("podeDefender"))
                    return defender(lutador);
                return esquivar(lutador);
                
            case MARRENTO:
                if(lutador.rodarDado() > 4){
                    if(podeFazer.contains("podeDarGolpe") && 
                            lutador.getSituacaoDeVida() == SituacaoDeVida.DESESPERADO)
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoMaisForte(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    else
                        if(lutador.getSituacaoDeVida() != SituacaoDeVida.DESESPERADO)
                            return lutadorAtacaAlvo(lutador,
                                definirAlvoMaisFraco(lutador).getNome(),
                                definirGolpeNoAlvo(lutador).getNome());
                    if(podeFazer.contains("podeEsquivar"))
                        return esquivar(lutador);
                    return defender(lutador);
                }
                else{
                    if(podeFazer.contains("podeDefender"))
                        return defender(lutador);
                    if(podeFazer.contains("podeDarGolpe"))
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoAleatorio(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    return esquivar(lutador);
                }
                    
            case INCENDIARIO:
                if(podeFazer.contains("podeDarGolpe"))
                    return lutadorAtacaAlvo(lutador,
                            definirAlvoMaisFraco(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                if(lutador.rodarDado() > 4){
                    if(podeFazer.contains("podeDefender"))
                        return defender(lutador);
                    return esquivar(lutador);
                }
                else{
                    if(podeFazer.contains("podeEsquivar"))
                        return esquivar(lutador);
                    return defender(lutador);
                }
                
            case CHEFE:
                if(lutador.getSituacaoDeVida() == SituacaoDeVida.ATORDOADO){
                    if(podeFazer.contains("podeDarGolpe"))
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoAleatorio(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    if(podeFazer.contains("podeDefender"))
                        return defender(lutador);
                    return esquivar(lutador);
                }
                else{
                    if(podeFazer.contains("podeDarGolpe"))
                        return lutadorAtacaAlvo(lutador,
                            definirAlvoMaisForte(lutador).getNome(),
                            definirGolpeNoAlvo(lutador).getNome());
                    if(podeFazer.contains("podeEsquivar"))
                        return esquivar(lutador);
                    return defender(lutador);
                }
                
            default://caso esquecam de colocar uma nova classe de inimigo aqui, defino acoes default
                if(podeFazer.contains("podeDarGolpe"))
                    return lutadorAtacaAlvo(lutador,
                        definirAlvoAleatorio(lutador).getNome(),
                        definirGolpeNoAlvo(lutador).getNome());
                if(podeFazer.contains("podeEsquivar"))
                    return esquivar(lutador);
                return defender(lutador);
        }
    }
        
    
    /**
     * Define um alvo aleatório a ser atacado.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoAleatorio(Personagem lutador) {
        List<Personagem> lista = batalhaAtual.getPossiveisAlvos(lutador);
        Random random = new Random();
        Personagem alvo = lista.get(random.nextInt(lista.size()));
        return alvo;
    }
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoMaisFraco(Personagem lutador) {
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(lutador);
        
        //Percorre a lista verificando qual é o alvo com maior dano
        Personagem alvo = alvos.get(0);
        int maiorDano = alvo.getDanoRecebido();
        for(Personagem p : alvos){
            SituacaoDeVida situacaoAlvo = p.getSituacaoDeVida();
            
            //Verifica se o dano do alvo é menor e se o alvo não está incapaz
            if (maiorDano < p.getDanoRecebido() &&
                    !(situacaoAlvo == SituacaoDeVida.INCONSCIENTE) &&
                    !(situacaoAlvo == SituacaoDeVida.MORTO)) {
                maiorDano = p.getDanoRecebido();
                alvo = p;
            }
        }
        return alvo;
    }    
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoMaisForte(Personagem lutador) {
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(lutador);
        
        //Percorre a lista verificando qual é o alvo com menor dano
        Personagem alvo = alvos.get(0);
        int menorDano = alvo.getDanoRecebido();
        for (Personagem p : alvos) {
            if (menorDano > p.getDanoRecebido()) {
                menorDano = p.getDanoRecebido();
                alvo = p;
            }
        }
        return alvo;
    }
    /**
     * Define um golpe aleatorio do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Golpe aleatorio, ou null caso n tenha golpe disponivel para o lutador
     */
    private Golpe definirGolpeNoAlvo(Personagem lutador){
        if(!lutador.getAindaPodeExecutarAlgumaAcao().contains("podeDarGolpe"))
            return null;
        
        List<Golpe> lista = lutador.getGolpes();
        Random random = new Random();
        
        int tam = lista.size();
        int aleatorio = random.nextInt(tam);
        Golpe g = lista.get(aleatorio);
        while(g.getCustoDeAcao() > lutador.getPontosDeAcao()){
            aleatorio++;
            
            if(aleatorio == tam)
                aleatorio = 0;
            
            g = lista.get(aleatorio);
        }
        return g;
    }
       

    
    
    //OUTROS
    
    /**
     * Passa o turno da batalha
     */
    public void passarTurno(){
        List<Personagem> list = batalhaAtual.getLutadores();
        //reinicio os pontos de acao para o proximo turno
        for(Personagem l : list){
            l.resetarPontosDeAcao();
        }
        batalhaAtual.passarTurno();
    }
    
    /**
     * Aumenta a pontuacao em um ponto, deve ser usado apos uma vitoria de batalha
     */
    public void ganharPonto(){
        pontuacao++;
    }
    
    /**
     * Retorna o vencedor da batalha atual
     * 
     * @return String dizendo quem venceu
     */
    public String getVencedores(){
        if(!batalhaAtual.batalhaAcabou())
            return "A Batalha ainda não acabou.";
        return batalhaAtual.getVencedores();
    }
}
