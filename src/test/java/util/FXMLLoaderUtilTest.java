package util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import java.util.logging.Filter;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import application.util.FXMLLoaderUtil;
import application.util.FXMLLoaderUtil.ErrorView;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class FXMLLoaderUtilTest extends ApplicationTest {
  private String storedBasePath;

  @Before
  public void setUp() {
    storedBasePath = FXMLLoaderUtil.basePath;
    FXMLLoaderUtil.basePath = "testFxml/";
  }

  @After
  public void tearDown() {
    FXMLLoaderUtil.basePath = storedBasePath;
  }

  @Test
  public void loadingOfExistingResourceSuccessfull() {
    Parent root = FXMLLoaderUtil.load("test.fxml");
    assertThat(root, is(not(instanceOf(ErrorView.class))));
  }

  @Test
  public void castingShouldWork() {
    Label root = (Label) FXMLLoaderUtil.load("test.fxml");
    assertThat(root, is(not(instanceOf(ErrorView.class))));
  }

  @Test
  public void loadingOfNotExistingResourceReturnsErrorView() {
    Filter expectedErrorFilter = Mockito.mock(Filter.class);
    Logger.getLogger(FXMLLoaderUtil.class.getName()).setFilter(expectedErrorFilter);
    Parent root = FXMLLoaderUtil.load("not-existing.fxml");
    Mockito.verify(expectedErrorFilter)
        .isLoggable(
            ArgumentMatchers.argThat(
                record -> record.getMessage().equals("Loading not-existing.fxml failed")));
    assertThat(root, is(instanceOf(ErrorView.class)));
  }
}
