import java.io.*; 
import java.util.*; 

public class IRSystem 
{
        
  // before witing index, assume and all index data will fit into memory
  Map<String, List<Integer>> index = new HashMap<>();
  List<String> documents = new ArrayList<String>();
  int docid = 0;
  
  // read and created index
  void create(File file) throws IOException 
  {
    String name = file.getName();
    // document ID
    documents.add(name);
    docid++;
    
    BufferedReader reader = null;
    try 
    {
      reader = new BufferedReader(new FileReader(file));
      String line;
      while((line = reader.readLine()) != null) 
      {
        String words[] = line.split("[^A-Za-z]"); // word tokenize
        for(String _word : words) 
        {
          String word = _word.toLowerCase();
          List<Integer> docs = index.get(word);
          if(docs == null) 
          {
            docs = new ArrayList<>();
            index.put(word, docs);
          }
          docs.add(docid);
        }
      }
    } 
    finally 
    {
      if(reader != null) 
      {
        reader.close();
      }
    }
          
  }
        
  // writes index back to file
  void write(File file) throws IOException 
  {
    BufferedWriter writer = null;
    try 
    {
      writer = new BufferedWriter(new FileWriter(file));
      // documents mapping
      for(int i=0;i<documents.size();i++) 
      {
        int id = i+1;
        writer.write(documents.get(i));
        writer.write(" ");
        writer.write(String.valueOf(id));
        writer.newLine();
      }
      // inverted index
      for(String word : index.keySet()) 
      {
        List<Integer> docs = index.get(word);
        writer.write(word);
        writer.write(" ");
        int s = docs.size();
        for(int i = 0; i < s - 1; i++) 
        {
          writer.write(String.valueOf(docs.get(i)));
          writer.write(" ");
        }
        writer.write(String.valueOf(docs.get(s - 1)));
        writer.newLine();
      }
    } 
    finally 
    {
      if(writer != null) 
      {
        writer.close();
      }
    }
  }

  public String editDistance(String word)
  {
    ArrayList<Tuple> record = new ArrayList<Tuple>();
    int dist;
    for(String word1 : index.keySet())
    {
        dist=editDistanceHelper(word,word1);
        record.add(new Tuple(word1,dist));
    } 
    int minidx=-1;
    String minword1,minword2,minword3;
    int firstmin = Integer.MAX_VALUE; 

    for(int i=0;i<record.size();i++)
    {
      if(record.get(i).dist<firstmin)
      {
        firstmin=record.get(i).dist;
        minidx=i;
      }
    }
    minword1=record.get(minidx).word;
    record.remove(minidx);

    firstmin = Integer.MAX_VALUE; 
    for(int i=0;i<record.size();i++)
    {
      if(record.get(i).dist<firstmin)
      {
        firstmin=record.get(i).dist;
        minidx=i;
      }
    }
    minword2=record.get(minidx).word;
    record.remove(minidx);

    firstmin = Integer.MAX_VALUE; 
    for(int i=0;i<record.size();i++)
    {
      if(record.get(i).dist<firstmin)
      {
        firstmin=record.get(i).dist;
        minidx=i;
      }
    }
    minword3=record.get(minidx).word;
    record.remove(minidx);

    System.out.println("Suggested Words:");
    System.out.println("1" + minword1);
    System.out.println("2" + minword2);
    System.out.println("3" + minword3);
    System.out.println("Enter the choice from 1,2,3");
    Scanner reader = new Scanner(System.in);
    int res=reader.nextInt();
    reader.close();
    if(res==1)
      return minword1;
    else if(res==2)
      return minword2;
    else
      return minword3;

  }

  public int editDistanceHelper(String word1, String word2)
  {
    int len1 = word1.length();
    int len2 = word2.length();
 
    // len1+1, len2+1, because finally return dp[len1][len2]
    int[][] dp = new int[len1 + 1][len2 + 1];
   
    for (int i = 0; i <= len1; i++)
      dp[i][0] = i;
    
   
    for (int j = 0; j <= len2; j++)
      dp[0][j] = j;
    
   
    //iterate though, and check last char
    for (int i = 0; i < len1; i++) 
    {
      char c1 = word1.charAt(i);
      for (int j = 0; j < len2; j++) 
      {
        char c2 = word2.charAt(j);
   
        //if last two chars equal
        if (c1 == c2) {
          //update dp value for +1 length
          dp[i + 1][j + 1] = dp[i][j];
        } 
        else 
        {
          int replace = dp[i][j] + 1;
          int insert = dp[i][j + 1] + 1;
          int delete = dp[i + 1][j] + 1;
   
          int min = replace > insert ? insert : replace;
          min = delete > min ? min : delete;
          dp[i + 1][j + 1] = min;
        }
      }
    }
    return dp[len1][len2];
  }

  public void querySearch(String _word) 
  {
      Set<String> answer = new HashSet<String>();
      String word = _word.toLowerCase();
      List<Integer> idx = index.get(word);

      if(idx==null)
        word=editDistance(word);
      
      idx = index.get(word);

      for (Integer t : idx) 
        answer.add(documents.get(t-1));
      
      
      System.out.print(word);
      for (String f : answer) 
        System.out.println(" " + f);
      
      System.out.println("");
  }
  
  // solution
  public void start() 
  {
    Scanner reader = new Scanner(System.in);
    System.out.println("Enter input directory: ");
    String input = reader.nextLine();
    System.out.println("Enter output file: ");
    String output = reader.nextLine();
    System.out.println("Enter the query word: ");
    String query = reader.nextLine();
    reader.close();
    
    try 
    {
      File files[] = new File(input).listFiles();
      System.out.println("Creating index.");
      
      for(File file : files) 
      {
        System.out.println("Processing: " + file);
        create(file); // create index
      }
      System.out.println("Writing.");
      // write it back
      write(new File(output));
      System.out.println("Done.");

      System.out.println("Searching the Query");
      querySearch(query);
    } 
    catch(IOException ex) 
    {
      // error
      throw new IllegalStateException(ex.getMessage(), ex);
    } 
    finally 
    {
      reader.close();
    }
  }

  public static void main (String args[]) throws Exception
  {    
    IRSystem ir = new IRSystem(); 
    ir.start(); 
  }
  private class Tuple 
  {
      private String word;
      private int dist;

      public Tuple(String word, int dist) 
      {
          this.word = word;
          this.dist = dist;
      }
  }   
}
