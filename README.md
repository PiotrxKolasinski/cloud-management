# Cloud-management
Project simulation Cloud Management. The project aims to test raft and multi-master algorithms in cloud environment. The program allows to test the version of static and dynamic algorithms.

Project consists of two modules. The first is the java module responsible for all cloud management and algorithms. The second (graphs-generator) is a module written in the Python programming language. It is a Flusk server and it is responsible for generating a summary of the algorithms results.

## Run
First, start the graphs-generator Flusk server. Then run the main java file

### Data inputs
Before run main java file:
1. Go to folder: /src/main/resources 
2. Set data inputs in file dataInputs.json
3. Set parameters in file application.properties

### Result
Result of the program operation can be seen in the folder:<br />
/graphs-generator/src/main/generated

### Generated files
Program generates graphs of the percentage of successful accesses and average access time for each algorithm. The static and dynamic version of the algorithm is presented on one graph. In addition, a csv file is created with a summary of the results.


