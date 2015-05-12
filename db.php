<?php // db.php

$dbhost = 'localhost';
$dbuser = 'alok';
$dbpass = 'zeal';

function dbConnect($db='')
{
	global $dbhost, $dbuser, $dbpass;

	$dbcnx = @mysql_pconnect($dbhost, $dbuser, $dbpass)
		or die('The site database appears to be down.');

	if ($db!='' and !@mysql_select_db($db))
		die('The site database is unavailable.');
    //echo "alert('success');";
	return $dbcnx;
}
?>