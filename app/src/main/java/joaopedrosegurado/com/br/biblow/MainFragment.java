package joaopedrosegurado.com.br.biblow;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private ListView lvMain;
    private MainActivity main;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);
        main = (MainActivity) getActivity();
        Log.d("view null", "" + (view == null));
        lvMain = (ListView) view.findViewById(R.id.lvMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txvOb = (TextView) view.findViewById(R.id.id);
                try {
                    JSONObject ob = new JSONObject(txvOb.getText().toString());
                    main.getLf().preencheInfos(ob);
                    if(!main.isTablet()){
                        FragmentTransaction ft = main.getFm().beginTransaction();
                        ft.setCustomAnimations(R.animator.fade_up, R.animator.fadeout, R.animator.fade_up, R.animator.fadeout);
                        ft.show(main.getLf());
                        ft.hide(main.getMf());
                        ft.addToBackStack(null);
                        ft.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public ListView getLvMain() {
        return lvMain;
    }
}
