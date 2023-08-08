package com.screen.screen001.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.screen.screen001.dto.TrainingBrowseHistory;
import com.screen.screen001.dto.TrainingFiles;
import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileControllerRepository;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.TrainingBrowseHistoryRepository;
import com.screen.screen001.repository.TrainingControllsRepository;
import com.screen.screen001.repository.TrainingFilesRepository;
import com.screen.screen001.repository.UserRepository;
import com.screen.screen001.services.AdminService;
import com.screen.screen001.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/screen001")
public class UserController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/TrainingFiles/";

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainingControllsRepository trainingRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingBrowseHistoryRepository browseHistoryRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired 
    private MemberProfileControllerRepository profileRepository;

    @Autowired
    private MemberProfileRepository memberProfileRepo;

    @Autowired 
    private TrainingFilesRepository filesRepository;

    @GetMapping("/menu")
    String menu(Model model, Authentication authenticate) {
        
        List<TrainingBrowseHistory> browse = new ArrayList<>();
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        List<TrainingTopics> topics = adminService.getAllTrainingTopics(userDetails.getUsername());
        List<TrainingBrowseHistory> history =  browseHistoryRepository.findByMemberId(userDetails.getUsername());
        
        String role = String.valueOf(userDetails.getAuthorities());
        boolean isAdmin = false;
        if(role.equals("[ROLE_ADMIN]")){
            isAdmin = true;
        } else {
            isAdmin = false;
        }
        
        for(int i = 0; i < topics.size(); i++){
            for(int j = 0; j < history.size(); j++){
                if(topics.get(i).getTrainingId().equals(history.get(j).getTrainingId())){
                    topics.get(i).setTrainingDetails("browsed=true");
                }
            }
        }

        if(topics.isEmpty()){
            LocalDate trainingDate = LocalDate.now();
            model.addAttribute("trainingDate", trainingDate);
        } else {
            Date trainingDate = topics.get(0).getTrainingDate();
            model.addAttribute("trainingDate", trainingDate);
        }

        

        

        model.addAttribute("browse", browse);
        model.addAttribute("role", isAdmin);
        
        model.addAttribute("listOfTopics", topics);
        return "/menu";
    }

    @GetMapping("/trainingboard/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoard(Model model, HttpServletRequest request, @PathVariable("id") String id, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        
        Optional<TrainingFiles> file = filesRepository.findByTrainingId(id);
        Optional<TrainingTopics> training = trainingRepository.findByTrainingId(id);
        // Optional<TrainingBrowseHistory> browse = browseHistoryRepository.findByTrainingId(id);
        // Iterable<TrainingBrowseHistory> userBrowse = browseHistoryRepository.findByMemberId(userDetails.getUsername());
        List<TrainingBrowseHistory> browse = browseHistoryRepository.findByMemberIdAndTrainingId(userDetails.getUsername(), id);
        Optional<User> user = userRepository.findByMemberId(training.get().getUpdateMember());
        // List<TrainingBrowseHistory> userHistory = new ArrayList<>();
        // List<TrainingBrowseHistory> historyData = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        if(file.isPresent()){
            model.addAttribute("fileName", file.get().getUploadFile());
        }

        if(browse.isEmpty()){
            TrainingBrowseHistory addBrowse = TrainingBrowseHistory
            .builder()
            .memberId(userDetails.getUsername())
            .trainingId(id)
            .browseDate(timestamp)
            .insertUser(userDetails.getUsername())
            .insertDate(timestamp)
            .updateUser(userDetails.getUsername())
            .updateDate(timestamp)
            .build();
            browseHistoryRepository.save(addBrowse);
        } else {
            TrainingBrowseHistory updateBrowse = browse.get(0);
            updateBrowse.setBrowseDate(timestamp);
            updateBrowse.setUpdateUser(userDetails.getUsername());
            updateBrowse.setUpdateDate(timestamp);
            browseHistoryRepository.save(updateBrowse);
        }


        
        String[] temp = String.valueOf(userDetails.getAuthorities().toArray()[0]).split("_");

        List<TrainingTopics> topics = adminService.getTopicsById(id);
        String startTime = String.valueOf(topics.get(0).getTrainingStartTime());
        String endTime = String.valueOf(topics.get(0).getTrainingEndTime());
        String[] updateDateTemp = String.valueOf(training.get().getUpdateDate()).split(" ");
        model.addAttribute("updateDate", updateDateTemp[0]);
        model.addAttribute("updateUser", user.get().getMemberName());
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("topic", topics);
        model.addAttribute("trainingId", id);
        model.addAttribute("isAdmin", temp[1]);

        return "/trainingBoard";
    }

    @GetMapping("/trainingboard/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoardEdit(Model model, @PathVariable("id") String id) {

        List<TrainingTopics> topic = adminService.getTopicsById(id);
        TrainingTopics training = trainingRepository.findByTrainingId(id).get();
        Optional<TrainingFiles> files = filesRepository.findByTrainingId(id);

        if(files.isPresent()){
            System.out.println(UPLOAD_DIRECTORY + "/" + files.get().getUploadFile());
            model.addAttribute("C:/Users/john/Documents/Screen Projects/PJ Project/screen001-main/TrainingFiles/"  + files.get().getUploadFile());
        } else{
            model.addAttribute("fileName", "ファイルが選択されていません");
        }
        
        model.addAttribute("trainingObj", training);
        model.addAttribute("trainingTopic", topic);
        model.addAttribute("trainingId", id);
        return "/trainingBoardEdit";
    }

    @GetMapping("/memberlist")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberList(Model model, HttpServletRequest request) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "/memberList";
    }

    @GetMapping("/memberlist/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberListId(Model model, @PathVariable("id") String id) {
        Iterable<MemberProfile> training = memberProfileRepo.findByMemberId(id);
        training.forEach(user -> {
            if(user.getBirthDate() != null){
                LocalDate birthDate = user.getBirthDate().toLocalDate();
                LocalDate now = LocalDate.now();
                model.addAttribute("age", Period.between(birthDate, now).getYears());
            } else{
                model.addAttribute("age", 0);
            }
            if(user.getAddress1() != null && user.getAddress2() != null){
                model.addAttribute("address", user.getAddress1() + user.getAddress2());
            } else {
                model.addAttribute("address", "");
            }
            
        });
        // TrainingTopics training = trainingRepository.findByTrainingId(id).get();
        model.addAttribute("profile",training);
        return "/memberListId";
    }

    @GetMapping("/myprofile")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String myprofile(Model model, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        List<MemberProfile> profile = userService.getProfile(userDetails.getUsername());
        if(profile.get(0).getBirthDate() != null){
            LocalDate birthDate = profile.get(0).getBirthDate().toLocalDate();
            LocalDate now = LocalDate.now();
            model.addAttribute("age", Period.between(birthDate, now).getYears());
        } else {
            model.addAttribute("age", 0);
        }
        if(profile.get(0).getAddress1() != null && profile.get(0).getAddress2() != null){
            model.addAttribute("address", profile.get(0).getAddress1() + profile.get(0).getAddress2());
        } else {
            model.addAttribute("address", "");
        }
        model.addAttribute("profile", profile);
        
        model.addAttribute("bloodType", profile.get(0).getBloodType());
        System.out.println(profile.get(0));
        return "/myprofile";
    }

    @GetMapping("/myprofile/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String showEditMyprofile(Model model, Authentication authenticate) {
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        MemberProfile member = profileRepository.findByMemberId(userDetails.getUsername()).get();

        model.addAttribute("profile", member);
        model.addAttribute("id", userDetails.getUsername());
        return "/editMyprofile";
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
