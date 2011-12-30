#!/bin/bash
function cleanup()
{
    echo ${1};
    for file in `find . -name "${1}" -type f`
    do
        target=`basename "${file}" `
        echo ${file}
        echo ${target}
        dos2unix ${file}
        expand -t 2 ${file} > /tmp/${target}
        sed 's/[ \t]*$//' /tmp/${target} > /tmp/${target}.notrailing
        cp -f /tmp/${target}.notrailing  ${file}
        rm -f /tmp/${target}
        rm -f /tmp/${target}.notrailing
    done
}

cleanup "pom.xml"
cleanup "*.java"
