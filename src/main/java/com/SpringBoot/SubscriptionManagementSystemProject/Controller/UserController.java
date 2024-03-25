package com.SpringBoot.SubscriptionManagementSystemProject.Controller;

import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Content;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Users;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.*;
import com.SpringBoot.SubscriptionManagementSystemProject.Service.AdminServices;
import com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface.UserInterface;
import com.SpringBoot.SubscriptionManagementSystemProject.Validations.UserValidations;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserInterface userServices;
    @Autowired
    private AdminServices adminServices;
    @Autowired
    private UserValidations userValidations;
    private int userId;

    @RequestMapping("/user")
    public String user(ModelMap model){
        int count=1;
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.NONE;
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("count",count);
        System.out.println("COUNT"+count);
        return "user";
    }
    @RequestMapping("/userRegister")
    public String userRegister(ModelMap model,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count){
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("count",count);
        model.addAttribute("userModel",new UserModel());
        return "userRegistration";
    }
    @RequestMapping("/userRegistered")
    public String userRegistered(@Valid @ModelAttribute("userModel") UserModel userModel, ModelMap model, @RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus, @RequestParam("count") int count, BindingResult bindingResult){
        userValidations.validate(userModel,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("subscriptionStatus",subscriptionStatus);
            model.addAttribute("count",count);
            System.out.println(bindingResult.getAllErrors());
            return "userRegistration";
        }
        UserModel userModel1= userServices.userRegistered(userModel);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        if(userModel1!=null){
           model.addAttribute("msg", "User is registered successfully");
           return "user";
        }
        else {
            model.addAttribute("msg1", "user is already available..Please add new USER");
            return "userRegistration";
        }
    }
    @RequestMapping("/viewUsers")
    public String viewUsers(ModelMap model){
        List<UserModel> userModelList=userServices.viewUsers();
        model.addAttribute("userModelList",userModelList);
        return "viewUsers";
    }
    @RequestMapping("/userLogin")
    public String userLogin(ModelMap model,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count) {
        model.addAttribute("subscriptionStatus", subscriptionStatus);
        model.addAttribute("count",count);
        model.addAttribute("userModel",new UserModel());
        return "userLogin";
    }
    @RequestMapping("/userCheck")
    public String userCheck(@Valid @ModelAttribute("userModel") UserModel userModel,ModelMap model,@RequestParam("userId") int userId ,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count,BindingResult bindingResult){

        userValidations.validate1(userModel,bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("subscriptionStatus",subscriptionStatus);
            model.addAttribute("count",count);
            return "userLogin";
        }
        Boolean userFound=userServices.userCheck(userModel);
        if(userFound){
            int userId1=userServices.findUserId(userModel);
            model.addAttribute("userId",userId1);
            model.addAttribute("user",userModel);
            SubscriptionStatus subscriptionStatus1=userServices.updatedStatus(userModel);
            model.addAttribute("subscriptionStatus",subscriptionStatus1);
            model.addAttribute("count",count);
            if(subscriptionStatus1.equals(SubscriptionStatus.ACTIVE)){
               int planId= userServices.getPlanId(userId1);
               model.addAttribute("planId",planId);
               return "subscriberFeatures";
            }
            return "userFeatures";
        }
        else{
            model.addAttribute("msg1","Sorry!!User not registered!Please Re-enter the credentials");
            model.addAttribute("subscriptionStatus",subscriptionStatus);
            model.addAttribute("count",count);
            return "userLogin";
        }
    }
    @RequestMapping("/subscriberFeatures")
    public String subscriberFeatures(@RequestParam("userId") int userId,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count, ModelMap model){
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("count",count);
        model.addAttribute("userId",userId);
        return "subscriberFeatures";
    }
    @RequestMapping("/subscribe")
    public String viewSubscriptionPlansForUser(ModelMap model, @RequestParam("userId") int userId, @RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count){
        List<SubscriptionPlanModel> subscriptionPlanModelList=adminServices.viewSubscriptionPlans();
        model.addAttribute("userId",userId);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionModelList",subscriptionPlanModelList);
        return "userSelectingFromSubscriptionPlans";
    }
    @RequestMapping("/selectingSubscriptionPlanByUser")
    public String selectingSubscriptionPlanByUser(@RequestParam("userId") int userId, @RequestParam("planId") int planId,@RequestParam("planPrice") double planPrice,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count, ModelMap model){
        model.addAttribute("userId",userId);
        model.addAttribute("planId",planId);
        model.addAttribute("planPrice",planPrice);
        SubscriptionStatus subscriptionStatus1 = userServices.setSubscriptionStatus(userId,planId);
        model.addAttribute("subscriptionStatus",subscriptionStatus1);
        String paymentStatus="Pending";
        model.addAttribute("paymentStatus",paymentStatus);
        String paymentStatus1=userServices.setPaymentStatus(paymentStatus);
        model.addAttribute("paymentStatus1",paymentStatus1);
        model.addAttribute("paymentModel",new PaymentModel());
        model.addAttribute("count",count);
        return "payment";
    }
    @RequestMapping("/paidPaymentForSubscription")
    public String paidPaymentForSubscription(PaymentModel paymentModel,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("userId") int userId, @RequestParam("planId") int planId, @RequestParam("count") int count, ModelMap model){
        SubscriptionPlanModel subscriptionPlanModel=userServices.findPlanByPlanId(planId);
        model.addAttribute("userId",userId);
        model.addAttribute("planId",planId);
        model.addAttribute("planModel",subscriptionPlanModel);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("paymentModel",paymentModel);
        if( subscriptionStatus==SubscriptionStatus.ACTIVE && count==1  ||  (subscriptionStatus==SubscriptionStatus.ACTIVE && count!=1)) {
            SubscriptionStatus subscriptionStatus1 = userServices.setSubscriptionStatus(userId,planId);
            userServices.removeSubscriptionPlan(userId, planId);
            model.addAttribute("subscriptionStatus", subscriptionStatus1);
            userServices.save(paymentModel);
            List<Content> contentList = userServices.display(userId, planId, subscriptionStatus);
            if (contentList != null) {
                model.addAttribute("contentList1", contentList);
                SubscriptionStatus subscriptionStatus2 = SubscriptionStatus.NONE;
                model.addAttribute("subscriptionStatus2", subscriptionStatus2);
                return "viewSubscriptionContentOfUser";
            }
        }
        else{
            return "payment";
        }
        return "changeSubscriptionPlan";
    }
    @RequestMapping("/subscriptionContentForUser")
    public String subscriptionContentForUser(@RequestParam("count") int count, @RequestParam("userId") int userId, @RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus, ModelMap model) {
        List<Content> contentList = userServices.subscriptionContentForUser(userId);
        if (contentList != null) {
            model.addAttribute("contentList", contentList);
            model.addAttribute("userId", userId);
            model.addAttribute("subscriptionStatus", subscriptionStatus);
            model.addAttribute("count", count);
            String planName = userServices.getPlanName(userId);
            SubscriptionPlan subscriptionPlan = userServices.getSubscriptionPlan(userId);
            if (subscriptionPlan == null) {
                return "viewFreeContent";
            }
            int planId = subscriptionPlan.getPlanId();
            model.addAttribute("planId", planId);
            model.addAttribute("planName", planName);
            return "viewSubscriptionContentForUser";
        }
        return "content";
    }
    @RequestMapping("/changeSubscriptionPlan")
    public String extra(@ModelAttribute("paymentModel") PaymentModel paymentModel,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("userId") int userId, @RequestParam("planId") int planId,SubscriptionPlanModel subscriptionPlanModel, @RequestParam("count") int count, ModelMap model){
        String planName=userServices.findPlanName(subscriptionPlanModel);
        model.addAttribute("planName",planName);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        model.addAttribute("userId",userId);
        model.addAttribute("planId",planId);
        model.addAttribute("planModel",subscriptionPlanModel);
        model.addAttribute("planName",planName);
        return "changeSubscriptionPlan";
    }
    @RequestMapping("/upgrade")
    public String upgradeSubscription(@RequestParam("userId") int userId, @RequestParam("planName") String planName,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count,@RequestParam("planId") int planId, ModelMap model){
        count=count+1;
        List<SubscriptionPlan> subscriptionPlanList=userServices.upgradeSubscription(userId,planName);
        model.addAttribute("subscriptionPlanList",subscriptionPlanList);
        model.addAttribute("userId",userId);
        model.addAttribute("count",count);
        model.addAttribute("planId",planId);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        return "upgrade";
    }
    @RequestMapping("/degrade")
    public String degradeSubscriptionPlan(@RequestParam("userId") int userId,@RequestParam("planName") String planName,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count,@RequestParam("planId") int planId, ModelMap model){
        count=count+1;
        List<SubscriptionPlan> subscriptionPlanList=userServices.degradeSubscription(userId,planName);
        model.addAttribute("subscriptionPlanList",subscriptionPlanList);
        model.addAttribute("userId",userId);
        model.addAttribute("planId",planId);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        return "downgrade";
    }
    @RequestMapping("/dashboard")
    public String dashboard(ModelMap model,@RequestParam("userId") int userId,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count) {
        model.addAttribute("userId",userId);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        return "dashboard";
    }
    @RequestMapping("/primePreferredPlan")
    public String primePreferredPlan(ModelMap model,@RequestParam("userId") int userId,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count){
       SubscriptionPlan subscriptionPlan=userServices.primePreferredPlan();
        model.addAttribute("userId",userId);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionPlan",subscriptionPlan);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
        return "primePreferredPlan";
    }
    @RequestMapping("/userWithHighestPaidTier")
    public String userWithHighestPaidTier(ModelMap model,@RequestParam("userId") int userId,@RequestParam("subscriptionStatus") SubscriptionStatus subscriptionStatus,@RequestParam("count") int count){
       Users user= userServices.userWithHighestPaidTier();
        model.addAttribute("userId",userId);
        model.addAttribute("count",count);
        model.addAttribute("subscriptionStatus",subscriptionStatus);
       model.addAttribute("user",user);
       return "userWithHighestPaidTier";
    }
}

