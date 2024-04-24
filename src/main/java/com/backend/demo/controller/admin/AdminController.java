package com.backend.demo.controller.admin;

import com.backend.demo.dto.admin.AdminSignupRequestBody;
import com.backend.demo.dto.property.*;
import com.backend.demo.dto.user.chillowUser.ChillowUserDto;
import com.backend.demo.service.admin.AdminService;
import com.backend.demo.service.property.PropertyService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chillow-users/api/v1/admin/")
public class AdminController {

    @Autowired
    PropertyService propertyService;
    @Autowired
    AdminService adminService;

    @PostMapping(value = "admin-signup")
    public ChillowUserDto adminSignup(@RequestBody AdminSignupRequestBody adminSignupRequestBody){
        return adminService.createUser(adminSignupRequestBody);
    }

    @PostMapping(value = "add-property-type")
    public ResponseEntity<?> addPropertyPartnerCategory(@RequestBody PropertyPartnerCategoryRequestBody propertyPartnerCategoryRequestBody) {
        return new ResponseEntity<>(propertyService.addPropertyPartnerCategory(propertyPartnerCategoryRequestBody), HttpStatus.OK);
    }

    @PutMapping(value = "update-property-type")
    public ResponseEntity<?> updatePropertyPartnerCategory(@RequestBody PropertyPartnerCategoryRequestBody propertyPartnerCategoryRequestBody) throws NotFoundException {
        return new ResponseEntity<>(propertyService.updatePropertyPartnerCategory(propertyPartnerCategoryRequestBody), HttpStatus.OK);
    }

    @GetMapping(value = "get-property-types")
    public ResponseEntity<?> getAllPropertyPartnerCategories(){
        return new ResponseEntity<>(propertyService.getAllPropertyPartnerCategories(),HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-property-type")
    public Boolean deletePropertyPartnerCategory(@RequestParam String id) throws NotFoundException {
        return propertyService.deleteCategory(id);
    }

    //ADD LISTING PROPERTY
    @PostMapping(value = "add-property-listing")
    public ResponseEntity<?> addProperty(@RequestBody AddListedPropertyRequestBody addListedPropertyRequestBody) {
        AddListedPropertyResponseBody addListedPropertyResponseBody = propertyService.addProperty(addListedPropertyRequestBody);
        if (addListedPropertyResponseBody != null) {
            return new ResponseEntity<>(addListedPropertyResponseBody, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid user id", HttpStatus.FORBIDDEN);
        }
    }
    //REST OF LISTING PROPERTY APIS

    //ADD PROPERTY-PARTNER APIS

    @PostMapping(value = "add-property-partner")
    public ResponseEntity<?> addPropertyPartner(@RequestBody PropertyPartnerRequestBody propertyPartnerRequestBody){
        return new ResponseEntity<>(propertyService.addPropertyPartner(propertyPartnerRequestBody),HttpStatus.OK);
    }

    //ADD PROPERTY-PARTNER APIS

}
