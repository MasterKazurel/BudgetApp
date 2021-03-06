package control;

import java.net.URL;
import java.util.ResourceBundle;

import application.Manager.Stages;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.CsAdmin;
import model.Profile;
import view.Animations;

public class DeleteAccountController extends Controller {

	@FXML AnchorPane root;
	@FXML HBox titleBox;
	@FXML Label promptLbl;
	@FXML TextField adminPwFld;
	@FXML Button delBtn, cancelBtn, exitBtn;
	private CsAdmin admin;
	private Profile acc;
	
/*--- SETUP ---------------------------------------------------------------------------*/
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupStageClose(root, Stages.DEL_ACC);
		setupStageDrag(titleBox, Stages.DEL_ACC);
		setupOnShow(root, (obs, oldScene, newScene) -> {
			if (newScene != null) {
				Animations.fadeIn(root).onFinishedProperty().set(e -> {
					cancelBtn.requestFocus();
				});
			}
		});
		setupValidation();
	}
	
	@Override
	protected void setupValidation() {
		validations = new Validation<?>[] {
			new Validation<>(promptLbl, adminPwFld, fld -> !fld.getText().equals(admin.getPassword()), "Invalid password. ")
		};
	}
	
	@Override
	public void receiveData(Object... data) {
		admin = (CsAdmin) data[0];
		acc = (Profile) data[1];
		if (acc.getTransactions().isEmpty())
			promptLbl.setText("Are you sure you want to delete " + acc.getFullName() + "'s account? (Warning: all account data will be lost!)");
		else {
			promptLbl.setText("Cannot delete account with transaction data. ");
			adminPwFld.setVisible(false);
			delBtn.setVisible(false);
		}
	}
	
/*--- HELPERS ---------------------------------------------------------------------------*/
	
	
/*--- FXML ---------------------------------------------------------------------------*/
	
	@FXML
	private void exit() {
		manager.close(Stages.DEL_ACC);
	}
	
	@FXML
	private void submit() {
		if (Validation.run(validations)) {
			admin.getUsers().remove(acc);
			manager.close(Stages.DEL_ACC);
		} else
			Animations.shake(manager.getStage(Stages.DEL_ACC));
	}

}
