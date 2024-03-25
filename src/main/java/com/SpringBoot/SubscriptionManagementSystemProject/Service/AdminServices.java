package com.SpringBoot.SubscriptionManagementSystemProject.Service;

import com.SpringBoot.SubscriptionManagementSystemProject.Conversions.Entity_Model;
import com.SpringBoot.SubscriptionManagementSystemProject.Conversions.Model_Entity;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.*;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.AdminModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.ContentModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.SubscriptionPlanModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.SubscriptionStatus;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.AdminRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.ContentRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.SubscriptionPlanRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.UserRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface.AdminInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServices implements AdminInterface {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Entity_Model entityModel;
    @Autowired
    private Model_Entity modelEntity;

    public Admin adminRegistered(AdminModel adminModel) {
        Admin admin = adminRepository.findByAdminUsername(adminModel.getAdminUsername());
        if (admin == null) {
            Admin admin1 = modelEntity.adminModel_Entity(adminModel);
            admin1.setRole("ROLE_ADMIN");
            admin1.setAdminUsername(adminModel.getAdminUsername());
            admin1.setAdminPassword(passwordEncoder.encode(adminModel.getAdminPassword()));
            adminRepository.save(admin1);
            return admin1;
        }
        return null;
    }

    public Boolean adminCheck(AdminModel adminModel) {
        Admin admin = adminRepository.findByAdminUsername(adminModel.getAdminUsername());
        if (admin != null && passwordEncoder.matches(adminModel.getAdminPassword(), admin.getAdminPassword())) {
            {
                return true;
            }
        }
        return false;
    }

    public Content addedContent(ContentModel contentModel) {
        Content content = contentRepository.findById(contentModel.getContentId()).orElse(null);
        if (content == null) {
            Content content1 = modelEntity.contentModel_Entity(contentModel);
            contentRepository.save(content1);
            List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
            subscriptionPlanList.forEach(subscriptionPlan -> {
                if (subscriptionPlan.getPlanName().equals(content1.getSubscriptionLevel())) {
                    List<Content> contentList = subscriptionPlan.getContentList();
                    contentList.add(content1);
                    subscriptionPlan.setContentList(contentList);
                    subscriptionPlanRepository.save(subscriptionPlan);
                }
            });
            content = content1;
        }
        return content;
    }
    public String deleteContent(int contentId) {
        Content content = contentRepository.findById(contentId).orElse(null);
        if (content != null) {
            List<SubscriptionPlan> subscriptionPlanList = content.getSubscriptionPlanList();
            subscriptionPlanList.forEach(subscriptionPlan -> {
                List<Content> contentList = subscriptionPlan.getContentList();
                if (contentList.contains(content)) {
                    contentList.remove(content);
                    subscriptionPlan.setContentList(contentList);
                    subscriptionPlanRepository.save(subscriptionPlan);
                }
            });
        content.setSubscriptionPlanList(new ArrayList<>());
        contentRepository.delete(content);
        return "Deleted Successfully";
    }
    return "msg";
    }
    public String deleteSubscriptionPlan(int planId) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId).orElse(null);
        if (subscriptionPlan == null) {
            return "Subscription plan not found";
        }
        else{
        List<Content> contentList = subscriptionPlan.getContentList();
        List<Users> users = userRepository.findAll();
        contentList.forEach(content -> {
        List<SubscriptionPlan> subscriptionPlanList =new ArrayList<>(content.getSubscriptionPlanList()) ;
        subscriptionPlanList.remove(subscriptionPlan);
        content.setSubscriptionPlanList(subscriptionPlanList);
        contentRepository.save(content);
        });
            boolean bool = users.stream()
                    .filter(user -> {
                        SubscriptionPlan subscriptionPlan1 = user.getSubscriptionPlan();
                        return subscriptionPlan1 != null && subscriptionPlan1.getPlanId() == planId;
                    })
                    .anyMatch(user -> user.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE));

            if(bool){
          return "msg";
        }
        subscriptionPlan.setContentList(new ArrayList<>());
        subscriptionPlanRepository.delete(subscriptionPlan);
        return "deleted";
      }}
    public String updatedContent(ContentModel contentModel,int contentId) {
        Content content = contentRepository.findById(contentId).orElse(null);
        if (content != null) {
            content.setTitle(contentModel.getTitle());
            content.setCategory(contentModel.getCategory());
            content.setDescription(contentModel.getDescription());
            content.setSubscriptionLevel(contentModel.getSubscriptionLevel());
            content.setDurationInMinutes(contentModel.getDurationInMinutes());
            content.setType(contentModel.getType());
            contentRepository.save(content);
            return "content updated";
        }
        return "content not found to update";
    }
    public List<ContentModel> viewContent(){
       List<Content> contentList=contentRepository.findAll();
       List<ContentModel> contents=new ArrayList<>();
       contentList.forEach(content->
       {
           ContentModel contentModel= entityModel.contentEntity_Model(content);
           contents.add(contentModel);
       });
       return contents;
   }

   //subscription
   public SubscriptionPlanModel addedSubscriptionPlan(SubscriptionPlanModel subscriptionPlanModel) {
       SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByPlanId(subscriptionPlanModel.getPlanId());
       if (subscriptionPlan == null) {
           SubscriptionPlan subscriptionPlan1 = modelEntity.subscriptionPlanModel_entity(subscriptionPlanModel);
           List<Content> contentList = contentRepository.findAll();
           contentList.forEach(content -> {
               if(content.getSubscriptionLevel().equals(subscriptionPlan1.getPlanName())){
                   List<Content> contentList1=subscriptionPlan1.getContentList();
                   contentList1.add(content);
                   subscriptionPlan1.setContentList(contentList1);
                   subscriptionPlanRepository.save(subscriptionPlan1);
               }
           });
       }
       return subscriptionPlanModel;
   }
    public List<SubscriptionPlanModel> viewSubscriptionPlans() {
        List<SubscriptionPlan> subscriptionPlanList = subscriptionPlanRepository.findAll();
        return subscriptionPlanList.stream()
                .map(subscriptionPlan -> entityModel.subscriptionEntity_Model(subscriptionPlan))
                .collect(Collectors.toList());
    }
    public String deleteSubscription(int planId) {
    SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);

        if (subscriptionPlan != null) {
            List<Content> contentList = subscriptionPlan.getContentList();
            if (contentList != null) {
                contentList.forEach(content -> {
                    List<SubscriptionPlan> subscriptionPlanList = content.getSubscriptionPlanList();
                    if (subscriptionPlanList != null && subscriptionPlanList.contains(subscriptionPlan)) {
                        subscriptionPlanList.remove(subscriptionPlan);
                        content.setSubscriptionPlanList(subscriptionPlanList);
                        contentRepository.save(content);
                    }
                });
            }
            subscriptionPlan.setContentList(new ArrayList<>());
            subscriptionPlanRepository.delete(subscriptionPlan);
            return "Deleted Successfully";
        }
        return "msg";
    }
    public SubscriptionPlanModel getSubscriptionPlanById(int planId){
        SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);
        SubscriptionPlanModel subscriptionPlanModel=entityModel.subscriptionEntity_Model(subscriptionPlan);
        return subscriptionPlanModel;
    }
    public SubscriptionPlanModel createUpdatedSubscriptionPlan(SubscriptionPlanModel subscriptionPlanModel) {
        SubscriptionPlanModel updatedSubscriptionPlan = new SubscriptionPlanModel();
        updatedSubscriptionPlan.setPlanId(subscriptionPlanModel.getPlanId());
        updatedSubscriptionPlan.setPlanName(subscriptionPlanModel.getPlanName());
        updatedSubscriptionPlan.setPlanFeatures(subscriptionPlanModel.getPlanFeatures());
        updatedSubscriptionPlan.setPlanDuration(subscriptionPlanModel.getPlanDuration());
        updatedSubscriptionPlan.setPlanPrice(subscriptionPlanModel.getPlanPrice());
        updatedSubscriptionPlan.setContentList(subscriptionPlanModel.getContentList());
        updatedSubscriptionPlan.setUsers(subscriptionPlanModel.getUsers());

        return updatedSubscriptionPlan;
    }
    public SubscriptionPlan updatedSubscriptionPlans(SubscriptionPlanModel subscriptionPlan,int planId){
        SubscriptionPlan subscriptionPlan1=subscriptionPlanRepository.findById(planId).orElse(null);
        if(subscriptionPlan1!=null){
           subscriptionPlan1.setPlanName(subscriptionPlan.getPlanName());
           subscriptionPlan1.setPlanFeatures(subscriptionPlan.getPlanFeatures());
           subscriptionPlan1.setPlanDuration(subscriptionPlan.getPlanDuration());
           subscriptionPlan1.setPlanPrice(subscriptionPlan.getPlanPrice());
           subscriptionPlan1.setUsers(subscriptionPlan.getUsers());
           subscriptionPlan1.setContentList(subscriptionPlan.getContentList());
           subscriptionPlanRepository.save(subscriptionPlan1);
           return subscriptionPlan1;
       }
       return null;
    }

    public SubscriptionPlanModel searchByPlanId(int planId){
       SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);
       if(subscriptionPlan!=null){
           SubscriptionPlanModel subscriptionPlanModel=entityModel.subscriptionEntity_Model(subscriptionPlan);
           return subscriptionPlanModel;
       }
       return null;
    }
    public ContentModel searchByContentId(int contentId){
       Content content=contentRepository.findById(contentId).orElse(null);
       if(content!=null){
           ContentModel contentModel=entityModel.contentEntity_Model(content);
           return contentModel;
       }
       return null;
    }
    public String getPlanNameByPlanId(int planId){
        SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);
        String planName=subscriptionPlan.getPlanName();
        return planName;
    }

}
