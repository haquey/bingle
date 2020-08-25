(function() {
    "use strict";
    
    window.onload = function() {
    	
    	var images = ['../assets/DSC_4187.JPG', '../assets/DSC_4188.JPG', '../assets/DSC_4202.JPG', '../assets/DSC_4204.JPG', '../assets/DSC_4205.JPG', 
    	'../assets/DSC_4206.JPG', '../assets/DSC_4207.JPG', '../assets/DSC_4208.JPG', '../assets/DSC_4214.JPG', '../assets/DSC_4215.JPG', 
    	'../assets/DSC_4220.JPG', '../assets/DSC_4241.JPG', '../assets/DSC_4242.JPG', '../assets/DSC_4243.JPG', '../assets/DSC_4244.JPG', 
    	'../assets/DSC_4245.JPG', '../assets/DSC_4246.JPG', '../assets/DSC_4247.JPG' ]
    	var randomImage = Math.floor(Math.random() * 18); // 0 to length of images array - 1
    	$(".homepage").css("background-image", "url('" + images[randomImage] + "')");
       
        // function getCurrentUser() {
        //     var l = document.cookie.split("user=");
        //     if (l.length > 1) return l[1];
        //     return null;
        // };

        // if(getCurrentUser() != null) {
        //     console.log(getCurrentUser());
        //     document.querySelector("#nav-username").innerHTML = getCurrentUser();
        // }
        // Alternates between account types
        $(document).on('click', '[name="AccType"]', function (e) {
            e.stopPropagation();

            $(this).addClass('btn-primary')
                .siblings().not(this).removeClass('btn-primary');
        })

        $('#submitInfo').click(function (e) {
            e.preventDefault();
            e.stopPropagation();

            var first_name = $('#fname-inpt').val();
            var last_name = $('#lname-inpt').val();
            var email = $('#email-inpt').val();
            var username = $('#username-inpt').val();
            var password = $('#password-inpt').val();
            var account_type;
            var valid = true;
            if ($('#studentType-btn').attr('class') == 'btn btn-primary') {
                account_type = "Student";
            }
            else {
                account_type = "Instructor";
            }

            $('#fname-inpt').css("border", "");
            $('#lname-inpt').css("border", "");
            $('#email-inpt').css("border", "");
            $('#username-inpt').css("border", "");
            $('#password-inpt').css("border", "");

            if (first_name == "")
            {
                $('#fname-inpt').css("border", "3px solid #fb6672");
                valid = false;
            }
            if (last_name == "")
            {
                $('#lname-inpt').css("border", "3px solid #fb6672");
                valid = false;
            }
            if (email == "")
            {
                $('#email-inpt').css("border", "3px solid #fb6672");
                valid = false;

            }
            if (username == "")
            {
                $('#username-inpt').css("border", "3px solid #fb6672");
                valid = false;
            }
            if (password == "")
            {
                $('#password-inpt').css("border", "3px solid #fb6672");
                valid = false;
            }
            if (valid){
                var registration_info = {
                    'first_name': first_name,
                    'last_name': last_name,
                    'email': email,
                    'username': username,
                    'password': password,
                    'account_type': account_type

                };
                $.ajax({
                    type: "PUT",
                    url: "/register",
                    data: JSON.stringify(registration_info),
                    contentType: "application/json",
                    success: function () {
                        location.reload();
                    },
                    error: function (msg, status, error) {
                        console.log(msg);
                        document.querySelector('#registerError').innerHTML = msg.responseText;
                        // console.log('An error occurred.');
                    },
                });
        }});

        document.querySelector('#loginForm').addEventListener('submit', function(e){

            // prevent from refreshing the page on submit
            e.preventDefault();
            var username = document.querySelector("#loginName").value;
            var password = document.querySelector("#loginPassword").value;

            /**
            $.ajax({
                type: 'POST',
                url: '/login',
                complete : function(xhr,e) {
                    console.log(xhr.status);
                },
                data: {"username": username, "password": password},
                dataType: 'json'
            });
            **/
            $.post('/login', {"username": username, "password": password})
                .done(function(msg, status, error){ location.reload()})
                .fail(function(xhr, status) {
                    document.querySelector('#loginError').innerHTML = "Wrong name or password";
                });


            });


    };
}());

