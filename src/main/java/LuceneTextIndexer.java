import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneTextIndexer {

//    static { System.loadLibrary("luceneSearcher");}

    private static final String INDEX_DIR = "/home/ilhami/javatest/LuceneReader/indexs";

    public static void searcherAndIndexer(String text, String entry) throws Exception {
        IndexWriter writer = createWriter();
        List<Document> documents = new ArrayList<>();

        String catchText = text;

        final Path docDir = Paths.get(INDEX_DIR);

        Document docu1 = indexDoc(writer, catchText, Files.getLastModifiedTime(docDir).toMillis());
        documents.add(docu1);
        writer.deleteAll();

        writer.addDocuments(documents);
        writer.commit();

        IndexReader reader = DirectoryReader.open(writer);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query q1 = qp.parse(entry);
        TopDocs hits = searcher.search(q1, 10);
        System.out.println("Results :: " + hits.totalHits);

        writer.close();
    }

    private static IndexWriter createWriter() throws IOException {
        FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    private static Document indexDoc(IndexWriter writer, String catchText, long lastModified) throws IOException {
        Document doc = new Document();

        doc.add(new StringField("path", "date", Field.Store.YES));
        doc.add(new LongPoint("modified", lastModified));
        doc.add(new TextField("contents", catchText, Field.Store.YES));

        writer.updateDocument(new Term("path", "date"), doc);

        return doc;
    }

    private static Document createDocument(String path, String context) {
        Document document = new Document();
        document.add(new StringField("id", path, Field.Store.YES));
        document.add(new TextField("firstName", context, Field.Store.YES));
        return document;
    }
}
