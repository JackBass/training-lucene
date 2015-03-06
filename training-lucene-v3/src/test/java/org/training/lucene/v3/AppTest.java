package org.training.lucene.v3;

import java.nio.file.Paths;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest{

	@Test
	public void test() {
		System.out.println(Paths.get("./src/resources/README").toAbsolutePath());
		
	}
}
