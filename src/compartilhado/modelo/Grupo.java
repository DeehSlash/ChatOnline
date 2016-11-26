package compartilhado.modelo;

import javax.swing.ImageIcon;

public class Grupo {
    
    private int id;
    private String nome;
    private int[] membros;
    private ImageIcon foto;
    
    public Grupo(int id, String nome, int[] membros, ImageIcon foto){
        this.id = id;
        this.nome = nome;
        membros = new int[10];
        this.membros = membros;
        this.foto = foto;
    }
    
    public int getId(){ return id; }
    public String getNome(){ return nome; }
    public int[] getMembros(){ return membros; }
    public ImageIcon getFoto(){ return foto; }
}
