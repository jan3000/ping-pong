package domain;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BallTest {

    public static final int START_X_VALUE = 10;
    public static final int START_Y_VALUE = 20;
    public static final int STEP_SIZE = 1;
    private Ball ball;

    @Before
    public void setUp() {
        ball = new Ball(START_X_VALUE, START_Y_VALUE);
    }

    @Test
    public void moveRightDown() {
        ball.move();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE + STEP_SIZE);
    }

    @Test
    public void moveRightUp() {
        ball.setDirectionDown(false);
        ball.move();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE + STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE - STEP_SIZE);
    }

    @Test
    public void moveLeftUp() {
        ball.setDirectionRight(false);
        ball.setDirectionDown(false);
        ball.move();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE - STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE - STEP_SIZE);
    }

    @Test
    public void moveLeftDown() {
        ball.setDirectionRight(false);
        ball.move();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE - STEP_SIZE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE + STEP_SIZE);
    }

    @Test
    public void reset() {
        ball.move();
        ball.reset();
        assertThat(ball.getXValue()).isEqualTo(START_X_VALUE);
        assertThat(ball.getYValue()).isEqualTo(START_Y_VALUE);
    }
}
