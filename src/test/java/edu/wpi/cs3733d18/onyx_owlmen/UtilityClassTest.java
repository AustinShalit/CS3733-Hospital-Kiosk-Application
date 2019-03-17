package edu.wpi.cs3733d18.onyx_owlmen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
abstract class UtilityClassTest {

  private final Class clazz;

  UtilityClassTest(Class clazz) {
    this.clazz = clazz;
  }

  @Test
  void testConstructorPrivate() {
    Constructor constructor = clazz.getDeclaredConstructors()[0];

    assertFalse(constructor.isAccessible());
  }

  @Test
  void testConstructorReflection() {
    assertThrows(InvocationTargetException.class, () -> {
      Constructor constructor = clazz.getDeclaredConstructors()[0];
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }
}
