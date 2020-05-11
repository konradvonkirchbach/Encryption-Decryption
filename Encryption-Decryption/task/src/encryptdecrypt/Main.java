package encryptdecrypt;
import java.util.Scanner;

public class Main {
    public static String encryption(String s, int key){
        String encrypted = "";
        for (int i=0; i < s.length(); i++) {
            int val = s.charAt(i) + key;
            encrypted += (char) (val % 128);
        }    
        return encrypted;
    }

    public static String decryption(String s, int key) {
        String decrypted = "";
        for (int i = 0; i < s.length(); i++) {
            int val = s.charAt(i) - key;
            val = val < 0 ? 127 + (val % 128) : val;
            decrypted += (char) val;
        }
        return decrypted;
    }
    
    public static void main(String[] args) {
        /*
        String message = "Welcome to hyperskill!";
        int key = 5;
        String mod= "enc";
        /**/
        Scanner scanner = new Scanner(System.in);
        String mod = scanner.nextLine();
        String message = scanner.nextLine();
        int key = scanner.nextInt();
        if ("dec".equals(mod)) {
            System.out.println(decryption(message, key));
        } else if ("enc".equals(mod)) {
            System.out.println(encryption(message, key));
        }
    }
}
