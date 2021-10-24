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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

public class DrawAndViewTree extends Fragment {

    Context context;
    Tree tree;
    private char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

    public DrawAndViewTree() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawAndViewTree = inflater.inflate(R.layout.fragment_draw_and_view_tree, container, false);

        context = getActivity().getApplicationContext();
        tree = (Tree) getArguments().getSerializable("tree");
        LinearLayout view = drawAndViewTree.findViewById(R.id.view);
        Bitmap bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        DrawTree draw = new DrawTree(context, tree);
        draw.draw(canvas);
        draw.setLayoutParams(new RelativeLayout.LayoutParams(1080, 1080));
        view.addView(draw);

        return drawAndViewTree;
    }

    class DrawTree extends View {

        Tree tree;

        public DrawTree(Context context, Tree tree) {
            super(context);
            this.tree = tree;
        }

        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            paint.setTypeface(Typeface.DEFAULT_BOLD);

            for (Edge edge : tree.getEdges()) {
                    drawEdge(canvas, edge, paint);
            }
            paint.setColor(Color.RED);
            paint.setTextSize(70);
            for (Vertice vertice : tree.getVertices()) {
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
            if (tree.isValued()) {
                Path path = new Path();
                path.moveTo(edge.getV1().getX(), edge.getV1().getY());
                path.lineTo(edge.getV2().getX(), edge.getV2().getY());
                canvas.drawPath(path, paint);
                canvas.drawTextOnPath(edge.getWeight() + "", path, 200, 40, paint);
            }
            canvas.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY(), paint);
        }

    }

}
