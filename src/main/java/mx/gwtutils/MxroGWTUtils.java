/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import one.utils.OneUtilsCollections;
import one.utils.OneUtilsStrings;

public class MxroGWTUtils {

	public static String cloneString(final String toClone) {
		if (toClone == null) {
			return null;
		}
		if (toClone.isEmpty()) {
			return new String("").concat(new String(""));
		}

		if (toClone.length() == 1) {
			return String.valueOf(toClone.charAt(0));
		}

		return toClone.substring(0, 1) + toClone.substring(1);
	}

	public static String removeFirstElement(final String s,
			final String separator) {
		final String[] list = s.split(separator);
		if (list.length == 0) {
			// UserError.singelton.log("mxro.Utils: String used for removeFirstElement() is empty.",
			// UserError.Priority.NORMAL);
			return null;
		}
		if (list.length == 1)
			return "";
		if (list.length == 2)
			return list[1];
		String res = list[1];
		for (int i = 2; i < list.length; i++) {
			res += "/" + list[i];
		}

		return res;
	}

	/**
	 * @see #getSimpleName(String)
	 * 
	 */
	public static String getSimpleName(final String forName,
			final int maxCharacters) {
		final String name = getSimpleName(forName);
		if (name.length() > maxCharacters)
			return name.substring(0, maxCharacters);
		return name;
	}

	/**
	 * Simplifies any given string and makes it conformant as file name for an
	 * URI. Illegal characters are replaced by an '_'.<br/>
	 * <br />
	 * For legal characters, see {@link #allowedCharacters}:<br/>
	 * {@value #allowedCharacters}
	 * 
	 * @param forName
	 * @return
	 */
	public static String getSimpleName(final String forName) {
		final String n = forName;
		if (n.length() > 0) {
			String simple = "";
			for (int i = 0; i < n.length(); i++) {
				final boolean found = OneUtilsStrings.isSimpleCharacter(n
						.charAt(i));
				if (found) {
					simple = simple + n.charAt(i);
				} else {
					simple = simple + '_';
				}
			}
			return simple;
		} else
			return n;
	}

	public static int length(final String s, final String separator) {
		final String[] list = s.split(separator);
		return list.length;
	}

	public static String removeLastElement(final String s,
			final String separator) {
		if (s.equals(""))
			return null;

		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		String res = (s.charAt(0) == '/' ? "/" : "");
		for (int i = 0; i < list.length - 1; i++) {
			res += list[i] + "/";
		}

		return res;
	}

	public static String lastElement(final String s, final String separator) {
		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		return list[list.length - 1];
	}

	public static String firstElement(final String s, final String separator) {
		final String[] list = s.split(separator);
		if (list.length == 0) {
			// UserError.singelton.log("mxro.Utils: String used for firstElement() is empty.",
			// UserError.Priority.NORMAL);
			return null;
		}
		return list[0];
	}

	public static String nthElement(final String s, final String separator,
			final int index) {
		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		if (index >= list.length)
			throw new IndexOutOfBoundsException("Could not get element "
					+ index + " in " + s);
		return list[list.length - 1];
	}

	/**
	 * Returns the extension of a given filename (excluding the .). If there is
	 * no extension defined, this function returns "".
	 * 
	 * @param path
	 * @return
	 */
	public static String getExtension(final String path) {
		final int dotPos = path.lastIndexOf(".");
		if (dotPos > 0) {
			return path.substring(dotPos + 1);
		}
		return "";
	}

	public static String getClassSimpleName(final Class<?> clz) {
		final String name = clz.getName();
		final int endCutOff = name.length();
		int beginCutOff = name.lastIndexOf(".") + 1;

		if (beginCutOff == -1)
			beginCutOff = 1;

		return name.substring(beginCutOff, endCutOff);
	}

	/**
	 * from http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-
	 * extension-from-a-string-in-java
	 * 
	 * @param s
	 * @return
	 */
	public static String removeExtension(final String s) {
		final String works = s + "";
		final String separator = "/"; // System.getProperty("file.separator");

		final String filename;

		// Remove the path up to the filename.
		final int lastSeparatorIndex = works.replaceAll("\\\\", "/")
				.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		final int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return s;

		return s.substring(0, lastSeparatorIndex + 1)
				+ filename.substring(0, extensionIndex);
	}

	/**
	 * Reverse the order of a list.
	 * 
	 * @param list
	 * @return
	 */
	public static <GPEntry> List<GPEntry> flip(final List<GPEntry> list) {
		final List<GPEntry> flipped = new LinkedList<GPEntry>();
		for (final GPEntry entry : list) {
			flipped.add(0, entry);
		}
		return flipped;
	}

	/**
	 * Joins the items of multiple lists into one list.
	 * 
	 * @return
	 */
	public static <GPItem> List<GPItem> joinLists(final List<List<GPItem>> lists) {
		if (lists.size() == 0) {
			return new ArrayList<GPItem>(0);
		}
		final List<GPItem> joined = new ArrayList<GPItem>(lists.size()
				* lists.get(0).size());
		for (final List<GPItem> list : lists) {
			joined.addAll(list);
		}

		return joined;
	}

	/**
	 * Necessary for GWT as Class.isInstance is not supported. Does not work
	 * reliably if <code>clazz</code> is an interface.
	 * 
	 * @param clazz
	 * @param object
	 * @return
	 */
	public static boolean instanceOf(final Class<? extends Object> clazz,
			final Object object) {

		if (object == null) {
			return false;
		}

		if ((object instanceof Serializable)
				&& clazz.equals(Serializable.class)) {
			return true;
		}

		return MxroGWTUtils.isSuperclass(clazz, object.getClass());
	}

	public static interface ListElementOperation<ElementType> {
		public void process(ElementType element);
	}

	public static <ElementType> void forAllListItem(
			final List<ElementType> list,
			final ListElementOperation<ElementType> operation) {
		for (final ElementType element : list) {
			operation.process(element);
		}
	}

	/**
	 * Checks whether all the items in a list have a compatible type.
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	public static <GPType> boolean allItemsPassTest(final List<GPType> list,
			final OneUtilsCollections.Predicate<GPType> test) {
		boolean result = true;
		for (final GPType o : list) {
			if (test.testElement(o)) {
				result = result && true;
			} else {
				result = false;
			}
		}
		return result;
	}

	public static <GPType> boolean anyItemPassTest(final List<GPType> list,
			final OneUtilsCollections.Predicate<GPType> test) {
		for (final GPType o : list) {

			if (test.testElement(o)) {
				return true;
			}
		}
		return false;
	}

	public static void checkUri(final String uri) {
		assert uri != null : "Uri cannot be null.";
		assert !uri.equals("") : "Uri cannot be empty";
	}

	public static String assertSlash(final String ofString) {
		if (ofString.endsWith("/"))
			return ofString;

		return ofString.concat("/");
	}

	public static String assertNoSlash(final String ofString) {
		if (!ofString.endsWith("/")) {
			return ofString;
		}

		return ofString.substring(0, ofString.length() - 1);
	}

	public static <GPType> List<GPType> asList(final GPType node) {
		final List<GPType> list = new Vector<GPType>(1);
		list.add(node);
		return list;
	}

	public static <GPType> List<GPType> asList(final GPType node1,
			final GPType node2) {
		final List<GPType> list = new Vector<GPType>();
		list.add(node1);
		list.add(node2);
		return list;
	}

	public static final boolean emptyOrNull(final String s) {
		return (s == null) || s.isEmpty();
	}

	public static boolean isSuperclass(
			final Class<? extends Object> superclass,
			final Class<? extends Object> clazz) {
		if (superclass.equals(clazz)) {
			return true;
		}

		// all classes are superlcass of Object
		if (clazz.equals(Object.class)) {
			return false;
		}

		return isSuperclass(superclass, clazz.getSuperclass());
	}

}
