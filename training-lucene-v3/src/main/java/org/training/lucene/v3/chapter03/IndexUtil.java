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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 搜索工具类<br>
 * 
 * <pre>
 * Field.Store.YES或者NO（存储域选项）
 * 设置成YES表示或把这个域中的内容完全存储到文件中，方便进行文本的还原
 * 设置为NO表示把这个域中的内容不存储到文件中，但是可以被索引，此时内容无法完全还原。（doc.get("")）
 * 
 * </pre>
 * 
 * <pre>
 * Field.Index（索引域选项）
 * Index.ANALYZED:进行分词和索引，适用于标题，内容等。
 * Index.NOT_ANNLYZED:进行索引，但是不进行分词，如身份证号码、姓名、ID等，适用于精确搜索。
 * Index.ANYLYZED_NOT_NORMS:进行分词但是不存储norms信息，这个norms中包括了创建索引的时间和权值等信息，排序信息
 * Index.NOT_ANALYZED_NOT_NORMS:既不进行分词也不存储norms信息，很少用
 * Index.NO:不进行索引
 * 
 * </pre>
 * 
 * 
 * <pre>
 * 最佳实践：
 * NOT_ANALYZED_NOT_NORMS  YES  标识符（主键、文件名）、电话号码、身份证号、姓名、日期
 * ANALYZED  YES 文档的标题和摘要
 * ANALYZED  NO 文档正文
 * NO YES 文档类型、数据库主键（不进行索引）
 * NOT_ANALYZED NO 隐藏关键字
 * </pre>
 * 
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
				doc = new Document();
				doc.add(new Field("id", ids[i], Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email", emails[i], Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", contents[i], Field.Store.NO,
						Field.Index.ANALYZED));
				doc.add(new Field("name", names[i], Field.Store.YES,
						Field.Index.NOT_ANALYZED_NO_NORMS));
				writer.addDocument(doc);
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

	public void query() {
		try {
			IndexReader reader = IndexReader.open(directory);
			System.out.println("numDocs:" + reader.numDocs());
			System.out.println("maxdoc:" + reader.maxDoc());
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
