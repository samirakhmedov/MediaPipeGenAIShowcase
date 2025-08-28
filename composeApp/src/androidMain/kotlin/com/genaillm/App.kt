package com.genaillm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val mainModel = koinInject<LlmViewModel>()

    MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val state by mainModel.state.collectAsStateWithLifecycle()

                var value by remember { mutableStateOf(TextFieldValue("")) }

                when (state) {
                    is LlmState.Error, is LlmState.Initial, is LlmState.Loading -> Init(state, onPrepare = { mainModel.initialize() })
                    is LlmState.Ready, is LlmState.Answer -> Input(
                        header = if (state is LlmState.Answer) (state as LlmState.Answer).answer else "Ready!",
                        state = value,
                        onValueChanged = { it ->
                            value = it
                        },
                        onTap = {
                            mainModel.infer(value.text)
                        }
                    )
                }
            }
    }
}

@Composable
fun Input(header: String, state: TextFieldValue, onValueChanged: (TextFieldValue) -> Unit, onTap: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
    ) {
        Text(header, modifier = Modifier.padding(bottom = 12.dp))

        TextField(
            value = state,
            onValueChange = onValueChanged,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Button(
            onClick = onTap,
        ) {
            Text("letsgo")
        }
    }
}

@Composable
fun Init(state: LlmState, onPrepare: () -> Unit) {

    Column {
        when (state) {
            is LlmState.Error -> Text("oops")
            is LlmState.Initial -> Text("start preparing!")
            is LlmState.Loading -> Text("warming up!")
            is LlmState.Answer -> TODO()
            is LlmState.Ready -> TODO()
        }

        Button(onClick = onPrepare) {
            Text("Prepare thyself!")
        }
    }
}
