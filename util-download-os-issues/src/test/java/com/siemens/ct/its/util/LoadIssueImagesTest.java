package com.siemens.ct.its.util;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoadIssueImagesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String url = "http://git.oschina.net/uploads/images/2014/0723/103950_cafa892c_76819.png";

		String file = ExportIssues.subfile(url);

		Assert.assertEquals(file, "103950_cafa892c_76819.png");
	}
}
