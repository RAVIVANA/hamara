/*  let date = document.getElementById("date");
 var currentDate = new Date();
 var options = { year: 'numeric', month: 'long', day: 'numeric' };
 var formattedDate = currentDate.toLocaleDateString(undefined, options);
 date.value = formattedDate; */



function withdraw_money() {

	var accountNumber = document.getElementById("accountNumber").value;
	var amount = document.getElementById("amount").value;
	var requestData = {
		AccountNumber: accountNumber,
		Amount: amount,
	};

	// Send the AJAX request
	$.ajax({
		url: 'moneyWithDrawlUrl',
		type: 'POST',
		data: requestData,
		success: function(response) {
			$('#successMessage').text(response).fadeIn();

			setTimeout(function() {
				$('#successMessage').fadeOut();
			}, 3000);
			form.trigger('reset'); // Optional: Reset the form after successful submission
			console.log('Third server request success:', response);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			// Handle the error
			console.error('Third server request error:', textStatus, errorThrown);
		}
	});

}