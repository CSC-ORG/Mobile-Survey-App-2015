<html>
<head>
<style>
body{
    background-image: url("light5.jpg");
    background-size: cover;
}
.container {
color: black;
text-decoration: none;
list-style-type: none;
}
a{
	color:black;
	text-decoration: none;
}
.container a{
    color:#121FA8;
}
</style>
<?php
include_once 'db.php';
include_once 'common.php';
dbConnect("survey");
$mailvalue = $_GET['para'];
$sql="SELECT surveytitle,surveyno
FROM surveytable WHERE emailid='$mailvalue'";
	$result = mysql_query($sql);
	if (!$result) {
			error('A database no 1 error occurred while checking your login details.');
		}
	$surveyname=array();
	$surveynumber=array();
	$k=0;
	while($row = mysql_fetch_array($result))
	{
	 if($row['surveytitle']!="")
	 {
      $surveyname[$k]=$row['surveytitle'];
      $surveynumber[$k]=$row['surveyno'];
      $k++;
     }
	}
?>
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="bootstrap-glyphicons.css">
<script type="text/javascript" src="bootstrap.min.js"></script>
<script>
$( document ).ready(function() {
var array1 = <?php echo json_encode($surveyname) ?>;
var array2 = <?php echo json_encode($surveynumber) ?>;
for(var i=0;i<array1.length;i++) {
	var div = document.createElement("li");
    //div.style.height = "400px";
    div.className="container";
    var link="graphmaking.php?"+"para="+encodeURI(array2[i]);
    var link1="barmaking.php?"+"para="+encodeURI(array2[i]);
    var link2="splinemaking.php?"+"para="+encodeURI(array2[i]);
    div.innerHTML="<h3><a href='"+link+"' target='_blank'>"+array1[i]+"</a></h3>";
    var list= document.createElement("ul");
    var sublist1= document.createElement("li");
    sublist1.innerHTML="<i><a href='"+link+"' target='_blank'>Analysis using pie chart</a></i>";
    var sublist2= document.createElement("li");
    sublist2.innerHTML="<i><a href='"+link1+"' target='_blank'>Analysis using bar chart</a></i>";
    var sublist3= document.createElement("li");
    sublist3.innerHTML="<i><a href='"+link2+"' target='_blank'>Analysis using spline</a></i>";
    list.appendChild(sublist1);
    list.appendChild(sublist2);
    list.appendChild(sublist3);
    div.appendChild(list);
    //console.log("here we go"+list);
    //document.body.appendChild(div);
    document.getElementById('add').appendChild(div);
}
});
</script>
</head>
<body>
<ul class="nav navbar-nav" style="float: right;margin-right: 10%;margin-top: -2%;">
<li class="dropdown">
    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Features Available<b class="caret"></b></a>
        <ul class="dropdown-menu">
            <li><a href="test.php" data-value='0' class="newsurvey" >Create new survey</a></li>
            <li><a href='logout.php' class='dropdown1a'>Logout</a></li>
        </ul>
</li>
</ul>
<h3>
<i>
The following list tells the name of survey for which report can be generated
</i>
</h3>
<ul id="add">
</ul>
</body>
</html>