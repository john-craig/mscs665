package project;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import sf.Sound;
import sf.SoundFactory;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class Board extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList blasts;
    private ArrayList barriers;
    private ArrayList xenos;
    private ArrayList aliens;
    private Mothership mothership;
    private Player player;
    private Shot shot;

    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;
    private int score = 0;
    private int shotsFired = 0;

    private boolean ingame = false;
    private boolean pregame = true;
    private final String expl = "/res/explosion.png";
    private final String alienpix = "/res/aliens.png";
    private final String alien_fires = "./res/alien_fires.wav";
    private final String player_fires = "./res/player_fires.wav";
    private final String alien_explodes = "./res/alien_explosion.wav";
    private final String bomb_lands = "./res/bombs_landing.wav";
    private final String shot_collision = "./res/shot_collision.wav";
    private final String player_hit = "./res/player_hit.wav";
    private final String spaceship_woosh = "./res/spaceship_woosh.wav";
    
    
    private String message = "Game Over";
    
    private Thread animator;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
    	blasts = new ArrayList();
    	barriers = new ArrayList();
    	aliens = new ArrayList();
    	xenos = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));
        
        for (int i=0; i < 4; i++) {
            for (int j=0; j < 5; j++) {
                Alien alien = new Alien(alienX + 18*j, alienY + 18*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }
    	
    	mothership = new Mothership(-30, 0, aliens);

    	for (int i=0; i < 3; i++) {
    		int barrierY = GROUND - 30;
    		int barrierX = (((BOARD_WIDTH / 3) - BARRIER_WIDTH) / 2) + ((BOARD_WIDTH / 3) * i);
    		
    		Barrier barrier = new Barrier(barrierX, barrierY);
    		barriers.add(barrier);
    	}
    	
        player = new Player();
        shot = new Shot();

        if (animator == null || !pregame) {
            animator = new Thread(this);
            animator.start();
        }
    }
    
    public void drawBlasts(Graphics g) {
        Iterator bl = blasts.iterator();
        		
       while (bl.hasNext()) {
    	   Blast blast = (Blast) bl.next();
    	   
    	   if(blast.isVisible()) {
    		   g.drawImage(blast.getImage(), blast.getX(), blast.getY(), this);
    	   }
    	   
    	   if(blast.isDying()) {
    		   blast.die();
    	   }
    			  
       }
    }
    
    public void drawMothership(Graphics g) {
    	if(mothership.isVisible()) {
    		g.drawImage(mothership.getImage(), mothership.getX(), mothership.getY(), this);
    	}
    	
    	if(mothership.isDying()) {
    		mothership.die();
    	}
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }
    
    public void drawXenos(Graphics g) 
    {
        Iterator xe = xenos.iterator();

        while (xe.hasNext()) {
            Alien xenos = (Alien) xe.next();

            if (xenos.isVisible()) {
                g.drawImage(xenos.getImage(), xenos.getX(), xenos.getY(), this);
            }

            if (xenos.isDying()) {
            	xenos.die();
            }
        }
    }


    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shot.isVisible())
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }

    public void drawBarriers(Graphics g)
    {
        Iterator ba = barriers.iterator();

        while (ba.hasNext()) {
            Barrier barrier = (Barrier)ba.next();

            if (barrier.isVisible()) {
                g.drawImage(barrier.getImage(), barrier.getX(), barrier.getY(), this);
            }

            if (barrier.isDying()) {
            	barrier.die();
            }
        }
    }
    
    public void drawFeedback(Graphics g) {
    	Font verySmall = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics vMetr = this.getFontMetrics(verySmall);
        
        String playerLives = "Lives: " + player.getLives();
        String hitRatio;
        
        if(shotsFired == 0) {
        	 hitRatio = "Hit Ratio: 0%";
        } else {
        	 hitRatio = "Hit Ratio: " + ((double)(deaths) / shotsFired) * 100.0 +"%";
        }
        
        String currentScore = "Score: " + score;
        		
        int y_offset = vMetr.getHeight() -2;
        
        g.setColor(Color.white);
        g.setFont(verySmall);
        
        g.drawString(playerLives, 1, y_offset);
        g.drawString(hitRatio, (BOARD_WIDTH - vMetr.stringWidth(hitRatio)) / 2, y_offset);
        g.drawString(currentScore, BOARD_WIDTH - ((vMetr.stringWidth(currentScore) * 2)), y_offset);

    }
    
    public void paint(Graphics g)
    {
      super.paint(g);

      g.setColor(Color.black);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.green);   

      if (pregame) {
    	  gameStart(g);
      }
      
      if (ingame) {

        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        drawBarriers(g);
        drawMothership(g);
        drawAliens(g);
        drawXenos(g);
        drawPlayer(g);
        drawShot(g);
        drawBombing(g);
        drawBlasts(g);
        
        drawFeedback(g);
      }
      
      if(!pregame && !ingame) {
    	  gameOver(g);
      }

      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    public void gameStart(Graphics g)
    {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        String title = "Void Invaders";
        
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(title, (BOARD_WIDTH - metr.stringWidth(title))/2, 
            BOARD_WIDTH/2);
        
        Font verySmall = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics vMetr = this.getFontMetrics(verySmall);

        String instructions = "Press Space to Start, Press Q to Quit";
        
        g.setColor(Color.white);
        g.setFont(verySmall);
        g.drawString(instructions, (BOARD_WIDTH - vMetr.stringWidth(instructions))/2, 
            (BOARD_WIDTH / 4) * 3);
    }
    
    public void gameOver(Graphics g)
    {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2, 
            BOARD_WIDTH/2);
        
        Font verySmall = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics vMetr = this.getFontMetrics(verySmall);

        int highscore_num = 99;
        String player_name = "AAA";
        
        String highscore = "Highscore: " + highscore_num;
        String initials = "Set By: " + player_name;
        
        g.setFont(verySmall);
        g.drawString(highscore, (BOARD_WIDTH - vMetr.stringWidth(highscore))/2, 
            (BOARD_WIDTH / 4) * 3);

        g.drawString(initials, (BOARD_WIDTH - vMetr.stringWidth(initials))/2, 
            (int) (((BOARD_WIDTH / 4) * 3) + (vMetr.getHeight() * 1.2)));
    }

    public void animationCycle()  {

        if (deaths == (aliens.size() + 1)) {
            ingame = false;
            message = "Game won!";
        }

        //player

        player.act();

        // shot
        if (shot.isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = shot.getX();
            int shotY = shot.getY();
            
            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();
                
                Alien.Bomb b = alien.getBomb();
               
                int bombX = b.getX();
                int bombY = b.getY();

                if (!b.isDestroyed()) {
                    if ( (bombX + 2) >= (shotX) && 
                        bombX <= (shotX + 2) &&
                        bombY >= (shotY) && 
                        bombY <= (shotY) ) {
                    		shot.die();
                            b.setDestroyed(true);
                            
                            blasts.add(new Blast(shotX, shotY));
                    		
                    		Sound sound = SoundFactory.getInstance(shot_collision);
                            SoundFactory.play(sound);
                        }
                }

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX) && 
                        shotX <= (alienX + ALIEN_WIDTH) &&
                        shotY >= (alienY) &&
                        shotY <= (alienY+ALIEN_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(getClass().getResource(expl));
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            deaths++;
                            score++;
                            shot.die();
                            
                            blasts.add(new Blast(shotX, shotY));
                            
                            Sound sound = SoundFactory.getInstance(alien_explodes);
                            SoundFactory.play(sound);
                        }
                }
            }
            
            if(mothership.isVisible() && shot.isVisible()) {
            	int mothershipX = mothership.getX();
            	int mothershipY = mothership.getY();
            	
            	if (shotX >= (mothershipX) && 
                        shotX <= (mothershipX + MOTHERSHIP_WIDTH) &&
                        shotY >= (mothershipY) &&
                        shotY <= (mothershipY+MOTHERSHIP_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(getClass().getResource(expl));
                            mothership.setImage(ii.getImage());
                            mothership.setDying(true);
                            deaths++;
                            score+=10;
                            shot.die();
                            
                            blasts.add(new Blast(shotX, shotY));
                            
                            Sound sound = SoundFactory.getInstance(alien_explodes);
                            SoundFactory.play(sound);
                        }
            }
            
            it = barriers.iterator();
            
            while(it.hasNext()) {
            	Barrier barrier = (Barrier)it.next();
            	int barX = barrier.getX();
            	int barY = barrier.getY();
            	
            	if(barrier.isVisible() && shot.isVisible()) {
            		if (shotX >= (barX) && 
                            shotX <= (barX + BARRIER_WIDTH) &&
                            shotY <= (barY)) {
                                shot.die();
                                barrier.takeHit();
                            }
            	}
            }

            int y = shot.getY();
            y -= 4;
            if (y < 0)
                shot.die();
            else shot.setY(y);
        }
        
        //blasts
        Iterator bl = blasts.iterator();
        
        while (bl.hasNext()) {
        	Blast blast = (Blast) bl.next();
        	blast.act();
        }

        // mothership
        if(mothership.isVisible()) {
        	mothership.act();
        }
        
        // aliens

         Iterator it1 = aliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int x = a1.getX();

             if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                 direction = -1;
                 Iterator i1 = aliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                     a2.setY(a2.getY() + GO_DOWN);
                     
                     Sound sound = SoundFactory.getInstance(spaceship_woosh);
                     SoundFactory.play(sound);
                 }
             }

            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setY(a.getY() + GO_DOWN);
                    
                    Sound sound = SoundFactory.getInstance(spaceship_woosh);
                    SoundFactory.play(sound);
                }
            }
        }


        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());   
                
                if(!i3.hasNext()) {
                	//Play the sound on the last bomb being "spawned"
                	 Sound sound = SoundFactory.getInstance(alien_fires);
                     SoundFactory.play(sound);
                }
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !b.isDestroyed()) {
                if ( bombX >= (playerX) && 
                    bombX <= (playerX+PLAYER_WIDTH) &&
                    bombY >= (playerY) && 
                    bombY <= (playerY+PLAYER_HEIGHT) ) {
                        player.loseLife();
                        b.setDestroyed(true);
                        
                        blasts.add(new Blast(bombX, bombY));
                        
                        Sound sound = SoundFactory.getInstance(player_hit);
                        SoundFactory.play(sound);
                    }
            }
            
            it = barriers.iterator();
            
            while(it.hasNext()) {
            	Barrier barrier = (Barrier)it.next();
            	int barX = barrier.getX();
            	int barY = barrier.getY();
            	
            	if(barrier.isVisible() && b.isVisible()) {
            		if (bombX >= (barX) && 
                            bombX <= (barX + BARRIER_WIDTH) &&
                            bombY >= (barY)) {
                                b.setDestroyed(true);
                                barrier.takeHit();
                            }
            	}
            }
            
            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);   
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                    
                    if(!i3.hasNext()) {
                    	//Play the sound on the last bomb is hitting the ground
                    	 Sound sound = SoundFactory.getInstance(bomb_lands);
                         SoundFactory.play(sound);
                    }
                }
            }
            
        }
    }

    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        
        while(pregame) {
        	repaint();
        }

        while (ingame) {
            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

          player.keyPressed(e);

          int x = player.getX();
          int y = player.getY();

          if(e.getKeyCode() == KeyEvent.VK_Q) {
        	  System.exit(0);
          }
          
          if (pregame) {
        	  if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        		  pregame = false;
        		  ingame = true;
        	  }
          }
          else if (ingame)
          {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (!shot.isVisible())
                    shot = new Shot(x, y);
                	shotsFired++;
                	Sound sound = SoundFactory.getInstance(player_fires);
                	SoundFactory.play(sound);

            }
          }
        }
    }
}