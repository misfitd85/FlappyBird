import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class flappyBird implements ActionListener, MouseListener {
//comment for git testing

    public static flappyBird flappyBird;

    public  final int WIDTH = 800, HEIGHT = 800;

    public Renderer renderer;
    public Rectangle bird;

    //motion of the bird
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public static ArrayList<Rectangle> columns;

    public Random rand;



    public  flappyBird(){
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        jframe.setTitle("Flappy Bird");


        renderer = new Renderer();
        rand = new Random();
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH/ 2-10, HEIGHT/2-10, 20, 20);
        columns = new ArrayList<Rectangle>();


        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

    }


    //logic for the jump function
    public void jump(){
        if(gameOver){

            bird = new Rectangle(WIDTH/ 2-10, HEIGHT/2-10, 20, 20);
            //columns = new ArrayList<Rectangle>();
            columns.clear();

            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if(!started){
            started = true;
        }
        else if(!gameOver){
        if(yMotion > 0){
            yMotion = 0;
        }
        yMotion -= 10;
        }
    }


    //event to allow repainting of background/bird
    @Override
    public void actionPerformed(ActionEvent e) {

        //giving motion to the bird
        int speed = 10;

        ticks++;

        if(started) {
            //logic to move columns over
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }


            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);

                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }
        }

//advances bird movement
        bird.y += yMotion;

        //checks for collision after bird moves
    //for each column inside columns
        //ends game if the bird hits column or ground
        for(Rectangle column : columns){
            if(column.intersects(bird))
            {
                gameOver = true;

                if(bird.x <= column.x) {
                    bird.x = column.x - bird.width;
                }
                else
                {
                    if (column.y != 0)
                    {
                        bird.y = column.y - bird.height;
                    }
                    else if (bird.y < column.height)
                    {
                        bird.y = column.height;
                    }}
            }
        }
    if(bird.y > HEIGHT -120 || bird.y <0){

        gameOver = true;
    }
 if(gameOver){
     bird.y = HEIGHT - 120 - bird.height;
 }

        renderer.repaint();

    }

//logic to add columns

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);


        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size()-1).x +600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - space));

        }
    }

//new event for painting columns

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);

    }


//repaint method

    public void repaint(Graphics g){
        //background color
        g.setColor(Color.cyan);
        g.fillRect(0,0, WIDTH, HEIGHT);

        //ground color
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT-150, WIDTH, 150);

        //grass on ground
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT-160, WIDTH, 20);

        //bird color
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);


        //iterator to paint columns

        for(Rectangle column: columns){
            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));


        //logic to start the game and then to print a screen when the game is over

        if(!started){
            g.drawString("Click to start!", 100, HEIGHT / 2-50);
        }


        if(gameOver){
            g.drawString("Game Over!", 100, HEIGHT / 2-50);
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

        jump();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}