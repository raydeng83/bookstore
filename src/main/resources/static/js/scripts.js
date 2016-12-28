// Empty JS for your own code to be here
function checkPasswordMatch() {
    var password = $("#txtNewPassword").val();
    var confirmPassword = $("#txtConfirmPassword").val();

    if (password == "" && confirmPassword == "") {
        $("#checkPasswordMatch").html("");
        $("#updateUserInfoButton").prop('disabled', false);
    } else {

        if (password != confirmPassword) {
            $("#checkPasswordMatch").html("Passwords do not match!");
            $("#updateUserInfoButton").prop('disabled', true);
        }
        else {
            $("#checkPasswordMatch").html("Passwords match.");
            $("#updateUserInfoButton").prop('disabled', false);
        }
    }
}

function checkBillingAddress() {
    if($("#theSameAsShippingAddress").is(":checked")) {
        $(".billingAddress").prop('disabled', true);
    } else {
        $(".billingAddress").prop('disabled', false);
    }
}

$(document).ready(function () {
    $("#txtConfirmPassword").keyup(checkPasswordMatch);
    $("#txtNewPassword").keyup(checkPasswordMatch);
    $("#theSameAsShippingAddress").on('click', checkBillingAddress);
});

