<html>
<head>
<style>
body{
    background-image: url("light5.jpg");
    background-size: cover;
}
</style>
<?php
include_once 'db.php';
include_once 'common.php';
dbConnect("survey");
$var1 = $_GET['para'];
$sql="SELECT DISTINCT emailid
FROM responsetable WHERE surveyno=$var1";
	$result = mysql_query($sql);
	if (!$result) {
			error('A database no 1 error occurred while checking your login details.');
		}
    $optionmulti=array();
    $optionsingle=array();
    $checker=0;
	while($row = mysql_fetch_array($result)){   //Creates a loop to loop through results
    $mainkey=$row['emailid'];
    $sql="SELECT * FROM responsetable WHERE emailid='$mainkey' AND surveyno=$var1 ORDER BY qno";
	$result1 = mysql_query($sql);
	if($checker==0){
	for($i=0;$i<mysql_num_rows($result1);$i++) {
		$optionmulti[$i] = array();
		$optionsingle[$i] =array();
	}
	$checker+=1;
}
$count=0;
	while($row1 = mysql_fetch_array($result1)){
		$type=$row1['type'];
		if($type=='text') {
          $optionmulti[$count][]="";
          $optionsingle[$count][]="";
          $count++;
		}
		else if($type=='multichoice') {
			$optionmulti[$count][]="";
          $optionsingle[$count][]=$row1['option'];
          $count++;

		}
		else if($type=='checkbox') {
          $optionmulti[$count][]=$row1['option'];
          $optionsingle[$count][]="";
          $count++;
		}
	}
	$total=$count;
	//echo "<br>";
}
?>
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/highcharts-3d.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
<script>
$( document ).ready(function() {
var array1 = <?php echo json_encode($optionmulti) ?>;
var array2 = <?php echo json_encode($optionsingle) ?>;
for(var i=0;i<array2.length;i++) {
    var div = document.createElement("div");
    div.style.height = "400px";
    div.className="container";
    document.body.appendChild(div);
}

var classteller=0;
//var div = document.createElement("div");
for(var i=0;i<array2.length;i++)
 {
    store=[];
    frequency=[];
    var counter=0;
  for(var j=0;j<array2[i].length;j++)
   {
	if(array2[i][j]!='')
	{
        counter=1;
        classteller=i;
   	 place=-1;
	 for(var k=0;k<store.length;k++){
	 	if(store[k]==array2[i][j]) {
	 		place=k;
	 	}
	 }
	 if(place==-1){
	 	store[store.length]=array2[i][j];
	 	place=store.length-1;
	 	frequency[place]=1;
	 }
	 else{
          frequency[place]+=1;
	 }
    }
   }
   if(counter!=1){
    
     for(var j=0;j<array1[i].length;j++) 
     {
        
        classteller=i;
        if(array1[i][j]!='')
       {
        counter=1;
        var temp=array1[i][j].split("#");
        for(var t=0;t<temp.length;t++)
        {
         place=-1;
         for(var k=0;k<store.length;k++)
         {
          if(store[k]==temp[t]) 
          {
            place=k;
          }
         }
         if(place==-1)
         {
         store[store.length]=temp[t];
         place=store.length-1;
         //store.length+=1;
         frequency[place]=1;
         }
         else
         {
          frequency[place]+=1;
         }
        }
       }
     }
 }
 if(counter!=1){
    console.log("we");
    var change=document.getElementsByClassName('container')[i];
    console.log("re");
    change.innerHTML="Analysis of question "+(i+1)+"<br><br>This was a text type question and you have responded well.";
    change.style.height="100px";
    change.style.fontSize="18px";
    change.style.color="#333333";
    change.style.fontFamily="'Lucida Grande', 'Lucida Sans Unicode', Arial, Helvetica, sans-serif";
 }
     for(var q=0;q<store.length;q++){
            console.log("multichoice ",store[q]);
        }
        for(var q=0;q<frequency.length;q++){
            console.log("multichoice frequency ",frequency[q]);
        }
    var sum=0;
 var percent=[];
 for(var t=0;t<store.length;t++) {
    console.log(store[t]);
    sum+=frequency[t];
 }
 for(var t=0;t<frequency.length;t++) {
    console.log(frequency[t]);
    percent[t]=parseFloat((frequency[t]/sum)*100);
 }
 for(var t=0;t<frequency.length;t++){
    console.log(percent[t]);
 }
 console.log("value of 3rd store is "+store[2]);
 if(counter!=0){
    console.log("value of classteller "+classteller);
    var paste=[];
    for(var w=0;w<store.length;w++) {
        if(w==(store.length/2)){
            paste[w]={
                    name: store[w],
                    y: percent[w],
                    sliced: true,
                    selected: true
                };
        }
        else{
            paste[w]=[store[w],   percent[w]];
        }
    }
    console.log("rechecking "+classteller+"sdfds "+counter);
checkout(classteller);
function checkout(classteller){
    var $container = $('.container');
    chart = new Highcharts.Chart({
        chart: {
            renderTo:$container[classteller],
            type: 'pie',
            backgroundColor:'rgba(255, 255, 255, 0.1)',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: 'Analysis of question '+(classteller+1)
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '{point.name}'
                }
            }
        },
        series: [{
            type: 'pie',
            name: 'Shared percentage',
            data: paste
        }]
    });
}
}
}
});
</script>
</head>
<body>
</body>
</html>
