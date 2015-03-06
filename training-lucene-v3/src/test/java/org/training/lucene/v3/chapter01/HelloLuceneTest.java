package org.training.lucene.v3.chapter01;

import org.junit.Test;

public class HelloLuceneTest {
	
	@Test
	public void testIndex() {
		HelloLucene index = new HelloLucene();
		index.index();
	}
	
	@Test
	public void testSearch() {
		HelloLucene index = new HelloLucene();
		index.search();
	}
}
