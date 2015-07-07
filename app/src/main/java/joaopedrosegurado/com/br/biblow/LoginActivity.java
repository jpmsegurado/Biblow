package joaopedrosegurado.com.br.biblow;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import joaopedrosegurado.com.br.async.LoginAsyncTask;


public class LoginActivity extends ActionBarActivity {

    private LoginAsyncTask lat;
    private ProgressDialog pgd;
    private SQLiteDatabase db;
    private EditText edtLogin,edtSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = openOrCreateDatabase("biblow.db", Context.MODE_PRIVATE, null);

        StringBuilder sql2 = new StringBuilder();
        sql2.append("CREATE TABLE IF NOT EXISTS aluno(");
        sql2.append("matricula varchar(100) UNIQUE,");
        sql2.append("nome varchar(100)");
        sql2.append(");");

        try {
            db.execSQL(sql2.toString());
        }catch(SQLException e){
            Log.e("Erro criação do banco", e.getMessage());
        }

        Cursor aluno = db.rawQuery("SELECT * FROM aluno",null);

        if(aluno.getCount() > 0){
            Intent it = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(it);
            finish();
        }

        edtLogin = (EditText) findViewById(R.id.login);
        edtSenha = (EditText) findViewById(R.id.senha);

        int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        edtLogin.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        edtSenha.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public EditText getEdtLogin() {
        return edtLogin;
    }

    public EditText getEdtSenha() {
        return edtSenha;
    }

    public void showPgd(String texto){
        pgd = new ProgressDialog(this);
        pgd.setMessage(texto);
        pgd.setCancelable(true);
        pgd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // cancelar login
                lat.cancel(true);
            }
        });
        pgd.show();
    }

    public void dismissPgd(){
        pgd.dismiss();
    }

    public void fazLogin(View v){
        lat = new LoginAsyncTask(this);
        lat.execute();
    }


    public void insereDadosAluno(String json){

        try {
            JSONArray result = new JSONArray(json);
            JSONObject aluno = result.getJSONObject(0);
            ContentValues values = new ContentValues();
            values.put("matricula",aluno.optString("matricula"));
            values.put("nome",aluno.optString("nome"));
            try {
                if(db.insert("aluno",null,values) > 0){
                    Intent it = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();
                }else{
                    Log.d("inserção","aluno não inserido");
                }
            }catch(SQLException e){
                Log.e("Erro",e.getMessage());
            }
        } catch (JSONException e) {
            Log.e("Erro",e.getMessage());
        }
    }

}
