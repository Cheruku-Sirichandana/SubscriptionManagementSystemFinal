package com.SpringBoot.SubscriptionManagementSystemProject.Service;

import com.SpringBoot.SubscriptionManagementSystemProject.Conversions.Entity_Model;
import com.SpringBoot.SubscriptionManagementSystemProject.Conversions.Model_Entity;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.*;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.*;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.ContentRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.PaymentRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.SubscriptionPlanRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.Repository.UserRepository;
import com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServices implements UserInterface {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private Entity_Model entityModel;
    @Autowired
    private Model_Entity modelEntity;
    public UserModel userRegistered(UserModel userModel){
       Users user=userRepository.findByUserName(userModel.getUserName());
       if(user==null){
           Users user1= modelEntity.userModel_Entity(userModel);
           user1.setRole("ROLE_USER");
           user1.setUserName(userModel.getUserName());
           user1.setUserPassword(passwordEncoder.encode(userModel.getUserPassword()));
           user1.setUserEmail(userModel.getUserEmail());
           user1.setSubscriptionPlan(userModel.getSubscriptionPlan());
           user1.setSubscriptionStatus(userModel.getSubscriptionStatus());
           userRepository.save(user1);
           return userModel;
       }
       return null;
   }
    public Boolean userCheck(UserModel userModel) {
        return userRepository.findAll().stream().anyMatch(users -> users.getUserName().equals(userModel.getUserName()) && passwordEncoder.matches(userModel.getUserPassword(),users.getUserPassword()));
    }
    public int findUserId(UserModel userModel) {
        Users user1=userRepository.findByUserName(userModel.getUserName());
        int userId1=user1.getUserId();
        return userId1;
    }
    public int getPlanId(int userId) {
        Users users=userRepository.findById(userId).orElse(null);
        if(users!=null){
            return users.getSubscriptionPlan().getPlanId();
        }
        return 0;
    }
    public SubscriptionPlan getSubscriptionPlan(int userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getSubscriptionPlan();
        }
        return null;
    }
    public List<UserModel> viewUsers(){
        List<Users> usersList=userRepository.findAll();
        List<UserModel> userModelList=new ArrayList<>();
        usersList.forEach(s->
        {
            UserModel userModel = entityModel.userEntity_Model(s);
            userModelList.add(userModel);
        });
        return userModelList;
    }
    public SubscriptionStatus subscriptionStatus(UserModel userModel){
       Users users=userRepository.findById(userModel.getUserId()).orElse(null);
       if(users!=null){
           SubscriptionStatus subscriptionStatus=users.getSubscriptionStatus();
           return subscriptionStatus;
       }
       return null;
    }
    public SubscriptionStatus updatedStatus(UserModel userModel){
        Users user1=userRepository.findByUserName(userModel.getUserName());
        Users user=userRepository.findById(user1.getUserId()).orElse(null);
        if (user != null && passwordEncoder.matches(userModel.getUserPassword(),user.getUserPassword()))
        {
                SubscriptionStatus subscriptionStatus=user.getSubscriptionStatus();
                return subscriptionStatus;
        }
        return SubscriptionStatus.NONE;
    }
    public List<Content> display(int userId, int planId, SubscriptionStatus subscriptionStatus) {
        Users user=userRepository.findById(userId).orElse(null);
        SubscriptionPlan newSubscriptionPlan = subscriptionPlanRepository.findById(planId).orElse(null);
        if (user != null && newSubscriptionPlan != null) {
            SubscriptionPlan currentSubscriptionPlan = user.getSubscriptionPlan();
            if (currentSubscriptionPlan != null) {
                List<Users> usersOfCurrentPlan = currentSubscriptionPlan.getUsers();
                usersOfCurrentPlan.remove(user);
                subscriptionPlanRepository.save(currentSubscriptionPlan);
            }
            user.setSubscriptionPlan(newSubscriptionPlan);
            userRepository.save(user);
            List<Content> contentList=contentRepository.findBySubscriptionLevel(newSubscriptionPlan.getPlanName());
            return contentList;
        }
        return null;
    }
    public SubscriptionStatus setSubscriptionStatus(int userId, int planId) {
        Users users = userRepository.findById(userId).orElse(null);
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId).orElse(null);
        if (users != null && subscriptionPlan != null) {
            if (users.getSubscriptionStatus() != SubscriptionStatus.ACTIVE) {
                users.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                users.setSubscriptionPlan(subscriptionPlan);
                List<Users> planUsers = new ArrayList<>(subscriptionPlan.getUsers());
                if (!planUsers.contains(users)) {
                    planUsers.add(users);
                    subscriptionPlan.setUsers(planUsers);
                }
                subscriptionPlanRepository.save(subscriptionPlan);
                userRepository.save(users);
                return SubscriptionStatus.ACTIVE;
            } else {
                return SubscriptionStatus.ACTIVE;
            }
        } else {
            return SubscriptionStatus.NONE;
        }
    }
    public int planId(int userId){
        Users users=userRepository.findById(userId).orElse(null);
        if(users!=null){
            int planId=users.getSubscriptionPlan().getPlanId();
            return planId;
        }
        return 0;
    }

    @Override
    public String setPaymentStatus(String paymentStatus) {
        return null;
    }

    public PaymentModel save(PaymentModel paymentModel){
        Payment payment = new Payment();
        payment.setPaymentId(paymentModel.getPaymentId());
        payment.setPaymentMethod(paymentModel.getPaymentMethod());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime paymentDate = LocalDate.parse(paymentModel.getFormattedDate(), formatter).atStartOfDay();
        paymentModel.setPaymentDate(paymentDate);
        payment.setPaymentDate(paymentModel.getPaymentDate());
        payment.setPaymentStatus(paymentModel.getPaymentStatus());
        payment.setAmount(paymentModel.getAmount());
        Payment savedPayment = paymentRepository.save(payment);
        if (savedPayment != null) {
        return paymentModel;
        } else {
            return null;
        }
    }
    public List<Content> subscriptionContentForUser(int userId){
        Users user=userRepository.findById(userId).orElse(null);
        if(user!=null) {
           SubscriptionStatus subscriptionStatus = user.getSubscriptionStatus();
           if (subscriptionStatus == SubscriptionStatus.ACTIVE) {
               SubscriptionPlan subscriptionPlan = user.getSubscriptionPlan();
               List<Content> contentList=subscriptionPlan.getContentList();
               contentList.removeIf(content -> !subscriptionPlan.getPlanName().equals(content.getSubscriptionLevel()));
               return contentList;

           }
           else {
               List<Content> freeContents = contentRepository.findBySubscriptionLevel("Free");
               return freeContents;
           }
       }
       return null;
    }
    public List<SubscriptionPlan> upgradeSubscription(int userId,String planName){
    Users users=userRepository.findById(userId).orElse(null);
       if(users!=null){
           if(planName.equals("Basic")){
               List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
               List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
               subcriptionPlans.forEach(s->
               {
                   if(s.getPlanName().equals("Premium") || s.getPlanName().equals("Gold") || s.getPlanName().equals("Platinum")){
                   subscriptionPlanList.add(s);
                   }
               });
               return subscriptionPlanList;
           }
           if(planName.equals("Premium")){
               List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
               List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
               subcriptionPlans.forEach(s->
               {
                   if( s.getPlanName().equals("Gold") || s.getPlanName().equals("Platinum")){
                       subscriptionPlanList.add(s);
                   }
               });
               return subscriptionPlanList;
           }
           if(planName.equals("Gold")){
               List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
               List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
               subcriptionPlans.forEach(s->
               {
                   if( s.getPlanName().equals("Platinum")){
                       subscriptionPlanList.add(s);
                   }
               });
               return subscriptionPlanList;

           }
       }
       return null;
    }
    public String findPlanName(SubscriptionPlanModel subscriptionPlanModel){
       int planId=subscriptionPlanModel.getPlanId();
       SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);
       if(subscriptionPlan!=null){
           return subscriptionPlan.getPlanName();
       }
       return "null";
    }
    public String removeSubscriptionPlan(int userId, int planId) {
        Users user=userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "User not found";
        }
        SubscriptionPlan newSubscriptionPlan = subscriptionPlanRepository.findById(planId).orElse(null);
        if (newSubscriptionPlan == null) {
            return "New subscription plan not found";
        }
        SubscriptionPlan currentSubscriptionPlan = user.getSubscriptionPlan();
        if (currentSubscriptionPlan != null) {
            if (currentSubscriptionPlan.getPlanId() == newSubscriptionPlan.getPlanId()) {
                return "User already subscribed to this plan";
            }
            List<Users> userList = currentSubscriptionPlan.getUsers();
            userList.remove(user);
            subscriptionPlanRepository.save(currentSubscriptionPlan);
        }
        user.setSubscriptionPlan(newSubscriptionPlan);
        userRepository.save(user);
        return "Successfully updated user's subscription plan";
    }
    public String getPlanName(int userId) {
        Users users=userRepository.findById(userId).orElse(null);
        if (users != null) {
            if (users.getSubscriptionPlan() != null) {
                String planName = users.getSubscriptionPlan().getPlanName();
                return planName;
            }
        }
        return null;
    }
    public List<SubscriptionPlan> degradeSubscription(int userId,String planName){
    Users user=userRepository.findById(userId).orElse(null);
    if(user!=null){
           if(planName.equals("Premium")){
               List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
              List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
              subcriptionPlans.forEach(s->
               {
                  if( s.getPlanName().equals("Basic")){
                       subscriptionPlanList.add(s);
                }
               });
               return subscriptionPlanList;
           }
          if(planName.equals("Gold")){
             List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
               List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
               subcriptionPlans.forEach(s->
               {
                   if( s.getPlanName().equals("Basic") || s.getPlanName().equals("Premium")){
                     subscriptionPlanList.add(s);
                   }
              });
              subscriptionPlanList.forEach(i -> System.out.println(i.getPlanName()));
              return subscriptionPlanList;
          }
          if(planName.equals("Platinum")){
              List<SubscriptionPlan> subcriptionPlans=subscriptionPlanRepository.findAll();
              List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
              subcriptionPlans.forEach(s->
              {
                  if( s.getPlanName().equals("Basic") || s.getPlanName().equals("Premium") || s.getPlanName().equals("Gold")){
                      subscriptionPlanList.add(s);
                  }
              });
              return subscriptionPlanList;
          }
      }
       return null;
    }

    public SubscriptionPlan primePreferredPlan() {
        List<Users> usersList = userRepository.findAll();
        List<SubscriptionPlan> subscriptionPlanList = usersList.stream()
                .map(Users::getSubscriptionPlan)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<SubscriptionPlan, Long> planUserCountMap = subscriptionPlanList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Find the plan with the maximum number of users
        Optional<Map.Entry<SubscriptionPlan, Long>> maxEntry = planUserCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // If there is a plan with the maximum number of users, return it
        return maxEntry.map(Map.Entry::getKey).orElse(null);
    }


    public Users userWithHighestPaidTier(){
        List<Users> usersList=userRepository.findAll();
        List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();
        List<Double> priceList=new ArrayList<>();
        usersList.forEach(users -> {
            if(users.getSubscriptionPlan()!=null){
            priceList.add(users.getSubscriptionPlan().getPlanPrice());}
        });
        Double price=priceList.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow();

        SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findByPlanPrice(price);
        List<Users> users=userRepository.findAll();
        Users user = users.stream()
                .filter(users1 -> users1.getSubscriptionPlan() != null && users1.getSubscriptionPlan().equals(subscriptionPlan))
                .findFirst()
                .orElse(null);


        return user;
    }
    public SubscriptionPlanModel findPlanByPlanId(int planId){
        SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(planId).orElse(null);
        SubscriptionPlanModel subscriptionPlanModel=entityModel.subscriptionEntity_Model(subscriptionPlan);
        return subscriptionPlanModel;
    }


}
