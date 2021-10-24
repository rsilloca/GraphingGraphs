package com.pandita.graphinggraphs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Graph implements Serializable {

    private ArrayList<Vertice> vertices = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private boolean isValued = false; // es valorado?

    public Graph() {
    }

    public Graph(int verticesCount) {
        for (int i = 1; i <= verticesCount; i++) {
            addVertice();
        }
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
        return isValued;
    }

    public void setValued(boolean valued) {
        isValued = valued;
    }

    public void addVertice(Vertice v) {
        vertices.add(v);
    }

    public void addEdge(Edge e) {
        if (!isExist(e)) {
            edges.add(e);
        }

    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public int getEdgesCount() {
        return edges.size();
    }

    public boolean isExist(Edge edge) { // edge: nueva arista
        for (Edge e: edges) {
            if (e.getV1() == edge.getV1() && e.getV2() == edge.getV2()
                    || e.getV2() == edge.getV1() && e.getV1() == edge.getV2()) {
                return true;
            }
        }
        return false;
    }

    public Vertice getVertice(int id) {
        return vertices.get(id);
    }

    public Edge getEdge(int idV1, int idV2) {
        for (Edge e: edges) {
            if (e.getV1().getId() == idV1 && e.getV2().getId() == idV2
                    || e.getV2().getId() == idV1 && e.getV1().getId() == idV2) {
                return e;
            }
        }
        return null;
    }

    public Vertice deleteVertice() {
        if (!vertices.isEmpty()) {
            int size = vertices.size();
            return vertices.remove(size - 1);
        }
        return null;
    }

    public Edge deleteEdge() {
        if (!edges.isEmpty()) {
            int size = edges.size();
            return edges.remove(size - 1);
        }
        return null;
    }

    public ArrayList<Edge> getAdjacentEdges(final int idVertice) {
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getV1().getId() == idVertice || edge.getV2().getId() == idVertice) {
                adjacentEdges.add(edge);
            }
        }
        Collections.sort(adjacentEdges, new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                int c1, c2;
                if (o1.getV1().getId() == idVertice) {
                    c1 = o1.getV2().getId();
                } else {
                    c1 = o1.getV1().getId();
                }
                if (o2.getV1().getId() == idVertice) {
                    c2 = o2.getV2().getId();
                } else {
                    c2 = o2.getV1().getId();
                }
                return new Integer(c1).compareTo(c2);
            }
        });
        return adjacentEdges;
    }

    public void addVertice() {
        int i = vertices.size();
        int altura = 100;
        int more = 432;
        if (i % 2 != 0) {
            this.vertices.add(new Vertice(i, 756, altura + ((i - 1) / 2) * more));
        } else {
            this.vertices.add(new Vertice(i, 324, altura + (i / 2) * more));
        }
        i = vertices.size();
        if (i > 4) {
            switch (i) {
                case 5: vertices.get(2).setX(108);
                    vertices.get(3).setX(972);
                    vertices.get(4).setX(540);
                    actualizarEdge(vertices.get(2));
                    actualizarEdge(vertices.get(3));
                    actualizarEdge(vertices.get(4));
                    break;
                case 6: vertices.get(2).setX(108);
                    vertices.get(3).setX(972);
                    vertices.get(4).setX(324);
                    actualizarEdge(vertices.get(2));
                    actualizarEdge(vertices.get(3));
                    actualizarEdge(vertices.get(4));
                    break;
            }
        }
    }

    public void actualizarEdge(Vertice vertice) {
        for (int i = 0; i < edges.size(); i++) {
            Edge aux = edges.get(i);
            if (aux.getV1().getId() == vertice.getId()) {
                aux.getV1().setX(vertice.getX());
            } else if (aux.getV2().getId() == vertice.getId()) {
                aux.getV2().setX(vertice.getX());
            }
        }
    }

    public boolean esConexo(){
        for (Vertice v: vertices) {
            int count  = 0;
            for (Edge e: edges) {
                if (e.getV1() == v || e.getV2() == v) {
                    count++;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        return true;
    }

}
