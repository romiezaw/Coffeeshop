$(function() {
	console.log("Calling ajax");
    $(".add-to-cart-btn").click(function(e) {
        //e.preventDefault();
        var self = $(this);
        console.log( self.attr("productId"));
        $.ajax({
            url: "api/cart",
            type: "POST",
            data: self.attr("productId"),
            contentType: "application/json; charset=utf-8",
            success: function() {
                //do nothing
            	console.log("Loading...");
            }
        });
    });
});