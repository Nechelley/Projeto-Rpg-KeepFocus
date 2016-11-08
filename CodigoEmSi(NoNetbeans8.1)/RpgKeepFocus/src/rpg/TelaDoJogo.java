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
    
    public void exibirInicio(){
        System.out.println("\n- Rpg The Game -");
        rpg.iniciar();
    }
    
    /**
     * Le as informacoes informaçoes do heroi na ordem
     * {nome,classe,foco,arma,armadura}
     * 
     * @return Vetor de Strings com as informaçoes do heroi
     */
    @Override
    public String[] lendoHeroi(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Insira o nome do personagem:");
        String nome = scan.nextLine();
        System.out.println("Insira a classe do personagem:\n"
                + "1-Guerreiro 2-Mago 3-Clerigo");
        String classe = scan.next();
        System.out.println("Insira o foco do personagem:\n"
                + "1-Força 2-Destreza 3-Constituição 4-Carisma");
        String foco = scan.next();
        System.out.println("Insira qual é a arma do personagem:\n"
                + "1-Pequena 2-Média 3-Grande");
        String arma = scan.next();
        System.out.println("Insira qual é o tipo de armadura do personagem:\n"
                + "1-Nada 2-Leve 3-Pesada");
        String armadura = scan.next();
        
        String[] heroi = {nome,
            String.valueOf(Integer.parseInt(classe) - 1),
            String.valueOf(Integer.parseInt(foco) - 1),
            String.valueOf(Integer.parseInt(arma) - 1),
            String.valueOf(Integer.parseInt(armadura) - 1)};
        return heroi;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais herois ao time de herois
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisHerois(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Adicao do personagem no time dos herois encerrada, deseja adicionar mais personagens ao time de herois:\n"
                + "1-Sim 2-Nao");
        return scan.nextInt();  
    }
    
    /**
     * Le as informacoes informaçoes do golpe do heroi na ordem
     * {nome,classe}
     * 
     * @return Vetor de Strings com as informaçoes do golpe do heroi
     */
    @Override
    public String[] lendoGolpe(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Qual a classe de golpe:\n"
                + "1-Fisico 2-Magico 3-Bola De Fogo 4-Meteoro 5-Lanca De Gelo 6-Nevasca");
        String classe = scan.next();
        System.out.println("Qual o nome do golpe:");
        String golpeNome = scan.next();
        String[] inf = {classe, golpeNome};
        return inf;
    }    
        
    /**
     * Pede a informacao ao usuario se deseja adicionar mais golpes ao heroi
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisGolpesDeHerois(){
        Scanner scan = new Scanner (System.in);
        System.out.println("Deseja adicionar mais golpes:\n"
                    + "1-Sim 2-Nao");
        return scan.nextInt();
    }    
    
    @Override
    public void antesDeIniciarRodadaDeBatalhas(){
        System.out.println("Inicia-se o jogo:");
    }
    
    @Override
    public void apresentandoInimigos(String[][] inf){
        System.out.println("\nInimigos:");
        for(String[] x : inf){
            System.out.println();
            System.out.println(
                    "Nome: " + x[0] +
                    "\nClasse: " + x[1] +
                    "\nFoco: " + x[2] +
                    "\nNivel de Saúde: " + x[3] +
                    "\nDano Recebido/Dano Recebido Máximo: " + x[4] +
                    "/" + x[5] +
                    "\nSituação de Vida: " + x[6] +
                    "\nArma: " + x[7] +
                    "\nArmadura: " + x[8] +
                    "\nIniciativa: " + x[9]
            );
        }
    }
    
    @Override
    public void IniciandoTurnos(){
        System.out.println("\n- Inicio dos turnos -\n");
    }
    
    @Override
    public void exibindoInformacoesResumidasDeTodosOsLutadores(String[][] inf){
        System.out.println("\nStatus:");
        int cont = 1;
        for(String[] x : inf){
            System.out.println(); 
            System.out.println(cont + ")");
            System.out.println(
                    "Nome: " + x[2] + x[0] + x[1] +
                    "\nDano Recebido/Dano Recebido Máximo: " + x[3] +
                    "/" + x[4] +
                    "\nSituação de Vida: " + x[5] +
                    "\nPontos de Ação: " + x[6]
            );
            cont++;
        }
        System.out.println();
    }
    
    @Override
    public void exibindoTurnoAtual(){
        System.out.println("[turno " + rpg.getTurno() + "]");
    }
    
    @Override
    public void antesDeExibirVezDoLutador(String lutadorNome){
        System.out.println("////////////////////////// "+lutadorNome+" //////////////////////////");
    }
    
    @Override
    public void exibindoRelatorio(String relatorio){
        System.out.println("Relatório: " + relatorio);
        System.out.println();
    }
    
    @Override
    public void encerramentoDaBatalha(){
        System.out.println("A batalha acabou!");
        System.out.println("Vencedores: " + rpg.getVencedores());
    }
    
    @Override
    public void exibirFim(){
        System.out.println("Pontuacao Final: " + rpg.getPontuacao());
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
