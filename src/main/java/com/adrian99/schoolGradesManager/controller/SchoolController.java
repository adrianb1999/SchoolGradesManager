package com.adrian99.schoolGradesManager.controller;

import com.adrian99.schoolGradesManager.model.School;
import com.adrian99.schoolGradesManager.service.SchoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("api/admin/school")
    public School getSchoolInfo(){
        return schoolService.findById(1L);
    }

    @PostMapping("api/admin/schoolSettings")
    public School postSchoolSetting(@RequestBody School school){
        return schoolService.save(school);
    }

}
