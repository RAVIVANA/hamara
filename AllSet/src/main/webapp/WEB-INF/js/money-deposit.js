  function validateAndProcessAccountNumber() {
        var input = document.getElementById("accountNumber");
        var value = input.value;

        // Validate the input
        if (value.length > 1) {
            input.value = value.slice(0, 1); // Restrict to one digit
        }

        // Call your function here with the account number value
        // Only if the input is valid and has a length of 1
        if (value.length === 1) {
            processAccountNumber(value);
        }
    }

    function processAccountNumber(accountNumber) {
        document.getElementById('resulttable').innerHTML = "";
        $.ajax({
            url: 'getAccountDetails',
            method: 'post',
            data: {accountNumber: accountNumber},
            success: function (resultText) {
                $('#resulttable').html(resultText);
            },
            error: function (jqXHR, exception) {
                console.log('Error occurred!');
            }
        })
        
        /* function processAccountNumber(accountNumber){
   	 document.getElementById('resulttable').innerHTML="";
   	  $.ajax({
   		  url: 'getaccountdetails',
   	      type: 'POST',
   	      data: { accountNumber: accountNumber },
   	      success: function (response) {
   		    	 $('#resulttable').html(resultText);
   		    },
   	  
   	    error: function(xhr, textStatus, errorThrown) {
               // Handle any error that occurred during the AJAX call
               console.error("Error during AJAX call:", errorThrown);
               // Your code to handle the error here
           }
   	  })
   }
   */
    }