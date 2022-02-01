package cz.ppro.poolapp.be.authentication;
import cz.ppro.poolapp.be.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//Controller
@CrossOrigin
@RestController
public class AuthenticationController {

    private class AuthToSend {
        private Long id;
        private String roleName;
        public AuthToSend(Long id, String roleName) {
            this.id = id;
            this.roleName = roleName;
        }
        public Long getId() {
            return id;
        }
        public String getRoleName() {
            return roleName;
        }
    }

    @CrossOrigin
    @GetMapping(path = "/basicauth")
    public AuthToSend helloWorldBean() {
        //throw new RuntimeException("Ověření selhalo");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (auth.getPrincipal() == null)) {
            return null;
        }
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return  new AuthToSend(user.getId(), user.getRoleString());
    }
}
