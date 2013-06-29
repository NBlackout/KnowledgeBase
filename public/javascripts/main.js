$(function() {
	// Handler for TAB and SHIFT + TAB keys
	$("textarea").keydown(function(event) {
		var tabChars = "    ";
		var tabKeyCode = 9;

		// Assert pressing tab key
		if (event.keyCode == tabKeyCode) {
			var value = $(this).val();
			var top = this.scrollTop;
			var start = this.selectionStart;
			var end = this.selectionEnd;

			if (event.shiftKey) {
				// Assert removing tab characters
				if (value.substring(start, start - tabChars.length) == tabChars) {
					$(this).val(value.substring(0, start - tabChars.length) + value.substring(end));

					this.selectionStart = start - tabChars.length;
					this.selectionEnd = start - tabChars.length;
				}
			} else {
				$(this).val(value.substring(0, start) + tabChars + value.substring(end));

				this.selectionStart = start + tabChars.length;
				this.selectionEnd = start + tabChars.length;
			}

			// Reverse scroll bar position
			this.scrollTop = top;

			return false;
		}
	});
});