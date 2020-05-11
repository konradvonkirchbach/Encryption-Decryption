import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        str = str.toLowerCase();
        int index = -1;
        
        for (int i = 0; i < str.length() - 2; i++) {
            if ("the".equals(str.substring(i, i + 3))) {
                index = i;
                break;
            }
        }
        
        System.out.println(index);
    }
}
