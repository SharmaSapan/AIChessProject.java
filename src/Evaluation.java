public class Evaluation {

    public static int rating(int ledger, int depth) {

        int c = 0, material = rateMaterial();
        c += rateAttack();
        c += material;
        c += rateMovement(ledger, depth);
        c += ratePosition(material);
        ChessProject.flipBoard();
        material = rateMaterial();
        c -= rateAttack();
        c -= material;
        c -= rateMovement(ledger, depth);
        c -= ratePosition(material);
        ChessProject.flipBoard();
        return - (c+depth*50);
    }

    public static int rateAttack() {
        int c=0;
        int tempPositionC=ChessProject.kingP1;
        for (int i=0; i<64; i++) {
            switch (ChessProject.board[i/8][i%8]) {
                case "P": {
                    ChessProject.kingP1 = i; if (!ChessProject.checkOnKing()) { c-=64;}
                }
                break;
                case "R": {
                    ChessProject.kingP1 = i; if (!ChessProject.checkOnKing()) { c-=500;}
                }
                break;
                case "Q": {
                    ChessProject.kingP1 = i; if (!ChessProject.checkOnKing()) { c-=900;}
                }
                break;
                case "K": {
                    ChessProject.kingP1 = i; if (!ChessProject.checkOnKing()) { c-=300;}
                }
                break;
                case "B": {
                    ChessProject.kingP1 = i; if (!ChessProject.checkOnKing()) { c-=300;}
                }
                break;
            }
        }

        ChessProject.kingP1 = tempPositionC;
        if (!ChessProject.checkOnKing()) {
            c -= 200;
        }
        return c/2;
    }

    public static int rateMaterial() {

        int c = 0, bishopCounter = 0;
        for (int i=0;i<64;i++) {
            switch (ChessProject.board[i/8][i%8]) {
                case "P": c+=100;
                    break;
                case "B": bishopCounter += 1;
                    break;
                case "Q": c+=900;
                    break;
                case "R": c+=500;
                    break;
                case "K": c+=300;
                    break;
            }
        }

        if (bishopCounter>=2) {
            c += 300*bishopCounter;
        }
        else {
            if (bishopCounter==1) {
                c += 250;
            }
        }
        return c;
    }

    public static int rateMovement(int listLength, int depth) {

        int c = 0;
        c += listLength;//5 pointer per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!ChessProject.checkOnKing()) {//if checkmate
                c+=-200000*depth;
            } else {//if stalemate
                c+=-150000*depth;
            }
        }
        return 0;
    }

    // acknowledgement to https://www.chessprogramming.org/Simplified_Evaluation_Function
    // Piece square values which help provide moves to the AI
    // bonuses for certain locations and penalties for certain
    // 0 pertains to neutral

    private static int[][] knightBoard ={
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}};

    private static int[][] pawnBoard ={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}};

    private static int[][] bishopBoard ={
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};

    private static int[][] queenBoard ={
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};

    private static int[][] kingMidBoard ={
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}};

    private static int[][] kingEndBoard ={
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};

    private static int[][] rookBoard ={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 0,  0,  0,  5,  5,  0,  0,  0}};

    public static int ratePosition(int material) {
        int c = 0;
        for (int i=0; i<64; i++) {
            switch (ChessProject.board[i/8][i%8]) {
                case "P": c += pawnBoard[i/8][i%8];
                    break;
                case "R": c += rookBoard[i/8][i%8];
                    break;
                case "Q": c += queenBoard[i/8][i%8];
                    break;
                case "K": c += knightBoard[i/8][i%8];
                    break;
                case "B": c += bishopBoard[i/8][i%8];
                    break;
                case "A": if (material >= 1750) {
                    c+= kingMidBoard[i/8][i%8];
                    c += ChessProject.validKingMove(ChessProject.kingP1).length()*10;
                    } else
                        {c+=kingEndBoard[i/8][i%8]; c+=ChessProject.validKingMove(ChessProject.kingP1).length()*30;}
                    break;
            }
        }
        return c;
    }
}
