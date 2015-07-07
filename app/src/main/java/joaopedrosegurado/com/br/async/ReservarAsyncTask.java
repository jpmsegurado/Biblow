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
import joaopedrosegurado.com.br.biblow.MainActivity;

public class ReservarAsyncTask extends AsyncTask<Void,Void,String> {

    private MainActivity main;
    private String id;

    public ReservarAsyncTask(MainActivity main,String id){
        this.main = main;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        main.showPgd("Reservando...");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        main.dismissPgd();
        if(s.equals("y")){
            main.alertReservar("Reservado com sucesso.");
            main.getFm().popBackStack();
        }else{
            main.alert("Erro, tente novamente.");
        }

    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient cli = new DefaultHttpClient();
        HttpResponse res;
        String url = BiblowContract.biblow_url_reservar;

        Log.d("url", url);
        try{
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> names = new ArrayList<NameValuePair>();
            names.add(new BasicNameValuePair("id_exemplar",id));
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
