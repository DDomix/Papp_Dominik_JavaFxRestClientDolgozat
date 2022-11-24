package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdatePaymentController extends Controller {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private Spinner<Integer> vasarlasosszegField;
    @FXML
    private Spinner<Integer> husegpontokField;
    @FXML
    private Button updateButton;
    private Vasarlas vasarlas;


    public void setPerson(Vasarlas vasarlas) {
        this.vasarlas = vasarlas;
        nameField.setText((this.vasarlas.getName()));
        emailField.setText((this.vasarlas.getEmail()));
        vasarlasosszegField.getValueFactory().setValue((this.vasarlas.getValue()));
        husegpontokField.getValueFactory().setValue(this.vasarlas.getPoints());
    }

    @FXML
    private void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 50000);
        vasarlasosszegField.setValueFactory(valueFactory);
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory2 =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 250);
        husegpontokField.setValueFactory(valueFactory2);
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        int ar = vasarlasosszegField.getValue();
        int pontok=husegpontokField.getValue();
        if (name.isEmpty()) {
            warning("Name is required");
            return;
        }
        if (email.isEmpty()) {
            warning("Email is required");
            return;
        }
        // TODO: validate email format
        this.vasarlas.setName(name);
        this.vasarlas.setEmail(email);
        this.vasarlas.setValue(ar);
        this.vasarlas.setPoints(pontok);
        Gson converter = new Gson();
        String json = converter.toJson(this.vasarlas);
        try {
            String url= App.BASE_URL+ "/" +this.vasarlas.getId();
            Response response = RequestHandler.put(url, json);
            if (response.getResponseCode() == 200) {
                Stage stage =(Stage) this.updateButton.getScene().getWindow();
                stage.close();
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
