package service;

import com.google.common.collect.Lists;
import domain.Ball;
import domain.Paddle;
import enumeration.Player;
import javafx.scene.Node;

import java.util.List;

public class GameService {

    public static final int WINNING_SCORE = 3;
    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private int paddlePadding;
    private int maxWidth;
    private int maxHeight;
    private int scoreLeft;
    private int scoreRight;

    public GameService(int width, int height, int paddlePadding, int paddleLength) {
        this.paddlePadding = paddlePadding;
        this.maxWidth = width;
        this.maxHeight = height;
        leftPaddle = new Paddle(paddlePadding, height / 2, paddlePadding, height / 2 + paddleLength);
        rightPaddle = new Paddle(width - paddlePadding, height / 2, width - paddlePadding, height / 2 + paddleLength);
        ball = new Ball(10, 10);
        scoreLeft = 0;
        scoreRight = 0;
    }

    public List<Node> getNodes() {
        return Lists.newArrayList(ball, leftPaddle, rightPaddle);
    }

    public void moveBall() {
        determineBallDirection();
        ball.move();
    }

    private void determineBallDirection() {
        if (isLeftPaddleHit()) {
            ball.setDirectionRight(true);
        } else if (isRightPaddleHit()) {
            ball.setDirectionRight(false);
        } else if (isBallCollisionTop()) {
            ball.setDirectionDown(true);
        } else if (isBallCollisionBottom()) {
            ball.setDirectionDown(false);
        }
    }

    public void resetBall() {
        ball.reset();
    }

    public void startNewGame() {
        resetBall();
        scoreLeft = 0;
        scoreRight = 0;
    }

    public void moveLeftPaddleUp() {
        leftPaddle.movePaddleUp();
    }

    public void moveLeftPaddleDown() {
        leftPaddle.movePaddleDown(maxHeight);
    }

    public void moveRightPaddleUp() {
        rightPaddle.movePaddleUp();
    }

    public void moveRightPaddleDown() {
        rightPaddle.movePaddleDown(maxHeight);
    }

    private boolean isLeftPaddleHit() {
        if (paddlePadding + ball.getRadius() == ball.getXValue()
                && ball.getYValue() > leftPaddle.getStartYProperty() - ball.getRadius()
                && ball.getYValue() < leftPaddle.getEndYProperty() + ball.getRadius()) {
            return true;
        }
        return false;
    }

    private boolean isRightPaddleHit() {
        if (maxWidth - paddlePadding - ball.getRadius() == ball.getXValue()
                && ball.getYValue() > rightPaddle.getStartYProperty() - ball.getRadius()
                && ball.getYValue() < rightPaddle.getEndYProperty() + ball.getRadius()) {
            return true;
        }
        return false;
    }

    public Player checkForPoint() {
        if (isBallCollisionLeft()) {
            scoreRight++;
            return Player.PLAYER_LEFT;
        } else if (isBallCollisionRight()) {
            scoreLeft++;
            return Player.PLAYER_RIGHT;
        }
        return null;
    }

    public Player checkForWinner() {
        if (scoreLeft >= WINNING_SCORE) {
            return Player.PLAYER_LEFT;
        } else if (scoreRight >= WINNING_SCORE) {
            return Player.PLAYER_RIGHT;
        }
        return null;
    }

    public boolean isBallCollisionRight() {
        return ball.isDirectionRight() && ball.getXValue() + 1 >= maxWidth;
    }

    public boolean isBallCollisionLeft() {
        return !ball.isDirectionRight() && ball.getXValue() - 1 <= 0;
    }

    private boolean isBallCollisionTop() {
        return !ball.isDirectionDown() && ball.getYValue() - 1 <= ball.getRadius();
    }

    private boolean isBallCollisionBottom() {
        return ball.isDirectionDown() && ball.getYValue() + 1 >= maxHeight - ball.getRadius();
    }

    public Ball getBall() {
        return ball;
    }

    public Paddle getLeftPaddle() {
        return leftPaddle;
    }

    public Paddle getRightPaddle() {
        return rightPaddle;
    }

    public int getScoreRight() {
        return scoreRight;
    }

    public int getScoreLeft() {
        return scoreLeft;
    }
}
