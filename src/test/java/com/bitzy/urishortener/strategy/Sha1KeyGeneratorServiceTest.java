package com.bitzy.urishortener.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bitzy.urishortener.model.Bitzy;
import com.bitzy.urishortener.model.BitzyURI;

public class Sha1KeyGeneratorServiceTest {
  public static String TEST_URI1 = "http://somethinglong.com/";
  public static String TEST_URI1_SHA1_HASH = "hqbuEmggabEPzScd4zszqd259Ww";

  BitzyGeneratorStrategy strategy;
  Bitzy first, second, third, fourth;

  @BeforeEach
  public void setup() {
    strategy = new Sha1BitzyGeneratorStrategy(new BitzyURI(TEST_URI1));
  }

  @Test
  public void testThatTheFirstGenerationOfaBitzyIsAsExpected() {
    int index = 0;
    assertThat(true).isEqualTo(strategy.hasNext());
    while (strategy.hasNext()) {
      Bitzy expected = new Bitzy(TEST_URI1_SHA1_HASH.substring(index, index + Bitzy.BITZY_LENGTH));
      assertThat(expected).isEqualTo(strategy.next());
      index++;
    }
  }

  @Test()
  public void testThatTheGenerateorWillThrowAnEceptionIfitRunsOutOfOptions() {
    Exception exception = assertThrows(BitzyGenerationFailureException.class, () -> {
      int index = 0;
      while (true) {
        Bitzy result = strategy.next();
        Bitzy expected = new Bitzy(TEST_URI1_SHA1_HASH.substring(index, index + Bitzy.BITZY_LENGTH));
        assertThat(expected).isEqualTo(result);
        index++;
      }
    });
    assertThat("All posible bitzys for uri have been exhausted").isEqualTo(exception.getMessage());
  }
}