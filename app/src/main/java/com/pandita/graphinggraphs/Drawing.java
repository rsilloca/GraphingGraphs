package com.pandita.graphinggraphs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Drawing extends View implements View.OnClickListener {

    private Path drawPath;
    private Paint drawPaint, canvasPaint, textPaint;
    private final int COLOR_BROWN = 0xFF451005;
    private int colorActual = 0xFF000000; // colorActual = color escogido por el usuario, default negro.
    private Canvas drawCanvas;
    private Bitmap bitmap;
    private float x, y;
    private int v1 = -1, v2 = -1;
    private Graph graph = new Graph();
    private char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};
    private ArrayList<Integer> actions = new ArrayList<>();
    private ArrayList<Vertice> reciclerVertices = new ArrayList<>();
    private ArrayList<Edge> reciclerEdges = new ArrayList<>();
    private ArrayList<Integer> reciclerActions = new ArrayList<>();

    Activity context;
    AlertDialog dialog;
    Button aceptar, cancelar;
    TextView newValor;


    public Drawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(COLOR_BROWN);
        drawPaint.setTextSize(70);
        drawPaint.setTypeface(Typeface.DEFAULT_BOLD);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint = new Paint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setStrokeWidth(20);
        textPaint.setTextSize(50);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(bitmap);
        dibujar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX(); // Obtenemos la coordenada (x, y)
        float touchY = event.getY(); // que el usuario ha presionado
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // Cuando el dedo toca la pantalla
                x = touchX; // Guardamos el x e y
                y = touchY; // donde se ha presionado por primera vez
                v1 = collisionDetection((int) touchX, (int) touchY); // Verificamos si la coordenada corresponde a algún vértice
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE: // Cuando el dedo se mueve sobre la pantalla
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP: // Cuando el dedo deja de tocar la pantalla
                v2 = collisionDetection((int) touchX, (int) touchY); // Verificamos si la coordenada corresponde a algún vértice
                if (Math.abs(touchX - x) < 30 && Math.abs(touchY - y) < 30
                        && v1 == -1 && v2 == -1 && graph.getVerticesCount() < 21) { // Limitamos el número de vértices en 20
                    graph.addVertice(new Vertice(graph.getVerticesCount(), (int) touchX, (int) touchY));
                    actions.add(1); // Añadimos a la lista de acciones la creación de un nuevo vértice
                    dibujar();
                } else if (v1 != -1 && v2 != -1) { // Si se ha detectado la colisión con dos vértices
                    if (graph.isValued()) { // Si es un grafo valorado
                        createAlertDialog(); // crea un dialogo para insertar el peso de la arista
                    } else { // De lo contrario agrega una arista sin peso
                        int weigth = -1;
                        graph.addEdge(new Edge(graph.getVertice(v1), graph.getVertice(v2), weigth, colorActual));
                        actions.add(2); // Añadimos a la lista de acciones la creación de una nueva arista
                        dibujar();
                    }
                }
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == aceptar) {
            if (!newValor.getText().toString().equals("")) {
                int weigth = Integer.parseInt(newValor.getText().toString());
                graph.addEdge(new Edge(graph.getVertice(v1), graph.getVertice(v2), weigth, colorActual));
                actions.add(2); // Añadimos a la lista de acciones la creación de una nueva arista
                dibujar();
                dialog.cancel();
            }
        } else if (v == cancelar) {
            dialog.cancel();
        }
    }

    public void dibujar() {
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Limpiamos el canvas
        for (Edge e : graph.getEdges()) {
            drawPaint.setColor(e.getColor());
            drawEdge(e);
        }
        drawPaint.setColor(COLOR_BROWN);
        for (Vertice v : graph.getVertices()) {
            drawVertice(v);
        }
        drawPaint.setColor(colorActual);
        invalidate();
    }

    public void drawVertice(Vertice vertice) {
        drawPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.addCircle(vertice.getX(), vertice.getY(), Vertice.getRadius(), Path.Direction.CW);
        drawCanvas.drawPath(path, drawPaint); // dibujamos el círculo (vértice)
        drawPaint.setColor(Color.WHITE);
        drawCanvas.drawTextOnPath(letters[vertice.getId()] + "", path, 210, 75, drawPaint);
        drawPaint.setColor(COLOR_BROWN);
        drawPaint.setStyle(Paint.Style.STROKE);
    }

    public void drawEdge(Edge edge) {
        if (graph.isValued()) { // Si el grafo es valorado, se debe dibujar la arista y su respectivo peso
            drawCanvas.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY(), drawPaint);
            Path path = new Path();
            path.moveTo(edge.getV1().getX(), edge.getV1().getY());
            path.lineTo(edge.getV2().getX(), edge.getV2().getY());
            textPaint.setColor(edge.getColor());
            int medio = (int) Math.sqrt(Math.pow(edge.getV1().getX() - edge.getV2().getX(), 2) + Math.pow(edge.getV1().getY() - edge.getV2().getY(), 2))/2;
            drawCanvas.drawTextOnPath(edge.getWeight() + "", path, medio, 50, textPaint);
        } else if (edge.isLoop()) {
            Path path = new Path();
            path.addCircle(edge.getV1().getX() - 70, edge.getV1().getY(), 70, Path.Direction.CCW);
            drawCanvas.drawPath(path, drawPaint);
            if (graph.isValued()) {
                drawCanvas.drawTextOnPath(edge.getWeight() + "", path, 310, 40, textPaint);
            }
        } else { // En caso contrario, solo se dibuja una recta
            drawCanvas.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY(), drawPaint);
        }
    }

    public int collisionDetection(int x, int y) {
        for (int i = 0; i < graph.getVerticesCount(); i++) {
            Vertice aux = graph.getVertices().get(i);
            if (aux.getX() - Vertice.getRadius() < x && aux.getX() + Vertice.getRadius() > x
                    && aux.getY() - Vertice.getRadius() < y && aux.getY() + Vertice.getRadius() > y) {
                return i;
            }
        }
        return -1;
    }

    public void deshacer() {
        if (!actions.isEmpty()) {
            int size = actions.size();
            if (actions.get(size - 1) == 1) { // 1 = eliminar vértice
                reciclerVertices.add(graph.deleteVertice()); // reciclamos el vértice por si se quiere añadirlo de nuevo
            } else { // 0 = eliminar arista
                reciclerEdges.add(graph.deleteEdge()); // reciclamos la arista por si se quiere añadirla de nuevo
            }
            reciclerActions.add(actions.remove(size - 1));
            dibujar();
        }
    }

    public void rehacer() {
        if (!reciclerActions.isEmpty()) {
            int size = reciclerActions.size();
            int sizeAux;
            if (reciclerActions.get(size - 1) == 1) { // 1 = añadir último vértice eliminado
                sizeAux = reciclerVertices.size();
                graph.addVertice(reciclerVertices.remove(sizeAux - 1));
            } else { // 0 = añadir última arista eliminada
                sizeAux = reciclerEdges.size();
                graph.addEdge(reciclerEdges.remove(sizeAux - 1));
            }
            actions.add(reciclerActions.remove(size - 1));
            dibujar();
        }
    }

    public void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View miView = context.getLayoutInflater().inflate(R.layout.alert_input_dijkstra, null);
        newValor = miView.findViewById(R.id.newValor);
        cancelar = miView.findViewById(R.id.cancelar);
        cancelar.setOnClickListener(this);
        aceptar = miView.findViewById(R.id.aceptar);
        aceptar.setOnClickListener(this);
        builder.setView(miView);
        dialog = builder.create();
        dialog.show();
    }

    public void clear() {
        graph = new Graph(); // Reinicializamos el grafo
        actions.clear(); // Limpiamos la lista de acciones
        reciclerActions.clear();
        reciclerEdges.clear();
        reciclerVertices.clear();
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Limpiamos el canvas
        invalidate();
    }

    public void setColor(int color) {
        drawPaint.setColor(color);
        colorActual = color; // Actualizamos al color seleccionado por el usuario
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setValued(boolean isValued) {
        graph.setValued(isValued);
    }

    public void setContext(Activity context) {
        this.context = context;
    }

}
