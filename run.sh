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
cp home_screen_module/target/home_screen-0.0.1.jar running4/modules/
cd running4
java -jar interfacesdk-0.1.0-jar-with-dependencies.jar --manifest manifest.xml
cd ..
cd home_screen_module


