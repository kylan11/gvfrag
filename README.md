[![N|Solid](http://www.greenvulcanotechnologies.com/wp-content/uploads/2017/04/logo_gv_FLAT-300x138.png)](http://www.greenvulcanotechnologies.com)
# GV Frag - Fragmenting Utility Software for GAIA Projects

## What is it
This is a work-in-progress, simple, open-source and **experimental** utility software that makes it easier for developers to work on a single GAIA Project concurrently. So far, each GAIA deploy configuration resided in a single, huge XML file, making it a nightmare for projects that required multiple developers. Changes had to be merged manually. GVFrag solves that a provides a simple fragmenting structure that DOES NOT lose any data.

## Usage

Clone the gvfrag.jar file from this repository and place it anywhere.

Then, run by shell by typing the following command (**ONLY WORKING WITH JAVA 14 AND LINUX FS**)
```shell
java -jar gvfrag.jar [-S|-M] /path/to/[GVCore.xml|GVFrag.xml]
```
**-S** (SPLIT) Takes GVCore.xml as input, and produces (in an output folder where the JAR file is located) multiple direcories that represent the tags, (such as GVServices/, GVSystems/, GVPolicy/, GVAdapters/) and a skeleton file (**GVFrag.xml**) that contains the remaining tags which were NOT split. 
**-M** (MAKE) Takes GVFrag.xml as input, and searches for the necessary directories (such directories must be in the same folder as GVFrag.xml) and produces a fully functioning GVCore.xml file.
**--help** Displays a useful help message.

#### DO NOT USE IN PRODUCTION ENVIRONMENTS. GVFrag IS LARGELY UNTESTED AND MAY RESULT IN XML DATA CORRUPTION.
