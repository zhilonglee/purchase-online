package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.config.CustomizeConfig;
import com.zhilong.springcloud.constant.UserProviderConstant;
import com.zhilong.springcloud.entity.JsonResult;
import com.zhilong.springcloud.entity.Person;
import com.zhilong.springcloud.entity.enu.Status;
import com.zhilong.springcloud.entity.enu.TokenMoudle;
import com.zhilong.springcloud.fegin.ConfigClientFeginClient;
import com.zhilong.springcloud.service.EmailService;
import com.zhilong.springcloud.service.Oauth2Service;
import com.zhilong.springcloud.service.PersonService;
import com.zhilong.springcloud.utils.EncryptAndDecryptUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"http://localhost:8082", "null"})
@RefreshScope
@RestController
@RequestMapping("/v1/person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    CustomizeConfig customizeConfig;

    PersonService personService;

    EmailService emailService;

    Oauth2Service oauth2Service;

    @Autowired
    ConfigClientFeginClient configClientFeginClient;

    public PersonController(PersonService personService, EmailService emailService, Oauth2Service oauth2Service) {
        this.personService = personService;
        this.emailService = emailService;
        this.oauth2Service = oauth2Service;
    }

    @ApiOperation(value="Get All users list", notes="Give parameter page and size if you are paging")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "size", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public HttpEntity all(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", required = false) Integer size) {
        List<Person> all = null;
        if (size == null || size == 0) {
            all = personService.findAll();
        } else {
            all = personService.findAll(PageRequest.of(page, size, Sort.by(Sort.DEFAULT_DIRECTION, "createDate"))).getContent();
        }
        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/{username}")
    public HttpEntity getPerson(@PathVariable(value = "username") String username) {
        Person person = personService.findByUsername(username);
        return ResponseEntity.ok(person);
    }


    @Deprecated
    /**
     * POST method to do creation operations
     * @param person
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public HttpEntity savePerson(Person person) {
        try {
            logger.info("Access person save method .");
            personService.save(person);
        } catch (Exception e) {
            logger.error("", e);
            if (e.getMessage().contains("constraint")) {
                return ResponseEntity.badRequest().body("Creation operation failed.");
            }
        }

        return ResponseEntity.ok(person.getName() + " has registered successfully.");
    }

    @RequestMapping(value = "/{username}", method = {RequestMethod.DELETE})
    public HttpEntity deleteByUsername(@PathVariable("username") String username) {
        String message = null;
        try {
            int rows = personService.deleteByUsername(username);
            if (rows > 0) {
                return ResponseEntity.ok(username + " has terminated successfully.");
            } else {
                return ResponseEntity.badRequest().body("No named" + username + "user.");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * PATCH method to do update operations
     *
     * @param personParam
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH})
    public HttpEntity updatePersonInfo(@PathVariable("id") Long id, Person personParam) {
        return personService.updatePersonBasicInfo(id, personParam);
    }


    /**
     * PUT method update all fields. If URI does not exist, then will create it.
     *
     * @param personparam
     * @return
     */
    @RequestMapping(value = "/{username}", method = {RequestMethod.PUT})
    public HttpEntity putPerson(@PathVariable(value = "username") String username, Person personparam) {
        ResponseEntity responseEntity = personService.updatePersonAllfileds(username, personparam);
        return responseEntity;
    }


    /**
     * Register function. username and email is unique.
     *
     * @param person
     * @return
     */
    @PostMapping(value = "/register")
    public HttpEntity register(@RequestBody Person person) {
        if (StringUtils.isBlank(person.getPassword()) || StringUtils.isBlank(person.getUsername()) || StringUtils.isBlank(person.getEmail())) {
            return ResponseEntity.badRequest().body("Wrong Parameters");
        }
        ResponseEntity responseEntity = personService.register(person);
        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            String token = personService.createToken(person.getUsername(), TokenMoudle.ACTIVATEUSER);
            Map map = new HashMap<String, Object>();
            map.put("name", person.getUsername());
            map.put("message", "Congratulations, registration is successful!");
            map.put("href", UserProviderConstant.FRONTEND_URL + "active.html?token=" + token);
            map.put("description", "Do not share this registration confirmation letter with anyone else.");
            map.put("createdDate", person.getCreateDate());
            map.put("status", Status.NEW.toString());
            emailService.sendMessageMail("autoreply@puchaseonline.com", person.getEmail(), "Activate after registration", "activateAfterRegisterTemplate", map);
        }
        return responseEntity;
    }

    @PostMapping("/active/{token}")
    public HttpEntity active(@PathVariable(value = "token") String token) {
        return personService.active(token, TokenMoudle.ACTIVATEUSER);
    }


    @PostMapping("/resetpassword/{username}")
    public HttpEntity resetPassword(@PathVariable(value = "username") String username, @RequestBody Person person) {
        Person personInfo = personService.findByUsername(username);
        if (personInfo == null) return ResponseEntity.ok(JsonResult.badRequest("Sorry, none uses this username."));
        if (personInfo.getEmail().equals(person.getEmail())) {
            String token = personService.createToken(person.getUsername(), TokenMoudle.RESETPASSWORD);
            Map map = new HashMap<String, Object>();
            map.put("name", person.getUsername());
            map.put("message", "Congratulations, reset password request is successful!");
            map.put("href", UserProviderConstant.FRONTEND_URL + "newpassword.html?token=" + token);
            map.put("description", "Do not share this letter with anyone else.");
            map.put("createdDate", person.getCreateDate());
            map.put("status", Status.ACTIVE.toString());
            emailService.sendMessageMail("autoreply@puchaseonline.com", person.getEmail(), "Reset Your Password", "activateAfterRegisterTemplate", map);
            return ResponseEntity.ok(JsonResult.ok("The Reset Password Email has been sent."));
        } else {
            return ResponseEntity.ok(JsonResult.badRequest("Sorry, The email not matching this username."));
        }
    }

    @PostMapping("/newpassword/{token}")
    public HttpEntity newPassword(@PathVariable(value = "token") String token, @RequestBody Person person) {
        ResponseEntity<JsonResult> responseEntity = personService.active(token, TokenMoudle.RESETPASSWORD);
        if (responseEntity.getBody().getStatusCode() == 200) {
            if (StringUtils.isNotBlank(person.getPassword())) {
                personService.resetPasswordByToken(person.getPassword(), token);
            } else {
                return ResponseEntity.ok(JsonResult.badRequest("password is empty."));
            }
        }
        return responseEntity;
    }

    @GetMapping(value = "/customizeconfig")
    public HttpEntity customizeconfig() {
        return ResponseEntity.ok(customizeConfig.toString());
    }

    @GetMapping(value = "/encrypt/{password}")
    public HttpEntity encrypt(@PathVariable(value = "password") String password) {
        return ResponseEntity.ok(EncryptAndDecryptUtils.md5DigestEncryptAsHex(password));
    }

    /**
     * Get the oauth2 token which can grant the authority to call the protected URL from API-GATEWAY
     * @param map
     * @return
     */
    @PostMapping("/oauth/token")
    public ResponseEntity getToken(@RequestBody Map<String,String> map) {
        return oauth2Service.getOauth2TokenViaApiGateWay(map);
    }

    @PostMapping("/oauth/token/extend/{username}")
    public JsonResult extendToken(@PathVariable("username") String username) {
        return oauth2Service.extendToken(username);
    }

    @GetMapping("/profile")
    public JsonResult getProfile(){
        String profile = configClientFeginClient.getProfile();
        return JsonResult.ok(profile);
    }

}
