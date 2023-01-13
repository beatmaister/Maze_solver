package maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new MainPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class MainPane extends JPanel {

        private JLabel message;
        private JTextField mazeSize;
        private JButton generateButton;
        private JTextField start;
        private JTextField stop;
        private JButton solveButton;
        private JButton setButton;
        private JButton randButton;

        private MazePane mazePane;

        public MainPane() {
            setLayout(new BorderLayout());

            mazePane = new MazePane();
            mazePane.setLayout(new GridBagLayout());

            add(mazePane);
            setFont(new Font("Helvetica", Font.PLAIN, 45));

            JPanel inputPane = new JPanel(new GridBagLayout());
            inputPane.setBackground(new Color(171,221,253));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(4, 4, 4, 4);

            message = new JLabel("Make your selections");
            message.setFont(new Font("Helvetica", Font.PLAIN, 20));
            inputPane.add(message, gbc);

            gbc.gridy++;
            mazeSize = new JTextField("Size");
            mazeSize.setBackground(new Color(226,236,237));
            inputPane.add(mazeSize, gbc);

            gbc.gridx++;
            generateButton = new JButton("Generate Maze");
            generateButton.setBackground(new Color(186,224,229));
            inputPane.add(generateButton, gbc);
            generateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generateMaze();
                }
            });

            gbc.gridx = 0;
            gbc.gridy++;
            start = new JTextField("Start(,)");
            start.setBackground(new Color(226,236,237));
            inputPane.add(start, gbc);

            gbc.gridy++;
            stop = new JTextField("Stop(,)");
            stop.setBackground(new Color(226,236,237));
            inputPane.add(stop, gbc);

            gbc.gridx++;
            gbc.gridy--;
            setButton = new JButton("Set targets!");
            setButton.setBackground(new Color(186,224,229));
            inputPane.add(setButton, gbc);
            setButton.addActionListener( new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    setEnds();
                }
            });

            gbc.gridy++;
            solveButton = new JButton("Solve!");
            solveButton.setBackground(new Color(142,210,136));
            inputPane.add(solveButton, gbc);
            solveButton.addActionListener( new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    findPath();
                }
            });
            gbc.gridx++;
            gbc.gridy-=2;
            randButton = new JButton("Random Targets");
            randButton.setBackground(new Color(186,224,229));
            inputPane.add(randButton, gbc);
            randButton.addActionListener( new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    randEnds();
                }
            });

            add(inputPane, BorderLayout.SOUTH);
        }

        protected void generateMaze() {
            String size = mazeSize.getText();
            try {
                int sized = Integer.parseInt(size);
                if (sized > 301) {
                    message.setText("Please choose a smaller width");
                } else if (sized <= 0) {
                    message.setText("Please enter valid numbers");
                } else {
                    mazePane.setMaze(new Generate(sized, sized).maze);
                    mazePane.path = new ArrayList<Integer>();
                    mazePane.start=null;
                    mazePane.end=null;
                    mazePane.current = true;
                }
                revalidate();
                repaint();
            } catch (NumberFormatException e) {
                message.setText("Please enter valid numbers");
            }
        }
        
        protected void setEnds(){
            String strt = start.getText();
            String stp = stop.getText();
            String[] strt3 = strt.split(",");         
            String[] stp3 = stp.split(",");
            if(mazePane.current==false){
                message.setText("first generate a maze.");
                return;
            }
            if(strt3.length>2 || strt3.length<2){
                message.setText("Enter a valid x,y coordinate");
                return;
            }else {
                try{
                    int strtx = Integer.parseInt(strt3[0]);
                    int strty = Integer.parseInt(strt3[1]);
                    int[] strtCoord = {strtx,strty};
                    int stpx = Integer.parseInt(stp3[0]);
                    int stpy = Integer.parseInt(stp3[1]);
                    int[] stpCoord = {stpx,stpy};
                    if(mazePane.maze[strtCoord[0]][strtCoord[1]] == 1){
                        message.setText("Must start on a white block.");
                        return;
                    }if(mazePane.maze[stpCoord[0]][stpCoord[1]] == 1){
                        message.setText("Must end on a white block.");
                        return;
                    }if(strtCoord[0]<0 || strtCoord[0]>=mazePane.maze.length || strtCoord[1]<0 || strtCoord[1]>=mazePane.maze.length){
                        message.setText("Please select from within bounds");
                        return;
                    }if(stpCoord[0]<0 || stpCoord[0]>=mazePane.maze.length || stpCoord[1]<0 || stpCoord[1]>=mazePane.maze.length){
                        message.setText("Please select from within bounds");
                        return;
                    }
                    mazePane.path = new ArrayList<Integer>();
                    mazePane.setCoords(strtCoord, stpCoord);
                    revalidate();
                    repaint();

                }catch(NumberFormatException e){
                    message.setText("Enter a valid x,y coordinate");
                    return;
                }

            }
        }
        
        protected void randEnds(){
            mazePane.path = new ArrayList<Integer>();
            if(mazePane.current==false){
                message.setText("first generate a maze.");
                return;
            }
            int[] strt = new int[2];
            int[] stp = new int[2];

            boolean pass = false;
            while(!pass){
                pass = true;
                Random random = new Random();
                int stx = random.nextInt(mazePane.maze.length);
                int spx = random.nextInt(mazePane.maze.length);
                int sty = random.nextInt(mazePane.maze.length);
                int spy = random.nextInt(mazePane.maze.length);
                strt[0] = stx; strt[1] = sty;
                stp[0] = spx; stp[1] = spy;
                if(mazePane.maze[strt[1]][strt[0]] == 1){
                    pass=false;
                }if(mazePane.maze[stp[1]][stp[0]] == 1){
                    pass=false;
                }
                if(stp[0]<0 || stp[0]>=mazePane.maze[0].length || stp[1]<0 || stp[1]>=mazePane.maze.length){
                    pass=false;
                }if(strt[0]<0 || strt[0]>=mazePane.maze[0].length || strt[1]<0 || strt[1]>=mazePane.maze.length){
                    pass=false;
                }

            }
            
            mazePane.setCoords(strt, stp);
            revalidate();
            repaint();
        }

        protected void findPath(){
            if(mazePane.current==false){
                message.setText("First generate a maze.");
                return;
            }if(mazePane.start == null){
                message.setText("First select a start and end point");
                return;
            }if(mazePane.path.size()>0){
                message.setText("Generate a new maze");
                return;
            }
            mazePane.path = new ArrayList<Integer>();
            DFS.searchPath(mazePane.maze, mazePane.start[0], mazePane.start[1], mazePane.path);
            revalidate();
            repaint();
            

        }
    }

    public class MazePane extends JPanel {

        private int[][] maze = {{}};
        private int[] start;
        private int[] end;
        private boolean current=false;
        private ArrayList<Integer> path = new ArrayList<Integer>();

        public MazePane() {
            setBackground(new Color(201,190,171));
        }

        public void setMaze(int[][] maze) {
            this.maze = maze;
            repaint();
        }
        public void setCoords(int[] start, int[] stop){
            if(this.start==null || this.end==null){
                this.start = start;
                this.end = stop;
            }if(this.start!=null && this.end!=null){
                this.maze[this.start[1]][this.start[0]] = 0;
                this.maze[this.end[1]][this.end[0]] = 0;
            }
            this.start = start;
            this.end = stop;
            this.maze[this.start[1]][this.start[0]] = 3;
            this.maze[this.end[1]][this.end[0]] = 9;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 900);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(50, 50);
            int scale = 7;
            if( maze.length<14) scale = 70;
            if( maze.length>=14 && maze.length<19) scale = 50;
            if( maze.length>=19 && maze.length<31) scale = 30;
            if( maze.length>=31 && maze.length<49) scale = 20;
            if( maze.length>=49 && maze.length<64) scale = 15;
            if( maze.length>=64 && maze.length<79) scale = 12;
            if( maze.length>=79 && maze.length<94) scale = 10;
            if( maze.length>=94 && maze.length<135) scale = 7;
            if( maze.length>=134 && maze.length<158) scale = 6;
            if( maze.length>=158) scale = 5;

            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.BLACK;
                            break;
                        case 3:
                            color = Color.GREEN;
                            break;
                        case 9:
                            color = Color.RED;
                            break;
                        default:
                            color = Color.WHITE;
                    }
                    g2d.setColor(color);
                    g2d.fillRect(scale * col, scale * row, scale, scale);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(scale * col, scale * row, scale, scale);
                }
            }
            for (int p = 0; p < path.size(); p += 2) {
                int pathX = path.get(p);
                int pathY = path.get(p + 1);
                g2d.setColor(Color.GREEN);
                g2d.fillRect(pathX * scale, pathY * scale, scale, scale);
            }
            g2d.dispose();
        }

    }
}
