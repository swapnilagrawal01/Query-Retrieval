# Query Retrieval
Java program interacts with user to process information retrieval queries using Inverted Index from a collection of text documents in a given directory.
The program will prompt for three pieces of information: the input directory, the inverted index output file and the query. When the input directory is received, the program will read the text files contained in that directory. The program will write the inverted index to the output file.<br/><br/>
The program handles single-term queries only. However,it is also able to handle misspelled queries. If a query term is submitted that is not contained in our index, the program determines a candidate list of 3 possible  matches. It uses the edit distance to determine the distance from each candidate to the misspelled query term. The program display each  candidate and distance in order (decreasing order in terms of distance) and allow the user to select the one he or she wants.The program then displays the documents relevant to that candidate as the query term<br/><br/>

To Execute the file, complile and run IRSystem.java
