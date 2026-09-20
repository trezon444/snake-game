import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;

public class MainMIDlet extends MIDlet {

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
    private boolean gameOver = false;

    private int score = 0;

    private int[] snakeX = new int[100];
    private int[] snakeY = new int[100];

    private int length = 5;

    private int foodX = 100;
    private int foodY = 60;

    public SnakeCanvas() {

        resetSnake();

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

        /* Title */
        g.setColor(0, 0, 150);
        g.drawString(
            "SNAKE - " + score,
            5,
            2,
            Graphics.TOP | Graphics.LEFT
        );

        /* Snake */
        for (int i = 0; i < length; i++) {

            if (i == 0) {
                g.setColor(0, 120, 0);
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

        /* Food */
        g.setColor(255, 0, 0);

        g.fillRect(
            foodX,
            foodY,
            8,
            8
        );

        /* Paused */
        if (paused) {

            g.setColor(0, 0, 0);

            g.drawString(
                "PAUSED",
                getWidth() / 2,
                getHeight() / 2,
                Graphics.HCENTER | Graphics.BASELINE
            );
        }

        /* Game Over */
        if (gameOver) {

            g.setColor(0, 0, 0);

            g.drawString(
                "GAME OVER",
                getWidth() / 2,
                getHeight() / 2 - 20,
                Graphics.HCENTER | Graphics.BASELINE
            );

            g.drawString(
                "Score: " + score,
                getWidth() / 2,
                getHeight() / 2,
                Graphics.HCENTER | Graphics.BASELINE
            );

            g.drawString(
                "0 = Restart",
                getWidth() / 2,
                getHeight() / 2 + 20,
                Graphics.HCENTER | Graphics.BASELINE
            );
        }

        /* Creator */
        g.setColor(80, 80, 80);

        g.drawString(
            "MADE BY DAVECREATOR",
            getWidth() / 2,
            getHeight() - 12,
            Graphics.HCENTER | Graphics.BASELINE
        );
    }


    private void moveSnake() {

        /* Move body */
        for (int i = length - 1; i > 0; i--) {

            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        /* Move head */
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

        /* Wall collision */
        if (snakeX[0] < 0 ||
            snakeY[0] < 20 ||
            snakeX[0] >= getWidth() ||
            snakeY[0] >= getHeight() - 15) {

            gameOver = true;
        }

        /* Food collision */
        if (Math.abs(snakeX[0] - foodX) < 8 &&
            Math.abs(snakeY[0] - foodY) < 8) {

            if (length < 99) {
                length++;
            }

            score += 10;

            foodX = 8 + ((score * 17) % (getWidth() - 16));
            foodY = 25 + ((score * 13) % (getHeight() - 45));
        }
    }


    protected void keyPressed(int keyCode) {

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
        direction = 6;
        paused = false;
        gameOver = false;

        resetSnake();

        foodX = 100;
        foodY = 60;
    }


    private void resetSnake() {

        length = 5;

        for (int i = 0; i < length; i++) {

            snakeX[i] = 60 - (i * 8);
            snakeY[i] = 60;
        }
    }
}