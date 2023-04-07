const quantityInput = document.querySelector('.quantity input');
const minusButton = document.querySelector('.quantity .btn-minus');
const plusButton = document.querySelector('.quantity .btn-plus');
const maxQuantity = parseInt(plusButton.getAttribute('data-bookquantity'));

let currentQuantity = parseInt(quantityInput.value);

function updateQuantity(value) {
	currentQuantity = Math.min(Math.max(value, 0), maxQuantity);
	quantityInput.value = currentQuantity;
	minusButton.disabled = currentQuantity === 0;
	plusButton.disabled = currentQuantity === maxQuantity;
}

minusButton.addEventListener('click', (event) => {
	event.preventDefault();
	updateQuantity(currentQuantity - 1);
});

plusButton.addEventListener('click', (event) => {
	event.preventDefault();
	updateQuantity(currentQuantity + 1);
});
