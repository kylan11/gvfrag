[![N|Solid](http://www.greenvulcanotechnologies.com/wp-content/uploads/2017/04/logo_gv_FLAT-300x138.png)](http://www.greenvulcanotechnologies.com)
# GV Frag - Fragmenting Utility Software for GAIA Projects

## What is it
This is a work-in-progress, simple, open-source and **experimental** utility software that makes it easier for developers to work on a single GAIA Project indipendently and concurrently. So far, each GAIA deploy configuration rested in a single, huge XML file, making it a nightmare for medium-big sized projects that required multiple developers. Changes had to be merged manually. GVFrag solves that and provides a simple fragmenting structure that does not lose any data. 
Each developer can add features, modify services and merge each change into a single Git repository. 


## Usage

Clone the gvfrag.jar file from this repository and place it anywhere.

Then, run by shell by typing the following command (**needs Java 11**)
```shell
java -jar gvfrag.jar [-S|-M|-h] /path/to/[GVCore.xml|GVFrag.xml] [/optional/output/dir]
```
- **-s** (SPLIT) Takes GVCore.xml as input, and produces (in an output folder where the JAR file is located) multiple direcories that represent the tags, (such as GVServices/, GVSystems/, GVPolicy/, GVAdapters/) and a skeleton file (**GVFrag.xml**) that contains the remaining tags which were NOT split. 

- **-m** (MAKE) Takes GVFrag.xml as input, and searches for the necessary directories (such directories must be in the same folder as GVFrag.xml) and produces a fully functioning GVCore.xml file.

- **-h or --help** Displays a useful help message.

**GVFrag only works on Linux/Macintosh file systems**.

#### WARNING: DO NOT USE IN PRODUCTION ENVIRONMENTS. GVFRAG IS AN EXPERIMENTAL FEATURE AND MAY RESULT IN XML DATA CORRUPTION.
