package sample;

import java.util.Random;

import com.sun.prism.Graphics;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class Main extends Application {

    //variable
    private static final int width = 800;
    private static final int height = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballYSpeed = 2;
    private int ballXSpeed = 2;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private int playerOneXPos = 0;
    private double playerTwoXPos = width - PLAYER_WIDTH;


    public void start(Stage window) throws Exception {
        window.setTitle("P O N G !");                               //set window title
        Canvas canvas = new Canvas(width,height);                   //set new canvas (an image that can be drawn on)
        GraphicsContext gc = canvas.getGraphicsContext2D();         //allows to draw the game
                                                                    //running run() every 10 milli-seconds
        Timeline t = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));

        t.setCycleCount(Timeline.INDEFINITE);                       //defines the number of cycles in animation - set to endless

        canvas.setOnMouseMoved(e ->  playerOneYPos  = e.getY());    //sets Y-position of player by mouse position
        canvas.setOnMouseClicked(e ->  gameStarted = true);         //first click on canvas starts the game
        window.setScene(new Scene(new StackPane(canvas)));          //create new scene with the canvas

        window.show();                                              //activate scene
        t.play();                                                   //activate timeline
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);                                    //sets the current color of GraphicContext
        gc.fillRect(0, 0, width, height);                       //fill the scene with the given color

        gc.setFill(Color.WHITE);                                    //sets the current color of GraphicContext
        gc.setFont(Font.font(30));                                  //set font size

        if (gameStarted) {                                          //game started after first click
            //setting ball movement
            ballXPos += ballXSpeed;                                 //updates X-pos given
            ballYPos += ballYSpeed;                                 //updates Y-pos given

            //computer player - moves computer player related to X-pos of ball
            if (ballXPos < width - width / 4) {
                playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
            }
            else {
                playerTwoYPos =  (ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ? playerTwoYPos += 2 : playerTwoYPos - 2);
            }

            gc.setFill(Color.DARKGRAY);
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);        //implements the ball
            gc.setFill(Color.WHITE);
        }
        else {                                                      //game didn't start yet
            gc.setStroke(Color.AQUA);
            gc.setTextAlign(TextAlignment.CENTER);                  //align text to the middle
                                                                    //display text
            gc.strokeText("CLICK TO START THE GAME", width / 2, height / 2);

            ballXPos = width / 2;                                   //ball is replaced in middle
            ballYPos = height / 2;                                  //ball is replaced in middle

            ballXSpeed = new Random().nextInt(2) == 0 ? 2: -2;  //speed of ball is being reset
            ballYSpeed = new Random().nextInt(2) == 0 ? 2: -2;  //speed of ball is being reset
        }

        if (ballYPos > height || ballYPos < 0) {                    //ball is out of window
            ballYSpeed *= -1;                                       //change Y direction
        }
        if (ballXPos < playerOneXPos - PLAYER_WIDTH) {              //computer won
            scoreP2++;                                              //add a point
            gameStarted = false;                                    //end game
        }
        if (ballXPos > playerTwoXPos + PLAYER_WIDTH) {              //player won
            scoreP1++;                                              //add a point
            gameStarted = false;                                    //end game
        }


        //increase the speed after the ball hits the player
        if( (ballXPos + BALL_R > playerTwoXPos && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
                (ballXPos < playerOneXPos + PLAYER_WIDTH && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballXSpeed *= -(1 + Math.random());                     //change X direction in random speed (between 1 to 2)
            ballYSpeed *= -(1 + Math.random());                     //change Y direction in random speed (between 1 to 2)
        }

        gc.fillText(scoreP1 + "\t\t\t\t\t" + scoreP2, width / 2, 100);


        gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);     //implement racket of player
        gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);     //implement racket of computer
    }

    // start the application
    public static void main(String[] args) {
        launch(args);
    }
}