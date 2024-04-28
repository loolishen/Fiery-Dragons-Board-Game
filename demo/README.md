# FIT3077 Sprint 2
# Fiery Dragons


NOTE: The executables were built using Maven based on the pom.xml file. The output files should be located in a directory called 'target'. The YouTube link at the bottom of this README provides a short walkthrough
To produce the zip file: Use the pom.xml file as-is, and run the command ```mvn javafx:jlink``` using Maven.
To produce the jar file: Uncomment the block below:
```
<!--    <build>-->
<!--        <plugins>-->
<!--            <plugin>-->
<!--                <artifactId>maven-assembly-plugin</artifactId>-->
<!--                <configuration>-->
<!--                    <archive>-->
<!--                        <manifest>-->
<!--                            <mainClass>com.example.demo.FieryDragonsApplication</mainClass>-->
<!--                        </manifest>-->
<!--                    </archive>-->
<!--                    <descriptorRefs>-->
<!--                        <descriptorRef>jar-with-dependencies</descriptorRef>-->
<!--                    </descriptorRefs>-->
<!--                </configuration>-->
<!--            </plugin>-->
<!--        </plugins>-->
<!--    </build>-->
```
And comment out this block:
```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>19</source>
                    <target>19</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <executions>
                    <execution>
                        <!-- Default configuration for running with: mvn clean javafx:run -->
                        <id>default-cli</id>
                        <configuration>
                            <mainClass>com.example.demo.FieryDragonsApplication</mainClass>
                            <launcher>start-game</launcher>
                            <jlinkZipName>fieryDragons</jlinkZipName>
                            <jlinkImageName>fieryDragons</jlinkImageName>
                            <noManPages>true</noManPages>
                            <stripDebug>true</stripDebug>
                            <noHeaderFiles>true</noHeaderFiles>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
Then, open Maven and reload. Go to the Plugins directory, and into the assembly directory, then select 'assembly:single' and run the command. 

## demo/docs
This folder contains the single PDF containing all drawn/written deliverables

## demo/executables

This folder contains two options for launching the game:
- zip file: fieryDragons.zip
  - Platform: Windows
  - Steps to run application:
    - Download and extract zip file.
    - Open a terminal and navigate to fieryDragons/bin, where 'fieryDragons' is the extracted folder. Example ```cd Downloads/fieryDragons/bin```
    - Run the command ```./start-game``` to launch the game application.
- .jar file: fieryDragons-1.0.jar
  - Platform: Windows
  - JDK version: 22
    - To check java version, in a terminal run ```java -version```. It should show something like  
    **java version "22.0.1" 2024-04-16  
    Java(TM) SE Runtime Environment (build 22.0.1+8-16)  
    Java HotSpot(TM) 64-Bit Server VM (build 22.0.1+8-16, mixed mode, sharing)**
  - Steps to run application:
    - Download the .jar file.
    - Open a terminal and navigate to the directory where the .jar file is located.
    - Run the command ```java -jar fieryDragons-1.0.jar``` to launch the game application

## demo/src/main
This folder contains two subfolders for the Technical Work-In-Progress:
- resources
  - This folder contains a folder 'com/example/demo/assets' which contains all the .png resources for the UI.
- java
  - This folder contains a folder 'com/example/demo' which contains all the Java source code.
Currently, the code does not support creating random segments of arbitrary length. Additionally, some parts of the code suffer from complex logic(math based, and involving indexes) that could have simpler solutions. 

More comprehensive testing of the various functionalities of the FieryDragons game is demonstrated with this video:
https://youtu.be/9h_PM7vrw9w

## demo/video
This folder contains videos of the prototype demonstration.
Youtube link with timestamps:
https://youtu.be/2bmGBBa5XHw
Video where 2/3 players can play the game:
https://youtu.be/HlQhavesGrk

