package app.confectionery.authorization.service;

import app.confectionery.authorization.model.request.AuthenticationRequest;
import app.confectionery.authorization.model.request.RegisterRequest;
import app.confectionery.authorization.model.response.AuthenticationResponse;

public interface AuthorizationService {

    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
