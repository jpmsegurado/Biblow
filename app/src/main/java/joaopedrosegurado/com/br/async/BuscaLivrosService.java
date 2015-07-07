package joaopedrosegurado.com.br.async;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class BuscaLivrosService extends Service {

    private BuscaLivrosAsync bla;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String comando = intent.getStringExtra("comando");
        if(comando.equals("buscar_livros")){
            bla = new BuscaLivrosAsync(this);
            bla.execute();
        }else if(comando.equals("parar")){
            if(bla.getStatus().equals(AsyncTask.Status.RUNNING)){
                bla.cancel(true);
            }
            sendBroadcast(new Intent("dismissPgd"));
            stopSelf();
        }

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service destruido","service destruido");
    }
}
