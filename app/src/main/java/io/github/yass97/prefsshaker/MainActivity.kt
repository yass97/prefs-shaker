package io.github.yass97.prefsshaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import java.io.File

class MainActivity : AppCompatActivity() {
    private val prefsName: String
        get() = getString(R.string.prefs_name)

    private val prefs: SharedPreferences by lazy {
        getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    private val prefsFile: File by lazy {
        File(dataDir, "shared_prefs/$prefsName.xml")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var data by remember {
                mutableStateOf<List<Pair<String, String?>>>(emptyList())
            }

            LaunchedEffect(Unit) {
                if (prefsFile.exists()) {
                    data = PREFS_KEYS.map { it to prefs.getString(it, null) }
                }
            }

            MaterialTheme(
                colorScheme = lightColorScheme(
                    surface = Color.LightGray
                )
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) }
                        )
                    }
                ) { padding ->
                    Content(
                        padding,
                        prefsFile.absolutePath,
                        data
                    ) {
                        prefs.edit {
                            PREFS_KEYS.forEachIndexed { index, key ->
                                putString(key, "sample$index")
                            }
                            apply()
                        }
                        data = PREFS_KEYS.map { it to prefs.getString(it, null) }
                    }
                }
            }
        }
    }

    companion object {
        private val PREFS_KEYS = List(10) { "key$it" }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    prefsPath: String,
    data: List<Pair<String, String?>>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(stringResource(R.string.message))
            }
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.title_file),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(prefsPath)
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.title_key_value),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                data.forEach {
                    Text(
                        "${it.first} - ${it.second}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                enabled = data.isEmpty(),
                onClick = onClick
            ) {
                Text(stringResource(R.string.button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val data = List(10) { "key${it + 1}" to "sample$it" }
    Content(PaddingValues(), "sample/shared_prefs/sample_prefs.xml", data) {

    }
}