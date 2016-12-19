package cliente.jogo;

import compartilhado.aplicacao.Imagem;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

public class PanelJogo extends javax.swing.JPanel {
    
    private int x;
    private int y;
    
    private Image fundo;
    private Image veiculoAzul;
    private Image veiculoVermelho;
    
    private Point posicaoAzul;
    private int rotacaoAzul;
    
    private Point posicaoVermelho;
    private int rotacaoVermelho;
    
    public PanelJogo(){
        inicializar();
    }
    
    private void inicializar(){
        x = 500; // variáveis que determinam o tamanho do painel
        y = 500;
        carregarImagens(); // carrega as imagens para o jogo
        setPreferredSize(new Dimension(x, y));
        posicaoAzul.x = (x / 2) - (veiculoAzul.getWidth(this) / 2); // coordenada inicial para x e y
        posicaoAzul.y = y - veiculoAzul.getHeight(this);
        rotacaoAzul = 0;
        posicaoVermelho.x = (x / 2) - (veiculoVermelho.getWidth(this) / 2);
        posicaoVermelho.y = 0;
        rotacaoVermelho = 180;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(fundo, 0, 0, null);
        desenharVida(g, "azul", 5);
        desenharVida(g, "vermelho", 5);
        desenharVeiculo(g, "azul", azulX, azulY, rotacaoAzul);
        desenharVeiculo(g, "vermelho", vermelhoX, vermelhoY, rotacaoVermelho);
    }
    
    private void carregarImagens(){
        ImageIcon imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/fundo1.png"));
        fundo = imagem.getImage();
        imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/veiculo_azul.png"));
        veiculoAzul = Imagem.redimensionarImagem(imagem.getImage(), 50, true);
        imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/veiculo_vermelho.png"));
        veiculoVermelho = Imagem.redimensionarImagem(imagem.getImage(), 50, true);
    }
    
    private void desenharVeiculo(Graphics g, String veiculo, int x, int y, int rotacao){
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(rotacao)); // rotaciona o gráfico
        if(rotacao == 180){ // se a rotação for 180, muda as coordendas
            x += 50; // x e y são acrescidos em 50 (tamanho do veículo), pois quando é invertido (180º) as coordenadas dizem até que ponto será desenhado,
            y += 50; // não em qual ponto começará a ser desenhado, como é por padrão.
            x *= -1; // além disso o x e y se torna negativo para aparecer na coordenada certa, já que está invertido em 180º
            y *= -1;
        }
        if(veiculo.equals("azul")) // desenha o veículo informado
            g2d.drawImage(veiculoAzul, x, y, null);
        else if(veiculo.equals("vermelho"))
            g2d.drawImage(veiculoVermelho, x, y, null);
        g2d.rotate(Math.toRadians(0));
    } 
    
    private void desenharVida(Graphics g, String time, int vida){
        ImageIcon imagem;
        if(vida == 0)
            imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_0.png"));
        else
            imagem = new ImageIcon(getClass().getResource("/cliente/jogo/imagens/vida_" + time + "_" + Integer.toString(vida) + ".png"));
        Image imagemRedimensionada = Imagem.redimensionarImagem(imagem.getImage(), 100, true);
        if(time.equals("azul"))
            g.drawImage(imagemRedimensionada, 0, (y - imagemRedimensionada.getHeight(null)), null);
        else if(time.equals("vermelho"))
            g.drawImage(imagemRedimensionada, 0, 0, null);
    }
}
