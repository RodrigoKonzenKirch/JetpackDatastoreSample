package com.example.jetpackdatastoresample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import com.example.jetpackdatastoresample.data.Prefs
import com.example.jetpackdatastoresample.ui.theme.JetpackDatastoreSampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val Context.dataStore by preferencesDataStore(
        name = Prefs.PREFS_NAME
    )
    private val prefs: Prefs by lazy {
        Prefs(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackDatastoreSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppScreen(prefs)
                }
            }
        }
    }
}

@Composable
fun AppScreen(prefs: Prefs) {
    Column {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Preferences DataStore",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        PreferencesDataStoreScreen(prefs)

        Text(
            modifier = Modifier
                .padding(top = 24.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            text = "Proto DataStore",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        ProtoDataStoreScreen()
    }
}

@Composable
fun PreferencesDataStoreScreen(prefs: Prefs) {
    var wordTextField by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    val storedWord = prefs.userPreferencesWordFlow.collectAsState(initial = "")
    val counter = prefs.userPreferencesCounterFlow.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.Blue)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "Word currently stored on data store: ${storedWord.value}"
        )

        OutlinedTextField(
            value = wordTextField,
            onValueChange = { newWord ->
                wordTextField = newWord
                            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            placeholder = { Text(text = "Type a word and press the button to save") }
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    prefs.saveWord(wordTextField)
                }
                      },
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(text = "Save")
        }

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Tap + or - to increase/decrease the number"
        )
        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
            Button(
                onClick = { coroutineScope
                    .launch {
                        prefs.decrementCounter()
                    }
                }
            ) {
                Text(text = "-")
            }

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = "${counter.value}",
                fontSize = 22.sp
            )

            Button(
                onClick = { coroutineScope
                    .launch {
                        prefs.incrementCounter()
                    }
                }
            ) {
                Text(text = "+")
            }

        }
    }
}

@Composable
fun ProtoDataStoreScreen() {
    // TODO: Not implemented yet
}

