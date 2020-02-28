package views;

import controllers.ControllerClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Volunteer;

import java.io.IOException;

public class SceneChanger {
    private static Volunteer loggedInUser;


    public static Volunteer getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Volunteer loggedInUser) {
        SceneChanger.loggedInUser = loggedInUser;
    }

    /**
     * This method will accept the title of the new scene and the .fxml file name for the view
     * and the ActionEvent that triggered the change
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));

        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        // get the stage from the event
        // but to use the event, we need to get the parent's scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method will change scenes and preload the next scene with a Volunteer object
     */
    public void changeScenes(ActionEvent event, String viewName, String title, Volunteer volunteer,
                             ControllerClass controllerClass) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        // after accessing the scene, we need to go to the ControllerClass and preload the Volunteer data
        controllerClass = loader.getController();
        controllerClass.preloadData(volunteer);

        // get the stage from the event
        // but to use the event, we need to get the parent's scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

    }

}
