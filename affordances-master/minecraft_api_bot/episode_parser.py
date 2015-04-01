import sys

worldNum = sys.argv[1]

episodeFile = file("planResult_world" + worldNum + ".episode")

outfile = file("plan_world" + worldNum + ".p","w")

for line in episodeFile:
	if line[0].isalpha():
		line = line.strip() + ","
		outfile.write(line)
