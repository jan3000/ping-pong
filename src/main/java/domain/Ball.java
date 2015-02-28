package domain;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    private static final int HORIZONTAL_STEP = 1;
    private static final int VERTICAL_STEP = 1;
    private static final int BALL_RADIUS = 10;
    private SimpleDoubleProperty xValue = new SimpleDoubleProperty();
    private SimpleDoubleProperty yValue = new SimpleDoubleProperty();
    private boolean directionRight = true;
    private boolean directionDown = true;
    private int initialXValue;
    private int initialYValue;

    public Ball(int xValue, int yValue) {
        super(xValue, yValue, BALL_RADIUS);
        initialXValue = xValue;
        initialYValue = yValue;
        this.setFill(Color.BLACK);
        this.xValue.setValue(xValue);
        this.yValue.setValue(yValue);
        this.centerXProperty().bind(this.xValue);
        this.centerYProperty().bind(this.yValue);
    }

    public void reset() {
        xValue.setValue(initialXValue);
        yValue.setValue(initialYValue);
        setDirectionRight(true);
        setDirectionDown(true);
    }

    public void move() {
        setNextXPosition();
        setNextYPosition();
    }

    public void setNextXPosition() {
        if (directionRight) {
            setXValue(getXValue() + HORIZONTAL_STEP);
        } else {
            setXValue(getXValue() - HORIZONTAL_STEP);
        }
    }

    public void setNextYPosition() {
        if (directionDown) {
            setYValue(getYValue() + VERTICAL_STEP);
        } else {
            setYValue(getYValue() - VERTICAL_STEP);
        }
    }

    public boolean isDirectionRight() {
        return directionRight;
    }

    public void setDirectionRight(boolean directionRight) {
        this.directionRight = directionRight;
    }

    public boolean isDirectionDown() {
        return directionDown;
    }

    public void setDirectionDown(boolean directionDown) {
        this.directionDown = directionDown;
    }

    public double getXValue() {
        return xValue.get();
    }

    public void setXValue(double xValue) {
        this.xValue.set(xValue);
    }

    public SimpleDoubleProperty xValueProperty() {
        return xValue;
    }

    public double getYValue() {
        return yValue.get();
    }

    public void setYValue(double yValue) {
        this.yValue.set(yValue);
    }

    public SimpleDoubleProperty yValueProperty() {
        return yValue;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("domain.Ball{");
        sb.append("xValue=").append(xValue);
        sb.append(", yValue=").append(yValue);
        sb.append(", directionRight=").append(directionRight);
        sb.append(", directionDown=").append(directionDown);
        sb.append('}');
        return sb.toString();
    }
}
