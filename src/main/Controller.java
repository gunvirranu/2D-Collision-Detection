package main;

import collision.SeparatingAxisTheorem;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import util.Polygon;

import java.util.ArrayList;


public class Controller {

    @FXML
    StackPane stackPane;

    @FXML
    Canvas canvas;

    private GraphicsContext gc;

//    private Polygon testPoly = new Polygon(new double[]{500, 600, 600, 500}, new double[]{500, 500, 600, 600}, 4);
    private Polygon testPoly = new Polygon(new double[]{100, 200, 200, 100}, new double[]{100, 100, 200, 200}, 4);
    private Polygon poly;

    private final double speed = 8;
    private final double rotspeed = Math.PI / 90;
    private int vertical, horizontal;
    private int anticlockwise;
    private ArrayList<Double> tempX = new ArrayList<>();
    private ArrayList<Double> tempY = new ArrayList<>();

    @FXML
    void initialize() {

        canvas.setFocusTraversable(true);
        gc = canvas.getGraphicsContext2D();

        initCanvasEvents();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                if (poly != null) {
                    if (horizontal != 0 || vertical != 0)
                        poly.move(horizontal * speed, vertical * speed);
                    if (anticlockwise != 0)
                        poly.rotate(anticlockwise * rotspeed);

                    if (SeparatingAxisTheorem.isCollide(poly, testPoly))
                        gc.setFill(Color.RED);
                    else
                        gc.setFill(Color.GREEN);

                    drawPoly();
                }

                drawTestPoly();
                drawTempPoly();
            }
        };
        animationTimer.start();
    }

    void drawPoly() {

        gc.fillPolygon(poly.vertsX, poly.vertsY, poly.vertsNum);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(poly.minX, poly.minY, poly.maxX - poly.minX, poly.maxY - poly.minY);

        gc.setFill(Color.BLUE);
        gc.fillOval(poly.avgX - 4, poly.avgY - 4, 4, 4);
    }

    void drawTestPoly() {
        gc.setFill(Color.ROYALBLUE);
        gc.fillPolygon(testPoly.vertsX, testPoly.vertsY, testPoly.vertsNum);
    }

    void drawTempPoly() {

        double[] xs = new double[tempX.size()];
        double[] ys = new double[tempY.size()];

        for (int i = 0; i < tempX.size(); i++) {
            xs[i] = tempX.get(i);
            ys[i] = tempY.get(i);
        }

        gc.setFill(Color.BLACK);
        gc.fillPolygon(xs, ys, xs.length);
    }

    void initCanvasEvents() {

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                tempX.add(event.getX());
                tempY.add(event.getY());
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                double[] xs = new double[tempX.size()];
                double[] ys = new double[tempY.size()];

                for (int i = 0; i < tempX.size(); i++) {
                    xs[i] = tempX.get(i);
                    ys[i] = tempY.get(i);
                }

                poly = new Polygon(xs, ys, xs.length);

                tempX.clear();
                tempY.clear();
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) anticlockwise = -1;
            if (event.getCode() == KeyCode.D) anticlockwise = 1;
            if (vertical == 0) {
                if (event.getCode() == KeyCode.UP) vertical = -1;
                if (event.getCode() == KeyCode.DOWN) vertical = 1;
            }
            if (horizontal == 0) {
                if (event.getCode() == KeyCode.LEFT) horizontal = -1;
                if (event.getCode() == KeyCode.RIGHT) horizontal = 1;
            }
        });

        canvas.setOnKeyReleased(event -> {
            if (anticlockwise == -1 && event.getCode() == KeyCode.A) anticlockwise = 0;
            if (anticlockwise == 1 && event.getCode() == KeyCode.D) anticlockwise = 0;
            else if (vertical == -1 && event.getCode() == KeyCode.UP) vertical = 0;
            else if (vertical == 1 && event.getCode() == KeyCode.DOWN) vertical = 0;
            else if (horizontal == -1 && event.getCode() == KeyCode.LEFT) horizontal = 0;
            else if (horizontal == 1 && event.getCode() == KeyCode.RIGHT) horizontal = 0;
        });
    }
}
