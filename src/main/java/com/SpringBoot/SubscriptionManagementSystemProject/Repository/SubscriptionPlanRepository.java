package com.SpringBoot.SubscriptionManagementSystemProject.Repository;

import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan,Integer> {
    SubscriptionPlan findByPlanPrice(Double price);
    SubscriptionPlan findByPlanName(String planName);
    SubscriptionPlan findByPlanId(int planId);
}
