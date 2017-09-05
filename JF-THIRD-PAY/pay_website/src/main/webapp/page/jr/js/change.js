var strs = location.search,
	str = "";
strs = strs.slice(1);
strs = strs.split(",");
for(var i=0 ; i<strs.length;i++){
	str+='<div class="imgcontainer fl"><img src="'+strs[i]+'"/></div>';
}
$(".examplecontainer").html(str);

var imgs = $(".examplecontainer").children(),
	length = imgs.length,
	imgarr = strs,
	directions = "",
	index = 0;
for(var i = 0; i < length; i++) {
	imgs[i].index = i
	imgs[i].onclick = function() {
		$(".cover").css({
			"display": "block"
		});
		$(".coverimg img").attr({
			"src": imgarr[this.index]
		})
		tou();
	}
}

$(".coverimg").on("click", function() {
	$(".cover").css({
		"display": "none"
	})
})
$(".closed").on("click", function() {
	$(".cover").css({
		"display": "none"
	})
})

function GetSlideAngle(dx, dy) {
	return Math.atan2(dy, dx) * 180 / Math.PI;
}

//根据起点和终点返回方向 1：向上，2：向下，3：向左，4：向右,0：未滑动
function GetSlideDirection(startX, startY, endX, endY) {
	var dy = startY - endY;
	var dx = endX - startX;
	var result = 0;

	//如果滑动距离太短
	if(Math.abs(dx) < 2 && Math.abs(dy) < 2) {
		return result;
	}

	var angle = GetSlideAngle(dx, dy);
	if(angle >= -45 && angle < 45) {
		result = 4;
	} else if(angle >= 45 && angle < 135) {
		result = 1;
	} else if(angle >= -135 && angle < -45) {
		result = 2;
	} else if((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
		result = 3;
	}

	return result;
}
//滑动处理
function tou() {
	var startX, startY;
	document.addEventListener('touchstart', function(ev) {
		startX = ev.touches[0].pageX;
		startY = ev.touches[0].pageY;
	}, false);
	document.addEventListener('touchend', function(ev) {
		var endX, endY;
		endX = ev.changedTouches[0].pageX;
		endY = ev.changedTouches[0].pageY;
		var direction = GetSlideDirection(startX, startY, endX, endY);
		var srcs = $(".coverimg img").attr("src");
		for(var j = 0; j < imgarr.length; j++) {
			if(imgarr[j] === srcs) {
				index = j;
			}
		}
		switch(direction) {
			case 3:
				directions = "left";
				if(index === imgarr.length - 1) {
					index = -1;
				}
				$(".coverimg img").attr({
					"src": imgarr[index + 1]
				})
				break;
			case 4:
				directions = "right";
				if(index === 0) {
					index = imgarr.length;
				}
				$(".coverimg img").attr({
					"src": imgarr[index - 1]
				})

				break;
			default:
		}
	}, false);
}