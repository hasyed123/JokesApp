package com.example.jokesapp.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jokesapp.app.model.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jokesapp.app.model.Category
import com.example.jokesapp.app.model.Flags
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.lang.annotations.JdkConstants

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
        setContent {
            Screen()
        }
    }

}
@Composable
fun Screen() {
    val viewModel: MainViewModel = viewModel()
    val joke = viewModel.joke.observeAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        JokeContainer(modifier = Modifier.fillMaxHeight(.5f), joke.value?.setup, joke.value?.delivery, joke.value?.joke)
        CheckboxGroup()

        Button(
            onClick = { viewModel.tellMeAJoke()},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black
            )
        ) {
            Text("Generate new joke", color = Color.White)
        }
    }
}

@Composable
fun CheckboxGroup() {
    val viewModel: MainViewModel = viewModel()

    Box(
        modifier = Modifier
            .padding(10.dp)
            .width(170.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.DarkGray
                    )
                )
            )
            .padding(10.dp)
    ) {
        Column {
            MyCheckbox(viewModel.christmas.observeAsState().value)
            MyCheckbox(viewModel.dark.observeAsState().value)
            MyCheckbox(viewModel.misc.observeAsState().value)
            MyCheckbox(viewModel.programming.observeAsState().value)
            MyCheckbox(viewModel.pun.observeAsState().value)
            MyCheckbox(viewModel.spooky.observeAsState().value)
        }
    }

}

@Composable fun MyCheckbox(category: Category?) {
    val viewModel: MainViewModel = viewModel()
    category?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Text(
                text = it.name,
                fontSize = 16.sp,
                color = Color.White
            )
            Checkbox(
                checked = it.selected,
                onCheckedChange = { viewModel.updateCategory(category.name) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = Color.Black
                )
            )
        }
    }
}

@Composable
fun JokeContainer(modifier: Modifier, setup: String?, delivery: String?, joke: String?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(10.dp)
    ) {
        setup?.let{
            TextContainer(modifier = Modifier.weight(1f), it)
        }
        delivery?.let{
            TextContainer(modifier = Modifier.weight(1f), it)
        }
        joke?.let{
            TextContainer(modifier = Modifier.weight(1f), it)
        }
    }
}

@Composable
fun TextContainer(modifier: Modifier, text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.DarkGray
                    )
                )
            )
            .padding(5.dp)
            .wrapContentHeight()
    )
}

