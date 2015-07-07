package joaopedrosegurado.com.br.biblow;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FotoFragment extends Fragment {



    public FotoFragment() {}

    LineChartView chart ;
    private MainActivity main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foto, container, false);

        main = (MainActivity) getActivity();

        chart = (LineChartView) view.findViewById(R.id.chart);

        ContainerScrollType ctype;


        chart.setInteractive(true);

        List<PointValue> values = new ArrayList<PointValue>();
        List<Float> floatValues = new ArrayList<Float>();
        for(int i=0; i<10;i++){
            floatValues.add(i,Float.valueOf(i));
            values.add(new PointValue(i,i));
        }

        LineChartData data = new LineChartData();

        Line line = new Line(values);
        line.setColor(Color.BLUE);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        data.setLines(lines);

        Axis axis = Axis.generateAxisFromRange(0,10,1);
        data.setAxisXBottom(axis);

        chart.setLineChartData(data);

        chart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i2, PointValue point) {
                main.alert(point.toString());
            }

            @Override
            public void onValueDeselected() {

            }
        });

        return view;

    }

}
