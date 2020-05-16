package app;

import dao.FileOperatorDAO;
import task.DBInit;
import task.FileOperateTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import task.FileScanCallback;
import task.FileScanTask;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane rootPane; //选择目录

    @FXML
    private TextField searchField; //搜索框

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory; // 标签


    private Thread t = new Thread();

    public void initialize(URL location, ResourceBundle resources) {
        DBInit.init();
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //freshTable(newValue);
                freshTable();
            }
        });
    }

    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();

        if(t != null) { // 线程执行完毕，t是否为空

            t.interrupt();
        }
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    FileScanCallback callback = new FileOperateTask();
                    FileScanTask task = new FileScanTask(callback);
                    task.startScan(file);
                    task.waitFinish();
                    System.out.println("执行完毕");
                    //freshTable(srcDirectory.getText());
                    freshTable();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }

    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();

        List<FileMeta> dates = FileOperatorDAO.search(srcDirectory.getText(),
                searchField.getText());

        metas.addAll(dates);

        // TODO
    }

}
