package encryptdecrypt;
import kotlin.ExceptionsKt;
import kotlin.text.StringsKt;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

class Config {
    private String mode = "enc";
    private String inputFile = null;
    private String outputFile = null;
    private String algorithm = "shift";
    private String data = null;
    private Integer key  = 0;

    public Config(String[] args) {
        for (int i = 0; i < args.length - 1;) {
            switch (args[i]) {
                case "-mode":
                    this.mode = args[i + 1];
                    i += 2;
                    break;
                case "-data":
                    ++i;
                    this.data = "";
                    while (i < args.length && !"-mode".equals(args[i]) && !"-key".equals(args[i])
                            && !"-in".equals(args[i]) && !"-out".equals(args[i]) && !"-alg".equals(args[i])) {
                        this.data += args[i];
                        if (i < args.length - 1 && !"-mode".equals(args[i + 1]) && !"-key".equals(args[i + 1]))
                            this.data += " ";
                        ++i;
                    }
                    break;
                case "-key":
                    this.key = StringsKt.toIntOrNull(args[i + 1]);
                    i += 2;
                    break;
                case "-in":
                    this.inputFile = args[i + 1];
                    i += 2;
                    break;
                case "-out":
                    this.outputFile = args[i + 1];
                    i += 2;
                    break;
                case "-alg":
                    this.algorithm = args[i + 1];
                    i += 2;
                    break;
            }
        }
    }

    public String getMode() {
        return mode;
    }

    public String getData() {
        return data;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Integer getKey() {
        return key;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}

class IOHandler {
    private Config config;

    public IOHandler(Config config) {
        this.config = config;
    }

    private String readFromFile(String filename) {
        String content = "";
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                content += scanner.nextLine();
                content += "\n";
            }
        } catch (Exception e) {
            System.out.println("Error while reading file. " + e.getMessage());
        }
        return content;
    }

    private void writeToFile(String filename, String stringToWrite) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(stringToWrite);
        } catch (Exception e) {
            System.out.println("Error while writing to file. " + e.getMessage());
        }
    }

    public String getMessage() throws IOException {
        if (config.getData() != null) {
            return config.getData();
        } else if (config.getInputFile() != null) {
            return readFromFile(config.getInputFile());
        } else {
            throw new IOException();
        }
    }

    public void writeMessage(String message) throws IOException {
        if (config.getOutputFile() != null) {
            writeToFile(config.getOutputFile(), message);
        } else {
            System.out.println(message);
        }
    }
}

interface EncryptionDecryption {
    String encrypt(String message);
    String decrypt(String message);

    default String run(String mode, String message) {
        switch (mode) {
            case "enc":
                return encrypt(message);
            case "dec":
                return decrypt(message);
            default:
                return null;
        }
    }
}

class UnicodeCryptor implements EncryptionDecryption {
    private Integer key;

    public UnicodeCryptor(Config config) {
        this.key = config.getKey();
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }

    @Override
    public String encrypt(String message) {
        String encrypted = "";
        for (int i=0; i < message.length(); i++) {
            if ("\n".equals(message.charAt(i))) {
                encrypted += '\n';
                continue;
            }
            int val = message.charAt(i) + key;
            encrypted += (char) (val % 128);
        }
        return encrypted;
    }

    @Override
    public String decrypt(String message) {
        String decrypted = "";
        for (int i = 0; i < message.length(); i++) {
            if ("\n".equals(message.charAt(i))) {
                decrypted += message.charAt(i);
                continue;
            }
            int val = message.charAt(i) - key;
            val = val < 0 ? 127 + (val % 128) : val;
            decrypted += (char) val;
        }
        return decrypted;
    }
}

class ShiftCryptor implements EncryptionDecryption {
    private Integer key;

    public ShiftCryptor(Config config) {
        this.key = config.getKey();
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String encrypt(String message) {
        String encrypted = "";
        for (int i=0; i < message.length(); i++) {
            int c = (int) message.charAt(i);
            if (c > 64 && c < 91) {
                c = (c + key.intValue());
                c = c < 65 ? 91 - (65 - c) : c;
                c = c > 90 ? 64 + (c - 90) : c;
                encrypted += (char) c;
                continue;
            }
            if (c > 96 && c < 123) {
                c = (c + key.intValue());
                c = c < 97 ? 123 - (96 - c) : c;
                c = c > 122 ? 96 + (c - 122) : c;
                encrypted += (char) c;
                continue;
            }
            encrypted += (char) message.charAt(i);
        }
        return encrypted;
    }

    @Override
    public String decrypt(String message) {
        String decrypted = "";
        for (int i = 0; i < message.length(); i++) {
            int c = (int) message.charAt(i);
            if (c > 64 && c < 91) {
                c = (c - key.intValue()) % 127 ;
                c = c < 65 ? 91 - (65 - c) : c;
                c = c > 90 ? 64 + (c - 90) : c;
                decrypted += (char) c;
                continue;
            }
            if (c > 96 && c < 123) {
                c = (c - key.intValue()) % 127;
                c = c < 97 ? 123 - (97 - c) : c;
                c = c > 122 ? 96 + (c - 122) : c;
                decrypted += (char) c;
                continue;
            }
            decrypted += message.charAt(i);
        }
        return decrypted;
    }
}

class CryptorFactory {
    private Config config;
    private String alg = null;

    public CryptorFactory(Config config) {
        this.config = config;
    }

    public CryptorFactory(String alg) {
        this.alg = alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public EncryptionDecryption getEncryptorDecryptor() {
        String algorithm;
        if (alg == null) {
            algorithm = config.getAlgorithm();
        } else {
            algorithm = alg;
        }
        switch (algorithm) {
            case "unicode":
                return new UnicodeCryptor(config);
            case "shift":
                return new ShiftCryptor(config);
            default:
                return new UnicodeCryptor(config);
        }
    }
}

class EncryptorDecryptorHandler {
    private Config config;
    private CryptorFactory cryptorFactory;
    private IOHandler ioHandler ;

    public EncryptorDecryptorHandler(Config config) {
        this.config = config;
        cryptorFactory = new CryptorFactory(this.config);
        ioHandler = new IOHandler(this.config);
    }

    public void run() {
        EncryptionDecryption cryptionDevice = cryptorFactory.getEncryptorDecryptor();
        String inMessage = null;
        try{
            inMessage = ioHandler.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        String outMessage = cryptionDevice.run(config.getMode(), inMessage);;
        try {
            ioHandler.writeMessage(outMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String[] testArgs = {"-alg", "shift", "-mode", "enc", "-key", "26", "-in", "in.txt", "-out", "output.txt"};
        Config config = new Config(args);
        EncryptorDecryptorHandler handler = new EncryptorDecryptorHandler(config);
        handler.run();
    }
}
