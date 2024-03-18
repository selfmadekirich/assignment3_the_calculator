package mmcs.assignment3_calculator.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import kotlin.math.abs
import kotlin.math.floor

class CalculatorViewModel: BaseObservable(), Calculator {
    override var display = ObservableField<String>()
    init{
        display.set("0")
    }

    private var current = "0"
    private var lastOp = ""
    private var precalc = ""
    private var last_was_sign = false

    override fun addDigit(dig: Int) {
        if(current == "0"){
            current = ""
        }
        last_was_sign = false
        current += dig.toString();
        display.set(precalc + current)
    }

    override fun addPoint() {
        if(!current.contains('.'))
            current += '.'
        display.set(precalc + current)
    }

    private fun getOpStr(op: Operation):String{
        when(op){
            Operation.ADD -> return "+"
            Operation.DIV -> return "/"
            Operation.MUL -> return "*"
            Operation.SUB -> return "-"
        }
        return ""
    }

    override fun addOperation(op: Operation) {
        if(lastOp != "" || last_was_sign)
            return

        if(current == "0"){
               current = "-"
               display.set(precalc + current)
               return
           }
        last_was_sign = true

        lastOp = getOpStr(op)
        precalc += current + lastOp
        current = ""
        display.set(precalc)

    }

    private fun calc():Double{
        var prc =  precalc.toDouble();
        var cur = current.toDouble();

        when(lastOp){
            "+" -> return prc + cur
            "/" -> if(cur == 0.0) return 0.0 else return prc / cur
            "*" -> return prc * cur
            "-" -> return prc - cur
        }
        return 0.0
    }
    override fun compute() {
        if(lastOp == "" || current == "0" || current == "")
            return
        current = precalc + current
        var temp = current.split(lastOp)
        precalc = temp[0]
        current = temp[1]

        var temp_f  = calc()
        precalc = if(abs(temp_f - floor(temp_f)) > 0)  temp_f.toString() else temp_f.toInt().toString()
        current = ""
        lastOp = ""
        last_was_sign = false
        display.set(precalc)
    }

    override fun clear() {
        current = "0"
        display.set(current)
    }

    override fun reset() {
        current = "0"
        lastOp = ""
        precalc = ""
        display.set(current)
    }
}