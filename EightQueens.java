

public class EightQueens implements Cloneable{
    private char[][] board = new char[8][8];
    private int queens;
    private int currentColumn;
    private int skipToRow;

    public static void main (String[] args) throws CloneNotSupportedException {
        System.out.println("Testing setQueens with an empty board and 8 queens.");
        EightQueens test1 = new EightQueens();
        test1.setQueens(8);
        test1.printBoard();
        System.out.println();
    }

    public EightQueens() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 'o';
            }
        }
        queens = 0;
        currentColumn = 0;
        skipToRow = 0;
    }

    public Object clone() throws CloneNotSupportedException {
        EightQueens copy = (EightQueens)super.clone();
        copy.board = this.getBoard();
        return copy;
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public char[][] getBoard() {
        char[][] copy = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public void setQueen(int row, int column) {
        board[row][column] = 'Q';
        queens++;
    }

    public void setInitialQueen(int row, int column) {
        board[row][column] = 'q';
        queens++;
    }

    public void emptySquare(int row, int column) {
        if (board[row][column] == 'Q' || board[row][column] == 'q') {
            queens--;
        }
        board[row][column] = 'o';
    }

    public void finalizeQueenPlacement() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'q') {
                    board[i][j] = 'Q';
                }
            }
        }
    }

    public void emptyBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 'o';
            }
        }
        queens = 0;
    }

    public void removeInitialQueens() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'q') {
                    board[i][j] = 'o';
                }
            }
        }
    }

    public boolean setQueens (int queensRemaining) {
        if (queensRemaining == 0) {
            finalizeQueenPlacement();
            return true;
        } else if (queensRemaining > 8 || queensRemaining < 0) {
            throw new IllegalArgumentException();
        } else if (queens + queensRemaining > 8) {
            return false;
        } else if (currentColumn > 7) {
            return false;
        } else {
            boolean done;
            int i;
            if (skipToRow == -1) {
                removeInitialQueens();
                skipToRow = 0;
                return false;
            } else if (skipToRow > 0) {
                i = skipToRow;
                skipToRow = 0;
            } else {
                i = 0;
            }
            
            while (true) {
                if (checkColumn(currentColumn) != 0) {
                    currentColumn++;
                } else if (i == 8) {
                    i = 0;
                    break;
                } else {
                    setInitialQueen(i, currentColumn);
                    if (checkForDisallowed()) {
                        currentColumn++;
                        done = setQueens(queensRemaining - 1);
                        if (done) {
                            return true;
                        }
                    } else {
                        emptySquare(i, currentColumn);
                        if (i == 7) {
                            i = 0;
                            break;
                        }
                    }
                }
                if (i < 7) {
                    i++;
                } else {
                    i = 0;
                    break;
                }
            }
            currentColumn--;
            skipToRow = findRemoveQueenInColumn(currentColumn);
            return false;
        }
    }

    public int findRemoveQueenInColumn(int column) {
        int row = 0;
        while (true) {
            if (row > 7) {
                column--;
                currentColumn--;
                row = 0;
            } else if (column < 0) {
                return -1;
            } else if (board[row][column] == 'Q') {
                if (column == 0) {
                    return -1;
                } else {
                    currentColumn--;
                    column--;
                    row = 0;
                }
            } else  if (board[row][column] == 'q') {
                emptySquare(row, column);
                return row + 1;
            } else {
                row++;
            }
        }
    } //Returns the row of the previous intital queen plus 1, removes the 
      //previous initial queen, and updates currentColumn if necessary. 

    public boolean checkForDisallowed() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'Q' || board[i][j] == 'q') {
                    if (checkRow(i) > 1 || checkColumn(j) > 1 || !checkDiagonals(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    } //Returns true if there are no conflicts between any of the queens on the board, 
      //returns false if there are conflicts. 

    public int checkRow(int row) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (board[row][i] == 'Q' || board[row][i] == 'q') {
                count++;
            }
        }
        return count;
    } //Returns the number of queens in the specified row.

    public int checkColumn(int column) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (board[i][column] == 'Q' || board[i][column] == 'q') {
                count++;
            }
        }
        return count;
    } //Returns the number of queens in the specified column.

    public boolean checkDiagonals(int row, int column) {
        int i, j;
        if (row < 7 && column < 7) {
            i = row + 1;
            j = column + 1;
            while (i < 8 && j < 8) {
                if (board[i][j] == 'Q' || board[i][j] == 'q') {
                    return false;
                }
                i++;
                j++;
            }
        }

        if (row > 0 && column > 0) {
            i = row - 1;
            j = column - 1;
            while (i >= 0 && j >= 0) {
                if (board[i][j] == 'Q' || board[i][j] == 'q') {
                    return false;
                }
                i--;
                j--;
            }
        }

        if (row < 7 && column > 0) {
            i = row + 1;
            j = column - 1;
            while (i < 8 && j >= 0) {
                if (board[i][j] == 'Q' || board[i][j] == 'q') {
                    return false;
                }
                i++;
                j--;
            }
        }

        if (row > 0 && column < 7) {
            i = row - 1;
            j = column + 1;
            while (i >= 0 && j < 8) {
                if (board[i][j] == 'Q' || board[i][j] == 'q') {
                    return false;
                }
                i--;
                j++;
            }
        }

        return true;
    } //Returns false if there is a queen located diagonally from
      //the specified position, returns true if there is not. 
} //End of class declaration. 