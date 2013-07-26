$(function() {
	$('.save-knowledge').submit(function() {
		var tags = $('.selectedTag');
		if (tags.length != 0) {
			var tagIds = new Array();

			tags.each(function(index, tag) {
				tagIds.push($(tag).attr('data-tag-id'));
			});

			$('.save-knowledge input[name="tagIds"]').val(tagIds);
		} else {
			$('.save-knowledge input[name="tagIds"]').attr('disabled', 'disabled');
		}
	});
});