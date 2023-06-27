function deposit_money() {
	var loanid = document.getElementById("loanid").value;
	var emi = document.getElementById("emi").value;
	var interest = document.getElementById("interest").value;
	var total = document.getElementById("total").value;
	var amount = document.getElementById("amount").value;
	var complete = document.getElementById("complete").value;
	var date = document.getElementById("date").value;
	var installment_no = document.getElementById("remainMonths").value;
	var requestData = {
		Loanid: loanid,
		EMI: emi,
		Interset: interest,
		Total: total,
		Amount: amount,
		Complete: complete,
		Date: date,
		installment_no: installment_no
	};
	// Send the AJAX request
	$.ajax({
		url: 'loanRepaymentUrl',
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

/* 
  let date = document.getElementById("date");
  var currentDate = new Date();
  var options = { year: 'numeric', month: 'long', day: 'numeric' };
  var formattedDate = currentDate.toLocaleDateString(undefined, options);
  date.value = formattedDate; */