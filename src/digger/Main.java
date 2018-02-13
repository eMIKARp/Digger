package digger;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.HashMap;

public class Main extends JFrame
{
    public final static int CUBIT = 30; // primary dimension
    public final static Color BROWN = new Color(139, 69, 19);
    
    private double sWidth = this.getToolkit().getScreenSize().getWidth();
    private double sHeight = this.getToolkit().getScreenSize().getHeight();
    
    private JPanel sPanel = new JPanel();
    private GamePanel gPanel = new GamePanel();
    private JPanel lBorderPanel = new JPanel();
    private JPanel rBorderPanel = new JPanel();
    private JPanel cPanel = new JPanel();
    
    private JLabel sLabel = new JLabel("Score:   ");
    private JLabel cScore = new JLabel("# yourScore");
    private JLabel lLabel = new JLabel("Lifes:   ");
    private JLabel cLifes = new JLabel("# yourLifes");
    private JLabel signature = new JLabel("by Emil Karpowicz Feb 2018");
   
    public ControlButton sButton = new ControlButton("Start");
    public ControlButton pButton = new ControlButton("Stop");
    
    private Digger digger = new Digger();
    public Image diggerImage;
    private Predator predator = new Predator();
    public Image predatorImage;
    
    public final static int n = 43;
    public final static int m = 21;
    public MazeMatrix mMatrix = new MazeMatrix(n,m);
    
    Main()
    {
        
        this.setTitle("The Digger 1983 Tribute");
        this.setBounds(((int)sWidth - 44 * CUBIT)/2, ((int)sHeight - 24 * CUBIT)/2, 44 * CUBIT, 24 * CUBIT);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        this.getContentPane().add(sPanel, BorderLayout.NORTH);
            sPanel.setBackground(Color.GRAY);
            sPanel.setBorder(BorderFactory.createBevelBorder(0));
            sPanel.setPreferredSize(new Dimension(44 * CUBIT, CUBIT));
            sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.LINE_AXIS));
            sPanel.add(Box.createRigidArea(new Dimension(30,0)));
            sPanel.add(sLabel).setForeground(Color.WHITE);
            sPanel.add(cScore).setForeground(Color.WHITE);
            sPanel.add(Box.createRigidArea(new Dimension(50,0)));
            sPanel.add(lLabel).setForeground(Color.WHITE);
            sPanel.add(cLifes).setForeground(Color.WHITE);
            sPanel.add(Box.createHorizontalGlue());
        this.getContentPane().add(rBorderPanel, BorderLayout.EAST);
            rBorderPanel.setBackground(Color.GRAY);
            rBorderPanel.setPreferredSize(new Dimension(CUBIT/2, 24 * CUBIT));
            rBorderPanel.setBorder(BorderFactory.createBevelBorder(0));
        this.getContentPane().add(lBorderPanel, BorderLayout.WEST);
            lBorderPanel.setBackground(Color.GRAY);
            lBorderPanel.setPreferredSize(new Dimension(CUBIT/2, 24 * CUBIT));
            lBorderPanel.setBorder(BorderFactory.createBevelBorder(0));
        this.getContentPane().add(gPanel, BorderLayout.CENTER);
            gPanel.setBackground(Color.GRAY);
            gPanel.setBorder(BorderFactory.createBevelBorder(2));
        this.getContentPane().add(cPanel, BorderLayout.SOUTH);
            cPanel.setBackground(Color.GRAY);
            cPanel.setBorder(BorderFactory.createBevelBorder(0));
            cPanel.setPreferredSize(new Dimension(44 * CUBIT, CUBIT));
            cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.LINE_AXIS));
            cPanel.add(Box.createRigidArea(new Dimension(30, 0)));
            cPanel.add(sButton).setPreferredSize(new Dimension(100,0));
            cPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            cPanel.add(pButton).setPreferredSize(new Dimension(100,0));
            cPanel.add(Box.createHorizontalGlue());
            cPanel.add(signature).setForeground(Color.WHITE);
            cPanel.add(Box.createRigidArea(new Dimension(25, 0)));
    }
    
     
    public void startGame()
    {
        
        ThreadGroup allThreads = new ThreadGroup("All Thrreads");
        Thread dThread = new Thread(allThreads, new DiggerRunnable());
        Thread pThread = new Thread(allThreads, new PredatorRunnable());
        dThread.start();
        pThread.start();
    }
    
    public void stopGame()
    {
        System.out.println("Koniec gry!");
        System.out.println(gPanel.getWidth()/30);
        System.out.println(gPanel.getHeight()/30);
    }
    
   
    
    public static void main(String[] args) 
    {
        new Main().setVisible(true);
    }


class GamePanel extends JPanel
    {
        @Override
        public void paintComponent(Graphics g)    
        {
            super.paintComponent(g);
            
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                {
                    g.setColor(mMatrix.matrix[i][j].getMazeCellColor());
                    g.fillRect(mMatrix.matrix[i][j].getMazeCellXCoordinate(),mMatrix.matrix[i][j].getMazeCellYCoordinate(),mMatrix.matrix[i][j].getMazeCellHeight(),mMatrix.matrix[i][j].getMazeCellWidth());
                }            
                g.drawImage(diggerImage, digger.getDiggerX(), digger.getDiggerY(), digger.getDiggerWidth(), digger.getDiggerHeight(), null);
                g.drawImage(predatorImage, predator.getPredatorX(), predator.getPredatorY(), predator.getPredatorWidth(), predator.getPredatorHeight(), null);
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
                        if (((JButton)(e.getSource())).getText() == "Start") startGame(); 
                        if (((JButton)(e.getSource())).getText() == "Stop") stopGame();
                    }
                });
                
                this.addKeyListener(new KeyAdapter() 
                    {
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_UP) 
                            {
                                digger.dMove(0, -30, gPanel);
                                digger.diggerDirection = "Up";
                                digger.dDigTunel("Up");
                            }
                            else if (e.getKeyCode() == KeyEvent.VK_DOWN) 
                            {
                                digger.dMove(0, 30, gPanel);
                                digger.diggerDirection = "Down";
                                digger.dDigTunel("Down");
                            }
                            else if (e.getKeyCode() == KeyEvent.VK_LEFT) 
                            {
                                digger.dMove(-30, 0, gPanel);
                                digger.diggerDirection = "Left";
                                digger.dDigTunel("Left");
                            }
                            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
                            {
                                digger.dMove(30, 0, gPanel);
                                digger.diggerDirection = "Right";
                                digger.dDigTunel("Right");
                            }
                        }
                    });
            }
    }

class MazeCell
{
    public Color slotBackColor;
    public int slotXCoordinate;
    public int slotYCoordinate;
    public int slotWidth;
    public int slotHeight;
    public boolean isPenetrable;
   
    MazeCell (Color slotBackColor, int slotXCoordinate, int slotYCoordinate, int slotWidth, int slotHeight, boolean isPenetrable)
    {
        this.slotBackColor = slotBackColor;
        this.slotXCoordinate = slotXCoordinate;
        this.slotYCoordinate = slotYCoordinate;
        this.slotHeight = slotHeight;
        this.slotWidth = slotWidth;
        this.isPenetrable = isPenetrable;
    }

    public Color getMazeCellColor()
    {
        return slotBackColor;
    }
    
    public int getMazeCellXCoordinate()
    {
        return slotXCoordinate;
    }
    
    public int getMazeCellYCoordinate()
    {
        return slotYCoordinate;
    }
    
    public int getMazeCellWidth()
    {
        return slotWidth;
    }
    
    public int getMazeCellHeight()
    {
        return slotHeight;
    }
    
    public boolean isMazeCellPenetrable()
    {
        return isPenetrable;
    }
    
    
    public void setMazeCellColor(Color color)
    {
        slotBackColor = color;
    }
    
    public void setMazeCellPenetrablity(Boolean isPenetrable)
    {
        isPenetrable = isPenetrable;
    }
    
}

class MazeMatrix
{
    MazeCell[][] matrix;
    
    MazeMatrix(int n, int m)
    {
        matrix = new MazeCell[n][m];
        
        for (int i = 0; i < n; i++)         // Create maze matrix
        {
           for (int j = 0; j < m; j++)
           {
               matrix[i][j] = new MazeCell(Color.BLACK, i * 30, j * 30, 30, 30, true); 
           }
        }
        
        for (int i = 35; i < 42; i++)         // Create initial path
        {
           matrix[i][2].setMazeCellColor(BROWN);
        }
        
        for (int i = 2; i < 20; i++)        
        {
           matrix[35][i].setMazeCellColor(BROWN);
        }
        
        for (int i = 15; i < 35; i++)         
        {
           matrix[i][19].setMazeCellColor(BROWN);
        }
        
        for (int i = 12; i < 20; i++)        
        {
           matrix[15][i].setMazeCellColor(BROWN);
        }
        
        for (int i = 8; i < 15; i++)         
        {
           matrix[i][12].setMazeCellColor(BROWN);
        }
        
        for (int i = 2; i < 12; i++)        
        {
           matrix[8][i].setMazeCellColor(BROWN);
        }
        
        for (int i = 2; i < 8; i++)         
        {
           matrix[i][2].setMazeCellColor(BROWN);
        }
        
         System.out.println("Macież o wymiarach: " + n +" x " + m + " została utowrzona!");
    }
}

class Predator
{
    private String predatorDirection = "west";
    private boolean isNextStepAvailable = true;
    
    private Image pFirstImage = new ImageIcon("PredatorFirstImage.png").getImage();
    private Image pSecondImage = new ImageIcon("PredatorSecondImage.png").getImage();
    
    private int x = 41 * CUBIT;
    private int y = 2 * CUBIT;
    
    private int pWidth = CUBIT;
    private int pHeight = CUBIT;
    
    private int cIndex;
    private int rIndex;
    
    public HashMap possibleWays = new HashMap<String, Double>();
    private String shortestWay;
    private double distance;
    
    public int getPredatorX()
    {
        return x;
    }
    
    public int getPredatorY()
    {
        return y;
    }
    
    public int getPredatorWidth()
    {
        return pWidth;
    }
    
    public int getPredatorHeight()
    {
        return pHeight;
    }
    
    public Image getPredatorFirstImage()
    {
        return pFirstImage;
    }
    
    public Image getPredatorSecondImage()
    {
        return pSecondImage;
    }
    
    public boolean isNextStepAvailable(int cIndex, int rIndex, String predatorDirection)
    {
        if (predatorDirection == "west")
            if (cIndex - 1 >= 0)
                if ((mMatrix.matrix[cIndex - 1][rIndex].getMazeCellColor()).equals(BROWN) == true) return true; else return false;
            else
                return false;
        else if (predatorDirection == "north")
            if (rIndex - 1 >= 0)
                if ((mMatrix.matrix[cIndex][rIndex-1].getMazeCellColor()).equals(BROWN) == true) return true; else return false;
            else
                return false;
        else if (predatorDirection == "south")
            if (rIndex + 1 <= 21)
                    if ((mMatrix.matrix[cIndex][rIndex+1].getMazeCellColor()).equals(BROWN) == true) return true; else return false;
                else
                    return false;
        else if (predatorDirection == "east")
            if (cIndex + 1 <= 43)
                if ((mMatrix.matrix[cIndex + 1][rIndex].getMazeCellColor()).equals(BROWN) == true) return true; else return false;
            else
                return false;
        else return false;
    }
    
    public String whichWayToGo(int cIndex, int rIndex)
    {
        possibleWays.put("west", (x - 30 - digger.getDiggerX())^2 + (y - digger.getDiggerY())^2);
        possibleWays.put("north", (x - digger.getDiggerX())^2 + (y - 30 - digger.getDiggerY())^2);
        possibleWays.put("south",(x - digger.getDiggerX())^2 + (y + 30 - digger.getDiggerY())^2);
        possibleWays.put("east",(x + 30 -digger.getDiggerX())^2 + (y - digger.getDiggerY())^2);
        
        if (isNextStepAvailable(cIndex, rIndex, "west")) shortestWay = "west";
        
        if (isNextStepAvailable(cIndex, rIndex, "north"))
        {
            if (shortestWay == "west")
                if ((int)possibleWays.get("west") <= (int)possibleWays.get("north")) shortestWay = "west";
                else shortestWay = "north";
        else shortestWay = "north";    
        }
        if (isNextStepAvailable(cIndex, rIndex, "south"))
            if (shortestWay == "west")
                if ((int)possibleWays.get("west") <= (int)possibleWays.get("south")) shortestWay = "west";
                else shortestWay = "south";
            else if (shortestWay == "north")
                if ((int)possibleWays.get("north") <= (int)possibleWays.get("south")) shortestWay = "north";
                else shortestWay = "south";
        else shortestWay = "south";      
        
        if (isNextStepAvailable(cIndex, rIndex, "east")) 
            if (shortestWay == "west")
                if ((int)possibleWays.get("west") <= (int)possibleWays.get("east")) shortestWay = "west";
                else shortestWay = "east";
            else if (shortestWay == "north")
                if ((int)possibleWays.get("north") <= (int)possibleWays.get("east")) shortestWay = "north";
                else shortestWay = "east";
            else if (shortestWay == "south")
                if ((int)possibleWays.get("south") <= (int)possibleWays.get("east")) shortestWay = "south";
                else shortestWay = "east";
        else shortestWay = "east"; 
        
        System.out.println(shortestWay);
        return shortestWay;
    }
    
    public void pMove(GamePanel gPanel)
    {
        cIndex = (x + pWidth / 2) / CUBIT;
        rIndex = (y + pHeight / 2) / CUBIT;
        
        boolean west = (mMatrix.matrix[cIndex - 1][rIndex].getMazeCellColor()).equals(BROWN);
        boolean north = (mMatrix.matrix[cIndex][rIndex - 1].getMazeCellColor()).equals(BROWN);
        boolean south = (mMatrix.matrix[cIndex][rIndex + 1].getMazeCellColor()).equals(BROWN);
        boolean east = (mMatrix.matrix[cIndex + 1][rIndex].getMazeCellColor()).equals(BROWN);

        if (isNextStepAvailable(cIndex, rIndex, predatorDirection))
            {
                System.out.println("Droga wolna");   
               
                if (predatorDirection == "west") x-=30;
                else if (predatorDirection == "north") y-=30;
                else if (predatorDirection == "south") y+=30;
                else if (predatorDirection == "east") x+=30;
            }
        else
            {
                System.out.println("Musisz wybrać nowy kierunek");
                predatorDirection = whichWayToGo(cIndex, rIndex);
                
//                if ((x > digger.getDiggerX()) && (y > digger.getDiggerY()))
//                {
//                    if (west == true) predatorDirection = "west";
//                    else if (north == true) predatorDirection = "north";
//                    else if (south == true) predatorDirection = "south";
//                    else if (east == true) predatorDirection = "east";
//                }
//                
//                else if ((x > digger.getDiggerX()) && (y < digger.getDiggerY()))
//                {
//                    if (west == true) predatorDirection = "west";
//                    else if (south == true) predatorDirection = "south";
//                    else if (north == true) predatorDirection = "north";
//                    else if (east == true) predatorDirection = "east";
//                }
//                
//                else if ((x > digger.getDiggerX()) && (y == digger.getDiggerY()))
//                {
//                    if (west == true) predatorDirection = "west";
//                    else if (north == true) predatorDirection = "north";
//                    else if (south == true) predatorDirection = "south";
//                    else if (east == true) predatorDirection = "east";
//                }
//                
//                if ((x == digger.getDiggerX()) && (y > digger.getDiggerY()))
//                {
//                    if (north == true) predatorDirection = "north";
//                    else if (west == true) predatorDirection = "west";
//                    else if (east == true) predatorDirection = "east";
//                    else if (south == true) predatorDirection = "south";
//                }
//                
//                else if ((x == digger.getDiggerX()) && (y < digger.getDiggerY()))
//                {
//                    if (south == true) predatorDirection = "south";
//                    else if (west == true) predatorDirection = "west";
//                    else if (east == true) predatorDirection = "east";
//                    else if (north == true) predatorDirection = "north";
//                }
//                
//                else if ((x == digger.getDiggerX()) && (y == digger.getDiggerY()))
//                {
//                    pWins();
//                }
//                
//                else if ((x < digger.getDiggerX()) && (y > digger.getDiggerY()))
//                {
//                    if (east == true) predatorDirection = "east";
//                    else if (north == true) predatorDirection = "north";
//                    else if (south == true) predatorDirection = "south";
//                    else if (west == true) predatorDirection = "west";
//                }
//                else if ((x < digger.getDiggerX()) && (y < digger.getDiggerY()))
//                {
//                    if (east == true) predatorDirection = "east";
//                    else if (south == true) predatorDirection = "south";
//                    else if (north == true) predatorDirection = "north";
//                    else if (west == true) predatorDirection = "west";
//                }
//                else if ((x < digger.getDiggerX()) && (y == digger.getDiggerY()))
//                {
//                    if (east == true) predatorDirection = "east";
//                    else if (north == true) predatorDirection = "north";
//                    else if (south == true) predatorDirection = "south";
//                    else if (west == true) predatorDirection = "west";
//                }
//                
            }
    }
    
    
    public void pWins()
    {
        System.out.println("Straciłeś życie!");
    }
}

class PredatorRunnable implements Runnable
{
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                predator.pMove(gPanel);
                Thread.sleep(5);
                predatorImage = predator.getPredatorFirstImage();
                gPanel.repaint();
                Thread.sleep(5);
                predatorImage = predator.getPredatorSecondImage();
                gPanel.repaint();
                Thread.sleep(5);
                }
                catch (InterruptedException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        }
}

class Digger 
    {
        private String diggerDirection;
        
        private Image dGoDownClose = new ImageIcon("DiggerGoDownClose.png").getImage();
        private Image dGoDownOpen = new ImageIcon("DiggerGoDownOpen.png").getImage();
        private Image dGoUpClose = new ImageIcon("DiggerGoUpClose.png").getImage();
        private Image dGoUpOpen = new ImageIcon("DiggerGoUpOpen.png").getImage();
        private Image dGoLeftClose = new ImageIcon("DiggerGoLeftClose.png").getImage();
        private Image dGoLeftOpen = new ImageIcon("DiggerGoLeftOpen.png").getImage();
        private Image dGoRightClose = new ImageIcon("DiggerGoRightClose.png").getImage();
        private Image dGoRightOpen = new ImageIcon("DiggerGoRightOpen.png").getImage();

        private int x = 2 * CUBIT;
        private int y = 2 * CUBIT;

        private int dWidth = CUBIT;
        private int dHeight = CUBIT;
        
        public int getDiggerX()
        {
            return x;
        }

        public int getDiggerY()
        {
            return y;
        }

        public int getDiggerWidth()
        {
            return dWidth;
        }

        public int getDiggerHeight()
        {
            return dHeight;
        }

        public String getDiggerDirection()
        {
            return diggerDirection;
        }

        public Image getDiggerOpenImage(String diggerDirection)
        {
            if (diggerDirection == "Up") return dGoUpOpen;
            else if (diggerDirection == "Down") return dGoDownOpen;
            else if (diggerDirection == "Left") return dGoLeftOpen;
            else if (diggerDirection == "Right") return dGoRightOpen;
            else return dGoRightOpen;
        }

        public Image getDiggerCloseImage(String diggerDirection)
        {
            if (diggerDirection == "Up") return dGoUpClose;
            else if (diggerDirection == "Down") return dGoDownClose;
            else if (diggerDirection == "Left") return dGoLeftClose;
            else if (diggerDirection == "Right") return dGoRightClose;
            else return dGoRightClose;
        }

        public void dMove (int dX, int dY, GamePanel gPanel)
        {
            if ((x + dWidth + dX) >= gPanel.getWidth()) x = gPanel.getWidth() - dWidth;
            else if ((x + dX) <= 0) x = 0; 
            else x += dX;
            
            if ((y + dY) <= 0) y = 0; 
            else if ((y + dHeight + dY) >= gPanel.getHeight()) y = gPanel.getHeight() - dHeight; 
            else y += dY;
        }
        
        public void dDigTunel (String diggerDirection)
        {
            if (diggerDirection == "Up") mMatrix.matrix[(x+dWidth/2)/CUBIT][(y+dHeight/2)/CUBIT].setMazeCellColor(BROWN);
            else if (diggerDirection == "Down") mMatrix.matrix[(x+dWidth/2)/CUBIT][(y+dHeight/2)/CUBIT].setMazeCellColor(BROWN);
            else if (diggerDirection == "Left") mMatrix.matrix[(x+dWidth/2)/CUBIT][(y+dHeight/2)/CUBIT].setMazeCellColor(BROWN);
            else if (diggerDirection == "Right") mMatrix.matrix[(x+dWidth/2)/CUBIT][(y+dHeight/2)/CUBIT].setMazeCellColor(BROWN);
        }
    }

class DiggerRunnable implements Runnable
    {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                diggerImage = digger.getDiggerOpenImage(digger.getDiggerDirection());
                gPanel.repaint();
                Thread.sleep(50);
                diggerImage = digger.getDiggerCloseImage(digger.getDiggerDirection());
                gPanel.repaint();
                Thread.sleep(50);
                }  
                catch (InterruptedException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
        }

    }

}