package event.reminder;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Meem
 */
public class AlertController implements Initializable {

    @FXML
    private TextField text;
    @FXML
    private Button ok_button;
    @FXML
    private Button cancel_button;
    private MainFXMLController mainFXMLController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleOKbutton(ActionEvent event) {
        /**
         * Checking if the string entered is "OK"
         */
        String st = text.getText();
        if(st.equals("OK")) mainFXMLController.ConfirmDeleteAll(true);
        Stage current_stage = (Stage) ok_button.getScene().getWindow();
        current_stage.close();
        
    }
  
    public void setMainFXMLController(MainFXMLController mainFXMLController) {
        this.mainFXMLController = mainFXMLController;
    }
    @FXML
    private void handleCancelbutton(ActionEvent event) {
        Stage current_stage = (Stage) cancel_button.getScene().getWindow();
        current_stage.close();
    }
    
    
}
