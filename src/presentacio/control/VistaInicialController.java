package presentacio.control;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import bll.Coordina2;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class VistaInicialController implements Initializable {
    @FXML private HBox boxInvisible;
    public static Coordina2 cd2 = Coordina2.getInstancia();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    
    @FXML public void eixir(){Platform.exit();}
    @FXML public void apretarTutelats(){VistaNavigator.loadVista(VistaNavigator.VISTASEGTUTELAT);}
    @FXML public void apretarTutorsAlumnes(){VistaNavigator.loadVista(VistaNavigator.VISTASEGALTUT);}
    @FXML public void apretarProfessors(){VistaNavigator.loadVista(VistaNavigator.VISTASEGPROF);}
    @FXML public void apretarGrups(){VistaNavigator.loadVista(VistaNavigator.VISTASEGRUP);}
    @FXML public void apretarMailing(){VistaNavigator.loadVista(VistaNavigator.VISTAMAIL);}
    @FXML public void apretarCarregaInicial(){VistaNavigator.loadVista(VistaNavigator.VISTACARREGA);}
    @FXML public void apretarAcreditacions(){
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Guardar les acreditacions");
    	fc.getExtensionFilters().addAll(	
    			new ExtensionFilter("PDF *.pdf", "*.pdf"));
    	fc.showSaveDialog(null);
    	cd2.crearAcreditacions(fc.getInitialDirectory().toString());
    	System.out.println("El path es: " + fc.getInitialDirectory().toString());
    }
    
    @FXML public void borrarBD(){
    	Alert al = new Alert (AlertType.WARNING);
		al.setTitle("Atencio!");
		al.setHeaderText("Esta segur que vol esborrar totes les dades?");
		al.setContentText(null);
		ButtonType botoOk = new ButtonType("Si");
		ButtonType botoCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		al.getButtonTypes().setAll(botoOk, botoCancel);
		Optional<ButtonType> result = al.showAndWait();
		if(result.get() == botoOk){
			if(cd2.borraBD()){
				Alert al2 = new Alert (AlertType.CONFIRMATION);
	    		al2.setTitle("Exit");
	    		al2.setHeaderText("Base de dades esborrada correctament");
	    		al2.setContentText(null);
	    		al2.showAndWait();
			} else {
				Alert al2 = new Alert (AlertType.ERROR);
	    		al2.setTitle("Error");
	    		al2.setHeaderText("Ha hagut un error inesperat");
	    		al2.setContentText(null);
	    		al2.showAndWait();
			}
		} else {
			//System.out.println("Fora");
		}
    }
    
}
