  /* let date = document.getElementById("date");
  var currentDate = new Date();
  var options = { year: 'numeric', month: 'long', day: 'numeric' };
  var formattedDate = currentDate.toLocaleDateString(undefined, options);
  date.value = formattedDate; */


  
  function deposit_money() {
	  
	  var accountNumber = document.getElementById("accountNumber").value;
	  var amount = document.getElementById("amount").value;      
	  var requestData = {
			  AccountNumber: accountNumber,
			  Amount: amount,
	  };

	  // Send the AJAX request
	  $.ajax({
	    url: 'moneyDepositUrl',
	    type: 'POST',
	    data: requestData,
	    success: function(response) {
	      // Handle the success response
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
 
/* 	  
      console.log("Money deposit done successfully");
		var formdata = $('#accountNumber').val();
		console.log(formdata);
      $.ajax({
        url: "moneyDepositurl",
        type: "GET",
    
    	
        success: function(response) {
          // Handle success response
          console.log(data);
          console.log("Money deposit done successfully");
          
        },
        error: function(xhr, status, error) {
          // Handle error response
          console.log("Error occurred while deposit");
          

        }
      }); */
      
    }
  