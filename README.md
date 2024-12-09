This project is to demonstrate issues with the version 11.5.1 or 11.6.1 of MapLibre. It is intended for debugging, it is not a production project in any sense.
 
- When maplibre.json from assets is used, it is expected to crash upon clicking the 'Show Map' button. This is for reproducing the issue here: https://github.com/maplibre/maplibre-native/issues/3042
- On branch "bugfix/Issue-3039-reproduction", when "maplibre-test-tiles.json" is used, upon clicking on filter buttons, it is expected that the layer filtering is only applied after zoom level changes, or another filter is applied. This is for reproducing the issue: https://github.com/maplibre/maplibre-native/issues/3039


In order to run the app successfully, you need to replace the line on MainActivity.kt where MapLibre is initialized to input your own apikey.

```kotlin
onCreate(savedInstanceState: Bundle?) {
       ....
       MapLibre.getInstance(
                applicationContext,
                "<api_key>",
                WellKnownTileServer.MapLibre
            )
          
