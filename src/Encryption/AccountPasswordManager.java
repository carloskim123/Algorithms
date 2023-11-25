package Encryption;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AccountPasswordManager {
    private static final String STORAGE_DIRECTORY = "user_accounts/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to Account Password Manager");
            System.out.println("1. Create an account");
            System.out.println("2. Login to an account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }

        System.out.println("Exiting Account Password Manager. Goodbye!");
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (userExists(username)) {
            System.out.println("Username already exists. Please choose another username.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        String hashedPassword = hashPassword(password);

        try {
            File userDirectory = new File(STORAGE_DIRECTORY + username);
            userDirectory.mkdir();

            File file = new File(userDirectory, "password.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(hashedPassword);
            fileWriter.close();

            File passwordFile = new File(userDirectory, "passwords.txt");
            passwordFile.createNewFile();

            System.out.println("Account created for " + username);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in creating account.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String hashedPassword = hashPassword(password);

        try {
            File passwordFile = new File(STORAGE_DIRECTORY + username + "/password.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(passwordFile));
            String storedHashedPassword = bufferedReader.readLine();
            bufferedReader.close();

            if (storedHashedPassword != null && storedHashedPassword.equals(hashedPassword)) {
                System.out.println("Login successful for " + username);
                managePasswords(username, scanner);
            } else {
                System.out.println("Incorrect username or password.");
            }
        } catch (IOException e) {
            System.out.println("Incorrect username or password.");
        }
    }

    private static void managePasswords(String username, Scanner scanner) {
        boolean managing = true;
        Map<String, String> passwords = loadPasswords(username);

        while (managing) {
            System.out.println("\nPassword Manager for " + username);
            System.out.println("1. Store a password");
            System.out.println("2. Retrieve a password");
            System.out.println("3. Back to main menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    storePassword(username, scanner, passwords);
                    break;
                case 2:
                    retrievePassword(scanner, passwords);
                    break;
                case 3:
                    savePasswords(username, passwords);
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static void storePassword(String username, Scanner scanner, Map<String, String> passwords) {
        System.out.print("Enter website/service name: ");
        String website = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        passwords.put(website, password);
        System.out.println("Password stored for " + website);
    }

    private static void retrievePassword(Scanner scanner, Map<String, String> passwords) {
        System.out.print("Enter website/service name: ");
        String website = scanner.nextLine();

        if (passwords.containsKey(website)) {
            String storedPassword = passwords.get(website);
            System.out.println("Password for " + website + ": " + storedPassword);
        } else {
            System.out.println("No password found for " + website);
        }
    }

    private static void savePasswords(String username, Map<String, String> passwords) {
        try {
            File passwordFile = new File(STORAGE_DIRECTORY + username + "/passwords.txt");
            FileWriter fileWriter = new FileWriter(passwordFile);
            for (Map.Entry<String, String> entry : passwords.entrySet()) {
                fileWriter.write(entry.getKey() + "|" + entry.getValue() + "\n");
            }
            fileWriter.close();
            System.out.println("Passwords saved for " + username);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in saving passwords.");
        }
    }

    private static Map<String, String> loadPasswords(String username) {
        Map<String, String> passwords = new HashMap<>();

        try {
            File passwordFile = new File(STORAGE_DIRECTORY + username + "/passwords.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(passwordFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    passwords.put(parts[0], parts[1]);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords;
    }

    private static boolean userExists(String username) {
        File file = new File(STORAGE_DIRECTORY + username);
        return file.exists();
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
