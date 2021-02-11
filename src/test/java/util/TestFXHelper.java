package util;

import javafx.scene.Node;
import org.testfx.api.FxAssert;

public class TestFXHelper {
  /**
   * Workaround due to Breaking Change in use of verifyThat
   * https://github.com/TestFX/TestFX/issues/673
   *
   * <p>Lookup nodeQuery and return the Node or null if Node was not found.
   *
   * @param nodeQuery
   * @return
   */
  public static Node carefulQuery(String nodeQuery) {
    return FxAssert.assertContext().getNodeFinder().lookup(nodeQuery).tryQuery().orElse(null);
  }
}
