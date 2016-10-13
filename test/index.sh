#!/bin/bash

# test aosp patch code

CUR_DIR=$(cd `dirname $0`;pwd)
cd $CUR_DIR
cd ../AndroidContainer
./gradlew cAT
