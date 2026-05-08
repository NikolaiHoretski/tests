package com.nikolaihoretski.tests.service;

import com.nikolaihoretski.tests.dto.UserPrivilegesDto;
import lombok.NonNull;

public interface PrivilegeService {

    @NonNull
    UserPrivilegesDto getUserPrivileges();

}
