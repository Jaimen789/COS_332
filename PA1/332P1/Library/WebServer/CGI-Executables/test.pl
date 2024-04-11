#!/usr/bin/perl

# Jaimen Govender (u20464348)
# Priyul Mahabeer (u20421159)

print "Content-type: text/html\n\n";


print "<meta http-equiv='cache-control' content='no-cache'/>";

print "<html>";
print "<head>";

print "</head>";
print "<body>";
print "<h1>Click on the biggest of the 2 numbers!</h1>";

$random_number1 = int(rand(100));
$random_number2 = int(rand(100));

if ($random_number1 > $random_number2) {
    print "<a href = '../right.htm'>$random_number1         </a> <br><br>";
    print "<a href = '../wrong.htm'>$random_number2         </a>";
} else {
    print "<a href = '../right.htm'>$random_number2         </a><br><br>";
    print "<a href = '../wrong.htm'>$random_number1         </a>";
}



print "</body>";
print "</html>";