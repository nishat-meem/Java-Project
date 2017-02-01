package event.reminder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Meem
 */
public class Main extends Application {

    private static Stage primaryStage;
    private static MainFXMLController mainFXMLController;
    private static AlarmClock alarmclock;
    private AlarmFXMLController alarmFXMLController;
    private ObservableList<Data> eventList = FXCollections.observableArrayList();
    public String Msg1, Msg2;
    BufferedReader br = null;

    public Main() throws Exception {

        File file = new File("data.csv");
        if (!file.exists()) {
            file.createNewFile();
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                bw.write("Event Name, Event Date, Remind Me At, DatePassed\n");
                bw.write("Meem's Birthday, 08 July 1994, 07 July 11:57 PM\n");
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * reading data from .csv file to observable list
         */
        try {
            br = new BufferedReader(new FileReader(new File("data.csv")));
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                Data data = new Data();
                data.setEvent_name(arr[0]);
                data.setEvent_at(arr[1]);
                data.setRemind_me_at(arr[2]);
                eventList.add(data);

                this.Msg1 = arr[0];
                this.Msg2 = arr[1];
                /**
                 * starting alarm thread for every data of .csv file
                 */
                try {
                    AlarmClock a = new AlarmClock(arr[2]);
                    Task<Void> task = new Task<Void>() {
                        @Override
                        public Void call() throws Exception {
                            a.CALL();
                            done();
                            //System.out.println("doneeeee");
                            return null;
                        }
                    };
                    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                   
                            /**
                             * if alarm starts, creating new stage for alarm
                             */
                            try {
                                FXMLLoader fXMLLoader = new FXMLLoader(Main.class.getResource("AlarmFXML.fxml"));
                                fXMLLoader.load();
                                alarmFXMLController = fXMLLoader.getController();
                                alarmFXMLController.setMsg(Msg1, Msg2);
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
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        /**
         * loading main stage
         */
        this.primaryStage = stage;
        FXMLLoader fXMLLoader = new FXMLLoader(Main.class.getResource("MainFXML.fxml"));
        fXMLLoader.load();
        Scene scene = new Scene(fXMLLoader.getRoot());
        mainFXMLController = fXMLLoader.getController();
        mainFXMLController.showinTable(this);
        stage.setScene(scene);
        stage.setTitle("Event Reminder");
        stage.setResizable(false);
        stage.show();

    }

    public ObservableList<Data> getEventList() {
        return eventList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
