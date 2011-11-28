/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mx.gwtutils.tests;


import mx.gwtutils.GwtCompatible;
import mx.gwtutils.Nullable;


/**
 * Contains additional assertion methods not found in JUnit.
 *
 * @author kevinb@google.com (Kevin Bourillion)
 */
@GwtCompatible
public final class GuavaAsserts {

  private GuavaAsserts() { }

  /**
   * Utility for testing equals() and hashCode() results at once.
   * Tests that lhs.equals(rhs) matches expectedResult, as well as
   * rhs.equals(lhs).  Also tests that hashCode() return values are
   * equal if expectedResult is true.  (hashCode() is not tested if
   * expectedResult is false, as unequal objects can have equal hashCodes.)
   *
   * @param lhs An Object for which equals() and hashCode() are to be tested.
   * @param rhs As lhs.
   * @param expectedResult True if the objects should compare equal,
   *   false if not.
   */
  public static void checkEqualsAndHashCodeMethods(
      final String message, final Object lhs, final Object rhs, final boolean expectedResult) {

    if ((lhs == null) && (rhs == null)) {
      assertTrueImpl(
          "Your check is dubious...why would you expect null != null?",
          expectedResult);
      return;
    }

    if ((lhs == null) || (rhs == null)) {
      assertTrueImpl(
          "Your check is dubious...why would you expect an object "
          + "to be equal to null?", !expectedResult);
    }

    if (lhs != null) {
      assertEqualsImpl(message, expectedResult, lhs.equals(rhs));
    }
    if (rhs != null) {
      assertEqualsImpl(message, expectedResult, rhs.equals(lhs));
    }

    if (expectedResult) {
      String hashMessage =
          "hashCode() values for equal objects should be the same";
      if (message != null) {
        hashMessage += ": " + message;
      }
      assertTrueImpl(hashMessage, lhs.hashCode() == rhs.hashCode());
    }
  }

  /**
   * Determines whether two possibly-null objects are equal. Returns:
   *
   * <ul>
   * <li>{@code true} if {@code a} and {@code b} are both null.
   * <li>{@code true} if {@code a} and {@code b} are both non-null and they are
   *     equal according to {@link Object#equals(Object)}.
   * <li>{@code false} in all other situations.
   * </ul>
   *
   * <p>This assumes that any non-null objects passed to this function conform
   * to the {@code equals()} contract.
   */
  public static boolean equal(@Nullable final Object a, @Nullable final Object b) {
    return a == b || (a != null && a.equals(b));
  }
  
  private static void assertTrueImpl(final String message, final boolean expression) {
	  if (!expression) {
		  failWithMessage(message, "expected true but got false");
	  }
  }
  
  /**
   * Replacement of Assert.assertEquals which provides the same error
   * message in GWT and java.
   */
  private static void assertEqualsImpl(
      final String message, final Object expected, final Object actual) {
    if (!equal(expected, actual)) {
      failWithMessage(
          message, "expected:<" + expected + "> but was:<" + actual + ">");
    }
  }

  private static void failWithMessage(final String userMessage, final String ourMessage) {
    new RuntimeException((userMessage == null)
        ? ourMessage
        : userMessage + ' ' + ourMessage);
  }

}
