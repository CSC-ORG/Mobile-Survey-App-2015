<?php // accesscontrol.php
session_start();

include_once 'common.php';
include_once 'db.php';

if(isset($_SESSION['uid']) && isset($_POST['uid']))
header('Location:test.php');
?>

<!DOCTYPE html PUBLIC "-//W3C/DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Login</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="login-box.css" type="text/css" />
	</head>
	<body>
		<div class="login-box" style="margin-left:-.68%;">
		    <div class="top-heading">
            <h1> <a href="test.php">Survey</a></h1>
		    </div>
			<form method="post" autocomplete="off" action="check.php" style="margin-left:522px;">
				<div id="login-box-name" style="margin-top:20px;display:none;">Username:</div>
				<div id="login-box-field" style="margin-top:20px;"><input class="form-login" style="display:none;" type="text" autocomplete="off" value="" name="uid" placeholder="Username"/><input class="form-login" type="text" autocomplete="off" value="" name="uid" placeholder="Username"/></div><br />
				<div id="login-box-name" style="margin-bottom:20px;display:none;">Password:</div>
				<div id="login-box-field" style="margin-bottom:20px;margin-top:-20px;"><input class="form-login" autocomplete="off" style="border-top:1px solid grey;" placeholder="Password" type="password" name="pwd" /></div>
				<p><input type="image" src="/extra/loginbutton@.png" value="Sign In" height="38px" width="224px"></p>
			</form><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			<span class="login-box-options"><h3 style="float:left;color:white;margin-left:2%;">Not a member? </h3> <a href="signup.php"><h3 style="float:left;color:white;">&nbspSign up now</h3></a></span>
		</div>
	</body>
</html>
