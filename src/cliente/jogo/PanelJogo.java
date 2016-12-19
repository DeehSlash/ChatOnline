package cliente.jogo;

import compartilhado.aplicacao.Imagem;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class PanelJogo extends javax.swing.JPanel {
    
    private final Point tamJanela;

    private Image fundo;
    private Image veiculoAzul;
    private Image veiculoVermelho;
    private Image tiro;
    
    private Point posicaoAzul;
    private int rotacaoAzul;
    
    private Point posicaoVermelho;
    private int rotacaoVermelho;
    
    private int vidaAzul;
    private int vidaVermelho;
    
    private boolean tiroAzul;
    private Point posicaoTiroAzul;
    private boolean tiroVermelho;
    private Point posicaoTiroVermelho;
    
    // variáveis para animação da próxima posição
    private Point proximaPosicao;
    private String timeMovimentacao;
    private String timeTiro;
    private char eixo;
    
    private Timer timerMovimentacao;
    private Timer timerTiro;
    private final int delayMovimentacao;
    private final int delayTiro;
    
    public PanelJogo(){
        tamJanela = new Point(500, 500); // variáveis que determinam o tamanho do painel
        delayMovimentacao = 10;
        delayTiro = 1;
        posicaoAzul = new Point();
        posicaoVermelho = new Point();
        vidaAzul = 5;
        vidaVermelho = 5;
        proximaPosicao = new Point();
        posicaoTiroAzul = new Point();
        posicaoTiroVermelho = new Point();
        setPreferredSize(new Dimension(tamJanela.x, tamJanela.y));
        inicializar();
    }
    
    public void setPosicaoAzul(Point posicao) { posicaoAzul = posicao; }
    public void setPosicaoVermelho(Point posicao) { posicaoVermelho = posicao; }
    
    private void inicializar(){
        carregarImagens(); // carrega as imagens para o jogo
        posicaoAzul.x = (tamJanela.x / 2) - (veiculoAzul.getWidth(null) / 2); // coordenada inicial para x e y
        posicaoAzul.y = tamJanela.y - veiculoAzul.getHeight(null);
        rotacaoAzul = 0;
        posicaoVermelho.x = (tamJanela.x / 2) - (veiculoVermelho.getWidth(null) / 2);
        posicaoVermelho.y = 0;
        rotacaoVermelho = 180;
        tiroAzul = false;
        tiroVermelho = false;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(fundo, 0, 0, null);
        desenharVida(g, "azul");
        desenharVida(g, "vermelho");
        desenharVeiculo(g, "azul");
        desenharVeiculo(g, "vermelho");
        if(tiroAzul)
            desenharTiro(g, "azul");
        if(tiroVermelho)
            desenharTiro(g, "vermelho");
    }
    
    private void carregarImagens(){
        ImageIcon imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/fundo1.png"));
        fundo = imagem.getImage();
        imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/veiculo_azul.png"));
        veiculoAzul = Imagem.redimensionarImagem(imagem.getImage(), 50, true);
        imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/veiculo_vermelho.png"));
        veiculoVermelho = Imagem.redimensionarImagem(imagem.getImage(), 50, true);
        imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/tiro.png"));
        tiro = Imagem.redimensionarImagem(imagem.getImage(), 10, true);
    }
    
    private Rectangle getLimiteVeiculo(String time){
        if(time.equals("azul")){
            return new Rectangle(posicaoAzul.x, posicaoAzul.y, veiculoAzul.getWidth(null), veiculoAzul.getHeight(null));
        }else if(time.equals("vermelho"))
            return new Rectangle(posicaoVermelho.x, posicaoVermelho.y, veiculoVermelho.getWidth(null), veiculoVermelho.getHeight(null));
        return null;
    }
    
    private Rectangle getLimiteTiro(String time){
        if(time.equals("azul")){
            return new Rectangle(posicaoTiroAzul.x, posicaoTiroAzul.y, tiro.getWidth(null), tiro.getHeight(null));
        }else if(time.equals("vermelho"))
            return new Rectangle(posicaoTiroVermelho.x, posicaoTiroVermelho.y, tiro.getWidth(null), tiro.getHeight(null));
        return null;
    }
    
    private Point rotacionar(Point ponto, int graus){
        Point p = new Point(ponto.x, ponto.y);
        if(graus == 180){
            p.x += 50; // x e y são acrescidos em 50 (tamanho do veículo), pois quando é invertido (180º) as coordenadas dizem até que ponto será desenhado,
            p.y += 50; // não em qual ponto começará a ser desenhado, como é por padrão.
            p.x *= -1; // além disso o x e y se torna negativo para aparecer na coordenada certa, já que está invertido em 180º
            p.y *= -1;
        }
        return p;
    }
    
    private void desenharVeiculo(Graphics g, String time){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        if(time.equals("azul")){
            g2d.drawImage(veiculoAzul, posicaoAzul.x, posicaoAzul.y, null);
        }else{
            Point p = new Point(posicaoVermelho.x, posicaoVermelho.y);
            p = rotacionar(p, 180);
            g2d.rotate(Math.toRadians(180)); // rotaciona o gráfico
            g2d.drawImage(veiculoVermelho, p.x, p.y, null);
        }
        g2d.setTransform(old);
    } 
    
    private void desenharVida(Graphics g, String time){
        ImageIcon imagem;
        if(time.equals("azul")){
            if(vidaAzul == 0)
                imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_0.png"));
            else
                imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_" + time + "_" + Integer.toString(vidaAzul) + ".png"));
        }else{
            if(vidaVermelho == 0)
                imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_0.png"));
            else
                imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_" + time + "_" + Integer.toString(vidaVermelho) + ".png"));
        }
        Image imagemRedimensionada = Imagem.redimensionarImagem(imagem.getImage(), 100, true);
        if(time.equals("azul"))
            g.drawImage(imagemRedimensionada, 0, (tamJanela.y - imagemRedimensionada.getHeight(null)), null);
        else if(time.equals("vermelho"))
            g.drawImage(imagemRedimensionada, 0, 0, null);
    }
    
    private void desenharTiro(Graphics g, String time){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        if(time.equals("azul"))
            g2d.drawImage(tiro, posicaoTiroAzul.x, posicaoTiroAzul.y, null);
        else{
            Point p = new Point(posicaoTiroVermelho.x, posicaoTiroVermelho.y);
            p = rotacionar(p, 180);
            g2d.rotate(Math.toRadians(180)); // rotaciona o gráfico
            g2d.drawImage(tiro, p.x, p.y, null);
        }
        g2d.setTransform(old);
    }
    
    public void atualizarPosicao(Point posicao, String time){
        proximaPosicao = posicao;
        this.timeMovimentacao = time;
        Point diferenca;
        if(time.equals("azul"))
            diferenca = new Point(posicao.x - posicaoAzul.x, posicao.y - posicaoAzul.y);
        else
            diferenca = new Point(posicao.x - posicaoVermelho.x, posicao.y - posicaoVermelho.y);
        if(diferenca.x == 0)
            eixo = 'y';
        else
            eixo = 'x';
        
        ActionListener action = (ActionEvent e) -> {
            animarMovimento();
        };
        timerMovimentacao = new Timer(delayMovimentacao, action);
        timerMovimentacao.start();
    }
    
    public void animarMovimento(){
        if(timeMovimentacao.equals("azul") && eixo == 'x'){
            if(proximaPosicao.x > posicaoAzul.x)
                posicaoAzul.x++;
            else if (proximaPosicao.x < posicaoAzul.x)
                posicaoAzul.x--;
            else if(proximaPosicao.x == posicaoAzul.x){
                timerMovimentacao.stop();
                return;
            }
        }
        if(timeMovimentacao.equals("azul") && eixo == 'y'){
            if(proximaPosicao.y > posicaoAzul.y)
                posicaoAzul.y++;
            else if(proximaPosicao.y < posicaoAzul.y)
                posicaoAzul.y--;
            else if(proximaPosicao.y == posicaoAzul.y){
                timerMovimentacao.stop();
                return;
            }
        }
        if(timeMovimentacao.equals("vermelho") && eixo == 'x'){
            if(proximaPosicao.x > posicaoVermelho.x)
                posicaoVermelho.x++;
            else if(proximaPosicao.x < posicaoVermelho.x)
                posicaoVermelho.x--;
            else if(proximaPosicao.x == posicaoVermelho.x){
                timerMovimentacao.stop();
                return;
            }
        }
        if(timeMovimentacao.equals("vermelho") && eixo == 'y'){
            if(proximaPosicao.y > posicaoVermelho.y)
                posicaoVermelho.y++;
            else if(proximaPosicao.y < posicaoVermelho.y)
                posicaoVermelho.y--;
            else if(proximaPosicao.y == posicaoVermelho.y){
                timerMovimentacao.stop();
                return;
            }
        }
        repaint();
    }            
    
    public void criarTiro(String time) {
        timeTiro = time;
        if(timeTiro.equals("azul")){
            posicaoTiroAzul.x = posicaoAzul.x + (veiculoAzul.getWidth(null) / 2) - (tiro.getWidth(null) / 2);
            posicaoTiroAzul.y = posicaoAzul.y - veiculoAzul.getHeight(null);
            tiroAzul = true;
        }else{
            posicaoTiroVermelho.x = posicaoVermelho.x + (veiculoVermelho.getWidth(null) / 2) - (tiro.getWidth(null) / 2);
            posicaoTiroVermelho.y = posicaoVermelho.y + veiculoVermelho.getHeight(null);
            tiroVermelho = true;
        }
        repaint();
        
        ActionListener action = (ActionEvent e) -> {
            animarTiro(time);
        };
        timerTiro = new Timer(delayTiro, action);
        timerTiro.start();
    }
    
    public void animarTiro(String time){
        // verifica se acertou o veículo inimigo ou se passou do limite do Frame
        boolean colisao = false;
        boolean limiteX = false;
        boolean limiteY = false;
        if(time.equals("azul")){
            colisao = getLimiteTiro(time).intersects(getLimiteVeiculo("vermelho"));
            limiteX = posicaoTiroAzul.getX() < 0 || posicaoTiroAzul.getX() > tamJanela.x;
            limiteY = posicaoTiroAzul.getY() < 0 || posicaoTiroAzul.getY() > tamJanela.y;
        }else if (time.equals("vermelho")){
            colisao = getLimiteTiro(time).intersects(getLimiteVeiculo("azul"));
            limiteX = posicaoTiroVermelho.getX() < 0 || posicaoTiroVermelho.getX() > tamJanela.x;
            limiteY = posicaoTiroVermelho.getY() < 0 || posicaoTiroVermelho.getY() > tamJanela.y;
        }
        if(colisao || limiteX || limiteY){
            timerTiro.stop();
            if(time.equals("azul"))
                tiroAzul = false;
            else
                tiroVermelho = false;
            repaint();
            return;
        }
        // incrementa o eixo apropriado e renderiza novamente
        if(time.equals("azul")){
            posicaoTiroAzul.y--;
        }else{
            posicaoTiroVermelho.y++;
        }
        repaint();
    }
    
    public void atualizarVida(int vida, String time){
        if(time.equals("azul"))
            vidaAzul = vida;
        else
            vidaVermelho = vida;
        repaint();
    }
}
