package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ListPaymentController extends Controller {

    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Vasarlas> vasarlasTable;
    @FXML
    private TableColumn<Vasarlas, String> nameCol;
    @FXML
    private TableColumn<Vasarlas, String> emailCol;
    @FXML
    private TableColumn<Vasarlas, Integer> arCol;
    @FXML
    private TableColumn<Vasarlas, Integer> pontokCol;

    @FXML
    private void initialize() {
        // getName() függvény eredményét írja ki
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        arCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        pontokCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        Platform.runLater(() -> {
            try {
                loadPeopleFromServer();
            } catch (IOException e) {
                error("Couldn't get data from server", e.getMessage());
                Platform.exit();
            }
        });
    }

    private void loadPeopleFromServer() throws IOException {
        Response response = RequestHandler.get(App.BASE_URL);
        String content = response.getContent();
        Gson converter = new Gson();
        Vasarlas[] vasarlas1 = converter.fromJson(content, Vasarlas[].class);
        vasarlasTable.getItems().clear();
        for (Vasarlas vasarlas : vasarlas1) {
            vasarlasTable.getItems().add(vasarlas);
        }
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("create-payment-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Create Payment");
            stage.setScene(scene);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnCloseRequest(event -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadPeopleFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        int selectedIndex = vasarlasTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a shopment from the list first");
            return;
        }
        Vasarlas selected = vasarlasTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("update-people-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Update Shopment");
            stage.setScene(scene);
            UpdatePaymentController controller= fxmlLoader.getController();
            controller.setPerson(selected);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnHidden(event -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadPeopleFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }

    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int selectedIndex = vasarlasTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a shopment from the list first");
            return;
        }

        Vasarlas selected = vasarlasTable.getSelectionModel().getSelectedItem();
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", selected.getName()));
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();
        if (optionalButtonType.isEmpty()) {
            System.err.println("Unknown error occurred");
            return;
        }
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)) {
            String url = App.BASE_URL + "/" + selected.getId();
            try {
                RequestHandler.delete(url);
                loadPeopleFromServer();
            } catch (IOException e) {
                error("An error occurred while communicating with the server");
            }
        }
    }
}