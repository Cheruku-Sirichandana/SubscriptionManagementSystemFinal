package com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface;

import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Content;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Users;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserInterface {
     UserModel userRegistered(UserModel userModel);
     List<UserModel> viewUsers();
     Boolean userCheck(UserModel userModel);
     SubscriptionStatus subscriptionStatus(UserModel userModel);
     SubscriptionStatus updatedStatus(UserModel userModel);
     List<Content> display(int userId, int planId, SubscriptionStatus subscriptionStatus);
     SubscriptionStatus setSubscriptionStatus(int userId,int planId);
     int planId(int userId);
     String setPaymentStatus(String paymentStatus);
     PaymentModel save(PaymentModel paymentModel);
     List<Content> subscriptionContentForUser(int userId);
     List<SubscriptionPlan> upgradeSubscription(int userId, String planName);
     String findPlanName(SubscriptionPlanModel subscriptionPlanModel);
     String removeSubscriptionPlan(int userId, int planId);
     String getPlanName(int userId);
     List<SubscriptionPlan> degradeSubscription(int userId,String planName);
     Users userWithHighestPaidTier();
     SubscriptionPlan primePreferredPlan();
     SubscriptionPlanModel findPlanByPlanId(int planId);
     int findUserId(UserModel userModel);
     int getPlanId(int userId);
     SubscriptionPlan getSubscriptionPlan(int userId);

}
