package com.roszyk.maksymilian.biblioteka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.roszyk.maksymilian.biblioteka.Model.Film;
import com.roszyk.maksymilian.biblioteka.Model.Ksiazka;

/**
 * Created by Kavvson on 13 sty 2018.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "biblioteka.db";
    private static final String TABLE_NAME_LIBRARY = "people_table";
    private static final String TABLE_NAME_FILM = "film_table";
    private static final String TABLE_NAME_BOOK = "book_table";

    private static final String COL1_LIBRARY = "ID";
    private static final String COL2_LIBRARY = "name";

    private static final String COL1_FILM = "ID";
    private static final String COL2_FILM = "tytul";
    private static final String COL3_FILM = "rezyser";
    private static final String COL4_FILM = "czas";
    private static final String COL5_FILM = "rok";
    private static final String COL6_FILM = "gatunek";
    private static final String COL7_FILM = "opis";
    private static final String COL8_FILM = "ID_biblioteka";
    private static final String COL9_FILM = "obejrzany";

    private static final String COL1_BOOK = "ID";
    private static final String COL2_BOOK = "tytul1";
    private static final String COL3_BOOK = "autor";
    private static final String COL4_BOOK = "iloscstron";
    private static final String COL5_BOOK = "rok1";
    private static final String COL6_BOOK = "gatunek1";
    private static final String COL7_BOOK = "opis1";
    private static final String COL8_BOOK = "ID_biblioteka";
    private static final String COL9_BOOK = "przeczytana";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, 13);
    }

    /**
     * Budowa schematu bazy danych o ile jest potrzebna
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableLibrary = "CREATE TABLE " + TABLE_NAME_LIBRARY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_LIBRARY + " TEXT)";
        String createTableFilm = "CREATE TABLE " + TABLE_NAME_FILM + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_FILM + " TEXT," +
                COL3_FILM + " TEXT," +
                COL4_FILM + " TEXT," +
                COL5_FILM + " TEXT," +
                COL6_FILM + " TEXT," +
                COL7_FILM + " TEXT," +
                COL8_FILM + " INTEGER," +
                COL9_FILM + " BOOLEAN)";
        String createTablebook = "CREATE TABLE " + TABLE_NAME_BOOK + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_BOOK + " TEXT," +
                COL3_BOOK + " TEXT," +
                COL4_BOOK + " TEXT," +
                COL5_BOOK + " TEXT," +
                COL6_BOOK + " TEXT," +
                COL7_BOOK + " TEXT," +
                COL8_BOOK + " INTEGER," +
                COL9_BOOK + " BOOLEAN)";

        db.execSQL(createTableLibrary);
        db.execSQL(createTableFilm);
        db.execSQL(createTablebook);
    }


    /**
     * W przypadku zmiany wesji "version" w DBHandler zostanie wykonany poniższy kod, czyly przebudowa bazy danych
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_LIBRARY);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_FILM);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_BOOK);
        onCreate(db);
    }

    /**
     * dodajFilm
     * Dodaje film do bazy wszystkie parametry pochodzą z Modelu Film
     * i przypisuje film do biblioteki - @id getFK_Biblioteka
     *
     * @param f
     * @return
     */
    public boolean dodajFilm(Film f) {
        // insert na postawie https://stackoverflow.com/a/15391413/2513428 i z zajec
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL2_FILM, f.getTytul());
        values.put(COL3_FILM, f.getRezyser());
        values.put(COL4_FILM, f.getDlugosc());
        values.put(COL5_FILM, f.getRok());
        values.put(COL6_FILM, f.getGatunek());
        values.put(COL7_FILM, f.getOpis());
        values.put(COL8_FILM, f.getFK_Biblioteka());
        values.put(COL9_FILM, f.getObejrzany());

        Log.i(TAG,
                "Dodawanie filmu ::: " +
                        " BibliotekaID " + f.getFK_Biblioteka() +
                        " Rezyser " + f.getRezyser() +
                        " Tytul " + f.getTytul() +
                        " Dlugosc  " + f.getDlugosc() +
                        " Rok " + f.getRok() +
                        " Gatunek " + f.getGatunek() +
                        " Opis " + f.getOpis() +
                        " CzyObejrzany? " + f.getObejrzany()

        );

        long result = db.insert(TABLE_NAME_FILM, null, values);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * dodajKsiazke
     * Dodaje książkę do bazy wszystkie parametry pochodzą z Modelu Ksiazka
     * i przypisuje książkę do biblioteki - @id getFK_Biblioteka
     *
     * @param k
     * @return
     */
    public boolean dodajKsiazke(Ksiazka k) {
        // insert na postawie https://stackoverflow.com/a/15391413/2513428 i z zajec
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL2_BOOK, k.getTytul());
        values.put(COL3_BOOK, k.getAutor());
        values.put(COL4_BOOK, k.getStrony());
        values.put(COL5_BOOK, k.getRok());
        values.put(COL6_BOOK, k.getGatunek());
        values.put(COL7_BOOK, k.getOpis());
        values.put(COL8_BOOK, k.getFK_Biblioteka());
        values.put(COL9_BOOK, k.getPrzeczytana());

        Log.i(TAG,
                "Dodawanie ksiazki ::: " +
                        " BibliotekaID " + k.getFK_Biblioteka() +
                        " Autor " + k.getAutor() +
                        " Tytul " + k.getTytul() +
                        " Ilosc stron  " + k.getStrony() +
                        " Rok " + k.getRok() +
                        " Gatunek " + k.getGatunek() +
                        " Opis " + k.getOpis() +
                        " CzyPczeczytana? " + k.getPrzeczytana()

        );

        long result = db.insert(TABLE_NAME_BOOK, null, values);


        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    // TODO:: przebudowa
    public boolean addDataLibrary(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_LIBRARY, item);

        Log.d(TAG, "addDataLibrary: Adding " + item + " to " + TABLE_NAME_LIBRARY);

        long result = db.insert(TABLE_NAME_LIBRARY, null, contentValues);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * liczbaKsiazek
     * Oblicza ilość książek w bibliotece ID
     *
     * @param id
     * @return
     */
    public int liczbaKsiazek(int id) {

        String countQuery = "SELECT  * FROM " + TABLE_NAME_BOOK + " WHERE " + COL8_BOOK + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /**
     * liczbaFilmow
     * Oblicza ilość filmów w bibliotece ID
     *
     * @param id
     * @return
     */
    public int liczbaFilmow(int id) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_FILM + " WHERE " + COL8_FILM + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // TODO:: przebudowa
    public Cursor getFilmsFromLibrary(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_FILM + " WHERE " + COL8_FILM + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // TODO:: przebudowa
    public Cursor getbookFromLibrary(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BOOK + " WHERE " + COL8_BOOK + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // TODO:: przebudowa
    public Cursor getFilm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_FILM + " WHERE " + COL1_FILM + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // TODO:: przebudowa
    public Cursor getbook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BOOK + " WHERE " + COL1_BOOK + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * pobierzWszystkieBiblioteki
     * Pobiera liste wszystkich bibliotek
     *
     * @return
     */
    public Cursor pobierzWszystkieBiblioteki() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_LIBRARY;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor pobierz_id_na_podstawie_nazwy_biblioteki(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1_LIBRARY + " FROM " + TABLE_NAME_LIBRARY +
                " WHERE " + COL2_LIBRARY + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    /**
     * ksiazkaPrzeczytana
     * Ustawia wartość 0|1 dla wybranej ksiazki
     *
     * @param idKsiazki
     * @param wartosc
     */
    public void ksiazkaPrzeczytana(int idKsiazki, int wartosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_BOOK + " SET " + COL9_BOOK +
                " = '" + wartosc + "' WHERE " + COL1_BOOK + " = '" + idKsiazki + "'";
        db.execSQL(query);
    }

    /**
     * filmObejrzany
     * Ustawia wartość 0|1 dla wybranego filmu
     *
     * @param idFilmu
     * @param wartosc
     */
    public void filmObejrzany(int idFilmu, int wartosc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_FILM + " SET " + COL9_FILM +
                " = '" + wartosc + "' WHERE " + COL1_FILM + " = '" + idFilmu + "'";
        db.execSQL(query);
    }


    /**
     * usunKsiazke
     * Usuwa książkę o podanym ID
     *
     * @param id
     */
    public void usunKsiazke(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_BOOK + " WHERE "
                + COL1_BOOK + " = '" + id + "'";
        Log.i(TAG, "usunKsiazke: Usuwanie ksiazki id " + id + " ");
        db.execSQL(query);
    }

    /**
     * usunFilm
     * Usuwa film o podanym ID
     *
     * @param id
     */
    public void usunFilm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_FILM + " WHERE "
                + COL1_FILM + " = '" + id + "'";
        Log.i(TAG, "usunKsiazke: Usuwanie filmu id " + id + " ");
        db.execSQL(query);
    }

    /**
     * usunBiblioteke
     * Usuwanie biblioteki i wszystkich powiazanych wpisow
     *
     * @param id
     */
    public void usunBiblioteke(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete_biblioteka = "DELETE FROM " + TABLE_NAME_LIBRARY + " WHERE "
                + COL1_LIBRARY + " = '" + id + "'";
        String delete_filmy = "DELETE FROM " + TABLE_NAME_FILM + " WHERE "
                + COL8_FILM + " = '" + id + "'";
        String delete_ksiazki = "DELETE FROM " + TABLE_NAME_BOOK + " WHERE "
                + COL8_BOOK + " = '" + id + "'";

        db.execSQL(delete_biblioteka);
        db.execSQL(delete_filmy);
        db.execSQL(delete_ksiazki);

        Log.i(TAG, "Usuwanie biblioteki " + id);
    }

}
