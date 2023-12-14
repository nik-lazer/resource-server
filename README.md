# Microsoft oAuth configuration
1. Create application-local.yaml in resources directory
2. Add the following lines:
```
lan:
  test:
    oauth:
      tenant: <TENANT_ID>
      client: <APP_REGISTRATION_ID>
      scopes: <SCOPES_SEPARATED_BY_SPACE>
```
