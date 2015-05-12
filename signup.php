<?php // signup.php

include("common.php");
include("db.php");
dbConnect("survey");
//if (!isset($_POST['submitok'])):
	// Display the user signup form
?>

<!DOCTYPE html PUBLIC "-//W3C/DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Signup</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="login-box.css" type="text/css" />
	</head>
	<body>
	<div class="login-box1">
	<h1 style="color:white;margin-left:42%;margin-top:6%;font-size:52px;">ScrapMe</h1>
	<form method="post" action="congrats.php" style="margin-left:39%;">
	<table border="0" cellpadding="0" cellspacing="5">
		<tr>
			<td align="right">
				
			</td>
			<td>
				<input class="form-login" placeholder="Full name" name="newname" type="text" maxlength="100" size="25" />
			</td>
		</tr>
		<tr>
			<td align="right">
				
			</td>
			<td>
				<input class="form-login" placeholder="Mail Address" name="newemail" type="text" maxlength="100" size="25" />
			</td>
		</tr>
		<tr>
			<td align="right">
				
			</td>
			<td>
			    <input class="form-login" style="display:none;" placeholder="User ID" name="newid" type="text" maxlength="100" size="25" />
				<input class="form-login" placeholder="User ID" name="newid" type="text" maxlength="100" size="25" />
			</td>
		</tr>
		<tr>
			<td align="right">
				
			</td>
			<td>
				<input class="form-login"  placeholder="Password" name="newpass" type="password" maxlength="16" size="25" />
			</td>
		</tr>
	</table>
	
				<input type="image" src="signup@.png" name="submitok" value="   OK   " style="width:24%; height:7%;margin-left:51px;" />
	</form>
</div>
	</body>
</html>

