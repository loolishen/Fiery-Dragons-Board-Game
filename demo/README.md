# FIT3077 Sprint 2
# Fiery Dragons


NOTE: The executables were built using Maven based on the pom.xml file.

## demo/docs
This folder contains the single PDF containing all drawn/written deliverables

## demo/executables

This folder contains two options for launching the game:
- zip file: fieryDragons-1.0.zip
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
This folder contains two subfolders:
- resources
  - This folder contains a folder 'com/example/demo/assets' which contains all the .png resources for the UI.
- java
  - This folder contains a folder 'com/example/demo' which contains all the Java source code.

## demo/video
This folder contains a video of the prototype demonstration.
Youtube link with timestamps:
https://youtu.be/2bmGBBa5XHw
