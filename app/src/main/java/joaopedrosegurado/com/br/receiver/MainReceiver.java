package joaopedrosegurado.com.br.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import joaopedrosegurado.com.br.biblow.MainActivity;

public class MainReceiver extends BroadcastReceiver {


    public MainReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent it) {
        MainActivity main = (MainActivity) context;
        String comand = it.getAction();
        if(comand.equals("carregandoLivros")){
            main.showCarregandoLivrosPgd();
        }else if(comand.equals("carregouLivros")){
            String json = it.getStringExtra("json");
            Log.d("json", json);
            main.preencheLivros(json);
            main.dismissPgd();
        }else if(comand.equals("dismissPgd")){
            main.dismissPgd();
        }else{

        }
    }
}
