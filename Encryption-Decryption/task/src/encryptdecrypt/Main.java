package encryptdecrypt;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;

public class Main {
    public static String encryption(String s, int key){
        String encrypted = "";
        for (int i=0; i < s.length(); i++) {
            if ("\n".equals(s.charAt(i))) {
                //encrypted += s.charAt(i);
                continue;
            }
            int val = s.charAt(i) + key;
            encrypted += (char) (val % 128);
        }    
        return encrypted;
    }

    public static String decryption(String s, int key) {
        String decrypted = "";
        for (int i = 0; i < s.length(); i++) {
            if ("\n".equals(s.charAt(i))) {
                decrypted += s.charAt(i);
                continue;
            }
            int val = s.charAt(i) - key;
            val = val < 0 ? 127 + (val % 128) : val;
            decrypted += (char) val;
        }
        return decrypted;
    }

    public static String readFromFile(String filename) {
        String content = "";
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                content += scanner.nextLine();
                //content += "\n";
            }
        } catch (Exception e) {
            System.out.println("Error while reading file. " + e.getMessage());
        }
        return content;
    }

    public static void writeToFile(String filename, String stringToWrite) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(stringToWrite);
        } catch (Exception e) {
            System.out.println("Error while writing to file. " + e.getMessage());
        }
    }
    
    public static String[] configuration(String[] args) {
        //values are: mode, data, key, in, out
        String[] values = {"enc", "", "0", "", ""};
        
        for (int i = 0; i < args.length - 1;) {
            switch (args[i]) {
                case "-mode":
                    values[0] = args[i + 1];
                    i += 2;
                    break;
                case "-data":
                    ++i;
                    while (i < args.length && !"-mode".equals(args[i]) && !"-key".equals(args[i])
                            && !"-in".equals(args[i]) && !"-out".equals(args[i])) {
                        values[1] += args[i];
                        if (i < args.length - 1 && !"-mode".equals(args[i + 1]) && ! !"key".equals(args[i + 1]))
                            values[1] += " ";
                        ++i;
                    } 
                    break;
                case "-key":
                    values[2] = args[i + 1];
                    i += 2;
                    break;
                case "-in":
                    values[3] = args[i + 1];
                    i += 2;
                    break;
                case "-out":
                    values[4] = args[i + 1];
                    i += 2;
                    break;
            }
        }
        
        return values;
    }

    public static void modeSelection(String mod, String data, String in, String out, int key) {
        String message = data;
        if (!"".equals(in)) {
            message = readFromFile(in);
        }

        switch(mod) {
            case "dec":
                message = decryption(message, key);
                break;
            case "enc":
                message = encryption(message, key);
                break;
            default:
                message = decryption(message, key);
        }

        if (!"".equals(out)) {
            writeToFile(out, message);
        } else {
            System.out.println(message);
        }
    }
    
    public static void main(String[] args) {
        /*
        String message = "Welcome to hyperskill!";
        int key = 5;
        String mod= "enc";
        /*
        Scanner scanner = new Scanner(System.in);
        String mod = scanner.nextLine();
        String message = scanner.nextLine();
        int key = scanner.nextInt();
        */
        String[] values = configuration(args);
        
        String mod = values[0];
        String message = values[1];
        int key = Integer.parseInt(values[2]);
        String in = values[3];
        String out = values[4];

        modeSelection(mod, message, in, out, key);
    }
}
