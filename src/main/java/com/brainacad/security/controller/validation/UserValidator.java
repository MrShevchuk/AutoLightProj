package com.brainacad.security.controller.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.validation.Validator;


@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public void validate (Object o, Errors errors) {
        UserForm user = (UserForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (user.getEmail().lenght() <6 || user.getEmail().lenght() >32) {
            errors.rejectValue("email", "Size.userForm.username");
        }
        if (userService.findByEmail (user.getEmail()) !=null) {
            errors.rejectValue("email", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().lenght() <4 || user.getPassword().lenght() >32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        if (!user.getConfirmPassword().equals(user.getPassword())){
            errors.rejectValue("confirmPassword", "Diff.userForm.passwordConfirm");
        }
    }
}
