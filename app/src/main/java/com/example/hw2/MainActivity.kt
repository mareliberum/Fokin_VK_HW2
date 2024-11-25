package com.example.hw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.hw2.Retrofit.RetrofitInstance
import com.example.hw2.data.GiphyResponse
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

    LaunchedEffect(isError) {
        try {
            coroutineScope {
                val response: GiphyResponse = RetrofitInstance.api.getTrendigGifs(API_KEY, 10, 0)
                for (element in response.data) {
                    list.add(element.images.original.url)
                }
            }
        }catch (e: Exception){
            isError = true
        }

    }

    if (isError){
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
               onClick ={
                   isError = false
               }
           ) { Text(text = "Try Again")}
        }
    }

    Column {

        LazyColumn() {
            items(list) {
                GlideImage(url = it)
            }

        }

    }
}

@Composable
fun Loading() {
//    var loading by remember { mutableStateOf(false) }
//
//    Button(onClick = { loading = true }, enabled = !loading) {
//        Text("Start loading")
//    }
//
//    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,

    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideImage(url : String){

    var isLoading by remember { mutableStateOf(true) }
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            color = MaterialTheme.colorScheme.onPrimary, // Индикатор контрастного цвета
            strokeWidth = 2.dp
        )
    }

    GlideImage(
        model = url,
        contentDescription = "gif",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize().padding(4.dp),
        loading = placeholder(R.drawable.placeholder),
        failure = placeholder(R.drawable.placeholder),
    )

}