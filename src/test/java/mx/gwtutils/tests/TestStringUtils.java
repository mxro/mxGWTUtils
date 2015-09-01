/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.tests;

import delight.strings.UriUtils;

import org.junit.Assert;
import org.junit.Test;

import mx.gwtutils.MxroGWTUtils;

public class TestStringUtils {

	@Test
	public void test_string_cloner() {
		final String toStart = "Node 1";
		Assert.assertEquals(toStart, MxroGWTUtils.cloneString(toStart));
		Assert.assertFalse(toStart == MxroGWTUtils.cloneString(toStart));
	}

	@Test
	public void test_get_simple_class_name() {
		Assert.assertEquals("String",
				MxroGWTUtils.getClassSimpleName(String.class));
	}

	@Test
	public void test_get_file_seperator() {
		Assert.assertEquals("M:\\Eclipse\\mxGWTUtils\\test" + "", UriUtils
				.removeExtension("M:\\Eclipse\\mxGWTUtils\\test.txt"));

		Assert.assertEquals("/Eclipse/mxGWTUtils/test",
				UriUtils.removeExtension("/Eclipse/mxGWTUtils/test.txt"));

	}

	@Test
	public void test_no_slash() {
		{
			final String uri = "http://localhost/";
			Assert.assertEquals("http://localhost",
					UriUtils.assertNoSlash(uri));
		}
	}

}
