import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String url = scanner.nextLine();
        //String url = "https://target.com/index.html?pass=12345&port=8080&cookie=&host=localhost";
        boolean pass = false;
        String password = null;
        String[] values = url.split("\\?");
        String[] pairs = values[values.length - 1].split("&");
        
        for (String str : pairs) {
            String[] keyValue = str.split("=");
            String key = keyValue[0];
            String value = "not found";
            if (keyValue.length > 1) {
                value = keyValue[1];
            }
            
            if ("pass".equals(key)) {
                pass = true;
                password = new String(value);
            }
            
            System.out.println(key + " : " + value);
        }
        
        if (pass) {
            System.out.println("password : " + password);
        }
    }
}
