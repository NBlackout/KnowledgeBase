$(function() {
	var Modes = {
		Read: 'read',
		Edit: 'write'
	};

	toggleMode = function(comment, mode) {
		var contentDiv = comment.find('.content-div');
		var contentTextarea = comment.find('.content-textarea');

		var actions = comment.find('.actions');
		var edit = actions.find('.edit');
		var save = actions.find('.save');
		var cancel = actions.find('.cancel');

		switch (mode) {
			case Modes.Read:
				contentTextarea.hide();
				contentDiv.show();

				edit.show();
				save.hide();
				cancel.hide();

				break;
			case Modes.Edit:
				contentTextarea.show();
				contentDiv.hide();

				edit.hide();
				save.show();
				cancel.show();
				break;
			default:
				break;
		}
	};

	saveComment = function(comment) {
		comment.find('.actions .save-comment input[name="content"]').val(comment.find('.content-textarea').val());
		comment.find('.actions .save-comment').submit();
	}

	deleteComment = function(comment) {
		if (confirm(messages['alert.comment.delete.confirm'])) {
			comment.find('.actions .delete-comment').submit();
		}
	}

	highlightComment = function() {
		var commentId = window.location.hash.replace('#', '');

		$('#' + commentId).animate({
			backgroundColor: '#669966'
		}, 500);
		$('#' + commentId).animate({
			backgroundColor: 'transparent'
		}, 1500);
	}

	$('.comment .actions a').click(function() {
		var action = $(this);
		var comment = action.closest('.comment');

		if (action.hasClass('edit')) {
			toggleMode(comment, Modes.Edit);
		} else if (action.hasClass('cancel')) {
			toggleMode(comment, Modes.Read);
		} else if (action.hasClass('save')) {
			saveComment(comment);
		} else if (action.hasClass('delete')) {
			deleteComment(comment);
		}

		return false;
	});

	$(window).hashchange(function() {
		highlightComment();
	});

	highlightComment();
});