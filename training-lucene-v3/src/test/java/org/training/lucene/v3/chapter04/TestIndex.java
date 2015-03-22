package org.training.lucene.v3.chapter04;

import org.junit.Test;

public class TestIndex {

	@Test
	public void testDelete() {
		IndexUtil iu = new IndexUtil();
		iu.delete();
	}
	
	@Test
	public void testUnDelete() {
		IndexUtil iu = new IndexUtil();
		iu.undelete();
	}
}
