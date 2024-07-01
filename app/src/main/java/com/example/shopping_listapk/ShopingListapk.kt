package com.example.shopping_listapk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItems(
    var name: String,
    var id: Int,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingList() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItems>()) }
    var showDialog by remember { mutableStateOf(value = false) }
    var itemName by remember { mutableStateOf(value = "") }
    var itemQuantity by remember { mutableStateOf(value = "") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,

    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 55.dp)

        ) {
            Text(text = "Add Item", fontSize = 22.sp)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(sItems, key = { it.id }) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                        sItems = sItems.map {
                            if (it.id == item.id) it.copy(name = editedName, quantity = editedQuantity, isEditing = false)
                            else it
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                    }, onDeleteClick = {
                        sItems = sItems.filter { it.id != item.id }
                    })
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            modifier = Modifier.alpha(0.8f),
            containerColor = Color(0xFFa2f07d),
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItems(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toIntOrNull() ?: 1
                            )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Yellow,
                        containerColor = Color.Black
                    )) {
                        Text(text = "Add")
                    }
                    Button(onClick = { showDialog = false }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Yellow,
                        containerColor = Color.Black
                    )) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = { Text("Add Shopping items", color = Color.Black) },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        placeholder = { Text("Enter item name") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        placeholder = { Text("Enter item quantity") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItems,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp), fontSize = 22.sp)
        Text(text = "Qty:${item.quantity}", modifier = Modifier.padding(8.dp),fontSize = 22.sp, )
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(color = Color(0xFFbcf2a2))
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(color = Color(0xFFbcf2a2))
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItems,
    onEditComplete: (String, Int) -> Unit
) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(18.dp)
                .background(Color.Transparent)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12)),

            verticalArrangement = Arrangement.spacedBy(8.dp),
            Alignment.CenterHorizontally


        ) {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle = TextStyle(fontSize = 28.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp,15.dp,8.dp,8.dp)

            )
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(8.dp,0.dp,8.dp,0.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle = TextStyle(fontSize = 28.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(8.dp,0.dp,8.dp,0.dp)
            )

            Button(
                onClick = {
                    onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                }, modifier = Modifier.padding(8.dp)
//            modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Save", fontSize = 22.sp, modifier = Modifier.padding(8.dp))
            }
        }

}
