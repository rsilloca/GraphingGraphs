package com.pandita.graphinggraphs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GenerateMinimalTree extends Fragment implements View.OnClickListener {

    Spinner opciones;
    String[] opc = {"Prim", "Kruskal"};
    ImageButton deshacer, rehacer, clear, pincel;
    ImageButton black, red, green, blue;
    AlertDialog dialog;
    int[] colors = {0xFF000000, 0xFFFF0000, 0xFF0B930B, 0xFF0000FF}; //black, red, green, blue
    int[] backgroundColors = {R.drawable.redondeado_brush_black, R.drawable.redondeado_brush_red,
            R.drawable.redondeado_brush_green, R.drawable.redondeado_brush_blue};
    Drawing drawing;
    Button generarArbol, aceptar;
    View generateMinimalTree;
    Graph graph;
    Tree tree = new Tree();
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    // Algoritmo de Prim
    ArrayList<Vertice> s = new ArrayList<>();

    // Algoritmo de Kruskal
    ArrayList<Edge> t = new ArrayList<>();

    public GenerateMinimalTree() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        generateMinimalTree = inflater.inflate(R.layout.fragment_generate_minimal_tree, container, false);
        inicializarComponentes();
        return generateMinimalTree;
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
        } else if (v == generarArbol) {
            graph = drawing.getGraph();
            if (graph.esConexo()) {
                if (opciones.getSelectedItemPosition() == 0) {
                    algoritmoDePrim();
                } else {
                    algoritmoDeKruskal();
                }
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment drawTree = new DrawAndViewTree();
                Bundle datos = new Bundle();
                datos.putSerializable("tree", tree);
                drawTree.setArguments(datos);
                transaction.replace(R.id.managerFragments, drawTree);
                transaction.commit();
            } else {
                errorMessage();
            }
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
        } else if (v == aceptar) {
            dialog.cancel();
        }
    }

    public void algoritmoDePrim() {
        inicializar();
        while (!loEncontro()) {
            agregarLado();
        }
    }

    public void algoritmoDeKruskal() {
        iniciar();
        while (!loEncontro()) {
            agregarLadoK();
        }
    }

    public void inicializar() {
        Vertice raiz = graph.getVertice(0);
        raiz.setNivel(1);
        tree.getVertices().add(raiz);
        s.add(raiz);
    }

    public boolean loEncontro() {
        return tree.getEdges().size() == graph.getVertices().size() - 1;
    }

    public void agregarLado() {
        ArrayList<Edge> adyacentes = new ArrayList<>();
        //Obtenemos todas las aristas incidentes en los vertices del arbol
        for (Vertice v : s) { // Recorremos todas las aristas del grafo
            for (Edge e : graph.getAdjacentEdges(v.getId())) { // Buscamos los adyaventes
                if (!s.contains(e.getV1()) || !s.contains(e.getV2())) {
                    adyacentes.add(e);
                }
            }
        }
        // Oordenamos por peso
        Collections.sort(adyacentes, new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                if (new Integer(o1.getWeight()).compareTo(o2.getWeight()) == 0){
                    if (new Integer(o1.getV1().getId()).compareTo(o2.getV1().getId()) == 0) {
                        return new Integer(o1.getV2().getId()).compareTo(o2.getV2().getId());
                    }
                    return new Integer(o1.getV1().getId()).compareTo(o2.getV1().getId());
                }
                return new Integer(o1.getWeight()).compareTo(o2.getWeight());
            }
        });

        Edge menor = adyacentes.get(0); // La lista esta ordenada, tomamos el menor que es el primero
        tree.addEdge(menor);
        if (s.contains(menor.getV1())) {
            s.add(menor.getV2());
        } else {
            s.add(menor.getV1());
        }
    }

    public void iniciar() {
        t = graph.getEdges(); // Obtenemos todas las aristas
        Collections.sort(t, new Comparator<Edge>() { // Las ordenamos de acuerdo a su peso
            @Override
            public int compare(Edge o1, Edge o2) {
                if (new Integer(o1.getWeight()).compareTo(o2.getWeight()) == 0){
                     if (new Integer(o1.getV1().getId()).compareTo(o2.getV1().getId()) == 0) {
                             return new Integer(o1.getV2().getId()).compareTo(o2.getV2().getId());
                    }
                    return new Integer(o1.getV1().getId()).compareTo(o2.getV1().getId());
                }
                return new Integer(o1.getWeight()).compareTo(o2.getWeight());
            }
        });
    }

    public void agregarLadoK() {
        Edge menor = t.get(0);
        if (!hayCamino(menor)) { // Evitamos ciclos
            tree.addEdge(menor);
        }
        t.remove(menor);
    }

    ArrayList<Vertice> adyacentesK = new ArrayList<>();
    public boolean hayCamino(Edge m) {
        Vertice v1 = m.getV1();
        Vertice v2 = m.getV2();
        if (tree.getVertices().contains(v1) && tree.getVertices().contains(v2)) {
                adyacentesK.clear();
                adyacentesK.add(v1);
                int position = 0;
                while (position < adyacentesK.size()) {
                    if (compararAdyacentes(adyacentesK.get(position), v2)) {
                        return true;
                    }
                    position++;
                }
        }
        return false;
    }

    public boolean compararAdyacentes(Vertice v1, Vertice v2) {
        ArrayList<Edge> ad = getAdyacentes(v1);
        if (ad.isEmpty()) {
            if (adyacentesK.get(adyacentesK.size()-1) == v1) {
                return false;
            }
        }
        Vertice adyacente;
        for (Edge e: ad) {
            if (e.getV1() == v1) {
                adyacente = e.getV2();
            } else {
                adyacente = e.getV1();
            }
            if (adyacente.getId() == v2.getId()) {
                return true;
            }
            if (!adyacentesK.contains(adyacente)) {
                adyacentesK.add(adyacente);
            }
        }
        return false;
    }

    public ArrayList<Edge> getAdyacentes(Vertice v) {
        ArrayList<Edge> es = new ArrayList<>();
        for (Edge e: tree.getEdges()) {
            if (e.getV1().getId() == v.getId() || e.getV2().getId() == v.getId()) {
                if (!adyacentesK.contains(e.getV1()) || !adyacentesK.contains(e.getV2())) {
                    es.add(e);
                }
            }
        }
        return es;
    }

    public void errorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View miView = getActivity().getLayoutInflater().inflate(R.layout.error_message, null);
        aceptar = miView.findViewById(R.id.aceptar);
        aceptar.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }

    public void inicializarComponentes() {
        opciones = generateMinimalTree.findViewById(R.id.opciones);
        ArrayAdapter<String> o = new ArrayAdapter<>(getContext(), R.layout.spinner_item_me, opc);
        opciones.setAdapter(o);
        pincel = generateMinimalTree.findViewById(R.id.pincel);
        black = generateMinimalTree.findViewById(R.id.black);
        black.setOnClickListener(this);
        red = generateMinimalTree.findViewById(R.id.red);
        red.setOnClickListener(this);
        green = generateMinimalTree.findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = generateMinimalTree.findViewById(R.id.blue);
        blue.setOnClickListener(this);
        drawing = generateMinimalTree.findViewById(R.id.viewDrawing);
        drawing.setContext(getActivity());
        drawing.setValued(true);
        deshacer = generateMinimalTree.findViewById(R.id.deshacer);
        deshacer.setOnClickListener(this);
        rehacer = generateMinimalTree.findViewById(R.id.rehacer);
        rehacer.setOnClickListener(this);
        clear = generateMinimalTree.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        generarArbol = generateMinimalTree.findViewById(R.id.generarArbol);
        generarArbol.setOnClickListener(this);
        tree.setValued(true);
    }

}
