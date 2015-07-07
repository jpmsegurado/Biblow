package joaopedrosegurado.com.br.biblow;


import android.content.ContentValues;
import joaopedrosegurado.com.br.data.BiblowProvider;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import joaopedrosegurado.com.br.async.ReservarAsyncTask;
import joaopedrosegurado.com.br.data.BiblowProvider;


public class LivroFragment extends Fragment {


    public LivroFragment() {
        // Required empty public constructor
    }


    private TextView autor,obra,editora,situacao,nenhum;
    private LinearLayout lin_livro;
    private Button reservar,avise;
    private JSONObject atual;
    private MainActivity main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_livro, container, false);
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
        avise    = (Button)view.findViewById(R.id.avise);

        avise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues ctv = new ContentValues();
                    ctv.put(BiblowProvider.id_exemplar,atual.getString("id"));
                    ctv.put(BiblowProvider.autor,atual.getString("autor"));
                    ctv.put(BiblowProvider.titulo,atual.getString("titulo"));
                    ctv.put(BiblowProvider.editora,atual.getString("editora"));
                    try {
                        main.getContentResolver().insert(BiblowProvider.CONTENT_URL,ctv);
                        main.alert("Adicionado aos seus interesses.");
                    }catch (SQLException e){
                        e.printStackTrace();
                        main.alert("Este exemplar já está nos seus interesses.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservarAsyncTask rat = new ReservarAsyncTask(main,atual.optString("id"));
                rat.execute();
            }
        });

        return view;
    }

    public void restart(){
        lin_livro.setVisibility(View.GONE);
        nenhum.setVisibility(View.VISIBLE);
    }

    public void preencheInfos(JSONObject ob){

        if(main.isTablet()){
            if(lin_livro.getVisibility() == View.VISIBLE){
                lin_livro.setVisibility(View.GONE);
            }else{
                nenhum.setVisibility(View.GONE);
            }

            Animation anim = AnimationUtils.loadAnimation(getActivity(),R.animator.fade_up);
            lin_livro.startAnimation(anim);
            lin_livro.setVisibility(View.VISIBLE);

        }
        atual = ob;
        autor.setText(ob.optString("autor"));
        obra.setText(ob.optString("titulo"));
        editora.setText(ob.optString("editora"));
        if(ob.optString("status").equals("D")) {
            situacao.setText("Disponível");
            if(reservar.getVisibility() != View.VISIBLE){
                reservar.setVisibility(View.VISIBLE);
                avise.setVisibility(View.GONE);
            }

        }else{
            if(reservar.getVisibility() != View.GONE){
                reservar.setVisibility(View.GONE);
                avise.setVisibility(View.VISIBLE);
            }
            situacao.setText("Indisponível");
        }
    }

}
