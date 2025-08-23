package com.example.disasterapi.Service;

import com.example.disasterapi.Model.UserSkill;
import com.example.disasterapi.Repository.UserSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSkillService {

        private final UserSkillRepository userSkillRepository;

        public UserSkillService(UserSkillRepository userSkillRepository) {
            this.userSkillRepository = userSkillRepository;
        }
        public List<UserSkill> getUserSkills(Long userId) {
            return userSkillRepository.findByIdUserId(userId);
        }

        public UserSkill addSkill(UserSkill userSkill) {
            return userSkillRepository.save(userSkill);
        }
}
