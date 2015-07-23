package org.training.lucene.v3.chapter05;

import org.junit.Before;
import org.junit.Test;

public class SearchUtilTest {

	private SearcherUtil su;

	@Before
	public void init() {
		su = new SearcherUtil();
	}

	@Test
	public void searchByTerm() {
		su.searchByTerm("name", "mike", 3);
	}

	@Test
	public void searchByTermRange() {
		su.searchByTermRange("id", "1", "3", 10);
	}

	@Test
	public void searchByWildcard() {
		su.searchByWildcard("email", "*@itat.org", 5);;
	}
}
