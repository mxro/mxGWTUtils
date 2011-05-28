package mx.gwtutils.tests;

import mx.gwtutils.MxroGWTUtils;

import org.junit.Assert;
import org.junit.Test;

public class TestStringUtils {
	
	@Test
	public void test_string_cloner() {
		final String toStart = "Node 1";
		Assert.assertEquals(toStart, MxroGWTUtils.cloneString(toStart));
		Assert.assertFalse(toStart == MxroGWTUtils.cloneString(toStart));
	}
	
	@Test public void test_get_simple_class_name() {
		Assert.assertEquals("String", MxroGWTUtils.getClassSimpleName(String.class));
	}
	
	@Test public void test_get_file_seperator() {
		Assert.assertEquals("M:\\Eclipse\\mxGWTUtils\\test"+"", MxroGWTUtils.removeExtension("M:\\Eclipse\\mxGWTUtils\\test.txt"));
		
		Assert.assertEquals("/Eclipse/mxGWTUtils/test", MxroGWTUtils.removeExtension("/Eclipse/mxGWTUtils/test.txt"));
		
	}
	
}
