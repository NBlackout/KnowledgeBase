$(function() {
	var NAV_FIX = 0;

	var Operations = {
		Enlarge: '+=',
		Shrink: '-='
	};

	var findTagsByNameLike = actions['TagController.findTagsByNameLike(:name)'];

	var tags = $('.tags');
	var selectedTags = $('.selectedTags');
	var inputTag = $('.input-tag');
	var suggestions = $('.suggestions');

	updateInputTag = function(operation) {
		inputTag.css('width', tags.width() - selectedTags.width()).css('width', operation + NAV_FIX);
		inputTag.val('');
		inputTag.focus();
	}

	buildTag = function(tag) {
		var removeTag = buildActionIcon('icon-remove');

		var selectedTag = $('<span>').addClass('selectedTag').attr('title', tag.description).attr('data-tag-id', tag.id);
		selectedTag.append(tag.name, [removeTag]);

		selectedTags.append(selectedTag);

		removeTag.click(function() {
			updateInputTag(Operations.Enlarge);
			selectedTag.remove();
		});

		updateInputTag(Operations.Shrink);
	}

	buildSuggestion = function(tag) {
		var name = $('<a>').attr('href', '#').text(tag.name);
		name.click(function() {
			buildTag(tag);

			suggestions.empty().hide();

			return false;
		});

		var suggestion = $('<span>').addClass('suggestion');
		suggestion.append(name);

		suggestions.append(suggestion);
	}

	inputTag.focus(function() {
		tags.addClass('focused');
	});

	inputTag.blur(function() {
		tags.removeClass('focused');
	});

	inputTag.keyup(function() {
		suggestions.empty().hide();
	});

	inputTag.keyupDelay(function() {
		var name = inputTag.val();
		if (name) {
			$.getJSON(findTagsByNameLike({
				name: name
			}), function(data) {
				if (data.length != 0) {
					$.each(data, function(index, tag) {
						buildSuggestion(tag);
					});

					suggestions.show();
				}
			});
		}
	}, 500);
});