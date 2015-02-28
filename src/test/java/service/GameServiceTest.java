package service;

import domain.Ball;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class GameServiceTest {

    public static final int START_X_VALUE = 10;
    public static final int START_Y_VALUE = 10;
    public static final int STEP_SIZE = 1;
    public static final int PADDLE_PADDING = 10;
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

    private GameService gameService;
    private Ball ball;

    @Before
    public void setUp() {
        gameService = new GameService(WIDTH, HEIGHT, PADDLE_PADDING, 10);
        ball = gameService.getBall();
    }

    @Test
    public void moveBallAtStart() {
        gameService.moveBall();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE + STEP_SIZE);
    }

    @Test
    public void moveBallLeftPaddleHitDirectionLeftDown() {
        hitLeftPaddle();
        assertThat(ball.getXValue()).isEqualTo(PADDLE_PADDING + ball.getRadius() + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(gameService.getLeftPaddle().getStartYProperty() + STEP_SIZE);
    }

    @Test
    public void moveBallLeftPaddleHitDirectionLeftUp() {
        ball.setDirectionDown(false);
        hitLeftPaddle();
        assertThat(ball.getXValue()).isEqualTo(PADDLE_PADDING + ball.getRadius() + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(gameService.getLeftPaddle().getStartYProperty() - STEP_SIZE);
    }

    private void hitLeftPaddle() {
        ball.setDirectionRight(false);
        ball.setXValue(PADDLE_PADDING + ball.getRadius());
        ball.setYValue(gameService.getLeftPaddle().getStartYProperty());
        gameService.moveBall();
    }

    @Test
    public void moveBallRightPaddleHitDirectionRightDown() {
        hitRightPaddle();
        assertThat(ball.getXValue()).isEqualTo(WIDTH - PADDLE_PADDING - ball.getRadius() - STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(gameService.getRightPaddle().getStartYProperty() + STEP_SIZE);
    }

    @Test
    public void moveBallLeftPaddleHitDirectionRightUp() {
        ball.setDirectionDown(false);
        hitRightPaddle();
        assertThat(ball.getXValue()).isEqualTo(WIDTH - PADDLE_PADDING - ball.getRadius() - STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(gameService.getRightPaddle().getStartYProperty() - STEP_SIZE);
    }

    private void hitRightPaddle() {
        ball.setDirectionRight(true);
        ball.setXValue(WIDTH - PADDLE_PADDING - ball.getRadius());
        ball.setYValue(gameService.getRightPaddle().getStartYProperty());
        gameService.moveBall();
    }

    @Test
    public void isBallCollisionLeft() {
        ball.setDirectionRight(false);
        ball.setXValue(STEP_SIZE);
        ball.move();
        assertThat(gameService.isBallCollisionLeft()).isTrue();
    }

    @Test
    public void isBallCollisionRight() {
        ball.setXValue(WIDTH - STEP_SIZE);
        gameService.moveBall();
        assertThat(gameService.isBallCollisionRight()).isTrue();
    }

    @Test
    public void moveBallCollisionTop() {
        ball.setYValue(0);
        ball.setDirectionDown(false);
        gameService.moveBall();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(STEP_SIZE);
    }


    @Test
    public void moveBallCollisionDown() {
        ball.setYValue(HEIGHT);
        gameService.moveBall();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(HEIGHT - STEP_SIZE);
    }


}
