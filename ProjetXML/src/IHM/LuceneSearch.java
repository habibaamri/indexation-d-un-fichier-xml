package IHM;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneSearch {

   Searcher searcher;

   public static void main(String[] args) {
	   LuceneSearch tester;
		
	   String indexDir = "E:\\Index";
	   String dataDir = "E:\\Data";
      try {
         tester = new LuceneSearch();
         tester.search("Galos",indexDir,dataDir);
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   public String search(String searchQuery,String indexDir,String dataDir) throws IOException, ParseException{
      searcher = new Searcher(indexDir);
      Document doc;
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();

      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime) +" ms");
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
          doc = searcher.getDocument(scoreDoc);
         System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
         return doc.get(LuceneConstants.FILE_PATH);
      }
      searcher.close();
	return "Rien Trouvé";
   }	
}