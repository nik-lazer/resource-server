# Microsoft oAuth configuration
1. Create application-local.yaml in resources directory
2. Add the following lines:
```
lan:
  test:
    oauth:
      idp-url: https://login.microsoftonline.com/${lan.test.oauth.tenant}
      authorization-url: ${lan.test.oauth.idp-url}/oauth2/v2.0/authorize
      issuer-uri: ${lan.test.oauth.idp-url}/v2.0
      jwk-set-uri: ${lan.test.oauth.idp-url}/discovery/v2.0/keys
      tenant: <TENANT_ID>
      client: <APP_REGISTRATION_ID>
      scopes: <SCOPES_SEPARATED_BY_SPACE>
```

# Keycloak
## Materials
- https://habr.com/en/companies/axenix/articles/780422/
- https://dzone.com/articles/secure-spring-boot-application-with-keycloak

## Run keycloak
Setup:
- Dockerized keycloak
- Postgres DB as a storage

Steps:
1. Created local Postgres DB keycloakdemo
2. Fill environment passwords in the environment file for the keycloak docker installation (keycloak.env)
3. Run the docker image by command
```
docker run -p 8180:8080 --add-host=host.docker.internal:host-gateway --env-file .\keycloaknew.env quay.io/keycloak/keycloak:23.0.3 start-dev
```
4. Open http://localhost:8180/ and configure Keycloak accordingly.
5. Fill in application-local.yaml:
```
lan:
  test:
    oauth:
      idp-url: http://localhost:8180/realms/<REALM>
      authorization-url: ${lan.test.oauth.idp-url}/protocol/openid-connect/auth
      issuer-uri: ${lan.test.oauth.idp-url}
      jwk-set-uri: ${lan.test.oauth.idp-url}/protocol/openid-connect/certs
      client: <CLIENT>
      scopes: openid
```