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
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.FillExtrusionLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.Layer
import org.maplibre.android.style.layers.SymbolLayer

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    var mapView: MapView? = null
    private val testCoordinates = LatLng(41.06221284003195, 28.993892065742415)
    private val initialZoomLevel = 13.5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (MapLibre.hasInstance().not()) {
            MapLibre.getInstance(
                applicationContext,
                "<add-api-key>",
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
        val filterPikaButton = findViewById<Button>(R.id.filterPika)
        filterPikaButton.setOnClickListener {
            mapView?.getMapAsync { mapboxMap ->
                mapboxMap.style?.layers?.filter { it.isTestLayer() }
                    ?.forEach { layer ->
                        layer.updateLayer(3)
                    }
            }
        }
        val filterBehlulButton = findViewById<Button>(R.id.filterBehlul)
        filterBehlulButton.setOnClickListener {
            mapView?.getMapAsync { mapboxMap ->
                mapboxMap.style?.layers?.filter { it.isTestLayer() }
                    ?.forEach { layer ->
                        layer.updateLayer(4)
                    }
            }
        }
        val removeFilterButton = findViewById<Button>(R.id.removeFilters)
        removeFilterButton.setOnClickListener {
            mapView?.getMapAsync { mapboxMap ->
                mapboxMap.style?.layers?.filter { it.isTestLayer() }
                    ?.forEach { layer ->
                        layer.removeFilters()
                    }
            }
        }
    }

    fun Layer.isTestLayer(): Boolean {
        return id == "countries-fill-copy" || id == "countries-label-copy"
    }

    fun Layer.removeFilters() {
        when (this) {
            is FillExtrusionLayer -> this.setFilter(Expression.all())
            is FillLayer -> this.setFilter(Expression.all())
            is SymbolLayer -> this.setFilter(Expression.all())
            else -> Unit
        }
    }

    fun Layer.updateLayer(level: Int) {
        val expressions = mutableListOf<Expression>()
        expressions.add(
            Expression.eq(
                Expression.get("lvl"),
                Expression.literal(level)
            )
        )
        when (this) {
            is FillLayer -> {
                setFilter(Expression.all(*expressions.toTypedArray()))
                Log.v("elican", "filters set: $filter")
            }

            is SymbolLayer -> {
                setFilter(Expression.all(*expressions.toTypedArray()))
                Log.v("elican", "filters set: $filter")
            }

            is FillExtrusionLayer -> {
                setFilter(Expression.all(*expressions.toTypedArray()))
                Log.v("elican", "filters set: $filter")
            }

            else -> {
                Log.v("elican", "could not set filters")
            }
        }
    }

    fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }

    private fun showMap() {
        mapView?.getMapAsync { mapLibreInternal ->
            Log.v(TAG, "mapLibreMap received")
            val styleJson = applicationContext.assets.readAssetsFile("maplibre-test-tiles.json") ?: ""
            Log.v(TAG, "Trying to load style: $styleJson for mapbox: $mapLibreInternal")
            mapLibreInternal.setStyle(Style.Builder().fromJson(styleJson)) { style ->
                Handler(mainLooper).postDelayed({
                    val cameraUpdate = object : CameraUpdate {
                        override fun getCameraPosition(maplibreMap: MapLibreMap): CameraPosition {
                            val builder = CameraPosition.Builder()
                            builder.target(testCoordinates)
                            builder.bearing(0.0)
                            builder.tilt(0.0)
                            builder.zoom(initialZoomLevel)
                            return builder.build()
                        }
                    }

                    mapLibreInternal.animateCamera(cameraUpdate, object : MapLibreMap.CancelableCallback {
                        override fun onCancel() {
                            Log.v(TAG, "Initial focus was cancelled")
                        }

                        override fun onFinish() {
                            Log.v(TAG, "Initial focus finished.")
                        }
                    })
                }, 200L)
            }

        }
    }
}