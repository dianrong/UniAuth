(function() {
	var msg = 'hello, this is a test';
	if (!!console && !!console.log) {
		console.log(msg);
	} else {
		alert(msg);
	}
})();