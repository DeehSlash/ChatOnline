package compartilhado.aplicacao;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Imagem {
    
    public static BufferedImage rotateImage(BufferedImage rotateImage, double angle) {
        angle %= 360;
        if (angle < 0) angle += 360;

        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), rotateImage.getWidth() / 2.0, rotateImage.getHeight() / 2.0);

        double ytrans = 0;
        double xtrans = 0;
        if( angle <= 90 ){
            xtrans = tx.transform(new Point2D.Double(0, rotateImage.getHeight()), null).getX();
            ytrans = tx.transform(new Point2D.Double(0.0, 0.0), null).getY();
        }
        else if( angle <= 180 ){
            xtrans = tx.transform(new Point2D.Double(rotateImage.getWidth(), rotateImage.getHeight()), null).getX();
            ytrans = tx.transform(new Point2D.Double(0, rotateImage.getHeight()), null).getY();
        }
        else if( angle <= 270 ){
            xtrans = tx.transform(new Point2D.Double(rotateImage.getWidth(), 0), null).getX();
            ytrans = tx.transform(new Point2D.Double(rotateImage.getWidth(), rotateImage.getHeight()), null).getY();
        }
        else{
            xtrans = tx.transform(new Point2D.Double(0, 0), null).getX();
            ytrans = tx.transform(new Point2D.Double(rotateImage.getWidth(), 0), null).getY();
        }

        AffineTransform translationTransform = new AffineTransform();
        translationTransform.translate(-xtrans, -ytrans);
        tx.preConcatenate(translationTransform);

        return new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR).filter(rotateImage, null);
    }
    
    public static Image redimensionarImagem(Image imagem, int tam, boolean manterProporcao){
        Image imagemRedimensionada;
        BufferedImage bi = new BufferedImage(imagem.getWidth(null), imagem.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bi.createGraphics();
        bGr.drawImage(imagem, 0, 0, null);
        bGr.dispose();
        if(manterProporcao)
            imagemRedimensionada = bi.getScaledInstance(tam, imagem.getHeight(null) / (imagem.getWidth(null) / tam), Image.SCALE_SMOOTH);
        else
            imagemRedimensionada = bi.getScaledInstance(tam, tam, Image.SCALE_SMOOTH);
        return imagemRedimensionada;
    }
    
    public static ByteArrayInputStream imagemParaBlob(Image imagem) throws IOException{
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
