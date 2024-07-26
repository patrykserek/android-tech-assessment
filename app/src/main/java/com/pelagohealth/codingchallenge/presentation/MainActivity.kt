package com.pelagohealth.codingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pelagohealth.codingchallenge.presentation.MainViewModel
import com.pelagohealth.codingchallenge.presentation.model.Message
import com.pelagohealth.codingchallenge.presentation.ui.thenIf
import com.pelagohealth.codingchallenge.ui.theme.PelagoCodingChallengeTheme
import com.valentinilk.shimmer.shimmer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PelagoCodingChallengeTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { insetPadding ->
                    MainScreen(
                        modifier = Modifier.padding(insetPadding),
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    val viewModel: MainViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.message) {
        state.message?.let {
            val result = snackbarHostState.showSnackbar(context.getString(it.textRes))
            if (result == SnackbarResult.Dismissed) {
                viewModel.onMessageDismissed()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
            text = stringResource(R.string.label_random_fact),
            style = MaterialTheme.typography.labelLarge,
        )
        TextCard(
            text = state.latestFact,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        )
        if (state.previousFacts.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 4.dp),
                text = stringResource(R.string.label_history),
                style = MaterialTheme.typography.labelLarge,
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
            ) {
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
    val swipeToDismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.Default) {
                false
            } else {
                onSwipe(text)
                true
            }
        }
    )

    SwipeToDismiss(
        modifier = modifier,
        state = swipeToDismissState,
        background = {
            val direction = swipeToDismissState.dismissDirection ?: return@SwipeToDismiss
            val backgroundColor by animateColorAsState(
                when (swipeToDismissState.targetValue) {
                    DismissValue.Default ->
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                    else -> MaterialTheme.colorScheme.errorContainer
                }
            )
            val iconColor by animateColorAsState(
                when (swipeToDismissState.targetValue) {
                    DismissValue.Default ->
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

                    else -> MaterialTheme.colorScheme.onErrorContainer
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val scale by animateFloatAsState(
                if (swipeToDismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor, CardDefaults.shape),
                contentAlignment = alignment,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = iconColor,
                    modifier = Modifier
                        .scale(scale)
                        .padding(horizontal = 16.dp),
                )
            }
        },
        dismissContent = { TextCard(text = text) },
    )
}

@Composable
fun TextCard(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Card(
        modifier = Modifier.thenIf(text.isBlank()) { shimmer() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(),
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

private val Message.textRes: Int
    @StringRes
    get() = when (this) {
        Message.FactDeleted -> R.string.message_fact_deleted
        Message.GeneralError -> R.string.message_general_error
    }

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PelagoCodingChallengeTheme {
        MainScreen(snackbarHostState = SnackbarHostState())
    }
}