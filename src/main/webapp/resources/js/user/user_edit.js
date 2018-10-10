$(function () {
    $(function () {
            $(".btn-save-changes").click(function (e) {
                    var userForm = $(".user-form");
                    $(function () {
                        userForm.validate({
                            rules: {
                                firstName: {
                                    required: true,
                                    maxlength: 16
                                },
                                lastName: {
                                    required: true,
                                    maxlength: 16
                                },
                                email: {
                                    required: true,
                                    email: true
                                },
                                phone: {
                                    required: true,
                                    minlength: 10,
                                    maxlength: 18
                                },
                                password: {
                                    minlength: 6,
                                    maxlength: 12
                                }
                            },
                            messages: {
                                firstName: {
                                    required: "First name cant be empty!",
                                    maxlength: "Max length: 16 symbols!"
                                },
                                lastName: {
                                    required: "Last name cant be empty!",
                                    maxlength: "Max length: 16 symbols!"
                                },
                                email: {
                                    required: "Email cant be empty!",
                                    email: "Is not valid email address!"
                                },
                                phone: {
                                    required: "Phone cant be empty!",
                                    minlength: "Min length: 10 symbols!",
                                    maxlength: "Max length: 18 symbols!"
                                },
                                password: {
                                    minlength: "Min length: 6 symbols!",
                                    maxlength: "Max length: 12 symbols!"
                                }
                            }
                        });
                    });
                    if(userForm.valid()){
                        let service = new AJAXService();
                        var btn = $(this);
                        var user = new User($('#fname').val(),
                            $('#lname').val(), $('#email').val(),
                            $('#phone').val(), $('#pass').val());

                        function success() {
                            location.replace("/users");
                        };

                        function fail() {
                            console.log("fail");
                        };
                        service.put("/api/1.0/users/" + btn.val(), user, success, fail);
                        e.preventDefault();
                    }
            }
            );
        }
    );

    $(function () {
        $(".form-control").dblclick(function (e) {
            $(this).attr("readonly",false);
        });
    });

    $(function () {
        $(".form-control").blur(function (e) {
            $(this).attr("readonly",true);
        });
    });

});