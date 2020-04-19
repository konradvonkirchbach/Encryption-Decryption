package encryptdecrypt;

public class Main {
    public static String shift(String s){
        String encrypted = "";
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            char x;
            if (c != ' ' && c != '!') {
                encrypted += (char)('a' + 'z' - c);
            }
            else {
                encrypted += c;
            }
        }    
        return encrypted;
    }
    
    public static void main(String[] args) {
        String message = "we found a treasure!";
        message = shift(message);
        System.out.println(message);
    }
}
