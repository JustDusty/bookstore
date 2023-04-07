$(document).ready(function() {
	// Variables for pagination
	var books = $('.book-item');
	var itemsPerPage = 9;
	var currentPage = 1;
	var totalPages = Math.ceil(books.length / itemsPerPage);




	// Function to display books for current page
	function displayBooks() {
		var startIndex = (currentPage - 1) * itemsPerPage;
		var endIndex = startIndex + itemsPerPage;
		books.hide().slice(startIndex, endIndex).show();
	}

	// Function to update pagination buttons
	// Function to update pagination buttons
	function updatePagination() {
		// Clear previous buttons
		$('.pagination').empty();

		// Add previous button
		if (currentPage > 1) {
			$('.pagination').append('<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage - 1) + '">Previous</a></li>');
		} else {
			$('.pagination').append('<li class="page-item disabled"><span class="page-link">Previous</span></li>');
		}

		// Add page buttons
		var startPage, endPage;
		var delta = 2;
		if (totalPages <= 6) {
			startPage = 1;
			endPage = totalPages;
		} else {
			if (currentPage <= delta) {
				startPage = 1;
				endPage = 2 * delta + 1;
			} else if (currentPage >= totalPages - delta) {
				startPage = totalPages - 2 * delta;
				endPage = totalPages;
			} else {
				startPage = currentPage - delta;
				endPage = currentPage + delta;
			}
		}

		if (startPage > 1) {
			$('.pagination').append('<li class="page-item"><a class="page-link" href="#" data-page="1">1</a></li>');
			if (startPage > 2) {
				$('.pagination').append('<li class="page-item disabled"><span class="page-link">...</span></li>');
			}
		}

		for (var i = startPage; i <= endPage; i++) {
			if (i == currentPage) {
				$('.pagination').append('<li class="page-item active"><span class="page-link">' + i + '</span></li>');
			} else {
				$('.pagination').append('<li class="page-item"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>');
			}
		}

		if (endPage < totalPages) {
			if (endPage < totalPages - 1) {
				$('.pagination').append('<li class="page-item disabled"><span class="page-link">...</span></li>');
			}
			$('.pagination').append('<li class="page-item"><a class="page-link" href="#" data-page="' + totalPages + '">' + totalPages + '</a></li>');
		}

		// Add next button
		if (currentPage < totalPages) {
			$('.pagination').append('<li class="page-item"><a class="page-link" href="#" data-page="' + (currentPage + 1) + '">Next</a></li>');
		} else {
			$('.pagination').append('<li class="page-item disabled"><span class="page-link">Next</span></li>');
		}
	}



	// Display books for initial page
	displayBooks();
	// Update pagination buttons for initial page
	updatePagination();

	// Event listener for page buttons
	$(document).on('click', '.pagination .page-link', function(e) {
		e.preventDefault();
		currentPage = parseInt($(this).attr('data-page'));
		displayBooks();
		updatePagination();
	});

	// Event listener for "Showing" dropdown
	$(document).on('click', '.show-page', function(e) {
		e.preventDefault();
		itemsPerPage = parseInt($(this).attr('data-page'));
		totalPages = Math.ceil(books.length / itemsPerPage);
		currentPage = 1;
		displayBooks();
		updatePagination();
	});




});

