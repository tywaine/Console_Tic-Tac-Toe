import java.util.Scanner;

public interface MyUtils{
    static void pressEnterToContinue(){
        System.out.print("Press enter to continue...");
        try {
            // Use Scanner to capture any key press
            new Scanner(System.in).nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for pausing (seconds)
    static void waitTimer(int sec){
        try {
            for (int i = 0; i < sec; i++) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void pause(int milli){
        try {
            Thread.sleep(milli);

            } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void displayWaitTimer(int sec){
        try {
            for (int i = 0; i < sec; i++) {
                System.out.print(sec-i);
                Thread.sleep(1000);
                System.out.print("\033[2K\033[1G");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void extraSpace(int space){
        for (int i = 0; i < space; i++) {
            System.out.println();
        }
    }

    static void clearConsole() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("windows")) {
                // For Windows: use 'cls' command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();

            }
        } catch (Exception e) {
            // Fallback message if clearing console fails
            System.out.println("Unable to clear console. Error: " + e.getMessage());
        }
    }

}
