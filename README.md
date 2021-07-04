# Evenly

To use the app, you should replace the BuildConfig properties: CLIENT_ID and CLIENT_SECRET with the corresponding credentials generated from Foursquare API
To do that:
1) Open build.gradle (app)
2) inside the defaultConfig{...}, you should see:  
        ```
        buildConfigField "String", "CLIENT_ID", "\"API_KEY\""
        ```  
        ```
        buildConfigField "String", "CLIENT_SECRET", "\"CLIENT_SECRET\""
        ```
3) Replace the keyword API_KEY with your generated API_KEY from Foursquare API.
4) Replace the keyword CLIENT_SECRET with your generated CLIENT_SECRET from Foursquare API
##### Note: Any other character should not be deleted
5) Sync and rebuild
Evenly should now run properly on your device.
