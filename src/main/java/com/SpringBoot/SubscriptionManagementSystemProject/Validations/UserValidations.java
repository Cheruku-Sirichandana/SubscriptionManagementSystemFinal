package com.SpringBoot.SubscriptionManagementSystemProject.Validations;

import com.SpringBoot.SubscriptionManagementSystemProject.Model.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class UserValidations implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        UserModel userModel = (UserModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "userName");
        if (userModel.getUserEmail() != null&& !userModel.getUserEmail().endsWith("@gmail.com")){
            errors.rejectValue("userEmail","userEmail");
        }
        String userPassword=userModel.getUserPassword();
        if(userPassword!=null && userPassword.length()<6)
        {
            errors.rejectValue("userPassword", "userPassword");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"subscriptionStatus","subscriptionStatus");
    }
    public void validate1(Object target, Errors errors) {
        UserModel userModel = (UserModel) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "userName");
        String userPassword=userModel.getUserPassword();
        if(userPassword.isBlank()){
            errors.rejectValue("userPassword","userPassword1");
        }
        if(null!=userPassword && userPassword.length()<6)
        {
            errors.rejectValue("userPassword", "userPassword");
        }
    }
}
