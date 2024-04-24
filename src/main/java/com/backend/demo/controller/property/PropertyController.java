package com.backend.demo.controller.property;

import com.backend.demo.dto.entrata.admin.SendLeadRequestBody;
import com.backend.demo.dto.property.*;
import com.backend.demo.service.entrata.EntrataService;
import com.backend.demo.service.property.PropertyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chillow-property/api/v1/property/")
public class PropertyController {

    @Autowired
    PropertyService propertyService;
    @Autowired
    EntrataService entrataService;

    @GetMapping(value = "get-suggestions")
    public List<SuggestionsResponseBody>
    getPropertyPartnerSuggestionsForUser(@RequestParam String complexName,
                                         @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
        return propertyService.getPropertyPartnerSuggestionsForUsers(complexName, limit);
    }

    @GetMapping(value = "get-suggestions-admin")
    public List<SuggestionsResponseBody>
    getPropertyPartnerSuggestionsForAdmin(@RequestParam String complexName,
                                          @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
        return propertyService.getPropertyPartnerSuggestionsForAdmin(complexName, limit);
    }

    @GetMapping(value = "get-property-listings")
    public ResponseEntity<?> getPropertyListing(@RequestParam String userId,
                                                @RequestParam(defaultValue = "0",required = false) Integer page,
                                                @RequestParam(defaultValue = "100000", required = false) Integer limit,
                                                @RequestParam(required = false) String valueToSearch) {
        return new ResponseEntity<>(propertyService.getPropertyListing(userId, page, limit, valueToSearch), HttpStatus.OK);
    }

    @GetMapping(value = "get-featured-partners-for-admin")
    public ResponseEntity<?> getFeaturedPartners() {
        return new ResponseEntity<>(propertyService.getFeaturedPartnersForAdmin(), HttpStatus.OK);
    }

    @GetMapping(value = "get-featured-listings")
    public ResponseEntity<?> getFeaturedPartners(@RequestParam String propertyPartnerId, @RequestParam Double longitude, @RequestParam Double latitude) {
        return new ResponseEntity<>(propertyService.getFeaturedPartners(propertyPartnerId, longitude, latitude), HttpStatus.OK);
    }
    @GetMapping(value = "get-featured-listings-without-radius")
    public ResponseEntity<?> getFeaturedPartners(@RequestParam String propertyPartnerId) {
        return new ResponseEntity<>(propertyService.getFeaturedPartnerListingsWithoutRadius(propertyPartnerId), HttpStatus.OK);
    }

    @GetMapping(value = "get-property-partner-info")
    public ResponseEntity<?> getPropertyPartnerInfo(@RequestParam String propertyPartnerId) {
        return new ResponseEntity<>(propertyService.getPropertyPartnerData(propertyPartnerId), HttpStatus.OK);
    }

    @PostMapping(value = "add-property")
    public ResponseEntity<?> addProperty(@RequestBody AddListedPropertyRequestBody addListedPropertyRequestBody) {
        AddListedPropertyResponseBody addListedPropertyResponseBody = propertyService.addProperty(addListedPropertyRequestBody);
        if (addListedPropertyResponseBody != null) {
            return new ResponseEntity<>(addListedPropertyResponseBody, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid user id", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "update-property")
    public ResponseEntity<?> updateProperty(@RequestBody UpdateListedPropertyRequestBody updateListedPropertyRequestBody) {
        ListedPropertyResponseBody isSuccessful = propertyService.updateProperty(updateListedPropertyRequestBody);
        if (isSuccessful != null) {
            return new ResponseEntity<>(isSuccessful, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid listing id", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value = "delete-property")
    public Boolean deleteProperty(@RequestParam String id) {
        return propertyService.deleteProperty(id);
    }


    @GetMapping(value = "get-partnered-properties")
    public ResponseEntity<?> getPartneredPropertiesAccordingUsingLongitudeAndLatitude(@RequestParam Double longitude, @RequestParam Double latitude) {
        PropertyPartnerWithCategories propertyPartnerWithCategories = propertyService.getPartneredPropertiesAccordingUsingLongitudeAndLatitude(longitude, latitude);
        return new ResponseEntity<>(propertyPartnerWithCategories, HttpStatus.OK);
    }

    @GetMapping(value = "get-all-property-listings")
    public GetPropertiesWithPaginationDto getAllPropertyListings(@RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "10", required = false) int size,
                                                                 @RequestParam(defaultValue = "100", required = false) int radius,
                                                                 @RequestParam(defaultValue = "0.0", required = false) Double longitude,
                                                                 @RequestParam(defaultValue = "0.0", required = false) Double latitude,
                                                                 @RequestParam(required = true) String userId,
                                                                 @RequestParam(defaultValue = "100", required = false) Integer minRent,
                                                                 @RequestParam(defaultValue = "10000", required = false) Integer maxRent,
                                                                 @RequestParam(required = false) Boolean isColiving,
                                                                 @RequestParam(required = false) Boolean isStudentHousing,
                                                                 @RequestParam(defaultValue = "10", required = false) Integer room,
                                                                 @RequestParam(defaultValue = "10", required = false) Integer apartment,
                                                                 @RequestParam(defaultValue = "10", required = false) Integer townhouse,
                                                                 @RequestParam(defaultValue = "10", required = false) Integer house) {
        return propertyService.getAllPropertyListings(page, size, userId, radius, longitude, latitude, minRent, maxRent, isColiving, isStudentHousing, room, apartment, townhouse, house);
    }

    @GetMapping(value = "get-listed-property")
    public ResponseEntity<?> getListedProperty(@RequestParam String id) throws NotFoundException {
        ListedPropertyResponseBody listedPropertyResponseBody = propertyService.getListedPropertyById(id);
        if (listedPropertyResponseBody != null) {
            return new ResponseEntity<>(propertyService.getListedPropertyById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>("This property does not exist or has been deleted!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "property-exist-on-location")
    public Boolean checkForExistingPropertyOnLocation(@RequestParam Double longitude, @RequestParam Double latitude) {
        return propertyService.checkExistingPropertyOnLocation(longitude, latitude);
    }

    @GetMapping("entrata-health")
    private String getit() throws IOException {
        return entrataService.checkHealthStatusEntrata();
    }
    @PutMapping(value = "post-to-entrata-from-fe")
    public Boolean postToEntrata(@RequestBody SendLeadRequestBody sendLeadRequestBody) throws NotFoundException, JsonProcessingException {
        return propertyService.postToEntrataFromFrontend(sendLeadRequestBody);
    }

    @PostMapping("/create-external-partner")
    public ExternalPropertyResponse createExternalPartner(
            @RequestBody @Valid ExternalPropertyPartnerRequestBody externalPropertyPartnerDto)
    {
        return propertyService.createExternalProperty(externalPropertyPartnerDto);
    }

//    @PostMapping("/send-mail-on-mutual-interest")
//    public ResponseEntity<?> sendMailToPropertyPartnerOnUserMutualInterest(@RequestParam String userId1,String userId2)
//    {
//        return new ResponseEntity<>(propertyService.sendMail(userId1,userId2),HttpStatus.OK);
//    }
}
