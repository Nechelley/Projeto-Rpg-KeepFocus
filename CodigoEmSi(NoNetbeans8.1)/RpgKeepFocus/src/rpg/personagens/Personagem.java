package rpg.personagens;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rpg.excecoes.AcaoInvalidaException;
import rpg.Dado;
import rpg.golpes.Golpe;
import rpg.golpes.GolpeFisico;
import rpg.excecoes.InfInvalidoException;
import rpg.excecoes.InfJaExistenteException;
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
            if(!verificaNome(nome))
                throw new InfInvalidoException("Nome",nome);
            this.nome = nome;
            if(classe == null)
                throw new InfInvalidoException("Classe","NULL");
            this.classe = classe;
            if(foco == null)
                throw new InfInvalidoException("Foco","NULL");
            this.foco = foco;
            
            this.nivelDeSaude = calculaNivelDeSaude();
            
            danoRecebido = 0;
            danoRecebidoMaximo = nivelDeSaude*5;
            this.situacaoDeVida = SituacaoDeVida.SAUDAVEL;
            
            if(arma == null)
                throw new InfInvalidoException("Arma","NULL");
            this.arma = arma;
            if(armadura == null)
                throw new InfInvalidoException("Armadura","NULL");
            this.armadura = armadura;
            
            iniciativa = 0;
            
            estados = new ArrayList<Estado>();
            golpes = new ArrayList<Golpe>();
            
            d6 = new Dado(6);
            
            pontosDeAcao = 2;
    }
    /**
     * Verifica se um nome é valido, ou seja, composto de apenas letras(maiusculas e minusculas), numeros(0-9) e spaces
     * 
     * @param nome String com o nome a ser verificado
     * @return True se o nome for valido, false caso contrario
     */
    private static boolean verificaNome(String nome){
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
     * 
     * @return Qual sera o nivel de saude do personagem
     */
    private int calculaNivelDeSaude() {
        //Nivel de saude padrão para as classes não listadas
        int ns = 4;
        
        if(classe == Classe.GUERREIRO || classe == Classe.OGRO)
            ns = 3;
        else if (classe == Classe.GOBLIN || classe == Classe.CULTISTA ||
                classe == Classe.ZUMBI)
            ns = 2;
        
        if(foco == Foco.CONSTITUICAO)//isso é para o caso do personagem tiver ponto forte em constituição
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
    
    
    
    /**
     * ACAO ESQUIVAR
     * 
     * Faz com que o personagem entre em estado de esquiva,
     * gastando um ponto de acao
     */
    public void esquivar(){
        if(pontosDeAcao <= 0)
            throw new AcaoInvalidaException("esquivar",0);
        if(getEstaEsquivando())
            throw new AcaoInvalidaException("esquivar",3);

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
            throw new AcaoInvalidaException("defender",0);
        if(getEstaDefendendo())
            throw new AcaoInvalidaException("defender",4);
        
        pontosDeAcao--;
        estados.add(Estado.DEFENDENDO);
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
        if(golpe == null || !temGolpe(golpe))
            throw new AcaoInvalidaException("atacar",1);
        
        int custoDaAcao = golpe.getCustoDeAcao();
        // vejo se o personagem tem pontos de acao suficientes
        if(custoDaAcao > pontosDeAcao)
            throw new AcaoInvalidaException("atacar",0);
        //cobro o custo
        pontosDeAcao -= custoDaAcao;
        
        String relatorio = getNome() + " atacou " + alvo.getNome() + " usando " + golpe;
        
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
        golpe.setDanoEmBatalha(d6.rodarDado());
        int dano = golpe.getDano();

        //aumento o dano em +1 se o personagem possuir foco em força
        if(foco == Foco.FORCA)
            dano++;

        //reduzo o dano de acordo com a armadura do alvo
        dano -= alvo.armadura.getValor();

        //garantia que o dano sera no minimo zero
        if(dano < 0)
            dano = 0;

        //verifico se o alvo consegue defender
        if(alvo.tentarDefender()){
            dano *= 0.5;//ou seja, reduzo o dano na metade
            alvo.receberDano(dano,golpe);
            return relatorio + " porem o alvo se defendeu e recebeu apenas " + dano + " de dano.";
        }
        
        //caso tudo ocorra normal
        
        alvo.receberDano(dano,golpe);
        return relatorio + " causando " + dano + " de dano.";
    }
    /**
     * Verifica se o usuario tem o golpe g
     * 
     * @param g Golpe a ser verificado na lista de golpes
     * @return True se o personagem possui o golpe, false caso contrario
     */
    private boolean temGolpe(Golpe g){
        for(Golpe x : golpes){
            if(x == g)
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
            if(e == Estado.ESQUIVANDO){
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
        //return g.getChanceDeAcerto() < (dado1chance + dado2chance);
        return true;//true por motivos de teste
        
    }
    /**
     * Tentativa do alvo de se defender
     * 
     * @return True se defendeu, false caso contrario
     */
    private boolean tentarDefender(){
        for(Estado d : estados){
            if(d == Estado.DEFENDENDO){
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
     * @param golpe Golpe que esta recebendo
     */
    protected void receberDano(int dano,Golpe golpe){
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
    protected void attSituacaoDeVida(){
        //comentario de auxilio
        //0-Saudável     1-Atordoado     2-Desesperado     3-Inconsciente   4-Morto
        //20% dano       40% dano        60% dano          99% dano         100% dano
        
        if (danoRecebido < danoRecebidoMaximo * 0.2)
            situacaoDeVida = SituacaoDeVida.SAUDAVEL;
        else if (danoRecebido < danoRecebidoMaximo * 0.4)
            situacaoDeVida = SituacaoDeVida.ATORDOADO;
        else if (danoRecebido < danoRecebidoMaximo * 0.6)
            situacaoDeVida = SituacaoDeVida.DESESPERADO;
        else if (danoRecebido < danoRecebidoMaximo)
            situacaoDeVida = SituacaoDeVida.INCONSCIENTE;
        else{
            situacaoDeVida = SituacaoDeVida.MORTO;
            diminuiNumInstancias();
        }
    }
    
    /**
     * Diminui o número de instâncias já criadas quando o inimigo morre.
     */
    public abstract void diminuiNumInstancias ();
    
    /**
     * Adiciona um golpe ja existente ao arsenal de um personagem
     * 
     * @param g Golpe a ser adicionado
     */
    public void addGolpe(Golpe g){
        if(g == null)
            throw new InfInvalidoException("Golpe","NULL");
        golpes.add(g);
    }
    
    /**
     * Adiciona um golpe fisico a lista de golpes que o personagem pode executar
     * 
     * @param nome String com o nome do golpe
     */
    public void addGolpeFisico(String nome){
        if(possueGolpe(nome))
            throw new InfJaExistenteException("Golpe");
        golpes.add(new GolpeFisico(nome,arma));
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
        if(possueGolpe(nome))
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
        if(possueGolpe(nome))
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
        if(possueGolpe(nome))
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
        if(possueGolpe(nome))
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
        if(possueGolpe(nome))
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
    
    /**
     * Retorna se o personagem esta vivo(true) ou morto(false)
     * 
     * @return Boolean com true se vivo e false se morto
     */
    public boolean estaVivo(){
        return situacaoDeVida != SituacaoDeVida.MORTO;
    } 
    
    /**
     * @return True se esta defendendo, false se nao
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
     * @return True se esta esquivando, false se nao
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
     * @param iniciativa Valor do dado
     */
    public void setIniciativa(int iniciativa){
        if(iniciativa < 0 || iniciativa > 6)
            throw new InfInvalidoException("Pontos de ação",String.valueOf(pontosDeAcao));
        
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
