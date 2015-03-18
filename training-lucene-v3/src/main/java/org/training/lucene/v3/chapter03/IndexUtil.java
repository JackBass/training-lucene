package org.training.lucene.v3.chapter03;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 搜索工具类<br>
 * <pre>
 * Field.Store.YES或者NO（存储域选项）
 * 设置成YES表示或把这个域中的内容完全存储到文件中，方便进行文本的还原
 * 设置为NO表示把这个域中的内容不存储到文件中，但是可以被索引，此时内容无法完全还原。（doc.get("")）
 * </pre>
 * 
 * <pre>
 * Field.Index()
 * </pre>
 * @author percy
 *
 */
public class IndexUtil {
	private String[] ids = { "1", "2", "3", "4", "5", "6" };
	private String[] emails = { "aa@itat.org", "bb@itat.org", "cc@itat.org",
			"dd@itat.org", "ee@itat.org", "ff@itat.org" };
	private String[] contents = { "welcome to visited the spaces", "hello boy",
			"my name is cc", "i like football", "i like basketball",
			"i like swim" };
	private int[] attachs = { 2, 3, 5, 6, 3, 1 };
	private String[] names = { "zhangsan", "lisi", "john", "jetty", "mike",
			"jake" };

	private Directory directory = null;

	public IndexUtil() {
		File indexed = Paths.get("./src/resources/index-files").toFile();
		try {
			directory = FSDirectory.open(indexed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void index() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)));
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
//				doc.add(new Field(ids[i], Store.YES, Index.NOT_ANALYZED));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
