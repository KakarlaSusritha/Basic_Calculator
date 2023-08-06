package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.calculator.databinding.ActivityMainBinding
import android.widget.Button
import android.content.ContentValues.TAG

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentInput: String = ""
    private var currentOperator: Operator? = null
    private var previousValue: Double = 0.0
    private var numberAfterOp = false
    enum class Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, PERCENTILE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View) {
        if(view is Button) {
            binding.inputText.append(view.text)
            val text = view.text.toString()
            if (text == "." && currentInput.contains(".")) {
                return
            }
            currentInput += text
            if (currentOperator != null) {
                numberAfterOp = true;
            }
        }
    }

    fun operatorAction(view: View) {
        if(view is Button) {
            if (currentOperator != null) {
                if (currentInput.isNotEmpty()) {
                    equals(view)
                } else {
                    return
                }
            }
            binding.inputText.append(view.text)
            when (view.text.toString()) {
                "*" -> currentOperator = Operator.MULTIPLY
                "+" -> currentOperator = Operator.ADD
                "-" -> currentOperator = Operator.SUBTRACT
                "/" -> currentOperator = Operator.DIVIDE
                "%" -> currentOperator = Operator.PERCENTILE
                else -> {}
            }
            previousValue = currentInput.toDouble()
            currentInput = ""
            numberAfterOp = false
        }
    }

    fun allClear(view: View) {
        currentInput = ""
        currentOperator = null
        previousValue = 0.0
        binding.inputText.text = ""
        updateResult()
    }

    fun backSpaceAction(view: View) {
        val length = binding.inputText.length()
        print(Log.d(TAG, "backSpaceAction: numberAfterOp $length"))
        if (binding.inputText.length() == 0) {return}
        if (currentOperator != null) {
            val lastEle = binding.inputText.text.last()

            if (lastEle == '*' || lastEle == '+' || lastEle == '/' || lastEle == '%' || lastEle == '-') {
                currentOperator = null
                binding.inputText.text = binding.inputText.text.substring(0, binding.inputText.text.length - 1)
                currentInput = previousValue.toString()
            } else {
                currentInput =  currentInput.substring(0, currentInput.length - 1)
                binding.inputText.text = binding.inputText.text.substring(0, binding.inputText.text.length - 1)
            }
        } else {
            currentInput =  currentInput.substring(0, currentInput.length - 1)
            binding.inputText.text = binding.inputText.text.substring(0, binding.inputText.text.length - 1)
        }
    }

    fun equals(view: View)  {
        if (currentOperator == Operator.DIVIDE && currentInput == "0") {
            currentInput = ""
            currentOperator = null
            previousValue = 0.0
            binding.result.text = "Error"
            binding.inputText.text = ""
        } else if (currentOperator != null && currentInput.isNotEmpty()) {
            val currentValue = currentInput.toDouble()
            val result = when (currentOperator) {
                Operator.ADD -> previousValue + currentValue
                Operator.SUBTRACT -> previousValue - currentValue
                Operator.MULTIPLY -> previousValue * currentValue
                Operator.DIVIDE -> previousValue / currentValue
                Operator.PERCENTILE -> previousValue % currentValue
                else -> 0.0
            }
            currentInput = result.toString()
            currentOperator = null
            updateResult()
        }
    }

    private fun updateResult() {
        val resultValue = currentInput.toDoubleOrNull()
        if (resultValue != null) {
            val formattedResult = if (resultValue.compareTo(resultValue.toInt()) == 0) {
                String.format("%d", resultValue.toLong())
            } else {
                String.format("%.2f", resultValue)
            }
            binding.result.text = formattedResult
            binding.inputText.text = formattedResult
        } else {
            binding.result.text = currentInput
            binding.inputText.text = currentInput
        }
    }
}