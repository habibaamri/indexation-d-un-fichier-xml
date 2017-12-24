package IHM;
import java.io.IOException;

public class LuceneTester {
	
   
   Indexer indexer;
   
   public static void main(String[] args) {
	   String indexDir = "E:\\Index";
	   String dataDir = "E:\\Data";
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex(indexDir,dataDir);
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }

   public void createIndex(String indexDir,String dataDir) throws IOException{
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" fichir indexer, temp d'execution: "
         +(endTime-startTime)+" ms");		
   }
}