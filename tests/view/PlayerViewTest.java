package view;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import controller.GameController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.player.PlayerModelInterface;


public class PlayerViewTest extends ApplicationTest {

	PlayerModelInterface player;
	GameController gameController;
	
    @Override
    public void start(Stage stage) {
		Game game = new Game();
		gameController = new GameController();
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		player = game.getPlayer();
		stage.setScene(scene);
        stage.show();
    }
	
	@Test
	public void labelsShouldDisplayCorrectInformation() {
		int lifes = player.getLifes();
		verifyThat("#lifes", hasText(Integer.toString(lifes)));
		
		int money = player.getMoney();
		verifyThat("#money", hasText(Integer.toString(money)));
	}
	
	@Test
	public void labelsShouldChangeWhenModelChanges() {
		int lifes = player.getLifes();
		int money = player.getMoney();
		
		interact(() -> {
			player.looseLife();
		});
		verifyThat("#lifes", hasText(Integer.toString(lifes-1)));
		
		int earnings = 50;
		interact(() -> {			
			player.earnMoney(earnings);
		});
		verifyThat("#money", hasText(Integer.toString(money+earnings)));
	}
}
