package mx.gwtutils;

import java.util.List;
import java.util.Vector;



public class MxroGWTUtils {
	public static String removeFirstElement(String s, String separator) {
		final String[] list = s.split(separator);
		if (list.length == 0) {
			//UserError.singelton.log("mxro.Utils: String used for removeFirstElement() is empty.", UserError.Priority.NORMAL);
			return null;
		}
		if (list.length == 1)
			return "";
		if (list.length == 2)
			return list[1];
		String res=list[1];
		for (int i=2;i<list.length;i++) {
			res += "/"+list[i];
		}
		
		return res;
	}

	public static char[] allowedCharacters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
	        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
	        's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
	        '_', '-', '.',
	        '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        '0'	                                      
	};

	public static String getSimpleName(String forName) {
		final String n = forName;
		if (n.length() > 0) {
			String simple="";
			for (int i = 0; i<n.length(); i++) {
				boolean found = false;
				for (final char element : allowedCharacters) {
					found = found || n.charAt(i) == element ||
					n.charAt(i) == Character.toUpperCase(element);
				}
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

	public static int length(String s, String separator) {
		final String[] list = s.split(separator);
		return list.length;
	}

	public static String removeLastElement(String s, String separator) {
		if (s.equals("")) return null;
		
		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		String res = ( s.charAt(0)=='/' ? "/" : "" );
		for (int i=0;i<list.length-1;i++) {
			res += list[i]+"/";
		}
		
		return res;
	}

	public static String lastElement(String s, String separator) {
		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		return list[list.length-1];
	}

	public static String firstElement(String s, String separator) {
		final String[] list = s.split(separator);
		if (list.length == 0) {
			//UserError.singelton.log("mxro.Utils: String used for firstElement() is empty.", UserError.Priority.NORMAL);
			return null;
		}
		return list[0];
	}

	public static String nthElement(String s, String separator, int index) {
		final String[] list = s.split(separator);
		if (list.length <= 0)
			return null;
		if (index >= list.length)
			throw new IndexOutOfBoundsException("Could not get element "+index+" in "+s);
		return list[list.length-1];
	}

	public static String getExtension(String path) {
		final int dotPos = path.lastIndexOf(".");
		if (dotPos>0)
			return path.substring(dotPos+1);
		return "";
	}

	/**
	 * from http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-extension-from-a-string-in-java
	 * 
	 * @param s
	 * @return
	 */
	public static String removeExtension(String s) {
	
	    String separator = ".";//System.getProperty("file.separator");
	    String filename;
	
	    // Remove the path upto the filename.
	    int lastSeparatorIndex = s.lastIndexOf(separator);
	    if (lastSeparatorIndex == -1) {
	        filename = s;
	    } else {
	        filename = s.substring(lastSeparatorIndex + 1);
	    }
	
	    // Remove the extension.
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return s;
	
	    return s.substring(0, lastSeparatorIndex+1) + filename.substring(0, extensionIndex);
	}

	public static <GPEntry> List<GPEntry> flip(List<GPEntry> list) {
		List<GPEntry> flipped = new Vector<GPEntry>();
		for (GPEntry entry : list) {
			flipped.add(0, entry);
		}
		return flipped;
	}

	public static String assertSlash(String ofString) {
		if (ofString.endsWith("/")) return ofString;
		
		return ofString.concat("/");
			
	}

	public static <GPType> List<GPType> asList(GPType node) {
		List<GPType> list = new Vector<GPType>();
		list.add(node);
		return list;
	}

	public static <GPType> List<GPType> asList(GPType node1, GPType node2) {
		List<GPType> list = new Vector<GPType>();
		list.add(node1);
		list.add(node2);
		return list;
	}
}
