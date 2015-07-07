package joaopedrosegurado.com.br.biblow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import joaopedrosegurado.com.br.adapter.LivroAdapter;
import joaopedrosegurado.com.br.async.BiblowSyncAdapter;
import joaopedrosegurado.com.br.async.BuscaLivrosService;
import joaopedrosegurado.com.br.receiver.MainReceiver;


public class MainActivity extends ActionBarActivity {

    private ProgressDialog pgd;
    private MainFragment mf;
    private LivroFragment lf;
    private FragmentManager fm;
    private InteressesFragment itf;
    private LivroIntFragment lif;
    private FotoFragment ff;
    private MainReceiver receiver;
    private SQLiteDatabase db;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout dl;
    private ListView list;
    private boolean tablet;



    public FragmentManager getFm() {
        return fm;
    }

    public MainFragment getMf() {

        return mf;
    }

    public LivroFragment getLf() {
        return lf;
    }

    public LivroIntFragment getLif() {
        return lif;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.frame_detail) != null){
            tablet = true;
        }else{
            tablet = false;
        }

        list = (ListView)findViewById(R.id.list);

        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,dl,R.string.nome_da_obra,R.string.nome_da_obra){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dl.setDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.syncState();
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(getApplicationContext(),R.array.menu,R.layout.item_menu);

        list.setAdapter(aa);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!tablet) {
                    final FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.animator.fade_up, R.animator.fadeout, R.animator.fade_up, R.animator.fadeout);
                    ft.addToBackStack(null);
                    if (position == 1) {
                        if (!itf.isVisible() && !lif.isVisible()) {
                            ft.hide(getFragmentVisivel());
                            ft.show(itf);
                        } else {
                            dl.closeDrawers();
                            return;
                        }
                    } else if (position == 2) {

                        if (ff.isVisible()) {
                            dl.closeDrawers();
                            return;
                        }

                        ft.hide(getFragmentVisivel());
                        ft.show(ff);

                    } else if (position == 0) {

                        if (mf.isVisible()) {
                            dl.closeDrawers();
                            return;
                        }

                        ft.hide(getFragmentVisivel());
                        ft.show(mf);
                    }
                    dl.closeDrawers();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ft.commit();
                        }
                    }, 250);
                } else {

                    if (position == 0) {
                        if (!mf.isVisible()) {

                            final FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.animator.fade_up, R.animator.fadeout, R.animator.fade_up, R.animator.fadeout);
                            ft.addToBackStack(null);
                            ft.hide(itf);
                            ft.hide(lif);
                            ft.show(mf);
                            ft.show(lf);

                            dl.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ft.commit();
                                }
                            }, 250);
                        } else {
                            dl.closeDrawers();
                        }
                    } else if (position == 1) {
                        if (!itf.isVisible()) {

                            final FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.animator.fade_up, R.animator.fadeout, R.animator.fade_up, R.animator.fadeout);
                            ft.addToBackStack(null);
                            ft.show(itf);
                            ft.show(lif);
                            ft.hide(mf);
                            ft.hide(lf);

                            dl.closeDrawers();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ft.commit();
                                }
                            }, 250);
                        } else {
                            dl.closeDrawers();
                        }
                    }


                }
            }
        });


        db = openOrCreateDatabase("biblow.db", Context.MODE_PRIVATE, null);

        if(receiver == null){
            receiver = new MainReceiver();
            IntentFilter itf = new IntentFilter();
            itf.addAction("carregandoLivros");
            itf.addAction("carregouLivros");
            itf.addAction("dismissPgd");
            registerReceiver(receiver,itf);
        }

        fm = getSupportFragmentManager();

        // Iniciando os fragments
        mf = new MainFragment();
        lf = new LivroFragment();
        itf = new InteressesFragment();
        lif = new LivroIntFragment();
        ff = new FotoFragment();


        if(!tablet){
            //Adicionando os fragments
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frame_container,mf);
            ft.add(R.id.frame_container,lf);
            ft.add(R.id.frame_container,itf);
            ft.add(R.id.frame_container,lif);
            ft.add(R.id.frame_container,ff);
            ft.hide(lf);
            ft.hide(itf);
            ft.hide(lif);
            ft.hide(mf);
            ft.commit();
        }else{
            //Adicionando os fragments
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frame_container,mf);
            ft.add(R.id.frame_container,itf);
            ft.add(R.id.frame_detail, lf);
            ft.add(R.id.frame_detail, lif);


            ft.hide(itf);
            ft.hide(lif);

            ft.commit();
        }

        // carregando os livros
        Intent it = new Intent(MainActivity.this,BuscaLivrosService.class);
        it.putExtra("comando","buscar_livros");
        startService(it);

        BiblowSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isTablet(){
        return tablet;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public Fragment getFragmentVisivel(){
        if(mf.isVisible() && lf.isHidden() && itf.isHidden() && lif.isHidden()){
            return mf;
        }else if(itf.isVisible() && lf.isHidden() && mf.isHidden() && lif.isHidden() && ff.isHidden()){
            return itf;
        }else if(itf.isHidden() && lf.isHidden() && mf.isHidden() && lif.isVisible() && ff.isHidden()){
            return lif;
        }else if(itf.isHidden() && lf.isHidden() && mf.isHidden() && lif.isVisible() && ff.isHidden()){
            return lif;
        }else if(itf.isHidden() && lf.isHidden() && mf.isHidden() && lif.isHidden() && ff.isVisible()){
            return ff;
        }else{
            return lf;
        }
    }

    public InteressesFragment getItf() {
        return itf;
    }

    public void carregaLivros(){
        Intent it = new Intent(MainActivity.this,BuscaLivrosService.class);
        it.putExtra("comando","buscar_livros");
        startService(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();

        unregisterReceiver(receiver);
    }

    public void showCarregandoLivrosPgd(){
        pgd = new ProgressDialog(this);
        pgd.setCancelable(true);
        pgd.setMessage("Carregando livros...");
        pgd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent it = new Intent(MainActivity.this,BuscaLivrosService.class);
                it.putExtra("comando","parar");
                startService(it);
            }
        });
        pgd.show();
    }

    public void showPgd(String msg){
        pgd = new ProgressDialog(this);
        pgd.setCancelable(true);
        pgd.setMessage(msg);
        pgd.show();
    }

    public void dismissPgd(){
        if(pgd != null){
            if(pgd.isShowing()){
                pgd.dismiss();
            }
        }
    }


    public void preencheLivros(String json){
        try {
            JSONArray ar = new JSONArray(json);
            mf.getLvMain().setAdapter(new LivroAdapter(ar,this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void alertReloadLivros(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carregaLivros();
                if(tablet){lf.restart();}
            }
        });
        alert.create().show();
    }

    public void alert(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setPositiveButton("ok",null);
        alert.create().show();
    }

    public void alertReservar(String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg);
        alert.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carregaLivros();
                if(tablet){lf.restart();}
            }
        });
        alert.create().show();
    }

}
