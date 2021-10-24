package com.pandita.graphinggraphs;

import java.io.Serializable;

public class Edge implements Serializable {

    private int weight;
    private Vertice v1;
    private Vertice v2;
    private boolean loop = false;
    private int color = 0xFF000000; // default color: black

    public Edge(){}

    public Edge(Vertice v1, Vertice v2, int weight){
        setValues(v1, v2, weight);
    }

    public Edge(Vertice v1, Vertice v2, int weight, int color){
        setValues(v1, v2, weight);
        this.color = color;
    }

    public Vertice getV1() {
        return v1;
    }

    public void setV1(Vertice v1) {
        this.v1 = v1;
    }

    public Vertice getV2() {
        return v2;
    }

    public void setV2(Vertice v2) {
        this.v2 = v2;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isLoop(){
        return loop;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setValues(Vertice v1, Vertice v2, int weight) {
        if (v1.getId() > v2.getId()){
            this.v1 = v2;
            this.v2 = v1;
        } else if (v1.getId() == v2.getId()){
            this.v1 = v1;
            this.v2 = v2;
            this.loop = true;
        } else {
            this.v1 = v1;
            this.v2 = v2;
        }
        this.weight = weight;
    }
}
