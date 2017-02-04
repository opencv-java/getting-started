## Getting started with OpenCV and JavaFX

*Computer Vision course - [Politecnico di Torino](http://www.polito.it)*

Two simple projects for getting started with the [OpenCV](http://opencv.org) library (version 3.x) and with JavaFX (version 2 or superior).

Please, note that the `helloCV` projects is an IntelliJ project that uses gradle. The other is an Eclipse project, without gradle. They were realized for teaching purposes. Before using any of them, you need to install the OpenCV library and create a `User Library` named `opencv` that links to the OpenCV jar and native libraries.

A guide for getting started with OpenCV and Java is available at [http://opencv-java-tutorials.readthedocs.org/en/latest/index.html](http://opencv-java-tutorials.readthedocs.org/en/latest/index.html).


### Prepare a new gradle project with OpenCV
* install OpenCV
* mkdir $folder/getting-started/HelloCV/app/libs/
* cp ../build/bin/opencv-310.jar $folder/getting-started/HelloCV/app/libs/
* cp ../build/lib/libopencv_java310.so $folder/getting-started/HelloCV/app/libs/
* under OS X only - cp $folder/getting-started/HelloCV/app/libs/libopencv_java310.so $folder/getting-started/HelloCV/app/libs/libopencv_java310.dylib

### Compile
* ./gradlew assemble

### Run
* java -cp 'app/build/libs/app.jar:app/libs/*'  it.polito.elite.teaching.cv.HelloCV
