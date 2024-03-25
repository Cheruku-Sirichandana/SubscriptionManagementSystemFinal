package com.SpringBoot.SubscriptionManagementSystemProject.Controller;

import ch.qos.logback.core.model.Model;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Admin;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.Content;
import com.SpringBoot.SubscriptionManagementSystemProject.Entity.SubscriptionPlan;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.AdminModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.ContentModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Model.SubscriptionPlanModel;
import com.SpringBoot.SubscriptionManagementSystemProject.Service.AdminServices;
import com.SpringBoot.SubscriptionManagementSystemProject.ServiceInterface.AdminInterface;
import com.SpringBoot.SubscriptionManagementSystemProject.Validations.AdminValidations;
import com.SpringBoot.SubscriptionManagementSystemProject.Validations.ContentValidations;
import com.SpringBoot.SubscriptionManagementSystemProject.Validations.SubscriptionPlanValidations;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminInterface adminServices;
    @Autowired
    AdminValidations adminValidations;
    @Autowired
    ContentValidations contentValidations;
    @Autowired
    SubscriptionPlanValidations subscriptionPlanValidations;
    @RequestMapping("/home")
    public String home(){
        return "home";
    }
    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }
    @RequestMapping("/adminRegister")
    public String adminRegister(ModelMap map){
        map.addAttribute("adminModel", new AdminModel());
        return "adminRegister";
    }
    @RequestMapping("/adminRegistered")
    public String adminRegistered(@Valid @ModelAttribute("adminModel") AdminModel adminModel, BindingResult bindingResult, ModelMap map){
        adminValidations.validate(adminModel,bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "adminRegister";
        }
        Admin admin1= adminServices.adminRegistered(adminModel);
        if(admin1!=null){
            map.addAttribute("msg","You have Successfully  Registered");
            return "admin";
        }
        else{
            map.addAttribute("msg","Admin already present,cant register again..Please add new Credentials for Registration");
            return "adminRegister";
        }
    }
    @RequestMapping("/adminLogin")
    public String adminLogin(ModelMap map) {
        map.addAttribute("adminModel",new AdminModel());
        return "adminLogin";
    }
    @RequestMapping("/adminCheck")
    public String adminCheck(@Valid @ModelAttribute("adminModel") AdminModel adminModel,ModelMap model,BindingResult bindingResult){
        adminValidations.validate(adminModel,bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "adminLogin";
        }
        Boolean adminFound=adminServices.adminCheck(adminModel);
        if(adminFound){
            return "adminFeatures";
        }
        else{
            model.addAttribute("msg1","Sorry!!Admin not registered!Please Re-enter the credentials");
            return "adminLogin";
        }
    }
    @RequestMapping("/adminFeatures")
    public String adminFeatures(){
        return "adminFeatures";
    }
    @RequestMapping("/content")
    public String content(){
        return "content";
    }
    @RequestMapping("/addContent")
    public String addContent(ModelMap map){
        map.addAttribute("contentModel",new ContentModel());
        return "addContent";
    }
    @RequestMapping("/addedContent")
    public String addedContent(@Valid @ModelAttribute("contentModel") ContentModel contentModel,ModelMap model,BindingResult bindingResult) {
        contentValidations.validate(contentModel,bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "addContent";
        }
        Content content2= adminServices.addedContent(contentModel);
        if(content2!=null){
            return "redirect:/viewContent";
        }
        else {
            model.addAttribute("msg2", "content is already available..Cannot add again");
            return "addContent";
        }
    }
    @RequestMapping("/deleteContent")
    public String deletingContent(@RequestParam("contentId") int contentId,ModelMap model){
        String res=adminServices.deleteContent(contentId);
        if(res.equals("msg")){
            model.addAttribute("msg","Sorry...cannot delete content because content is already subscribed by some user!!!");
            List<ContentModel> contentList=adminServices.viewContent();
            model.addAttribute("contentList",contentList);
            return "viewContent";
        }
        return "redirect:/viewContent";
    }
    @RequestMapping("/updateContent")
    public String updateContent(ModelMap model,@RequestParam("contentId") int contentId,@RequestParam("title") String title ){
        ContentModel contentModel1=adminServices.searchByContentId(contentId);
        model.addAttribute("title",title);
        model.addAttribute("contentModel",contentModel1);
        return "updateContentDetails";
    }
    @RequestMapping("/updatedContent")
    public String updatedContent(@Valid @ModelAttribute("contentModel") ContentModel contentModel,
                                 BindingResult bindingResult,
                                 @RequestParam("title") String title,@RequestParam("contentId") int contentId,
                                 ModelMap model) {
        contentValidations.validate(contentModel, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("contentId",contentId);
            model.addAttribute("title", title);
            return "updateContentDetails";
        }
        String message = adminServices.updatedContent(contentModel,contentId);
        if ("content updated".equals(message)) {
            return "redirect:/viewContent";
        } else {
            model.addAttribute("errorMessage", message);
            return "updateContentDetails";
        }
    }

    @RequestMapping("/viewContent")
    public String viewContent(ModelMap model){
       List<ContentModel> contentList1= adminServices.viewContent();
       model.addAttribute("contentList1",contentList1);
       return "viewContent";
    }

    //Subscription Plans
    @RequestMapping("/subscriptionPlans")
    public String subscriptionPlan(){
        return "subscriptionPlans";
    }
    @RequestMapping("/addSubscriptionPlan")
    public String addSubscriptionPlan(ModelMap model){
        model.addAttribute("subscriptionPlan",new SubscriptionPlanModel());
        return "addSubscriptionPlan";
    }
    @RequestMapping("/addedSubscriptionPlan")
    public String addedSubscriptionPlan(@Valid @ModelAttribute("subscriptionPlan") SubscriptionPlanModel subscriptionPlanModel,ModelMap model,BindingResult bindingResult){
        subscriptionPlanValidations.validate(subscriptionPlanModel, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "addSubscriptionPlan";
        }
        SubscriptionPlanModel subscriptionPlan=adminServices.addedSubscriptionPlan(subscriptionPlanModel);
        if(subscriptionPlan!=null){
            model.addAttribute("subscriptionPlan",subscriptionPlan);
            return "redirect:/viewSubscriptionPlans";
        }
        else {
            model.addAttribute("msg6", "subscriptionPlan is already available..Add new SubscriptionPlan");
            return "addSubscriptionPlan";
        }
    }
    @RequestMapping("/deleteSubscriptionPlan")
    public String deleteSubscriptionPlan(@RequestParam("planId") int planId,ModelMap model){
        String res=adminServices.deleteSubscriptionPlan(planId);
        if(res.equals("msg")){
            model.addAttribute("msg","Sorry...cannot delete subscription plan because subscriptionplan is already subscribed by some user");
            List<SubscriptionPlanModel> subscriptionPlanModelList=adminServices.viewSubscriptionPlans();
            model.addAttribute("subscriptionModelList",subscriptionPlanModelList);
            return "viewSubscriptionPlanPage";
        }
        return "redirect:/viewSubscriptionPlans";
    }
    @RequestMapping("/viewSubscriptionPlans")
    public String viewSubscriptionPlans(ModelMap model){
        List<SubscriptionPlanModel> subscriptionPlanModelList=adminServices.viewSubscriptionPlans();
        model.addAttribute("subscriptionModelList",subscriptionPlanModelList);
        return "viewSubscriptionPlans";
    }
    @RequestMapping("/updateSubscriptionPlan")
    public String updateSubscriptionPlan(@RequestParam("planId") int planId,ModelMap model){
        SubscriptionPlanModel subscriptionPlan=adminServices.searchByPlanId(planId);
        String planName=adminServices.getPlanNameByPlanId(planId);
        model.addAttribute("subscriptionPlan",subscriptionPlan);
        model.addAttribute("planName",planName);
        return "updatingSubscriptionPlan";
    }
    @RequestMapping("/updatedSubscriptionPlans")
    public String updatedSubscriptionPlans(@Valid @ModelAttribute("subscriptionPlan") SubscriptionPlanModel subscriptionPlanModel,
                                           @RequestParam("planId") int planId,
                                           BindingResult bindingResult,
                                           ModelMap model) {
        subscriptionPlanValidations.validate(subscriptionPlanModel, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            String planName = adminServices.getPlanNameByPlanId(planId);
            model.addAttribute("subscriptionPlan", subscriptionPlanModel);
            model.addAttribute("planName", planName);
            return "updatingSubscriptionPlan";
        }
        int planId1=subscriptionPlanModel.getPlanId();
        SubscriptionPlanModel subscriptionPlan1 =adminServices.createUpdatedSubscriptionPlan(subscriptionPlanModel);
        if (subscriptionPlan1 != null) {
            adminServices.updatedSubscriptionPlans(subscriptionPlan1,planId1);
            return "redirect:/viewSubscriptionPlans";
        } else {
            return "updateSubscriptionPlan";
        }
    }
}

