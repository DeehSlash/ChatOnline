package compartilhado.aplicacao;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Foto {
    
    public static Image redimensionarFoto(File arquivo, int tam){
        BufferedImage bi;
        Image imagem = null;
        try {
            bi = ImageIO.read(arquivo);
            imagem = bi.getScaledInstance(tam, tam, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            ex.getMessage();
        }
        return imagem;
    }
    
    public static ByteArrayInputStream imageToBlob(Image imagem) throws IOException{
        BufferedImage bi = new BufferedImage(imagem.getWidth(null), imagem.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(imagem, 0, 0, null);
        g2d.dispose();
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
