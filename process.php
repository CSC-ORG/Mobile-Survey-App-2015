<html>
<head>
<title>Survey Creation</title>
<!--<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="bootstrap-glyphicons.css">
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap.min.js"></script>-->
<link rel="stylesheet" href="bootstrap.min.css">
<?php
include_once 'db.php';
include_once 'common.php';
dbConnect("survey");
if(isset($_POST['submit'])){
	$emailid='email';
	$sql="SELECT emailid
          FROM surveytable";
	$result = mysql_query($sql);
	$mailvalue = $_GET['para'];
	$maxsurvey=mysql_num_rows($result)+1;
	//echo "no of surveys are".$maxsurvey;
	$nameofsurvey=$_POST["nos"];
	$sql="INSERT INTO surveytable (emailid, surveytitle, surveyno)
		      VALUES ('$mailvalue','$nameofsurvey[0]','$maxsurvey')";
		$result = mysql_query($sql);
	     if (!$result) {
			error('A database no 1 error occurred while checking your login details.');
		}
 	$len=count($_POST["qtitle"]);
	//$i1=0
	//$help=count($_POST["qtitle"]);
	$i2=0;
	//$arr = array_values($_POST["qtitle"]);
	$arr=array_values($_POST["qtitle"]);
	$arr1=$_POST["htitle"];
	$arr2=$_POST["hidden1"];
	$arr3=$_POST["hidden2"];
	$arr4=$_POST["hidden3"];
	$arr5=$_POST["hidden4"];
	$arr6=$_POST["textvalue"];
	$arr7=$_POST["optionvalue"];
	$arr8=$_POST["thumbsource"];
	$arr9=$_POST["othervalue"];
	$arr10=$_POST["hidden5"];
	//$arr11=$_POST["hidden6"];
	echo "total no of options are ";
	print_r($arr7);
	echo "<br>";
	//print_r($arr11);
	//echo $arr8[0];
	$thumbimages=array();
	for($l=0;$l<6;$l++)
	$thumbimages[$l]="";
	$noOfOptions=0;
	$k2=0;
	$p3=0;
	for($i=0;$i<$len;$i++){
		//echo $arr[$i];
		//echo $arr1[$i];
		$textarea="";
		$otherarea="";
		$textdelimit1="";
		$textdelimit2="";
		$imagesrc="";
		$type="";
		$optionvalue="";
		$noOfOptions=0;
		//$i2=0;
		$k1=0;
		if($arr2[$i]=="text"){
			$textarea=$arr6[$i];
			$type="textbox";
		//	if($p3==0){
			$i2++;
		//	$p3=1;
		//}
		//else
			//$i2--;
           // echo $textarea;
		}
		else if($arr2[$i]=="multichoice"){
			$noOfOptions+=intval($arr4[$i]);
			echo "noofoption for this case ".intval($noOfOptions);
			$textdelimit1="";
			$type="radio";
			$p=0;
			//$i2=0;
			for(;$p<$noOfOptions;$p++){
				echo "value is".$i2."<br>";
				if($p<$noOfOptions-1)
				$textdelimit1.=$arr7[$i2++]."#";
			    else
			    $textdelimit1.=$arr7[$i2++];	
				$thumbimages[$k1]=$arr8[$k2];
				$k2+=1;
				$k1+=1;
				//echo "$".$i2." &".$arr7[$i2];
			}
			if($k1<6){
				for(;$k1<6;$k1++)
				$thumbimages[$k1]="";	
			}
            //echo $textdelimit1;
            $optionvalue=$textdelimit1;
		}
		else if($arr2[$i]=="checkbox"){
			$noOfOptions+=intval($arr4[$i]);
			echo "noofoption for this case ".intval($noOfOptions);
			//echo " ".$i2."hello";
			$textdelimit2="";
			$type="checkbox";
			$p=0;

			for(;$p<$noOfOptions;$p++){
				echo "value is".$i2."<br>";
				if($p<$noOfOptions-1)
				$textdelimit2.=$arr7[$i2++]."#";
			else
				$textdelimit2.=$arr7[$i2++];
			$thumbimages[$k1]=$arr8[$k2];
				$k2+=1;
				$k1+=1;
				//echo "$".$i2." &".$arr7[$i2];
			}
            if($k1<6){
				for(;$k1<6;$k1++)
				$thumbimages[$k1]="";	
			}
            //echo $textdelimit2;
            $optionvalue=$textdelimit2;
		}
		if($arr3[$i]=="image"){
			$imagesrc=($arr5[$i]);
			//echo '<img src="'.$imagesrc.'">';
		}
		if($arr10[$i]=="othertext"){
			$otherarea=$arr9[$i];
			//$optionvalue.="**".$otherarea;
			$type="other".$type;
			//$type="text";
            //echo $textarea;
		}
		$u=$i+1;
        $sql="INSERT INTO questiontable (surveyno, questiontitle, type, noofoptions, options, image, thumb0,/* thumb1, thumb2, thumb3, thumb4, thumb5,*/ help,qno)
		      VALUES ('$maxsurvey','$arr[$i]','$type','$noOfOptions','$optionvalue',
		  	  '($imagesrc)', '$thumbimages[0]', /*'$thumbimages[1]'/*, '$thumbimages[0]',
		  	   '$thumbimages[0]', '$thumbimages[0]', '$thumbimages[0]',*/ '$arr1[$i]','$u')";
		$result = mysql_query($sql);
	     if (!$result) {
			error('A database error occurred while checking your login details.');
		} 
	}
	//echo $_POST["helptitle"]."hoolabalo</br>"

?>
<script>
 
</script>
</head>
<body>
<div class="process">
<h1 style="margin-left:41%;">Congrats!!</h1>
<h3 style="margin-left:33%;">You have successfully created survey</h3>
<h4 style="margin-left:34%;">You can click over <a href="test.php">here</a> to make another survey</h4>
</div>

<!--<div class="row">
<div class="col-xs-2">Question title</div>
<div class="col-xs-2">Help text</div>
<div class="col-xs-2">Question type</div>
<div class="col-xs-2">Text question</div>
<div class="col-xs-2">Options</div>
<div class="col-xs-2">Images</div>
</div>-->
</body>
</html>
<?php
exit;
}
else{
?>
<!DOCTYPE html PUBLIC "-//W3C/DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Access Denied </title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<h1> Access Denied </h1>
		<p>Sorry you had to face this problem but since you have seen this page it means that either you are not a registered user or you have logged out from your present account.
		<br>To register for instant access, click <a href="signup.php">here</a>.
		<br>To log in again, click <a href="protectedpage.php">here</a>.</p>
	</body>
</html>

<?php
	exit;
}
?>
