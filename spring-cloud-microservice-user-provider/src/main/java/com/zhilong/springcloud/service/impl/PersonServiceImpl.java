package com.zhilong.springcloud.service.impl;


import com.zhilong.springcloud.contonst.PurchaseOnlieGlobalConstant;
import com.zhilong.springcloud.entity.*;
import com.zhilong.springcloud.entity.bulider.PersonBuilder;
import com.zhilong.springcloud.entity.enu.AuthorityRole;
import com.zhilong.springcloud.entity.enu.Status;
import com.zhilong.springcloud.entity.enu.TokenMoudle;
import com.zhilong.springcloud.repository.*;
import com.zhilong.springcloud.service.PersonService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

@Service
public class PersonServiceImpl implements PersonService {

    private static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonTokenRepository tokenRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    @Autowired
    OauthClientDetailsRepository oauthClientDetailsRepository;

    public List<Person> findAll() {

        return repository.findAll();
    }

    public Page<Person> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    public Person findOne(Serializable id) {

        return repository.findById((Long)id).get();
    }

    public void save(Person t) {
        repository.save(t);

    }

    public void delete(Person t) {
        repository.delete(t);

    }

    public List<Person> findByAddress(String address) {
        return repository.findByAddress(address);
    }

    public Person findByNameAndAddress(String name, String address) {

        return repository.findByNameAndAddress(name, address);
    }

    public Person withNameAndAddressQuery(String name, String address) {

        return repository.withNameAndAddressQuery(name, address);

    }

    public int deleteByUsername(String username) {
        return repository.deleteByUsername(username);
    }

    public int updateAgeByName(String name, Integer age) {
        return repository.updateAgeByName(name, age);
    }

    @Transactional
    public void update(Person person) throws Exception {
        if(person.getId() == null){
            throw new Exception("update operation must contain id.");
        }
        repository.save(person);

    }

    @Override
    public Long countPersonByUsernameOrEmail(Person person) {
        if (StringUtils.isBlank(person.getUsername())) {
            return -1L;
        }
        if (StringUtils.isBlank(person.getPassword())) {
            return -1L;
        }
        if (StringUtils.isBlank(person.getEmail())) {
            return -1L;
        }
        Long count = repository.countPersonByUsernameOrEmail(person.getUsername(), person.getEmail());
        return count;
    }

    public Page<Person> findAll(final Person person, Pageable pageable) throws Exception {

        if(person == null){
            throw new Exception("Person object can not be null !");
        }
        return repository.findAll(new Specification<Person>() {

            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {

                List<Predicate> predicateList = new ArrayList<Predicate>();

                if(StringUtils.isNotEmpty(person.getName())){
                    Predicate predicateByName = cb.like(root.get("name").as(String.class), "%" + person.getName() + "%");
                    predicateList.add(predicateByName);
                }
                if( null != person.getAge()){
                    Predicate predicateByAge = cb.equal(root.get("age").as(Integer.class), person.getAge());
                    predicateList.add(predicateByAge);
                }
                if(StringUtils.isNotEmpty(person.getAddress())){
                    Predicate predicateByAddress = cb.like(root.get("address").as(String.class), "%" + person.getAddress() + "%");
                    predicateList.add(predicateByAddress);
                }
                Date birthDate = person.getBirthDay();
                if(null != birthDate){
                    Calendar calendar = DateUtils.toCalendar(birthDate);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date beginDate = calendar.getTime();

                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endDate = calendar.getTime();

                    Predicate predicateByBirthDate = cb.between(root.get("birthDay").as(Date.class), beginDate, endDate);
                    predicateList.add(predicateByBirthDate);
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                return cb.and(predicates);
            }
        } ,pageable);
    }

    public Person findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity updatePersonBasicInfo(Long id,Person personParam) {
        Person person = repository.findById(id).get();
        if (person != null) {
            person = new PersonBuilder().copy2EntityUsingNullValidation(personParam,person);
            repository.save(person);
            return ResponseEntity.ok(id + "'s age has updated successfully.");
        }else{
            return ResponseEntity.badRequest().body("No id : " + id +" user.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity updatePersonAllfileds(String username, Person personparam) {
        // The first step, finding the URI in DB
        Person person = repository.findByUsername(username);
        try {
            // The second step, if URI exist, then do update operations.
            // Otherwise, will do creation operations
            logger.info("Access person save method .");
            if (person != null) {
                person = new PersonBuilder().copy2Entity(personparam,person,"id","createDate");
                if (StringUtils.isNotBlank(person.getPassword())) person.encryptPassword();
                repository.save(person);
            } else {
                repository.save(personparam);
            }

        } catch (Exception e) {
            logger.error("",e);
            if(e.getMessage().contains("constraint")){
                return ResponseEntity.badRequest().body("Creation operation failed.");
            }
            throw e;
        }
        return ResponseEntity.ok(username+" has registered successfully.");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity register(Person person) {
        Long count = repository.countPersonByUsernameOrEmail(person.getUsername(),person.getEmail());
        if (count == -1) {
            return ResponseEntity.badRequest().body("Invalid email,username or password.Kindly check!");
        } else if(count == 0) {
            String password = "{bcrypt}"+new BCryptPasswordEncoder().encode(person.getPassword());
            person.setStatus(Status.NEW);
            person.encryptPassword();
            repository.save(person);

            // security userdetail setting
            Users users = new Users();
            users.setUsername(person.getUsername());
            users.setPassword(password);
            users.setEnabled(false);
            usersRepository.save(users);

            Authorities authorities = new Authorities();
            authorities.setAuthority(AuthorityRole.USER.name());
            EmbeddedUsers embeddedUsers = new EmbeddedUsers();
            embeddedUsers.setUsers(users);
            authorities.setEmbeddedUsers(embeddedUsers);
            authoritiesRepository.saveAndFlush(authorities);

            // oauth2 credential
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            oauthClientDetails.setClientId(person.getUsername());
            oauthClientDetails.setClientSecret(password);
            oauthClientDetails.setResourceIds(PurchaseOnlieGlobalConstant.USER_RESOURCE_ID);
            oauthClientDetails.setScope("select");
            oauthClientDetails.setAuthorizedGrantTypes("password,refresh_token");
            oauthClientDetails.setWebServerRedirectUri("");
            oauthClientDetails.setAuthorities("oauth2");
            oauthClientDetails.setAdditionalInformation("{}");
            oauthClientDetailsRepository.saveAndFlush(oauthClientDetails);
        } else {
            return ResponseEntity.badRequest().body("Duplicate email or username.");
        }
        return ResponseEntity.ok(JsonResult.ok("Register successfully!"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createToken(String username, TokenMoudle tokenMoudle) {
        Person person = repository.findByUsername(username);
        String token = UUID.randomUUID().toString().replace("-","");
        PersonToken personToken = new PersonToken(person,token,tokenMoudle);
        tokenRepository.save(personToken);
        return token;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<JsonResult> active(String token,TokenMoudle tokenMoudle) {
        PersonToken activeToken = tokenRepository.findPersonTokenByTokenAndTokenMoudle(token,tokenMoudle);
        if (activeToken.getExpiredDate().before(new Date())) {
            return ResponseEntity.ok(JsonResult.badRequest("The token is expired!"));
        } else if (activeToken.getIsUse() == true) {
            return ResponseEntity.ok(JsonResult.badRequest("The token is used!"));
        } else {
            if (activeToken.getTokenMoudle().ordinal() == TokenMoudle.ACTIVATEUSER.ordinal()) {
                activeToken.getPerson().setStatus(Status.ACTIVE);
                Users users = usersRepository.findByUsername(activeToken.getPerson().getUsername());
                users.setEnabled(true);
            }
            activeToken.setIsUse(true);
            return ResponseEntity.ok(JsonResult.ok("The token is used successfully!"));
        }

    }

    @Override
    public void resetPasswordByToken(String password,String token) {
        PersonToken resetPasswordToken = tokenRepository.findPersonTokenByTokenAndTokenMoudle(token,TokenMoudle.RESETPASSWORD);
        Person person = resetPasswordToken.getPerson();
        person.setPassword(password);
        person.encryptPassword();
        repository.save(person);

        String newPassword = "{bcrypt}"+new BCryptPasswordEncoder().encode(password);

        Users users = usersRepository.findByUsername(person.getUsername());
        users.setPassword(newPassword);
        usersRepository.save(users);

        OauthClientDetails oauthClientDetails = oauthClientDetailsRepository.findByClientId(person.getUsername());
        oauthClientDetails.setClientSecret(newPassword);
        oauthClientDetailsRepository.save(oauthClientDetails);

    }
}
