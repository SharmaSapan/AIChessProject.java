import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UI extends JPanel implements MouseMotionListener, MouseListener {

    static int squareSize=64;
    static int mouseX, mouseY, newMouseX, newMouseY;

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.setBackground(Color.BLUE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int i=0; i<64; i+=2) {
            g.setColor(new Color(255,220,120));
            g.fillRect(( i % 8 + (i/8) % 2) * squareSize, (i/8) * squareSize, squareSize, squareSize);
            g.setColor(new Color(180,70,40));
            g.fillRect(((i+1) % 8- ( (i+1) / 8) % 2) * squareSize,((i+1) / 8) * squareSize, squareSize, squareSize);
        }

        Image pieces;

        pieces = new ImageIcon("Chess.png").getImage();
        for (int i=0; i<64; i++) {
            int j = -1,k = -1;
            switch (ChessProject.board[i/8][i%8]) {
                // defining each piece on the board
                case "R": j=2; k=0;
                    break;
                case "r": j=2; k=1;
                    break;
                case "P": j=5; k=0;
                    break;
                case "p": j=5; k=1;
                    break;
                case "Q": j=1; k=0;
                    break;
                case "q": j=1; k=1;
                    break;
                case "A": j=0; k=0;
                    break;
                case "a": j=0; k=1;
                    break;
                case "K": j=4; k=0;
                    break;
                case "k": j=4; k=1;
                    break;
                case "B": j=3; k=0;
                    break;
                case "b": j=3; k=1;
                    break;

            }

            if (j!=-1 && k!=-1) {
                g.drawImage(pieces, (i % 8)*squareSize, (i / 8)*squareSize, (i % 8 + 1)*squareSize,
                        (i / 8 + 1) * squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) {
            //if inside the board
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX() < 8 * squareSize && e.getY() < 8*squareSize) {
            //if inside the board
            newMouseX = e.getX();
            newMouseY = e.getY();
            if (e.getButton() == MouseEvent.BUTTON1) {
                String dragMove;
                if (newMouseY/squareSize == 0 && mouseY/squareSize == 1 && "P".equals
                        (ChessProject.board[mouseY/squareSize][mouseX/squareSize])) {
                    //pawn promotion
                    dragMove =""+ mouseX/squareSize + newMouseX/squareSize +
                            ChessProject.board[newMouseY/squareSize][newMouseX/squareSize]+"QP";
                }
                else {
                    //regular move
                    dragMove = ""+ mouseY/squareSize + mouseX/squareSize + newMouseY/squareSize +
                            newMouseX/squareSize + ChessProject.board[newMouseY/squareSize][newMouseX/squareSize];
                }

                String userPosibilities = ChessProject.validMoves();
                if (userPosibilities.replaceAll(dragMove, "").length()<userPosibilities.length()) {
                    //if valid move
                    ChessProject.makeMove(dragMove);
                    ChessProject.flipBoard();
                    ChessProject.makeMove(ChessProject.alphaBeta(ChessProject.globalDepth, 1000000, -1000000, "", 0));
                    ChessProject.flipBoard();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}