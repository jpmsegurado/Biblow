package joaopedrosegurado.com.br.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class BiblowProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "joaopedrosegurado.com.br";
    public static final String URL = "content://"+PROVIDER_NAME+"/biblow";
    public static final Uri CONTENT_URL = Uri.parse(URL);

    public static final String id_exemplar = "_id";
    public static final String titulo = "titulo";
    public static final String editora = "editora";
    public static final String autor = "autor";
    public static final String TABLE_NAME = "interessados";

    public static final int uriCode = 25091993;

    private SQLiteDatabase db;

    private static HashMap<String,String> values;

    public static final UriMatcher matcher;
    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME,"biblow",uriCode);
    }

    @Override
    public boolean onCreate() {

        DbHelper helper  = new DbHelper(getContext());
        db = helper.getReadableDatabase();

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch(matcher.match(uri)){
            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("URI desconhecida "+uri);
        }

        Cursor cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch(matcher.match(uri)){
            case uriCode:
                return "vnd.android.cursor.dir/biblow";
            default:
                throw new IllegalArgumentException("URI não suportada "+uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(TABLE_NAME,null,values);

        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            Log.d("insert uri", _uri.toString());
            return uri;
        }else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;

        switch(matcher.match(uri)){
            case uriCode:

                rowsDeleted = db.delete(TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("URI desconhecida "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        switch(matcher.match(uri)){
            case uriCode:

                rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("URI desconhecida "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rowsUpdated;
    }
}
