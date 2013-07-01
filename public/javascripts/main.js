$(function() {
	// Add a delay for key up event
	$.fn.keyupDelay = function(callback, delay) {
		var timer = 0;

		return $(this).on('keyup', function() {
			clearTimeout(timer);
			timer = setTimeout(callback, delay);
		});
	};

	// Remove element from an array
	Array.prototype.remove = function(elem) {
		return $.grep(this, function(e) {
			return e !== elem;
		});
	};

	// Build an icon with its defined action
	buildActionIcon = function(icon, action) {
		var icon = $('<i>').addClass(icon);
		var link = $('<a>').append(icon);

		return link;
	};

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