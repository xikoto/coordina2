package presentacio.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import bll.Coordina2;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import pojo.AlumneTutor;
import pojo.Grup;
import pojo.Professor;
import pojo.Tutelat;

public class VistaSegonaControllerGrup implements Initializable{

    @FXML private TableView<Grup> taula;
	@FXML private TableColumn<Grup, AlumneTutor> columnaPrimerAL;
    @FXML private TableColumn<Grup, String> columnaNomGrup;
    @FXML private TableColumn<Grup, AlumneTutor> columnaSegonAL;
    @FXML private TableColumn<Grup, String> columnaProfessor;
    @FXML private TableView<Tutelat> taulaTutelats;
    @FXML private TableColumn<Tutelat, String> columnaTutelats;
    @FXML private TextField barraBuscadora;
    private Coordina2 cd2 = Coordina2.getInstancia();
	private ArrayList<Grup> grup = cd2.llistarGrups();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	ObservableList<Grup> grups = FXCollections.observableArrayList(grup);
    	columnaNomGrup.setCellValueFactory(param -> new SimpleStringProperty((param.getValue()).getNom()));
    	columnaProfessor.setCellValueFactory(param -> new ReadOnlyObjectWrapper <>((param.getValue()).getProfessor().toString()));
    	columnaPrimerAL.setCellValueFactory(param -> new ReadOnlyObjectWrapper <>((param.getValue()).getAlumne1()));
    	columnaSegonAL.setCellValueFactory(param -> new ReadOnlyObjectWrapper <>((param.getValue()).getAlumne2()));
    	taula.setItems(grups);
    	
    	/********LISTENER DE LA TAULA*********/
    	
    	//ObservableList de Tutelats
    	ObservableList<Tutelat> personesgrup = FXCollections.observableArrayList();
    	//listener
    	taula.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if(newSelection != null){
				personesgrup.clear();//buidem la observableList
	    		List<Tutelat> llistapersgrup = cd2.obtindreTutelatsPerGrup(newSelection.getNom());
	    		for(Tutelat t : llistapersgrup){
	    			personesgrup.add(t);
	    		}
				columnaTutelats.setCellValueFactory(param -> new ReadOnlyObjectWrapper <>(param.getValue().getNif() 
						+ " - " + param.getValue().getCognoms() + ", "+ param.getValue().getNom()));
				taulaTutelats.setItems(personesgrup);
			}
    	});
    	
    	/********FILTRATGE*******************/
    	FilteredList<Grup> filteredData = new FilteredList<>(grups,p -> true);
    	barraBuscadora.textProperty().addListener((ob, vell, nou) -> {
    		filteredData.setPredicate(grup -> {
				if (nou == null || nou.isEmpty()) {
					return true;
				}
				String minuscules = nou.toLowerCase();

				if (grup.getNom().toLowerCase().contains(minuscules)) {
					return true;
				}	
				return false;
			});
    	}); // de listener
    	SortedList<Grup> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(taula.comparatorProperty());
		taula.setItems(sortedData);
    }
    
    @FXML void enrere(ActionEvent event) {VistaNavigator.loadVista(VistaNavigator.VISTAINI);}

    @FXML
    void afegirGrup(ActionEvent event) {
    	Dialog<Grup> dialog = new Dialog<>();
    	dialog.setTitle("Afegir grup");
    	dialog.setHeaderText("Dialeg per a afegir un grup nou. Emplene tots els camps.");
    	dialog.setResizable(true);
    	Label lb1 = new Label("Nom:");
    	Label lb2 = new Label("Professor:");
    	Label lb3 = new Label("Alumne Tutor 1:");
    	Label lb4 = new Label("Alumne Tutor 2:");
    	TextField tx1 = new TextField();
    	ComboBox<String> tx2 = new ComboBox<String>();
    	ArrayList<Professor> profes = cd2.llistarProfessors();
    	for(Professor p : profes){
    		tx2.getItems().add(p.getNif() + " - " + p.getNom()+ ", " + p.getCognoms());
    	}
    	ComboBox<String> tx3 = new ComboBox<String>();
    	ComboBox<String> tx4 = new ComboBox<String>();
    	ArrayList<AlumneTutor> alumnestutors = cd2.llistarAlumnesTutors();
    	for(AlumneTutor altutor : alumnestutors){
    		tx3.getItems().add(altutor.getNif() + " - " + altutor.getNom()+ ", " + altutor.getCognoms());
    		tx4.getItems().add(altutor.getNif() + " - " + altutor.getNom()+ ", " + altutor.getCognoms());
    	}
    	GridPane grid = new GridPane();
    	grid.add(lb1, 1, 1);
    	grid.add(lb2, 1, 2);
    	grid.add(lb3, 1, 3);
    	grid.add(lb4, 1, 4);
    	grid.add(tx1, 2, 1);
    	grid.add(tx2, 2, 2);
    	grid.add(tx3, 2, 3);
    	grid.add(tx4, 2, 4);
    	dialog.getDialogPane().setContent(grid);
    	ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    	dialog.setResultConverter(new Callback<ButtonType, Grup>() {
    		@Override
    		public Grup call(ButtonType b){
    			if(b == buttonTypeOk){
    				Professor p = cd2.buscarProfessor(tx2.getSelectionModel().getSelectedItem().split(" -")[0]);
    				AlumneTutor at1 = cd2.buscarAlumneTutor(tx3.getSelectionModel().getSelectedItem().split(" -")[0]);
    				AlumneTutor at2 = cd2.buscarAlumneTutor(tx4.getSelectionModel().getSelectedItem().split(" -")[0]);
    				return new Grup(tx1.getText(), p, at1, at2);
    			} else {
    				return null;
    			}
    		} 	
    	});    	
    	Optional<Grup> result = dialog.showAndWait();
    	if(result.isPresent()){
    		cd2.afegirGrup(result.get());
			ObservableList<Grup> grups = FXCollections.observableArrayList(cd2.llistarGrups());
			taula.setItems(grups);
    	}
    }

    @FXML
    void editarGrup(ActionEvent event) {
    	if(taula.getSelectionModel().getSelectedItem() == null){
    		Alert al = new Alert (AlertType.WARNING);
    		al.setTitle("Atencio!");
    		al.setHeaderText("Seleccione un element a editar");
    		al.setContentText(null);
    		al.showAndWait();
    	} else {
    		Dialog<Grup> dialog = new Dialog<>();
    		Grup aux = taula.getSelectionModel().getSelectedItem();
        	dialog.setTitle("Editar Grup");
        	dialog.setHeaderText("Dialeg per a editar un grup. Emplene tots els camps.");
        	dialog.setResizable(true);
        	Label lb1 = new Label("Nom:");
        	Label lb2 = new Label("Professor:");
        	Label lb3 = new Label("Alumne Tutor 1:");
        	Label lb4 = new Label("Alumne Tutor 2:");
        	TextField tx1 = new TextField(aux.getNom());
        	tx1.setEditable(false);
        	tx1.setDisable(true);
        	
        	ComboBox<String> tx2 = new ComboBox<String>();
        	ArrayList<Professor> listprofes = cd2.llistarProfessors();
        	for(Professor p : listprofes){
        		tx2.getItems().add(p.getNif() + " - " + p.getNom() + ", " + p.getCognoms());
        	}
        	tx2.getSelectionModel().select(aux.getProfessor().getNif() + " - " + aux.getProfessor().getNom()
        								+ ", " + aux.getProfessor().getCognoms());
        	
        	ComboBox<String> tx3 = new ComboBox<String>();
        	ArrayList<AlumneTutor> listaltutors = cd2.llistarAlumnesTutors();
        	for(AlumneTutor p : listaltutors){
        		tx3.getItems().add(p.getNif() + " - " + p.getNom() + ", " + p.getCognoms());
        	}
        	tx3.getSelectionModel().select(aux.getAlumne1().getNif() + " - " + aux.getAlumne1().getNom()
        								+ ", " + aux.getAlumne1().getCognoms());
        	
        	ComboBox<String> tx4 = new ComboBox<String>();
        	for(AlumneTutor p : listaltutors){
        		tx4.getItems().add(p.getNif() + " - " + p.getNom() + ", " + p.getCognoms());
        	}
        	tx4.getSelectionModel().select(aux.getAlumne2().getNif() + " - " + aux.getAlumne2().getNom()
        								+ ", " + aux.getAlumne2().getCognoms());
        	
        	GridPane grid = new GridPane();
        	grid.add(lb1, 1, 1);
        	grid.add(lb2, 1, 2);
        	grid.add(lb3, 1, 3);
        	grid.add(lb4, 1, 4);
        	grid.add(tx1, 2, 1);
        	grid.add(tx2, 2, 2);
        	grid.add(tx3, 2, 3);
        	grid.add(tx4, 2, 4);
        	dialog.getDialogPane().setContent(grid);
        	ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        	dialog.setResultConverter(new Callback<ButtonType, Grup>() {
        		@Override
        		public Grup call(ButtonType b){
        			Professor p = cd2.buscarProfessor(tx2.getSelectionModel().getSelectedItem());
        			AlumneTutor al1 = cd2.buscarAlumneTutor(tx3.getSelectionModel().getSelectedItem());
        			//no cal ternaria, si es alguna cosa que nop toca no el trobara i tornara null
        			AlumneTutor al2 = cd2.buscarAlumneTutor(tx4.getSelectionModel().getSelectedItem());
        			if(b == buttonTypeOk){
        				return new Grup(tx1.getText(), p, al1, al2);
        			}
        			return null;
        		} 	
        	});    	
        	Optional<Grup> result = dialog.showAndWait();
			if(result.isPresent()){
				grup = cd2.llistarGrups();
        		for(int i = 0; i < grup.size(); i++){
        			if(grup.get(i).getNom().equals(result.get().getNom())){ //si el troba
        				grup.remove(i);
        			}
        		}
        		grup.add(result.get());
				cd2.editarGrup(result.get());
				ObservableList<Grup> altu = FXCollections.observableArrayList(grup);
				taula.setItems(altu);
        	}
    	}
    }

    @FXML
    void esborrarGrup(ActionEvent event) {
    	Grup aux = taula.getSelectionModel().getSelectedItem(); 
    	if(aux == null){
    		Alert al = new Alert (AlertType.WARNING);
    		al.setTitle("Atencio!");
    		al.setHeaderText("Seleccione un element a esborrar");
    		al.setContentText(null);
    		al.showAndWait();
    	} else {
    		Alert al = new Alert (AlertType.WARNING);
    		al.setTitle("Atencio!");
    		al.setHeaderText("Esta segur que vol esborrar l'element?");
    		al.setContentText(null);
    		Optional<ButtonType> result = al.showAndWait();
    		if(result.isPresent() && result.get() == ButtonType.OK){
    			cd2.borrarGrup(aux);
    			ObservableList<Grup> grups = FXCollections.observableArrayList(cd2.llistarGrups());
    			taula.setItems(grups);
    		}
    	}
    }
}
