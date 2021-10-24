package com.pandita.graphinggraphs;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

public class ViewGeneratedMatrix extends Fragment implements View.OnClickListener {

    Graph graph;
    GridLayout gridMatrix;
    Context context;
    Button back;
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    public ViewGeneratedMatrix() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewGeneratedMatrix = inflater.inflate(R.layout.fragment_view_generated_matrix, container, false);

        graph = (Graph) getArguments().getSerializable("graph");
        context = getActivity().getApplicationContext();
        gridMatrix = viewGeneratedMatrix.findViewById(R.id.gridMatrix);
        back = viewGeneratedMatrix.findViewById(R.id.back);
        back.setOnClickListener(this);
        generateMatrix();

        return viewGeneratedMatrix;
    }

    public void generateMatrix(){
        int size = graph.getVerticesCount() + 1;
        gridMatrix.setColumnCount(size);
        gridMatrix.setRowCount(size);

        // Inicializamos el GridLayout (Matriz)
        for (int i=0; i<size; i++){
            if(i == 0){
                Button blank = new Button(context, null, R.style.buttonMatrixBlank, R.style.buttonMatrixBlank);
                gridMatrix.addView(blank);
            } else {
                Button node = new Button(context, null, R.style.buttonMatrixNode, R.style.buttonMatrixNode);
                node.setText(letters[i-1] + "");
                gridMatrix.addView(node);
            }
        }
        int countNode = 1;
        for (int i=size; i<Math.pow(size,2); i++) {
            if (i % size == 0){
                Button node = new Button(context, null, R.style.buttonMatrixNode, R.style.buttonMatrixNode);
                node.setText(letters[countNode-1] + "");
                gridMatrix.addView(node);
                countNode++;
            } else {
                Button buttonMatrix = new Button(context, null, R.style.buttonMatrix, R.style.buttonMatrix);
                if (graph.isValued()) {
                    buttonMatrix.setText("-");
                } else {
                    buttonMatrix.setText("0");
                }
                gridMatrix.addView(buttonMatrix);
            }
        }

        // Colocamos los pesos de las aristas en los espacios de la matriz
        for (Edge edge: graph.getEdges()) {
            int value1 = edge.getV1().getId() + 1;
            int value2 = edge.getV2().getId() + 1;
            Button b1 = (Button) gridMatrix.getChildAt((size*value1) + value2);
            Button b2 = (Button) gridMatrix.getChildAt((size*value2) + value1);
            if (graph.isValued()){
                b1.setText(edge.getWeight() + "");
                b2.setText(edge.getWeight() + "");
            } else {
                b1.setText("1");
                b2.setText("1");
            }
            b1.setBackgroundColor(edge.getColor());
            b1.setTextColor(0xFFFFFFFF);
            b2.setBackgroundColor(edge.getColor());
            b2.setTextColor(0xFFFFFFFF);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("graph", graph);
            Fragment drawGraph = new DrawGraph();
            drawGraph.setArguments(bundle);
            transaction.replace(R.id.managerFragments, drawGraph);
            transaction.commit();
        }
    }
}
