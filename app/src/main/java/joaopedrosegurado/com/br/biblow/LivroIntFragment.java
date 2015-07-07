package joaopedrosegurado.com.br.biblow;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import joaopedrosegurado.com.br.async.BuscaStatusAsync;
import joaopedrosegurado.com.br.async.ReservarAsyncTask;
import joaopedrosegurado.com.br.data.BiblowProvider;


public class LivroIntFragment extends Fragment {


    public LivroIntFragment() {
        // Required empty public constructor
    }

    private TextView autor,obra,editora,situacao,nenhum;
    private LinearLayout lin_livro;
    private Button reservar,remover;
    private MainActivity main;
    private String id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_livro_int, container, false);
        nenhum = (TextView)view.findViewById(R.id.nenhum);
        lin_livro = (LinearLayout)view.findViewById(R.id.lin_livro);
        main = (MainActivity) getActivity();

        if(main.isTablet()){
            lin_livro.setVisibility(View.GONE);
            nenhum.setVisibility(View.VISIBLE);
        }

        autor       = (TextView)view.findViewById(R.id.autor);
        obra        = (TextView)view.findViewById(R.id.obra);
        editora     = (TextView)view.findViewById(R.id.editora);
        situacao    = (TextView)view.findViewById(R.id.situacao);
        reservar    = (Button)view.findViewById(R.id.reservar);
        remover    = (Button)view.findViewById(R.id.remover);

        return view;
    }

    public void restart(){
        lin_livro.setVisibility(View.GONE);
        nenhum.setVisibility(View.VISIBLE);
    }

    public void preencheInfos(final Cursor cursor,int position){


        cursor.moveToPosition(position);

        autor.setText(cursor.getString(cursor.getColumnIndex("autor")));
        editora.setText(cursor.getString(cursor.getColumnIndex("editora")));
        obra.setText(cursor.getString(cursor.getColumnIndex("titulo")));

        id = cursor.getString(cursor.getColumnIndex("_id"));

        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservarAsyncTask rat = new ReservarAsyncTask(main,id);
                rat.execute();
            }
        });

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.getContentResolver().delete(BiblowProvider.CONTENT_URL,"_id = ?",new String[]{cursor.getString(cursor.getColumnIndex("_id"))});
                main.getItf().load();
                if(main.isTablet()){
                    restart();
                }else{
                    main.getFm().popBackStack();
                }

                main.alert("removido com sucesso");
            }
        });

        BuscaStatusAsync bsa = new BuscaStatusAsync(main,id,situacao,reservar);
        bsa.execute();

        if(main.isTablet()){
            if(nenhum.getVisibility() == View.VISIBLE){
                nenhum.setVisibility(View.GONE);
            }

            Animation anim = AnimationUtils.loadAnimation(main,R.animator.fade_up);
            lin_livro.setVisibility(View.GONE);

            lin_livro.startAnimation(anim);
            lin_livro.setVisibility(View.VISIBLE);
        }

    }


}
