$(function() {
	console.log("in shopping cart");
    $("#placeOrder-btn").click(function(e) {
    	console.log("in shopping cart..");
        $.ajax({
            type: "POST",
            url: "api/placeOrder",
            contentType: "application/json; charset=utf-8",
            success: function() {
            }
        });
    });
}); 