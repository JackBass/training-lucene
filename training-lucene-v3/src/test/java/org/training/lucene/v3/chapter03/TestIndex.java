package org.training.lucene.v3.chapter03;

import org.junit.Test;

public class TestIndex {
	
	@Test
	public void testIndex() {
		IndexUtil iu= new IndexUtil();
		iu.index();
	}
	
	@Test
	public void testQuery() {
		IndexUtil iu= new IndexUtil();
		iu.query();
	}

}
