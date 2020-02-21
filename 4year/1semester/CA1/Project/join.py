from os import listdir
from os.path import isfile, join
import sys

dirs = listdir(".")
onlyfiles=[]

for file in dirs:
	if isfile(join(".",file)) and ".ts" in file:
		onlyfiles.append(file)

final=[]
for i in range(0,len(onlyfiles)):
	final.append("file 'out"+str(i)+"."+sys.argv[1]+".ts'")

with open('list.txt','w') as f:
	for item in final:
		print >> f, item
