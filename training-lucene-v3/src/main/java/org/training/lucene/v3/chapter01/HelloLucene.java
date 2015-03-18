package org.training.lucene.v3.chapter01;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class HelloLucene {

	/**
	 * 建立索引
	 */
	public void index() {
		IndexWriter writer = null;
		try {
			// 1、创建Directory
			// Directory directory = new RAMDirectory();
			File indexed = Paths.get("./src/resources/index-files").toFile();
			Directory directory = FSDirectory.open(indexed);
			// 2、创建IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					new StandardAnalyzer(Version.LUCENE_36));

			writer = new IndexWriter(directory, iwc);

			// 3、创建Document对象
			Document doc = null;

			// 4、为Document添加Field
			File f = Paths.get("./src/resources/index-docs").toFile();
			for (File file : f.listFiles()) {
				System.out.println("============:" + file.getName());
				doc = new Document();
				doc.add(new Field("content", new FileReader(file)));
				doc.add(new Field("filename", file.getName(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("path", file.getName(), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				// 5、通过IndexWriter添加文档
				writer.addDocument(doc);
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 搜索
	 */
	public void search() {

		try {
			// 1、创建Directory
			File indexed = Paths.get("./src/resources/index-files").toFile();
			Directory directory = FSDirectory.open(indexed);
			// 2、创建IndexReader
			IndexReader reader = IndexReader.open(directory);
			// 3、根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			// 4、创建搜索的Query
			// 创建QueryPaser来确定要搜索文件的内容，第二个参数就是
			QueryParser parser = new QueryParser(Version.LUCENE_36, "content",
					new StandardAnalyzer(Version.LUCENE_36));
			Query query = parser.parse("java");
			// 5、根据searcher搜索并且返回TopDocs
			TopDocs tds = searcher.search(query, 10);
			// 6、根据TopDocs获取ScoreDoc评分对象
			ScoreDoc[] sds = tds.scoreDocs;

			for (ScoreDoc sd : sds) {
				// 7、根据searcher和ScoreDoc对象获取具体的Document对象
				Document d = searcher.doc(sd.doc);
				// 8、根据Document对象获取需要的值
				System.out.println(d.get("filename") + "[" + d.get("path")
						+ "]");
			}

			// 9、关闭reader
			reader.close();
			searcher.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
