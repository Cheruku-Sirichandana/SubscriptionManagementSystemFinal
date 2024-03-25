package com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface;

import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Admin;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Content;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.AdminModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.ContentModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.SubscriptionPlanModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminInterface {
    Admin adminRegistered(AdminModel adminModel);
    Boolean adminCheck(AdminModel adminModel);
    Content addedContent(ContentModel contentModel);
    String deleteContent(int contentId);
    String deleteSubscriptionPlan(int planId);
    String updatedContent(ContentModel contentModel,int contentId);
    List<ContentModel> viewContent();
    SubscriptionPlanModel addedSubscriptionPlan(SubscriptionPlanModel subscriptionPlanModel);
    List<SubscriptionPlanModel> viewSubscriptionPlans();
    SubscriptionPlan updatedSubscriptionPlans(SubscriptionPlanModel subscriptionPlan,int planId);
    SubscriptionPlanModel searchByPlanId(int planId);
    ContentModel searchByContentId(int contentId);
    SubscriptionPlanModel getSubscriptionPlanById(int planId);
    String getPlanNameByPlanId(int planId);
    SubscriptionPlanModel createUpdatedSubscriptionPlan(SubscriptionPlanModel subscriptionPlanModel);
}
