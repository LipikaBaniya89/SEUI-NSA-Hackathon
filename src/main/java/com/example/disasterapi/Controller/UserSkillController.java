package com.example.disasterapi.Controller;

import com.example.disasterapi.Model.UserSkill;
import com.example.disasterapi.Service.UserSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-skills")
public class UserSkillController {

    private final UserSkillService userSkillService;

    public UserSkillController(UserSkillService userSkillService) {
        this.userSkillService = userSkillService;
    }

    @GetMapping("/{userId}")
    public List<UserSkill> getUserSkills(@PathVariable Long userId) {
        return userSkillService.getUserSkills(userId);
    }

    @PostMapping
    public UserSkill addSkill(@RequestBody UserSkill userSkill) {
        return userSkillService.addSkill(userSkill);
    }
}
