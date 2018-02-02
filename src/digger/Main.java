package digger;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyAdapter;
    import java.awt.event.KeyEvent;
    import java.awt.event.KeyListener;
    import javafx.scene.input.KeyCode;

public class Main extends JFrame
{
    
    double sWidth = this.getSize().getWidth();
    double sHeight = this.getSize().getHeight();
    
    JPanel sPanel = new JPanel();
    AnimationPanel aPanel = new AnimationPanel();
    JPanel cPanel = new JPanel();
    
    JLabel eLabel = new JLabel("1");
    JLabel sLabel = new JLabel("Score: ");
    JLabel cScore = new JLabel("# yourScore");
    JLabel lLabel = new JLabel("Lifes: ");
    JLabel cLifes = new JLabel("# yourLifes");
    
    ControlButton uButton = new ControlButton("Up");
    ControlButton dButton = new ControlButton("Down");
    ControlButton lButton = new ControlButton("Left");
    ControlButton rButton = new ControlButton("Right");
    ControlButton sButton = new ControlButton("Start");
    ControlButton pButton = new ControlButton("Stop");

    DiggerClass diggerObject = new DiggerClass();
    Image diggerImage;
    Image diggerImageOpen;
    Image diggerImageClose;
  
    static String diggerDirection;
    
    public Main()
    {
        
        this.setTitle("Digger 1983 - tribute game");
        this.setBounds(300,300,600,400);
        
        this.getContentPane().add(sPanel, BorderLayout.NORTH);
            sPanel.setBorder(BorderFactory.createBevelBorder(0));
            sPanel.setPreferredSize(new Dimension((int)sWidth, 30));
            sPanel.setLayout(new BoxLayout(sPanel,BoxLayout.LINE_AXIS));
                sPanel.add(Box.createRigidArea(new Dimension(40,0)));
                sPanel.add(sLabel);
                sPanel.add(Box.createRigidArea(new Dimension(20,0)));
                sPanel.add(cScore);
                sPanel.add(Box.createHorizontalGlue());
                sPanel.add(lLabel);
                sPanel.add(Box.createRigidArea(new Dimension(20,0)));
                sPanel.add(cLifes); 
                sPanel.add(Box.createRigidArea(new Dimension(40,0)));
                
        this.getContentPane().add(aPanel, BorderLayout.CENTER);
            aPanel.setBorder(BorderFactory.createBevelBorder(1));
            aPanel.setBackground(Color.GRAY);
            
        this.getContentPane().add(cPanel, BorderLayout.SOUTH);
        
            cPanel.setBorder(BorderFactory.createBevelBorder(0));
            cPanel.setPreferredSize(new Dimension((int)sWidth, 55));
            cPanel.setLayout(new BorderLayout());
            
            Box uBox = Box.createHorizontalBox();
                uBox.add(Box.createHorizontalGlue());
                uBox.add(uButton).setPreferredSize(new Dimension(80,25));
                uBox.add(Box.createHorizontalGlue());
                cPanel.add(uBox, BorderLayout.NORTH);
                
            Box lBox = Box.createHorizontalBox();
            
                lBox.add(Box.createHorizontalGlue());
                lBox.add(sButton).setPreferredSize(new Dimension(80,25));
                lBox.add(Box.createRigidArea(new Dimension(80,25)));
                lBox.add(lButton).setPreferredSize(new Dimension(80,25));
                lBox.add(dButton).setPreferredSize(new Dimension(80,25));
                lBox.add(rButton).setPreferredSize(new Dimension(80,25));
                lBox.add(Box.createRigidArea(new Dimension(80,25)));
                lBox.add(pButton).setPreferredSize(new Dimension(80,25));
                lBox.add(Box.createHorizontalGlue());
                cPanel.add(lBox, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    }

           
    class AnimationPanel extends JPanel
    {
        Thread sThread;
        ThreadGroup gThreads = new ThreadGroup("Group of Threads");

        public void addDigger()
        {
            sThread = new Thread(gThreads, new DiggerRunnable(diggerDirection));
            sThread.start();
        }
        
        class DiggerRunnable implements Runnable
        {
            String diggerDirection;
            
            DiggerRunnable (String diggerDirection)
            {
                this.diggerDirection = diggerDirection;
            }
            
            @Override
            public void run() 
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    if (uButton.getDirection() == "Up" ) 
                    {
                        diggerImageOpen = diggerObject.diggerGoUpOpen;
                        diggerImageClose = diggerObject.diggerGoUpClose;
                    }
                    else if (dButton.getDirection() == "Down" ) 
                    {
                        diggerImageOpen = diggerObject.diggerGoDownOpen;
                        diggerImageClose = diggerObject.diggerGoDownClose;
                    }
                    else if (lButton.getDirection() == "Left" ) 
                    {
                        diggerImageOpen = diggerObject.diggerGoLeftOpen;
                        diggerImageClose = diggerObject.diggerGoLeftClose;
                    }
                    else if (rButton.getDirection() == "Right" ) 
                    {
                        diggerImageOpen = diggerObject.diggerGoRightOpen;
                        diggerImageClose = diggerObject.diggerGoRightClose;
                    }        
                    else
                    {
                        diggerImageOpen = diggerObject.diggerGoRightOpen;
                        diggerImageClose = diggerObject.diggerGoRightClose;
                    }
                    try 
                    {
                        diggerImage = diggerImageOpen;
                        aPanel.repaint();
                        Thread.sleep(5);
                        diggerImage = diggerImageClose;
                        aPanel.repaint();
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                }
                    
                }
         
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage (diggerImage, diggerObject.x, diggerObject.y, null);
        }
        
        public void startGame()
        {
            addDigger();
        }
        
        public void endGame()
        {
            gThreads.interrupt();
        }
    }
    
    class DiggerClass
    {
        public Image diggerGoRightOpen = new ImageIcon("DiggerGoRightOpen.png").getImage();
        public Image diggerGoRightClose = new ImageIcon("DiggerGoRightClose.png").getImage();
        public Image diggerGoLeftOpen = new ImageIcon("DiggerGoLeftOpen.png").getImage();
        public Image diggerGoLeftClose = new ImageIcon("DiggerGoLeftClose.png").getImage();
        public Image diggerGoUpOpen = new ImageIcon("DiggerGoUpOpen.png").getImage();
        public Image diggerGoUpClose = new ImageIcon("DiggerGoUpClose.png").getImage();
        public Image diggerGoDownOpen = new ImageIcon("DiggerGoDownOpen.png").getImage();
        public Image diggerGoDownClose = new ImageIcon("DiggerGoDownClose.png").getImage();

        int x = 100;
        int y = 100;
        int dX = 1;
        int dY = 1;

        public void aObjectMove (JPanel playGround, int dX, int dY)
        {
           x += dX;
           y += dY;
        }
    }
    
    class ControlButton extends JButton
    {
        ControlButton(String name)
        {
            super(name);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    if (((JButton)e.getSource()).getText() == "Up") diggerObject.aObjectMove(aPanel, 0, -1);
                    if (((JButton)e.getSource()).getText() == "Down") diggerObject.aObjectMove(aPanel, 0, 1);
                    if (((JButton)e.getSource()).getText() == "Left") diggerObject.aObjectMove(aPanel, -1, 0);
                    if (((JButton)e.getSource()).getText() == "Right") diggerObject.aObjectMove(aPanel, 1, 0);
                    if (((JButton)e.getSource()).getText() == "Start") aPanel.startGame();
                    if (((JButton)e.getSource()).getText() == "Stop") aPanel.endGame();
                    
                }
            });
            this.addKeyListener(new KeyAdapter() 
            {
                @Override
                public void keyPressed(KeyEvent e) 
                {
                    if (e.getKeyCode() == KeyEvent.VK_UP) 
                    {
                        diggerDirection = "Up"; 
                        diggerObject.aObjectMove(aPanel, 0, -10);
                }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) 
                    {
                        diggerDirection = "Down"; 
                        diggerObject.aObjectMove(aPanel, 0, 10);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) 
                    {
                        diggerDirection = "Left"; 
                        diggerObject.aObjectMove(aPanel, -10, 0);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
                    {
                        diggerDirection = "Right"; 
                        diggerObject.aObjectMove(aPanel, 10, 0);
                    }
                }
            });
        }
        
        public String getDirection()
        {
            return diggerDirection;
        }
    }
    
    public static void main(String[] args) 
    {
        new Main().setVisible(true);
    }
    
}





