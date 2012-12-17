$(function(){
	$("a.confirmDelete").click(function(e) { e.preventDefault(); confirmDelete($(this), $(this).attr("rel")); });
	$("button.confirmDelete").click(function(e) { e.preventDefault(); confirmDelete($(this), $(this).attr("value")); });
	$("button.submitAction").click(function(e) { e.preventDefault(); submitActionHelper($(this), $(this).val());});
	$("a.submitAction").click(function(e) { e.preventDefault(); submitActionHelper($(this), $(this).attr("rel"));});
	$("button.linkbutton").click(function(e){
		e.preventDefault();
		var proceed = true;
		if ($(this).hasClass("confirmCancel")) {
			proceed = showQuestion("Are you sure you want to lose the changes?");
		}if ($(this).hasClass("confirmDelete")) {
			proceed = showQuestion("Are you sure you want to delete the selected object?");
		}
		if (proceed) {
			var url = $(this).attr("value");
			window.location = url;
		}
	});
	
	// default submit button
	$("input").keydown(function (e){
	    if(e.keyCode == 13){
	    	var def = findDefaultButton($(this));
	    	if (def != null) {
	    		e.preventDefault();
	    		def.trigger("click");
	    	}
	    }
	});
});
function findDefaultButton(startElement) {
	while (startElement != null && startElement.size() > 0) {
		var found = startElement.find(".defaultButton");
		if (found.size() > 0) {
			return found.eq(0);
		}
		startElement = startElement.parent();
	}
	return null;
}


function confirmDelete(source, action) {
	showQuestion("Are you sure you want to delete the selected object?", function(){submitActionHelper(source, action);});
}
function showQuestion(question, callback) {
	if (confirm(question)){
		if (callback) {
			callback();
		}
		return true;
	}
	return false;
}
function submitActionHelper(source, action) {
	var form = source.closest("form");
	var arr = action.split('|');
	for (var i = 0; i < arr.length / 2; i++) {
		form.append('<input type="hidden" name="{0}" value="{1}" />'.format(arr[2*i], arr[2*i+1]));
	}
	form.submit();
}

RegExp.escape = function (text) {
	return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
};
String.prototype.replaceAll = function (replace, with_this) {
	return this.replace(new RegExp(RegExp.escape(replace), 'g'), with_this);
};
String.prototype.format = function () {
	var res = this;
	for (var i = 0; i < arguments.length; i++) {
		res = res.replaceAll("{" + i + "}", arguments[i].toString());
	}
	return res;
};