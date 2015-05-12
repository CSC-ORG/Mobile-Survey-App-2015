<html>
<head>
<style>
body{
    background-image: url("light5.jpg");
    background-size: cover;
}
.container {
	height: 400px; 
	min-width: 310px; 
	max-width: 800px;
	margin: 0 auto;
}
</style>
<?php
include_once 'db.php';
include_once 'common.php';
dbConnect("survey");
$var1 = $_GET['para'];
$sql="SELECT DISTINCT clienttable.emailid, clienttable.gender
FROM clienttable
INNER JOIN responsetable
ON clienttable.emailid=responsetable.emailid
WHERE responsetable.surveyno=$var1";
$aresult = mysql_query($sql);
	if (!$aresult) {
			error('A database no 1 error occurred while checking your login details.');
		}
$sql="SELECT DISTINCT emailid
FROM responsetable WHERE surveyno=$var1";
	$result = mysql_query($sql);
	if (!$result) {
			error('A database no 1 error occurred while checking your login details.');
		}
	$gender=array();
	$x=0;
	while($row = mysql_fetch_array($aresult))
	{
		
            $gender[$x++]= $row['gender'];
			//break;
		//
	}
	//print_r($gender);
	for($i=0;$i<mysql_num_rows($result);$i++){

	}
    $optionmulti=array();
    $optionsingle=array();
    $checker=0;
	while($row = mysql_fetch_array($result)){   //Creates a loop to loop through results
    $mainkey=$row['emailid'];
    //$gtype=$gender[$mainkey];
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
     //echo $row1['type']." ".$row1['option']."<br>";
	}
	$total=$count;
	//echo "<br>";
}
//print_r($optionsingle);
//print_r($optionmulti);
?>
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/highcharts-3d.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
<script>
$( document ).ready(function() {
var array1 = <?php echo json_encode($optionmulti) ?>;
var array2 = <?php echo json_encode($optionsingle) ?>;
var array3 = <?php echo json_encode($gender) ?>;
//console.log("printing details abt  "+array3[0]);
for(var i=0;i<array2.length;i++) {
    //div[i]=document.createElement("div");
    
    var div = document.createElement("div");
    div.style.height = "400px";
    div.className="container";
    document.body.appendChild(div);
}
var classteller=0;
for(var i=0;i<array2.length;i++)
 {
    store=[];
    frequency=[];
    male={};
    female={};
    var counter=0;
  for(var j=0;j<array2[i].length;j++)
   {
	if(array2[i][j]!='')
	{
        counter=1;
        classteller=i;
        console.log("entered "+i);
	 //alert(array2[i][j]+" hell yeah");
	 //store[p1]=array2[i][j];
	 place=-1;
	 for(var k=0;k<store.length;k++){
	 	if(store[k]==array2[i][j]) {
	 		place=k;
	 		if(array3[j]=='M'){
            	if(!(store[k] in male))
	 			male[store[k]]=1;
	 		    else
	 			male[store[k]]+=1;
	 			console.log("second step "+male[store[k]]);
            }
	 		else{
	 			console.log("come here");
	 			if(!(store[k] in female)){
	 				console.log("but not come here");
	 			female[store[k]]=1;
	 		}
	 		    else
	 			female[store[k]]+=1;
	 		}
	 	}
	 }
	 if(place==-1){
	 	store[store.length]=array2[i][j];
	 	
	 	place=store.length-1;
	 	//store.length+=1;
	 	frequency[place]=1;
	 	if(array3[j]=='M'){
	 			male[array2[i][j]]=1;
	 			console.log("third step "+male[array2[i][j]]+" "+array2[i][j]);
         }
	 		else
	 			female[array2[i][j]]=1;
	 }
	 else{
          frequency[place]+=1;
	 }
    }
   }
   if(counter!=1){
    
     for(var j=0;j<array1[i].length;j++) 
     {
        
        console.log("gender type "+array3[j]);
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
            if(array3[j]=='M'){
            	if(!(store[k] in male))
	 			male[store[k]]=1;
	 		    else
	 			male[store[k]]+=1;
	 			//console.log("second step "+male[store[k]]);
            }
	 		else{
	 			//console.log("come here");
	 			if(!(store[k] in female)){
	 			//	console.log("but not come here");
	 			female[store[k]]=1;
	 		}
	 		    else
	 			female[store[k]]+=1;
	 		}
          }
         }
         if(place==-1)
         {
         store[store.length]=temp[t];
         place=store.length-1;
         //store.length+=1;
         frequency[place]=1;
         if(array3[j]=='M'){
	 			male[temp[t]]=1;
	 			//console.log("third step "+male[temp[t]]+" "+temp[t]);
         }
	 		else
	 			female[temp[t]]=1;
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
 console.log("value of 3rd store is "+store[2]);
 if(counter!=0){
    console.log("value of classteller "+classteller);
    var paste=[];
    var marray=[];
    var farray=[];
    for(var w=0;w<store.length;w++) {
        paste[w]=store[w];
        if(store[w] in male)
        marray[w]=male[store[w]];
    else
    	marray[w]=0;
    if(store[w] in female)
        farray[w]=female[store[w]];
    else
    	farray[w]=0;
    }
    console.log("rechecking "+classteller+"sdfds "+counter);
checkout(classteller);
function checkout(classteller){
    var $container = $('.container');
    chart = new Highcharts.Chart({
      colors: ["#7cb5ec", "#f7a35c", "#90ee7e", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
      "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
     chart: {
     	    renderTo:$container[classteller],
            type: 'column',
            backgroundColor:null,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                viewDistance: 25,
                depth: 40
            },
            marginTop: 80,
            marginRight: 40
        },
        legend: {
      itemStyle: {
         fontWeight: 'bold',
         fontSize: '13px'
      }
   },
title: {
          style: {
         fontSize: '16px',
         fontWeight: 'bold',
         textTransform: 'uppercase'
      },
            text: 'Variability of option selection in question '+(classteller+1) +',grouped by gender'
        },

        xAxis: {
          gridLineWidth: 1,
      labels: {
         style: {
            fontSize: '12px'
         }
      },
            categories: paste
        },

        yAxis: {
          title: {
         style: {
            textTransform: 'uppercase'
         }
      },
      labels: {
         style: {
            fontSize: '12px'
         }
      },
            allowDecimals: false,
            min: 0,
            title: {
                text: 'Number of people'
            }
        },

        tooltip: {
            headerFormat: '<b>{point.key}</b><br>',
            pointFormat: '<span style="color:{series.color}">\u25CF</span> {series.name}: {point.y} / {point.stackTotal}'
        },

        plotOptions: {
            column: {
                stacking: 'normal',
                depth: 40
            }
        },
background2: '#F0F0EA',
        series: [{
            name: 'Male',
            data: marray,
            stack: 'male'
        }, {
            name: 'Female',
            data: farray,
            stack: 'female'
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