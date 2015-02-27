import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
    private Text title;
    private Timeline timeline;
    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;

    public static void main(String[] args) {
        launch(args);
    }

    //look: http://stackoverflow.com/questions/21331519/how-to-get-smooth-animation-with-keypress-event-in-javafx

    @Override
    public void start(Stage stage) throws Exception {
        leftPaddle = new Paddle(PADDLE_PADDING, MAX_HEIGHT / 2, PADDLE_PADDING, MAX_HEIGHT / 2 + PADDLE_LENGTH);
        rightPaddle = new Paddle(MAX_WIDTH - PADDLE_PADDING, MAX_HEIGHT / 2, MAX_WIDTH - PADDLE_PADDING, MAX_HEIGHT / 2 + PADDLE_LENGTH);
        ball = new Ball();

        startTimeline();
        title = new Text("Start");
        title.setStyle("-fx-min-height: 50px; -fx-font-weight: bold; " +
                "-fx-text-alignment: center; -fx-background-color: cornflowerblue");
        Group group = new Group(ball, leftPaddle, rightPaddle);
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
                if (isPaddleEndStillInside()) {
                    leftPaddle.movePaddleDown();
                }
            } else if (event.getText().equals("w")) {
                if (isPaddleStartStillInside()) {
                    leftPaddle.movePaddleUp();
                }
            } else if (event.getText().equals("n")) {
                title.setText("start again honey");
                ball.setXValue(10);
                ball.setYValue(10);
                timeline.playFromStart();
            }
        };
    }

    private boolean isPaddleStartStillInside() {
        return leftPaddle.getStartYProperty() - PADDLE_MOVE_STEP >= 0;
    }

    private boolean isPaddleEndStillInside() {
        return leftPaddle.getEndYProperty() + PADDLE_MOVE_STEP <= MAX_HEIGHT;
    }

    private void startTimeline() {
        EventHandler onFinished = event -> {
            handleBallMovement(ball.getXValue(), ball.getYValue());
        };
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(5), onFinished);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void handleBallMovement(double xValue, double yValue) {
        if (isLeftPaddleHit()) {
            directionRight = true;
        } else {
            directionRight = checkXDirection(xValue, directionRight, MAX_WIDTH);
            directionDown = checkVerticalDirection(yValue, directionDown, MAX_HEIGHT);
        }
        double newX = ball.getNextPosition(xValue, directionRight, HORIZONTAL_STEP);
        double newY = ball.getNextPosition(yValue, directionDown, VERTICAL_STEP);
        ball.setXValue(newX);
        ball.setYValue(newY);
    }

    private boolean isLeftPaddleHit() {
        if (PADDLE_PADDING + BALL_RADIUS == ball.getXValue()
                && ball.getYValue() > leftPaddle.getStartYProperty() - BALL_RADIUS
                && ball.getYValue() < leftPaddle.getEndYProperty() + BALL_RADIUS) {
            System.out.println("HIT!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return true;
        }
        return false;
    }

    private boolean checkXDirection(double positionValue, Boolean direction, int maxValue) {
        if (direction) {
            if (positionValue + 1 >= maxValue) {
                return false;
            }
        } else {
            if (positionValue - 1 <= 0) {
                System.out.println("LOOSYYYY!!!!");
                title.setText("Sweety, you loose!");
                timeline.stop();
                return true;
            }
        }
        return direction;
    }

    private boolean checkVerticalDirection(double positionValue, Boolean direction, int maxValue) {
        if (direction) {
            if (positionValue + 1 >= maxValue - BALL_RADIUS) {
                return false;
            }
        } else {
            if (positionValue - 1 <= 0 + BALL_RADIUS) {
                return true;
            }
        }
        return direction;
    }
}
