package com.pandita.graphinggraphs;

import java.io.Serializable;

public class Vertice implements Serializable {

    private int id; // Número de creación
    private int x; // Posición (x, y)
    private int y;
    private int nivel = 0;
    private static final int radius = 50;

    public Vertice(){}

    public Vertice(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static int getRadius() {
        return radius;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}