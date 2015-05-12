<?php
	// Process signup submission
	include("signup.php");
	dbConnect("survey");

	if ($_POST['newname']=='' or $_POST['newemail']=='' or $_POST['newid']=='' or $_POST['newpass']=='')
	{
		error('One or more required fields were left blank.\\nPlease fill them in and try again.');
	}
	if(!filter_var($_POST['newemail'], FILTER_VALIDATE_EMAIL))
  {
  error("E-mail is not valid");
  }
	// Check for existing user with the new id
	$sql = "SELECT COUNT(*) FROM user WHERE userid = '$_POST[newid]'";
	$result = mysql_query($sql);
	if (!$result)
	{
		error('A database error occurred in processing your submission.');
	}

	if (mysql_result($result,0,0)>0)
	{
		error('A user already exists with your chosen userid.\\nPlease try another.');
	}
	$sql = "SELECT COUNT(*) FROM user WHERE email = '$_POST[newemail]'";
	$result = mysql_query($sql);
	if (!$result)
	{
		error('A database error occurred in processing your submission.');
	}

	if (mysql_result($result,0,0)>0)
	{
		error('A user already exists with your chosen email.\\nPlease try another.');
	}

    //error("hello");
	$sql = "INSERT INTO user SET 
				userid = '$_POST[newid]', 
				password = PASSWORD('$_POST[newpass]'), 
				fullname = '$_POST[newname]', 
				email = '$_POST[newemail]'";
	if (!mysql_query($sql))
		error('A database error occurred in processing your submission.\\n' . mysql_error());
?>

<!DOCTYPE html PUBLIC "-//W3C/DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Registration Complete </title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	    <link rel="stylesheet" href="login-box.css" type="text/css" />
	</head>
	<body>
	<div class="successful">
		<p><strong style="margin-left:41%;">User registration successful!</strong></p>
		<p style="margin-left:43%;">To log in, click <a href="protectedpage.php">here</a>.</p>
	</div>
	</body>
</html>
