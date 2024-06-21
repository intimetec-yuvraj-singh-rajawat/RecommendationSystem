package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CafeteriaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread listenerThread = new Thread(new Runnable() {
                public void run() {
                    String serverMessage;
                    try {
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println("Server: " + serverMessage);
                            if (serverMessage.equals("LOGOUT SUCCESSFUL")) {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            listenerThread.start();

            System.out.println("Connected to the server.");

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            out.println("LOGIN " + username + " " + password);

            while (scanner.hasNextLine()) {
                String clientMessage = scanner.nextLine();

                if (clientMessage.equalsIgnoreCase("LOGOUT")) {
                    out.println(clientMessage);
                    break;
                } else if (clientMessage.equals("1")) {
                    out.println("View " + clientMessage);
                } else if (clientMessage.equals("2")) {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Is available (true/false): ");
                    boolean available = Boolean.parseBoolean(scanner.nextLine());
                    out.println("ADD " + name + " " + description + " " + price + " " + available);
                } else if (clientMessage.equals("3")) {
                    System.out.print("Enter ID of the menu item to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Is available (true/false): ");
                    boolean available = Boolean.parseBoolean(scanner.nextLine());
                    out.println("UPDATE " + id + " " + name + " " + description + " " + price + " " + available);
                } else if (clientMessage.equals("4")) {
                    System.out.print("Enter ID of the menu item to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    out.println("DELETE " + id);
                } else {
                    out.println(clientMessage);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
