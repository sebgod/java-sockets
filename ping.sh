#!/bin/env sh

sudo java -classpath rocksaw/lib/*:vserv-tcpip/lib/*:rocksaw/build.src -Djava.library.path=rocksaw/lib example.Ping $*
