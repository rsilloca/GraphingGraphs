package com.pandita.graphinggraphs;


import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class GenerateTree extends Fragment implements View.OnClickListener {

    Spinner opciones;
    String[] opc = {"ancho", "largo"};
    ImageButton deshacer, rehacer, clear, pincel;
    ImageButton black, red, green, blue;
    int[] colors = {0xFF000000, 0xFFFF0000, 0xFF0B930B, 0xFF0000FF}; //black, red, green, blue
    int[] backgroundColors = {R.drawable.redondeado_brush_black, R.drawable.redondeado_brush_red,
            R.drawable.redondeado_brush_green, R.drawable.redondeado_brush_blue};
    Drawing drawing;
    Button generarArbol, aceptar;
    Graph graph;
    Tree tree = new Tree();
    Vertice raiz;
    Activity context;
    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    String algoritmo = "";
    AlertDialog dialog;
    View generateTree;

    // Búsqueda a lo ancho
    ArrayList<Vertice> s = new ArrayList<>();
    ArrayList<Vertice> adyacentesList = new ArrayList<>();
    int position;

    // Búsqueda a lo largo
    Vertice w;

    public GenerateTree() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        generateTree = inflater.inflate(R.layout.fragment_generate_tree, container, false);
        inicializarComponentes();
        return generateTree;
    }

    @Override
    public void onClick(View v) {
        if (v == deshacer) {
            drawing.deshacer();
        } else if (v == rehacer) {
            drawing.rehacer();
        } else if (v == clear) {
            drawing.clear();
        } else if (v == generarArbol) {
            graph = drawing.getGraph();
            if (graph.esConexo()) {
                if (opciones.getSelectedItemPosition() == 0) {
                    busquedaALoAncho();
                } else {
                    busquedaALoLargo();
                }
                texto();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment drawTree = new DrawAndViewTree();
                Bundle datos = new Bundle();
                datos.putSerializable("tree", tree);
                datos.putString("texto", texto);
                datos.putString("algoritmo", algoritmo);
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

    public void busquedaALoAncho() {
        inicializar();
        while (agregarLados()) {
            actualizarS();
        }
    }

    public void busquedaALoLargo() {
        inicializar(); // Paso 1
        while (true) {
            if (!agregarLado()) { // Paso 2
                if (termino()) { //Paso 3
                    break;
                } else {
                    Vertice padre = getPadre(w);
                    w = padre; // Paso 4
                }
            }
        }
    }

    public void inicializar(){
        tree = new Tree();
        raiz = graph.getVertice(0);
        raiz.setNivel(1);
        tree.getVertices().add(raiz);
        algoritmo += "raiz: " + letters[raiz.getId()];
        s.add(raiz); // Búsqueda a lo ancho
        position = 0; // Búsqueda a lo ancho
        w = raiz; // Búsqueda a lo largo
        System.out.println("w es raiz");
    }

    public boolean agregarLados() {
        if (position < graph.getVertices().size()) {
            ArrayList<Edge> adyacentes = graph.getAdjacentEdges(s.get(position).getId());
            Vertice actual = s.get(position);
            Vertice adyacente;

            for (Edge e : adyacentes) { //Recorremos la lista de aristas adyacentes
                if (e.getV1().getId() == actual.getId()) {
                    adyacente = graph.getVertice(e.getV2().getId());
                } else {
                    adyacente = graph.getVertice(e.getV1().getId());
                }
                if (!s.contains(adyacente)) { // Agregamos si no forma ciclo
                    algoritmo += "\nagregamos " + letters[adyacente.getId()]
                            + " porque no forma ciclo";
                    tree.addEdge(e);
                    adyacentesList.add(adyacente);
                }
            }
            position++; //Saltamos al siguiente vertice
            return true;
        }
        return false;
    }

    public void actualizarS() {
        for (Vertice v : adyacentesList) {
            s.add(v);
        }
        adyacentesList.clear();
    }

    String texto = "";
    public void texto() {
        texto += "vertices: \n";
        for (Vertice v: graph.getVertices()) {
            texto += " " + letters[v.getId()];
        }
        texto += "\n aristas: \n";
        for (Edge e: graph.getEdges()) {
            texto += "" + letters[e.getV1().getId()] + " con " + letters[e.getV2().getId()] + " ";
        }
    }

    public boolean agregarLado() {
        if (null != w) {
            System.out.println("w es " + letters[w.getId()] + "y su nivel " + w.getNivel());
        } else {
            System.out.println("w es nulo");
        }
        Edge menor = getAdjacentMinor(w.getId());
        if (null != menor) {
            System.out.println("adyacentes a: " + letters[w.getId()]);
            System.out.println("la arista menor es: " + letters[menor.getV1().getId()] + " con " + letters[menor.getV2().getId()]);
            Vertice adyacente;
            if (menor.getV1().getId() == w.getId()) {
                adyacente = menor.getV2();
            } else {
                adyacente = menor.getV1();
            }
            tree.addEdge(menor);
            if (null != adyacente) {
                System.out.println("adyacente menor es: " + letters[adyacente.getId()]);
            } else {
                System.out.println("adyacente es nulo");
            }
            w = adyacente;
            return true;
        }
        return false;
    }

    public Edge getAdjacentMinor(int id) {
        ArrayList<Edge> adyacentes = graph.getAdjacentEdges(id);
        if (!adyacentes.isEmpty()) {
            Vertice adyacente;
            for (Edge  e: adyacentes) {
                if (e.getV1().getId() == id){
                    adyacente = e.getV2();
                } else {
                    adyacente = e.getV1();
                }
                if (!tree.getVertices().contains(adyacente)) {
                    return e;
                }
            }
        }
        return null;
    }

    public boolean termino() {
        return w.getId() == raiz.getId();
    }

    public Vertice getPadre(Vertice vertice){
        ArrayList<Vertice> padres = new ArrayList<>();
        for (Vertice v: tree.getVertices()) {
            if ((v.getNivel() + 1) == vertice.getNivel()) {
                System.out.println("posible padre es " + letters[v.getId()]);
                padres.add(v);
            }
        }
        for (Vertice v: padres) {
            for (Edge e: tree.getEdges()) {
                if (e.getV1() == v && e.getV2() == vertice
                        || e.getV1() == vertice && e.getV2() == v) {
                    return v;
                }
            }
        }
        return vertice;
    }

    public void errorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View miView = context.getLayoutInflater().inflate(R.layout.error_message, null);
        aceptar = miView.findViewById(R.id.aceptar);
        aceptar.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }

    public void inicializarComponentes() {
        opciones = generateTree.findViewById(R.id.opciones);
        ArrayAdapter<String> o = new ArrayAdapter<>(getContext(), R.layout.spinner_item_me, opc);
        opciones.setAdapter(o);
        pincel = generateTree.findViewById(R.id.pincel);
        black = generateTree.findViewById(R.id.black);
        black.setOnClickListener(this);
        red = generateTree.findViewById(R.id.red);
        red.setOnClickListener(this);
        green = generateTree.findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = generateTree.findViewById(R.id.blue);
        blue.setOnClickListener(this);
        drawing = generateTree.findViewById(R.id.viewDrawing);
        drawing.setContext(getActivity());
        deshacer = generateTree.findViewById(R.id.deshacer);
        deshacer.setOnClickListener(this);
        rehacer = generateTree.findViewById(R.id.rehacer);
        rehacer.setOnClickListener(this);
        clear = generateTree.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        generarArbol = generateTree.findViewById(R.id.generarArbol);
        generarArbol.setOnClickListener(this);
        context = getActivity();
    }

}
