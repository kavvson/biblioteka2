package com.roszyk.maksymilian.biblioteka.Model;

/**
 * Created by Kavvson on 14 sty 2018.
 */

public class Ksiazka {
    private int id;
    private String tytul;
    private String autor;
    private int strony;
    private String rok;
    private String gatunek;
    private String opis;

    public int getFK_Biblioteka() {
        return FK_Biblioteka;
    }

    public void setFK_Biblioteka(int FK_Biblioteka) {
        this.FK_Biblioteka = FK_Biblioteka;
    }

    private int FK_Biblioteka;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getStrony() {
        return strony;
    }

    public void setStrony(int strony) {
        this.strony = strony;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getGatunek() {
        return gatunek;
    }

    public void setGatunek(String gatunek) {
        this.gatunek = gatunek;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Boolean getPrzeczytana() {
        return przeczytana;
    }

    public void setPrzeczytana(Boolean przeczytana) {
        this.przeczytana = przeczytana;
    }

    private Boolean przeczytana;


    public Ksiazka() {
    }

    public Ksiazka(String title, String author) {
        super();
        this.tytul = title;
        this.autor = author;
    }



    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + tytul + ", author=" + autor
                + "]";
    }

}
