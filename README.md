# WebClient Builder Spring Boot Starter #

The aim of this Spring Boot Starter is to allow for a uniformly defined way to initialize multiple WebClient instances. 

## Example Configuration ## 

The top-level Property definition is `webclient.builder`. Each WebClient is defined as a list under this property.
```yaml
webclient.builder:
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
_TODO_  
Sometimes, there might be several WebClients that need a set of default settings, which can be defined by 
```yaml
webclient.builder:
  defaults:
    connectionTimeout: 500
    ...
    proxy:
      ...
```
Note: OAuth settings are typically not something that can be defaulted, but proxy settings might be required by all clients. Any setting defined in a specific configuration will be utilized over the defaults. 
