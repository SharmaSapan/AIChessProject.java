
// Sapan Sharma


import javax.swing.*;

import java.util.*;

class ChessProject {

    /**
     * Simply run the Chess project class which has main class. Enter the depth of the MIN-MAX tree for cutoff
     * ideal depth 4 or 5 in the console
     * After entering depth java pane with title "AI chess" will open
     * click in the screen to refresh mouse listener. To move the piece press and hold and move to desired location
     * Wait for opponent and then again take your turn.
     * acknowledgement to https://www.chessprogramming.org/Simplified_Evaluation_Function for piece square value
     */

     static String[][] board = {
            {"r", "k", "b", "q", "a", "b", "k", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "K", "B", "Q", "A", "B", "K", "R"}
    };


    static Scanner sc = new Scanner(System.in);

    static int kingP1, kingP2;

    static int globalDepth = sc.nextInt();



    public static void main(String[] args) {
        System.out.println("Entered Depth of tree: "+globalDepth);
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }

        ChessProject ab = new ChessProject();
        JFrame frame = new JFrame("AI Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI graphics = new UI();
        frame.add(graphics);
        frame.setSize(528, 543);
        frame.setVisible(true);

        while (!"A".equals(board[kingP1 / 8][kingP1 % 8])) {
            kingP1++;
        }
        while (!"a".equals(board[kingP2 / 8][kingP2 % 8])) {
            kingP2++;
        }
        sortMoves(validMoves());
    }

    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {

        String ledger = validMoves();
        if (depth == 0 || ledger.length() == 0) {
            return move + (Evaluation.rating(ledger.length(), depth) * (player * 2 - 1));
        }

        ledger = sortMoves(ledger);
        player = 1 - player; // to change between ai and human
        for (int i = 0; i < ledger.length(); i += 5) {
            makeMove(ledger.substring(i, i + 5));
            flipBoard();
            String returnString = alphaBeta(depth - 1, beta, alpha, ledger.substring(i, i + 5), player);
            int value = Integer.parseInt(returnString.substring(5));
            flipBoard();
            undoMove(ledger.substring(i, i + 5));
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }

            } else {
                if (value > alpha) {
                    alpha = value;
                    if (depth == globalDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            }
            if (alpha >= beta) {
                if (player == 0) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }
        if (player == 0) {
            return move + beta;
        } else {
            return move + alpha;
         // it returns the string value of move i.e. row1, column 1 to row 2 and column 2 with possible attack on piece
        }
    }

    public static void flipBoard() {
       // board can be easily flipped by changing case of the characters, and keeping rules same and array manipulations
        String tempBoard;
        for (int i = 0; i < 32; i++) {
            int k = i / 8, q = i % 8;
            if (Character.isUpperCase(board[k][q].charAt(0))) {
                tempBoard = board[k][q].toLowerCase();
            } else {
                tempBoard = board[k][q].toUpperCase();
            }
            if (Character.isUpperCase(board[7 - k][7 - q].charAt(0))) {
                board[k][q] = board[7 - k][7 - q].toLowerCase();
            } else {
                board[k][q] = board[7 - k][7 - q].toUpperCase();
            }
            board[7 - k][7 - q] = tempBoard;
        }
        int kingTemp = kingP1;
        kingP1 = 63 - kingP2;
        kingP2 = 63 - kingTemp;
    }

    public static void makeMove(String move) {

        if (move.charAt(4) != 'P') {
            board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] =
                    board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
            if ("A".equals(board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]))
            {
                kingP1 = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
            }
        } else {
            board[1][Character.getNumericValue(move.charAt(0))] = " ";
            board[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
            // when pawn is promoted
        }
    }

    public static void undoMove(String move) {
        if (move.charAt(4) != 'P') {
            board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] =
                    board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] =
                    String.valueOf(move.charAt(4));
            if ("A".equals(board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]))
            {
                kingP1 = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
            }
        } else {
            //if pawn promotion
            board[1][Character.getNumericValue(move.charAt(0))] = "P";
            board[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
        }
    }

    public static String sortMoves(String move) {
        int[] score = new int[move.length() / 5];
        for (int i = 0; i < move.length(); i += 5) {
            makeMove(move.substring(i, i + 5));
            score[i / 5] = -Evaluation.rating(-1, 0);
            undoMove(move.substring(i, i + 5));
        }
        String newMoveA = "", newMoveB = move;
        for (int i = 0; i < Math.min(6, move.length() / 5); i++) {//first few moves only
            int max = -10000000, maxLocation = 0;
            for (int j = 0; j < move.length() / 5; j++) {
                if (score[j] > max) {
                    max = score[j];
                    maxLocation = j;
                }
            }
            score[maxLocation] = -10000000;
            newMoveA += move.substring(maxLocation * 5, maxLocation * 5 + 5);
            newMoveB = newMoveB.replace(move.substring(maxLocation * 5, maxLocation * 5 + 5), "");
        }
        return newMoveA + newMoveB;
    }

    public static String validMoves() {
        String ledger = "";
        for (int i = 0; i < 64; i++) {
            switch (board[i / 8][i % 8]) {
                case "P":
                    ledger += validPawnMove(i);
                    break;
                case "R":
                    ledger += validRookMove(i);
                    break;
                case "K":
                    ledger += validKnightMove(i);
                    break;
                case "Q":
                    ledger += validQueenMove(i);
                    break;
                case "B":
                    ledger += validBishopMove(i);
                    break;
                case "A":
                    ledger += validKingMove(i);
                    break;
            }
        }
        return ledger;
        // ledger contains all the moves in given syntax:
        // r1,c1,r2,c2,captured piece...
    }

//        public static boolean repeatedMoves() {
//
//        }

    public static boolean checkOnKing() {

        int temp = 1;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    while (" ".equals(board[kingP1 / 8 + temp * i][kingP1 % 8 + temp * j])) {
                        temp++;
                    }
                    if ("b".equals(board[kingP1 / 8 + temp * i][kingP1 % 8 + temp * j]) ||
                            "q".equals(board[kingP1 / 8 + temp * i][kingP1 % 8 + temp * j])) {
                        return false;
                    }
                    // check by bishop or queen
                } catch (Exception e) {
                }
                temp = 1;
            }
        }

        for (int i = -1; i <= 1; i += 2) {
            try {
                while (" ".equals(board[kingP1 / 8][kingP1 % 8 + temp * i])) {
                    temp++;
                }
                if ("r".equals(board[kingP1 / 8][kingP1 % 8 + temp * i]) ||
                        "q".equals(board[kingP1 / 8][kingP1 % 8 + temp * i])) {
                    return false;
                }
                // check by rook or queen
            } catch (Exception e) {
            }
            temp = 1;
            try {
                while (" ".equals(board[kingP1 / 8 + temp * i][kingP1 % 8])) {
                    temp++;
                }
                if ("r".equals(board[kingP1 / 8 + temp * i][kingP1 % 8]) ||
                        "q".equals(board[kingP1 / 8 + temp * i][kingP1 % 8])) {
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if ("k".equals(board[kingP1 / 8 + i][kingP1 % 8 + j * 2])) {
                        return false;
                    }
                } catch (Exception e) {
                }
                try {
                    if ("k".equals(board[kingP1 / 8 + i * 2][kingP1 % 8 + j])) {
                        return false;
                    }
                    // check performed by knight
                } catch (Exception e) {
                }
            }
        }

        if (kingP1 >= 16) {
            try {
                if ("p".equals(board[kingP1 / 8 - 1][kingP1 % 8 - 1])) {
                    return false;
                }
                // check performed by pawn
            } catch (Exception e) {
            }

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        try {
                            if ("a".equals(board[kingP1 / 8 + i][kingP1 % 8 + j])) {
                                return false;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String validPawnMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            try {
                if (Character.isLowerCase(board[r - 1][c + j].charAt(0)) && i >= 16) {
                    oldPiece = board[r - 1][c + j];
                    board[r][c] = " ";
                    board[r - 1][c + j] = "P";
                    if (checkOnKing()) {
                        ledger = ledger + r + c + (r - 1) + (c + j) + oldPiece;
                    }
                    board[r][c] = "P";
                    board[r - 1][c + j] = oldPiece;
                } //capture
            } catch (Exception e) {
            }
            try {
                if (Character.isLowerCase(board[r - 1][c + j].charAt(0)) && i < 16) {
                    String[] temp = {"Q", "R", "B", "K"};
                    for (int k = 0; k < 4; k++) {
                        oldPiece = board[r - 1][c + j];
                        board[r][c] = " ";
                        board[r - 1][c + j] = temp[k];
                        if (checkOnKing()) {
                            //column1,column2,captured-piece,new-piece,P
                            ledger = ledger + c + (c + j) + oldPiece + temp[k] + "P";
                        }
                        board[r][c] = "P";
                        board[r - 1][c + j] = oldPiece;
                    } //promotion && capture
                }
            } catch (Exception e) {
            }
        }

        try {     //move one up
            if (" ".equals(board[r - 1][c]) && i >= 16) {
                oldPiece = board[r - 1][c];
                board[r][c] = " ";
                board[r - 1][c] = "P";
                if (checkOnKing()) {
                    ledger = ledger + r + c + (r - 1) + c + oldPiece;
                }
                board[r][c] = "P";
                board[r - 1][c] = oldPiece;
            }
        } catch (Exception e) {
        }
        try {//promotion && no capture
            if (" ".equals(board[r - 1][c]) && i < 16) {
                String[] temp = {"Q", "R", "B", "K"};
                for (int k = 0; k < 4; k++) {
                    oldPiece = board[r - 1][c];
                    board[r][c] = " ";
                    board[r - 1][c] = temp[k];
                    if (checkOnKing()) {
                        //column1,column2,captured-piece,new-piece,P
                        ledger = ledger + c + c + oldPiece + temp[k] + "P";
                    }
                    board[r][c] = "P";
                    board[r - 1][c] = oldPiece;
                }
            }
        } catch (Exception e) {
        }
        try {    //move two up
            if (" ".equals(board[r - 1][c]) && " ".equals(board[r - 2][c]) && i >= 48) {
                oldPiece = board[r - 2][c];
                board[r][c] = " ";
                board[r - 2][c] = "P";
                if (checkOnKing()) {
                    ledger = ledger + r + c + (r - 2) + c + oldPiece;
                }
                board[r][c] = "P";
                board[r - 2][c] = oldPiece;
            }
        } catch (Exception e) {
        }
        return ledger;
    }

    public static String validRookMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            try {
                while (" ".equals(board[r][c + temp * j])) {
                    oldPiece = board[r][c + temp * j];
                    board[r][c] = " ";
                    board[r][c + temp * j] = "R";
                    if (checkOnKing()) {
                        ledger = ledger + r + c + r + (c + temp * j) + oldPiece;
                    }
                    board[r][c] = "R";
                    board[r][c + temp * j] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(board[r][c + temp * j].charAt(0))) {
                    oldPiece = board[r][c + temp * j];
                    board[r][c] = " ";
                    board[r][c + temp * j] = "R";
                    if (checkOnKing()) {
                        ledger = ledger + r + c + r + (c + temp * j) + oldPiece;
                    }
                    board[r][c] = "R";
                    board[r][c + temp * j] = oldPiece;
                }
            }
            catch (Exception e) {
            }

            temp = 1;
            try {
                while (" ".equals(board[r + temp * j][c])) {
                    oldPiece = board[r + temp * j][c];
                    board[r][c] = " ";
                    board[r + temp * j][c] = "R";
                    if (checkOnKing()) {
                        ledger = ledger + r + c + (r + temp * j) + c + oldPiece;
                    }
                    board[r][c] = "R";
                    board[r + temp * j][c] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(board[r + temp * j][c].charAt(0))) {
                    oldPiece = board[r + temp * j][c];
                    board[r][c] = " ";
                    board[r + temp * j][c] = "R";
                    if (checkOnKing()) {
                        ledger = ledger + r + c + (r + temp * j) + c + oldPiece;
                    }
                    board[r][c] = "R";
                    board[r + temp * j][c] = oldPiece;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        return ledger;
    }

    public static String validKnightMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    if (Character.isLowerCase(board[r + j][c + k * 2].charAt(0)) || " ".equals(board[r + j][c + k * 2]))
                    {
                        oldPiece = board[r + j][c + k * 2];
                        board[r][c] = " ";
                        if (checkOnKing()) {
                            ledger = ledger + r + c + (r + j) + (c + k * 2) + oldPiece;
                        }
                        board[r][c] = "K";
                        board[r + j][c + k * 2] = oldPiece;
                    }
                } catch (Exception e) {
                }
                try {
                    if (Character.isLowerCase(board[r + j * 2][c + k].charAt(0)) || " ".equals(board[r + j * 2][c + k]))
                    {
                        oldPiece = board[r + j * 2][c + k];
                        board[r][c] = " ";
                        if (checkOnKing()) {
                            ledger = ledger + r + c + (r + j * 2) + (c + k) + oldPiece;
                        }
                        board[r][c] = "K";
                        board[r + j * 2][c + k] = oldPiece;
                    }
                } catch (Exception e) {
                }
            }
        }
        return ledger;
    }

    public static String validBishopMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    while (" ".equals(board[r + temp * j][c + temp * k])) {
                        oldPiece = board[r + temp * j][c + temp * k];
                        board[r][c] = " ";
                        board[r + temp * j][c + temp * k] = "B";
                        if (checkOnKing()) {
                            ledger = ledger + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                        }
                        board[r][c] = "B";
                        board[r + temp * j][c + temp * k] = oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(board[r + temp * j][c + temp * k].charAt(0))) {
                        oldPiece = board[r + temp * j][c + temp * k];
                        board[r][c] = " ";
                        board[r + temp * j][c + temp * k] = "B";
                        if (checkOnKing()) {
                            ledger = ledger + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                        }
                        board[r][c] = "B";
                        board[r + temp * j][c + temp * k] = oldPiece;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        return ledger;
    }

    public static String validQueenMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (j != 0 || k != 0) {
                    try {
                        while (" ".equals(board[r + temp * j][c + temp * k])) {
                            oldPiece = board[r + temp * j][c + temp * k];
                            board[r][c] = " ";
                            board[r + temp * j][c + temp * k] = "Q";
                            if (checkOnKing()) {
                                ledger = ledger + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                            }
                            board[r][c] = "Q";
                            board[r + temp * j][c + temp * k] = oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(board[r + temp * j][c + temp * k].charAt(0))) {
                            oldPiece = board[r + temp * j][c + temp * k];
                            board[r][c] = " ";
                            board[r + temp * j][c + temp * k] = "Q";
                            if (checkOnKing()) {
                                ledger = ledger + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
                            }
                            board[r][c] = "Q";
                            board[r + temp * j][c + temp * k] = oldPiece;
                        }
                    } catch (Exception e) {
                    }
                    temp = 1;
                }
            }
        }
        return ledger;
    }

    public static String validKingMove(int i) {
        String ledger = "", oldPiece;
        int r = i / 8, c = i % 8;
        for (int j = 0; j < 9; j++) {
            if (j != 4) {
                try {
                    if (Character.isLowerCase(board[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) ||
                            " ".equals(board[r - 1 + j / 3][c - 1 + j % 3])) {
                        oldPiece = board[r - 1 + j / 3][c - 1 + j % 3];
                        board[r][c] = " ";
                        board[r - 1 + j / 3][c - 1 + j % 3] = "A";
                        int kingTemp = kingP1;
                        kingP1 = i + (j / 3) * 8 + j % 3 - 9;
                        if (checkOnKing()) {
                            ledger = ledger + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + oldPiece;
                        }
                        board[r][c] = "A";
                        board[r - 1 + j / 3][c - 1 + j % 3] = oldPiece;
                        kingP1 = kingTemp;
                    }
                } catch (Exception e) {
                }
            }
        }
        return ledger;
    }
}