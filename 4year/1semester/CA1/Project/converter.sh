#!/bin/sh

COM="ffmpeg -y -i $1 -vf scale=$2:-2,setsar=1:1 -c:v libx264 -c:a copy"
OUT=" out${1}"
FIRST=$COM$OUT

SND="ffmpeg -i out${1} -c copy -bsf:v h264_mp4toannexb out${1}.ts"

#ffmpeg -i "out${1}" -c copy -bsf:v h264_mp4toannexb "out${1}.ts"

eval $FIRST
eval $SND
