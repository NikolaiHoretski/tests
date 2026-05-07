package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserPrivilegesDto;
import lombok.NonNull;

import java.util.UUID;

public interface PrivilegeService {

    @NonNull
    UserPrivilegesDto getUserPrivileges();

}
