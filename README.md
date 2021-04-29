# Experiment-Automata
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://raw.githubusercontent.com/CMPUT301W21T03/Experiment-Automata/main/LICENSE)
[![Android CI](https://github.com/CMPUT301W21T03/Experiment-Automata/actions/workflows/android.yml/badge.svg)](https://github.com/CMPUT301W21T03/Experiment-Automata/actions/workflows/android.yml)
[![Plant Uml](https://github.com/CMPUT301W21T03/Experiment-Automata/actions/workflows/plantuml.yaml/badge.svg)](https://github.com/CMPUT301W21T03/Experiment-Automata/actions/workflows/plantuml.yaml)
[![Issues](https://img.shields.io/github/contributors/CMPUT301W21T03/Experiment-Automata)](https://github.com/CMPUT301W21T03/Experiment-Automata/graphs/contributors)
[![Issues](https://img.shields.io/github/issues/CMPUT301W21T03/Experiment-Automata)](https://github.com/CMPUT301W21T03/Experiment-Automata/issues) 

Semester W21 project for CMPUT 301 at the University of Alberta

OSMDROID - L:Apache License 2.0 - https://github.com/osmdroid/osmdroid  
ZXING - L:Apache License 2.0 - https://github.com/zxing/zxing  
zxing-android-embedded - L:Apache License 2.0 - https://github.com/journeyapps/zxing-android-embedded  

# Details

The goal of this application is to build results for custom experiments that can crowed sourced

## Goals

### Adding an Experiment

When the plus sign is pressed in places not lgically realted to trials this screen will apear. 

![Add Experiment pop up Window](https://github.com/CMPUT301W21T03/Experiment-Automata/blob/main/doc/Storyboard/Screens/Add%20Experiment.png)

### Opening an Experiment 

Taping on an experiment will get the experiment detials which will show everything logically related to an experiment. 
Ex: Location, Trial Data, Trial Histogram, Trial Graph Plot, The Used Trials....
Locations: Accesed by pressing the map button. ![Location Data](https://github.com/CMPUT301W21T03/Experiment-Automata/blob/main/doc/Storyboard/Screens/View%20Locations.png)
Trial Data:
* Trial Histogram: First item you see when looking through visualization. 
* Trial Graph Plot: Second item you see when looking through data visualization.
* Used Trials: A list of all the trials being used by the experiment to make generate the graphs. Trials can only be modified by the lead experimenter.

### Adding Trials (Adding Data to an Experiment)

Pressing the plus button on the experiment details page will give you the pop up window to add an experiment of the trial for that experiment. 
Depending on the lead experimenters prefrences the you may need to add a location. 
![Experiment Binomial Trial](https://github.com/CMPUT301W21T03/Experiment-Automata/blob/main/doc/Storyboard/Screens/Add%20Binomial%20Trial%20Loc.png)

Other features as displayed in our demo video
https://youtu.be/I7fxqJsjcz0
