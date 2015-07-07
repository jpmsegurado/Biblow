package joaopedrosegurado.com.br.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import joaopedrosegurado.com.br.BiblowContract;

/**
 * Created by JP on 16/04/2015.
 */
public class BuscaLivrosAsync extends AsyncTask<Void,Void,String> {
    BuscaLivrosService ctx;
    public BuscaLivrosAsync(BuscaLivrosService ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Intent it = new Intent("carregandoLivros");
        ctx.sendBroadcast(it);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent it = new Intent("carregouLivros");
        it.putExtra("json",s);
        ctx.sendBroadcast(it);
        ctx.stopSelf();
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient cli = new DefaultHttpClient();
        HttpResponse res;
        String url = BiblowContract.biblow_url_busca_livros;

        Log.d("url", url);
        try{
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
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
