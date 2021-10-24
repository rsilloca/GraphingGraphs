package com.pandita.graphinggraphs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class InsertMatrix extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton addColumn, removeColumn;
    Button viewGraph, aceptar, cancelar, resetear, aux;
    EditText newValor;
    int colorWhite = 0xFFFFFFFF, colorOrange = 0xFFE4B9A0;
    GridLayout gridMatrix;
    TextView countColumns, valorActual;
    CheckBox valuedMatrix;
    AlertDialog dialog;
    Context context;
    Graph graph;
    int sizeGrid;
    boolean isValued;
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    View insertMatrix;

    public InsertMatrix() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        insertMatrix = inflater.inflate(R.layout.fragment_insert_matrix, container, false);
        inicializarComponentes();
        return insertMatrix;
    }

    @Override
    public void onClick(View view) {
        if(view == addColumn) {
            createInsertMatrix(sizeGrid + 1);
        } else if (view == removeColumn){
            createInsertMatrix(sizeGrid - 1);
        } else if (view == viewGraph){
            generateViewGraph();
        } else if (view == cancelar) {
            dialog.cancel();
        } else if (view == aceptar){
            aux.setText(newValor.getText());
            searchSimetric(aux);
            dialog.cancel();
        } else if (view == resetear) {
            aux.setText("-");
            searchSimetric(aux);
            dialog.cancel();
        } else{
            updateValues((Button) view);
        }
    }

    private void generateGrid(int size, Boolean isValued){
        gridMatrix.setColumnCount(size);
        gridMatrix.setRowCount(size);
        String text = "0";
        if (isValued) {
            text = "-";
        }
        //Inicializamos la primera fila del GridLayout
        for (int i = 0; i < size; i++){
            if(i == 0) {
                Button blank = new Button(context, null, R.style.buttonMatrixBlank, R.style.buttonMatrixBlank);
                gridMatrix.addView(blank);
            } else {
                Button node = new Button(context, null, R.style.buttonMatrixNode, R.style.buttonMatrixNode);
                node.setText(letters[i-1] + "");
                gridMatrix.addView(node);
            }
        }
        //Inicializamos las filas restantes
        int fila = 1, columna;
        for (int i = size; i < Math.pow(size,2); i++) {
            columna = i % size;
            if (columna == 0){
                Button node = new Button(context, null, R.style.buttonMatrixNode, R.style.buttonMatrixNode);
                node.setText(letters[fila-1] + "");
                gridMatrix.addView(node);
                fila++;
            } else {
                Button buttonMatrix = new Button(context,null, R.style.buttonMatrix, R.style.buttonMatrix);
                buttonMatrix.setText(text);
                String tag = (fila - 1) + " " + columna;
                buttonMatrix.setTag(tag);
                buttonMatrix.setOnClickListener(this);
                gridMatrix.addView(buttonMatrix);
            }
        }
    }

    public void createInsertMatrix(int size){
        if (size > 1 && size < 8) { // Limita la matriz entre 1 y 6 vertices
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle data = new Bundle();
            Fragment insertMatrix = new InsertMatrix();
            data.putInt("sizeGrid", size);
            data.putBoolean("isValued", valuedMatrix.isChecked());
            insertMatrix.setArguments(data);
            transaction.replace(R.id.managerFragments, insertMatrix);
            transaction.commit();
        }
    }
    public void generateViewGraph() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle data = new Bundle();
        Fragment viewGeneratedGraph = new ViewGeneratedGraph();
        graph = generateGraph();
        data.putSerializable("graph", graph);
        viewGeneratedGraph.setArguments(data);
        transaction.replace(R.id.managerFragments, viewGeneratedGraph);
        transaction.commit();
    }
    public void updateValues(Button button) {
        aux = button; //Guardamoms el valor del boton que ha sido presionado para luego actualizarlo
        if (isValued){
            createAlertDialog(button);
        } else {
            int num = Integer.parseInt(button.getText().toString());
            if(num == 0){
                button.setText("1");
                button.setBackgroundColor(colorOrange);
            } else {
                button.setText("0");
                button.setBackgroundColor(colorWhite);
            }
            searchSimetric(button);
        }
    }
    public void searchSimetric(Button button){
        String tag = button.getTag().toString();
        String[] arrayTag = tag.split(" ");
        String newTag = arrayTag[1] + " " + arrayTag[0];
        Button b;
        String text = "0";
        if (isValued){
            text = "-";
        }
        for (int i = sizeGrid + 1; i < gridMatrix.getChildCount(); i++) {
            b = (Button) gridMatrix.getChildAt(i);
            if (i % sizeGrid != 0 && b.getTag().toString().equals(newTag)) {
                b.setText(button.getText());
                if (!button.getText().toString().equals(text)) {
                    button.setBackgroundColor(colorOrange);
                    b.setBackgroundColor(colorOrange);
                } else {
                    button.setBackgroundColor(colorWhite);
                    b.setBackgroundColor(colorWhite);
                }
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String s = "0";
        Button b;
        isValued = isChecked;
        if (isChecked) {
            s = "-";
        }
        for (int i = sizeGrid + 1; i < Math.pow(sizeGrid, 2); i++){
            b = (Button) gridMatrix.getChildAt(i);
            if (i % sizeGrid != 0) {
                b.setText(s);
                b.setBackgroundColor(colorWhite);
            }
        }
    }
    public void createAlertDialog(Button viewButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View miView = getActivity().getLayoutInflater().inflate(R.layout.alert_input_data, null);
        valorActual = miView.findViewById(R.id.valorActual);
        valorActual.setText(viewButton.getText());
        newValor = miView.findViewById(R.id.newValor);
        cancelar = miView.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(this);
        aceptar = miView.findViewById(R.id.aceptar);
        aceptar.setOnClickListener(this);
        resetear = miView.findViewById(R.id.resetear);
        resetear.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }
    public Graph generateGraph(){
        graph = new Graph(sizeGrid-1);
        String value = "0";
        if (valuedMatrix.isChecked()) {
            graph.setValued(true);
            value = "-";
        }
        int weigth, v1, v2;
        for (int i = sizeGrid + 1; i < Math.pow(sizeGrid, 2); i++){ //Recorremos el grid
            Button button = (Button) gridMatrix.getChildAt(i);
            String text = button.getText().toString();
            if (i % sizeGrid != 0 && !value.equals(text)) {
                weigth = Integer.parseInt(text); //Peso de la arista
                String tag = button.getTag().toString();
                String[] indices = tag.split(" ");
                v1 = Integer.parseInt(indices[0]) - 1; //Indice del primer vertice
                v2 = Integer.parseInt(indices[1]) - 1; //Indice del segundo vertice
                graph.addEdge(new Edge(graph.getVertice(v1), graph.getVertice(v2), weigth));
            }
        }
        return graph;
    }

    public void inicializarComponentes() {
        context = getActivity().getApplicationContext();
        gridMatrix = insertMatrix.findViewById(R.id.grid);

        //Inicializamos el gridLayout de acuerdo al tamaño de la matriz
        if ( getArguments() != null ){
            sizeGrid = getArguments().getInt("sizeGrid");
            isValued = getArguments().getBoolean("isValued");
        } else {
            sizeGrid = 3;
        }
        generateGrid(sizeGrid, isValued);

        //Inicializamos los controladores de tamaño de la matriz
        addColumn = insertMatrix.findViewById(R.id.addColumn);
        addColumn.setOnClickListener(this);
        removeColumn = insertMatrix.findViewById(R.id.removeColumn);
        removeColumn.setOnClickListener(this);

        //Inicializamos el botón que iniciará el fragment con el grafo dibujado
        viewGraph = insertMatrix.findViewById(R.id.viewGraph);
        viewGraph.setOnClickListener(this);

        //TextView que mostrará el tamaño actual de la matriz
        countColumns = insertMatrix.findViewById(R.id.countColumns);
        countColumns.setText((sizeGrid-1) + "");

        //Checkbox que controla si la matriz ingresada es valorada o no
        valuedMatrix = insertMatrix.findViewById(R.id.isValued);
        valuedMatrix.setChecked(isValued);
        valuedMatrix.setOnCheckedChangeListener(this);
    }

}