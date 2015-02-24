import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    public static final int MAX_WIDTH = 600;
    public static final int MAX_HEIGHT = 300;
    public static final int PADDLE_LENGTH = 60;
    public static final int PADDLE_PADDING = 30;
    public static final int PADDLE_MOVE_STEP = 15;
    public static final int BALL_RADIUS = 10;
    public static final int TITLE_HEIGHT = 50;
    private static final int HORIZONTAL_STEP = 1;
    private static final int VERTICAL_STEP = 1;
    private boolean directionRight = true;
    private boolean directionDown = true;
    private SimpleDoubleProperty startYLeftPaddle;
    private SimpleDoubleProperty endYLeftPaddle;
    private SimpleDoubleProperty startYRightPaddle;
    private SimpleDoubleProperty endYRightPaddle;
    private SimpleIntegerProperty xValue;
    private SimpleIntegerProperty yValue;
    private Text title;
    private Timeline timeline;
    private Circle circle;

    public static void main(String[] args) {
        launch(args);
    }

    //look: http://stackoverflow.com/questions/21331519/how-to-get-smooth-animation-with-keypress-event-in-javafx

    @Override
    public void start(Stage stage) throws Exception {
        Line leftPaddle = new Line(PADDLE_PADDING, 0, PADDLE_PADDING, 0);
        Line rightPaddle = new Line(MAX_WIDTH - PADDLE_PADDING, 0, MAX_WIDTH - PADDLE_PADDING, 0);
        initPaddleProperties(leftPaddle, rightPaddle);

        circle = new Circle(10, 10, BALL_RADIUS);
        circle.setFill(Color.RED);
        initCircleProperties(circle);

        startTimeline();
        title = new Text("Start");
        title.setStyle("-fx-min-height: 50px");
        Group group = new Group(circle, leftPaddle, rightPaddle);
        Pane pane = new Pane(group);
        pane.setStyle("-fx-background-color: lightsteelblue");
        pane.setOnKeyPressed(createPaddleKeyEvent());
        pane.setMaxHeight(MAX_HEIGHT);
        pane.setMinHeight(MAX_HEIGHT);

        BorderPane borderLayout = new BorderPane();
        borderLayout.setTop(title);
        borderLayout.setCenter(pane);

        group.setFocusTraversable(true);
        Scene scene = new Scene(borderLayout, MAX_WIDTH, MAX_HEIGHT + TITLE_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    private EventHandler<KeyEvent> createPaddleKeyEvent() {
        return event -> {
            System.out.println(("keyPressed: " + event.getText()));
            if (event.getText().equals("s")) {
                startYLeftPaddle.setValue(startYLeftPaddle.getValue() + PADDLE_MOVE_STEP);
                endYLeftPaddle.setValue(endYLeftPaddle.getValue() + PADDLE_MOVE_STEP);
            } else if (event.getText().equals("w")) {
                startYLeftPaddle.setValue(startYLeftPaddle.getValue() - PADDLE_MOVE_STEP);
                endYLeftPaddle.setValue(endYLeftPaddle.getValue() - PADDLE_MOVE_STEP);
            } else if (event.getText().equals("n")) {
                title.setText("start again honey");
                xValue.setValue(10);
                yValue.setValue(10);
                timeline.playFromStart();
            }
        };
    }

    private void startTimeline() {
        EventHandler onFinished = event -> {
            handleBallMovement(xValue, yValue);
        };
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(5), onFinished);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void initCircleProperties(Circle circle) {
        xValue = new SimpleIntegerProperty(10);
        yValue = new SimpleIntegerProperty(20);
        circle.centerXProperty().bind(xValue);
        circle.centerYProperty().bind(yValue);
    }

    private void initPaddleProperties(Line leftPaddle, Line rightPaddle) {
        startYLeftPaddle = new SimpleDoubleProperty(MAX_HEIGHT / 2);
        endYLeftPaddle = new SimpleDoubleProperty(MAX_HEIGHT / 2 + PADDLE_LENGTH);
        leftPaddle.startYProperty().bind(startYLeftPaddle);
        leftPaddle.endYProperty().bind(endYLeftPaddle);
        startYRightPaddle = new SimpleDoubleProperty(MAX_HEIGHT / 2);
        endYRightPaddle = new SimpleDoubleProperty(MAX_HEIGHT / 2 + PADDLE_LENGTH);
        rightPaddle.startYProperty().bind(startYRightPaddle);
        rightPaddle.endYProperty().bind(endYRightPaddle);
    }

    private void handleBallMovement(SimpleIntegerProperty xValue, SimpleIntegerProperty yValue) {
        if (isLeftPaddleHit(xValue, yValue)) {
            directionRight = true;
        } else {
            directionRight = checkXDirection(xValue, directionRight, MAX_WIDTH);
            directionDown = checkVerticalDirection(yValue, directionDown, MAX_HEIGHT);
        }
        double newX = getNextPosition(xValue, directionRight, HORIZONTAL_STEP);
        double newY = getNextPosition(yValue, directionDown, VERTICAL_STEP);
        xValue.setValue(newX);
        yValue.setValue(newY);
    }

    private boolean isLeftPaddleHit(SimpleIntegerProperty xValue, SimpleIntegerProperty yValue) {
        if (PADDLE_PADDING + BALL_RADIUS == xValue.getValue()
                && yValue.getValue() > startYLeftPaddle.getValue() - BALL_RADIUS
                && yValue.getValue() < endYLeftPaddle.getValue() + BALL_RADIUS) {
            System.out.println("HIT!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return true;
        }
        return false;
    }

    private double getNextPosition(SimpleIntegerProperty positionValue, boolean direction, double step) {
        double newPositionValue;
        if (direction) {
            newPositionValue = positionValue.getValue() + step;
        } else {
            newPositionValue = positionValue.getValue() - step;
        }
        return newPositionValue;
    }

    private boolean checkXDirection(SimpleIntegerProperty positionValue, Boolean direction, int maxValue) {
        if (direction) {
            if (positionValue.getValue() + 1 >= maxValue) {
                return false;
            }
        } else {
            if (positionValue.getValue() - 1 <= 0) {
                System.out.println("LOOSYYYY!!!!");
                title.setText("Sweety, you loose!");
                timeline.stop();
                return true;
            }
        }
        return direction;
    }

    private boolean checkVerticalDirection(SimpleIntegerProperty positionValue, Boolean direction, int maxValue) {
        if (direction) {
            if (positionValue.getValue() + 1 >= maxValue - BALL_RADIUS) {
                return false;
            }
        } else {
            if (positionValue.getValue() - 1 <= 0 + BALL_RADIUS) {
                return true;
            }
        }
        return direction;
    }
}
