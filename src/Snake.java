import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;

public class Snake extends MIDlet {

    private SnakeCanvas canvas;

    public void startApp() {
        if (canvas == null) {
            canvas = new SnakeCanvas();
        }

        Display.getDisplay(this).setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}


class SnakeCanvas extends Canvas {

    private int direction = 6;
    private boolean paused = false;

    private int score = 0;

    private int[] snakeX = new int[100];
    private int[] snakeY = new int[100];

    private int length = 5;

    private int foodX = 100;
    private int foodY = 60;

    private boolean gameOver = false;

    public SnakeCanvas() {

        for (int i = 0; i < length; i++) {
            snakeX[i] = 60 - (i * 8);
            snakeY[i] = 60;
        }

        new Thread(new Runnable() {
            public void run() {

                while (true) {

                    if (!paused && !gameOver) {
                        moveSnake();
                        repaint();
                    }

                    try {
                        Thread.sleep(180);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    protected void paint(Graphics g) {

        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(0, 0, 0);

        g.drawString(
            "Snake - " + score,
            5,
            2,
            Graphics.TOP | Graphics.LEFT
        );

        for (int i = 0; i < length; i++) {

            if (i == 0) {
                g.setColor(0, 150, 0);
            } else {
                g.setColor(0, 200, 0);
            }

            g.fillRect(
                snakeX[i],
                snakeY[i],
                8,
                8
            );
        }

        g.setColor(255, 0, 0);

        g.fillRect(
            foodX,
            foodY,
            8,
            8
        );

        if (paused) {

            g.setColor(0, 0, 0);

            g.drawString(
                "PAUSED",
                getWidth() / 2,
                getHeight() / 2,
                Graphics.HCENTER | Graphics.BASELINE
            );
        }

        if (gameOver) {

            g.setColor(0, 0, 0);

            g.drawString(
                "GAME OVER",
                getWidth() / 2,
                getHeight() / 2 - 15,
                Graphics.HCENTER | Graphics.BASELINE
            );

            g.drawString(
                "Score: " + score,
                getWidth() / 2,
                getHeight() / 2 + 5,
                Graphics.HCENTER | Graphics.BASELINE
            );

            g.drawString(
                "0 = Restart",
                getWidth() / 2,
                getHeight() / 2 + 25,
                Graphics.HCENTER | Graphics.BASELINE
            );
        }
    }

    private void moveSnake() {

        for (int i = length - 1; i > 0; i--) {

            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        if (direction == 2) {
            snakeY[0] -= 8;
        }

        if (direction == 4) {
            snakeX[0] -= 8;
        }

        if (direction == 6) {
            snakeX[0] += 8;
        }

        if (direction == 8) {
            snakeY[0] += 8;
        }

        if (snakeX[0] < 0 ||
            snakeY[0] < 20 ||
            snakeX[0] >= getWidth() ||
            snakeY[0] >= getHeight()) {

            gameOver = true;
        }

        if (Math.abs(snakeX[0] - foodX) < 8 &&
            Math.abs(snakeY[0] - foodY) < 8) {

            if (length < 99) {
                length++;
            }

            score += 10;

            foodX = 8 + ((score * 17) % (getWidth() - 16));
            foodY = 25 + ((score * 13) % (getHeight() - 35));
        }
    }

    protected void keyPressed(int keyCode) {

        int key = getGameAction(keyCode);

        if (keyCode == KEY_NUM2) {
            direction = 2;
        }

        if (keyCode == KEY_NUM4) {
            direction = 4;
        }

        if (keyCode == KEY_NUM6) {
            direction = 6;
        }

        if (keyCode == KEY_NUM8) {
            direction = 8;
        }

        if (keyCode == KEY_NUM5) {
            paused = !paused;
        }

        if (keyCode == KEY_NUM0) {
            restart();
        }

        repaint();
    }

    private void restart() {

        score = 0;
        length = 5;
        direction = 6;
        paused = false;
        gameOver = false;

        for (int i = 0; i < length; i++) {

            snakeX[i] = 60 - (i * 8);
            snakeY[i] = 60;
        }

        foodX = 100;
        foodY = 60;
    }
}