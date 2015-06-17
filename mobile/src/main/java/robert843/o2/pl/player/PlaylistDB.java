package robert843.o2.pl.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class PlaylistDB extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "playlistsData";
    private static final String TABLE_LISTPLAYLIST = "listplaylist";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME_PLAYLIST = "playlist";
    private static final String KEY_NAME_TRACK = "track";
    private static final String TABLE_DEFAULT = "defaultPlaylist";

    public PlaylistDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createListPlaylistTable = "CREATE TABLE " + TABLE_LISTPLAYLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME_PLAYLIST + " TEXT" + ")";

        String createDefaultPlaylistTable = "CREATE TABLE IF NOT EXISTS " + TABLE_DEFAULT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME_TRACK + " TEXT" + ")";

        db.execSQL(createDefaultPlaylistTable);
        db.execSQL(createListPlaylistTable);

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME_PLAYLIST, TABLE_DEFAULT);
        db.insert(TABLE_LISTPLAYLIST,null,cv);

        String[] tables = getListPlaylistForDatabase(db);
        for(String table : tables )
        {
            if(!table.equals(TABLE_DEFAULT))
            {
                String createPlaylistTable = "CREATE TABLE " + table + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME_TRACK + " TEXT" + ")";
                db.execSQL(createPlaylistTable);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String[] tables = getListPlaylistForDatabase(db);
        for(String table : tables)
        {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTPLAYLIST);
        onCreate(db);
    }
    public void addTracks(String[] tracks, String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv;

        for(String track : tracks)
        {
            cv = new ContentValues();
            cv.put(KEY_NAME_TRACK, track);
            db.insert(playlist, null, cv);
        }
        db.close();
    }
    public void addOneTrack(String track ,String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME_TRACK, track);
        db.insert(playlist, null, cv);
        db.close();
    }
    public void deleteTrack(String track,String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(playlist, KEY_NAME_TRACK + " = ?", new String[]{track});
        db.close();
    }

    public String[] getTracks(String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> tracks = new ArrayList<String>();

        Cursor c = db.query(playlist,null,null, null, null, null, null);

        if (c != null && c.moveToFirst())
        {
            do
            {
                tracks.add(c.getString(c.getColumnIndex(KEY_NAME_TRACK)));

            }while(c.moveToNext());
        }
        db.close();
        String[] result = new String[tracks.size()];
        result = tracks.toArray(result);
        return result;
    }
    public String[] getListPlaylistForDatabase(SQLiteDatabase db)
    {
        ArrayList<String> listPlaylist = new ArrayList<String>();
        Cursor c = db.query(TABLE_LISTPLAYLIST,null,null,null,null,null,null,null);
        if (c != null && c.moveToFirst())
        {
            do
            {
                listPlaylist.add(c.getString(c.getColumnIndex(KEY_NAME_PLAYLIST)));

            }while(c.moveToNext());
        }

        String[] result = new String[listPlaylist.size()];
        result = listPlaylist.toArray(result);
        return  result;
    }
    public String[] getListPlaylist()
    {
        ArrayList<String> listPlaylist = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_LISTPLAYLIST,null,null,null,null,null,null,null);
        if (c != null && c.moveToFirst())
        {
            do
            {
                listPlaylist.add(c.getString(c.getColumnIndex(KEY_NAME_PLAYLIST)));

            }while(c.moveToNext());
        }
        db.close();

        String[] result = new String[listPlaylist.size()];
        result = listPlaylist.toArray(result);
        return  result;
    }
    public void deletePlaylist(String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LISTPLAYLIST,KEY_NAME_PLAYLIST + " = ?", new String[]{playlist});
        db.delete(playlist,null,null);
        db.execSQL("DROP TABLE IF EXISTS " + playlist);
        db.close();
    }
    public void changeColumns(String playlist , String track,String trackUpDown)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cTrack = db.query(playlist,null,KEY_NAME_TRACK + " = ?",new String[]{track},null,null,null,null);
        cTrack.moveToFirst();
        int idTrack = cTrack.getInt(cTrack.getColumnIndex(KEY_ID));

        Cursor cTrackUpDown = db.query(playlist,null,KEY_NAME_TRACK + " = ?",new String[]{trackUpDown},null,null,null,null);
        cTrackUpDown.moveToFirst();
        int idTrackUpDown = cTrackUpDown.getInt(cTrack.getColumnIndex(KEY_ID));



        ContentValues values = new ContentValues();
        values.put (KEY_NAME_TRACK, trackUpDown);
        String where = KEY_ID+ " = ?";
        String[] whereArgs = new String[]{idTrack + ""};

        db.update (playlist, values, where, whereArgs);

        ContentValues cv = new ContentValues();
        cv.put (KEY_NAME_TRACK, track);
        String[] whereArgsSec = new String[]{ idTrackUpDown + ""};

        db.update (playlist, cv, where, whereArgsSec);

        db.close();

    }
    public void addPlaylist(String playlist)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME_PLAYLIST,playlist);
        db.insert(TABLE_LISTPLAYLIST,null,cv);
        String createPlaylistTable = "CREATE TABLE " + playlist + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME_TRACK + " TEXT" + ")";
        db.execSQL(createPlaylistTable);
        db.close();
    }
}