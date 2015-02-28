import enumeration.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    public static final int SPACING = 20;

    private GameService gameService;
    private Text titleText;
    private Timeline timeline;
    private Text scoreText;
    private Group playgroundGroup;
    private Pane playgroundPane;
    private Button startGameButton;
    private Button stopGameButton;

    public static void main(String[] args) {
        launch(args);
    }

    //look: http://stackoverflow.com/questions/21331519/how-to-get-smooth-animation-with-keypress-event-in-javafx

    @Override
    public void start(Stage stage) throws Exception {
        gameService = new GameService(MAX_WIDTH, MAX_HEIGHT, PADDLE_PADDING, PADDLE_LENGTH);
        startTimeline();

        BorderPane borderLayout = new BorderPane();
        borderLayout.setTop(createHeader());
        borderLayout.setCenter(createPlayground());

        Scene scene = new Scene(borderLayout, MAX_WIDTH, MAX_HEIGHT + TITLE_HEIGHT);
        scene.setOnKeyPressed(createPaddleKeyEvents());
        stage.setTitle("Ping-Pong");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createPlayground() {
        playgroundGroup = new Group(gameService.getNodes());
        playgroundPane = new Pane(playgroundGroup);
        return playgroundPane;
    }

    private void startGame() {
        timeline.play();
        startGameButton.setDisable(true);
        stopGameButton.setDisable(false);
    }

    private void stopGame() {
        timeline.stop();
        startGameButton.setDisable(false);
        stopGameButton.setDisable(true);
    }

    private HBox createHeader() {
        VBox buttonVBox = new VBox(5);
        startGameButton = new Button("Start game");
        startGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startGame();
            }
        });
        stopGameButton = new Button("Stop game");
        stopGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopGame();
            }
        });
        stopGameButton.setDisable(true);
        buttonVBox.getChildren().addAll(startGameButton, stopGameButton);

        titleText = new Text("Start");
        scoreText = new Text(getScoreText());
        StackPane stackPane = new StackPane(scoreText);
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        HBox titleBox = new HBox(SPACING);
        titleBox.setStyle("-fx-background-color: cornsilk; -fx-font-smoothing-type: gray; -fx-border-width: 1px; " +
                "-fx-border-color: gray");
        titleBox.getChildren().addAll(buttonVBox, stackPane, titleText);
        HBox.setHgrow(stackPane, Priority.ALWAYS);
        titleBox.setPadding(new Insets(5, 12, 5, 12));

        return titleBox;
    }

    private String getScoreText() {
        return String.format("%s - %s", gameService.getScoreLeft(), gameService.getScoreRight());
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
                    gameService.resetBall();
                    titleText.setText("start again honey");
                    timeline.playFromStart();
            }
        };
    }

    private void startTimeline() {
        EventHandler onFinished = event -> {
            gameService.moveBall();
            Player pointMaker = gameService.checkForPoint();
            if (pointMaker != null) {
                timeline.stop();
                scoreText.setText(getScoreText());
                Player winner = gameService.checkForWinner();
                if (winner != null) {
                    titleText.setText(String.format("Waaaaahnsinn!!! The incredible %s wins the whole match!", winner));
                    new Alert(Alert.AlertType.INFORMATION, "Winner is " + winner, ButtonType.CLOSE).showAndWait();
                    gameService.startNewGame();
                } else {

                    titleText.setText(String.format("Yeahi!!! The incredible %s wins the point!", pointMaker));
                }
            }
        };

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(5), onFinished);
        timeline.getKeyFrames().add(keyFrame);
    }
}
