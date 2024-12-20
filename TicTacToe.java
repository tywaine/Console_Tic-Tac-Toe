import java.util.*;

public class TicTacToe implements Comparable<TicTacToe>{

    protected  final Random random = new Random();
    protected final Scanner sc = new Scanner(System.in);
    private final int[] CORNERS = {0, 2, 6, 8};
    private final int[] SIDES = {1, 3, 5, 7};
    private final int[] SIDES_TO_SIDE = {3, 5};
    private final int[] SIDES_TOP = {1, 7};
    protected final HashSet<Integer> playerValues = new HashSet<>();
    protected final HashSet<Integer> currentValues = new HashSet<>();
    private String currentDisplay = "";
    private int numOf_X_Wins = 0, numOf_O_Wins = 0, numOfDraws = 0;
    protected int i;
    protected final int SIZE = 3;
    protected int[] values = new int[9];
    private String[][] arrayBoard = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};

    public TicTacToe(){}

    public int compareTo(TicTacToe other){
        return this.numOf_X_Wins - other.numOf_X_Wins;
    }


    public String toString(){
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < SIZE; i++){
            str.append("\t| ").append(arrayBoard[i][0]).append(" | ").append(arrayBoard[i][1]).append(" | ").append(arrayBoard[i][2]).append(" |");

            if(i < SIZE - 1){
                str.append("\n");
            }
        }

        return str.toString();
    }

    public String[][] getBoard(){
        return arrayBoard;
    }

    public int getNumOf_O_Wins() {
        return numOf_O_Wins;
    }

    public int getNumOf_X_Wins() {
        return numOf_X_Wins;
    }

    public int getNumOfDraws() {
        return numOfDraws;
    }

    public void resetWinsAndDraws(){
        numOf_X_Wins = 0;
        numOf_O_Wins = 0;
        numOfDraws = 0;
    }

    public void evaluateResult(){
        int result = isSolved();

        if(result == 1){
            numOf_O_Wins++;
        }

        if(result == 2){
            numOf_X_Wins++;
        }

        if(result == 0){
            numOfDraws++;
        }
    }

    public void resetProgress(){
        i = 0;
        playerValues.clear();
        currentValues.clear();
        currentDisplay = "";
        values = new int[9];
        arrayBoard = new String[][] {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    }

    protected void exampleBoard(){
        System.out.println("| 0 | 1 | 2 |\n| 3 | 4 | 5 |\n| 6 | 7 | 8 |");
    }

    protected void input(int numInput, String strInput){
        if(numInput > 8 || numInput < 0 || currentValues.contains(numInput)){
            System.err.println("Number input is already in TicTacToe board");
            return;
        }

        if(numInput >= 6){
            arrayBoard[2][numInput - 6] = strInput;
        }
        else if(numInput >= 3){
            arrayBoard[1][numInput - 3] = strInput;
        }
        else{
            arrayBoard[0][numInput] = strInput;
        }

        currentValues.add(numInput);
        values[i++] = numInput;
    }
    //


    // Player input method
    private void playerInput(int number, boolean vsComputer, int turn){
        MyUtils.clearConsole();
        exampleBoard();
        input(number, (turn == 2) ? "X" : "O");
        playerValues.add(number);

        if(vsComputer){
            System.out.println("\n Current board:\n");
        }
        else{
            System.out.println("\n Player " + turn + " choose: " + number);
            System.out.println("\nCurrent board:\n");
        }

        currentDisplay = playerMessage(number, vsComputer, turn);
        displayBoard();
    }
    //


    // Computer input methods
    public void computerInputEasy(String input, boolean vsComputer, int turn, boolean display){
        int num = getRandomNum();
        input(num, input);

        if(display){
            MyUtils.clearConsole();
            exampleBoard();

            if(!vsComputer){
                System.out.println("\n Computer choose: " + num + "\n");
            }
            else{
                System.out.println("\n Computer " + turn + " (easy) choose: " + num + "\n");
            }

            currentDisplay = computerMessage(num, vsComputer, turn, "easy");
            displayBoard();
        }
    }

    public void computerInputHard(boolean vsComputer, boolean display){
        // Check if the first move was just played
        if(currentValues.size() == 1){
            // If the player didn't choose the center, occupy it
            if(values[0] != 4){
                input(4, "X");
            }
            // If the player chose the center, occupy a corner randomly
            else{
                input(getRandomCornerNum(), "X");
            }
        }
        // Check if the computer can win in the next move
        else if(hasWinningNumberRow("X")){
            input(findWinningNumberRow("X"), "X");
        }
        else if(hasWinningNumberColumn("X")){
            input(findWinningNumberColumn("X"), "X");
        }
        else if(hasWinningNumberDiagonal("X")){
            input(findWinningNumberDiagonal("X"), "X");
        }
        // Check if the player can win in the next move and block them
        else if(hasWinningNumberRow("O")){
            input(findWinningNumberRow("O"), "X");
        }
        else if(hasWinningNumberColumn("O")){
            input(findWinningNumberColumn("O"), "X");
        }
        else if(hasWinningNumberDiagonal("O")){
            input(findWinningNumberDiagonal("O"), "X");
        }
        // Check if it's the third move
        else if(currentValues.size() == 3){
            int num;
            // Check the positions of the first two moves and choose the third move accordingly
            if(isACornerNum(values[1])){
                input(getRandomCornerNum(), "X");
            }
            else if(isASideNum(values[0]) && isASideNum(values[2])){
                if((values[0] == 1 && values[2] == 5) || (values[0] == 5 && values[2] == 1)){
                    input(2, "X");
                }
                else if((values[0] == 1 && values[2] == 3) || (values[0] == 3 && values[2] == 1)){
                    input(0, "X");
                }
                else if((values[0] == 3 && values[2] == 7) || (values[0] == 7 && values[2] == 3)){
                    input(6, "X");
                }
                else{
                    input(8, "X");
                }
            }
            else if((isACornerNum(values[0]) && isASideNum(values[2])) || (isACornerNum(values[2]) && isASideNum(values[0]))){
                if((values[0] == 0 || values[2] == 0 || values[0] == 2 || values[2] == 2) && (values[0] == 7 || values[2] == 7)){
                    input(generateRandomSideToSideNum(), "X");
                }
                else if((values[0] == 6 || values[2] == 6 || values[0] == 8 || values[2] == 8) && (values[0] == 1 || values[2] == 1)){
                    input(generateRandomSideToSideNum(), "X");
                }
                else if((values[0] == 6 || values[2] == 6 || values[0] == 0 || values[2] == 0) && (values[0] == 5 || values[2] == 5)){
                    input(generateRandomSideTopNum(), "X");
                }
                else if((values[0] == 2 || values[2] == 2 || values[0] == 8 || values[2] == 8) && (values[0] == 3 || values[2] == 3)){
                    input(generateRandomSideTopNum(), "X");
                }
            }
            else{
                do{
                    int x = random.nextInt(4);
                    num = SIDES[x];
                }while(currentValues.contains(num));

                input(num, "X");
            }
        }
        // Check if corners are available and make a move accordingly
        else if(cornersAreAvailable()){
            if(currentValues.size() == 5){
                if(playerValues.contains(0) && playerValues.contains(5) && playerValues.contains(7) && numIsAvailable(8)){
                    input(8, "X");
                }
                else if(playerValues.contains(1) && playerValues.contains(3) && numIsAvailable(0)){
                    input(0, "X");
                }
                else if(playerValues.contains(1) && playerValues.contains(5) && numIsAvailable(2)){
                    input(2, "X");
                }
                else if(playerValues.contains(3) && playerValues.contains(7) && numIsAvailable(6)){
                    input(6, "X");
                }
                else if(playerValues.contains(7) && playerValues.contains(5) && numIsAvailable(8)){
                    input(8, "X");
                }
                else{
                    input(getRandomCornerNum(), "X");
                }
            }
            else{
                input(getRandomCornerNum(), "X");
            }
        }
        // If none of the above conditions are met, make a random move
        else{
            input(getRandomNum(), "X");
        }

        if(display){
            MyUtils.clearConsole();
            exampleBoard();

            if(vsComputer){
                System.out.println("\n Computer 2 (hard) choose: " + values[i - 1] + "\n");
            }
            else{
                System.out.println("\n Computer choose: " + values[i - 1] + "\n");
            }

            currentDisplay = computerMessage(values[i - 1], vsComputer, 2, "hard");
            displayBoard();
        }
    }


    public void computerInputMiniMax(String input, boolean display){
        input(findBestMove(arrayBoard, input), input);

        if(display){
            MyUtils.clearConsole();
            exampleBoard();
            System.out.println("\n Computer 1 (hard) choose: " + values[i - 1] + "\n");
            currentDisplay = computerMessage(values[i - 1], true, 1, "hard");
            displayBoard();
        }
    }

    private int miniMax(String[][] board, String player) {
        // Check if the game is over
        if(isSolved() != -1){
            String result = checkWinner(board);

            if(result.equals("X")){
                return -1; // X wins
            }
            else if(result.equals("O")){
                return 1; // O wins
            }
            else {
                return 0; // Draw
            }
        }

        // Initialize best score based on the player
        int bestScore = (player.equals("O")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Loop through all empty cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].equals(" ")) {
                    // Try placing the player's symbol in the empty cell
                    board[i][j] = player;
                    // Recursively calculate score for the next move
                    int score = miniMax(board, (player.equals("X")) ? "O" : "X");
                    // Undo the move
                    board[i][j] = " ";
                    // Update the best score based on the player
                    if (player.equals("O")) {
                        bestScore = Math.max(score, bestScore); // Maximize for O
                    }
                    else{
                        bestScore = Math.min(score, bestScore); // Minimize for X
                    }
                }
            }
        }

        return bestScore;
    }

    // Method to find the best move using MiniMax
    public int findBestMove(String[][] board, String player){
        if(currentValues.isEmpty()){
            return getRandomNum();
        }

        int bestScore = (player.equals("O")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestMove = -1;

        // Loop through all empty cells
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].equals(" ")) {
                    // Try placing the player's symbol in the empty cell
                    board[i][j] = player;
                    // Recursively calculate score for the next move
                    int score = miniMax(board, (player.equals("X")) ? "O" : "X");
                    // Undo the move
                    board[i][j] = " ";
                    // Update the best move based on the player and score
                    if((player.equals("O") && score > bestScore) || (player.equals("X") && score < bestScore)) {
                        bestScore = score;
                        bestMove = i * 3 + j;
                    }
                }
            }
        }

        return bestMove;
    }

    // Helper method to check the winner
    private String checkWinner(String[][] board) {
        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals(" ")) {
                return board[i][0];
            }
        }
        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j]) && !board[0][j].equals(" ")) {
                return board[0][j];
            }
        }
        // Check diagonals
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals(" ")) {
            return board[0][0];
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals(" ")) {
            return board[0][2];
        }

        return " "; // No winner
    }

    //


    // Win-checking methods
    private int findWinningNumberRow(String input){
        validTicTacToeBoardInputCheck(input);

        int generalCount = 0, spaceIndex = 0;

        for(String[] board: arrayBoard){
            int count = 0;

            for(String str: board){
                if(str.equals(input)){
                    count++;
                }
                else if(str.equals(" ")){
                    spaceIndex = generalCount;
                }
                else{
                    spaceIndex = -1;
                }
                generalCount++;
            }

            if(count == 2 && spaceIndex != -1){
                return spaceIndex;
            }
        }

        return -1;
    }

    private int findWinningNumberColumn(String input){
        validTicTacToeBoardInputCheck(input);

        int generalCount, spaceIndex = 0;

        for(int i = 0; i < SIZE; i++){
            generalCount = i;
            int count = 0;

            for(int j = 0; j < SIZE; j++){
                if(arrayBoard[j][i].equals(input)){
                    count++;
                }
                else if(arrayBoard[j][i].equals(" ")){
                    spaceIndex = generalCount;
                }
                else{
                    spaceIndex = -1;
                }
                generalCount += 3;
            }

            if(count == 2 && spaceIndex != -1){
                return spaceIndex;
            }
        }

        return -1;
    }

    private int findWinningNumberDiagonal(String input){
        validTicTacToeBoardInputCheck(input);

        int generalCount = 0, count = 0, spaceIndex = 0;

        for(int i = 0; i < SIZE; i++){
            if(arrayBoard[i][i].equals(input)){
                count++;
            }
            else if(arrayBoard[i][i].equals(" ")){
                spaceIndex = generalCount;
            }
            else{
                spaceIndex = -1;
            }
            generalCount += 4;
        }

        if(count == 2 && spaceIndex != -1){
            return spaceIndex;
        }
        else{
            generalCount = 2;
            count = 0;
        }

        for(int i = 0; i < SIZE; i++){
            if(arrayBoard[i][2 - i].equals(input)){
                count++;
            }
            else if(arrayBoard[i][2 - i].equals(" ")){
                spaceIndex = generalCount;
            }
            else{
                spaceIndex = -1;
            }
            generalCount += 2;
        }

        return (count == 2) ? spaceIndex : -1;
    }

    private boolean hasWinningNumberRow(String input){
        return findWinningNumberRow(input) != -1;
    }

    private boolean hasWinningNumberColumn(String input){
        return findWinningNumberColumn(input) != -1;
    }

    private boolean hasWinningNumberDiagonal(String input){
        return findWinningNumberDiagonal(input) != -1;
    }

    private boolean isASideNum(int num){
        for(int i: SIDES){
            if(num == i){
                return true;
            }
        }

        return false;
    }

    private boolean isACornerNum(int num){
        for(int i: CORNERS){
            if(num == i){
                return true;
            }
        }

        return false;
    }

    private boolean numIsAvailable(int num){
        for(int i: values){
            if(i == num){
                return false;
            }
        }

        return true;
    }

    private boolean sideToSideNumAvailable(){
        for(int i: SIDES_TO_SIDE){
            if(numIsAvailable(i))
                return true;
        }

        return false;
    }

    private boolean sidesTopNumAvailable(){
        for(int i: SIDES_TOP){
            if(numIsAvailable(i))
                return true;
        }

        return false;
    }

    private boolean cornersAreAvailable(){
        int count = 0;
        for(int i: currentValues){
            if(isACornerNum(i)){
                count++;
            }
        }

        return count != 4;
    }
    //


    // Helper methods for computer moves
    private int getRandomCornerNum(){
        if(cornersAreAvailable()){
            int num;

            do{
                int x = random.nextInt(4);
                num = CORNERS[x];

            }while(currentValues.contains(num));

            return num;
        }

        throw new RuntimeException("Corner Numbers are not available");
    }

    private int getRandomNum(){
        if(currentValues.size() < 9){
            int num;

            do{
                num = random.nextInt(9);

            }while(currentValues.contains(num));

            return num;
        }

        throw new RuntimeException("Random numbers are not available");
    }

    private int generateRandomSideToSideNum(){
        if(sideToSideNumAvailable()){
            int num;

            do{
                int x = random.nextInt(2);
                num = SIDES_TO_SIDE[x];

            }while(currentValues.contains(num));

            return num;
        }

        throw new RuntimeException("Side to side numbers are not available");
    }

    private int generateRandomSideTopNum(){
        if(sidesTopNumAvailable()){
            int num;

            do{
                int x = random.nextInt(2);
                num = SIDES_TOP[x];

            }while(currentValues.contains(num));

            return num;
        }

        throw new RuntimeException("Side Top numbers are not available");
    }
    //


    // Game state method
    public int isSolved(){
        int count0 = 0;

        for(int i = 0; i < SIZE; i++) {
            int rowResult = determineCount(arrayBoard[i][0], arrayBoard[i][1], arrayBoard[i][2]);
            if(rowResult > 0){
                return rowResult;
            }
            else if(rowResult == 0){
                count0++;
            }

            int colResult = determineCount(arrayBoard[0][i], arrayBoard[1][i], arrayBoard[2][i]);
            if(colResult > 0){
                return colResult;
            }
            else if(colResult == 0){
                count0++;
            }
        }

        int diagResult = determineCount(arrayBoard[0][0], arrayBoard[1][1], arrayBoard[2][2]);
        if(diagResult > 0){
            return diagResult;
        }
        else if(diagResult == 0){
            count0++;
        }

        diagResult = determineCount(arrayBoard[0][2], arrayBoard[1][1], arrayBoard[2][0]);
        if(diagResult > 0){
            return diagResult;
        }
        else if(diagResult == 0){
            count0++;
        }

        if(count0 > 0){
            return -1;
        }

        return 0;
    }

    private int determineCount(String a, String b, String c){
        if(a.equals("O") && b.equals("O") && c.equals("O")){
            return 1;
        }
        else if(a.equals("X") && b.equals("X") && c.equals("X")){
            return 2;
        }
        else if(a.equals(" ") || b.equals(" ") || c.equals(" ")){
            return 0;
        }
        return -1;
    }
    //

    // Display board method
    private void displayBoard(){
        for(int i = 0; i < SIZE; i++){
            System.out.println("          | " + arrayBoard[i][0] + " | " + arrayBoard[i][1] + " | " + arrayBoard[i][2] + " |");
        }
    }

    // Player and computer messages
    private String playerMessage(int number, boolean vsComputer, int turn){
        if(vsComputer){
            return "\n Current board:\n";
        }
        else{
            return "\n Player " + turn + " choose: " + number + "\n Current board:\n";
        }
    }

    private String computerMessage(int number, boolean vsComputer, int turn, String difficulty){
        if(vsComputer){
            return "\n Computer " + turn + " (" + difficulty + ") choose: " + number + "\n";
        }
        else{
            return "\n Computer choose: " + number + "\n";
        }
    }

    // Input validation methods
    private boolean validTicTacToeChoice(String choice){
        return (choice.equals("0") || validAskToPlayChoice(choice)) && !currentValues.contains(Integer.parseInt(choice));
    }

    private boolean validAskToPlayChoice(String choice){
        return choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5")
                || choice.equals("6") || choice.equals("7") || choice.equals("8");
    }

    private int makeChoiceValid(String choiceGameStr, int play, boolean computer){
        while(!(choiceGameStr.equals("1") || choiceGameStr.equals("2"))){
            invalidInputDisplay();
            MyUtils.waitTimer(1);
            MyUtils.clearConsole();
            exampleBoard();
            System.out.println(currentDisplay);
            displayBoard();
            showResultForFinish(play);

            if(computer){
                askToPlayAgainComputer();
            }
            else{
                askToPlayAgain();
            }

            choiceGameStr = sc.nextLine();
        }

        return Integer.parseInt(choiceGameStr);
    }

    private void validTicTacToeBoardInputCheck(String input){
        if(!(input.equals("O") || input.equals("X"))){
            throw new IllegalArgumentException("Not a valid TicTacToeBoard input");
        }
    }
    //

    // Methods for interacting with players
    private void addToTicTacToeBoard(String str, int player){
        int choice;

        while(!validTicTacToeChoice(str)){
            invalidInputDisplay();
            MyUtils.waitTimer(1);
            MyUtils.clearConsole();
            exampleBoard();

            if(!currentDisplay.isBlank()){
                System.out.println(currentDisplay);
                displayBoard();
            }

            MyUtils.extraSpace(2);
            askForValue();
            str = sc.nextLine();
        }
        choice = Integer.parseInt(str);

        if(player == 1){
            playerInput(choice, false, 1);
        }
        else if(player == 2){
            playerInput(choice, false, 2);
        }
        else{
            playerInput(choice, true, 1);
        }

        MyUtils.waitTimer(1);
    }

    private void whichGameToPlay(){
        System.out.println("Which game do you wish to play?");
        System.out.print("                             Press 1 to play against the regular computer\n");
        System.out.print("                             Press 2 to play against the hard computer\n");
        System.out.print("                             Press 3 to play two player\n");
        System.out.print("                             Press 4 to see computer (easy) vs computer (easy)\n");
        System.out.print("                             Press 5 to see computer (easy) vs computer (hard)\n");
        System.out.print("                             Press 6 to see computer (hard) vs computer (easy)\n");
        System.out.print("                             Press 7 to see computer (hard) vs computer (hard)\n");
        System.out.print("                             Press 8 to quit\n... ");
    }

    private void askToPlayAgain(){
        System.out.println("Do you wish to play again?");
        System.out.print("                             Press 1 to play again\n");
        System.out.print("                             Press 2 to go back to main menu\n... ");
    }

    private void askToPlayAgainComputer(){
        System.out.println("Do you wish to see the computers play again?");
        System.out.print("                             Press 1 to see them play again\n");
        System.out.print("                             Press 2 to go back to main menu\n... ");
    }


    private void invalidInputDisplay(){
        System.out.println("\nInvalid input. Please enter a valid choice...");
    }

    private void askForValue(){
        System.out.print("Choose a value (0-8): ");
    }

    private void showResultForFinish(int play){
        if(play == 1){
            if(isSolved() == 1){
                System.out.println("\nCongratulations!!! You won\n");
            }
            else if(isSolved() == 2){
                System.out.println("\nYou lost...\n");
            }
            else{
                System.out.println("\nIt was a draw...\n");
            }
        }
        else if(play == 2){
            if(isSolved() == 1){
                System.out.println("\nPlayer 1 won!!!\n");
            }
            else if(isSolved() == 2){
                System.out.println("\nPlayer 2 won!!!\n");
            }
            else{
                System.out.println("\nIt was a draw...\n");
            }
        }
        else{
            if(isSolved() == 1){
                System.out.println("\nComputer 1 won!!!\n");
            }
            else if(isSolved() == 2){
                System.out.println("\nComputer 2 won!!!\n");
            }
            else{
                System.out.println("\nIt was a draw...\n");
            }
        }

    }
    //


    // Game modes
    public void computerVsPlayer(int difficulty){
        int choiceGame;

        do{
            MyUtils.clearConsole();
            exampleBoard();

            do{
                MyUtils.extraSpace(2);
                askForValue();
                String str = sc.nextLine();

                addToTicTacToeBoard(str, 3);

                if(isSolved() == -1){
                    if(difficulty == 1){
                        computerInputEasy("X", false, 2, true);
                    }
                    else{
                        computerInputMiniMax("X", true);
                    }
                }

            }while(isSolved() == -1);

            showResultForFinish(1);
            MyUtils.waitTimer(1);
            askToPlayAgain();
            String choiceGameStr = sc.nextLine();
            choiceGame = makeChoiceValid(choiceGameStr, 1, false);
            MyUtils.clearScreen();
            resetProgress();

        }while(choiceGame == 1);
    }

    public void playerVsPlayer(){
        int choiceGame;

        do{
            MyUtils.clearConsole();
            exampleBoard();
            int player = 1;

            do{
                int size = currentValues.size();
                MyUtils.extraSpace(2);
                askForValue();
                String str = sc.nextLine();
                addToTicTacToeBoard(str, player);

                if(currentValues.size() > size){
                    player = 2;
                    size++;
                }

                if(isSolved() == -1){
                    MyUtils.extraSpace(2);
                    askForValue();
                    String str2 = sc.nextLine();
                    addToTicTacToeBoard(str2, player);

                    if(currentValues.size() > size){
                        player = 1;
                    }
                }

            }while(isSolved() == -1);

            showResultForFinish(2);
            MyUtils.waitTimer(1);
            askToPlayAgain();
            String choiceGameStr = sc.nextLine();
            choiceGame = makeChoiceValid(choiceGameStr, 2, false);
            MyUtils.clearScreen();
            resetProgress();

        }while(choiceGame == 1);
    }

    public void computerVsComputer(String difficulty1, String difficulty2){
        int choiceGame;

        do{
            MyUtils.clearConsole();
            exampleBoard();
            MyUtils.waitTimer(1);

            do{
                if(difficulty1.equalsIgnoreCase("easy")){
                    computerInputEasy("O", true, 1, true);
                }
                else{
                    computerInputMiniMax("O", true);
                }

                MyUtils.waitTimer(1);

                if(isSolved() == -1){
                    if(difficulty2.equalsIgnoreCase("easy")){
                        computerInputEasy("X", true, 2, true);
                    }
                    else{
                        computerInputHard(true, true);
                    }

                    MyUtils.waitTimer(1);
                }

            }while(isSolved() == -1);

            showResultForFinish(3);
            MyUtils.waitTimer(1);
            askToPlayAgainComputer();
            String choiceGameStr = sc.nextLine();
            choiceGame = makeChoiceValid(choiceGameStr, 3, true);
            MyUtils.clearScreen();
            resetProgress();

        }while(choiceGame == 1);
    }
    //


    // TicTacToe game to play
    public void play(){
        MyUtils.clearConsole();
        System.out.println("""
                Welcome to Tywaine's TicTacToe!

                You will be playing as 'O' against the computer which will be represent as 'X'.
                Unless you select two player where player 1 is 'O' and player 2 is 'X'
                You can also see the different computer difficulties (easy or hard) play against each other.""");
        System.out.println("The board will be represented as:\n");
        System.out.println("\t| 0 | 1 | 2 |\n\t| 3 | 4 | 5 |\n\t| 6 | 7 | 8 |");
        System.out.println("\nYou will choose a value ranging from 0-8\n");
        MyUtils.pressEnterToContinue();
        String choice;

        do{
            MyUtils.clearConsole();
            whichGameToPlay();
            choice = sc.nextLine();

            while(!validAskToPlayChoice(choice)){
                invalidInputDisplay();
                MyUtils.waitTimer(1);
                MyUtils.clearConsole();
                whichGameToPlay();
                choice = sc.nextLine();
            }

            switch(choice){
                case "1" -> computerVsPlayer(1);
                case "2" -> computerVsPlayer(2);
                case "3" -> playerVsPlayer();
                case "4" -> computerVsComputer("easy", "easy");
                case "5" -> computerVsComputer("easy", "hard");
                case "6" -> computerVsComputer("hard", "easy");
                case "7" -> computerVsComputer("hard", "hard");
            }

        }while(!choice.equals("8"));

        sc.close();
        System.out.println("\nThank you for playing!!");
        MyUtils.waitTimer(1);
        MyUtils.extraSpace(5);
        System.out.println("Program will now close!");
        MyUtils.waitTimer(1);
        MyUtils.clearConsole();
        System.exit(0);
    }

    public static void main(String[] args){
        new TicTacToe().play();
    }

}