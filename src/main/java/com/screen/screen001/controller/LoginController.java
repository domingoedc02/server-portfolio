package com.screen.screen001.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.screen.screen001.dto.ChangePassword;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.UserControllerRepository;
import com.screen.screen001.repository.UserRepository;
import com.screen.screen001.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/screen001")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserControllerRepository userControlsRepo;
    @Autowired
    private UserService userService;
    
    

    @GetMapping("/login")
    String login(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String redirectURL = "";
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "/login";
        } else{
            String role = String.valueOf(authentication.getAuthorities().toArray()[0]);
            if(role.equals("ROLE_ADMIN")){
                redirectURL = "/adminmenu";
            } else if (role.equals("ROLE_USER")){ 
                redirectURL = "/menu";
            }
        }
        return "redirect:/screen001"+redirectURL;
    }

    @GetMapping("/changepassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String changePassword(Model model, Authentication authenticate) {
        
        
        List<ChangePassword> change = new ArrayList<ChangePassword>();
        change.add(new ChangePassword());
        ChangePassword changePass = change.get(0);
        model.addAttribute("changePassObj", changePass);

        
        return "changePassword";
    }

    @PutMapping(path = "/form/changepassword", consumes = "application/x-www-form-urlencoded")
    String updatePassword(Model model, Authentication authenticate, User user, ChangePassword passwordData){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        Optional<User> users = userRepository.findByMemberId(userDetails.getUsername());
        if(passwordEncoder.matches(passwordData.getPassword(), userDetails.getPassword())){
            if(passwordData.getNewPassword().equals(passwordData.getConfirmPassword())){
                if(users.isPresent()){
                    User updatePassword = users.get();
                    updatePassword.setPassword(passwordEncoder.encode(passwordData.getNewPassword()));
                    updatePassword.setUpdateMember(userDetails.getUsername());
                    updatePassword.setUpdateDate(timestamp);
                    userRepository.save(updatePassword);
                    userControlsRepo.save(updatePassword);
                    return "redirect:/screen001/menu?changedPassword=true";
                } else{
                    return "redirect:/screen001/changepassword?changedPassword=false";
                }
            } else{
                return "redirect:/screen001/changepassword?changedPassword=false";
            }
        } else{
            return "redirect:/screen001/changepassword?changedPassword=false";
        }

        
    }

    @GetMapping("/forgotpassword")
    String forgotPassword(Model model, User user) {
        model.addAttribute("user", user);
        return "forgotPassword";
    }

    @PostMapping(path = "/forgotpassword")
    String validForgotPassword(Model model, User user, final HttpServletRequest request){
        Optional<User> users = userRepository.findByMemberId(user.getMemberId());
        String url = request.getRequestURL().toString().replace("forgotpassword", "resetpassword");
        
        if(users.isPresent()){
            if(users.get().getEmail().equals(user.getEmail())){
                userService.forgotPassword(user.getMemberId(), user.getEmail(), url);
                return "redirect:/screen001/forgotpassword?confirm";
            }
        }

        return "redirect:/screen001/forgotpassword?true";
    }

    @GetMapping("/resetpassword")
    String resetPassword(Model model, @RequestParam("token") String token, User users, ChangePassword changePassword) {
        Base64 decoder = new Base64(true);
        // String payload = new String(decoder.decode(token));
        String[] tokens = token.split("\\.");
        System.out.println(tokens[1]);
        String body = new String(decoder.decode(tokens[1]));
        String temp1 = body.replaceAll("\"", "");
        String temp2 = temp1.replace("{", "");
        String temp3 = temp2.replace("}", "");
        String[] user = temp3.split(",");
        
        model.addAttribute("memberId", user[0].split(":")[1]);
        model.addAttribute("email", user[1].split(":")[1]);
        model.addAttribute("userObj", changePassword);
        
        return "resetPassword";
    }

    @PutMapping(path = "/resetpassword" )
    String updateResetPassword(Model model, User user, ChangePassword changePassword, @RequestParam("memberId") String memberId, @RequestParam("email") String email){
        Optional<User> data = userRepository.findByMemberId(memberId);

        if(data.isPresent()){
            if(changePassword.getNewPassword().equals(changePassword.getConfirmPassword())){
                User updatePassword = data.get();
                updatePassword.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                userRepository.save(updatePassword);
                userControlsRepo.save(updatePassword);
                return "redirect:/screen001/login?reset-successfull";
            }
        }
        return "";
    }

    

}
