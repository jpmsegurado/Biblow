package joaopedrosegurado.com.br.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import joaopedrosegurado.com.br.BiblowContract;

/**
 * Created by Agencia_PC on 04/05/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static String create_SQL =
            "CREATE TABLE IF NOT EXISTS interessados(" +
            "_id INTEGER PRIMARY KEY," +
            "autor varchar(100)," +
            "titulo varchar(100)," +
            "editora varchar(100)" +
            ");";

    public DbHelper(Context context) {
        super(context, BiblowContract.nome_banco, null, BiblowContract.versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE "+BiblowContract.nome_banco);
        onCreate(db);
    }
}
