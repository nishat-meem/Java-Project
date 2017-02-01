package event.reminder;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * FXML Controller class
 *
 * @author Meem
 */
public class AlarmFXMLController implements Initializable {

    @FXML
    private Label event_name;
    @FXML
    private Label event_date;
    @FXML
    private Button ok_button;
    private String Msg1, Msg2;
    Main main;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Playing sound for alarm
         */
        try {
            Media media = new Media(new File("C:\\Users\\lenovo_pc\\Documents\\NetBeansProjects\\Event Reminder\\src\\event\\reminder\\alert_sound.mp3").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            MediaView mediaView = new MediaView(mediaPlayer);
        } catch (Exception e) {
        }
    }

    public void setMsg(String Msg1, String Msg2) {
        event_name.setText(Msg1);
        event_date.setText(Msg2);
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        Stage stage = (Stage) ok_button.getScene().getWindow();
        stage.close();
    }

}
