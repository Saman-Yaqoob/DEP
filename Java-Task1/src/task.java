import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class task extends Application {

    private InternDAO internDAO = new InternDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Intern Management System");

        // Create buttons for CRUD operations
        Button addInternButton = new Button("Add Intern");
        Button viewInternsButton = new Button("View Interns");
        Button updateInternButton = new Button("Update Intern");
        Button deleteInternButton = new Button("Delete Intern");

        // Style buttons
        String buttonStyle = "-fx-background-color: yellow; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold;";
        addInternButton.setStyle(buttonStyle);
        viewInternsButton.setStyle(buttonStyle);
        updateInternButton.setStyle(buttonStyle);
        deleteInternButton.setStyle(buttonStyle);

        // Set button actions
        addInternButton.setOnAction(e -> addIntern());
        viewInternsButton.setOnAction(e -> viewInterns());
        updateInternButton.setOnAction(e -> updateIntern());
        deleteInternButton.setOnAction(e -> deleteIntern());

        // Create a layout and add buttons to it
        VBox vbox = new VBox(20, addInternButton, viewInternsButton, updateInternButton, deleteInternButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: darkblue;");

        // Create a scene with the layout
        Scene scene = new Scene(vbox, 400, 300);

        // Set the scene and show the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addIntern() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Intern");
        dialog.setHeaderText("Enter intern details:");
        dialog.setContentText("Name, Email, Department:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(details -> {
            String[] parts = details.split(", ");
            if (parts.length == 3) {
                String name = parts[0];
                String email = parts[1];
                String department = parts[2];
                internDAO.addIntern(name, email, department);
                System.out.println("Added intern: " + name);
            } else {
                System.out.println("Invalid input format. Use: Name, Email, Department");
            }
        });
    }

    private void viewInterns() {
        List<Intern> interns = internDAO.getAllInterns();
        for (Intern intern : interns) {
            System.out.println(intern.getId() + ": " + intern.getName() + " - " + intern.getEmail() + " - " + intern.getDepartment());
        }
    }

    private void updateIntern() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Intern");
        dialog.setHeaderText("Enter intern ID and new details:");
        dialog.setContentText("ID, Name, Email, Department:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(details -> {
            String[] parts = details.split(", ");
            if (parts.length == 4) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String email = parts[2];
                String department = parts[3];
                internDAO.updateIntern(id, name, email, department);
                System.out.println("Updated intern with ID: " + id);
            } else {
                System.out.println("Invalid input format. Use: ID, Name, Email, Department");
            }
        });
    }

    private void deleteIntern() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Intern");
        dialog.setHeaderText("Enter intern ID to delete:");
        dialog.setContentText("ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            int id = Integer.parseInt(idStr);
            internDAO.deleteIntern(id);
            System.out.println("Deleted intern with ID: " + id);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}



//class to manage CRUD operations

 class InternDAO {

    // Method to add a new intern
    public void addIntern(String name, String email, String department) {
        String sql = "INSERT INTO interns (name, email, department) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseCollection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, department);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all interns
    public List<Intern> getAllInterns() {
        List<Intern> interns = new ArrayList<>();
        String sql = "SELECT * FROM interns";
        try (Connection conn = DatabaseCollection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Intern intern = new Intern();
                intern.setId(rs.getInt("id"));
                intern.setName(rs.getString("name"));
                intern.setEmail(rs.getString("email"));
                intern.setDepartment(rs.getString("department"));
                interns.add(intern);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interns;
    }

    // Method to update an intern's details
    public void updateIntern(int id, String name, String email, String department) {
        String sql = "UPDATE interns SET name = ?, email = ?, department = ? WHERE id = ?";
        try (Connection conn = DatabaseCollection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, department);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an intern by ID
    public void deleteIntern(int id) {
        String sql = "DELETE FROM interns WHERE id = ?";
        try (Connection conn = DatabaseCollection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


 
  class Intern {
	    private int id;
	    private String name;
	    private String email;
	    private String department;

	    // Getters and setters
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getDepartment() {
	        return department;
	    }

	    public void setDepartment(String department) {
	        this.department = department;
	    }
	}
