package joaopedrosegurado.com.br.biblow;


import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import joaopedrosegurado.com.br.adapter.InteresseAdapter;
import joaopedrosegurado.com.br.data.BiblowProvider;

public class InteressesFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {


    public InteressesFragment() {
    }

    private static int URL_LOADER = 1;
    private ListView list;
    private MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interesses, container, false);

        list = (ListView)view.findViewById(R.id.lvMain);
        main = (MainActivity) getActivity();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(isVisible()){
            load();
        }
    }

    public void load(){
        //Cursor interesse = main.getContentResolver().query(BiblowProvider.CONTENT_URL,null,null,null,null);
        //InteresseAdapter ia = new InteresseAdapter(main,interesse);
        //list.setAdapter(ia);

        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch(id){
            case 1:
                return new CursorLoader(
                        getActivity(),
                        BiblowProvider.CONTENT_URL,
                        null,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, final Cursor data) {
        InteresseAdapter ia = new InteresseAdapter(main,data);
        list.setAdapter(ia);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!main.isTablet()) {
                    FragmentTransaction ft = main.getFm().beginTransaction();
                    ft.setCustomAnimations(R.animator.fade_up, R.animator.fadeout, R.animator.fade_up, R.animator.fadeout);
                    ft.show(main.getLif());
                    ft.hide(InteressesFragment.this);
                    ft.addToBackStack(null);
                    ft.commit();

                    main.getLif().preencheInfos(data,position);
                } else {
                    main.getLif().preencheInfos(data,position);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}
