package com.example.todoapplication.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapplication.R
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TaskDetailItem(
    @StringRes labelResId: Int,
    taskDetailElement: String,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier){
        Text(text = stringResource(id = labelResId))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = taskDetailElement, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TaskCompletedCheckbox(
    @StringRes labelResId: Int,
    taskDetailElement: Boolean,
    onClickCheckbox: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = labelResId))
        Checkbox(checked = taskDetailElement, onCheckedChange = {onClickCheckbox(it)})
    }
}

@Composable
fun TaskProgressDisplayBox(
    @StringRes labelResId: Int,
    progress: Float,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(text = stringResource(id = labelResId))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .height(12.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
        )
        Text(text = stringResource(R.string.progress_value_unit, (progress * 100f).toInt().toString()) )
    }
}

@Composable
fun TaskDeadlineBox(
    @StringRes labelResId: Int,
    deadline: Long,
    modifier: Modifier = Modifier
){
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

    Column(modifier = modifier) {
        Text(text = stringResource(id = labelResId))
        Text(text = simpleDateFormat.format(Date(deadline)))
    }
}
