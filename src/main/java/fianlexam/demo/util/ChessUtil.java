package fianlexam.demo.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Stack;

/**
 * @author Brandon.
 * @date 2019/6/1.
 * @time 13:07.
 */

public class ChessUtil {
    private final static int MAX_SIZE = 30;

    private final static int[][] CHESSBOARD = new int[MAX_SIZE][MAX_SIZE];

    public static void clearArr(){
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                CHESSBOARD[i][j] = 0;
            }
        }
    }

    public static void regret(int x, int y){
        CHESSBOARD[x][y] = 0;
    }

    public static int play(int x, int y,int num){
        if(x > MAX_SIZE || y > MAX_SIZE){
            return -1;
        }

        if(CHESSBOARD[x][y] != 0){
            return -1;
        }

        CHESSBOARD[x][y] = num;
        int[] count = new int[4];
        count[0] = checkRow(x,y);
        count[1] = checkColumn(x,y);
        count[2] = checkOppositeLine(x,y);
        count[3] = checkPositiveDiagonal(x,y);
        return Arrays.stream(count).max().getAsInt();
    }

    /**
    * @return 返回横排相同的棋子数
    */
    private static int checkRow(int x, int y){
        int count = 1;
        for (int i = x + 1; i < MAX_SIZE; i++) {
            if(CHESSBOARD[i][y] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }
            }else{
                break;
            }
        }
        for (int i = x - 1; i >= 0 ; i--) {
            if(CHESSBOARD[i][y] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }
            }else{
                break;
            }
        }
        return count;
    }

    /**
     * @return 返回每列相同的棋子数
     * */
    private static int checkColumn(int x, int y){
        int count = 1;
        for (int i = y + 1; i < MAX_SIZE; i++) {
            if(CHESSBOARD[x][i] == CHESSBOARD[x][y]){
                count ++;
                if(count == 5){
                    return count;
                }
            }else{
                break;
            }
        }
        for (int i = y - 1; i >= 0; i--) {
            if(CHESSBOARD[x][i] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }
            }
            else{
                break;
            }
        }
        return count;
    }

    /**
     * @return 返回正对角线上的相同的棋子数量
     * */
    private static int checkOppositeLine(int x, int y){
        int count = 1;
        for (int i = x + 1, j = y + 1; i < MAX_SIZE || j < MAX_SIZE; i++,j++) {
            if(CHESSBOARD[i][j] == CHESSBOARD[x][y]){
                count++;
                if(count==5){
                    return count;
                }
            }else{
                break;
            }
        }
        for (int i = x - 1, j = y - 1; i >= 0 || j >= 0 ; i--, j--) {
            if(CHESSBOARD[i][j] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }
            }else{
                break;
            }
        }
        return count;
    }

    /**
     * @return 返回反对角线上的相同的棋子数量
     * */
    private static int checkPositiveDiagonal(int x, int y){
        int count = 1;
        for (int i = x - 1,j = y + 1 ; i >= 0 && j < MAX_SIZE; i--, j++) {
            if(CHESSBOARD[i][j] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }else{
                    break;
                }
            }
        }
        for (int i = x + 1, j = y - 1; i < MAX_SIZE && j >= 0 ; i++,j--) {
            if(CHESSBOARD[i][j] == CHESSBOARD[x][y]){
                count++;
                if(count == 5){
                    return count;
                }
            }else{
                break;
            }
        }
        return count;
    }
}
