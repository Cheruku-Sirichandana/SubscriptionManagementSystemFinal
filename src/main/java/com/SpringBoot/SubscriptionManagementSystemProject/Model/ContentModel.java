package com.SpringBoot.SubscriptionManagementSystemProject.Model;

import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Content;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ContentModel {
    private int contentId;
    private String title;
    private String description;
    private ContentType type;
    private String category;
    private int durationInMinutes;
    private String subscriptionLevel;

     List<SubscriptionPlan> subscriptionPlanList=new ArrayList<>();

}
