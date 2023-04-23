	

	$(document).on('click', '.btn-add-to-cart', function() {
		toastr.options = {
		      "closeButton": false,
		      "debug": false,
		      "newestOnTop": false,
		      "progressBar": false,
		      "positionClass": "toast-top-right",
		      "preventDuplicates": false,
		      "onclick": null,
		      "showDuration": "300",
		      "hideDuration": "1000",
		      "timeOut": "5000",
		      "extendedTimeOut": "1000",
		      "showEasing": "swing",
		      "hideEasing": "linear",
		      "showMethod": "fadeIn",
		      "hideMethod": "fadeOut"
		    }
	    var $addToCartButton = $(this);
	    var isAuthenticated = $addToCartButton.attr('data-authenticated');
	    console.log(isAuthenticated);
	    if (isAuthenticated === 'false') {
	        toastr.warning('You must be logged in access or add items to the cart.');
	        return false;
	    }
	});