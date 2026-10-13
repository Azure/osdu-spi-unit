package org.opengroup.osdu.unitservice.index;

import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.index.parser.KeywordQueryBuilder;
import org.opengroup.osdu.unitservice.model.extended.Result;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Indexer class supports indexing
 */
public class Indexer {
    private static final Logger log= Logger.getLogger( Indexer.class.getName() );
    private Analyzer analyzer;
    private Directory directory;
    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    private String catalogLastModified;

    /**
     * Constructor
     * @param catalogLastModified last modified date of the catalog
     * @throws IOException an exception will be thrown when failing to initialize the {@link Indexer}
     */
    public Indexer(String catalogLastModified) throws IOException {
        this.catalogLastModified = catalogLastModified;
        analyzer = CustomAnalyzer.builder()
                .withTokenizer(WhitespaceTokenizerFactory.class)
                .addTokenFilter(ASCIIFoldingFilterFactory.class, "preserveOriginal", "true")
                .addTokenFilter(LowerCaseFilterFactory.class)
                .build();
    }

    /**
     * Index the document represented by {@link IndexRow}.
     * @param indexRow document to be indexed
     * @throws IOException an exception will be thrown if index is not initialized properly.
     */
    public void index(IndexRow indexRow) throws IOException {
        ensureWriterOpen();

        Document doc = new Document();
        List<String> commonFieldValues = new ArrayList<>(indexRow.getCommonFieldValues());
        doc.add(new TextField(LuceneConstants.Id, indexRow.getId(), Field.Store.YES));
        doc.add(new TextField(LuceneConstants.Type, indexRow.getType(), Field.Store.YES));
        doc.add(new TextField(LuceneConstants.Reference, indexRow.toJsonString(), Field.Store.YES));
        commonFieldValues.add(indexRow.getType());
        commonFieldValues.add(indexRow.toJsonString());

        for (Map.Entry<String, String> entry : indexRow.getFieldValuePairs().entrySet()) {
            String fieldName = entry.getKey();
            String value = entry.getValue();

            if(Utility.isNullOrEmpty(fieldName) || Utility.isNullOrEmpty(value))
                continue;
            if(fieldName.equalsIgnoreCase(LuceneConstants.Id) ||
               fieldName.equalsIgnoreCase(LuceneConstants.Type) ||
               fieldName.equalsIgnoreCase(LuceneConstants.Reference))
                continue;

            doc.add(new TextField(fieldName, value, Field.Store.NO));
            commonFieldValues.add(value);
        }

        String contentString = join(commonFieldValues);
        doc.add(new TextField(LuceneConstants.DefaultField, contentString, Field.Store.NO));
        indexWriter.addDocument(doc);
    }

    /**
     * Commit the change on index. If commit fails, all the change since last commit will be rollback.
     * @throws IOException an exception will be thrown when commit fails.
     */
    public void commit() throws IOException {
        if(indexWriter == null)
            return;

        try {
            // Commit
            indexWriter.commit();
            indexWriter.close();
            indexWriter = null;

            // Close the open searcher to refresh it
            closeIndexReader();
        } catch (Exception ex) {
            try {
                indexWriter.rollback();
                indexWriter = null;
            } catch (Exception ex2) {
                //Ignore
            }
            log.warning("Fail to commit the change on indexing. \nError:" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Search the data from the index with search constraint, {@link SearchInput}.
     * @param searchInput search constraint
     * @return a list of documents represented by {@link IndexRow}s.
     * @throws Exception an exception will be thrown if any of the following conditions is met:
     * <ol>
     *     <li>searchInput is null</li>
     *     <li>keyword in {@link SearchInput#getKeyword()} is null or has invalid syntax</li>
     *     <li>offset in {@link SearchInput#getOffset()} is out of range.</li>
     * </ol>
     */
    public Result<IndexRow> search(SearchInput searchInput) throws Exception {
        if(searchInput == null)
            throw new IllegalArgumentException("Search input is null.");

        Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
        return search(query, searchInput.getOffset(), searchInput.getSize());
    }

    private Result<IndexRow> search(Query query, int offset, int size) throws Exception {
        ensureReaderOpen();

        ScoreDoc[] scoreDocs;
        try {
            scoreDocs = indexSearcher.search(query, LuceneConstants.MaximumItems).scoreDocs;
        }
        catch (Exception ex) {
            // The index reader could be obsolete if the index was updated by another instance
            // Re-open the index reader
            closeIndexReader();
            openIndexReader();
            scoreDocs = indexSearcher.search(query, LuceneConstants.MaximumItems).scoreDocs;
        }

        if(scoreDocs == null || scoreDocs.length == 0 || size == 0)
            return new Result<IndexRow>();

        if(offset < 0 || offset >= scoreDocs.length)
            throw new IndexOutOfBoundsException("The offset is out of the range");
        if(size < 0 || size > scoreDocs.length - offset)
            size = scoreDocs.length - offset;

        ArrayList<IndexRow> results = new ArrayList<>();
        for(int i = offset; i < offset + size; i++) {
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            String id = doc.get(LuceneConstants.Id);
            String reference = doc.get(LuceneConstants.Reference);
            String type = doc.get(LuceneConstants.Type);
            if(Utility.isNullOrEmpty(reference) || Utility.isNullOrEmpty(type))
                continue;
            results.add(new IndexRow(id, reference, type));
        }

        return new Result<IndexRow>(results, offset, scoreDocs.length);
    }

    public void ensureWriterOpen() throws IOException {
        if(indexWriter == null)
            openIndexWriter();
    }

    public void ensureReaderOpen() throws IOException {
        if(indexSearcher == null)
            openIndexReader();
    }

    private static String join(List<String> propertyValues) {
        StringBuilder sb = new StringBuilder();
        for(String value : propertyValues) {
            if(!Utility.isNullOrEmpty(value))
                sb.append(value + " ");
        }
        return sb.toString();
    }

    private void openIndexWriter() throws IOException {
        if(indexWriter != null)  return;

        Directory indexDirectory = getDirectory();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    private void openIndexReader() throws IOException {
        if(indexSearcher != null) 
            return;

        indexReader = DirectoryReader.open(getDirectory());
        indexSearcher = new IndexSearcher(indexReader);
    }

    private void closeIndexReader() {
        if(indexSearcher == null) 
            return;

        try {
            indexReader.close();
            indexReader = null;
            indexSearcher = null;
        }
        catch(IOException ex) {
            log.warning("Failed to open index reader. \nError:" + ex.getMessage());
        }
    }

    private Directory getDirectory() {
        if(directory == null)
            directory = new RAMDirectory();
        return directory;
    }
}
