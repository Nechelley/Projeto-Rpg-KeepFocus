package rpg;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Nechelley Alves
 */
public class TelaDoJogo implements ObservadorJogo, Serializable{
    private static Jogo rpg;
    public TelaDoJogo(){
        rpg = new Jogo(this);
    }
    
    public void exibir(){
        System.out.println("\n- Rpg The Game -");
        boolean loop = true;
        while (loop){
            
            if (novoJogo()) {
                
                loop = false;
                rpg.comecar(true);
                
            } else {

                Jogo j = null;


                try {
                    j = rpg.carregarJogo();
                } catch (Exception e) {
                    System.out.println("Erro: "+e.getMessage());
                }

                if (j != null) {
                    
                }
            }
        }
    }
    
    @Override
    public String lerNomeArquivo(String operacao) {
        System.out.println("Digite o nome do arquivo a ser "+operacao+":");
        Scanner scan = new Scanner (System.in);
        return scan.nextLine();
    }
    
    @Override
    public void arquivoInvalido(String mensagem,boolean criar) {
        if (criar) {
            System.out.println("Erro ao criar arquivo: "+mensagem);
        } else {
            System.out.println("Erro ao carregar arquivo: "+mensagem);
        }
    }
    
    @Override
    public void personagemArquivoInvalido(int linha) {
        System.out.println("Erro ao adicionar personagem na linha "+String.valueOf(linha));
    }
    
    private boolean novoJogo() {
        System.out.println("Deseja começar NOVO jogo ou CARREGAR jogo salvo?");
        Scanner scan = new Scanner (System.in);
        boolean loop = true;
        do {
            String opcao = scan.nextLine();
            if (opcao.equals("NOVO")) {
                loop = false;
                return true;
            } else if (opcao.equals("CARREGAR")) {
                loop = false;
                return false;
            } else {
                System.out.println("Opção inválida.");
            }
        } while (loop);
        return true;
    }
    
    @Override
    public boolean decideCarregarHerois() {
        Scanner scan = new Scanner (System.in);
        System.out.println("Você deseja CRIAR novos heróis ou CARREGAR um time de um arquivo?");
        boolean loop = true;
        while (loop) {
            String opcao = scan.nextLine();
            if (opcao.equals("CRIAR")) {
                return false;
            } else if (opcao.equals("CARREGAR")) {
                return true;
            } else {
                System.out.println("Opção inválida.");
            }
        }
        return false;
    }
    
    @Override
    public boolean decideSalvarHerois() {
        Scanner scan = new Scanner (System.in);
        System.out.println("Você deseja salvar os heróis criados? (s/n)");
        boolean loop = true;
        while (loop) {
            String opcao = scan.nextLine();
            if (opcao.equals("s")) {
                return true;
            } else if (opcao.equals("n")) {
                return false;
            } else {
                System.out.println("Opção inválida.");
            }
        }
        return false;
    }
    
    /**
     * Le as informacoes informaçoes do heroi na ordem
     * {nome,classe,ponto Forte,arma,armadura}
     * 
     * @return vetor de Strings com as informaçoes do heroi
     */
    private String[] lerInformacoesParaCriarHeroi(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Insira o nome do personagem:");
        String nome = scan.nextLine();
        System.out.println("Insira a classe do personagem:\n"
                + "Guerreiro, Mago ou Clerigo");
        String classe = scan.next();
        System.out.println("Insira o ponto forte do personagem:\n"
                + "0: Força 1: Destreza 2: Constituição 3: Carisma");
        String pontoForte = scan.next();
        System.out.println("Insira qual é a arma do personagem:\n"
                + "0: Pequena 1: Média 2: Grande");
        String arma = scan.next();
        System.out.println("Insira qual é a armadura do personagem:\n"
                + "Valor de 0 até 2");
        String armadura = scan.next();
        
        String[] heroi = {nome,classe,pontoForte,arma,armadura};
        return heroi;
        
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais herois ao time de herois
     * 
     * @return retorna 0-sim 1-nao
     */
    private int confirmarSeTemMaisHerois(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Adicao do personagem no time dos herois encerrada, deseja adicionar mais personagens ao time de herois:\n"
                + "0: Sim 1: Nao");
        return scan.nextInt();  
    }
    
    /**
     * Le as informacoes informaçoes do golpe do heroi na ordem
     * {tipo,nome do golpe}
     * 
     * @return vetor de Strings com as informaçoes do golpe do heroi
     */
    private String[] lerInformacoesParaCriarHeroiGolpe(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Qual o tipo de golpe:\n"
                + "0: Fisico 1: Magico 2: Bola De Fogo 3: Meteoro 4: Lanca De Gelo 5: Nevasca");
        String tipo = scan.next();
        System.out.println("Qual o nome do golpe:");
        String golpeNome = scan.next();
        String[] inf = {tipo, golpeNome};
        return inf;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais golpes ao heroi
     * 
     * @return retorna 0-sim 1-nao
     */
    private int confirmarSeTemMaisGolpesDeHerois(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Deseja adicionar mais golpes:\n"
                    + "0: Sim 1: Nao");
        return scan.nextInt();
    }
    
    @Override
    public void antesDeCriarTimeDeHerois(){
        System.out.println("Primeiramente forme seu time para batalha:");
        
        int op;
        do{
            String[] heroiInf = lerInformacoesParaCriarHeroi();
            String nome = heroiInf[0];
            String classe = heroiInf[1];
            int pontoForte = Integer.parseInt(heroiInf[2]);
            int arma = Integer.parseInt(heroiInf[3]);
            int armadura = Integer.parseInt(heroiInf[4]);
            
            rpg.adicionarHeroi(nome, classe, pontoForte, arma, armadura);
            
            //loop para adicionar os golpes do personagem
            int opg;
            do{
                String[] golpeInf = lerInformacoesParaCriarHeroiGolpe();
                int tipo = Integer.parseInt(golpeInf[0]);
                String nomeGolpe = golpeInf[1];
                if(tipo == 0){
                    rpg.addGolpeFisicoNoHeroi(nome, nomeGolpe);
                }else if(tipo == 1){
                    rpg.addGolpeMagicoBasicoNoHeroi(nome, nomeGolpe);
                }else if(tipo == 2){
                    rpg.addGolpeMagicoBolaDeFogoNoHeroi(nome, nomeGolpe);
                }else if(tipo == 3){
                    rpg.addGolpeMagicoMeteoroNoHeroi(nome, nomeGolpe);
                }else if(tipo == 4){
                    rpg.addGolpeMagicoLancaDeGeloNoHeroi(nome, nomeGolpe);
                }else if(tipo == 5){
                    rpg.addGolpeMagicoNevascaNoHeroi(nome, nomeGolpe);
                }
                opg = confirmarSeTemMaisGolpesDeHerois();
            }while(opg != 1);
            op = confirmarSeTemMaisHerois();
        }while(op != 1);
        
        /*para testes 
        rpg.adicionarHeroi("nech", "Mago", 2, 0, 0);
        rpg.addGolpeFisicoNoHeroi("nech", "fis");
        rpg.addGolpeMagicoBasicoNoHeroi("nech", "beam");
        rpg.addGolpeMagicoBolaDeFogoNoHeroi("nech", "fogo");
        rpg.addGolpeMagicoMeteoroNoHeroi("nech", "meteoro");
        rpg.addGolpeMagicoLancaDeGeloNoHeroi("nech", "lanca");*/
    }
    
    @Override
    public void antesDeIniciarRodadaDeBatalhas(){
        System.out.println("Inicia-se o jogo:");
    }
    
    @Override
    public void antesDeApresentarInimigos(){
        System.out.println("\nInimigos:");
        for(int i = 0; i < rpg.getNumInimigosNaBatalha(); i++){
            System.out.println();
            System.out.println(
                    "Nome: " + rpg.inimigoGetNome(i) +
                    "\nClasse: " + rpg.inimigoGetClasse(i) +
                    "\nPonto Forte: " + rpg.inimigoGetPontoForte(i) +
                    "\nPontos de Saúde: " + rpg.inimigoGetSaude(i) +
                    "\nDano Recebido/Dano Recebido Máximo: " + rpg.inimigoGetDanoRecebido(i) +
                    "/" + rpg.inimigoGetDanoRecebidoMaximo(i) +
                    "\nSituação de Vida: " + rpg.inimigoGetSituacaoDeVida(i) +
                    "\nArma: " + rpg.inimigoGetArma(i) +
                    "\nArmadura: " + rpg.inimigoGetArmadura(i) +
                    "\nIniciativa: " + rpg.inimigoGetIniciativa(i)
            );
        }
    }
    
    @Override
    public void antesDoInicioDoTurno(){
        System.out.println("\n- Inicio dos turnos -\n");
    }
    
    @Override
    public void antesDeExibirInformacoesResumidasDeTodosOsLutadores(){
        System.out.println("\nStatus:");
        for(int k = 0; k < rpg.getNumLutadores(); k++){
            System.out.println(); 
            System.out.println(k + ")");
            String fazendo = new String();
            if(rpg.lutadorGetEstaDefendendo(k))
                fazendo += " (defendendo)";
            if(rpg.lutadorGetEstaEsquivando(k))
                fazendo += " (esquivando)";
            if(rpg.lutadorGetEstaCongelado(k))
                fazendo += " (congelado)";
            System.out.println(
                    "Nome: " + rpg.lutadorGetNome(k) + fazendo + 
                    "\nDano Recebido/Dano Recebido Máximo: " + rpg.lutadorGetDanoRecebido(k) +
                    "/" + rpg.lutadorGetDanoRecebidoMaximo(k) +
                    "\nSituação de Vida: " + rpg.lutadorGetSituacaoDeVida(k) +
                    "\nSituação de Vida: " + rpg.lutadorGetPontosDeAcao(k)
            );
        }
        System.out.println();
    }
    
    @Override
    public void antesDeExibirTurnoAtual(){
        System.out.println("[turno " + rpg.getTurno() + "]");
    }
    
    @Override
    public void antesDeExibirVezDoLutador(String lutadorNome){
        System.out.println("////////////////////////// "+lutadorNome+" //////////////////////////");
    }
    
    @Override
    public void antesDeExibirRelatorio(String relatorio){
        System.out.println("Relatório: " + relatorio);
        System.out.println();
    }
    
    @Override
    public void antesDoEncerramentoDaBatalha(){
        System.out.println("A batalha acabou!");
        System.out.println("Vencedores: " + rpg.getVencedores());
    }
    
    @Override
    public void antesDaDerrota(){
        System.out.println("Pontuacao Final: " + rpg.getPontuacao());
    }
  
    
    @Override
    public void antesDaVitoria(){
        System.out.println("Pontuacao Atual: " + rpg.getPontuacao());
    }
    
    /**
     * Verifica se o usuario deseja parar de jogar
     * 
     * @return retorna 0-sim 1-nao 
     */
    @Override
    public int antesDeSair(boolean jaSalvou){
        if (jaSalvou)
            System.out.println("Digite SAIR para encerrar o jogo ou aperte enter para continuar...");
        else
            System.out.println("Digite SAIR para encerrar, SALVAR para salvar o jogo ou aperte enter para continuar...");
        Scanner scan = new Scanner (System.in);
        String opcao = scan.nextLine();
        if (opcao.equals("SAIR"))
            return 0;
        else if (opcao.equals("SALVAR") && !jaSalvou)
            return 2;
        return 1;
    }
     
    @Override
    public int antesDeEscolherAcao(int i){
        String aux = "Escolha sua ação " + rpg.lutadorGetNome(i) + ", voce tem " + rpg.lutadorGetPontosDeAcao(i) + " pontos de acao restantes: 1-Atacar ";
        if(!rpg.lutadorGetEstaDefendendo(i))
            aux += "2-Defender ";
        if(rpg.lutadorGetPontoForte(i).equals("Destreza") && !rpg.lutadorGetEstaEsquivando(i))
            aux += "3-Esquivar ";
        aux += "5-Passar vez 6-Pedir para exibir status dos personagens";
        System.out.println(aux);
        Scanner scan = new Scanner (System.in);  
        return scan.nextInt();
    }
    
    @Override
    public String escolhendoAlvo(int i){
        System.out.println();
        System.out.println("Escolha quem atacar , diga o nome do seu alvo:");
        System.out.println("A seguir estão suas possíveis escolhas:");

        for(int j = 0; j < rpg.getNumPossiveisAlvos(i); j++){
            System.out.println((j+1) + ")" + rpg.possivelAlvoLutadorGetNome(i,j));
        }
        boolean nomeInvalido = true;
        String alvoNome = "";
        while(nomeInvalido){
            System.out.println ();
            System.out.print("Nome: ");
            Scanner scan = new Scanner (System.in);
            alvoNome = scan.nextLine();

            // verifico se o nome é valido, ou seja, esta dentro das opcoes disponiveis
            for(int j = 0; j < rpg.getNumPossiveisAlvos(i); j++){
                if(alvoNome.equals(rpg.possivelAlvoLutadorGetNome(i,j))){
                    nomeInvalido = false;
                }
            }
            if(nomeInvalido){// se o nomeInvalido ainda estiver verdadeiro entao deu erro
                System.out.println("Nome inválido, digite denovo.");
            }
        }
        return alvoNome;
    }
    
    /**
     * Metodo para se adquirir um nome de golpe valido
     * 
     * @param i int com a posicao do personagem
     * @return String com o nome do golpe
     */
    @Override
    public String escolhendoGolpe(int i){
        System.out.println();
        System.out.println("Informe o golpe que sera executado:");
        System.out.println("A seguir estão suas possíveis escolhas:");

        for(int j = 0; j < rpg.lutadorGetNumGolpes(i); j++){
            System.out.println((j+1) + ")" + rpg.lutadorGetGolpe(i,j) + " (custa " + rpg.lutadorGetGolpeCusto(i,j) + " pontos de acao)");
        }
        boolean golpeInvalido = true;
        String golpeNome = "";
        while(golpeInvalido){
            System.out.println ();
            System.out.print("Nome do golpe: ");
            Scanner scan = new Scanner (System.in);
            golpeNome = scan.nextLine();

            // verifico se o golpe é valido, ou seja, esta dentro das opcoes disponiveis
            for(int j = 0; j < rpg.lutadorGetNumGolpes(i); j++){
                if(golpeNome.equals(rpg.lutadorGetGolpe(i,j))){
                    golpeInvalido = false;
                }
            }
            if(golpeInvalido){// se o golpeInvalido ainda estiver verdadeiro entao deu erro
                System.out.println("Golpe inválido, digite denovo.");
            }
        }
        return golpeNome;
    }
}
