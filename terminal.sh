#!/bin/bash

java -javaagent:/snap/intellij-idea-community/163/lib/idea_rt.jar=41213:/snap/intellij-idea-community/163/bin -Dfile.encoding=UTF-8 -classpath \
$HOME/NetBeansProjects/Terminal/Terminal/target/classes:\
$HOME/.m2/repository/commons-dbcp/commons-dbcp/1.2/commons-dbcp-1.2.jar:\
$HOME/.m2/repository/commons-collections/commons-collections/2.1/commons-collections-2.1.jar:\
$HOME/.m2/repository/commons-pool/commons-pool/1.2/commons-pool-1.2.jar:\
$HOME/.m2/repository/xml-apis/xml-apis/1.0.b2/xml-apis-1.0.b2.jar:\
$HOME/.m2/repository/xerces/xercesImpl/2.0.2/xercesImpl-2.0.2.jar:\
$HOME/.m2/repository/mysql/mysql-connector-java/8.0.16/mysql-connector-java-8.0.16.jar:\
$HOME/.m2/repository/com/google/protobuf/protobuf-java/3.6.1/protobuf-java-3.6.1.jar:\
$HOME/.m2/repository/com/google/zxing/core/3.4.0/core-3.4.0.jar:\
$HOME/.m2/repository/com/google/zxing/javase/3.4.0/javase-3.4.0.jar:\
$HOME/.m2/repository/com/beust/jcommander/1.72/jcommander-1.72.jar:\
$HOME/.m2/repository/com/github/jai-imageio/jai-imageio-core/1.4.0/jai-imageio-core-1.4.0.jar:\
$HOME/.m2/repository/com/github/sarxos/webcam-capture/0.3.12/webcam-capture-0.3.12.jar:\
$HOME/.m2/repository/com/nativelibs4java/bridj/0.7.0/bridj-0.7.0.jar:\
$HOME/.m2/repository/org/slf4j/slf4j-api/1.7.2/slf4j-api-1.7.2.jar:\
$HOME/.m2/repository/com/intellij/forms_rt/7.0.3/forms_rt-7.0.3.jar:\
$HOME/.m2/repository/asm/asm-commons/3.0/asm-commons-3.0.jar:\
$HOME/.m2/repository/asm/asm-tree/3.0/asm-tree-3.0.jar:\
$HOME/.m2/repository/asm/asm/3.0/asm-3.0.jar:\
$HOME/.m2/repository/com/jgoodies/forms/1.1-preview/forms-1.1-preview.jar:\
$HOME/.m2/repository/jdom/jdom/1.0/jdom-1.0.jar Terminal