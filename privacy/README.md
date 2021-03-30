# Privacy Module

Creates a documentation via [OpenAPI specification](https://rapidapi.com/blog/api-glossary/openapi/) for the privacy policy of the app.  
This policy is mandatory although no data from the user is stored on the app.  
Privacy Policy was provided by [App Privacy Policy Generator](https://app-privacy-policy-generator.firebaseapp.com/). 

## How it works

This module creates OpenAPI metadata using the *@OpenApi()* annotation.  
Also, it is created a GET handler, which retrieves *privacy.html* from **./src/main/resources/**.
