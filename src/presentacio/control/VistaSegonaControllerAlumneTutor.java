package presentacio.control;

import presentacio.control.VistaNavigator;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import bll.Coordina2;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import pojo.AlumneTutor;
import pojo.exceptions.ArgumentErroniException;

public class VistaSegonaControllerAlumneTutor implements Initializable {
    
	@FXML private TableColumn<AlumneTutor, String> taulaDNI;
    @FXML private TableColumn<AlumneTutor, String> taulaNom;
    @FXML private TableColumn<AlumneTutor, String> taulaCognoms;
    @FXML private TableColumn<AlumneTutor, String> taulaCorreuUPV;
    @FXML private TableView<AlumneTutor> taula;
    @FXML private TextField barraBuscadora;
    @FXML private Button afegirAT, editarAT, esborrarAT, botoenrere;
	Coordina2 cd2 = Coordina2.getInstancia();
	private ArrayList<AlumneTutor> alumnetutor = cd2.llistarAlumnesTutors();
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	ObservableList<AlumneTutor> altuts = FXCollections.observableArrayList(alumnetutor);
    	
    	/* Plenar les columnes */
    	taulaDNI.setCellValueFactory(param -> new ReadOnlyObjectWrapper <>((param.getValue()).getNif()));
    	taulaNom.setCellValueFactory(param -> new ReadOnlyObjectWrapper <> ((param.getValue()).getNom()));
    	taulaCognoms.setCellValueFactory(param -> new ReadOnlyObjectWrapper <> ((param.getValue()).getCognoms()));
    	taulaCorreuUPV.setCellValueFactory(param -> new ReadOnlyObjectWrapper <> (param.getValue().getCorreu_upv()));
    	taula.setItems(altuts);
    	
    	botoenrere.setOnMouseEntered( ev -> {
    		botoenrere.setVisible(true);
    	});

    	/* Listener de la barra de filtratge */
    	barraBuscadora.textProperty().addListener((ob, vell, nou) -> {filtratge(nou);});
    	
    	/* Tooltips */
    	Tooltip tp1 = new Tooltip();
    	tp1.setText("Afegir alumne tutor");
    	afegirAT.setTooltip(tp1);
    	Tooltip tp2 = new Tooltip();
    	tp2.setText("Editar alumne tutor");
    	editarAT.setTooltip(tp2);
    	Tooltip tp3 = new Tooltip();
    	tp3.setText("Esborrar alumne tutor");
    	esborrarAT.setTooltip(tp3);
    	
		/* Listeners de les opcions CRUD */
		afegirAT.setOnAction((event) -> {afegirAlumneTutor();});
		editarAT.setOnAction((event) -> {editarAlumneTutor();});
		esborrarAT.setOnAction((event) -> {esborrarAlumneTutor();});
    }
        
    @FXML public void enrere(){VistaNavigator.loadVista(VistaNavigator.VISTAINI);}
    
    @FXML public void afegirAlumneTutor(){
    	Dialog<AlumneTutor> dialog = new Dialog<>();
    	dialog.setTitle("Afegir Alumne Tutor");
    	dialog.setHeaderText(null);
    	DialogPane dp = dialog.getDialogPane();
    	dp.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
    	dialog.setResizable(true);
    	Label lb1 = new Label("DNI:");
    	Label lb2 = new Label("Nom:");
    	Label lb3 = new Label("Cognoms:");
    	Label lb4 = new Label("Correu UPV:");
    	TextField tx1 = new TextField();
    	TextField tx2 = new TextField();
    	TextField tx3 = new TextField();
    	TextField tx4 = new TextField();
    	GridPane grid = new GridPane();
    	grid.add(lb1, 1, 1);
    	grid.add(lb2, 1, 2);
    	grid.add(lb3, 1, 3);
    	grid.add(lb4, 1, 4);
    	grid.add(tx1, 2, 1);
    	grid.add(tx2, 2, 2);
    	grid.add(tx3, 2, 3);
    	grid.add(tx4, 2, 4);
    	grid.setHgap(1);
    	grid.setVgap(1);
    	dialog.getDialogPane().setContent(grid);
    	ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    	dialog.setResultConverter(new Callback<ButtonType, AlumneTutor>() {
    		@Override
    		public AlumneTutor call(ButtonType b){
    			if(b == buttonTypeOk){
    				return new AlumneTutor(tx1.getText(), tx2.getText(), 
    						tx3.getText(), tx4.getText());
    			}
    			return null;
    		} 	
    	});    	
    	Optional<AlumneTutor> result = dialog.showAndWait();
    	if(result.isPresent()){
    		try {
				cd2.afegirAlumneTutor(result.get());
				alumnetutor = cd2.llistarAlumnesTutors();
				ObservableList<AlumneTutor> altu = FXCollections.observableArrayList(alumnetutor);
				taula.setItems(altu);
				
			} catch (ArgumentErroniException e) {
				e.printStackTrace();
			}
    	}

    }
    
    @FXML public void editarAlumneTutor(){
    	if(taula.getSelectionModel().getSelectedItem() == null){
    		Alert al = new Alert (AlertType.WARNING);
        	DialogPane dp = al.getDialogPane();
        	dp.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
    		al.setTitle("Atencio!");
    		al.setHeaderText("Seleccione un element a editar");
    		al.setContentText(null);
    		al.showAndWait();
    	} else {
    		Dialog<AlumneTutor> dialog = new Dialog<>();
        	DialogPane dp = dialog.getDialogPane();
        	dp.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
    		AlumneTutor aux = taula.getSelectionModel().getSelectedItem();
        	dialog.setTitle("Editar alumne tutor");
        	dialog.setHeaderText(null);
        	dialog.setResizable(true);
        	Label lb1 = new Label("DNI:");
        	Label lb2 = new Label("Nom:");
        	Label lb3 = new Label("Cognoms:");
        	Label lb4 = new Label("Correu UPV:");
        	TextField tx1 = new TextField(aux.getNif());
        	tx1.setEditable(false);
        	tx1.setDisable(true);
        	TextField tx2 = new TextField(aux.getNom());
        	TextField tx3 = new TextField(aux.getCognoms());
        	TextField tx4 = new TextField(aux.getCorreu_upv());
        	GridPane grid = new GridPane();
        	grid.add(lb1, 1, 1);
        	grid.add(lb2, 1, 2);
        	grid.add(lb3, 1, 3);
        	grid.add(lb4, 1, 4);
        	grid.add(tx1, 2, 1);
        	grid.add(tx2, 2, 2);
        	grid.add(tx3, 2, 3);
        	grid.add(tx4, 2, 4);
        	grid.setHgap(1);
        	grid.setVgap(1);
        	dialog.getDialogPane().setContent(grid);
        	ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        	dialog.setResultConverter(new Callback<ButtonType, AlumneTutor>() {
        		@Override
        		public AlumneTutor call(ButtonType b){
        			if(b == buttonTypeOk){
        				return new AlumneTutor(tx1.getText(), tx2.getText(), 
        						tx3.getText(), tx4.getText());
        			}
        			return null;
        		} 	
        	});    	
        	Optional<AlumneTutor> result = dialog.showAndWait();
        	
        	if(result.isPresent()){
				try {
					cd2.editarAlumneTutor(result.get());
					alumnetutor = cd2.llistarAlumnesTutors();
					ObservableList<AlumneTutor> altu = FXCollections.observableArrayList(alumnetutor);
					taula.setItems(altu);
					taula.getColumns().get(0).setVisible(false);
					taula.getColumns().get(0).setVisible(true);
				} catch (ArgumentErroniException e) {
					e.printStackTrace();
				}    			
        	}
    	}
    }
    
    @FXML public void esborrarAlumneTutor(){
    	if(taula.getSelectionModel().getSelectedItem() == null){
    		Alert al = new Alert (AlertType.WARNING);
        	DialogPane dp = al.getDialogPane();
        	dp.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
    		al.setTitle("Atencio!");
    		al.setHeaderText("Seleccione un element a esborrar");
    		al.setContentText(null);
    		al.showAndWait();
    	} else {
    		Alert al = new Alert (AlertType.WARNING);
        	DialogPane dp = al.getDialogPane();
        	dp.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
    		al.setTitle("Atenci" + "\u00f3" + "!"); //Intent de simbols en unicode
    		al.setHeaderText("Est" + "\00e0" + " segur que vol esborrar l'element?");
    		al.setContentText(null);
    		Optional<ButtonType> result = al.showAndWait();
    		if(result.isPresent() && result.get() == ButtonType.OK){
    			try{
    				cd2.borrarAlumneTutor(taula.getSelectionModel().getSelectedItem());
    				alumnetutor = cd2.llistarAlumnesTutors();
    				ObservableList<AlumneTutor> altu = FXCollections.observableArrayList(alumnetutor);
    				taula.setItems(altu);
    			} catch (ArgumentErroniException e){
    				e.printStackTrace();
    			}
    			
    		}
    	}
    }
    
    public void filtratge(String nou){
    	ObservableList<AlumneTutor> at = FXCollections.observableArrayList(alumnetutor);
    	FilteredList<AlumneTutor> filteredData = new FilteredList<>(at, p -> true);
    	filteredData.setPredicate(altuu -> {
			if (nou == null || nou.isEmpty()) {
				return true;
			}
			String minuscules = nou.toLowerCase();
			if (altuu.getNom().toLowerCase().contains(minuscules)
					|| altuu.getNif().toLowerCase().contains(minuscules) 
					|| altuu.getCognoms().toLowerCase().contains(minuscules)) {
				return true;
			}	
			return false;
		});
    	SortedList<AlumneTutor> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(taula.comparatorProperty());
		taula.setItems(sortedData);
    }
}