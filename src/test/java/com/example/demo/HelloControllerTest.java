package test.java.com.example.demo;

import com.example.demo.controller.HelloController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloControllerTest {
  @Test
  void helloReturnsExpectedMessage() {
    HelloController controller = new HelloController();
    assertEquals("Hello, CI/CD!", controller.hello());
  }
}
