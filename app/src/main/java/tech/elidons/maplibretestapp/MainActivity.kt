package tech.elidons.maplibretestapp

import android.content.res.AssetManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdate
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    var mapView: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (MapLibre.hasInstance().not()) {
            MapLibre.getInstance(
                applicationContext,
                "<api_key>",
                WellKnownTileServer.MapLibre
            )
        }
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(null)
        val showMapButton = findViewById<Button>(R.id.showMapButton)
        showMapButton.setOnClickListener {
            showMap()
        }
    }

    fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

    private fun showMap() {
        mapView?.getMapAsync { mapboxMap ->
            Log.v(TAG, "mapbox received")
            val styleJson = applicationContext.assets.readAssetsFile("maplibre.json") ?: ""
            Log.v(TAG, "Trying to load style: $styleJson for mapbox: $mapboxMap")
            mapboxMap.setStyle(Style.Builder().fromJson(styleJson)) { style ->
                Log.v(TAG, "Style is loaded")
            }

        }
    }
}