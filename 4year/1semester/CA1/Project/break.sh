#!/bin/sh

IFS='.' read -r -a array <<< "${1}"

mkdir logs
COM="ffmpeg -i ${1} -c copy -map 0 -segment_time 5 -f segment -reset_timestamps 1 %01d.${array[1]}"
exec $COM