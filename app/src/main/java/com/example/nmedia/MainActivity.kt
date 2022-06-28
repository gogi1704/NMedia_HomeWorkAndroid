package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textContent = findViewById<TextView>(R.id.text_content)
        textContent.text = "Обратите внимание на блок с количеством просмотров. С ним есть небольшая проблема (поскольку он расположен справа). Если количество просмотров вырастет, например, до 500, то есть целых два варианта:\n" +
                "\n" +
                "Отталкиваться от текста (т.е. установить фиксированное расстояние от текста до границы родителя, а саму иконку приклеить к границе текста)\n" +
                "Оставить достаточное количество места, чтобы уместилось и 500 и 1К, тогда на всех карточках положение этого блока будет одинаковым"
    }
}