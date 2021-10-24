package com.pandita.graphinggraphs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class DrawGraph extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton viewMatrix, deshacer, rehacer, clear, pincel;
    ImageButton black, red, green, blue;
    int[] colors = {0xFF000000, 0xFFFF0000, 0xFF0B930B, 0xFF0000FF}; //black, red, green, blue
    int[] backgroundColors = {R.drawable.redondeado_brush_black, R.drawable.redondeado_brush_red,
            R.drawable.redondeado_brush_green, R.drawable.redondeado_brush_blue};
    Drawing drawing;
    Graph graph;
    Context context;
    CheckBox valued;
    View drawGraph;

    public DrawGraph() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        drawGraph = inflater.inflate(R.layout.fragment_draw_graph, container, false);
        inicializarComponentes();
        return drawGraph;
    }
    @Override
    public void onClick(View view) {
        if (view == viewMatrix){
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            graph = drawing.getGraph();
            bundle.putSerializable("graph", graph);
            Fragment viewGeneratedMatrix = new ViewGeneratedMatrix();
            viewGeneratedMatrix.setArguments(bundle);
            transaction.replace(R.id.managerFragments, viewGeneratedMatrix);
            transaction.commit();
        } else if (view == deshacer) {
            drawing.deshacer();
        } else if (view == rehacer) {
            drawing.rehacer();
        } else if (view == clear) {
            drawing.clear();
        } else if (view == black) {
            drawing.setColor(colors[0]);
            pincel.setBackgroundResource(backgroundColors[0]);
        } else if (view == red) {
            drawing.setColor(colors[1]);
            pincel.setBackgroundResource(backgroundColors[1]);
        } else if (view == green) {
            drawing.setColor(colors[2]);
            pincel.setBackgroundResource(backgroundColors[2]);
        } else if (view == blue) {
            drawing.setColor(colors[3]);
            pincel.setBackgroundResource(backgroundColors[3]);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        drawing.clear();
        drawing.setValued(isChecked);
    }

    public void inicializarComponentes() {
        context = getActivity().getApplicationContext();
        drawing = drawGraph.findViewById(R.id.viewDrawing);
        drawing.setContext(getActivity());
        viewMatrix = drawGraph.findViewById(R.id.viewMatrix);
        viewMatrix.setOnClickListener(this);
        deshacer = drawGraph.findViewById(R.id.deshacer);
        deshacer.setOnClickListener(this);
        rehacer = drawGraph.findViewById(R.id.rehacer);
        rehacer.setOnClickListener(this);
        clear = drawGraph.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        valued = drawGraph.findViewById(R.id.isValued);
        valued.setOnCheckedChangeListener(this);
        pincel = drawGraph.findViewById(R.id.pincel);
        black = drawGraph.findViewById(R.id.black);
        black.setOnClickListener(this);
        red = drawGraph.findViewById(R.id.red);
        red.setOnClickListener(this);
        green = drawGraph.findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = drawGraph.findViewById(R.id.blue);
        blue.setOnClickListener(this);
        if (getArguments() != null) {
            graph = (Graph) getArguments().getSerializable("graph");
            drawing.setGraph(graph);
            if (graph.isValued()) {
                valued.setChecked(true);
            }
        }
    }

}
