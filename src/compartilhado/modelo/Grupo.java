package compartilhado.modelo;

import java.awt.Image;

public class Grupo {
    
    private int id;
    private String nome;
    private int[] membros;
    private Image foto;
    
    public Grupo(int id, String nome, int[] membros, Image foto){
        this.id = id;
        this.nome = nome;
        membros = new int[10];
        this.membros = membros;
        this.foto = foto;
    }
    
    public int getId(){ return id; }
    public String getNome(){ return nome; }
    public int[] getMembros(){ return membros; }
    public Image getFoto(){ return foto; }
}
