package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.util.*;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

public class Game
{
    @FXML
    public AnchorPane gameScene;
    @FXML
    public Label lengthLabel;
    @FXML
    public Label lengthText;
    @FXML
    public Button BacktomenuButton;
    @FXML
    public Text nickNameTEXT;
    public Label gameoverLabel;

    //List of the main snake and the bots
    private ArrayList<Ball> list;
    private ArrayList<Ball> botList1;
    private ArrayList<Ball> botList2;
    private ArrayList<Ball> botList3;
    private ArrayList<Ball> botList4;
    private ArrayList<Ball> botList5;
    private ArrayList<Food> foodList;
    private ArrayList<Food> foodList2;

    //Other variables that are needed
    private static double count=0;
    private int expand =0;
    private Random ran=new Random();
    private int z;
    private Color color;

    //Length of each snake
    private int snakeLength =10;
    private int bot1Length = 20 + ran.nextInt(20);
    private int bot2Length = 10 + ran.nextInt(20);
    private int bot3Length = 15 + ran.nextInt(20);
    private int bot4Length = 40 + ran.nextInt(20);
    private int bot5Length = 30 + ran.nextInt(20);

    @FXML
    public void initialize()
    {
        //For reading the value from file for changing the snakes color
        try {
            Scanner sc = new Scanner(new FileInputStream("names.txt"));
            boolean b;
            b = sc.hasNext();
            if (b)
                z = sc.nextInt();
            sc.close();
        }catch (Exception ignored){}
        if (z == 0)
            color = Color.WHITE;
        else if (z == 1)
            color = Color.YELLOW;
        else if (z == 2)
            color = Color.RED;
        else
            color = Color.CYAN;

        //Going back to the main menu
        BacktomenuButton.setOnAction(event -> {
            Stage G=(Stage) (gameScene.getScene().getWindow());
            G.close();

        });

        //Defining the objects for the main snake and the bots
        Ball[] balls = new Ball[snakeLength];
        list=new ArrayList<Ball>(snakeLength);
        for (int i = 0; i< snakeLength; i++) {
            balls[i]=new Ball(50,50,8,0,0, color);
            list.add(balls[i]);
        }

        Ball[] bot1 = new Ball[bot1Length];
        botList1=new ArrayList<Ball>(bot1Length);
        for (int i = 0; i < bot1Length; i++) {
            bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
            botList1.add(bot1[i]);
        }

        Ball[] bot2 = new Ball[bot2Length];
        botList2=new ArrayList<Ball>(bot2Length);
        for (int i = 0; i < bot2Length; i++) {
            bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
            botList2.add(bot2[i]);
        }

        Ball[] bot3 = new Ball[bot3Length];
        botList3=new ArrayList<Ball>(bot3Length);
        for (int i = 0; i < bot3Length; i++) {
            bot3[i]=new Ball(50,250,9,100,0, Color.SALMON);
            botList3.add(bot3[i]);
        }

        Ball[] bot4 = new Ball[bot4Length];
        botList4=new ArrayList<Ball>(bot4Length);
        for (int i = 0; i < bot4Length; i++) {
            bot4[i]=new Ball(50,350,10,100,0, Color.GREENYELLOW);
            botList4.add(bot4[i]);
        }

        Ball[] bot5 = new Ball[bot5Length];
        botList5=new ArrayList<Ball>(bot5Length);
        for (int i = 0; i < bot5Length; i++) {
            bot5[i]=new Ball(50,450,7,100,0, Color.SILVER);
            botList5.add(bot5[i]);
        }

        //Creating foods in the scene
        int foodNum = 100;
        Food[] foods = new Food[foodNum];
        foodList=new ArrayList<Food>(foodNum);
        foodList2=new ArrayList<Food>();

        for (int i = 0; i < foodNum; i++) {
            Random rand=new Random();
            double x=rand.nextInt(960);
            double y=rand.nextInt(550);
            double r=rand.nextInt(5);

            foods[i]=new Food(x,y,r+3.0,Color.
                    rgb(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255),0.8));
            foodList.add(foods[i]);
        }

        //Shows snakes and the foods in the scene
        gameScene.getChildren().addAll(foodList);
        gameScene.getChildren().addAll(foodList2);
        gameScene.getChildren().addAll(botList1);
        gameScene.getChildren().addAll(botList2);
        gameScene.getChildren().addAll(botList3);
        gameScene.getChildren().addAll(botList4);
        gameScene.getChildren().addAll(botList5);

        for (int i = list.size()-1; i >=0 ; i--) {
            gameScene.getChildren().add(list.get(i));
        }

        //Shows the length of the main snake at any moment
        String str=Integer.toString(snakeLength+ expand);
        lengthText.setText(str);

        //Moving the main snake
        gameScene.setOnMouseMoved((MouseEvent event) -> {
            //Getting the mouse position in the screen
            double a = event.getX();
            double c = event.getY();

            //Position of the main snakes head in the scene
            double Xb = Xplace(list.get(0));
            double Yb = Yplace(list.get(0));

            //Assigning speeds to each ball object of the main snake
            speed(list.get(0), a, c);
            for (int i = 1; i<list.size(); i++){
                speed(list.get(i), Xb, Yb);
                Xb = Xplace(list.get(i));
                Yb = Yplace(list.get(i));
            }
        });

        //For cloning the objects movements
        for (int i = 1; i <list.size(); i++){
            list.get(i).speedX = list.get(i-1).speedX;
            list.get(i).speedY = list.get(i-1).speedY;
        }

        //Reads the users name from a file
        try {
            Scanner sc = new Scanner(new FileInputStream("name.txt"));
            String s = sc.next();
            nickNameTEXT.setText("Nickname: "+s);
            sc.close();
        }
        catch (FileNotFoundException ignored){}

        //Adds animation for updating the scene
        AnimationTimer aa=new AnimationTimer() {
            long preTimeStamp;
            @Override
            public void start(){
                super.start();
                preTimeStamp=System.nanoTime();
            }
            @Override
            public void handle(long now) {
                long el=now-preTimeStamp;
                preTimeStamp=now;

                Point p=MouseInfo.getPointerInfo().getLocation();
                double x=p.getX();
                double y=p.getY();
                double Xb = Xplace(list.get(0));
                double Yb = Yplace(list.get(0));
                speed(list.get(0), x, y);
                for (int i = 1; i<list.size(); i++) {
                    speed(list.get(i), Xb, Yb);
                    Xb = Xplace(list.get(i));
                    Yb = Yplace(list.get(i));
                }
                checkCollision();
                updateWorld(el);
            }
        };
        aa.start();

    }

    //For moving the snakes in each moment
    private void updateWorld(long E) {
        double t=E/1_000_000_000.0;
        for(Ball ball:list) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
        for(Ball ball:botList1) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
        for(Ball ball:botList2) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
        for(Ball ball:botList3) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
        for(Ball ball:botList4) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
        for(Ball ball:botList5) {
            ball.setCenterX(ball.getCenterX()+ball.speedX*t);
            ball.setCenterY(ball.getCenterY()+ball.speedY*t);
        }
    }

    //Checking that whether snakes collate with each other or with the scene borders
    private void checkCollision() {
        for (Ball ball:list) {
            if (ball.getBoundsInParent().getMaxX() > gameScene.getWidth() - 5 && ball.speedX > 0) {
                ball.speedY = ball.speedX;
                ball.speedX = 0;
            }
            else if (ball.getBoundsInParent().getMinX() < 5 && ball.speedX < 0) {
                ball.speedY = ball.speedX;
                ball.speedX = 0;
            }
            else if (ball.getBoundsInParent().getMaxY() > gameScene.getHeight() - 20 && ball.speedY > 0) {
                ball.speedX = -ball.speedY;
                ball.speedY = 0;
            }
            else if (ball.getBoundsInParent().getMinY() < 5 && ball.speedY < 0) {
                ball.speedX = -ball.speedY;
                ball.speedY = 0;
            }
        }

        TurnToFood2(list, botList1);
        //Checks collision of the snake to scene borders
        for (Ball ball2:botList1) {
            if(ball2.getBoundsInParent().getMaxX()>gameScene.getWidth()-50&&ball2.speedX>0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;
            }
            else if(ball2.getBoundsInParent().getMinX()<50 &&ball2.speedX<0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;            }
            else if(ball2.getBoundsInParent().getMaxY()>gameScene.getHeight()-50&&ball2.speedY>0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
            else if(ball2.getBoundsInParent().getMinY()<50&&ball2.speedY<0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
        }

        TurnToFood2(list, botList2);
        //Checks collision of the snake to scene borders
        for (Ball ball2:botList2) {
            if(ball2.getBoundsInParent().getMaxX()>gameScene.getWidth()-100&&ball2.speedX>0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;
            }
            else if(ball2.getBoundsInParent().getMinX()<100 &&ball2.speedX<0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;            }
            else if(ball2.getBoundsInParent().getMaxY()>gameScene.getHeight()-100&&ball2.speedY>0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
            else if(ball2.getBoundsInParent().getMinY()<100&&ball2.speedY<0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
        }

        TurnToFood2(list, botList3);
        //Checks collision of the snake to scene borders
        for (Ball ball2:botList3) {
            if(ball2.getBoundsInParent().getMaxX()>gameScene.getWidth()-150&&ball2.speedX>0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;
            }
            else if(ball2.getBoundsInParent().getMinX()<150 &&ball2.speedX<0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;            }
            else if(ball2.getBoundsInParent().getMaxY()>gameScene.getHeight()-150&&ball2.speedY>0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
            else if(ball2.getBoundsInParent().getMinY()<150&&ball2.speedY<0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
        }

        TurnToFood2(list, botList4);
        //Checks collision of the snake to scene borders
        for (Ball ball2:botList4) {
            if(ball2.getBoundsInParent().getMaxX()>gameScene.getWidth()-200&&ball2.speedX>0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;
            }
            else if(ball2.getBoundsInParent().getMinX()<200 &&ball2.speedX<0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;            }
            else if(ball2.getBoundsInParent().getMaxY()>gameScene.getHeight()-200&&ball2.speedY>0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
            else if(ball2.getBoundsInParent().getMinY()<200&&ball2.speedY<0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
        }

        TurnToFood2(list, botList5);
        //Checks collision of the snake to scene borders
        for (Ball ball2:botList5) {
            if(ball2.getBoundsInParent().getMaxX()>gameScene.getWidth()-250&&ball2.speedX>0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;
            }
            else if(ball2.getBoundsInParent().getMinX()<250 &&ball2.speedX<0) {
                ball2.speedY=ball2.speedX;
                ball2.speedX=0;            }
            else if(ball2.getBoundsInParent().getMaxY()>gameScene.getHeight()-250&&ball2.speedY>0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
            else if(ball2.getBoundsInParent().getMinY()<250&&ball2.speedY<0) {
                ball2.speedX=-1*ball2.speedY;
                ball2.speedY=0;
            }
        }

        //For adding the foods to the snakes length when the snake reaches it
        for (Food food:foodList) {
            if(food.getBoundsInParent().intersects(list.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,8,0,0, color);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(list.get(list.size()-1).getLayoutX()+list.get(list.size()-1).getCenterX());
                b.setLayoutY(list.get(list.size()-1).getLayoutY()+list.get(list.size()-1).getCenterY());
                list.add(b);

                expand++;
                lengthText.setText(Integer.toString(snakeLength+ expand));

                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList1.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,6,botList1.get(botList1.size()-1).speedX,botList1.get(botList1.size()-1).speedY, Color.YELLOW);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList1.get(botList1.size()-1).getLayoutX()+botList1.get(botList1.size()-1).getCenterX());
                b.setLayoutY(botList1.get(botList1.size()-1).getLayoutY()+botList1.get(botList1.size()-1).getCenterY());
                botList1.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList2.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,9,botList2.get(botList2.size()-1).speedX,botList2.get(botList2.size()-1).speedY, Color.FUCHSIA);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList2.get(botList2.size()-1).getLayoutX()+botList2.get(botList2.size()-1).getCenterX());
                b.setLayoutY(botList2.get(botList2.size()-1).getLayoutY()+botList2.get(botList2.size()-1).getCenterY());
                botList2.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList3.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,9,botList3.get(botList3.size()-1).speedX,botList3.get(botList3.size()-1).speedY, Color.SALMON);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList3.get(botList3.size()-1).getLayoutX()+botList3.get(botList3.size()-1).getCenterX());
                b.setLayoutY(botList3.get(botList3.size()-1).getLayoutY()+botList3.get(botList3.size()-1).getCenterY());
                botList3.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList4.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,10,botList4.get(botList4.size()-1).speedX,botList4.get(botList4.size()-1).speedY, Color.GREENYELLOW);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList4.get(botList4.size()-1).getLayoutX()+botList4.get(botList4.size()-1).getCenterX());
                b.setLayoutY(botList4.get(botList4.size()-1).getLayoutY()+botList4.get(botList4.size()-1).getCenterY());
                botList4.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList5.get(0).getBoundsInParent())) {
                Random r=new Random();
                food.setLayoutX(r.nextInt(960));
                food.setLayoutY(r.nextInt(550));
                food.setCenterX(0);
                food.setCenterY(0);

                Ball b=new Ball(50,50,7,botList5.get(botList5.size()-1).speedX,botList5.get(botList5.size()-1).speedY, Color.SILVER);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList5.get(botList5.size()-1).getLayoutX()+botList5.get(botList5.size()-1).getCenterX());
                b.setLayoutY(botList5.get(botList5.size()-1).getLayoutY()+botList5.get(botList5.size()-1).getCenterY());
                botList5.add(b);
                gameScene.getChildren().addAll(b);
            }
        }
        for (Food food:foodList2) {
            if(food.getBoundsInParent().intersects(list.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,8,0,0, color);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(list.get(list.size()-1).getLayoutX()+list.get(list.size()-1).getCenterX());
                b.setLayoutY(list.get(list.size()-1).getLayoutY()+list.get(list.size()-1).getCenterY());
                list.add(b);

                expand++;
                lengthText.setText(Integer.toString(snakeLength+ expand));

                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList1.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,6,botList1.get(botList1.size()-1).speedX,botList1.get(botList1.size()-1).speedY, Color.YELLOW);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList1.get(botList1.size()-1).getLayoutX()+botList1.get(botList1.size()-1).getCenterX());
                b.setLayoutY(botList1.get(botList1.size()-1).getLayoutY()+botList1.get(botList1.size()-1).getCenterY());
                botList1.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList2.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,9,botList2.get(botList2.size()-1).speedX,botList2.get(botList2.size()-1).speedY, Color.FUCHSIA);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList2.get(botList2.size()-1).getLayoutX()+botList2.get(botList2.size()-1).getCenterX());
                b.setLayoutY(botList2.get(botList2.size()-1).getLayoutY()+botList2.get(botList2.size()-1).getCenterY());
                botList2.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList3.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,9,botList3.get(botList3.size()-1).speedX,botList3.get(botList3.size()-1).speedY, Color.SALMON);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList3.get(botList3.size()-1).getLayoutX()+botList3.get(botList3.size()-1).getCenterX());
                b.setLayoutY(botList3.get(botList3.size()-1).getLayoutY()+botList3.get(botList3.size()-1).getCenterY());
                botList3.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList4.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,10,botList4.get(botList4.size()-1).speedX,botList4.get(botList4.size()-1).speedY, Color.GREENYELLOW);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList4.get(botList4.size()-1).getLayoutX()+botList4.get(botList4.size()-1).getCenterX());
                b.setLayoutY(botList4.get(botList4.size()-1).getLayoutY()+botList4.get(botList4.size()-1).getCenterY());
                botList4.add(b);
                gameScene.getChildren().addAll(b);

            }
            if(food.getBoundsInParent().intersects(botList5.get(0).getBoundsInParent())) {
                gameScene.getChildren().removeAll(food);
                Ball b=new Ball(50,50,7,botList5.get(botList5.size()-1).speedX,botList5.get(botList5.size()-1).speedY, Color.SILVER);
                b.setCenterX(0);
                b.setCenterY(0);
                b.setLayoutX(botList5.get(botList5.size()-1).getLayoutX()+botList5.get(botList5.size()-1).getCenterX());
                b.setLayoutY(botList5.get(botList5.size()-1).getLayoutY()+botList5.get(botList5.size()-1).getCenterY());
                botList5.add(b);
                gameScene.getChildren().addAll(b);
            }
        }

        //Checks the collision of each snake to the other ones
        //If the collate then they will turn into foods and the bots will start from first
        for (Ball ball:list) {
            if (botList1.get(bot1Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList1);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList1.size());
                Food[] foods = new Food[botList1.size()];
                for (int i = botList1.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(botList1.get(i)), Yplace(botList1.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot1 = new Ball[botList1.size()];
                botList1.removeAll(botList1);
                botList1 = new ArrayList<Ball>(bot1Length);

                for (int i = bot1Length; i > -1 ; i--) {
                    bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
                    botList1.add(bot1[i]);
                }
                gameScene.getChildren().addAll(botList1);
            }
            if (botList2.get(bot2Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList2);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList2.size());
                Food[] foods2 = new Food[botList2.size()];
                for (int i = botList2.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods2[i] = new Food(Xplace(botList2.get(i)), Yplace(botList2.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods2[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot2 = new Ball[botList2.size()];
                botList2.removeAll(botList2);
                botList2 = new ArrayList<Ball>(bot2Length);

                for (int i = bot2Length; i > -1 ; i--) {
                    bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList2.add(bot2[i]);
                }
                gameScene.getChildren().addAll(botList2);
            }
            if (botList3.get(bot3Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList3);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList3.size());
                Food[] foods3 = new Food[botList3.size()];
                for (int i = botList3.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods3[i] = new Food(Xplace(botList3.get(i)), Yplace(botList3.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods3[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot3 = new Ball[botList3.size()];
                botList3.removeAll(botList3);
                botList3 = new ArrayList<Ball>(bot3Length);

                for (int i = bot3Length; i > -1 ; i--) {
                    bot3[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList3.add(bot3[i]);
                }
                gameScene.getChildren().addAll(botList3);
            }
            if (botList4.get(bot4Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList4);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList4.size());
                Food[] foods4 = new Food[botList4.size()];
                for (int i = botList4.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods4[i] = new Food(Xplace(botList4.get(i)), Yplace(botList4.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods4[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot4 = new Ball[botList4.size()];
                botList4.removeAll(botList4);
                botList4 = new ArrayList<Ball>(bot4Length);

                for (int i = bot4Length - 1; i > -1 ; i--) {
                    bot4[i]=new Ball(50,150,10,100,0, Color.GREENYELLOW);
                    botList4.add(bot4[i]);
                }
                gameScene.getChildren().addAll(botList4);
            }
            if (botList5.get(bot5Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList5);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList5.size());
                Food[] foods5 = new Food[botList5.size()];
                for (int i = botList5.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods5[i] = new Food(Xplace(botList5.get(i)), Yplace(botList5.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods5[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot5 = new Ball[botList5.size()];
                botList5.removeAll(botList5);
                botList5 = new ArrayList<Ball>(bot5Length);

                for (int i = bot5Length - 1; i > -1 ; i--) {
                    bot5[i]=new Ball(50,350,7,100,0, Color.SILVER);
                    botList5.add(bot5[i]);
                }
                gameScene.getChildren().addAll(botList5);
            }
        }
        for (Ball ball:botList1) {
            if (botList2.get(bot2Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList2);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList2.size());
                Food[] foods2 = new Food[botList2.size()];
                for (int i = botList2.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods2[i] = new Food(Xplace(botList2.get(i)), Yplace(botList2.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods2[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot2 = new Ball[botList2.size()];
                botList2.removeAll(botList2);
                botList2 = new ArrayList<Ball>(bot2Length);

                for (int i = bot2Length; i > -1 ; i--) {
                    bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList2.add(bot2[i]);
                }
                gameScene.getChildren().addAll(botList2);
            }
            if (botList3.get(bot3Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList3);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList3.size());
                Food[] foods3 = new Food[botList3.size()];
                for (int i = botList3.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods3[i] = new Food(Xplace(botList3.get(i)), Yplace(botList3.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods3[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot3 = new Ball[botList3.size()];
                botList3.removeAll(botList3);
                botList3 = new ArrayList<Ball>(bot3Length);

                for (int i = bot3Length; i > -1 ; i--) {
                    bot3[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList3.add(bot3[i]);
                }
                gameScene.getChildren().addAll(botList3);
            }
            if (botList4.get(bot4Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList4);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList4.size());
                Food[] foods4 = new Food[botList4.size()];
                for (int i = botList4.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods4[i] = new Food(Xplace(botList4.get(i)), Yplace(botList4.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods4[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot4 = new Ball[botList4.size()];
                botList4.removeAll(botList4);
                botList4 = new ArrayList<Ball>(bot4Length);

                for (int i = bot4Length - 1; i > -1 ; i--) {
                    bot4[i]=new Ball(50,150,10,100,0, Color.GREENYELLOW);
                    botList4.add(bot4[i]);
                }
                gameScene.getChildren().addAll(botList4);
            }
            if (botList5.get(bot5Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList5);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList5.size());
                Food[] foods5 = new Food[botList5.size()];
                for (int i = botList5.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods5[i] = new Food(Xplace(botList5.get(i)), Yplace(botList5.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods5[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot5 = new Ball[botList5.size()];
                botList5.removeAll(botList5);
                botList5 = new ArrayList<Ball>(bot5Length);

                for (int i = bot5Length - 1; i > -1 ; i--) {
                    bot5[i]=new Ball(50,350,7,100,0, Color.SILVER);
                    botList5.add(bot5[i]);
                }
                gameScene.getChildren().addAll(botList5);
            }
        }
        for (Ball ball:botList2) {
            if (botList1.get(bot1Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList1);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList1.size());
                Food[] foods = new Food[botList1.size()];
                for (int i = botList1.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(botList1.get(i)), Yplace(botList1.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot1 = new Ball[botList1.size()];
                botList1.removeAll(botList1);
                botList1 = new ArrayList<Ball>(bot1Length);

                for (int i = bot1Length; i > -1 ; i--) {
                    bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
                    botList1.add(bot1[i]);
                }
                gameScene.getChildren().addAll(botList1);
            }
            if (botList3.get(bot3Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList3);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList3.size());
                Food[] foods3 = new Food[botList3.size()];
                for (int i = botList3.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods3[i] = new Food(Xplace(botList3.get(i)), Yplace(botList3.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods3[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot3 = new Ball[botList3.size()];
                botList3.removeAll(botList3);
                botList3 = new ArrayList<Ball>(bot3Length);

                for (int i = bot3Length; i > -1 ; i--) {
                    bot3[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList3.add(bot3[i]);
                }
                gameScene.getChildren().addAll(botList3);
            }
            if (botList4.get(bot4Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList4);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList4.size());
                Food[] foods4 = new Food[botList4.size()];
                for (int i = botList4.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods4[i] = new Food(Xplace(botList4.get(i)), Yplace(botList4.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods4[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot4 = new Ball[botList4.size()];
                botList4.removeAll(botList4);
                botList4 = new ArrayList<Ball>(bot4Length);

                for (int i = bot4Length - 1; i > -1 ; i--) {
                    bot4[i]=new Ball(50,150,10,100,0, Color.GREENYELLOW);
                    botList4.add(bot4[i]);
                }
                gameScene.getChildren().addAll(botList4);
            }
            if (botList5.get(bot5Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList5);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList5.size());
                Food[] foods5 = new Food[botList5.size()];
                for (int i = botList5.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods5[i] = new Food(Xplace(botList5.get(i)), Yplace(botList5.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods5[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot5 = new Ball[botList5.size()];
                botList5.removeAll(botList5);
                botList5 = new ArrayList<Ball>(bot5Length);

                for (int i = bot5Length - 1; i > -1 ; i--) {
                    bot5[i]=new Ball(50,350,7,100,0, Color.SILVER);
                    botList5.add(bot5[i]);
                }
                gameScene.getChildren().addAll(botList5);
            }
        }
        for (Ball ball:botList3) {
            if (botList1.get(bot1Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList1);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList1.size());
                Food[] foods = new Food[botList1.size()];
                for (int i = botList1.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(botList1.get(i)), Yplace(botList1.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot1 = new Ball[botList1.size()];
                botList1.removeAll(botList1);
                botList1 = new ArrayList<Ball>(bot1Length);

                for (int i = bot1Length; i > -1 ; i--) {
                    bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
                    botList1.add(bot1[i]);
                }
                gameScene.getChildren().addAll(botList1);
            }
            if (botList2.get(bot2Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList2);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList2.size());
                Food[] foods2 = new Food[botList2.size()];
                for (int i = botList2.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods2[i] = new Food(Xplace(botList2.get(i)), Yplace(botList2.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods2[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot2 = new Ball[botList2.size()];
                botList2.removeAll(botList2);
                botList2 = new ArrayList<Ball>(bot2Length);

                for (int i = bot2Length; i > -1 ; i--) {
                    bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList2.add(bot2[i]);
                }
                gameScene.getChildren().addAll(botList2);
            }
            if (botList4.get(bot4Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList4);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList4.size());
                Food[] foods4 = new Food[botList4.size()];
                for (int i = botList4.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods4[i] = new Food(Xplace(botList4.get(i)), Yplace(botList4.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods4[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot4 = new Ball[botList4.size()];
                botList4.removeAll(botList4);
                botList4 = new ArrayList<Ball>(bot4Length);

                for (int i = bot4Length - 1; i > -1 ; i--) {
                    bot4[i]=new Ball(50,150,10,100,0, Color.GREENYELLOW);
                    botList4.add(bot4[i]);
                }
                gameScene.getChildren().addAll(botList4);
            }
            if (botList5.get(bot5Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList5);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList5.size());
                Food[] foods5 = new Food[botList5.size()];
                for (int i = botList5.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods5[i] = new Food(Xplace(botList5.get(i)), Yplace(botList5.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods5[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot5 = new Ball[botList5.size()];
                botList5.removeAll(botList5);
                botList5 = new ArrayList<Ball>(bot5Length);

                for (int i = bot5Length - 1; i > -1 ; i--) {
                    bot5[i]=new Ball(50,350,7,100,0, Color.SILVER);
                    botList5.add(bot5[i]);
                }
                gameScene.getChildren().addAll(botList5);
            }
        }
        for (Ball ball:botList4) {
            if (botList1.get(bot1Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList1);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList1.size());
                Food[] foods = new Food[botList1.size()];
                for (int i = botList1.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(botList1.get(i)), Yplace(botList1.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot1 = new Ball[botList1.size()];
                botList1.removeAll(botList1);
                botList1 = new ArrayList<Ball>(bot1Length);

                for (int i = bot1Length; i > -1 ; i--) {
                    bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
                    botList1.add(bot1[i]);
                }
                gameScene.getChildren().addAll(botList1);
            }
            if (botList2.get(bot2Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList2);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList2.size());
                Food[] foods2 = new Food[botList2.size()];
                for (int i = botList2.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods2[i] = new Food(Xplace(botList2.get(i)), Yplace(botList2.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods2[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot2 = new Ball[botList2.size()];
                botList2.removeAll(botList2);
                botList2 = new ArrayList<Ball>(bot2Length);

                for (int i = bot2Length; i > -1 ; i--) {
                    bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList2.add(bot2[i]);
                }
                gameScene.getChildren().addAll(botList2);
            }
            if (botList3.get(bot3Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList3);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList3.size());
                Food[] foods3 = new Food[botList3.size()];
                for (int i = botList3.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods3[i] = new Food(Xplace(botList3.get(i)), Yplace(botList3.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods3[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot3 = new Ball[botList3.size()];
                botList3.removeAll(botList3);
                botList3 = new ArrayList<Ball>(bot3Length);

                for (int i = bot3Length; i > -1 ; i--) {
                    bot3[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList3.add(bot3[i]);
                }
                gameScene.getChildren().addAll(botList3);
            }
            if (botList5.get(bot5Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList5);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList5.size());
                Food[] foods5 = new Food[botList5.size()];
                for (int i = botList5.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods5[i] = new Food(Xplace(botList5.get(i)), Yplace(botList5.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods5[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot5 = new Ball[botList5.size()];
                botList5.removeAll(botList5);
                botList5 = new ArrayList<Ball>(bot5Length);

                for (int i = bot5Length - 1; i > -1 ; i--) {
                    bot5[i]=new Ball(50,350,7,100,0, Color.SILVER);
                    botList5.add(bot5[i]);
                }
                gameScene.getChildren().addAll(botList5);
            }
        }
        for (Ball ball:botList5) {
            if (botList1.get(bot1Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList1);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList1.size());
                Food[] foods = new Food[botList1.size()];
                for (int i = botList1.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(botList1.get(i)), Yplace(botList1.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot1 = new Ball[botList1.size()];
                botList1.removeAll(botList1);
                botList1 = new ArrayList<Ball>(bot1Length);

                for (int i = bot1Length; i > -1 ; i--) {
                    bot1[i]=new Ball(150,100,6,100,0, Color.YELLOW);
                    botList1.add(bot1[i]);
                }
                gameScene.getChildren().addAll(botList1);
            }
            if (botList2.get(bot2Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList2);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList2.size());
                Food[] foods2 = new Food[botList2.size()];
                for (int i = botList2.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods2[i] = new Food(Xplace(botList2.get(i)), Yplace(botList2.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods2[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot2 = new Ball[botList2.size()];
                botList2.removeAll(botList2);
                botList2 = new ArrayList<Ball>(bot2Length);

                for (int i = bot2Length; i > -1 ; i--) {
                    bot2[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList2.add(bot2[i]);
                }
                gameScene.getChildren().addAll(botList2);
            }
            if (botList3.get(bot3Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList3);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList3.size());
                Food[] foods3 = new Food[botList3.size()];
                for (int i = botList3.size() - 1; i > -1 ; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods3[i] = new Food(Xplace(botList3.get(i)), Yplace(botList3.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods3[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot3 = new Ball[botList3.size()];
                botList3.removeAll(botList3);
                botList3 = new ArrayList<Ball>(bot3Length);

                for (int i = bot3Length; i > -1 ; i--) {
                    bot3[i]=new Ball(50,150,9,100,0, Color.FUCHSIA);
                    botList3.add(bot3[i]);
                }
                gameScene.getChildren().addAll(botList3);
            }
            if (botList4.get(bot4Length - 1).getBoundsInParent().intersects(ball.getBoundsInParent())) {
                gameScene.getChildren().removeAll(botList4);
                gameScene.getChildren().removeAll(foodList2);
                foodList2 = new ArrayList<Food>(botList4.size());
                Food[] foods4 = new Food[botList4.size()];
                for (int i = botList4.size() - 1; i > -1; i--) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods4[i] = new Food(Xplace(botList4.get(i)), Yplace(botList4.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods4[i]);
                }
                gameScene.getChildren().addAll(foodList2);


                Ball[] bot4 = new Ball[botList4.size()];
                botList4.removeAll(botList4);
                botList4 = new ArrayList<Ball>(bot4Length);

                for (int i = bot4Length - 1; i > -1 ; i--) {
                    bot4[i]=new Ball(50,150,10,100,0, Color.GREENYELLOW);
                    botList4.add(bot4[i]);
                }
                gameScene.getChildren().addAll(botList4);
            }
        }
    }

    //Creates the foods
    private class Food extends Circle {
        Food(double X,double Y,double R,Color c) {
            super(X,Y,R,c);
        }
    }

    //Creates the objects needed for making the snakes
    private class Ball extends Food {
        double speedX,speedY;
        Ball(double X, double Y, double R, double speedX, double speedY, Color c) {
            super(X,Y,R,c);

            this.speedX=speedX;
            this.speedY=speedY;
            if (count>960)
                count=0;

            setLayoutX(count);
            count+=3;
            setLayoutY(0);
        }
    }

    //When the main snake collates to other snakes this will show a game over label for
    //3 seconds and then it will load the main menu
    private void GameOver(boolean a) throws InterruptedException{
        if(a)
        {
            gameoverLabel.setText("Game Over!!!");
            PauseTransition delay=new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> {
                Stage G=(Stage)(gameScene.getScene().getWindow());
                G.close();
            });
            delay.play();
        }
    }

    //Assigns a ball object's speed
    private void speed( Ball b, double x, double y) {
        double ba = Xplace(b);
        double bb = Yplace(b);

        double c = Math.atan((y - bb) / (x - ba));

        if (x > ba) {
            b.speedX = 100 * Math.cos(c);
            if (y > bb)
                b.speedY = -100 * Math.sin(-c);
            else
                b.speedY = -100 * Math.sin(-c);
        }
        else {
            b.speedX = -100 * Math.cos(c);
            if (y > bb)
                b.speedY = 100 * Math.sin(-c);
            else
                b.speedY = 100 * Math.sin(-c);
        }
    }

    //Gets the position of the ball object in the screen
    private double Xplace(Ball b){
        return b.getLayoutX() + b.getCenterX();
    }
    private double Yplace(Ball b){
        return b.getLayoutY() + b.getCenterY();
    }

    //Checks the collision of the main snake's head to the other snakes
    private void TurnToFood2(ArrayList<Ball> b, ArrayList<Ball> b1) {
        for (Ball ball3: b1) {
            if (b.get(0).getBoundsInParent().intersects(ball3.getBoundsInParent())) {
                gameScene.getChildren().removeAll(b);
                foodList2 = new ArrayList<Food>(b.size());
                Food[] foods = new Food[b.size()];
                for (int i = 0; i < b.size(); i++) {
                    Random rand = new Random();
                    double r = rand.nextInt(5);

                    foods[i] = new Food(Xplace(b.get(i)), Yplace(b.get(i)), r + 3.0, Color.
                            rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255), 0.8));
                    foodList2.add(foods[i]);
                }
                gameScene.getChildren().addAll(foodList2);
                try {
                    GameOver(true);
                } catch (InterruptedException e){}
            }
        }
    }
}