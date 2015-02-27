import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Line;

public class Paddle extends Line {

    public static final int PADDLE_MOVE_STEP = 15;

    SimpleDoubleProperty startYProperty = new SimpleDoubleProperty();
    SimpleDoubleProperty endYProperty = new SimpleDoubleProperty();

    public Paddle(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
        startYProperty.setValue(startY);
        endYProperty.setValue(endY);

        this.startYProperty().bind(startYProperty);
        this.endYProperty().bind(endYProperty);
    }

    public void movePaddleUp() {
        startYProperty.setValue(startYProperty.getValue() - PADDLE_MOVE_STEP);
        endYProperty.setValue(endYProperty.getValue() - PADDLE_MOVE_STEP);
    }

    public void movePaddleDown() {
        startYProperty.setValue(startYProperty.getValue() + PADDLE_MOVE_STEP);
        endYProperty.setValue(endYProperty.getValue() + PADDLE_MOVE_STEP);
    }

    public double getStartYProperty() {
        return startYProperty.get();
    }

    public void setStartYProperty(double startYProperty) {
        this.startYProperty.set(startYProperty);
    }

    public SimpleDoubleProperty startYPropertyProperty() {
        return startYProperty;
    }

    public double getEndYProperty() {
        return endYProperty.get();
    }

    public void setEndYProperty(double endYProperty) {
        this.endYProperty.set(endYProperty);
    }

    public SimpleDoubleProperty endYPropertyProperty() {
        return endYProperty;
    }
}
