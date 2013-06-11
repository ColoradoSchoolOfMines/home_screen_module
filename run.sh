#!/usr/bin/env bash
#
# run.sh
# Copyright (C) 2013 Andrew DeMaria (muff1nman) <ademaria@mines.edu>
#
# All Rights Reserved.
#


mvn clean package
if [[ $? -ne "0" ]];
then
  echo "Issue with maven build"
  exit 1
fi
cd ..
cp home_screen_module/target/home_screen-0.0.1.jar running/modules/
cd running
java -jar interfacesdk-0.0.6-jar-with-dependencies.jar --manifest manifest.xml
cd ..
cd home_screen_module


