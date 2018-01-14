package com.roszyk.maksymilian.biblioteka.Model;

/**
 * Created by Kavvson on 14 sty 2018.
 */

public class Film {
    private int id;
    private String tytul;
    private String rezyser;
    private int dlugosc;
    private String rok;
    private String gatunek;
    private String opis;
    private int FK_Biblioteka;
    private Boolean obejrzany;


    public Film() {
    }

    public Film(String tytul, String rezyser) {
        super();
        this.tytul = tytul;
        this.rezyser = rezyser;
    }


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

    public String getRezyser() {
        return rezyser;
    }

    public void setRezyser(String rezyser) {
        this.rezyser = rezyser;
    }

    public int getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(int dlugosc) {
        this.dlugosc = dlugosc;
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

    public int getFK_Biblioteka() {
        return FK_Biblioteka;
    }

    public void setFK_Biblioteka(int FK_Biblioteka) {
        this.FK_Biblioteka = FK_Biblioteka;
    }

    public Boolean getObejrzany() {
        return obejrzany;
    }

    public void setObejrzany(Boolean obejrzany) {
        this.obejrzany = obejrzany;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + tytul + ", author=" + rezyser
                + "]";
    }

}
