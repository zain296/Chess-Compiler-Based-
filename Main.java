import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
public class Main {
    static Scanner in = new Scanner(System.in);
    static String[][] MainBoard = BoardStructure();
    static void Instructions() {
        System.out.println("""
                Chess is a two-player strategy game played on an 8x8 board. Each player starts with 16 pieces: one king, 
                one queen, two rooks, two bishops, 
                two knights, and eight pawns.The objective of the game is to checkmate your opponent's king,which means 
                the king is under threat of capture 
                and cannot make any legal move to escape.
                Each type of piece moves in a unique way:
                
                1.King: Moves one square in any direction.
                2.Queen: Moves any number of squares in any direction.
                3.Rook: Moves any number of squares horizontally or vertically.
                4.Bishop: Moves diagonally across any number of squares.
                5.Knight: Moves in an L shape. Knights can jump over other pieces.
                6.Pawn: Moves forward one square at a time. On its first move, a pawn can move forward two squares.
                
                Players take turns making moves.""");
        System.out.println();
    }
    static int[] MoveDefine(char a, char b){
        int startcolumn=0;
        int endcolumn=0;
        if(a =='a'){startcolumn=1;}
        else if(a =='b') {startcolumn=2;}
        else if(a =='c') {startcolumn=3;}
        else if(a =='d') {startcolumn=4;}
        else if(a =='e') {startcolumn=5;}
        else if(a =='f') {startcolumn=6;}
        else if(a =='g') {startcolumn=7;}
        else if(a =='h') {startcolumn=8;}
        if(b =='a'){endcolumn=1;}
        else if(b =='b') {endcolumn=2;}
        else if(b =='c') {endcolumn=3;}
        else if(b =='d') {endcolumn=4;}
        else if(b =='e') {endcolumn=5;}
        else if(b =='f') {endcolumn=6;}
        else if(b =='g') {endcolumn=7;}
        else if(b =='h') {endcolumn=8;}
        return new int[]{startcolumn,endcolumn};
    }
    // ── BOARD NOW USES SIMPLE TEXT CODES INSTEAD OF UNICODE SYMBOLS ──────────
    // White pieces: wK wQ wR wB wN wP
    // Black pieces: bK bQ bR bB bN bP
    static String[][] BoardStructure(){
        String[][] board = new String[8][8];
        board[0][4] = "wK"; board[7][4] = "bK";
        board[0][3] = "wQ"; board[7][3] = "bQ";
        board[0][2] = "wB"; board[0][5] = "wB";
        board[7][2] = "bB"; board[7][5] = "bB";
        board[0][1] = "wN"; board[0][6] = "wN";
        board[7][1] = "bN"; board[7][6] = "bN";
        board[0][0] = "wR"; board[0][7] = "wR";
        board[7][0] = "bR"; board[7][7] = "bR";
        for (int i=0;i<8;i++){
            board[1][i]= "wP";
            board[6][i]= "bP";
        }
        for (int i = 2; i < 6; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = "Empty";
        return board;
    }
    // ── PRINTBOARD UPDATED FOR FIXED-WIDTH TEXT CODES ─────────────────────────
    // Each cell is exactly 4 visible characters wide between separators
    // (e.g. "| - |" or "| wK|"), matching the "+---+" border segments below,
    // so every column lines up perfectly no matter what's in the cell.
    static void PrintBoard(String[][] board){
        System.out.println("________________________________");
        System.out.println("    a   b   c   d   e   f   g   h");
        System.out.println("  +---+---+---+---+---+---+---+---+");
        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < 8; j++) {
                if (board[i][j].equals("Empty")) {
                    System.out.print(" - |");   // empty square (4 chars: space,-,space,|)
                } else {
                    System.out.print(" " + board[i][j] + "|"); // e.g. " wK|" (4 chars)
                }
            }
            System.out.println();
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    a   b   c   d   e   f   g   h");
        System.out.println("________________________________");
    }
    // ─────────────────────────────────────────────────────────────────────────
    static boolean range(int r, int c) {
        return r >= 0 && r <= 7 && c >= 0 && c <= 7;
    }
    // Piece colour is now just the first character of the code: 'w' or 'b'
    static char PieceType(String p) {
        if (p.equals("Empty")) { return '-'; }
        char first = p.charAt(0);
        if (first == 'w') { return 'W'; }
        if (first == 'b') { return 'B'; }
        return '-';
    }
    static boolean PieceMoves(int FromRow, int FromCol, int ToRow, int ToCol ,boolean whitesturn){
        if (!range(FromRow, FromCol) || !range(ToRow, ToCol)) { return false; }
        String piece = MainBoard[FromRow][FromCol];
        String destination = MainBoard[ToRow][ToCol];
        if (piece.equals("Empty")) { return false; }
        if (whitesturn && PieceType(piece)=='B') { return false; }
        if (!whitesturn && PieceType(piece)=='W') { return false; }
        boolean SameTypePieces = (!destination.equals("Empty")) && PieceType(destination)==PieceType(piece);
        if(SameTypePieces){ return false; }
        int RowDiff = ToRow - FromRow;
        int ColDiff = ToCol - FromCol;
        // Piece kind is now the second character of the code: P, R, N, B, Q, K
        char kind = piece.charAt(1);
        if (kind == 'P') {
            return PawnMove(piece, FromRow, FromCol, ToRow, ToCol, destination);
        } else if (kind == 'R') {
            return RookMove(FromRow, FromCol, ToRow, ToCol);
        } else if (kind == 'N') {
            return KnightMove(RowDiff, ColDiff);
        } else if (kind == 'B') {
            return BishopMove(FromRow, FromCol, ToRow, ToCol);
        } else if (kind == 'Q') {
            return QueenMove(FromRow, FromCol, ToRow, ToCol);
        } else if (kind == 'K') {
            return KingMove(RowDiff, ColDiff);
        }
        return false;
    }
    static boolean PawnMove(String pice,int FR, int FC, int TR, int TC ,String dest){
        int direction;
        int startRow;
        if (PieceType(pice)=='W') {
            direction = 1;
            startRow = 1;
        } else {
            direction = -1;
            startRow = 6;
        }
        if (FC == TC && dest.equals("Empty")) {
            if (TR - FR == direction) { return true; }
            else if (FR == startRow && TR - FR == 2*direction && (MainBoard[FR + direction][FC]).equals("Empty")) { return true; }
        }
        if(Math.abs(TC - FC) == 1 && TR - FR == direction && !dest.equals("Empty")) { return true; }
        return false;
    }
    static boolean KingMove(int RDiff,int CDiff){
        return Math.abs(RDiff) <= 1 && Math.abs(CDiff) <= 1;
    }
    static boolean KnightMove(int RDiff,int CDiff){
        RDiff = Math.abs(RDiff); CDiff = Math.abs(CDiff);
        return (RDiff == 2 && CDiff == 1) || (RDiff == 1 && CDiff == 2);
    }
    static boolean BishopMove(int FR, int FC, int TR, int TC ){
        if (Math.abs(TR - FR) != Math.abs(TC - FC)) { return false; }
        int rowStep = (TR > FR) ? 1 : -1;
        int colStep = (TC > FC) ? 1 : -1;
        int r = FR + rowStep;
        int c = FC + colStep;
        while (r != TR && c != TC) {
            if (!MainBoard[r][c].equals("Empty")) { return false; }
            r += rowStep; c += colStep;
        }
        return true;
    }
    static boolean RookMove(int FR, int FC, int TR, int TC ){
        if (FR != TR && FC != TC) { return false; }
        if (FR == TR) {
            if (FC < TC) { for (int c = FC + 1; c < TC; c++) { if (!MainBoard[FR][c].equals("Empty")) return false; } }
            else         { for (int c = FC - 1; c > TC; c--) { if (!MainBoard[FR][c].equals("Empty")) return false; } }
        }
        if (FC == TC) {
            if (FR < TR) { for (int r = FR + 1; r < TR; r++) { if (!MainBoard[r][FC].equals("Empty")) return false; } }
            else         { for (int r = FR - 1; r > TR; r--) { if (!MainBoard[r][FC].equals("Empty")) return false; } }
        }
        return true;
    }
    static boolean QueenMove(int FR, int FC, int TR, int TC ){
        return(RookMove(FR, FC, TR, TC) || BishopMove(FR, FC, TR, TC));
    }
    static int[] KingPos(boolean white) {
        String kingSymbol = white ? "wK" : "bK";
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (MainBoard[r][c].equals(kingSymbol))
                    return new int[]{r, c};
        return null;
    }
    static boolean KingCheck(boolean white) {
        int[] kingPosition = KingPos(white);
        if (kingPosition == null) { return false; }
        int kgRow = kingPosition[0];
        int kgCol = kingPosition[1];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String piece = MainBoard[row][col];
                if (piece.equals("Empty")) { continue; }
                char color = PieceType(piece);
                if (white && color == 'B' || !white && color == 'W') {
                    if (PieceMoves(row, col, kgRow, kgCol, !white)) { return true; }
                }
            }
        }
        return false;
    }
    static boolean Checkmate(boolean white) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                String piece = MainBoard[fromRow][fromCol];
                if (piece.equals("Empty")) continue;
                char currentPlayer = white ? 'W' : 'B';
                if (PieceType(piece) != currentPlayer) { continue; }
                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        if (!range(toRow, toCol)) continue;
                        if (PieceMoves(fromRow, fromCol, toRow, toCol, white)) {
                            String fromBackup = MainBoard[fromRow][fromCol];
                            String toBackup = MainBoard[toRow][toCol];
                            MainBoard[toRow][toCol] = fromBackup;
                            MainBoard[fromRow][fromCol] = "Empty";
                            boolean stillInCheck = KingCheck(white);
                            MainBoard[fromRow][fromCol] = fromBackup;
                            MainBoard[toRow][toCol] = toBackup;
                            if (!stillInCheck) { return false; }
                        }
                    }
                }
            }
        }
        return true;
    }
    static void playGame() {
        MainBoard = BoardStructure();
        boolean whiteTrn = true;
        boolean filee = true;
        System.out.println("Before Starting the Game,Tell if you want to store Moves in a File?(y/n) ");
        String storemoves = in.next().toLowerCase();
        if(storemoves.equals("y")){
            try { File(); } catch (Exception e) { throw new RuntimeException(e); }
        } else {
            System.out.println("Ok");
            filee = false;
        }
        PrintBoard(MainBoard);
        while (true) {
            try {
                if (whiteTrn) { System.out.println("White's turn:"); }
                else          { System.out.println("Black's turn:"); }
                int startRow = in.nextInt();
                char startCol = in.next().toLowerCase().charAt(0);
                int endRow = in.nextInt();
                char endCol = in.next().toLowerCase().charAt(0);
                int sCol = 0, eCol = 0;
                int[] Col = MoveDefine(startCol, endCol);
                sCol = Col[0]; eCol = Col[1];
                if (filee){ Fwriter(startRow,startCol,endRow,endCol); }
                if (PieceMoves(startRow - 1, sCol - 1, endRow - 1, eCol - 1, whiteTrn)) {
                    String movedPiece = MainBoard[startRow - 1][sCol - 1];
                    String capturedPiece = MainBoard[endRow - 1][eCol - 1];
                    MainBoard[endRow - 1][eCol - 1] = movedPiece;
                    MainBoard[startRow - 1][sCol - 1] = "Empty";
                    boolean kingStillInCheck = KingCheck(whiteTrn);
                    if (kingStillInCheck) {
                        System.out.println("You can't move into check!");
                        MainBoard[startRow - 1][sCol - 1] = movedPiece;
                        MainBoard[endRow - 1][eCol - 1] = capturedPiece;
                    } else {
                        PrintBoard(MainBoard);
                        whiteTrn = !whiteTrn;
                        boolean opponentInCheck = KingCheck(whiteTrn);
                        if (opponentInCheck) {
                            if (whiteTrn) { System.out.println("White is in CHECK!"); }
                            else          { System.out.println("Black is in CHECK!"); }
                            boolean isMate = Checkmate(whiteTrn);
                            if (isMate) {
                                if (whiteTrn) { System.out.println("CHECKMATE! Black wins!"); }
                                else          { System.out.println("CHECKMATE! White wins!"); }
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("Illegal Move.Try Again:");
                }
            } catch (Exception e) {
                System.out.println("Invalid input format! Please use format like: 2 a 3 b");
                in.nextLine();
            }
        }
        if (filee){
            System.out.println("If you want to get Recorded moves tel (y/n): ");
            String resdfle = in.next().toLowerCase();
            if(resdfle.equals("y")){
                try { Freader(); } catch (Exception e) { throw new RuntimeException(e); }
            } else {
                System.out.println("Ok,no problem!");
            }
        }
    }
    static void File() throws Exception{
        File F = new File("GameRecord.txt");
        F.createNewFile();
    }
    static void Fwriter(int a, char b,int c,char d)throws Exception{
        FileWriter FileWr = new FileWriter("GameRecord.txt", true);
        FileWr.write("Moves: "+ a+" " + b+" "  + c+" "  + d+ " \n");
        FileWr.close();
    }
    static void Freader() throws Exception{
        FileReader FReader = new FileReader("GameRecord.txt");
        Scanner fileScanner = new Scanner(FReader);
        System.out.println("Recorded Moves:");
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            System.out.println(line);
        }
        fileScanner.close();
        FReader.close();
    }
    public static void main (String[]args){
        System.out.println("Welcome To Chess!");
        System.out.println("First of all if you need information about learning chess press y, else press n:");
        String I = in.next().toLowerCase();
        if (I.equals("y")) { Instructions(); }
        else { System.out.println("OK,now we proceed towards the game and making moves."); }
        System.out.println("""
                    Down there is a sample chess board.Game starts with white pieces making a move.
                    Now for making a move first you have to tell which row is this (named from 1-8) and which column is it
                    (named from a-h). Then you have to tell your destination same tell row and then column. Like this: (2 a 3 b)""");
        while (true) {
            playGame();
            try {
                System.out.print("Do you want to play again? (y/n): ");
                String again = in.next().toLowerCase();
                if (!again.equals("y")) { System.out.println("Thanks for playing!"); break; }
            } catch (Exception e) {
                System.out.println("Invalid input. Exiting game.");
                break;
            }
        }
    }
}
