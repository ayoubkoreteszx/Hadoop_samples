# Cleanup directories
hadoop fs -rm -r -f /inmapper-average-output
hadoop fs -rm -r -f /inmapper-average
# Create input directory and add text file
hadoop fs -mkdir /inmapper-average
hadoop fs -put files/input/access_log /inmapper-average
# Run MapReduce Program
hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.bd.lab.inmapperaverage.InMapperAverageRunner /inmapper-average/access_log /inmapper-average-output