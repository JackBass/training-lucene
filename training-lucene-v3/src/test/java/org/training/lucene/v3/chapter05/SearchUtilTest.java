package org.training.lucene.v3.chapter05;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
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
		su.searchByWildcard("email", "*@itat.org", 5);
	}

	@Test
	public void searchByQueryParser01() throws ParseException {
		QueryParser parser = new QueryParser(Version.LUCENE_36, "content",
				new StandardAnalyzer(Version.LUCENE_36));
		Query query = parser.parse("like");
		su.searchByQueryParse(query, 10);
	}
}
