package vn.edu.fpt.util;

import org.springframework.stereotype.Component;

@Component
public class Validation {

    public boolean isValidPhone(String phone){
        if(phone == null || phone.trim().isEmpty()) return false;

        return phone.matches("^(03|05|07|08|09)[0-9]{8}$");
    }
}
