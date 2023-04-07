

$(document).ready(function() {
    $('.search-input-category').keyup(function() {
        var search = $(this).val().toLowerCase();
        $('.radio-button-wrapper-category .radio-button').each(function() {
            var label = $(this).find('label');
            var catname = label.attr('data-catname').toLowerCase();
            if (catname.indexOf(search) < 0) {
                $(this).hide();
            } else {
                $(this).show();
            }
        });
    });
    
    $('.search-input-author').keyup(function() {
        var search = $(this).val().toLowerCase();
        $('.radio-button-wrapper-author .radio-button').each(function() {
            var label = $(this).find('label');
            var authorname = label.attr('data-author').toLowerCase();
            if (authorname.indexOf(search) < 0) {
                $(this).hide();
            } else {
                $(this).show();
            }
        });
    });
});
