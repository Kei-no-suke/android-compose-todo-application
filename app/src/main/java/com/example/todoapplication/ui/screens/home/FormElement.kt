package com.example.todoapplication.ui.screens.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapplication.R
import java.text.SimpleDateFormat
import java.util.Date

// ラベル付きテキスト表示
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

// ラベル付きチェックボックス
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

// ラベル、進捗バー付きテキスト表示
@Composable
fun TaskProgressDisplayBox(
    @StringRes labelResId: Int,
    progress: Float,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(text = stringResource(id = labelResId))
        TaskProgressBar(progress)
        Text(text = stringResource(R.string.progress_value_unit, (progress * 100f).toInt().toString()) )
    }
}

// プログレスバー単体
@Composable
fun TaskProgressBar(
    progress: Float
){
    LinearProgressIndicator(
        progress = progress,
        modifier = Modifier
            .height(12.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface)
    )
}

// 進捗テキストボックス
@Composable
fun TaskProgressTextBox(
    progress: Float
){
    Text(
        text = stringResource(R.string.progress_text_box, (progress * 100f).toInt().toString())
    )
}

// 締め切り日テキストボックス
@Composable
fun DeadlineTextBox(
    deadline: Long,
    modifier: Modifier = Modifier
){
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.deadline_text_box, simpleDateFormat.format(Date(deadline)))
        )
    }
}

// ラベル付き、締め切り日テキスト表示
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



// テキストフィールド
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldArea(
    @StringRes labelResId: Int,
    inputFieldText: String,
    onValueChange: (String) -> Unit,
    isDisplayUnit: Boolean = false,
    unitText: String = "",
    modifier: Modifier = Modifier
){
    Row(modifier = modifier){
        Text(
            text = stringResource(id = labelResId),
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = inputFieldText,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )
        if(isDisplayUnit){
            Text(
                text = unitText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// デイトピッカー
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogArea(
    @StringRes labelResId: Int,
    inputFieldText: Long?,
    isDisplay: Boolean,
    onClickConfirmButton: (Long?) -> Unit,
    onDismissRequest: () -> Unit,
    onCalenderIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = inputFieldText
    )

    val isTextFieldFocused = remember {
        mutableStateOf(false)
    }

    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    var inputFieldFormattedText: String
    try{
        inputFieldFormattedText = if(inputFieldText == null){ "" } else {
            simpleDateFormat.format(Date(datePickerState.selectedDateMillis!!))
        }
    }catch(e: Exception){
        Log.d("DatepickerError", e.toString())
        inputFieldFormattedText = ""
    }

    Row(modifier = modifier){
        Text(text = stringResource(id = labelResId))
        TextField(
            value = inputFieldFormattedText,
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = onCalenderIconClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.onFocusChanged { FocusState ->
                isTextFieldFocused.value = FocusState.isFocused
                if(isTextFieldFocused.value){
                    onCalenderIconClick()
                }
            }
        )
        if(isDisplay){
            DatePickerDialog(
                onDismissRequest = onDismissRequest,
                confirmButton = {
                    ConfirmButtonInDatePickerDialog(
                        deadline = datePickerState.selectedDateMillis,
                        onClick = { deadline -> onClickConfirmButton(deadline) }
                    )
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

// デイトピッカーダイアログ用確認ボタン
@Composable
fun ConfirmButtonInDatePickerDialog(
    deadline: Long?,
    onClick: (Long?) -> Unit
){
    Button(onClick = { onClick(deadline) }) {
        Text(text = stringResource(id = R.string.confirm_button_text))
    }
}

// タスク簡易表示詳細表示切り替え機能付きボックス
@Composable
fun VariableDisplayModeTaskCard(
    homeUiState: HomeUiState,
    onArchiveButtonClick: (Int) -> Unit,
    onClickCheckbox: (Boolean, Int) -> Unit
){
    val nameTextBoxWeight = if(homeUiState.taskDetail.isArchived){ 3f } else { 4f }
    val isDisplayDetail = rememberSaveable(){
        mutableStateOf(false)
    }
    Card {
        Row() {
            Checkbox(
                checked = homeUiState.taskDetail.isCompleted,
                onCheckedChange = {flag -> onClickCheckbox(flag, homeUiState.taskDetail.id)},
                modifier = Modifier.weight(1f)
            )
            Text(
                text = homeUiState.taskDetail.name,
                modifier = Modifier.weight(nameTextBoxWeight)
            )
            if(homeUiState.taskDetail.isCompleted){
                Column(modifier = Modifier.clickable(){
                    onArchiveButtonClick(homeUiState.taskDetail.id)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_archive_24),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.archived_list),
                        fontSize = 10.sp
                    )
                }
            }
            IconButton(onClick = { isDisplayDetail.value = !isDisplayDetail.value }) {
                Icon(
                    painter = painterResource(
                        id =
                        if(isDisplayDetail.value)
                        {R.drawable.baseline_expand_less_24}
                        else
                        {R.drawable.baseline_expand_more_24}
                    ),
                    contentDescription = null
                )
            }
        }
        if(isDisplayDetail.value){
            Divider(thickness = 2.dp)
            val progress = homeUiState.taskDetail.progress.toFloat() / 100f
            TaskProgressBar(progress = progress)
            Row(){
                TaskProgressTextBox(progress = progress)
                if(homeUiState.taskDetail.deadline != null){
                    DeadlineTextBox(deadline = homeUiState.taskDetail.deadline)
                }
            }
        }
    }
}

// タスクカードリスト
@Composable
fun TaskCardList(
    modifier: Modifier = Modifier,
    homeUiStateList: List<HomeUiState>,
    onClickCheckbox: (Boolean, Int) -> Unit,
    onArchiveButtonClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier){
        items(homeUiStateList.size){
            VariableDisplayModeTaskCard(
                homeUiState = homeUiStateList[it],
                onArchiveButtonClick = { id -> onArchiveButtonClick(id) },
                onClickCheckbox = {flag, id -> onClickCheckbox(flag, id)}
            )
        }
    }
}
