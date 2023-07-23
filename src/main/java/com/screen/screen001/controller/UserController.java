package com.screen.screen001.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.screen.screen001.dto.TrainingBrowseHistory;
import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileControllerRepository;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.TrainingBrowseHistoryRepository;
import com.screen.screen001.repository.TrainingControllsRepository;
import com.screen.screen001.services.AdminService;
import com.screen.screen001.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/screen001")
public class UserController {

    
    

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainingControllsRepository trainingRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingBrowseHistoryRepository browseHistoryRepository;

    

    @Autowired 
    private MemberProfileControllerRepository profileRepository;

    @Autowired
    private MemberProfileRepository memberProfileRepo;

    @GetMapping("/menu")
    String menu(Model model, Authentication authenticate) {
        List<TrainingTopics> topics = adminService.getAllTrainingTopics();
        List<TrainingBrowseHistory> browse = new ArrayList<>();
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        // List<TrainingBrowseHistory> history =  browseHistoryRepository.findAll();
        String role = String.valueOf(userDetails.getAuthorities());
        boolean isAdmin = false;
        if(role.equals("[ROLE_ADMIN]")){
            isAdmin = true;
        } else {
            isAdmin = false;
        }
        // if(!history.isEmpty()){
        //     history.forEach(data -> {
        //         browse.add(TrainingBrowseHistory
        //             .builder()
        //             .trainingId(data.getTrainingId())
        //             .build()
        //         );
        //     });
        //     topics.forEach(data -> {
        //         for(int i = 0; i < browse.size(); i++){
        //             if(data.getTrainingId().equals(browse.get(i).getTrainingId()) && browse.get(i).getMemberId().equals(userDetails.getUsername())){
        //                 model.addAttribute(data.getTrainingId(), true);
        //             } else {
        //             }
        //         }
        //     });
        // }
        // System.out.println(browse.size());
        // System.out.println(topics.size());
        // topics.forEach(data -> {
        //     for(int i = 0; i < browse.size(); i++){
        //         if(data.getTrainingId().equals(browse.get(i).getTrainingId())){
        //             model.addAttribute(data.getTrainingId(), true);
        //         }
        //     }
        // });

        model.addAttribute("browse", browse);
        model.addAttribute("role", isAdmin);
        model.addAttribute("listOfTopics", topics);
        return "menu";
    }

    @GetMapping("/trainingboard/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoard(Model model, HttpServletRequest request, @PathVariable("id") String id, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        Optional<TrainingBrowseHistory> browse = browseHistoryRepository.findByTrainingId(id);
        Iterable<TrainingBrowseHistory> userBrowse = browseHistoryRepository.findByMemberId(userDetails.getUsername());
        // List<TrainingBrowseHistory> historyData = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(!browse.isPresent()){
            TrainingBrowseHistory history = TrainingBrowseHistory
                .builder()
                .memberId(userDetails.getUsername())
                .trainingId(id)
                .browseDate(timestamp)
                .insertUser(userDetails.getUsername())
                .insertDate(timestamp)
                .updateUser(userDetails.getUsername())
                .updateDate(timestamp)
                .build();
            browseHistoryRepository.save(history);
        } else {
            userBrowse.forEach(data -> {
                if(data.getTrainingId().equals(id)){
                    TrainingBrowseHistory history = browse.get();
                    history.setBrowseDate(timestamp);
                    history.setUpdateDate(timestamp);
                    history.setUpdateUser(userDetails.getUsername());
                    browseHistoryRepository.save(history);
                }
            });
        }


        List<TrainingTopics> topics = adminService.getTopicsById(id);
        String startTime = String.valueOf(topics.get(0).getTrainingStartTime());
        String endTime = String.valueOf(topics.get(0).getTrainingEndTime());
        
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("topic", topics);

        return "trainingBoard";
    }

    @GetMapping("/trainingboard/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoardEdit(Model model, @PathVariable("id") String id) {

        List<TrainingTopics> topic = adminService.getTopicsById(id);
        TrainingTopics training = trainingRepository.findByTrainingId(id).get();

        

        
        model.addAttribute("trainingObj", training);
        model.addAttribute("trainingTopic", topic);
        model.addAttribute("trainingId", id);

        return "trainingBoardEdit";
    }

    @GetMapping("/memberlist")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberList(Model model, HttpServletRequest request) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "memberList";
    }

    @GetMapping("/memberlist/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberListId(Model model) {
        return "memberListId";
    }

    @GetMapping("/myprofile")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String myprofile(Model model, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        List<MemberProfile> profile = userService.getProfile(userDetails.getUsername());
        model.addAttribute("address", profile.get(0).getAddress1() + profile.get(0).getAddress2());
        model.addAttribute("profile", profile);
        LocalDate birthDate = profile.get(0).getBirthDate().toLocalDate();
        LocalDate now = LocalDate.now();
        model.addAttribute("age", Period.between(birthDate, now).getYears());
        model.addAttribute("bloodType", profile.get(0).getBloodType());
        System.out.println(profile.get(0));
        return "myprofile";
    }

    @GetMapping("/myprofile/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String showEditMyprofile(Model model, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        MemberProfile member = profileRepository.findByMemberId(userDetails.getUsername()).get();

        

        model.addAttribute("profile", member);
        model.addAttribute("id", userDetails.getUsername());
        return "editMyprofile";
    }

    @PutMapping(path = "/myprofile/edit/{id}", consumes = "application/x-www-form-urlencoded")
    String editMyProfile(@PathVariable("id") String id,Authentication authenticate, Model model, MemberProfile memberProfile){
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        Optional<MemberProfile> members = profileRepository.findByMemberId(userDetails.getUsername());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(members.isPresent()){
            try{
                MemberProfile profile = members.get();
                profile.setMemberName(memberProfile.getMemberName());
                profile.setBirthDate(memberProfile.getBirthDate());
                profile.setAddress1(memberProfile.getAddress1());
                profile.setAddress2(memberProfile.getAddress2());
                profile.setHobby(memberProfile.getHobby());
                profile.setSpeciality(memberProfile.getSpeciality());
                profile.setBloodType(memberProfile.getBloodType());
                profile.setFavoriteFood(memberProfile.getFavoriteFood());
                profile.setHatedFood(memberProfile.getHatedFood());
                profile.setGoalsYear(memberProfile.getGoalsYear());
                profile.setUpdateUser(userDetails.getUsername());
                profile.setUpdateDate(timestamp);
                memberProfileRepo.save(profile);
            return "redirect:/screen001/myprofile?update-successfull=true";
            } catch (Exception e){
                return "redirect:/screen001/myprofile?update-successfull=false";
            }
        } else{
            return "redirect:/screen001/myprofile?update-successfull=false";
        }
        
    }

}
