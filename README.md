# Demo App

## How to build the app

To build the app, use maven install to generate demo-0.0.1-bin.zip in the target folder.

## How to run the app

To run the app, simply unzip the demo-0.0.1-bin.zip to generate the demo-0.0.1 folder. Then go to the demo-0.0.1 folder, run the following command to start the app:

./bin/start.sh

To stop the app, run the ./bin/stop.sh

## Algorithm discussion

The simplest way of processing and aggregating the stream data is:

1. Read stream data line by line and filter the data with "success" status
2. Use the device^title^country of the stream data as key and put the data into a hashmap. The value of the hashmap is the count of the stream data which has the same key
3. Keep doing the aggregation until the timestamp of the stream data reaches to the next second
4. Print the aggregated result


## Further discussion
1. How would you scale this if all events could not be processed on a single processor, or a single machine?
If all events could not be processed on a single processor, or a single machine, we could split the workload to multiple processors/machines, each processor/machine processes and aggregates partial data and generate a partial result, then the partial results will be merged to generate the final result. Using hadoop map reduce function fits this case well. This can be run on hadoop or spark. 

2. How can your solution handle variations in data volume throughout the day?
I am not quite clear about the question, but below is what I thought based on my own understanding. 
If we know the data volume pattern in front or the data volume pattern is predictable throughout the day, we can schedule the job accordingly. For example, if the data volume is stable throughout the day, we can specify the fixed number of executors when we submit the job (for example a spark job). If the data volume changes a lot throughout the day, we can consider to use the dynamic resource allocation feature provided by YARN or Mesos to better utilize the shared resource with other applications.  

3. How would you productize this application?
To productize the application, I will do the following things:
a. Optimize the code and make it able to handle large amount of traffic
b. Add proper error handling to make sure the application can handle different errors and can continuously run without manually restart 
c. Add unit and functional test to achieve high test coverage
d. Write user-friendly documentation and user guide
e. Host application source code and deliverables in a proper place so that it is easily accessible by customers
...
4. How would you test your solution to verify that it is functioning correctly?
Verify the functional correctness with live streaming data is difficult. I would test my solution with some static data source to verify the aggregated result is correct or not. The static data source I use for testing should have the same data format and possible values with the live streaming data. 
