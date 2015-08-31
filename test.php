<?php

//include_once 'signup.php';
session_start();
//include_once("simple_html_dom.php");
if (isset($_SESSION['uid']))
echo "<span id='identify'><i><strong style='font:17px;color:black;margin-left:81%;font-size:initial;'>Hello&nbsp".$_SESSION['uid']."</strong></i></span>";
else
include_once 'process.php';
//include_once 'process.php';
include_once("db.php"); 
include_once 'common.php';
dbConnect("survey");

?>
<!DOCTYPE html>
<html>
<head>
<title>Survey Creation</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="bootstrap.min.css">
<link rel="stylesheet" href="bootstrap-glyphicons.css">
<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap.min.js"></script>
<style>
#sub {
	/*color: #000;
 font-size: 25px;
 width: 103px;
 height: 34px;
 border: 1px solid gray;
 margin: 0;
 padding: 0;*/
}
.help{
	margin-left:27%;
}
.process{
	display: none;
}
body{
	background-image: url("light5.jpg");
	background-size: cover;
}
</style>
<script>
function removeall1(item) {
var main='main'+item.getAttribute('data-value');
var displaystate = document.getElementById(main).getElementsByClassName("textchoice")[0].style.display;
if(displaystate=="initial") {
	document.getElementById(main).getElementsByClassName("textchoice")[0].style.display="none";
	return;
}
}
function removeall2(item) {
var main='main'+item.getAttribute('data-value');
var inputs = document.getElementById(main).getElementsByTagName("input");
var i;
for(i = 0; i < inputs.length; i++) {
    if(inputs[i].type == "checkbox") {
        inputs[i].type = "radio"; 
    }
}
}
function removeall3(item) {
var main='main'+item.getAttribute('data-value');
var inputs = document.getElementById(main).getElementsByTagName("input");
var i;
for(i = 0; i < inputs.length; i++) {
    if(inputs[i].type == "radio") {
        inputs[i].type = "checkbox"; 
    }  
}
}
function demo1(item,k){
  if(k==0){
	var main='main'+item.getAttribute('data-value');
	document.getElementById(main).getElementsByClassName("otherchoice")[0].style.display="none";
	var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
	var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
	hiddenclasses1[item.getAttribute('data-value')].value="text";
    //console.log('value of demo1 is'+main);
    var removedclasses1=document.getElementById(main).getElementsByClassName("multichoice");
    var l1=removedclasses1.length;
    var i;
    if(l1==1)
    removedclasses1[0].style.display="none";
	if(l1>1) {
		//removedclasses1[(l1-1)/2].style.display="none";
	console.log("value of multichoice over here "+l1);
	removedclasses1[0].style.display="none";
	if(l1==2||l1==3){
    if(l1==2)
    removedclasses1[l1-1].style.display="none";
    else if(l1==3)	
    removedclasses1[l1-2].style.display="none";	
	for(i=0;i<l1-1;i++){
		console.log("checker"+i);
	  //  console.log(removedclasses1[i]);
	    var element=removedclasses1[i];
		removedclasses1[i].parentNode.removeChild(element);
	 }
    }
    else if(l1==4) {
    	//removedclasses1[l1-2].style.display="none";
    	//$( removedclasses1[l1-2] ).remove();
    	for(i=1;i<l1-1;i++){
		console.log("checker"+i);
	  //  console.log(removedclasses1[i]);
	    var element=removedclasses1[i];
		removedclasses1[i].parentNode.removeChild(element);
	 }

		//$("#"+main+" .multichoice").eq(i).remove();
    }
	
    }
    //console.log($("#"+main+" #helpyou .multichoice").eq(i-1).value);
	var subtree=document.getElementById(main).getElementsByClassName("textchoice")[0].style.display="initial";
	//console.log(subtree);
	document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value="0";
	document.getElementById(main).getElementsByClassName("otherchoice")[0].style.display="none";
	//subtree.getElementsByClassName("textchoice")[0].style.display="initial";
}
else{
	flag=1;
	var main='main'+item.getAttribute('data-value');
	var subtree=document.getElementById(main).getElementsByClassName("otherchoice")[0].style.display="initial";
	document.getElementById(main).getElementsByClassName("textchoice")[0].style.display="none";
	var hiddenclasses1=document.getElementsByClassName("hiddenvalue5");
	hiddenclasses1[item.getAttribute('data-value')].value="othertext";
}
}
function demo2(item) {
	console.log("1");
 removeall1(item);
 console.log("2");
 removeall2(item);
 console.log("3");
 var main='main'+item.getAttribute('data-value');
 console.log("4");
 var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
 //var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
 console.log("5");
 document.querySelectorAll("input[type=submit]")[0].style.marginLeft="27%";
 console.log("6");
 hiddenclasses1[item.getAttribute('data-value')].value="multichoice";	
 console.log("7");
 var democlass=document.getElementById(main).getElementsByClassName("multichoice");
 console.log("8");
 var length=democlass.length;
 console.log("9");
 democlass[length-1].style.display="initial";
 if(document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value==="0"){
 document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value=length; 
 console.log("debugging "+length);
 }
}
function add(item) {
//var democlass=document.getElementsByClassName("row");
//var length=democlass.length;
var main='main'+item.id;
//console.log(item);
//console.log('value of main id is '+main);
//console.log('help you'+document.getElementById(main).getElementsByClassName("multichoice").length);
var disprop1=document.getElementById(main).getElementsByClassName("multichoice")[0].style.display;
//var disprop2=document.getElementById(main).getElementsByClassName("checkchoice")[0].style.display;
//console.log(disprop1);
var n=parseInt(document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value)+1;
console.log("checking value of n "+n );
document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value=n;
if(disprop1==='initial')
    var original=document.getElementById(main).getElementsByClassName("multichoice")[0];
//else if(disprop2==='initial')
//	var original=document.getElementById(main).getElementsByClassName("checkchoice")[0];
var clone = original.cloneNode(true);
//console.log(addedclass);
//addedclass.style.display="initial";
original.parentNode.appendChild(clone);
var plus=document.getElementsByClassName('addquestion');
for(i=0;i<plus.length;i++) {
var thumbclass=document.getElementById(main).getElementsByClassName("glyphicon-picture");
var thumbinput=document.getElementById(main).getElementsByClassName("thumbsrc");
var imageclass=document.getElementById(main).getElementsByClassName("thumbimage");
for(j=0;j<thumbclass.length;j++){
		//democlass[j].setAttribute('count-value',i);
		thumbclass[j].setAttribute('thumb-value',j);
		thumbinput[j].setAttribute('thumbinput',j);
		thumbclass[j].setAttribute('thumb-valu',i);
		imageclass[j].setAttribute('thumb-images',j);
		}
	}
}
function removediv(item){
//(item).remove();
//console.log("printing details about parent node "+"main"+item.getAttribute('count-value'));
var checker="main"+item.getAttribute('count-value');
var democlass=document.getElementById(checker).getElementsByClassName("multichoice");
 var length=democlass.length;
 if(length>1){
(item.parentNode.parentNode).remove();
console.log("more than one");
document.getElementById(checker).getElementsByClassName("hiddenvalue3")[0].value-="1";
}
else if (length==1){
(item.parentNode.parentNode).style.display='none';
document.getElementById(checker).getElementsByClassName("hiddenvalue3")[0].value="0";
}
return;
}
function removeimage(item){
var checker="main"+item.getAttribute('count-value');
document.getElementById(checker).getElementsByClassName("hiddenvalue4")[0].value="";
var democlass=document.getElementById(checker).getElementsByClassName("multichoice");
$("#"+checker+" #helptry").css("display","none");
$("#"+checker+" .glyphicon-remove").first().css("display","none");
}
function demo3(item){
removeall1(item);
removeall3(item);	
var main='main'+item.getAttribute('data-value');
var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
hiddenclasses1[item.getAttribute('data-value')].value="checkbox";
 var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
 //var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
 document.querySelectorAll("input[type=submit]")[0].style.marginLeft="27%";
 hiddenclasses1[item.getAttribute('data-value')].value="checkbox";	

var democlass=document.getElementById(main).getElementsByClassName("multichoice");
 var length=democlass.length;
 democlass[length-1].style.display="initial";
 if(document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value==="0"){
var optionclass=document.getElementById(main).getElementsByClassName("hiddenvalue3")[0].value=length;
console.log("checking value of length "+length);
}
}
function demo4(item){
var main='main'+item.getAttribute('data-value');
//$(document).ready(function(){
		$("#"+main+" #myModal").modal('show');
//	});
//var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
	var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
	hiddenclasses2[item.getAttribute('data-value')].value="image";
}
function thumbnail(item){
var main='main'+item.getAttribute('thumb-valu');
modern=modern+1;
console.log("valur of main after pressing glyphicon-picture "+main);
document.getElementById(main).getElementsByClassName("thumb")[0].value=parseInt(item.getAttribute('thumb-value'));
//$(document).ready(function(){
		$("#"+main+" #myModal").modal('show');
//	});
//var hiddenclasses1=document.getElementsByClassName("hiddenvalue1");
	//var hiddenclasses2=document.getElementsByClassName("hiddenvalue2");
	//hiddenclasses2[item.getAttribute('data-value')].value="image";
}
function copy(item){
	var main='main'+item.getAttribute('click-value');
	var main1='main0';
	//console.log('helping valye of click-value ',item.getAttribute('click-value'));
	var original=document.getElementById(main);//.getElementsByClassName('addquestion')[0];
	var original1=document.getElementById(main1);
	var clone = original1.cloneNode(true);
	original.parentNode.appendChild(clone);
	var plus=document.getElementsByClassName('glyphicon-plus');
	var plus1=document.getElementsByClassName('glyphicon-file');
    //var plus5=document.getElementsByClassName('glyphicon-remove');
    var plus2=document.getElementsByClassName('tquestion1');
    var plus3=document.getElementsByClassName('tquestion2');
    var plus4=document.getElementsByClassName('tquestion3');
    var plus6=document.getElementsByClassName('fileinput');
    var plus7=document.getElementsByClassName('tquestion4');
    var plus8=document.getElementsByClassName('byurl');
    var plus9=document.getElementsByClassName('glyphicon-trash');
    var plus10=document.getElementsByClassName('tquestion5');
    var plus11=document.getElementsByClassName('nameofsurvey');
    var i;
    //$(".help")
    //.css("margin-left","27%");
    //console.log(plus.length);
    var lastrow=document.createElement("div");
    lastrow.className="col-xs-12";
    var input = document.createElement("input");
input.type = "submit";
input.name = "submit";
input.value="Submit";
input.className="help"; // set the CSS class
input.id="sub";
input.style.marginLeft="27%";
lastrow.appendChild(input);
    for(i=0;i<plus.length-1;i++) {
    	//var remove='main'+i;
    	$(".help").eq(0).remove();
    }
    $(".help")
    .css("margin-left","27%");
	document.getElementsByTagName("form")[0].appendChild(lastrow);
	for(i=1;i<plus11.length;i++) {
     plus11[i].style.display="none";
	}
	for(i=0;i<plus.length;i++) {
		plus[i].id=i;
		plus1[i].setAttribute('click-value',i);
		plus2[i].setAttribute('data-value',i);
		plus3[i].setAttribute('data-value',i);
		plus4[i].setAttribute('data-value',i);
		plus6[i].setAttribute('image-value',i);
		plus7[i].setAttribute('data-value',i);
		plus8[i].setAttribute('data-value',i);
		plus9[i].setAttribute('delete-value',i);
		plus10[i].setAttribute('data-value',i);
		//console.log(plus6[i]);
		//console.log(plus6[i].getAttribute('image-value'));
	}

	var plus=document.getElementsByClassName('addquestion');
    var i;
    //console.log(plus.length);
	for(i=0;i<plus.length;i++) {
		plus[i].id='main'+i;
	}
		for(i=0;i<plus.length;i++) {
		var ch="main"+i;
		console.log("value of ch is "+ch);
		var democlass=document.getElementById(ch).getElementsByClassName("glyphicon-remove");
		var thumbclass=document.getElementById(ch).getElementsByClassName("glyphicon-picture");
		var thumbinput=document.getElementById(main).getElementsByClassName("thumbsrc");
		var thumbimages=document.getElementById(ch).getElementsByClassName("thumbimage");
		document.getElementById(ch).getElementsByClassName("thumb")[0].setAttribute('inputhumb',i);
		//var plus5=document.getElementsByClassName('glyphicon-remove');
		console.log("no of thumbclasses are"+thumbclass.length);
		democlass[0].setAttribute('count-value',i);
		for(j=0;j<democlass.length;j++){
			//if(j!=0)
		democlass[j].setAttribute('count-value',i);
	  }
	  for(j=0;j<thumbclass.length;j++){
	  	thumbclass[j].setAttribute('thumb-valu',i);
	  	thumbclass[j].setAttribute('thumb-value',j);
	  	thumbinput[j].setAttribute('thumbinput',j);
	  	thumbimages[j].setAttribute('thumb-images',j);
	  }
	}
}
function insertimage(item){
 var main='main'+item.getAttribute('data-value');
 //console.log("value of main in insertimage "+main);
 var democlass=document.getElementById(main);
 //var democlass=democlass.getElementById("myModal");//.value;
 var pri=democlass.getElementsByClassName("urlvalue")[0].value;//+item.getAttribute('data-value'))[0].value;
 //console.log(pri);	
 $.ajax({
    url:pri,
    type:'HEAD',
    error:
        function(){
            //do something depressing
            democlass.getElementsByClassName("error")[0].style.display="initial";
        },
    success:
        function(){
            //do something cheerful :)
            democlass.getElementsByClassName("error")[0].style.display="none";
            democlass.getElementsByClassName("success")[0].style.display="initial";

        }
});
}
function refresh(){
	location.reload();     
}
var prev='-1';
var modern='-1';
function readURL(input,datavalue) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            var main='main'+input.getAttribute('image-value');
            reader.onload = function (e) {
                $('#'+main+' #blah')
                    .attr('src', e.target.result)
                    .width(409)
                    .height(200)
                    .css("margin-top","2%")
                    .css("margin-right","26%")
                    .css("display",'initial');
                console.log("source value of image "+e.target.result);
              document.getElementById(main).getElementsByClassName("hiddenvalue4")[0].value=e.target.result;
              //document.getElementById(main).getElementsByClassName("hiddenvalue6")[0].value=path.split("\\")[path.split("\\").length-1];
              if(modern===prev){
                $('#'+main+' #helptry')
                    .attr('src', e.target.result)
                    .css("display",'initial')
                    .css("width","67%")
                    .css("padding-bottom","4%")
                    .css("margin-top","2%");
                $('#'+main+' .glyphicon-remove').first()
                      .css("display","initial");
               }
               else{
               	modern=prev;
               	var value1=$('#'+main+' .thumb:nth-of-type(1)').val();
               	console.log("special value is "+value1);
               	$('#'+main).find("[thumb-images='"+value1+"']")
                    .attr('src', e.target.result)
                    .css("display",'initial')
               	    .css("width","91px");
               	    console.log("new source of image"+e.target.result);
               	$('#'+main).find("[thumbinput='"+value1+"']")
                    .val(e.target.result);
                    //document.getElementById(main).querySelectorAll("[thumbinput='"+value1+"']").value=e.target.result;
               }       
            };

            reader.readAsDataURL(input.files[0]);
        }
    }

</script>
<script type="text/javascript">
	function helpingbusiness() {
    	//console.log("Welcome to the new age");
        document.getElementById("helping").value="helpers";
    }
    function removequestion(item){
    	//console.log("printing details about parent node "+"main"+item.getAttribute('count-value'));
		//console.log("you all are suckers");
		var democlass=document.getElementsByClassName("glyphicon-trash");
		if(democlass.length>1){
			var checker="main"+item.getAttribute('delete-value');
			//console.log("value of checker is"+checker);
			var el=document.getElementById(checker);//.getElementsByClassName("multichoice");
			el.parentNode.removeChild( el );
 			var length=el.length;
 		}
	}
</script>
</head>
<body style="overflow-x:hidden;">
<div id="reload">
<ul class="nav navbar-nav" style="float: right;margin-right: 10%;margin-top: -2%;">
<li class="dropdown">
    <a href="#" data-toggle="dropdown" class="dropdown-toggle">Features Available<b class="caret"></b></a>
        <ul class="dropdown-menu">
            <li><a href="" data-value='0' class="newsurvey" onclick="refresh()">Create new survey</a></li>
            <li><a href="listsurvey.php?para=<?php echo $_SESSION['email'] ?>" class="newsurvey" >Reports of Survey</a></li>
            <li><a href='logout.php' class='dropdown1a'>Logout</a></li>
        </ul>
</li>
</ul>
<form enctype="multipart/form-data" onsubmit="helpingbusiness()" method="post" autocomplete="off" action="process.php?para=<?php echo $_SESSION['email'] ?>">
<div class="addquestion" id="main0" style="margin-top:17px;">
<div style="margin-left:28%;">
 <div class="nameofsurvey">
  <div class="row">
   <div class="col-xs-12">
    <label for="Name of survey">Name of survey</label>
   </div>
  </div>
  <div class="row">
   <input type="text" class="form-control" style="width:57%;" placeholder="Enter name of survey" name="nos[]">
   <br/>
   <br/>
  </div>
 </div>
 <div class="row" >
  <div class="col-xs-12">
   <label for="Question title">Question title</label></div>
  </div>
 <div class="row">
  <input type="text" class="form-control" style="width:57%;" placeholder="Untitled Question" name="qtitle[]">
   <div class="col-xs-4">
 
    <img id="helptry" class="col-xs-2" src="#" style="display:none;">
     <div style="display:none;margin-left:-6%;margin-top:2%;" class="col-xs-2 glyphicon glyphicon-remove imageclose" count-value='0' onclick="removeimage(this)">
     </div>
   </div>
     <input type="text" style="display:none;"class="form-control" id="helping" placeholder="Untitled Question" name="helptitle">
 </div>

<input style="display:none;" type="text" class="hiddenvalue1" name="hidden1[]">
<input style="display:none;" type="text" class="hiddenvalue2" name="hidden2[]" value="noimage">
<input style="display:none;" type="text" class="hiddenvalue3" name="hidden3[]" value="0">
<input style="display:none;" type="text" class="hiddenvalue4" name="hidden4[]" value="">
<input style="display:none;" type="text" class="hiddenvalue5" name="hidden5[]" value="">
<input style="display:none;" type="text" class="hiddenvalue6" name="hidden6[]" value="">
<br/>
</div>
<div style="margin-left:28%;">
<div clas="row" >
<div class="col-xs-12">
<label for="Help text">Help Text</label></div></div>
<div class="row" style="margin-top:-1%;">
<input type="text" style="width:57%;" class="form-control" name="htitle[]" value="">
</div>
<br/>
</div>
<div style="margin-left:27%;">
<div class="col-xs-2"><label for="Question Type">Question Type</label></div> <div class="col-xs-4">
<ul class="nav navbar-nav" style="margin-top:-4%;">
<li class="dropdown">
                        <a href="#" data-toggle="dropdown" class="dropdown-toggle">Type of Questions<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#" data-value='0' class="tquestion1" onclick="demo1(this,0)">Text</a></li>
                            <li><a href="#" data-value='0' class="tquestion2" onclick="demo2(this)">Multiple choice</a></li>
                            <li><a href="#" data-value='0' class="tquestion3" onclick="demo3(this)">Checkbox</a></li>
                            <li><a href="#" data-value='0' class="tquestion4" onclick="demo4(this)">Images</a></li>
                            <li><a href="#" data-value='0' class="tquestion5" onclick="demo1(this,5)">Other choice</a></li>
                        </ul>
                    </li>
</ul>
</div>
<span class="glyphicon glyphicon-plus" id="0" onclick="add(this)"><img src="plus.png" style="margin-left:-21%;width:23%;margin-top:-5%;"></span>
<span class="glyphicon glyphicon-file" click-value="0" onclick="copy(this)" style="margin-left:-6%;"><img src="copy.png" style="margin-left: -10%;
width: 17%;
height: 3%;
margin-top: -2%;"></span>
<span class="glyphicon glyphicon-trash" delete-value="0" onclick="removequestion(this)" style="margin-left:-12%;"><img src="bin.png" style="margin-left: -25%;
width: 26%;
margin-top: -11%;
height: 3%;"></span>
</div>
</br></br>
<input type="text" class="thumb" inputhumb="0" style="display:none;">
<div id="helpyou" style="margin-left:28%;">
   <div class="multichoice col-xs-12 row" style="display:none;">
      <div class="col-xs-1" style="width:5.1%;"><input type="radio" name="checkradio" value="radiocheck" >
      </div>
      		
          <div class="col-xs-3">
              
              <input type="text" class="form-control" name="optionvalue[]" placeholder="Option 1">
          </div>
          <div class="col-xs-2" style="margin-top:.5%;"><img src="#" thumb-images='0' class="thumbimage" style="display:none;margin-left:-12%;margin-top:-14%;">
          <input type="text" class="thumbsrc" value="" style="display:none;"  thumbinput='0' name="thumbsource[]">
          <span class="glyphicon glyphicon-picture" thumb-valu="0" thumb-value='0' onclick="thumbnail(this)" style="margin-left:2%;">
               <img src="pictures.png" style="margin-left: -23%;
width: 13%;
margin-top: -4%;">                 </span>
          <span class="glyphicon glyphicon-remove" count-value='0' onclick="removediv(this)" style="margin-left:19%;margin-top:-12%;">
                    <img src="delete.png" style="margin-left: -26%;
width: 13%;
margin-top: -6%;">            </span>
          </div>
          <div class="col-xs-6"></div>
          <br/><br/>
    </div>

 </div>
<div class="col-xs-4 textchoice" style="display:none;margin-left:26%;">
 <textarea name="textvalue[]" class="form-control" rows="5" id="comment" placeholder="Enter your opinion">
 </textarea>
</div>
<div class="col-xs-4 otherchoice" style="display:none;margin-left:26%;">
 <textarea name="othervalue[]" class="form-control" rows="5" id="other" placeholder="Enter your other opinion">
 </textarea>
</div>

<div id="myModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Insert image</h4>
            </div>
            <div class="modal-body">
                <p>Enter the URL from where you want to access image?</p>
                <p class="text-warning"><small>If you don't save, your changes will be lost.</small></p>
                <label for="Question title">Image URL</label>
                <input type="text" class="form-control urlvalue" placeholder="URL of image">
                <p style="display:none;" class="error">The URL entered by you does not work</p>
                <p style="display:none;" class="success">The URL entered by you is valid.Press Save changes to continue</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary byurl" data-value="0" onclick="insertimage(this)">Save changes</button>
                <input class="fileinput" type='file' image-value="0" onchange="readURL(this,this.getAttribute('image-value'));" />
                <img id="blah" src="#" alt="your image" style="display:none;" />
            </div>
        </div>
    </div>
</div>
</div>
<div class="col-xs-12">
<input type="submit" value="Submit"  class="help" name="submit" id="sub" style="margin-left:27%;" />
</div>
</form>
</div>
</body>
</html>

