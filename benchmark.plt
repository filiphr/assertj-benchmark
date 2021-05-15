# Labels
set title 'AssertJ Object Assert'
set ylabel 'Assertions per second'
set xlabel 'Operation'
set xtics nomirror rotate by -45


# Ranges
set autoscale

# Input
set datafile separator ','

if (!exists("output")) output='results.png'
if (!exists("results")) results='results.csv'

# Output
set terminal png enhanced font "Verdana,9"
set output output
set grid
set key off
set boxwidth 0.8 relative


# box style
set style line 1 lc rgb '#5C91CD' lt 1
set style fill solid

# remove top and right borders
set style line 2 lc rgb '#808080' lt 1
set border 3 back ls 2
set tics nomirror

plot results every ::1 using 0:5:xticlabels( stringcolumn(1)[23 + strstrt(stringcolumn(1)[23:], "."):] . " (" . stringcolumn(8) . ")") with boxes ls 1,\
     results every ::1 using 0:5:6 with yerrorbars ls 1,\
     results every ::1 using 0:($5 + 1500):(sprintf("%d",$5)) with labels offset char 0,1
