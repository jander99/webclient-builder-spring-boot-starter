# WebClient Builder Spring Boot Starter #

The aim of this Spring Boot Starter is to allow for a uniformly defined way to initialize multiple WebClient instances. 

## Example Configuration ## 

The top-level Property definition is `webclients`. Each WebClient is defined as a list under this property.
```yaml
webclients:
- name: TestWebClient
  baseUrl: http://localhost:8080/
  connectionTimeout: 1000
  oauth:
    clientId: test1
    clientSecret: testsecret1
    grantType: client_credentials
    scope: test1
    tokenUrl: http://localhost:8080/oauth/token
  proxy:
    host: some.proxy.host.com
    port: 9090
```

