package omoikane.sistema;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 4/05/13
 * Time: 07:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SceneOverloaded extends Scene {
    private Initializable controller;
    public SceneOverloaded(Parent parent, Initializable controller) {
        super(parent);
        setController(controller);
    }

    public Initializable getController() {
        return controller;
    }

    public void setController(Initializable controller) {
        this.controller = controller;
    }
}
