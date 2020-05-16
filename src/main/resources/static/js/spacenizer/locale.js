let I18N = {};
$(document).ready (function() {
    $.get('/api/v1/spacenizer/localization', function(data) {
        if (data) {
            I18N.messages = data;
        }
    });
});