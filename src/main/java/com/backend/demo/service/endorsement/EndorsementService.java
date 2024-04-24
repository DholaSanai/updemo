package com.backend.demo.service.endorsement;

import com.backend.demo.dto.endorsement.AllEndorsementResponseBody;
import com.backend.demo.dto.endorsement.EndorsementCount;
import com.backend.demo.dto.endorsement.EndorsementRequestBody;
import com.backend.demo.entity.endorsement.Endorsement;
import com.backend.demo.entity.endorsement.RoommateEndorsement;
import com.backend.demo.entity.user.ChillowUser;
import com.backend.demo.exceptions.UserNotFoundException;
import com.backend.demo.repository.endorsement.EndorsementRepository;
import com.backend.demo.repository.endorsement.RoommateEndorsementRepository;
import com.backend.demo.repository.user.UserRepository;
import com.backend.demo.service.notification.NotificationService;
import com.backend.demo.service.user.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class EndorsementService {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoommateEndorsementRepository roommateEndorsementRepository;
    @Autowired
    private EndorsementRepository endorsementRepository;
    @Autowired
    private NotificationService notificationService;

    public Boolean addEndorsement(EndorsementRequestBody endorsementRequestBody) throws IllegalAccessException {
        Optional<ChillowUser> isUserExist = userRepository.findOneByNumber(endorsementRequestBody.getNumber());
        String endorsementGivenBy;
        if (isUserExist.isPresent()) {
            endorsementGivenBy = isUserExist.get().getId();
        } else {
            ChillowUser user = userService.createAccountWithForEndorsement(endorsementRequestBody.getNumber(),
                    endorsementRequestBody.getName(), endorsementRequestBody.getEmail());
            endorsementGivenBy = user.getId();
        }
        Optional<ChillowUser> endorsementGivenTo =
                userRepository.findById(endorsementRequestBody.getEndorsementReceiverId());

        if (endorsementGivenTo.isPresent()) {
            Optional<Endorsement> isAlreadyEndorsed = endorsementRepository
                    .findByGivenByUserIdAndGivenToUserId(endorsementGivenBy, endorsementRequestBody.getEndorsementReceiverId());
            if (!isAlreadyEndorsed.isPresent()) {

            String endorsementReceiver = endorsementGivenTo.get().getId();

            String roommateEndorsementId = UUID.randomUUID().toString();
            RoommateEndorsement roommateEndorsement;
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);

            roommateEndorsement = modelMapper.map(endorsementRequestBody, RoommateEndorsement.class);
            roommateEndorsement.setId(roommateEndorsementId);
            roommateEndorsement.setCreatedAt(LocalDateTime.now());
            roommateEndorsement.setUpdatedAt(LocalDateTime.now());
            roommateEndorsement.setAccommodating(endorsementRequestBody.getAccommodating());

            roommateEndorsementRepository.save(roommateEndorsement);

            notificationService.sendNotificationOnEndorsement(endorsementRequestBody.getEndorsementReceiverId(),
                    endorsementsMapping(endorsementRequestBody));

            Endorsement endorsement = new Endorsement(UUID.randomUUID().toString(),
                    roommateEndorsementId, endorsementGivenBy, endorsementReceiver,
                    endorsementRequestBody.getIsDiscloseInformation(), false,
                    LocalDateTime.now(), LocalDateTime.now());

            endorsementRepository.save(endorsement);

        }
            else {
                Optional<RoommateEndorsement> alreadyRoommateEndorsements =
                        roommateEndorsementRepository.findById(isAlreadyEndorsed.get().getRoommateEndorsementId());
                if (alreadyRoommateEndorsements.isPresent()) {
                    RoommateEndorsement roommateEndorsement = alreadyRoommateEndorsements.get();
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
                    modelMapper.map(endorsementRequestBody, roommateEndorsement);
                    roommateEndorsement.setId(alreadyRoommateEndorsements.get().getId());
                    roommateEndorsement.setAccommodating(endorsementRequestBody.getAccommodating());
                    roommateEndorsement.setUpdatedAt(LocalDateTime.now());
                    roommateEndorsementRepository.save(roommateEndorsement);
                }
            }
            return true;
        }
        throw new UserNotFoundException("The endorsement receiver does not exist!");
    }

    private Map<String, String> endorsementsMapping(EndorsementRequestBody endorsementRequestBody) throws IllegalAccessException {

//        private int trustworthy;
//        private int accommodating;
//        private int clean;
//        private int compassionate;
//        private int financiallyResponsible;
//        private int dependable;
//        private int responsible;
//        private int considerate;
//        private int kindHearted;
//        private int organized;
//        private int nightOwl;
//        private int earlyBird;

        Map<String,String> endorsements = new HashMap<>();
        if(endorsementRequestBody.getTrustworthy()) endorsements.put("trustworthy","trustworthy");
        if(endorsementRequestBody.getAccommodating()) endorsements.put("accommodating","accommodating");
        if(endorsementRequestBody.getClean()) endorsements.put("clean","clean");
        if(endorsementRequestBody.getCompassionate()) endorsements.put("compassionate","compassionate");
        if(endorsementRequestBody.getFinanciallyResponsible()) endorsements.put("financiallyResponsible","financiallyResponsible");
        if(endorsementRequestBody.getDependable()) endorsements.put("dependable","dependable");
        if(endorsementRequestBody.getResponsible()) endorsements.put("responsible","responsible");
        if(endorsementRequestBody.getConsiderate()) endorsements.put("considerate","considerate");
        if(endorsementRequestBody.getKindHearted()) endorsements.put("kindHearted","kindHearted");
        if(endorsementRequestBody.getOrganized()) endorsements.put("organized","organized");
        if(endorsementRequestBody.getNightOwl()) endorsements.put("nightOwl","nightOwl");
        if(endorsementRequestBody.getEarlyBird()) endorsements.put("earlyBird","earlyBird");
        return endorsements;
    }

    public Object getEndorsement(String userId) {
        EndorsementCount yourEndorsements;
        EndorsementCount givenEndorsements;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STANDARD);

        List<Endorsement> receivedEndorsements = endorsementRepository.findAllByGivenToUserId(userId);
        int endorsementsReceived = 0;
        int trustworthy = 0, accommodating = 0, clean = 0, compassionate = 0,
                financiallyResponsible = 0, dependable = 0, responsible = 0, considerate = 0,
                kindHearted = 0, organized = 0, nightOwl = 0, earlyBird = 0;
        if (!receivedEndorsements.isEmpty()) {

            for (Endorsement each : receivedEndorsements) {
                Optional<RoommateEndorsement> roommateEndorsement =
                        roommateEndorsementRepository.findById(each.getRoommateEndorsementId());
                if (roommateEndorsement.isPresent()) {
                    endorsementsReceived++;
                    RoommateEndorsement endorsement = roommateEndorsement.get();
                    if (endorsement.getTrustworthy()) trustworthy++;
                    if (endorsement.getAccommodating()) accommodating++;
                    if (endorsement.getClean()) clean++;
                    if (endorsement.getCompassionate()) compassionate++;
                    if (endorsement.getFinanciallyResponsible()) financiallyResponsible++;
                    if (endorsement.getDependable()) dependable++;
                    if (endorsement.getResponsible()) responsible++;
                    if (endorsement.getConsiderate()) considerate++;
                    if (endorsement.getKindHearted()) kindHearted++;
                    if (endorsement.getOrganized()) organized++;
                    if (endorsement.getNightOwl()) nightOwl++;
                    if (endorsement.getEarlyBird()) earlyBird++;
                }
            }
        }
        yourEndorsements = new EndorsementCount(trustworthy, accommodating, clean, compassionate,
                financiallyResponsible, dependable, responsible, considerate,
                kindHearted, organized, nightOwl, earlyBird);


        receivedEndorsements = endorsementRepository.findAllByGivenByUserId(userId);
        int endorsementsGiven = 0;
        trustworthy = 0;
        accommodating = 0;
        clean = 0;
        compassionate = 0;
        financiallyResponsible = 0;
        dependable = 0;
        responsible = 0;
        considerate = 0;
        kindHearted = 0;
        organized = 0;
        nightOwl = 0;
        earlyBird = 0;
        if (!receivedEndorsements.isEmpty()) {
            for (Endorsement each : receivedEndorsements) {
                Optional<RoommateEndorsement> roommateEndorsement =
                        roommateEndorsementRepository.findById(each.getRoommateEndorsementId());
                RoommateEndorsement endorsement = roommateEndorsement.get();
                if (roommateEndorsement.isPresent()) {
                    endorsementsGiven++;
                    if (endorsement.getTrustworthy()) trustworthy++;
                    if (endorsement.getAccommodating()) accommodating++;
                    if (endorsement.getClean()) clean++;
                    if (endorsement.getCompassionate()) compassionate++;
                    if (endorsement.getFinanciallyResponsible()) financiallyResponsible++;
                    if (endorsement.getDependable()) dependable++;
                    if (endorsement.getResponsible()) responsible++;
                    if (endorsement.getConsiderate()) considerate++;
                    if (endorsement.getKindHearted()) kindHearted++;
                    if (endorsement.getOrganized()) organized++;
                    if (endorsement.getNightOwl()) nightOwl++;
                    if (endorsement.getEarlyBird()) earlyBird++;
                }
            }
        }
        givenEndorsements = new EndorsementCount(trustworthy, accommodating, clean, compassionate,
                financiallyResponsible, dependable, responsible, considerate,
                kindHearted, organized, nightOwl, earlyBird);

        return new AllEndorsementResponseBody(endorsementsReceived, endorsementsGiven, yourEndorsements, givenEndorsements);
    }
}
