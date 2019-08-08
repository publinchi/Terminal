#!/bin/bash

mkdir -p ~/.Terminal
cd ~/.Terminal
git clone https://github.com/publinchi/Terminal.git
cd Terminal
git pull
mvn clean package install -DskipTests=true
cp Terminal.desktop ~/Escritorio
sed -i 's+/$HOME+'"$HOME"'+g' ~/Escritorio/Terminal.desktop