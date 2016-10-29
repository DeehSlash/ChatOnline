package compartilhado.modelo;

import javax.swing.ImageIcon;

public class Usuario implements java.io.Serializable {
    
    private int id;
    private String usuario;
    private boolean online;
    private ImageIcon foto;
    
    public Usuario(int id, String usuario, ImageIcon foto){
        this.id = id;
        this.usuario = usuario;
        this.foto = foto;
    }
    
    public int getId(){ return id; }
    public String getUsuario(){ return usuario; }
    public ImageIcon getFoto(){ return foto; }
    public void setOnline(boolean online){ this.online = online; }
    public void setFoto(ImageIcon foto){ this.foto = foto; }
    public boolean isOnline(){ return online; }
}
