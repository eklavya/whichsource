function moreclick(val){
	var x = document.getElementById('top');
	val.style.visibility = 'hidden';

	x.style.height = '180px';
	
	var y = document.getElementsByClassName('findmethod');
	
	for(loopVar=0;loopVar<y.length;loopVar++){
		y[loopVar].style.visibility = 'visible';
	}
	
	var lButton = document.getElementById('lbutton');
	
	lButton.style.visibility = 'visible';
} 

function lessclick(val){
	var x = document.getElementById('top');
	val.style.visibility = 'hidden';

	x.style.height = '23px';
	
	var y = document.getElementsByClassName('findmethod');
	for(loopVar=0;loopVar<y.length;loopVar++){
		y[loopVar].style.visibility = 'hidden';
	}

	var mButton = document.getElementById('Mbutton');
	
	mButton.style.visibility = 'visible';
}