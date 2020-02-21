#!/bin/sh

echo "Duration:"
ffprobe -i $1 -show_format -v quiet | sed -n 's/duration=//p'

echo "Ndivs[1]:"
mkdir tmp
ffmpeg -i ${1} -c copy -map 0 -segment_time 5 -f segment -reset_timestamps 1 tmp/%01d.mp4 -v quiet

ls tmp/ | wc

rm -rf tmp

