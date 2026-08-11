package it.luca.ilmiorto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import it.luca.ilmiorto.ui.GardenApp
import it.luca.ilmiorto.ui.theme.IlMioOrtoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IlMioOrtoTheme {
                GardenApp()
            }
        }
    }
}
