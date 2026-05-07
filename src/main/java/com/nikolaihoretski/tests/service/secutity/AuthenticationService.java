package com.nikolaihoretski.tests.service.secutity;

import com.nikolaihoretski.tests.dto.AuthResult;
import com.nikolaihoretski.tests.dto.LoginDto;
import lombok.NonNull;

public interface AuthenticationService {

    @NonNull
    AuthResult verify(@NonNull LoginDto loginDto);

    @NonNull
    AuthResult refresh(@NonNull String refreshToken);
}
