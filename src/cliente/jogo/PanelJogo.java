package cliente.jogo;

import compartilhado.aplicacao.Imagem;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

public class PanelJogo extends javax.swing.JPanel {
    
    private int x;
    private int y;
    
    private Image fundo;
    private Image veiculoAzul;
    private Image veiculoVermelho;
    
    private int azulX;
    private int azulY;
    private int azulRotacao;
    private int vermelhoX;
    private int vermelhoY;
    private int vermelhoRotacao;
    
    public PanelJogo(){
        x = 500;
        y = 500;
        inicializar();
    }
    
    private void inicializar(){
        carregarImagens();
        setPreferredSize(new Dimension(x, y));
        azulX = (x / 2) - (veiculoAzul.getWidth(this) / 2);
        azulY = y - veiculoAzul.getHeight(this);
        azulRotacao = 0;
        vermelhoX = (x / 2) - (veiculoVermelho.getWidth(this) / 2);
        vermelhoY = 0;
        vermelhoRotacao = 180;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(fundo, 0, 0, null);
        desenharVeiculo(g, "azul", azulX, azulY, azulRotacao);
        desenharVeiculo(g, "vermelho", vermelhoX, vermelhoY, vermelhoRotacao);
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
        g2d.rotate(Math.toRadians(rotacao));
        if(rotacao == 180){
            x += 50;
            y += 50;
            x *= -1;
            y *= -1;
        }
        if(veiculo.equals("azul"))
            g2d.drawImage(veiculoAzul, x, y, null);
        else if(veiculo.equals("vermelho"))
            g2d.drawImage(veiculoVermelho, x, y, null);
    } 
    
}
