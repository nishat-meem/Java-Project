package event.reminder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Meem
 */
public class MainFXMLController implements Initializable {

    @FXML
    private Button delete_all;
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> nameColumn;
    @FXML
    private TableColumn<Data, String> dateColumn;
    @FXML
    private TableColumn<Data, String> remindColumn;

    private Main main;

    @FXML
    private TextField event_name;
    @FXML
    private DatePicker dp;
    @FXML
    private TextField remind_hour;
    @FXML
    private TextField remind_minute;
    @FXML
    private TextField remind_date;
    @FXML
    private TextField remind_month;
    @FXML
    private Button export_button;
    @FXML
    private Button aboutme_button;
    @FXML
    private Button addevent_button;
    @FXML
    private Button import_button;
    @FXML
    private Button delete_button;
    private AlertController alertController;
    private AlarmFXMLController alarmFXMLController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("event_name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("event_at"));
        remindColumn.setCellValueFactory(new PropertyValueFactory<>("remind_me_at"));
    }

    @FXML
    private void handleAboutMeButton(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AboutMefxml.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("About Me");
        stage.show();
    }

    @FXML
    private void handleAddEventButton(ActionEvent event) throws Exception {
        
        /**
         * getting data from input fields when add button is clicked
         * and writing it to .csv file.
         */
   
        Data data = new Data();
        data.setEvent_name(event_name.getText());

        String st = dp.getEditor().getText();
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = outputFormat.format(inputFormat.parse(st));
        data.setEvent_at(formattedDate);
            
        st = remind_hour.getText() + ":" + remind_minute.getText() + " " + remind_date.getText() + "-" + remind_month.getText();
        inputFormat = new SimpleDateFormat("HH:mm dd-MM");
        outputFormat = new SimpleDateFormat("dd MMMM hh:mm a");
        formattedDate = outputFormat.format(inputFormat.parse(st));
        data.setRemind_me_at(formattedDate);

        data.setDatePassed(Integer.parseInt(remind_month.getText()), (Integer.parseInt(remind_date.getText())));

        st = data.getEvent_name() + "," + data.getEvent_at() + "," + data.getRemind_me_at() + "," + data.isDatePassed() + "\n";
        WriteinFile(st);
        
        /**
         * starting alarm thread for new event
         */
        try {
            AlarmClock a = new AlarmClock(data.getRemind_me_at());
            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    a.CALL();
                    done();
                    return null;
                }
            };
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    try {
                        /**
                         * if alarm starts, creating new stage for alarm
                         */
                        FXMLLoader fXMLLoader = new FXMLLoader(Main.class.getResource("AlarmFXML.fxml"));
                        fXMLLoader.load();
                        alarmFXMLController = fXMLLoader.getController();
                        alarmFXMLController.setMsg(data.getEvent_name(), data.getEvent_at());
                        Scene scene = new Scene(fXMLLoader.getRoot());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Reminder!!");
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        /**
         * clearing input fields when add button is clicked
         */
        table.getItems().add(data);
        event_name.clear();
        dp.getEditor().clear();
        remind_date.clear();
        remind_month.clear();
        remind_hour.clear();
        remind_minute.clear();
    }

    public static void WriteinFile(String st) {
        try {
            File file = new File("data.csv");
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(st);
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showinTable(Main main) {
        this.main = main;
        table.setItems(main.getEventList());
    }

    @FXML
    private void handleDeleteAllbutton(ActionEvent event) throws IOException, InvocationTargetException {
        FXMLLoader fXMLLoader = new FXMLLoader(MainFXMLController.class.getResource("Alert.fxml"));
        fXMLLoader.load();
        alertController = fXMLLoader.getController();
        alertController.setMainFXMLController(this);
        Scene scene = new Scene(fXMLLoader.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Sure?");
        stage.show();
    }

    public void ConfirmDeleteAll(boolean flag) {
        /**
         * to delete all events, deleting ta .csv file and clearing the tableview
         */
        if (flag) {
            ObservableList<Data> allData;
            allData = table.getItems();
            allData.clear();
            try {
                File file = new File("data.csv");
                file.delete();
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                bw.write("Event Name, Event Date, Remind Me At, DatePassed\n");
                System.out.println("Done");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void handleDeletebutton(ActionEvent event) {
        /**
         * to delete some particular events from table, deleting them from table and then keeping rest of the events in a observable list
         * thn deleting previous .csv file and writing data from observable list to a new .csv file
         */
        ObservableList<Data> DataSelected, allData;
        allData = table.getItems();
        DataSelected = table.getSelectionModel().getSelectedItems();
        allData.removeAll(DataSelected);
        BufferedWriter bw = null;
        try {
            File file = new File("data.csv");
            file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            bw = new BufferedWriter(fileWriter);
            bw.write("Event Name, Event Date, Remind Me At, DatePassed\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (Data data : allData) {
                String st = data.getEvent_name() + "," + data.getEvent_at() + "," + data.getRemind_me_at() + "," + data.isDatePassed() + "\n";
                bw.write(st);
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
