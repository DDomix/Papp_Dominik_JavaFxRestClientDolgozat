package hu.petrik.peoplerestclientjavafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class CreatePaymentController extends Controller {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private Spinner<Integer> vasarlasosszegField;
    @FXML
    private Spinner<Integer> husegpontokField;
    @FXML
    private Button submitButton;

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
    public void submitClick(ActionEvent actionEvent) {
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
        Vasarlas vasarlas = new Vasarlas(0,name,email,ar,pontok);
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = converter.toJson(vasarlas);
        try {
            Response response = RequestHandler.post(App.BASE_URL, json);
            if (response.getResponseCode() == 201) {
                warning("Shopping added!");
                nameField.setText("");
                emailField.setText("");
                vasarlasosszegField.getValueFactory().setValue(30000);
                husegpontokField.getValueFactory().setValue(250);

            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
