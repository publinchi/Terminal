#!/bin/bash

java -javaagent:$HOME/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-1/202.7660.26/lib/idea_rt.jar=44451:\
$HOME/.local/share/JetBrains/Toolbox/apps/IDEA-U/ch-1/202.7660.26/bin \
-Dfile.encoding=UTF-8 \
-classpath \
/usr/lib/jvm/zulu-8-amd64/jre/lib/charsets.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/cldrdata.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/dnsns.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/jaccess.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/legacy8ujsse.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/localedata.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/mysql-connector-java-8.0.19.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/nashorn.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/openjsse.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/sunec.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/sunjce_provider.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/sunpkcs11.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/ext/zipfs.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/jce.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/jfr.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/jsse.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/management-agent.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/resources.jar:\
/usr/lib/jvm/zulu-8-amd64/jre/lib/rt.jar:\
$HOME/.Terminal/Terminal/target/classes:\
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
$HOME/.m2/repository/jdom/jdom/1.0/jdom-1.0.jar \
com.terminal.view.MainGUI