package com.example.shopping.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopping.R
import java.io.Serializable


@Entity(tableName = "todotable")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "todopriority") var priority: String, // Change the type to String
    @ColumnInfo(name = "isdone") var isDone: Boolean
) : Serializable {

    fun getIcon(): Int {
        return when (priority) {
            "Dairy" -> {
                R.drawable.dairy
            }
            "Meat" -> {
                R.drawable.meat
            }
            else -> {
                R.drawable.produce
            }
        }
    }
}
