package util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.util.FXMLLoaderUtil;
import org.mazerunner.util.FXMLLoaderUtil.ErrorView;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

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

    ArgumentCaptor<LogRecord> argument = ArgumentCaptor.forClass(LogRecord.class);
    Mockito.verify(expectedErrorFilter).isLoggable(argument.capture());
    assertEquals("Loading not-existing.fxml failed", argument.getValue().getMessage());
    assertThat(root, is(instanceOf(ErrorView.class)));
  }
}
