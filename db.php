<?php // db.php

$dbhost = 'localhost';
$dbuser = 'your_username';
$dbpass = 'your_password';

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
