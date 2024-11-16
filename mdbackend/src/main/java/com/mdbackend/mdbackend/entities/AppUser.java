package com.mdbackend.mdbackend.entities;

import java.util.Set;

public interface AppUser {
    String getUserName();

    String getPassword();

    Set<String> getRoles();

    boolean getDelFlg();
    boolean getUserActive();

}
