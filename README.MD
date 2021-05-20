# command chain preset:

```
javac *.java

cat <filename> | java CreateRuns <heap size> | java MergeRuns <num files> > <output file name>
```

e.g.
```
javac *.java

cat BrownCorpus.txt | java CreateRuns 100 | java MergeRuns 100 > out.txt
```


# limitations:

On one tested pc using linux filestystem, using a heapsize of less than 6 a large file (BrownCorpus.txt),
we ran into a (Too many open files) IO Exception.
This error did not occur with:
- larger heapsize than 6
- another pc test using widnows filesystem regardless of heapsize smaller than 6
- smaller text files as input