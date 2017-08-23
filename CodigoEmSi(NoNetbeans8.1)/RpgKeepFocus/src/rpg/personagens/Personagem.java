package rpg.personagens;

import rpg.personagens.enums.Arma;
import rpg.personagens.enums.Armadura;
import rpg.personagens.enums.SituacaoDeVida;
import rpg.personagens.enums.Classe;
import rpg.personagens.enums.Foco;
import rpg.personagens.enums.Estado;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rpg.Dado;
import rpg.Metodos;
import rpg.golpes.Golpe;
import rpg.golpes.GolpeFisico;
import rpg.golpes.GolpeArcano2;
import rpg.golpes.GolpeArcano1;
import rpg.golpes.GolpeFogo1;
import rpg.golpes.GolpeFogo2;

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
    //nivel de saude do personagem, sera usado para calculo de quanto de dano o personagem aguenta
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
    //Saudável ou Atordoado ou Desesperado ou Inconsciente ou Morto
    private SituacaoDeVida situacaoDeVida;
    
    //quantos pontos o personagem tem de iniciativa, que determina quem começa a ação em uma batalha
    private int iniciativa;
    //representa quantos pontos o personagem tem para gastar com acoes
    private int pontosDeAcao;
    //lista que diz quais os estados do personagem em dado momento
    private List<Estado> estados;
    //lista com os golpes que o personagem pode usar
    private List<Golpe> golpes;
    
    
    //CONSTRUTORES
    
    /**
     * Construtor da classe Personagem
     * 
     * @param nome String com o nome do personagem
     * @param classe String com qual sera a classe do personagem
     * @param foco Foco com qual sera o ponto forte do personagem
     * @param arma Arma dizendo qual o tipo de arma o personagem usara
     * @param armadura Armadura com qual o tipo de armadura o personagem usara
     */
    public Personagem(String nome, Classe classe, Foco foco, Arma arma, Armadura armadura){
            if(!Metodos.verificaNome(nome))
                throw new RuntimeException("Nome inválido ( " + nome + " ).");
            this.nome = nome;
            if(classe == null)
                throw new RuntimeException("Classe inválida ( NULL ).");
            this.classe = classe;
            if(foco == null)
                throw new RuntimeException("Foco inválido ( NULL ).");
            this.foco = foco;
            
            this.nivelDeSaude = calculaNivelDeSaude();
            
            this.danoRecebido = 0;
            this.danoRecebidoMaximo = nivelDeSaude*5;
            this.situacaoDeVida = SituacaoDeVida.SAUDAVEL;
            
            if(arma == null)
                throw new RuntimeException("Arma inválida ( NULL ).");
            else if((arma == Arma.GRANDE || arma == Arma.MEDIA) && (classe == Classe.MAGO || classe == Classe.CLERIGO))
                throw new RuntimeException("Arma inválida ( " + arma.getString() + " ) para a classe ( " + classe.getString() + " ).");
            this.arma = arma;
            if(armadura == null)
                throw new RuntimeException("Armadura inválida ( NULL ).");
            else if((armadura == Armadura.LEVE || armadura == Armadura.PESADA) && classe == Classe.MAGO)
                throw new RuntimeException("Armadura inválida ( " + armadura.getString() + " ) para a classe ( " + classe.getString() + " ).");
            this.armadura = armadura;
            
            this.iniciativa = 0;// so inicializo
            
            this.estados = new ArrayList<Estado>();
            this.golpes = new ArrayList<Golpe>();
            
            this.d6 = new Dado(6);
            
            this.pontosDeAcao = 2;// so inicializo
    }
    
    /**
     * Define os pontos de saúde do personagem de acordo com a classe e ponto forte.
     * 
     * @return Qual sera o nivel de saude do personagem
     */
    private int calculaNivelDeSaude() {
        //Nivel de saude padrão para as classes não listadas
        int ns = 3;
        
        if(classe == Classe.GUERREIRO || classe == Classe.OGRO || 
                classe == Classe.CHEFE || classe == Classe.DRAGAO)
            ns = 4;
        else if (classe == Classe.GOBLIN || classe == Classe.CULTISTA ||
                classe == Classe.ZUMBI)
            ns = 2;
        
        if(foco.equals(Foco.CONSTITUICAO))//isso é para o caso do personagem tiver ponto forte em constituição
            ns++;
        
        return ns;
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
        this.d6 = p.d6;
        this.pontosDeAcao = p.pontosDeAcao;
    }
    
    
    //ACOES ESQUIVA E DEFESA
    
    /**
     * ACAO ESQUIVAR
     * 
     * Faz com que o personagem entre em estado de esquiva,
     * gastando um ponto de acao
     */
    public void esquivar(){
        if(pontosDeAcao <= 0)
            throw new RuntimeException("Ação Esquivar inválida.\nPontos de ação insuficientes.");
        if(getEstaEsquivando())
            throw new RuntimeException("Ação Esquivar inválida.\nPersonagem já esta em estado de esquiva.");

        pontosDeAcao--;
        estados.add(Estado.ESQUIVANDO);
    }
    
    /**
     * ACAO DEFENDER
     * 
     * Faz com que o personagem entre em estado de defesa,
     * gastando um ponto de acao
     */
    public void defender(){
        if(pontosDeAcao <= 0)
            throw new RuntimeException("Ação Defender inválida.\nPontos de ação insuficientes.");
        if(getEstaDefendendo())
            throw new RuntimeException("Ação Defender inválida.\nPersonagem já esta em estado de defesa.");
        
        pontosDeAcao--;
        estados.add(Estado.DEFENDENDO);
    }
    
     /**
     * @return True se esta defendendo, false se nao
     */
    public boolean getEstaDefendendo(){
        for(Estado d : estados){
            if(d.equals(Estado.DEFENDENDO)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return True se esta esquivando, false se nao
     */
    public boolean getEstaEsquivando(){
        for(Estado d : estados){
            if(d.equals(Estado.ESQUIVANDO)){
                return true;
            }
        }
        return false;
    }
    
    
    //ACAO ATACAR
    
    /**
     * Ação atacar, o personagem que executa esta ação escolhe um alvo e causa dano nele.
     * 
     * @param alvo Personagem que recebera o ataque
     * @param golpe Golpe que esta sendo executado
     * @return String com o relatorio do ataque
     */
    public String atacar(Personagem alvo, Golpe golpe){
        //vejo se o golpe e valido
        if(!temGolpe(golpe))
            throw new RuntimeException("Ação Atacar é inválida.\nGolpe usado durante o ataque é invalido.");
        
        int custoDaAcao = golpe.getCustoDeAcao();
        // vejo se o personagem tem pontos de acao suficientes
        if(custoDaAcao > pontosDeAcao)
            throw new RuntimeException("Ação Atacar é inválida.\nPontos de ação insuficientes.");
        //cobro o custo
        pontosDeAcao -= custoDaAcao;
        
        String relatorio = getNome() + " atacou " + alvo.getNome() + " usando " + golpe.getNome();
        
        //verifico se o alvo consegue esquivar
        if(alvo.tentarEsquivar()){
            return relatorio + " porém o alvo se esquivou e portanto nao recebeu dano.";
        }
            
        //agora verifico se o atacante acerta o alvo
        if( !golpeAcertou(golpe) ){
            //se o atacante errar
            return relatorio + " porém não conseguiu acertar.";
        }
        
        //se o atacante acertar o ataque

        //calculo do dano inicial
        int dano = golpe.getDanoEmBatalha(d6.rodarDado());

        //aumento o dano em +1 se o personagem possuir foco em força
        if(foco == Foco.FORCA)
            dano++;

        //reduzo o dano de acordo com a armadura do alvo
        dano -= alvo.armadura.getValor();
        
        //situacao de vida afetando a acao
        if(situacaoDeVida == SituacaoDeVida.DESESPERADO){
            dano--;
        }
        
        //garantia que o dano sera no minimo zero
        if(dano < 0)
            dano = 0;

        //verifico se o alvo consegue defender
        if(alvo.tentarDefender()){
            dano *= 0.5;//ou seja, reduzo o dano na metade
            alvo.receberDano(dano);
            return relatorio + " porem o alvo se defendeu e recebeu apenas " + dano + " de dano.";
        }
        
        //caso tudo ocorra normal
        
        alvo.receberDano(dano);
        return relatorio + " causando " + dano + " de dano.";
    }
    
    /**
     * Verifica se o usuario tem o golpe g
     * 
     * @param g Golpe a ser verificado na lista de golpes
     * @return True se o personagem possui o golpe, false caso contrario
     */
    private boolean temGolpe(Golpe g){
        if(g == null)
            return false;
        for(Golpe x : golpes){
            if(x.equals(g))
                return true;
        }
        return false;
    }
    
    /**
     * Tentativa do alvo de se esquivar
     * 
     * @return True se esquivou, false caso contrario
     */
    private boolean tentarEsquivar(){
        for(Estado e : estados){
            if(e.equals(Estado.ESQUIVANDO)){
                estados.remove(e);
                //o alvo roda dois dados
                int dado1chance = rodarDado();
                int dado2chance = rodarDado();

                if(dado1chance + dado2chance > 9){//se a soma dos dados for maior que 9, 27% de chance disso ocorrer
                    return true;
                }
                break;
            }   
        }
        return false;
    }
    
    /**
     * Verifica se o golpe vai acertar
     * 
     * @param golpe Golpe utilizado
     * @return Boolean com true se acertou e false se errou
     */
    private boolean golpeAcertou(Golpe g){
        //o personagem roda dois dados
        int dado1chance = rodarDado();
        int dado2chance = rodarDado();
        
        int mod = 0;
        //situacao de vida afetando a acao
        if(situacaoDeVida == SituacaoDeVida.ATORDOADO){
            mod = 1;
        }else if(situacaoDeVida == SituacaoDeVida.DESESPERADO){
            mod = 2;
        }
        
        //return g.getChanceDeAcerto() - mod >= (dado1chance + dado2chance);
        return true;//TRUE POR MOTIVOS DE TESTE
        
    }
    
    /**
     * Tentativa do alvo de se defender
     * 
     * @return True se defendeu, false caso contrario
     */
    private boolean tentarDefender(){
        for(Estado d : estados){
            if(d.equals(Estado.DEFENDENDO)){
                estados.remove(d);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Faz as atualizacoes necessarias quando o personagem recebe dano
     * 
     * @param dano Int com quantidade de dano tomado
     */
    private void receberDano(int dano){
        //acrescenta-se o dano calculado ao dano recebido do alvo
        danoRecebido += dano ;
        
        //nao permito extrapolar o maximo
        if(danoRecebido > danoRecebidoMaximo)
            danoRecebido = danoRecebidoMaximo;
        
        //atualizo a situação de vida do alvo
        attSituacaoDeVida();
    }
    
    /**
     * Atualiza a situacao de vida do personagem
     */
    private void attSituacaoDeVida(){
        //comentario de auxilio
        //0-Saudável     1-Atordoado     2-Desesperado     3-Inconsciente   4-Morto
        //30% dano       50% dano        75% dano          99% dano         100% dano
        
        if (danoRecebido < danoRecebidoMaximo * 0.3)
            situacaoDeVida = SituacaoDeVida.SAUDAVEL;
        else if (danoRecebido < danoRecebidoMaximo * 0.5)
            situacaoDeVida = SituacaoDeVida.ATORDOADO;
        else if (danoRecebido < danoRecebidoMaximo * 0.75)
            situacaoDeVida = SituacaoDeVida.DESESPERADO;
        else if (danoRecebido < danoRecebidoMaximo)
            situacaoDeVida = SituacaoDeVida.INCONSCIENTE;
        else
            situacaoDeVida = SituacaoDeVida.MORTO;
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    public abstract void diminuiNumInstancias ();
    
    
    
    
    //GOLPES
    
    /**
     * Adiciona um golpe fisico a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeFisico(String nome){
        if(possueGolpe(nome))
            throw new RuntimeException("Personagem ja possui um golpe com este nome ( " + nome + " ).");
        golpes.add(new GolpeFisico(nome,arma));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeArcano1(String nome){
        if(!verificaClasseMagica())
            throw new RuntimeException("Classe inválida, esta classe não é magica.");
        if(possueGolpe(nome))
            throw new RuntimeException("Personagem ja possui um golpe com este nome ( " + nome + " ).");
        golpes.add(new GolpeArcano1(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeArcano2(String nome){
        if(classe != Classe.MAGO)
            throw new RuntimeException("Classe inválida, apenas Magos.");
        if(possueGolpe(nome))
            throw new RuntimeException("Personagem ja possui um golpe com este nome ( " + nome + " ).");
        golpes.add(new GolpeArcano2(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeFogo1(String nome){
        if(!verificaClasseMagica())
            throw new RuntimeException("Classe inválida, esta classe não é magica.");
        if(possueGolpe(nome))
            throw new RuntimeException("Personagem ja possui um golpe com este nome ( " + nome + " ).");
        golpes.add(new GolpeFogo1(nome));
    }
    
    /**
     * Adiciona um golpes magicos a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeFogo2(String nome){
        if(classe != Classe.MAGO)
            throw new RuntimeException("Classe inválida, apenas Magos.");
        if(possueGolpe(nome))
            throw new RuntimeException("Personagem ja possui um golpe com este nome ( " + nome + " ).");
        golpes.add(new GolpeFogo2(nome));
    }
    
    /**
     * Verifica se o personagem ja tem um golpe com esse nome
     * 
     * @param nome Nome do golpe
     * @return True se ja possui o golpe, false caso contrario
     */
    public boolean possueGolpe(String nome){
        for(Golpe g: golpes){
            if(g.getNome().equals(nome))
                return true;
        }
        return false;
    }
    
     /**
     * Diz se uma classe é ou nao magica
     * 
     * @return True se sim, false se nao
     */
    public boolean verificaClasseMagica() {
        return (classe == Classe.MAGO ||
                classe == Classe.CLERIGO||
                classe == Classe.CULTISTA ||
                classe == Classe.DRAGAO ||
                classe == Classe.INCENDIARIO);
    }
    
    //GETS
    
    public abstract boolean getEhHeroi();
    
    public String getNome(){
        return nome;
    }
    
    public Classe getClasse(){
        return classe;
    }
    
    public Foco getFoco(){
        return foco;
    }
    
    public int getDanoRecebido(){
        return danoRecebido;
    }
    
    public int getDanoRecebidoMaximo(){
        return danoRecebidoMaximo;
    }
    
    public SituacaoDeVida getSituacaoDeVida(){
        return situacaoDeVida;
    }
    
    public Arma getArma(){
        return arma;
    }
    
    public Armadura getArmadura(){
        return armadura;
    }
    
    public int getNivelDeSaude(){
        return nivelDeSaude;
    }
    
    public int getIniciativa(){
        return iniciativa;
    }
    
    public List<Golpe> getGolpes(){
        return golpes;
    }
    
    public Golpe getGolpePeloNome(String g){
        for(Golpe gg : golpes){
            if(gg.getNome().equals(g))
                return gg;
        }
        return null;
    }
    
    public int getPontosDeAcao(){
        return pontosDeAcao;
    }
    
    
    /**
     * Retorna se o personagem esta consciente(true) ou nao(false)
     * 
     * @return Boolean com true se consciente e false caso contrario
     */
    public boolean estaConsciente(){
        return situacaoDeVida != SituacaoDeVida.MORTO && situacaoDeVida != SituacaoDeVida.INCONSCIENTE;
    } 
    
    //REVISAR UTILIDADE DISTO DEPOIS
    /**
     * Verifica se o personagem ainda tem alguma execucao que possa fazer
     * 
     * @return Uma concatenacao de: "podeDefender" se pode defender, "podeEsquivar" se pode esquivar, 
     * "podeDarGolpe" se existe algum golpe que pode ser executado, retorna vazio caso 
     * nao possa fazer nada
     */
    public String getAindaPodeExecutarAlgumaAcao(){
        String inf = "";

        
        //verifico se pode defender ou esquivar, 
        if(pontosDeAcao != 0){
            if(!getEstaDefendendo())
                inf += "podeDefender";
            if(!getEstaEsquivando() && foco == Foco.DESTREZA)
                inf += "podeEsquivar";
            //verifico se tem alguma golpe que pode fazer
            for(Golpe g : golpes)
                if(g.getCustoDeAcao() <= pontosDeAcao)
                    inf += "podeDarGolpe("+g.getNome()+")";
        }
        
        
        return inf;
    }
    
    

    //OUTROS
    
    /**
     * Reseta o personagem deixando ele "novo" para uma proxima partida
     */
    public void resetarStatus(){
        resetarPontosDeAcao();
        danoRecebido = 0;
        situacaoDeVida = SituacaoDeVida.SAUDAVEL;
        estados = new ArrayList<Estado>();
    }
    
    /**
     * Reseta os pontos para ficarem iguais a 2 no inicio do turno
     */
    public void resetarPontosDeAcao(){
        this.pontosDeAcao = 2;
    }
    
    
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
     * @param iniciativa Valor do dado
     */
    public void setIniciativa(int iniciativa){
        if(iniciativa < 0 || iniciativa > 6)
            throw new RuntimeException("Valor da iniciativa do personagem " + nome + " é inválida.");
        
        if(foco == Foco.DESTREZA)
            //se tiver foco em destreza
            this.iniciativa = iniciativa + 1;
        else
            //se nao tiver ponto forte em destreza
            this.iniciativa = iniciativa;
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
