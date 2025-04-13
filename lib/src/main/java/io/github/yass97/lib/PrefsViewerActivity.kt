package io.github.yass97.lib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

internal class PrefsViewerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val prefsName = intent.getStringExtra(KEY_PREFS_NAME)
        requireNotNull(prefsName) { "prefsName is null" }

        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val entries = prefs.all.entries.map {
            PrefEntry(it.key, it.value.toString())
        }

        setContent {
            PrefsViewerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(prefsName) },
                            navigationIcon = {
                                IconButton(onClick = ::finish) {
                                    Icon(
                                        Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(paddingValues)
                            .padding(horizontal = 12.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))
                        if (entries.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$prefsName is empty",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        entries.forEach { entry ->
                            CopyableText(
                                text = entry.key,
                                style = MaterialTheme.typography.titleMedium,
                                weight = FontWeight.Bold,
                                decoration = TextDecoration.Underline
                            )
                            TextBackground(text = entry.value)
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }

                BackHandler { finish() }
            }
        }
    }

    companion object {
        private const val KEY_PREFS_NAME = "key_prefs_name"

        fun createIntent(
            context: Context,
            prefsName: String
        ): Intent = Intent(context, PrefsViewerActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(KEY_PREFS_NAME, prefsName)
    }
}

@Composable
private fun CopyableText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    weight: FontWeight = FontWeight.Normal,
    decoration: TextDecoration = TextDecoration.None
) {
    val manager = LocalClipboardManager.current
    val context = LocalContext.current

    Text(
        modifier = modifier
            .clickable {
                manager.setText(AnnotatedString(text))
                Toast.makeText(context, R.string.toast_message_text_copy, Toast.LENGTH_SHORT).show()
            }
            .padding(vertical = 8.dp, horizontal = 10.dp),
        text = text,
        style = style,
        fontWeight = weight,
        textDecoration = decoration
    )
}

@Composable
private fun TextBackground(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        CopyableText(
            modifier = Modifier.fillMaxSize(),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private data class PrefEntry(
    val key: String,
    val value: String
)
