package com.pandita.graphinggraphs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewGeneratedGraph extends Fragment implements View.OnClickListener {

    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    Context context;
    Button back;
    Graph graph;

    public ViewGeneratedGraph() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGeneratedGraph = inflater.inflate(R.layout.fragment_view_generated_graph, container, false);
        context = getActivity().getApplicationContext();
        graph = (Graph) getArguments().getSerializable("graph");
        back = viewGeneratedGraph.findViewById(R.id.back);
        back.setOnClickListener(this);
        LinearLayout view = viewGeneratedGraph.findViewById(R.id.view);
        Bitmap bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        DrawingGraph drawGraph = new DrawingGraph(context, graph);
        drawGraph.draw(canvas);
        drawGraph.setLayoutParams(new RelativeLayout.LayoutParams(1080, 1080));
        view.addView(drawGraph);
        return viewGeneratedGraph;
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putInt("sizeGrid", graph.getVerticesCount() + 1);
            bundle.putBoolean("isValued", graph.isValued());
            Fragment insertMatrix = new InsertMatrix();
            insertMatrix.setArguments(bundle);
            transaction.replace(R.id.managerFragments, insertMatrix);
            transaction.commit();
        }
    }

    public class DrawingGraph extends View {

        Graph graph;

        public DrawingGraph(Context context, Graph graph) {
            super(context);
            this.graph = graph;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            for (Edge edge : graph.getEdges()) {
                if (edge.isLoop()) {
                    drawLoop(canvas, edge, paint);
                } else {
                    drawEdge(canvas, edge, paint);
                }
            }
            paint.setColor(Color.RED);
            paint.setTextSize(70);
            for (Vertice vertice : graph.getVertices()) {
                drawVertice(canvas, vertice, paint);
            }
        }

        public void drawVertice(Canvas canvas, Vertice vertice, Paint paint) {
            Path path = new Path();
            path.addCircle(vertice.getX(), vertice.getY(), Vertice.getRadius(), Path.Direction.CW);
            canvas.drawPath(path, paint);
            paint.setColor(Color.WHITE);
            canvas.drawTextOnPath(letters[vertice.getId()] + "", path, 210, 75, paint);
            paint.setColor(Color.RED);
        }

        public void drawEdge(Canvas canvas, Edge edge, Paint paint) {
            if (graph.isValued()) {
                Path path = new Path();
                path.moveTo(edge.getV1().getX(), edge.getV1().getY());
                path.lineTo(edge.getV2().getX(), edge.getV2().getY());
                canvas.drawPath(path, paint);
                canvas.drawTextOnPath(edge.getWeight() + "", path, 200, 40, paint);
            }
            canvas.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY(), paint);
        }

        public void drawLoop(Canvas canvas, Edge edge, Paint paint) {
            int position = edge.getV1().getId();
            paint.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            if (position % 2 != 0) {
                path.addCircle(edge.getV1().getX() + 70, edge.getV1().getY(), 70, Path.Direction.CCW);
            } else {
                path.addCircle(edge.getV1().getX() - 70, edge.getV1().getY(), 70, Path.Direction.CCW);
            }
            canvas.drawPath(path, paint);
            if (graph.isValued()) {
                paint.setStrokeWidth(4);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawTextOnPath(edge.getWeight() + "", path, 310, 40, paint);
                paint.setStrokeWidth(8);
            }
            paint.setStyle(Paint.Style.FILL);
        }
    }
}