# Privacy Module

Creates a REST API via [OpenAPI specification](https://swagger.io/docs/specification/about/) for the privacy policy of the app.

## How it works

This module creates an OpenAPI metadata using the *@OpenApi()* annotation.  
Also, it is created a GET handler, which retrieves *privacy.html* from **./src/main/resources/**.
