package org.opengroup.osdu.unitservice.index.parser;

import org.opengroup.osdu.unitservice.index.LuceneConstants;
import org.opengroup.osdu.unitservice.index.LuceneConstantsTest;
import org.opengroup.osdu.unitservice.index.SearchInput;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.search.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by ZMai on 6/16/2016.
 */
public class KeywordQueryBuilderTest {
    private static final String FieldDelimiter = ":";
    private Analyzer analyzer;
    private String[] supportedFields = LuceneConstantsTest.SupportedFields;

    @BeforeEach
    public void setup() {
        try {
            analyzer = CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)
                    .addTokenFilter(ASCIIFoldingFilterFactory.class, "preserveOriginal", "true")
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .build();
        }
        catch(Exception ex) {
            //Ignore
        }
    }

    @Test
    public void createFieldQueryWithoutDataTypeTest() throws Exception {
        assertNotNull(analyzer);

        String value = "a10";
        for (String fieldName: supportedFields) {
            String keyword = fieldName + FieldDelimiter + value;
            SearchInput searchInput = new SearchInput(keyword);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);

            String actualFieldName = LuceneConstants.resolveFieldName(fieldName);
            assertNotNull(actualFieldName);
            String queryText = actualFieldName + FieldDelimiter + value;
            assertEquals(queryText, query.toString());
        }
    }

    @Test
    public void createFieldQueryWithDataTypeTest() throws Exception {
        assertNotNull(analyzer);

        String dataType = "abc";
        String value = "a10";
        for (String fieldName: supportedFields) {
            String keyword = fieldName + FieldDelimiter + value;
            SearchInput searchInput = new SearchInput(keyword, dataType);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);

            String actualFieldName = LuceneConstants.resolveFieldName(fieldName);
            assertNotNull(actualFieldName);
            String queryText = "+" + actualFieldName + FieldDelimiter + value + " +" + LuceneConstants.Type + FieldDelimiter + dataType;
            assertEquals(queryText, query.toString());
        }
    }

    @Test
    public void createFieldQueryWithUnsupportedFieldNameTest() throws Exception {
        String dataType = "abc";
        String fieldName = "f123";
        String value = "a10";

        String keyword = fieldName + ":" + value;
        SearchInput searchInput = new SearchInput(keyword);
        Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
        String queryText = LuceneConstants.DefaultField + FieldDelimiter + value;
        assertEquals(queryText, query.toString());

        searchInput = new SearchInput(keyword, dataType);
        query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
        queryText = "+" + LuceneConstants.DefaultField + FieldDelimiter + value + " +" + LuceneConstants.Type + FieldDelimiter + dataType;;
        assertEquals(queryText, query.toString());
    }

    @Test
    public void createFieldQueryTestWithoutKeywordAndDataTypeTest() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            assertNotNull(analyzer);
            SearchInput searchInput = new SearchInput(null);
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
            });
    }

    @Test
    public void createFieldQueryTestWithInvalidFieldOperatorTest1() throws  Exception
    {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            assertNotNull(analyzer);
            SearchInput searchInput = new SearchInput("Name: A10");
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
            });
    }

    @Test
    public void createFieldQueryTestWithInvalidFieldOperatorTest2() throws  Exception
    {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {

            assertNotNull(analyzer);
            SearchInput searchInput = new SearchInput("Name :A10");
            Query query = KeywordQueryBuilder.createQuery(searchInput, analyzer, LuceneConstants.DefaultField);
            });
    }
}
