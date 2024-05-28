# Cleanup directories
hadoop fs -rm -r -f /wordcount-output
hadoop fs -rm -r -f /wordcount
# Create input directory and add text file
hadoop fs -mkdir /wordcount
hadoop fs -put files/input/big.txt /wordcount
# Run MapReduce Program
 hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.bd.lab.wordcount.WCRunner /wordcount/big.txt /wordcount-output