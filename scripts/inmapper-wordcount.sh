# Cleanup directories
hadoop fs -rm -r -f /inmapper-wordcount-output
hadoop fs -rm -r -f /inmapper-wordcount
# Create input directory and add text file
hadoop fs -mkdir /inmapper-wordcount
hadoop fs -put files/input/big.txt /inmapper-wordcount
# Run MapReduce Program
 hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.bd.lab.inmapperwordcount.WCInMapperRunner /inmapper-wordcount/big.txt /inmapper-wordcount-output