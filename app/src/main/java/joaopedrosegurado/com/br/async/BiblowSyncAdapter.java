package joaopedrosegurado.com.br.async;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import joaopedrosegurado.com.br.BiblowContract;
import joaopedrosegurado.com.br.biblow.MainActivity;
import joaopedrosegurado.com.br.biblow.R;
import joaopedrosegurado.com.br.data.BiblowProvider;

/**
 * Created by JP on 02/05/2015.
 */
public class BiblowSyncAdapter extends AbstractThreadedSyncAdapter {


    public BiblowSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        HttpClient cli = new DefaultHttpClient();
        HttpResponse res;
        String url = BiblowContract.biblow_url_busca_exemplar;

        Cursor exemplar = getContext().getContentResolver().query(BiblowProvider.CONTENT_URL,null,null,null,null);

        Log.d("exemplares",exemplar.toString());

        Log.d("Sync","starting sync");

        int count = 0;
        Log.d("count",String.valueOf(exemplar.getCount()));
        if(exemplar.getCount() > 0){
            exemplar.moveToFirst();
            do{

                Log.d("url", url);
                try{
                    HttpPost post = new HttpPost(url);
                    post.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    List<NameValuePair> names = new ArrayList<NameValuePair>();
                    names.add(new BasicNameValuePair("id_exemplar",exemplar.getString(exemplar.getColumnIndex("_id"))));
                    post.setEntity(new UrlEncodedFormEntity(names, HTTP.UTF_8));
                    res = cli.execute(post);
                    HttpEntity en = res.getEntity();
                    String resp = EntityUtils.toString(en);
                    Log.d("resposta", resp);

                    if(!resp.equals("[]")){
                        count++;
                        try {
                            JSONArray array = new JSONArray(resp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                catch(IOException ex){
                    Log.e("erro IO", ex.getMessage());
                    return;
                }

            }while(exemplar.moveToNext());

            if(count > 0){
                notifica(count);
            }
        }

    }

    public void notifica(int count){

        Log.d("notificacao","Há "+count+" livros dos seus interesses disponível");

        Intent resultIntent = new Intent(getContext(), MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setContentTitle("Biblow");
        builder.setAutoCancel(true);
        builder.setContentText("Há " + count + " livros do seu interesse disponíveis");
        builder.setSmallIcon(R.drawable.ic_launcher);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(25091993, builder.build());

    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),"joaopedrosegurado.com.br", bundle);

    }

     public static void initializeSyncAdapter(Context ctx){
         configurePeriodicSync(ctx,1,1);
     }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(1, 1).
                    setSyncAdapter(account, "joaopedrosegurado.com.br").
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,"joaopedrosegurado.com.br", new Bundle(), syncInterval);
        }
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account("biblow", "joaopedrosegurado.com.br");

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        BiblowSyncAdapter.configurePeriodicSync(context, 1, 1);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, "joaopedrosegurado.com.br", true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }
}
