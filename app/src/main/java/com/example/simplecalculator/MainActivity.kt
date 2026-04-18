package com.example.simplecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentValue = "0"
    private var storedValue = 0.0
    private var pendingOperator: String? = null
    private var shouldResetInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperatorButtons()
        updateDisplay()
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )

        numberButtons.forEach { button ->
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }

        binding.btnDot.setOnClickListener {
            if (shouldResetInput) {
                currentValue = "0"
                shouldResetInput = false
            }

            if (!currentValue.contains(".")) {
                currentValue += "."
                updateDisplay()
            }
        }
    }

    private fun setupOperatorButtons() {
        binding.btnClear.setOnClickListener {
            currentValue = "0"
            storedValue = 0.0
            pendingOperator = null
            shouldResetInput = false
            updateDisplay()
        }

        binding.btnAdd.setOnClickListener { applyOperator("+") }
        binding.btnSubtract.setOnClickListener { applyOperator("-") }
        binding.btnMultiply.setOnClickListener { applyOperator("*") }
        binding.btnDivide.setOnClickListener { applyOperator("/") }

        binding.btnEqual.setOnClickListener {
            calculateResult()
            pendingOperator = null
            shouldResetInput = true
        }
    }

    private fun appendNumber(number: String) {
        if (shouldResetInput) {
            currentValue = "0"
            shouldResetInput = false
        }

        currentValue = if (currentValue == "0") number else currentValue + number
        updateDisplay()
    }

    private fun applyOperator(operator: String) {
        if (pendingOperator != null && !shouldResetInput) {
            calculateResult()
        } else {
            storedValue = currentValue.toDoubleOrNull() ?: 0.0
        }

        pendingOperator = operator
        shouldResetInput = true
    }

    private fun calculateResult() {
        val currentNumber = currentValue.toDoubleOrNull() ?: 0.0

        val result = when (pendingOperator) {
            "+" -> storedValue + currentNumber
            "-" -> storedValue - currentNumber
            "*" -> storedValue * currentNumber
            "/" -> if (currentNumber == 0.0) 0.0 else storedValue / currentNumber
            else -> currentNumber
        }

        storedValue = result
        currentValue = formatResult(result)
        updateDisplay()
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    private fun updateDisplay() {
        binding.tvDisplay.text = currentValue
    }
}
