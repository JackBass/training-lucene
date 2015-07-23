package org.training.lucene.v3.chapter04;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
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
 * Field.Index（锁音域选项）
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
	private static IndexReader reader = null;

	public IndexUtil() {
		File indexed = Paths.get("./src/resources/index-files").toFile();
		try {
			directory = FSDirectory.open(indexed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 索引
	 */
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

	/**
	 * 增加权重
	 */
	public void indexWithBoost() {
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
				if (i % 2 == 0) {
					doc.setBoost(1.5f);
				}
				// 存储数字
				doc.add(new NumericField("attach", Field.Store.YES, true)
						.setIntValue(attachs[i]));
				// 存储日期
				doc.add(new NumericField("date", Field.Store.YES, true)
						.setLongValue(new Date().getTime()));
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

	/**
	 * 查询
	 */
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

	public void delete() {
		IndexWriter writer = null;

		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)));
			// 参数是一个选项，可以是一个Query:多个一系列的；也可以是一个Term
			// 此时的删除并不是真正删除，而是存储到回收站当中
			Term term = new Term("id", "1");
			writer.deleteDocuments(term);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void undelete() {
		// 恢复时候要使用reader来恢复
		try {
			IndexReader reader = IndexReader.open(directory, false);
			reader.undeleteAll();
			System.out.println("numDocs:" + reader.numDocs());
			System.out.println("maxdoc:" + reader.maxDoc());
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IndexSearcher getSearcher() {
		try {
			if (reader == null) {
				reader = IndexReader.open(directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(reader);
				if (tr != null) {
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 强制删除，不会放入回收站
	 */
	public void forceDelete() {
		IndexWriter writer = null;

		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)));
			writer.forceMergeDeletes();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 手动合并
	 * 
	 */
	public void merge() {
		IndexWriter writer = null;

		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)));
			// 合并索引为两段，不建议使用，会自动处理
			writer.forceMerge(2);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void update() {
		// 它并不提供更新方法，先删除后再添加
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(
					Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)));
			// 合并索引为两段，不建议使用，会自动处理
			writer.updateDocument(new Term("id", "1"), new Document());
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
