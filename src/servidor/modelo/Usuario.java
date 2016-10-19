package servidor.modelo;

import java.awt.Image;

public class Usuario {
    
    private int id;
    private String usuario;
    private String senha;
    boolean online;
    private Image foto;
    
    public Usuario(int id, String usuario, String senha, Image foto){
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.foto = foto;
    }
    
    public int getId(){ return id; }
    public String getUsuario(){ return usuario; }
    public void setFoto(Image foto){ this.foto = foto; }
    
    public boolean isOnline(){
        return online;
    }
}
