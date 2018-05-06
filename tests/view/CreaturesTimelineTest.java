package view;

import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.DebugUtils;

import controller.GameController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.level.LevelModelInterface;

public class CreaturesTimelineTest extends ApplicationTest {
	LevelModelInterface level;
	GameController gameController;
	
    @Override
    public void start(Stage stage) {
		Game game = new Game();
		gameController = new GameController();
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		level = game.getLevel();
		stage.setScene(scene);
        stage.show();
    }
	
    @Test
    public void shouldContainCorrectNumberOfImages() {
    	int countGroups = level.getCreatureTimeline().size();
    	verifyThat("#creatureTimelineView", NodeMatchers.hasChildren(countGroups, ".image-view"), DebugUtils.informedErrorMessage(this));
    }
}
