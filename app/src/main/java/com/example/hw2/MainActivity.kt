@file:OptIn(InternalLandscapistApi::class)

package com.example.hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hw2.Retrofit.RetrofitInstance
import com.example.hw2.data.GiphyResponse
import com.skydoves.landscapist.InternalLandscapistApi
import kotlinx.coroutines.coroutineScope

const val API_KEY = "VSdGE1dpdOvi2oLxen5sjNdg52Pj7ZEb"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GiphyAPI()
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun GiphyAPI() {
    val list = remember { mutableStateListOf<String>() }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val fullGif = remember { mutableStateOf(false) }
    val selectedGifUrl = remember { mutableStateOf("") }

    LaunchedEffect(isError) {
        try {
            coroutineScope {
                val response: GiphyResponse = RetrofitInstance.api.getTrendigGifs(API_KEY, 10, 0)
                for (element in response.data) {
                    list.add(element.images.original.url)
                }
            }
        } catch (e: Exception) {
            isError = true
        } finally {
            isLoading = false
        }

    }
    if (isLoading) {
        Loading()
    }
    if (isError) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "No internet connection",
                fontSize = 32.sp,
                color = Color.Red,
            )
            Button(
                onClick = {
                    isError = false
                }
            ) { Text(text = "Try Again") }
        }
    }

    if(!fullGif.value){
        Column {

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content =
                {
                    items(list) {
                        GlideImage(url = it, fullGif, selectedGifUrl)
                    }
                })

        }
    }
    else {
        FullGif(selectedGifUrl.value)
    }



}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,

            )
    }

}

@Composable
fun GlideImage(url: String, fullGif: MutableState<Boolean>, selectedGifUrl: MutableState<String>) {

    var tryAgain by remember { mutableIntStateOf(0) }

    key(tryAgain) {
        Surface(
            modifier = Modifier.clickable(onClick = { fullGif.value = true; selectedGifUrl.value = url})
                .fillMaxSize()
                .padding(4.dp)
                .border(
                    1.5.dp,
                    Color.White,
                    RoundedCornerShape(12.dp)
                ),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 1.dp
        ) {
            com.skydoves.landscapist.glide.GlideImage(
                imageModel = { url },
                modifier = Modifier
                    .fillMaxSize(),

                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        run {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp),
                                color = Color.Black, // Индикатор контрастного цвета
                                strokeWidth = 4.dp
                            )
                        }
                    }
                },
                failure = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Button(modifier = Modifier.fillMaxSize(), onClick = { tryAgain++ }) {
                            Text(text = "reload")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FullGif(url: String){

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ){
        com.skydoves.landscapist.glide.GlideImage(
            imageModel = { url },
            modifier = Modifier
                .fillMaxSize(),

            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    run {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                            color = Color.Black, // Индикатор контрастного цвета
                            strokeWidth = 4.dp
                        )
                    }
                }
            },

            )
    }

}