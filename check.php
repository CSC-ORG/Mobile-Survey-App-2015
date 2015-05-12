<?php
	// Process signup submission
	include("accesscontrol.php");
	dbConnect("survey");
	$uid = $_POST['uid'];
$pwd = $_POST['pwd'];

if (!isset($_SESSION['uid'])){
	$_SESSION['uid'] = $uid;

}

dbConnect("survey");
$sql = "SELECT * FROM user WHERE userid = '$uid' AND password = PASSWORD('$pwd')";
$result = mysql_query($sql);
if (!$result)
{
	error('A database error occurred while checking your login details.');
}
if (mysql_num_rows($result) != 0){
	$row = mysql_fetch_array($result);
	$_SESSION['email'] = $row['email'];
}
if (mysql_num_rows($result) == 0)
{
	unset($_SESSION['uid']);
	error("Your user ID or password is incorrect, or you are not a registered user on this site.");
}
else
header('Location: test.php');
?>

