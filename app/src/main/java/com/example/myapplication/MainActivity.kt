package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val salesmanViewModel = ViewModelProvider(this)[SalesmanViewModel::class.java]
        setContent {
            SalesmanApp(salesmanViewModel)
        }
    }
}

@Composable
@Preview
fun SalesmanListPreview() {
    val salesmen = listOf(
        Salesman("Artem Titarenko", listOf("76133")),
        Salesman("Bernd Schmitt", listOf("7619*")),
        Salesman("Chris Krapp", listOf("762*")),
        Salesman("Alex Uber", listOf("86*"))
    )

    SalesmanList(salesmen)
}

@Composable
@Preview
fun SalesmanAppPreview() {
    val salesmanViewModel = SalesmanViewModel()
    SalesmanApp(salesmanViewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesmanApp(salesmanViewModel: SalesmanViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                salesmanViewModel.setSearchQuery(query)
            },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Filled.Send, contentDescription = null) },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Display error message if it exists
        val errorMessage = salesmanViewModel.getErrorMessage().value
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        SalesmanList(salesmanViewModel.filteredSalesmen.collectAsState(initial = emptyList()).value)
    }
}


@Composable
fun SalesmanList(salesmen: List<Salesman>) {
    LazyColumn {
        items(salesmen) { salesman ->
            SalesmanItem(salesman)
        }
    }
}

@Composable
fun SalesmanItem(salesman: Salesman) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color = colorResource(id = R.color.grey_400), shape = CircleShape)
            ) {
                Text(
                    text = salesman.name.first().toString(),
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = salesman.name,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 16.sp
                )
                if (isExpanded) {

                    salesman.areas.forEach { area ->
                        Text(
                            text = area,
                            color = colorResource(id = R.color.grey_400),
                            fontSize = 14.sp

                        )
                    }
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }



        Divider()
    }


}