package com.pandita.graphinggraphs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DijkstraAlgorithm extends Fragment implements View.OnClickListener {

    ImageButton deshacer, close, rehacer, clear, pincel, verDetalles;
    ImageButton black, red, green, blue;
    int[] colors = {0xFF000000, 0xFFFF0000, 0xFF0B930B, 0xFF0000FF}; //black, red, green, blue
    int[] backgroundColors = {R.drawable.redondeado_brush_black, R.drawable.redondeado_brush_red,
                              R.drawable.redondeado_brush_green, R.drawable.redondeado_brush_blue};
    Button calcularCamino, cancelar, crearDialogo;
    TextView textoResultado, detalles;
    Drawing drawing;
    Graph graph;
    Activity context;
    String details = "";
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};
    Spinner v1, v2;
    AlertDialog dialog;
    View dijkstraAlgorithm;
    // Para calcular la ruta
    Map<Integer, Integer> allVertices = new HashMap<>(); //key = vertice.getId(); value = peso de arista adyacente;
    int camino, alpha = 2147483647, verticeActual, valorVerticeActual;

    public DijkstraAlgorithm() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dijkstraAlgorithm = inflater.inflate(R.layout.fragment_dijkstra_algorithm, container, false);
        inicializarComponentes();
        return dijkstraAlgorithm;
    }

    @Override
    public void onClick(View v) {
        if (v == deshacer) {
            drawing.deshacer();
        } else if (v == rehacer) {
            drawing.rehacer();
        } else if (v == clear) {
            drawing.clear();
            drawing.setValued(true);
            details = "";
            textoResultado.setText("Dibujando...");
        } else if (v == crearDialogo) {
            crearDialogo();
        } else if (v == calcularCamino) {
            details = "";
            calcularCamino(v1.getSelectedItemPosition(), v2.getSelectedItemPosition());
            textoResultado.setText("La ruta más corta es de " + camino + " unidades.");
            dialog.cancel();
        } else if (v == cancelar) {
            dialog.cancel();
        } else if (v == verDetalles) {

        } else if (v == close) {
            dialog.cancel();
        } else if (v == black) {
            drawing.setColor(colors[0]);
            pincel.setBackgroundResource(backgroundColors[0]);
        } else if (v == red) {
            drawing.setColor(colors[1]);
            pincel.setBackgroundResource(backgroundColors[1]);
        } else if (v == green) {
            drawing.setColor(colors[2]);
            pincel.setBackgroundResource(backgroundColors[2]);
        } else if (v == blue) {
            drawing.setColor(colors[3]);
            pincel.setBackgroundResource(backgroundColors[3]);
        }
    }

    public void calcularCamino(int a, int z) {
        inicializar(a); // Paso 1
        camino = -1; // No hay camino aún
        while (isFinish(z)) { // Si Z pertenece a T, continuamos
            buscarVerticeMinimo(); // Escogemos el vértice mínimo
            actualizarVertices(); // Actualizamos los valores de los vertices adyacentes al vertice actual
        }
    }

    public void inicializar(int a) {
        details += "[PASO 1]    Inicializamos: \n";
        for (Vertice vertice : graph.getVertices()) {
            if (vertice.getId() == a) {
                allVertices.put(vertice.getId(), 0);
                details += "* " + letters[a] + " = 0\n";
            } else {
                allVertices.put(vertice.getId(), alpha);
                details += "* " + letters[vertice.getId()] + " = alpha\n";
            }
        }
    }

    public void buscarVerticeMinimo() {
        Iterator iterator = allVertices.entrySet().iterator();
        Map.Entry vertice;
        int verticeMinimo = -1;
        int valorMenor = alpha;
        details += "\n[PASO 3]    Recorremos el conjunto de vertices y tomamos el de menor valor:\n";
        while (iterator.hasNext()) { // Recorremos el HashMap
            vertice = (Map.Entry) iterator.next();
            if ((int) vertice.getValue() < valorMenor) { // Comparamos los valores
                valorMenor = (int) vertice.getValue();   // de todos los vertices
                verticeMinimo = (int) vertice.getKey();  // y elegimos el menor
            }
        }
        agregarDetalles(verticeMinimo, valorMenor);
        verticeActual = verticeMinimo;
        valorVerticeActual = valorMenor;
        camino = valorMenor;
    }

    public void actualizarVertices() {
        Iterator iterator = allVertices.entrySet().iterator();
        Map.Entry vertice;
        List<Edge> adjacentEdges = graph.getAdjacentEdges(verticeActual);
        details += "\n[PASO 4]   Actualizamos los valores de los vertices adyacentes a " + letters[verticeActual] + ":\n";
        int numberAdjacentVertice;
        for (Edge edge : adjacentEdges) {
            if (edge.getV1().getId() == verticeActual) {
                numberAdjacentVertice = edge.getV2().getId();
            } else {
                numberAdjacentVertice = edge.getV1().getId();
            }
            while (iterator.hasNext()) { // Recorremos el HashMap
                vertice = (Map.Entry) iterator.next();
                if ((int) vertice.getKey() == numberAdjacentVertice) { // Si el vertice actual es vertice adyacente
                    int menor = Math.min((int) vertice.getValue(), valorVerticeActual + edge.getWeight());
                    allVertices.put(numberAdjacentVertice, menor); //actualizamos su valor
                    details += " >> "  + letters[numberAdjacentVertice] + " con valor mínimo de " + menor + "\n";
                    break;
                }
            }
            iterator = allVertices.entrySet().iterator();
        }
    }

    public boolean isFinish(int z) {
        details += "\n[PASO 2]   Revisamos si hemos llegado a " + letters[z] + ": ";
        Iterator iterator = allVertices.entrySet().iterator();
        Map.Entry vertice;
        while (iterator.hasNext()) {
            vertice = (Map.Entry) iterator.next(); // Recorremos el HashMap
            if ((int) vertice.getKey() == z) { // Si encontramos a Z
                details += "No hemos llegado aún.\n";
                return true;
            }
        }
        details += "Llegamos, la ruta es de " + camino + " unidades.\n";
        return false;
    }

    public void agregarDetalles(int verticeMinimo, int valorMenor) {
        Iterator iterator = allVertices.entrySet().iterator();
        Map.Entry vertice;
        details += "En este caso elegimos a " + letters[verticeMinimo] +
                " por tener como menor valor a " + valorMenor + "\n";
        allVertices.remove(verticeMinimo); // Eliminamos de T a el vertice que escogimos
        String listaRestante = "";
        iterator = allVertices.entrySet().iterator();
        while (iterator.hasNext()) {
            vertice = (Map.Entry) iterator.next();
            listaRestante += "" + letters[(int) vertice.getKey()] + ", ";
        }
        String T = "";
        if (!listaRestante.equals("")) {
            T = listaRestante.substring(0, listaRestante.length() - 2);
        }
        details += "Hacemos a " + letters[verticeMinimo] + " permanente y lo eliminamos del conjunto de vertices quedando " +
                "T = {" + T + "}\n";
    }

    public void crearDialogo() {
        graph = drawing.getGraph(); // Obtenemos el grafo dibujado
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View miView = context.getLayoutInflater().inflate(R.layout.alert_dijkstra, null);
        v1 = miView.findViewById(R.id.v1);
        v2 = miView.findViewById(R.id.v2);
        String[] opciones = new String[graph.getVertices().size()];
        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = letters[i];
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_me, opciones);
        v1.setAdapter(adapter);
        v2.setAdapter(adapter);
        calcularCamino = miView.findViewById(R.id.calcularRuta);
        calcularCamino.setOnClickListener(this);
        cancelar = miView.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }

    public void mostrarDetalles() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View miView = context.getLayoutInflater().inflate(R.layout.alert_details, null);
        detalles = miView.findViewById(R.id.details);
        detalles.setText(details);
        close = miView.findViewById(R.id.close);
        close.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }

    public void inicializarComponentes() {
        context = getActivity();
        drawing = dijkstraAlgorithm.findViewById(R.id.viewDrawing);
        drawing.setContext(getActivity());
        drawing.setValued(true); // El grafo debe ser siempre valorado
        crearDialogo = dijkstraAlgorithm.findViewById(R.id.crearDialogo);
        crearDialogo.setOnClickListener(this);
        deshacer = dijkstraAlgorithm.findViewById(R.id.deshacer);
        deshacer.setOnClickListener(this);
        rehacer = dijkstraAlgorithm.findViewById(R.id.rehacer);
        rehacer.setOnClickListener(this);
        clear = dijkstraAlgorithm.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        textoResultado = dijkstraAlgorithm.findViewById(R.id.textoResultado);
        verDetalles = dijkstraAlgorithm.findViewById(R.id.verDetalles);
        verDetalles.setOnClickListener(this);
        pincel = dijkstraAlgorithm.findViewById(R.id.pincel);
        black = dijkstraAlgorithm.findViewById(R.id.black);
        black.setOnClickListener(this);
        red = dijkstraAlgorithm.findViewById(R.id.red);
        red.setOnClickListener(this);
        green = dijkstraAlgorithm.findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = dijkstraAlgorithm.findViewById(R.id.blue);
        blue.setOnClickListener(this);
    }

}
