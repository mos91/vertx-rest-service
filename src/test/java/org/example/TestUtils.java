package org.example;

import org.example.exceptions.RequestValidationException;
import org.example.exceptions.ServiceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Oleg Meleshin on 08.05.2018.
 */
public class TestUtils {
  public static void expectValidationException(String message, Runnable cmd) {
    try {
      cmd.run();
    } catch (Exception e) {
      assertTrue(e instanceof RequestValidationException);
      assertEquals(message, e.getMessage());
    }
  }

  public static void expectServiceException(String message, Runnable cmd) {
    try {
      cmd.run();
    } catch (Exception e) {
      assertTrue(e instanceof ServiceException);
      assertEquals(message, e.getMessage());
    }
  }
}
