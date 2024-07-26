package com.pelagohealth.codingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pelagohealth.codingchallenge.presentation.MainViewModel
import com.pelagohealth.codingchallenge.ui.theme.PelagoCodingChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PelagoCodingChallengeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.label_random_fact),
            style = MaterialTheme.typography.labelLarge,
        )
        state.latestFact?.let {
            TextCard(
                text = it,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            )
        }
        if (state.previousFacts.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 12.dp),
                text = stringResource(R.string.label_history),
                style = MaterialTheme.typography.labelLarge,
            )
            LazyColumn(reverseLayout = true) {
                items(
                    items = state.previousFacts,
                    key = { it },
                ) {
                    SwipeToDismissItem(
                        modifier = Modifier.animateItemPlacement(),
                        text = it,
                        viewModel::onFactSwiped,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = viewModel::onMoreFactsClicked,
        ) {
            Text(text = stringResource(R.string.button_text))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    modifier: Modifier,
    text: String,
    onSwipe: (String) -> Unit,
) {
    SwipeToDismiss(
        modifier = modifier,
        state = rememberDismissState(
            confirmValueChange = { onSwipe(text);true }
        ),
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {},
        dismissContent = {
            TextCard(
                text = text,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        },
    )
}

@Composable
fun TextCard(
    text: String,
    backgroundColor: Color,
) {
    Card(
        modifier = Modifier.padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PelagoCodingChallengeTheme {
        MainScreen()
    }
}