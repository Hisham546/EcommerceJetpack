package com.example.ecommerce_jetpack

import ApiService
import DataModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce_jetpack.ui.theme.EcommerceJetpackTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceJetpackTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

val retrofit = ApiServiceBuilder.retrofit


@Composable
fun MainScreen(){
    var search by remember {
        mutableStateOf("")
    }
    var responseData by remember { mutableStateOf<List<DataModel>?>(null) }
    // Loading state
    var loading by remember { mutableStateOf(true) }

    // Error state
    var error by remember { mutableStateOf(false) }

    // LaunchedEffect to trigger the API call
    LaunchedEffect(true) {
        val result = runCatching {
            // Show loading indicator while fetching data
            loading = true

            // Make the API call
            val response = retrofit.create(ApiService::class.java).fetchData()
            responseData = response
            // Process the response as needed
            // For example, update your UI with the fetched data

            // Update loading state
            loading = false
        }

        result.onFailure { exception ->
            // Handle API call failure
            loading = false
            error = true
            Log.e("API_CALL_ERROR", "Failed to fetch data", exception)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()


    ) {
        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(120.dp),
            ) {
            Text(text = "Discover your Products", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
                horizontalArrangement = Arrangement.Center, //to center align content in a row
                verticalAlignment = Alignment.CenterVertically

            ) {

             OutlinedTextField(value = search, onValueChange = { text -> search = text },
                 modifier = Modifier
                     .width(250.dp)

                   )
                Spacer(modifier = Modifier.width(5.dp))
               Button(onClick = { /*TODO*/ },
               modifier = Modifier
                   .height(40.dp)
                   .width(150.dp),
                   colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)

                   ) {
                   Text(text = "Search",
                       color=Color.White,
                       fontWeight = FontWeight.Bold,
                       fontSize = 10.sp
                       )
               }
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()

        ) {

            responseData?.let { dataList ->
                for (data in dataList) {
                    Text(
                        text = buildString {
                            append("ID: ${data.id}\n")
                            append("Title: ${data.title}\n")
                            append("Price: ${data.price}\n")
                            append("Category: ${data.category}\n")
                            append("Description: ${data.description}\n")
                            append("Image: ${data.image}\n")
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }
            // Loading indicator
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            // Error message
            if (error) {
                Text(
                    text = "Failed to fetch data. Please try again.",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        }
    }


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EcommerceJetpackTheme {
        MainScreen()
    }
}