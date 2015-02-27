package service;

import com.google.common.collect.Lists;
import domain.Ball;
import domain.Paddle;
import javafx.scene.Node;

import java.util.List;

public class GameService {

    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private int paddlePadding;
    private int paddleLength;
    private int maxWidth;
    private int maxHeight;

    public GameService(int height, int width, int paddlePadding, int paddleLength) {
        this.paddleLength = paddleLength;
        this.paddlePadding = paddlePadding;
        this.maxWidth = width;
        this.maxHeight = height;
        leftPaddle = new Paddle(paddlePadding, height / 2, paddlePadding, height / 2 + paddleLength);
        rightPaddle = new Paddle(width - paddlePadding, height / 2, width - paddlePadding, height / 2 + paddleLength);
        ball = new Ball(10, 20);
    }

    public List<Node> getNodes() {
        return Lists.newArrayList(ball, leftPaddle, rightPaddle);
    }

    public void moveBall() {
        if (isLeftPaddleHit()) {
            ball.setDirectionRight(true);
        } else {
            ball.setDirectionRight(isHorizontalDirectionRight());
            ball.setDirectionDown(isVerticalDirectionDown());
        }
        ball.move();
    }

    public void reset() {
        ball.reset();
    }

    public void moveLeftPaddleUp() {
        leftPaddle.movePaddleUp();
    }

    public void moveLeftPaddleDown() {
        leftPaddle.movePaddleDown(maxHeight);
    }

    private boolean isLeftPaddleHit() {
        if (paddlePadding + ball.getRadius() == ball.getXValue()
                && ball.getYValue() > leftPaddle.getStartYProperty() - ball.getRadius()
                && ball.getYValue() < leftPaddle.getEndYProperty() + ball.getRadius()) {
            System.out.println("HIT!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return true;
        }
        return false;
    }

    private boolean isVerticalDirectionDown() {
        if (isBallCollisionBottom()) {
            return false;
        } else if (isBallCollisionTop()) {
            return true;
        }
        return ball.isDirectionDown();
    }

    private boolean isHorizontalDirectionRight() {
        if (isBallCollisionRight()) {
            return false;
        } else if (isBallCollisionLeft()) {
            // this is a loose can be removed?
            return true;
        }
        return ball.isDirectionRight();
    }

    public boolean isBallLost() {
        return isBallCollisionLeft();
    }

    private boolean isBallCollisionRight() {
        return ball.isDirectionRight() && ball.getXValue() + 1 >= maxWidth;
    }

    private boolean isBallCollisionLeft() {
        return !ball.isDirectionRight() && ball.getXValue() - 1 <= 0;
    }

    private boolean isBallCollisionTop() {
        return !ball.isDirectionDown() && ball.getYValue() - 1 <= ball.getRadius();
    }

    private boolean isBallCollisionBottom() {
        return ball.isDirectionDown() && ball.getYValue() + 1 >= maxHeight - ball.getRadius();
    }


}
