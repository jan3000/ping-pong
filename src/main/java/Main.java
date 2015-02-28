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
import service.GameService;

public class Main extends Application {

    public static final int MAX_WIDTH = 600;
    public static final int MAX_HEIGHT = 300;
    public static final int PADDLE_LENGTH = 60;
    public static final int PADDLE_PADDING = 30;
    public static final int TITLE_HEIGHT = 50;

    private GameService gameService;
    private Text title;
    private Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    //look: http://stackoverflow.com/questions/21331519/how-to-get-smooth-animation-with-keypress-event-in-javafx

    @Override
    public void start(Stage stage) throws Exception {
        gameService = new GameService(MAX_WIDTH, MAX_HEIGHT, PADDLE_PADDING, PADDLE_LENGTH);

        startTimeline();
        title = new Text("Start");
        title.setStyle("-fx-min-height: 50px; -fx-font-weight: bold; " +
                "-fx-text-alignment: center; -fx-background-color: cornflowerblue");
        Group group = new Group(gameService.getNodes());
        Pane pane = new Pane(group);
        pane.setStyle("-fx-background-color: lightsteelblue");
        pane.setOnKeyPressed(createPaddleKeyEvents());
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

    private EventHandler<KeyEvent> createPaddleKeyEvents() {
        return event -> {
            switch (event.getCode()) {
                case W:
                    gameService.moveLeftPaddleUp();
                    break;
                case S:
                    gameService.moveLeftPaddleDown();
                    break;
                case UP:
                    gameService.moveRightPaddleUp();
                    break;
                case DOWN:
                    gameService.moveRightPaddleDown();
                    break;
                case N:
                    gameService.reset();
                    title.setText("start again honey");
                    timeline.playFromStart();
            }
        };
    }

    private void startTimeline() {
        EventHandler onFinished = event -> {
            gameService.moveBall();
            if (gameService.isBallCollisionLeft() || gameService.isBallCollisionRight()) {
                System.out.println("LOOSYYYY!!!!");
                title.setText("Sweety, you loose!");
                timeline.stop();
            }
            ;
        };

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(5), onFinished);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
