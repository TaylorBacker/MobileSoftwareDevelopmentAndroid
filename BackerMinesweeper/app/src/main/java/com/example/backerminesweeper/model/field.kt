package com.example.backerminesweeper.model

data class Field(var type: Int, var minesNear: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)