/*!
 * jQuery hashchange event - v1.3 - 7/21/2010
 * http://benalman.com/projects/jquery-hashchange-plugin/
 * 
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function($, window, undefined) {
	'$:nomunge';

	var str_hashchange = 'hashchange';
	var doc = document;
	var fake_onhashchange;
	var special = $.event.special;
	var doc_mode = doc.documentMode;
	var supports_onhashchange = 'on' + str_hashchange in window && (doc_mode === undefined || doc_mode > 7);

	function get_fragment(url) {
		url = url || location.href;
		return '#' + url.replace(/^[^#]*#?(.*)$/, '$1');
	}

	$.fn[str_hashchange] = function(fn) {
		return fn ? this.bind(str_hashchange, fn) : this.trigger(str_hashchange);
	};

	$.fn[str_hashchange].delay = 50;

	special[str_hashchange] = $.extend(special[str_hashchange], {
		setup: function() {
			if (supports_onhashchange) {
				return false;
			}

			$(fake_onhashchange.start);
		},
		teardown: function() {
			if (supports_onhashchange) {
				return false;
			}

			$(fake_onhashchange.stop);
		}
	});

	fake_onhashchange = (function() {
		var fn_retval = function(val) {
			return val;
		};

		var self = {};
		var timeout_id;
		var last_hash = get_fragment();
		var history_set = fn_retval;
		var history_get = fn_retval;

		self.start = function() {
			timeout_id || poll();
		};

		self.stop = function() {
			timeout_id && clearTimeout(timeout_id);
			timeout_id = undefined;
		};

		function poll() {
			var hash = get_fragment();
			var history_hash = history_get(last_hash);

			if (hash !== last_hash) {
				history_set(last_hash = hash, history_hash);

				$(window).trigger(str_hashchange);
			} else if (history_hash !== last_hash) {
				location.href = location.href.replace(/#.*/, '') + history_hash;
			}

			timeout_id = setTimeout(poll, $.fn[str_hashchange].delay);
		}

		return self;
	})();
})(jQuery, this);