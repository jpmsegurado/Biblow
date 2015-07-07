package joaopedrosegurado.com.br.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import joaopedrosegurado.com.br.biblow.MainActivity;
import joaopedrosegurado.com.br.biblow.R;
import joaopedrosegurado.com.br.data.BiblowProvider;


public class InteresseAdapter extends CursorAdapter {


    public InteresseAdapter(Context context, Cursor c)  {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.item_interesse,parent,false);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView autor,titulo,editora;

        autor = (TextView) view.findViewById(R.id.autor);
        titulo = (TextView) view.findViewById(R.id.titulo);
        editora = (TextView) view.findViewById(R.id.editora);

        autor.setText(cursor.getString(cursor.getColumnIndex("autor")));

        titulo.setText(cursor.getString(cursor.getColumnIndex("titulo")));

        editora.setText(cursor.getString(cursor.getColumnIndex("editora")));

    }
}
