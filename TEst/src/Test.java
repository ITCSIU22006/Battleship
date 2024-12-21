import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Test extends JFrame{
    public static int Row = 8;
    public static int Column = 8;
    public static int numPlayerShips = 3;
    public static int NumComputerShips = 3;
    public static int numPlayerRemain = 5;
    public static int NumComputerRemain = 5;
    public static String[][] gridPLayer = new String[Row][Column];
    public static String[][] gridComputer = new String[Row][Column];
    public static boolean correctGuesses = false;
    public static int tem = 0 ;
    public static int x=0, y=0;
    public static int[][] justarandomnamebecauseidontknowwhattonamethisvaraiable = new int[2][8];
    public static Queue<int[]> playership1 = new LinkedList<>();
    public static Queue<int[]> playership2 = new LinkedList<>();
    public static Queue<int[]> playership3 = new LinkedList<>();
    public static int playerdeploycount = 0;

    public static Queue<int[]> comship1 = new LinkedList<>();
    public static Queue<int[]> comship2 = new LinkedList<>();
    public static Queue<int[]> comship3 = new LinkedList<>();

    private static JTextField txtX, txtY;
    private static JTextArea gameInfo;
    private static JButton btnDeploy, btnGuess;
    private static JComboBox<String> orientationComboBox;
    private static JPanel playerPanel, computerPanel;

    public Test() {
        setTitle("Battleship Game");
        setLayout(new BorderLayout());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        initializeGrid(gridPLayer);
        initializeGrid(gridComputer);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setBackground(new Color(200, 200, 255)); // Light gray background


        inputPanel.add(new JLabel("Enter X:"));
        txtX = new JTextField(3);
        inputPanel.add(txtX);

        inputPanel.add(new JLabel("Enter Y:"));
        txtY = new JTextField(3);
        inputPanel.add(txtY);

        inputPanel.add(new JLabel("Orientation:"));
        String[] orientations = {"horizontal", "vertical", "cross"};
        orientationComboBox = new JComboBox<>(orientations);
        inputPanel.add(orientationComboBox);

        btnDeploy = new JButton("Deploy Ship");
        btnDeploy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deployPlayerShips();
            }
        });
        inputPanel.add(btnDeploy);


        btnGuess = new JButton("Attack!!!");
        btnGuess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerTurn();
                computerTurn();
            }
        });
        inputPanel.add(btnGuess);
        btnGuess.setEnabled(false);


        gameInfo = new JTextArea(5, 40);
        gameInfo.setEditable(false);
        Font font = new Font("Dialog", Font.BOLD + Font.ITALIC, 14);
        gameInfo.setFont(font);
        gameInfo.setText("Game Started\n");
        gameInfo.setBackground(new Color(200, 200, 255)); // Light gray background
        JScrollPane infoScroll = new JScrollPane(gameInfo);
        add(inputPanel, BorderLayout.NORTH);
        add(infoScroll, BorderLayout.SOUTH);

        playerPanel = new JPanel(new GridLayout(Row, Column));
        playerPanel.setPreferredSize(new Dimension(300, 300));
        playerPanel.setBackground(new Color(200, 200, 255)); // Light gray background
        updateGridDisplay(playerPanel, gridPLayer);

        computerPanel = new JPanel(new GridLayout(Row, Column));
        computerPanel.setPreferredSize(new Dimension(300, 300));
        computerPanel.setBackground(new Color(200, 200, 255)); // Light gray background

        updateGridDisplay(computerPanel, gridComputer);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 2));
        gridPanel.add(playerPanel);
        gridPanel.add(computerPanel);
        add(gridPanel, BorderLayout.CENTER);

        setVisible(true);
        btnGuess.setVisible(false);
        deployComputerShips();
    }

    private void initializeGrid(String[][] grid) {
        for (int i = 0; i < Row; i++) {
            for (int j = 0; j < Column; j++) {
                grid[i][j] = " ";
            }
        }
    }

    private static void updatecomputerCell(int x, int y, String value) {
        gridComputer[x][y] = value;
        JLabel cellLabel = (JLabel) computerPanel.getComponent(x * Column + y);
        cellLabel.setText(value);
    }

    private static void updateplayerCell(int x, int y, String value) {
        gridPLayer[x][y] = value;
        JLabel cellLabel = (JLabel) playerPanel.getComponent(x * Column + y);
        cellLabel.setText(value);
    }

    private static void updateGridDisplay(JPanel panel, String[][] grid) {
        panel.removeAll();
        for (int i = 0; i < Row; i++) {
            for (int j = 0; j < Column; j++) {
                JLabel cellLabel = new JLabel(grid[i][j]);
                cellLabel.setPreferredSize(new Dimension(30, 30));
                cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(cellLabel);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void deployPlayerShips(){
        String orientation = (String) orientationComboBox.getSelectedItem();
        int x = Integer.parseInt(txtX.getText()) - 1;
        int y = Integer.parseInt(txtY.getText()) - 1;
        playerdeploycount++;
        if(playerdeploycount==1){
            ship1(x, y, gridPLayer);
        }
        if(playerdeploycount==2){
            if(orientation.equals("horizontal")){
                playership2.add(new int[]{x,y});
                playership2.add(new int[]{x,y+1});
            } else if (orientation.equals("vertical")) {
                playership2.add(new int[]{x,y});
                playership2.add(new int[]{x+1,y});
            } else {
                playership2.add(new int[]{x,y});
                playership2.add(new int[]{x+1,y+1});
            }
        } else if (playerdeploycount==3) {
            if(orientation.equals("horizontal")){
                playership3.add(new int[]{x,y});
                playership3.add(new int[]{x,y+1});
            } else if (orientation.equals("vertical")) {
                playership3.add(new int[]{x,y});
                playership3.add(new int[]{x+1,y});
            } else {
                playership3.add(new int[]{x, y});
                playership3.add(new int[]{x + 1, y + 1});
            }
        }


        if((x >= 0 && x < Row) && (y >= 0 && y < Column) && (gridPLayer[x][y].equals(" ")))
        {
            if (playerdeploycount <= 1) {
                ship1(x, y, gridPLayer);
            } else {
                ship2(x, y, orientation, gridPLayer);
            }
        }
        else if((x > 0 && x <= Row) && (y > 0 && y <= Column) && gridPLayer[x][y].equals("O"))
            JOptionPane.showMessageDialog(null,
                    "You can't place two ship in same location",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        else if((x < 0 || x > Row) || (y < 0 || y > Column)){
            JOptionPane.showMessageDialog(null,
                    "You can't place ships outside the " + Row + " by " + Column + " grid",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);}
        updateGridDisplay(playerPanel, gridPLayer);
        if (playerdeploycount == numPlayerShips) {
            btnDeploy.setEnabled(false);
            btnDeploy.setVisible(false);
            orientationComboBox.setEnabled(false);
            btnGuess.setEnabled(true);
            btnGuess.setVisible(true);
            gameInfo.append("All ships deployed! Now it's time to attack.\n");
        }
    }

    public static void deployComputerShips(){
        Test.NumComputerShips = 3;
        for (int i = 1; i <= Test.NumComputerShips; ) {
            int x = (int)(Math.random() * (Row -1) + 1);
            int y = (int)(Math.random() * (Column -1) + 1);
            String orientation = Math.random() < 0.33 ? "horizontal" : (Math.random() < 0.66 ? "vertical" : "cross");
            if (i <= 1) {
                while(gridPLayer[x][y] == "O") {
                    x = (int)(Math.random() * (Row -1) + 1);
                    y = (int)(Math.random() * (Column -1) + 1);
                    comship1.add(new int[]{x,y});
                }
                ship1(x, y, gridComputer);
            } else if (i == 2) {
                while(gridPLayer[x][y] == "O" || x>6 || y>6
                        || gridPLayer[x][y+1] == "O" || gridPLayer[x+1][y] == "O" || gridPLayer[x+1][y+1] == "O" ) {
                    x = (int)(Math.random() * (Row -1) + 1);
                    y = (int)(Math.random() * (Column -1) + 1);

                    if(orientation.equals("horizontal")){
                        comship2.add(new int[]{x,y});
                        comship2.add(new int[]{x,y+1});
                    } else if (orientation.equals("vertical")) {
                        comship2.add(new int[]{x,y});
                        comship2.add(new int[]{x+1,y});
                    } else {
                        comship2.add(new int[]{x,y});
                        comship2.add(new int[]{x+1,y+1});
                    }
                }
                ship2(x, y, orientation, gridComputer);
            } else if (i == 3) {
                while(gridPLayer[x][y] == "O" || x>6 || y>6
                        || gridPLayer[x][y+1] == "O" || gridPLayer[x+1][y] == "O" || gridPLayer[x+1][y+1] == "O" ) {
                    x = (int)(Math.random() * (Row -1) + 1);
                    y = (int)(Math.random() * (Column -1) + 1);

                    if(orientation.equals("horizontal")){
                        comship3.add(new int[]{x,y});
                        comship3.add(new int[]{x,y+1});
                    } else if (orientation.equals("vertical")) {
                        comship3.add(new int[]{x,y});
                        comship3.add(new int[]{x+1,y});
                    } else {
                        comship3.add(new int[]{x,y});
                        comship3.add(new int[]{x+1,y+1});
                    }
                }
                ship2(x, y, orientation, gridComputer);
            }
            gameInfo.append("Computer ship has been deployed!\n");
            i++;
        }
    }

    public static void whichshipiwashit(Queue<int[]> queues, int[] arrayToFind) {
        Iterator<int[]> iterator = queues.iterator();
        while (iterator.hasNext()) {
            int[] currentArray = iterator.next();
            if (Arrays.equals(currentArray, arrayToFind)) {
                iterator.remove();
                return;
            }
        }
    }

    public static void playerTurn(){
        gameOver();
        int x = Integer.parseInt(txtX.getText()) - 1;
        int y = Integer.parseInt(txtY.getText()) - 1;

        int[] temp=new int[]{x,y};

        if ((x >= 0 && x < Row) && (y >= 0 && y < Column))
        {
            if (gridComputer[x][y] == "O")
            {
                System.out.println("Boom! You hit the ship!");
                gridComputer[x][y] = "X"; //Hit mark
                updatecomputerCell(x, y, "X");
                --Test.NumComputerRemain;
                whichshipiwashit(comship1, temp);
                whichshipiwashit(comship2, temp);
                whichshipiwashit(comship3, temp);

                if(comship1==null||comship2==null||comship3==null){
                    gameInfo.append("One of your ship has been sunked!");
                }
            }
            else if (gridComputer[x][y] == " " ||gridComputer[x][y] == "-") {
                gameInfo.append("Sorry, you missed\n");
                gridComputer[x][y] = "-";
                updatecomputerCell(x, y, "-");
            }
        }
        else {
            JOptionPane.showMessageDialog(null,
                    "The ship must be place in the " + Row + "x" + Column + " grid",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            playerTurn();
        }
    }

    public static void computerTurn() {
        gameOver();
        if (!correctGuesses) {
            x = (int) (Math.random() * (Row));
            y = (int) (Math.random() * (Column));
            while (gridPLayer[x][y] == "-" || gridPLayer[x][y] == "X") {
                x = (int) (Math.random() * (Row));
                y = (int) (Math.random() * (Column));
            }

            int[] temp=new int[]{x,y};
            if (gridPLayer[x][y] == "O") {
                gameInfo.append("The Computer hit one of your ships!");
                gridPLayer[x][y] = "X";
                updateplayerCell(x, y, "X");
                --Test.numPlayerRemain;
                whichshipiwashit(playership1,temp);
                whichshipiwashit(playership2,temp);
                whichshipiwashit(playership3,temp);

                if(playership1==null||playership2==null||playership3==null){
                    gameInfo.append("one of your ship has been sunk by computer!!");
                }

                justarandomnamebecauseidontknowwhattonamethisvaraiable = possibleCoordinate(justarandomnamebecauseidontknowwhattonamethisvaraiable, x, y);

                correctGuesses = true;
            } else if (gridPLayer[x][y] == " ") {
                gameInfo.append("Computer missed");
                gridPLayer[x][y] = "-";
                updateplayerCell(x, y, "-");
            }
        } else if (correctGuesses) {
            if (tem == 8){
                tem = 0;
                correctGuesses = false;
                computerTurn();
            }

            x = justarandomnamebecauseidontknowwhattonamethisvaraiable[0][tem];
            y = justarandomnamebecauseidontknowwhattonamethisvaraiable[1][tem];

            if (gridPLayer[x][y] == "O") {
                gameInfo.append("The Computer hit one of your ships!");
                gridPLayer[x][y] = "X";
                updateplayerCell(x, y, "X");
                --Test.numPlayerRemain;
                tem = 0;
                correctGuesses = false;

            } else if (gridPLayer[x][y] == " ") {
                gameInfo.append("Computer missed");
                gridPLayer[x][y] = "-";
                updateplayerCell(x, y, "-");
                tem++;
            } else if (gridPLayer[x][y] == "-"||gridPLayer[x][y] == "X"){
                tem++;
                computerTurn();
            }

        }
    }

    public static int[][] possibleCoordinate(int[][] grid, int x, int y){
        int tem2=0;
        for(int i=0;i<8;i++){
            if(i==0&&y!=0){
                grid[0][tem2]=x;
                grid[1][tem2]=y-1;
                tem2++;
            }
            else if(i==1&&x!=0&&y!=0){
                grid[0][tem2]=x-1;
                grid[1][tem2]=y-1;
                tem2++;
            }
            else if(i==2&&x!=0){
                grid[0][tem2]=x-1;
                grid[1][tem2]=y;
                tem2++;
            }
            else if(i==3&&x!=0&&y!=7){
                grid[0][tem2]=x-1;
                grid[1][tem2]=y+1;
                tem2++;
            }
            else if(i==4&&y!=7){
                grid[0][tem2]=x;
                grid[1][tem2]=y+1;
                tem2++;
            }
            else if(i==5&&x!=7&&y!=7){
                grid[0][tem2]=x+1;
                grid[1][tem2]=y+1;
                tem2++;
            }
            else if(i==6&&x!=7){
                grid[0][tem2]=x+1;
                grid[1][tem2]=y;
                tem2++;
            }
            else if(i==7&&x!=7&&y!=0){
                grid[0][tem2]=x+1;
                grid[1][tem2]=y-1;
                tem2++;
            }

        }
        return grid;
    }

    public static void gameOver(){
        gameInfo.append("Your remaining: " + Test.numPlayerRemain + " | Computer remaining: " + Test.NumComputerRemain+"\n");
        if(Test.NumComputerRemain == 0) {
            JOptionPane.showMessageDialog(null,
                    "You win",
                    "Victory",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        else if (Test.numPlayerRemain == 0) {
            JOptionPane.showMessageDialog(null,
                    "Haha noob =)))",
                    "Lose",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static void ship1(int x, int y, String[][] grid){     //ship type 1x1
        if (grid[x][y] == " ") {
            grid[x][y] = "O";

        }
        else System.out.println("You can't place two ships on the same location");
    }

    public static void ship2(int x, int y,String type, String[][] grid){     //ship type 1x2
        if(type.equals("horizontal")){
            if (grid[x][y] == " " && grid[x][y+1] == " ") {
                grid[x][y] = "O";
                grid[x][y+1] = "O";
            }
            else System.out.println("You can't place two ships on the same location");
        } else if (type.equals("vertical")){
            if (grid[x][y] == " " && grid[x+1][y] == " ") {
                grid[x][y] = "O";
                grid[x+1][y] = "O";
            }
            else System.out.println("You can't place two ships on the same location");
        } else {
            if (grid[x][y] == " " && grid[x+1][y+1] == " ") {
                grid[x][y] = "O";
                grid[x+1][y+1] = "O";
            }
            else System.out.println("You can't place two ships on the same location");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Test();
            }
        });
    }

}

