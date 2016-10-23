package compartilhado.modelo;

import java.awt.Image;

public class Usuario {
    
    private int id;
    private String usuario;
    private boolean online;
    private Image foto;
    
    public Usuario(int id, String usuario, Image foto){
        this.id = id;
        this.usuario = usuario;
        this.foto = foto;
    }
    
    public int getId(){ return id; }
    public String getUsuario(){ return usuario; }
    public Image getFoto(){ return foto; }
    public void setFoto(Image foto){ this.foto = foto; }
    
    public boolean isOnline(){
        return online;
    }
}
