package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import Service.AdminService;

public class AdminController {
    private PrintWriter out;
    private BufferedReader in;
    private AdminService adminService;

    public AdminController(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
        adminService = new AdminService();
    }

    public void processCommand(String command) throws IOException {
        System.out.println("AdminController received command: " + command);
        if (command.startsWith("Admin_VIEW")) {
            try {
                adminService.viewAllMenuItems(out);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (command.startsWith("Admin_ADD")) {
            try {
                String response = adminService.handleAddMenuItem(command);
                System.out.println("Admin_ADD response: " + response);
                out.println(response);
                out.flush();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else if (command.startsWith("Admin_UPDATE")) {
            try {
                adminService.handleUpdateMenuItem(command, out);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else if (command.startsWith("Admin_DELETE")) {
            try {
                adminService.handleDeleteMenuItem(command, out);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            out.println("Unknown admin command");
            out.flush();
        }
    }
}
