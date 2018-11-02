package com.team1.kingofhonor.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team1.kingofhonor.model.Equipment;
import com.team1.kingofhonor.model.Hero;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeroSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "HeroDB";

    public HeroSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_HERO_TABLE = "CREATE TABLE heroes ( " +
                "_id AUTOINC," +
                "name TEXT PRIMARY KEY," +
                "image INTEGER," +
                "alias TEXT," +
                "category TEXT)";

        String CREATE_EQUIP_TABLE = "create table equipments ("
                + "_id AUTOINC ,"
                + "image integer,"
                + "name text PRIMARY KEY,"
                + "property text,"
                + "process text)";

        // create books table
        db.execSQL(CREATE_HERO_TABLE);
        db.execSQL(CREATE_EQUIP_TABLE);
        Log.e("ddd","create equipments");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS heroes");
        db.execSQL("DROP TABLE IF EXISTS equipments");
        // create fresh books table
        this.onCreate(db);
    }

    //执行查询语句
    public Cursor query(String sql, String[] args)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }

    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_HEROES = "heroes";

    // Books Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CATEGORY = "CATEGORY";
    private static final String[] COLUMNS = {KEY_NAME,KEY_ALIAS,KEY_IMAGE,KEY_CATEGORY};

    public void addHero(Hero hero){
        Log.d("addHero", hero.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, hero.getName());
        values.put(KEY_ALIAS, hero.getAlias());
        values.put(KEY_IMAGE, hero.getImage());
        values.put(KEY_CATEGORY, hero.getCategory());
        // 3. insert
        db.insert(TABLE_HEROES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Hero getHero(String name){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_HEROES, // a. table
                        COLUMNS, // b. column names
                        " name = ?", // c. selections
                        new String[] { name }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Hero hero = new Hero();
        hero.setName(cursor.getString(0));
        hero.setImage(cursor.getInt(1));
        hero.setAlias(cursor.getString(2));
        hero.setCategory(cursor.getString(3));
        // 5. return book
        return hero;
    }

    // Get All Books
    public List<Hero> getAllHeroes() {
        List<Hero> heroes = new LinkedList<Hero>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_HEROES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Hero hero = null;
        if (cursor.moveToFirst()) {
            do {
                hero = new Hero();
                hero.setName(cursor.getString(0));
                hero.setImage(Integer.parseInt(cursor.getString(2)));
                hero.setAlias(cursor.getString(1));
                hero.setCategory(cursor.getString(3));
                // Add book to heroes
                heroes.add(hero);
            } while (cursor.moveToNext());
        }


        // return heros
        return heroes;
    }

    public void deleteAllHeroes(){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_HEROES,null,null);


        // 3. close
        db.close();
    }
    // Updating single book
    public int updateHero(Hero hero) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", hero.getName());
        values.put("image", hero.getImage());
        values.put("alias", hero.getAlias());
        values.put("category", hero.getCategory()); // get author
        // 3. updating row
        int i = db.update(TABLE_HEROES, //table
                values, // column/value
                KEY_NAME+" = ?", // selections
                new String[] { hero.getName() }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single hero
    public void deleteHero(Hero hero) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_HEROES,
                KEY_NAME+" = ?",
                new String[] { hero.getName() });

        // 3. close
        db.close();


    }

    private static final String EQUIPMENT_TABLE = "equipments";

    public void addEquipment(Equipment equipment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", equipment.getImage());
        values.put("name", equipment.getName());
        values.put("property", equipment.getProperty());
        values.put("process", equipment.getProcess());
        db.insert(EQUIPMENT_TABLE, null, values);
        Log.e("eeeeeee","addEquipment");
        values.clear();
        db.close();
    }

    public Equipment getEquipment(String name){
        Equipment equipment = new Equipment();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from equipments where name =" + name, null);
        equipment.setImage(cursor.getInt(cursor.getColumnIndex("image")));
        equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
        equipment.setProperty(cursor.getString(cursor.getColumnIndex("property")));
        equipment.setProcess(cursor.getString(cursor.getColumnIndex("process")));
        cursor.close();
        return equipment;
    }

    public ArrayList<Equipment> getAllEquipment(){
        ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from equipments", null);
        if (cursor.moveToFirst()){
            do{
                Equipment equipment = new Equipment();
                equipment.setImage(cursor.getInt(cursor.getColumnIndex("image")));
                equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
                equipment.setProperty(cursor.getString(cursor.getColumnIndex("property")));
                equipment.setProcess(cursor.getString(cursor.getColumnIndex("process")));
                equipmentList.add(equipment);
            }while(cursor.moveToNext());
        }
        return equipmentList;
    }

    public void deleteAllEquipments(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("equipments", null, null);
        db.close();
    }

}
