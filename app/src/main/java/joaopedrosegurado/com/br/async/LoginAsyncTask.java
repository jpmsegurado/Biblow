package joaopedrosegurado.com.br.async;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import joaopedrosegurado.com.br.BiblowContract;
import joaopedrosegurado.com.br.biblow.LoginActivity;

/**
 * Created by JP on 14/04/2015.
 */
public class LoginAsyncTask extends AsyncTask<Void,Void,String> {

    private LoginActivity la;

    public LoginAsyncTask(LoginActivity la){
        this.la = la;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        la.showPgd("Fazendo login...");
    }

    @Override
    protected void onPostExecute(String s) {
        la.dismissPgd();
        la.insereDadosAluno(s);
    }

    @Override
    protected String doInBackground(Void... params) {

        HttpClient cli = new DefaultHttpClient();
        HttpResponse res;
        String url = BiblowContract.biblow_url_login;

        Log.d("url",url);
        try{
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> names = new ArrayList<NameValuePair>();
            names.add(new BasicNameValuePair("matricula",la.getEdtLogin().getText().toString()));
            names.add(new BasicNameValuePair("senha",la.getEdtSenha().getText().toString()));
            post.setEntity(new UrlEncodedFormEntity(names, HTTP.UTF_8));
            res = cli.execute(post);
            HttpEntity en = res.getEntity();
            String resp = EntityUtils.toString(en);
            Log.d("resposta", resp);
            return resp;
        }
        catch(IOException ex){
            Log.d("erro IO", ex.getMessage());
        }

        return "";

    }
}
