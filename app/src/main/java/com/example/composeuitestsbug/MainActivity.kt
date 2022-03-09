package com.example.composeuitestsbug

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.composeuitestsbug.ui.theme.ComposeUiTestsBugTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private var fragmentView: FrameLayout? = null
  private var composeView: ComposeView? = null
  private val state: MutableStateFlow<Boolean> = MutableStateFlow(false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    composeView = findViewById(R.id.feature_compose_container)
    fragmentView = findViewById(R.id.feature_fragment_container)
    composeView?.setContent {
      setComposeContent()
    }
    findViewById<Button>(R.id.next_btn).setOnClickListener {
      composeView?.isVisible = true
      fragmentView?.isInvisible = true
      lifecycleScope.launch {
        state.emit(true)
      }
    }
  }

  @Composable
  private fun setComposeContent() {
    ComposeUiTestsBugTheme {
      val showScreenState = state.collectAsState()
      // A surface container using the 'background' color from the theme
      Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        if (showScreenState.value) {
          Column {
            Button(
              onClick = {
                state.tryEmit(false)
                fragmentView?.isVisible = true
                composeView?.isInvisible = true
              },
              modifier = Modifier.testTag(BTN_TAG)
            ) {
              Text(text = "Back")
            }
            val textState = remember { mutableStateOf("") }
            TextField(
              value = textState.value,
              onValueChange = { textState.value = it },
              modifier = Modifier.testTag(EDIT_TAG)
            )
          }
        }
      }
    }
  }

  companion object {

    const val BTN_TAG = "BTN_TAG"
    const val EDIT_TAG = "EDIT_TAG"
  }
}