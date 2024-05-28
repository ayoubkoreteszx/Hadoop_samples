# Cleanup directories
hadoop fs -rm -r -f /striprelativefrequency-output
hadoop fs -rm -r -f /striprelativefrequency
# Create input directory and add text file
hadoop fs -mkdir /striprelativefrequency
hadoop fs -put files/input/Input-for-TEAM-4.txt /striprelativefrequency
# Run MapReduce Program
 hadoop jar target/WordCount-1.0-SNAPSHOT.jar com.bd.lab.striprelativefrequency.RFRunner /striprelativefrequency/Input-for-TEAM-4.txt /striprelativefrequency-output