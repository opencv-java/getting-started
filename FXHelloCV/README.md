# Get build tools
## OS X
Download build tools such as cmake and gcc

# Get OpenCv .jar and native libraries
## Build from sources on Linux and OS X
(tested on OS X)
Make sure you have installed the build tools as shown in previous section
- <code>mkdir -p /Users/pepito/bin/</code> where /Users/pepito is your user folder
- <code>git clone git://github.com/Itseez/opencv.git</code>
- <code>cd /Users/pepito/bin/opencv</code>
- <code>mkdir build ; cd build</code>
- <code>cmake -DBUILD_SHARED_LIBS=OFF ..</code>
- <code>make -j8</code>
## Download prebuild binaries
?

# Gradle
First configure OpenCv

## Configure gradle
- Open or create gradle.properties file
- add <code>opencv=/Users/pepito/bin/opencv</code>
## Run
<code>./gradlew run</code>

## Adapt this gradle configuration to other projects
### If you have gradle installed on PC
- copy this README.md to root folder
- copy build.gradle to root folder
- copy .gitignore to root folder
- <code>gradle wrapper --gradle-version=2.14.1</code> to download gradle scripts version
- Edit build.gradle so that your <code>main.resources.srcDir</code> (inside build.gradle) points to your resources like .fxml and .css files
- Make sure that your source code sits directly under 'src' folder, or edit <code>main.java.srcDir('src')</code> accordingly
- If linking to an opencv version different than 3.1.0 edit <code>def openCvJar="$opencv/build/bin/opencv-310.jar"</code> accordingly
### If you don't have gradle installed on PC
- Do all the instructions as above, skipping the <code>gradle wrapper . . .</code>
- Copy files <code>gradlew</code>, <code>gradlew.bat</code> and folder <code>gradle</code> to root folder

# Configure Intellij
- First configure gradle
- Choose 'Open existing project', navigate to build.gradle file and choose import

## Build
- Open project settings / project. Set project language level to 8

## Run
- Right click FXHelloCv
- Click either Run or Debug
- It will fail to find opencv native library.
- Now open the run configuration,and add -Djava.library.path=/Users/pepito/bin/ to the JVM arguments

# Add and run tests
- Add junit dependency to gradle
- Add test source code under <code>src/test/java</code>
- Run <code>./gradlew test</code>