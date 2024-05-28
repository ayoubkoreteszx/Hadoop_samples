# Cleanup directories
hadoop fs -rm -r -f /average-output
hadoop fs -rm -r -f /average
# Create input directory and add text file
hadoop fs -mkdir /average
hadoop fs -put files/input/access_log /average
# Run MapReduce Program
 hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.bd.lab.average.AverageRunner /average/access_log /average-output