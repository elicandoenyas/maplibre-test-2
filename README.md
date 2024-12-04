This project is to demonstrate a specific crash when the style(can be found in assets/maplibre.json) is used with the version 11.5.1 or 11.6.1 of MapLibre.
It is expected to crash upon clicking the 'Show Map' button. This project is intended for debugging, it is not a production project in any sense. 

In order to run the app successfully, you need to replace the line on MainActivity.kt where MapLibre is initialized to input your own apikey.

```kotlin
onCreate(savedInstanceState: Bundle?) {
       ....
       MapLibre.getInstance(
                applicationContext,
                "<api_key>",
                WellKnownTileServer.MapLibre
            )
          
