package com.pandita.graphinggraphs;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree implements Serializable {

    private int heigth = 0;
    private ArrayList<Vertice> vertices = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private boolean valued = false;

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertice> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public boolean isValued() {
        return valued;
    }

    public void setValued(boolean valued) {
        this.valued = valued;
    }

    public boolean tieneHijos(Vertice vertice) {
        for (Vertice v: vertices) {
            if (isAdjacent(vertice, v) && v.getNivel() == (vertice.getNivel() + 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdjacent(Vertice v1, Vertice v2) {
        for (Edge e : edges) {
            return e.getV1().getId() == v1.getId() && e.getV2().getId() == v2.getId()
                    || e.getV2().getId() == v1.getId() && e.getV1().getId() == v2.getId();
        }
        return false;
    }

    public void addEdge(Edge edge) {
        Vertice padre = null, hijo = null;
        if (vertices.contains(edge.getV1()) || vertices.contains(edge.getV2())) {
            if (vertices.contains(edge.getV1())) {
                padre = edge.getV1();
                hijo = edge.getV2();
            } else {
                padre = edge.getV2();
                hijo = edge.getV1();
            }
        } else if (edge.getV1().getId() < edge.getV2().getId()) {
                padre = edge.getV1();
                hijo  = edge.getV2();
                padre.setNivel(1);
                vertices.add(padre);
        }
        edges.add(edge);
        vertices.add(hijo);
        hijo.setNivel(padre.getNivel() + 1);
        if (hijo.getNivel() >  this.heigth + 1) {
            this.heigth += 1;
        }
    }

}
