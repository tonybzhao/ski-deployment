package edu.brown.cs.student;

import static org.testng.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class PracTest {

  @Test
  public void test() {
    int num = 5;
    int num2 = 4;
    int added = num + num2;

    assertEquals(added, 9);
  }
}
