package joaopedrosegurado.com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import joaopedrosegurado.com.br.biblow.R;

/**
 * Created by JP on 16/04/2015.
 */
public class LivroAdapter extends BaseAdapter{

    private JSONArray ar;
    private Context ctx;

    public LivroAdapter(JSONArray ar,Context ctx){
        this.ar = ar;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return ar.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return ar.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_list_livro,parent,false);
        }

        TextView titulo,autor,id;
        ImageView disponivel,indisponivel;

        titulo = (TextView) view.findViewById(R.id.titulo);
        autor = (TextView) view.findViewById(R.id.autor);
        disponivel = (ImageView) view.findViewById(R.id.disponivel);
        indisponivel= (ImageView) view.findViewById(R.id.indisponivel);
        id          = (TextView) view.findViewById(R.id.id);

        JSONObject ob = (JSONObject) getItem(position);

        if(ob.optString("status").equals("D")){
            disponivel.setVisibility(View.VISIBLE);
        }else{
            indisponivel.setVisibility(View.VISIBLE);
        }

        titulo.setText(ob.optString("titulo"));
        autor.setText(ob.optString("autor"));
        id.setText(ob.toString());


        return view;
    }
}
