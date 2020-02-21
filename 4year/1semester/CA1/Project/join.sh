#!/bin/sh

mkdir output

eval "python join.py ${1}"

ffmpeg -f concat -safe 0 -i list.txt -c copy  -bsf:a aac_adtstoasc "output/${2}"
