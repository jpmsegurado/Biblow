package joaopedrosegurado.com.br.async;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by JP on 02/05/2015.
 */
public class AccService extends Service {

    private BiblowAuth ba;

    @Override
    public void onCreate() {
        ba = new BiblowAuth(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ba.getIBinder();
    }
}
